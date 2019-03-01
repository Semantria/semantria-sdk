package main

import (
	"bufio"
	"encoding/json"
	"errors"
	"fmt"
	"os"
	"os/signal"
	"path/filepath"
	"strings"
	"syscall"
	"time"

	"github.com/dustin/go-humanize"

	docopt "github.com/docopt/docopt-go"
	"github.com/rs/zerolog"
	"github.com/rs/zerolog/log"
	s "github.com/semantria/sem4"
)

func dump(data interface{}) int {
	b, err := json.Marshal(data)
	if err != nil {
		log.Fatal().Err(err).Msg("Unable to unmarshall")
	}
	str := string(b)
	fmt.Println(str)
	return len(str)
}

func main() {
	usage := `Semantria SDK CLI

Usage:
    sem4cli [options] status
    sem4cli [options] configurations
    sem4cli [options] send <file>...
    sem4cli [options] poll [forever]
    sem4cli -h | --help
    sem4cli --version

Options:
    -h --help           Show this screen
    --version           Show version
    -v --verbose        Turn on verbose logging
    --key KEY           Consumer key (defaults to $SEMANTRIA_KEY env variable)
    --secret SECRET     Consumer secret (defaults to $SEMANTRIA_SECRET env variable)
    --jobid ID          Job id to use
    --configid ID       Configuration id to use
    --id-path S         Path to find the id field [default: id]
    --text-path S       Path to find the text field [default: text]

Commands:

    status
        Get Semantria including service availability

    configurations
        Get available configurations

    send
        Send documents from one or more files.  File handling is driven by file extension:

            .json files expect one json document per line
            otherwise are expected to be text files with one document per line

        If supported by the converter the --id-path and --text-path can be used to override
        how to find the id and text field

    poll
        Poll documents from Semantria on a specified job or config id.  If the 'forever'
        keyword is not given then polling will stop when no documents are found.
    `
	arguments, _ := docopt.ParseArgs(usage, os.Args[1:], s.WrapperVersion)

	log.Logger = log.Output(zerolog.ConsoleWriter{Out: os.Stderr})
	zerolog.SetGlobalLevel(zerolog.InfoLevel)
	if ok, _ := arguments.Bool("--verbose"); ok {
		zerolog.SetGlobalLevel(zerolog.DebugLevel)
	}

	consumerKey, _ := arguments.String("--key")
	if consumerKey == "" {
		consumerKey = os.Getenv("SEMANTRIA_KEY")
	}
	consumerSecret, _ := arguments.String("--secret")
	if consumerSecret == "" {
		consumerSecret = os.Getenv("SEMANTRIA_SECRET")
	}
	session, err := s.MakeSession(s.SessionOptions{
		ConsumerKey:     consumerKey,
		ConsumerSecret:  consumerSecret,
		ApplicationName: "cli",
	})
	if err != nil {
		log.Fatal().Err(err).Msg("Unable to MakeSession")
	}

	if ok, _ := arguments.Bool("status"); ok {
		log.Info().Msg("Getting status")
		data, err2 := session.GetStatus()
		if err2 != nil {
			log.Fatal().Err(err2).Msg("Unable to GetStatus")
		}
		dump(data)
	} else if ok, _ := arguments.Bool("configurations"); ok {
		log.Info().Msg("Getting configurations")
		data, err2 := session.GetConfigurations()
		if err2 != nil {
			log.Fatal().Err(err2).Msg("Unable to GetConfigurations")
		}
		dump(data)
	} else if ok, _ := arguments.Bool("poll"); ok {
		configID, _ := arguments.String("--configid")
		jobID, _ := arguments.String("--jobid")
		pollForever, _ := arguments.Bool("forever")
		log.Info().
			Str("configID", configID).
			Str("jobID", jobID).
			Msg("Polling")
		err = pollAvailableDocs(session, configID, jobID, pollForever)
		if err != nil {
			log.Fatal().Err(err).Msg("Ran into a problem")
		}
	} else if ok, _ := arguments.Bool("send"); ok {
		v, _ := arguments["<file>"]
		fileNames, _ := v.([]string)
		jobID, _ := arguments.String("--jobid")
		idPath, _ := arguments.String("--id-path")
		textPath, _ := arguments.String("--text-path")
		configID, _ := arguments.String("--configid")
		log.Info().
			Str("fileNames", strings.Join(fileNames, ",")).
			Str("configID", configID).
			Str("jobID", jobID).
			Msg("Sending")
		err = sendDocsFromFile(session, fileNames, configID, jobID, idPath, textPath)
		if err != nil {
			log.Fatal().Err(err).Msg("Ran into a problem")
		}
	} else {
		log.Fatal().Msg("ERROR: unhandled command")
	}

}

func pollAvailableDocs(session *s.Session,
	configID string,
	jobID string,
	pollForever bool) error {
	if session == nil {
		return errors.New("pollAvailableDocs requires a session")
	}

	sigs := make(chan os.Signal, 1)
	done := false
	signal.Notify(sigs, syscall.SIGINT, syscall.SIGTERM)

	// goroutine to wait for the signal
	go func() {
		<-sigs
		done = true
	}()

	start := time.Now()
	totalDocs := 0
	totalBytes := uint64(0)
	for {
		var docs []interface{}
		var err error
		if jobID != "" {
			docs, err = session.GetProcessedDocumentsByJobID(jobID)
		} else {
			docs, err = session.GetProcessedDocumentsByConfigID(configID)
		}
		if err != nil {
			return err
		}
		if docs == nil {
			if done {
				log.Info().Msg("Interrupted. Stopping")
				break
			}
			if !pollForever {
				log.Info().Msg("No docs found")
				break
			}
			time.Sleep(time.Second)
			continue
		}
		log.Info().Int("docs", len(docs)).Msg("Found docs")
		totalDocs += len(docs)
		for _, doc := range docs {
			totalBytes += uint64(dump(doc))
		}
	}
	if totalDocs > 0 {
		elapsed := time.Since(start)
		speedDocs := float64(totalDocs) / elapsed.Seconds()
		speedBytes := float64(totalBytes) / elapsed.Seconds()

		log.Info().
			Int("docs", totalDocs).
			Str("data", humanize.Bytes(totalBytes)).
			Str("rate(docs)", humanize.FormatFloat("#.##", speedDocs)+" docs/sec").
			Str("rate(data)", humanize.Bytes(uint64(speedBytes))+"/sec").
			Str("elapsed", strings.ReplaceAll(humanize.Time(start), " ago", "")).
			Bool("interrupted", done).
			Msg("Finished polling")
	}
	return nil
}

func sendDocsFromFile(session *s.Session,
	fileNames []string,
	configID string,
	jobID string,
	idPath string,
	textPath string) error {
	if session == nil {
		return errors.New("sendDocsFromFile requires a session")
	}

	sigs := make(chan os.Signal, 1)
	done := false
	signal.Notify(sigs, syscall.SIGINT, syscall.SIGTERM)

	// goroutine to wait for the signal
	go func() {
		<-sigs
		done = true
	}()

	batchSize := 100
	batch := make([]s.Document, 0, batchSize)
	totalDocs := 0
	totalBytes := uint64(0)
	start := time.Now()
	for _, fileName := range fileNames {
		log.Info().
			Str("fileName", fileName).
			Msg("Starting")

		c := newConverter(filepath.Ext(fileName), idPath, textPath)
		file, err := os.Open(fileName)
		if err != nil {
			return err
		}
		fi, _ := file.Stat()
		totalBytes += uint64(fi.Size())
		defer file.Close()

		scanner := bufio.NewScanner(file)
		for scanner.Scan() {
			doc, cErr := c.Convert(scanner.Text())
			if cErr != nil {
				log.Info().Err(cErr).Msg("Failed to convert. Skipping")
				continue
			}
			batch = append(batch, *doc)
			if len(batch) == batchSize {
				log.Info().Int("docs", len(batch)).Msg("Sending batch")
				_, err = session.QueueBatch(batch, configID, jobID)
				if err != nil {
					return err
				}
				totalDocs += len(batch)
				batch = batch[:0]
			}

			if done {
				break
			}
		}
		if len(batch) > 0 {
			log.Info().Int("docs", len(batch)).Msg("Sending final batch")
			_, err = session.QueueBatch(batch, configID, jobID)
			if err != nil {
				return err
			}
			totalDocs += len(batch)
			batch = batch[:0]
		}

		if err := scanner.Err(); err != nil {
			return err
		}

		if done {
			log.Info().Msg("Interrupted. Stopping")
			break
		}
	}

	elapsed := time.Since(start)
	speedDocs := float64(totalDocs) / elapsed.Seconds()
	speedBytes := float64(totalBytes) / elapsed.Seconds()

	log.Info().
		Int("docs", totalDocs).
		Str("data", humanize.Bytes(totalBytes)).
		Str("rate(docs)", humanize.FormatFloat("#.##", speedDocs)+" docs/sec").
		Str("rate(data)", humanize.Bytes(uint64(speedBytes))+"/sec").
		Str("elapsed", strings.ReplaceAll(humanize.Time(start), " ago", "")).
		Bool("interrupted", done).
		Msg("Finished sending")

	return nil
}
