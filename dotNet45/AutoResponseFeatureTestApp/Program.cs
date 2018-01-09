using System;
using System.Linq;
using System.IO;
using System.Threading;
using System.Collections;
using System.Collections.Generic;

using Semantria.Com;

namespace FacetsTest
{
    class Program
    {
        static void Main(string[] args)
        {
            // Set environment vars before calling this program
            // or edit this file and put your key and secret here.
            string consumerKey = Environment.GetEnvironmentVariable("SEMANTRIA_KEY");
            string consumerSecret = Environment.GetEnvironmentVariable("SEMANTRIA_SECRET");

            string CONFIG_NAME = "AutoResponseTest";  // config to use or create
            bool errorFlag = false;

            Console.WriteLine("Semantria Auto-response feature demo.");
            Console.WriteLine();

            List<List<dynamic>> batches = readData();
            dynamic resultsList = new List<dynamic>(100);

            using (Session session = Semantria.Com.Session.CreateSession(consumerKey, consumerSecret))
            {
                // Error callback handler. This event will occur if there's a server-side error
                session.Error += new Session.ErrorHandler(delegate(object sender, ResponseErrorEventArgs ea)
                {
                    Console.WriteLine(string.Format("{0}: {1}", (int)ea.Status, ea.Message));
                    errorFlag = true;
                });

                // Auto-response callback handler. This event will occur if analysis results are
                // delivered in response to a queue request.
                session.DocsAutoResponse += new Session.DocsAutoResponseHandler(delegate(object sender, DocsAutoResponseEventArgs ea)
                {
                    foreach (dynamic data in ea.AnalyticData)
                    {
                        resultsList.Add(data);
                    }
                });

                // Update or create configuration for this test.
                // The config must have auto_response set to true.
                var configsList = ((IEnumerable)session.GetConfigurations()).Cast<dynamic>();
                dynamic config = configsList.FirstOrDefault(item => item.name.Equals(CONFIG_NAME));
                string configId;
                if (config == null)
                {
                    Console.WriteLine("Creating new config named '{}'...", CONFIG_NAME);
                    dynamic configs = session.AddConfigurations(new [] { new { name = CONFIG_NAME, language = "English", auto_response = true } });
                    config = configs[0];
                    configId = config.id;
                    Console.WriteLine("Created new config - id = {0}", configId);
                }
                else
                {
                    configId = config.id;
                    Console.WriteLine("Using config '{0}' - id = {1}", CONFIG_NAME, configId);
                }

                if (! config.auto_response)
                {
                    throw new Exception(String.Format("config named '{0}' does not have auto_response set to true", CONFIG_NAME));
                }

                // Queue documents for analysis, sleeping for 100 msec between batches.
                // This allows enough time for some docs to be processed and thus be available to be
                // returned by auto response.
                int docsSent = 0;
                foreach (List<dynamic> batch in batches)
                {
                    if (errorFlag)
                        break;
                    session.QueueBatchOfDocuments(batch, configId);
                    docsSent += batch.Count;
                    Thread.Sleep(100);
                    Console.WriteLine("Documents queued/received: {0}/{1}", docsSent, resultsList.Count);
                }

                // Finally, poll for any docs that weren't returned via auto response.
                // This is needed in this demo because it is only processing a fixed number of docs.
                // A continuous process that runs forever would not need to do this step, because
                // it would never get to the end of it's docs to be processed.l 
                while (docsSent != resultsList.Count)
                {
                    Thread.Sleep(200);
                    dynamic lastResults = session.GetProcessedDocuments(configId);
                    foreach (dynamic data in lastResults)
                    {
                        resultsList.Add(data);
                    }
                    Console.WriteLine("Polling: Documents queued/received: {0}/{1}", docsSent, resultsList.Count);
                }

                Console.WriteLine("Done: Total documents queued/received: {0}/{1}", docsSent, resultsList.Count);
            }

            Console.WriteLine("Hit any key to exit.");
            Console.ReadKey(false);
        }

        static List<List<dynamic>> readData()
        {
            // Read data from the source.txt, one doc per line
            string path = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "source.txt");
            if (!File.Exists(path))
            {
                Console.WriteLine("Data file source.txt not found.");
                Environment.Exit(1);
            }

            Console.WriteLine("Reading documents from file...");
            int batchSize = 10;
            List<List<dynamic>> batches = new List<List<dynamic>>(20);
            List<dynamic> batch = new List<dynamic>(batchSize);
            using (StreamReader stream = new StreamReader(path))
            {
                while (!stream.EndOfStream)
                {
                    string line = stream.ReadLine();
                    if (string.IsNullOrEmpty(line) || line.Length < 3)
                        continue;
                    batch.Add(new { id = Guid.NewGuid().ToString(), text = line });
                    if (batch.Count == batchSize)
                    {
                        batches.Add(batch);
                        batch = new List<dynamic>(batchSize);
                    }
                }
            }
            if (batch.Count > 0)
            {
                batches.Add(batch);
            }
            return batches;
        }

     }
}
