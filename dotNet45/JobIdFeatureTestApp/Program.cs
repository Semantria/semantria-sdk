using System;
using System.IO;
using System.Collections.Generic;

using Semantria.Com;
using System.Linq;

namespace JobIdFeatureTestApp
{
    class Program
    {
        static void Main(string[] args)
        {
            // Set environment vars before calling this program
            // or edit this file and put your key and secret here.
            string consumerKey = Environment.GetEnvironmentVariable("SEMANTRIA_KEY");
            string consumerSecret = Environment.GetEnvironmentVariable("SEMANTRIA_SECRET");

            // null - send every single document separately
            // false - send uniqueJobIdCount batches
            // true - send all documents in single batch
            bool? dataSendingMode = false;
            int uniqueJobIdCount = 4;

            Dictionary<string, int> jobIds = new Dictionary<string, int>(uniqueJobIdCount);
            Dictionary<string, List<dynamic>> documents = new Dictionary<string, List<dynamic>>();

            Console.WriteLine("Semantria JobId feature demo.");

            string path = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "source.txt");
            if (!File.Exists(path))
            {
                Console.WriteLine("Source file isn't available.");
                return;
            }

            // Generate N unique jobId values
            for (int index = 0; index < uniqueJobIdCount; index++)
            {
                string id = Guid.NewGuid().ToString();

                jobIds.Add(id, 0);
                documents.Add(id, new List<dynamic>());
            }

            // Read documents from the source file
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

                    dynamic doc = new 
                    {
                        id = Guid.NewGuid().ToString(),
                        text = line,
                        job_id = jobId
                    };

                    documents[jobId].Add(doc);
                }
            }

            using (Session session = Session.CreateSession(consumerKey, consumerSecret))
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
                    foreach (KeyValuePair<string, List<dynamic>> pair in documents)
                    {
                        foreach (dynamic doc in pair.Value)
                        {
                            // Queue document for processing on Semantria service
                            session.QueueDocument(doc);
                        }

                        Console.WriteLine("{0} documents queued for {1} job ID", pair.Value.Count, pair.Key);
                    }
                }
                else if (!dataSendingMode.Value)
                {
                    foreach (KeyValuePair<string, List<dynamic>> pair in documents)
                    {
                        // Queue batch of documents for processing on Semantria service
                        if (session.QueueBatchOfDocuments(pair.Value) != -1)
                        {
                            Console.WriteLine("{0} documents queued for {1} job ID", pair.Value.Count, pair.Key);
                        }
                    }
                }
                else
                {
                    List<dynamic> aBatch = new List<dynamic>();

                    foreach (KeyValuePair<string, List<dynamic>> pair in documents)
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
                        // Wait a bit between polling requests.
                        System.Threading.Thread.Sleep(200);

                        // Requests processed results from Semantria service
                        List<dynamic> results = new List<dynamic>(pair.Value);
                        var temp = session.GetProcessedDocumentsByJobId(pair.Key);
                        results.AddRange(temp);
                        jobIds[pair.Key] -= results.Count;
                        count += results.Count;
                    }

                    Console.WriteLine("{0} documents received for {1} Job ID.", count, pair.Key);
                }
            }

            Console.WriteLine();
            Console.WriteLine("Done!");
            Console.WriteLine("Hit any key to exit.");
            Console.ReadKey(false);
        }
    }
}
