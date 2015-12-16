using System;
using System.IO;
using System.Collections.Generic;
using System.Threading.Tasks;

using Semantria.Com.CSV;
using Semantria.Com.Workers;

namespace SampleApp
{
    public class Program
    {
        static string apiKey = "";
        static string apiSecret = "";
        static string configId = "";

        static int threadsCount = 2;

        static TextWriter logWritter = null;
        static CsvFileWriter resWritter = null;

        static object logSyncObject = new object();
        static object resSyncObject = new object();

        static Dictionary<string, DocumentMetrics> resultsTracker = new Dictionary<string, DocumentMetrics>();

        public static void Main(string[] args)
        {
            string sourcePath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "sample_tweets.tsv");
            string logPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "analysis.log");
            string resPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "sentiment.tsv");

            resWritter = new CsvFileWriter(resPath);
            resWritter.Delimiter = '\t';
            resWritter.WriteRow(new List<string>()
            {
                "Id",
                "Sentiment score",
                "Sentiment Polarity"
            });

            logWritter = new StreamWriter(File.OpenWrite(logPath));

            CSVFileDataProvider provider = new CSVFileDataProvider(sourcePath)
            {
                Delimiter = '\t',
                IdColumnIndex = 0,
                TextColumnIndex = 6
            };

            WorkersBuilder builder = new WorkersBuilder(provider);
            builder.SemantriaAPIkey = apiKey;
            builder.SemantriaAPIsecret = apiSecret;
            builder.Configuration = configId;

            for (int index = 0; index < threadsCount; index++)
            {
                IWorker worker = builder.CreateSemantriaWorker();
                worker.ExecutedMethodCallback = MethodExecuted;
                worker.ErrorOccurredCallback = ErrorOccurred;
                worker.RecordQueuedCallback = RecordQueued;
                worker.OutputReceivedCallback = OutputReceived;
                worker.Initialize();

                Task task = new Task(() => worker.Process());
                task.Start();
            }

            Console.ReadLine();
        }

        //Callback method to log requests execution metrics.
        static void MethodExecuted(object sender, RequestMetrics metrics)
        {
            SemantriaWorker worker = sender as SemantriaWorker;

            if (metrics.ExecutedMethod == SemantriaAPImethod.QueueBatch)
            {
                int batchSize = (int)metrics.State;
                WriteLogLine("Worker {0}: A batch of {1} documents has been queued in {2:0.##} seconds.", worker.WorkerId, batchSize, metrics.ExecutionTime.TotalSeconds);
            }
            else if (metrics.ExecutedMethod == SemantriaAPImethod.RetrieveResultsByJobId)
            {
                int batchSize = (int)metrics.State;
                WriteLogLine("Worker {0}: A batch of {1} documents has been retrieved in {2:0.##} seconds.", worker.WorkerId, batchSize, metrics.ExecutionTime.TotalSeconds);
            }
        }

        //Callback method for occurred errors.
        static void ErrorOccurred(object sender, Exception ex)
        {
            SemantriaWorker worker = sender as SemantriaWorker;
            WriteLogLine("Worker {0}: ERROR: {1}", worker.WorkerId, ex.Message);
        }

        static void WriteLogLine(string format, params object[] args)
        {
            if (string.IsNullOrEmpty(format))
            {
                return;
            }

            string logEntry = string.Format(format, args);
            Console.WriteLine(logEntry);

            lock (logSyncObject)
            {
                logWritter.WriteLine(logEntry);
                logWritter.Flush();
            }
        }

        //Callback method for analysis output.
        static void RecordQueued(object sender, dynamic record, DocumentMetrics metrics)
        {
            if (record == null)
            {
                return;
            }

            lock (resSyncObject)
            {
                if (!resultsTracker.ContainsKey(record.id))
                {
                    resultsTracker.Add(record.id, metrics);
                }
            }
        }

        //Callback method for analysis output.
        static void OutputReceived(object sender, dynamic output, DocumentMetrics metrics)
        {
            if (output == null)
            {
                return;
            }

            lock (resSyncObject)
            {
                if (!resultsTracker.ContainsKey(output.id))
                {
                    return;
                }
                else
                {
                    resultsTracker[output.id] = metrics;
                }

                resWritter.WriteRow(new List<string>()
                {
                    output.id,
                    string.Format("{0:0.##}", output.sentiment_score),
                    output.sentiment_polarity
                });
            }
        }
    }
}
