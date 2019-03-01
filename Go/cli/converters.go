package main

import (
	"errors"

	"github.com/Jeffail/gabs"
	"github.com/google/uuid"
	"github.com/rs/zerolog/log"
	s "github.com/semantria/sem4"
)

type converter interface {
	Convert(data string) (*s.Document, error)
}

type textRecord struct {
}

func (tr textRecord) Convert(str string) (*s.Document, error) {
	docID := uuid.New().String()
	doc := s.Document{ID: docID, Text: str}
	return &doc, nil
}

type jsonRecord struct {
	idPath   string
	textPath string
}

func (tr jsonRecord) Convert(str string) (*s.Document, error) {
	jsonParsed, err := gabs.ParseJSON([]byte(str))
	if err != nil {
		return nil, err
	}
	idPath := tr.idPath
	if idPath == "" {
		idPath = "id"
	}
	textPath := tr.textPath
	if textPath == "" {
		textPath = "text"
	}
	var docID string
	var text string
	var ok bool
	docID, ok = jsonParsed.Path(idPath).Data().(string)
	if !ok {
		return nil, errors.New("Failed to find id at " + idPath)
	}
	text, ok = jsonParsed.Path(textPath).Data().(string)
	if !ok {
		return nil, errors.New("Failed to find text at " + textPath)
	}
	doc := s.Document{ID: docID, Text: text}
	return &doc, nil
}

func newConverter(format string, idPath string, textPath string) converter {
	if format == ".json" {
		log.Debug().Msg("Using the json converter")
		return jsonRecord{idPath: idPath, textPath: textPath}
	}
	log.Debug().Str("format", format).Msg("Defaulting to text converter")
	return textRecord{}
}
