using System;
using System.IO;
using System.Collections.Generic;

using Semantria.Com;
using Semantria.Com.Serializers;
using Semantria.Com.Mapping;
using Semantria.Com.Mapping.Output;

using System.Linq;
using Semantria.Com.Mapping.Configuration;

namespace JobIdFeatureTestApp
{
    class Program
    {
        static void Main(string[] args)
        {
            // Use correct Semantria API credentias here
            string consumerKey = System.Environment.GetEnvironmentVariable("SEMANTRIA_KEY");
            string consumerSecret = System.Environment.GetEnvironmentVariable("SEMANTRIA_SECRET");

            // null - send every single document separately
            // false - send uniqueJobIdCount batches
            // true - send all documents in single batch
            bool? dataSendingMode = true;
            int uniqueJobIdCount = 4;

            Dictionary<string, int> jobIds = new Dictionary<string, int>(uniqueJobIdCount);
            Dictionary<string, List<Document>> documents = new Dictionary<string, List<Document>>();

            Console.WriteLine("Semantria JobId feature demo.");

            string path = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "source.txt");
            if (!File.Exists(path))
            {
                Console.WriteLine("Source file isn't available.");
                return;
            }

            //Generates N unique jobId values
            for (int index = 0; index < uniqueJobIdCount; index++)
            {
                string id = Guid.NewGuid().ToString();

                jobIds.Add(id, 0);
                documents.Add(id, new List<Document>());
            }

            //Reads documents from the source file
            Console.WriteLine("Reading documents from file...");
            Console.WriteLine();
            using (StreamReader stream = new StreamReader(path))
            {
                Random rnd = new Random();

                while (!stream.EndOfStream)
                {
                    string line = stream.ReadLine();
                    if (string.IsNullOrEmpty(line) || line.Length < 3)
                        continue;

                    string jobId = jobIds.ElementAt(rnd.Next(uniqueJobIdCount)).Key;
                    jobIds[jobId]++;

                    Document doc = new Document()
                    {
                        Id = Guid.NewGuid().ToString(),
                        Text = line,
                        JobId = jobId
                    };

                    documents[jobId].Add(doc);
                }
            }

            // Creates JSON serializer instance
			ISerializer serializer = new JsonSerializer();

            // Initializes new session with the serializer object and the keys.
            using (Session session = Session.CreateSession(consumerKey, consumerSecret, serializer))
            {
                // Error callback handler. This event will occur in case of server-side error
                session.Error += new Session.ErrorHandler(delegate(object sender, ResponseErrorEventArgs ea)
                {
                    if ((int)ea.Status < 500)
                    {
                        Console.WriteLine(string.Format("{0}: {1}", (int)ea.Status, ea.Message));
                    }
                    else
                    {
                        Console.WriteLine(string.Format("{0}: {1}", (int)ea.Status, "Unhandled server error happened."));
                    }
                });

                if (dataSendingMode == null)
                {
                    foreach (KeyValuePair<string, List<Document>> pair in documents)
                    {
                        foreach (Document doc in pair.Value)
                        {
                            // Queues document for processing on Semantria service
                            session.QueueDocument(doc);
                        }

                        Console.WriteLine("{0} documents queued for {1} job ID", pair.Value.Count, pair.Key);
                    }
                }
                else if (!dataSendingMode.Value)
                {
                    foreach (KeyValuePair<string, List<Document>> pair in documents)
                    {
                        // Queues batch of documents for processing on Semantria service
                        if (session.QueueBatchOfDocuments(pair.Value) != -1)
                        {
                            Console.WriteLine("{0} documents queued for {1} job ID", pair.Value.Count, pair.Key);
                        }
                    }
                }
                else
                {
                    List<Document> aBatch = new List<Document>();

                    foreach (KeyValuePair<string, List<Document>> pair in documents)
                    {
                        aBatch.AddRange(pair.Value);
                    }

                    if (session.QueueBatchOfDocuments(aBatch) != -1)
                    {
                        Console.WriteLine("{0} documents queued in single batch", aBatch.Count);
                    }
                }

                Console.WriteLine();
                Dictionary<string, int> aCopy = new Dictionary<string, int>(jobIds);
                foreach (KeyValuePair<string, int> pair in aCopy)
                {
                    int count = 0;
                    while (jobIds[pair.Key] > 0)
                    {
                        // Waits half of second while Semantria process queued document
                        System.Threading.Thread.Sleep(1000);

                        // Requests processed results from Semantria service
                        List<DocAnalyticData> results = new List<DocAnalyticData>(pair.Value);                        
                        results.AddRange(session.GetProcessedDocumentsByJobId(pair.Key));
                        jobIds[pair.Key] -= results.Count;
                        count += results.Count;
                    }

                    Console.WriteLine("{0} documents received for {1} Job ID.", count, pair.Key);
                }
            }

            Console.WriteLine();
            Console.WriteLine("Done!");
            Console.ReadKey(false);
        }
    }
}
