using System;
using System.Linq;
using System.IO;
using System.Threading;
using System.Collections.Generic;

using Semantria.Com;
using Semantria.Com.Serializers;
using Semantria.Com.Mapping;
using Semantria.Com.Mapping.Output;
using Semantria.Com.Mapping.Configuration;

namespace AutoResponseTest
{
    class Program
    {
        static void Main(string[] args)
        {
            string consumerKey = System.Environment.GetEnvironmentVariable("SEMANTRIA_KEY");
            string consumerSecret = System.Environment.GetEnvironmentVariable("SEMANTRIA_SECRET");

            bool errorFlag = false;

            IList<Document> docsList = new List<Document>(100);
            IList<DocAnalyticData> resList = new List<DocAnalyticData>(100);

            Console.WriteLine("Semantria Auto-response feature demo.");
            Console.WriteLine();

            string path = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "source.txt");
            if (!File.Exists(path))
            {
                Console.WriteLine("Source file isn't available.");
                return;
            }

            //Reads collection from the source file
            Console.WriteLine("Reading documents from file...");
            using (StreamReader stream = new StreamReader(path))
            {
                while (!stream.EndOfStream)
                {
                    string line = stream.ReadLine();
                    if (string.IsNullOrEmpty(line) || line.Length < 3)
                        continue;

                    docsList.Add(new Document() { Id = Guid.NewGuid().ToString(), Text = line });
                }
            }

            //Creates JSON serializer to use JSON as data format
            JsonSerializer serializer = new JsonSerializer();

            //Initializes Semantria Session
            using (Session session = Semantria.Com.Session.CreateSession(consumerKey, consumerSecret, serializer))
            {
                // Error callback handler. This event will occur in case of server-side error
                session.Error += new Session.ErrorHandler(delegate(object sender, ResponseErrorEventArgs ea)
                {
                    Console.WriteLine(string.Format("{0}: {1}", (int)ea.Status, ea.Message));
                    errorFlag = true;
                });

                // Auto-response callback handler. This event will occur in case of automatic data delivering.
                session.DocsAutoResponse += new Session.DocsAutoResponseHandler(delegate(object sender, DocsAutoResponseEventArgs ea)
                {
                    foreach (DocAnalyticData data in ea.AnalyticData)
                    {
                        resList.Add(data);
                    }
                });

                // Remembers primary configuration to set it back after the test.
                IList<Configuration> configsList = session.GetConfigurations();
                Configuration primaryConf = configsList.First(item => item.IsPrimary.Equals(true));

                // Updates or Creates new configuration for the test purposes.
                if (!configsList.Any(item => item.Name.Equals("AutoResponseTest")))
                {
                    Configuration config = new Configuration() { Name = "AutoResponseTest", Language = "English", IsPrimary = true, AutoResponse = true };
                    session.AddConfigurations(new List<Configuration>() { config });
                }
                else
                {
                    Configuration config = configsList.First(item => item.Name.Equals("AutoResponseTest"));
                    config.IsPrimary = true;
                    session.UpdateConfigurations(new List<Configuration>() { config });
                }

                // Queues documents for analysis one by one
                for (int i = 0; i < docsList.Count; i++)
                {
                    if (errorFlag)
                        break;

                    session.QueueDocument(docsList[i]);
                    Thread.Sleep(200);

                    Console.WriteLine("Documents queued/received rate: {0}/{1}", i + 1, resList.Count);
                }

                // The final call to get remained data from server, Just for demo purposes.
                Thread.Sleep(1000);
                while (docsList.Count != resList.Count)
                {
                    IList<DocAnalyticData> lastResults = session.GetProcessedDocuments();
                    foreach (DocAnalyticData data in lastResults)
                    {
                        resList.Add(data);
                    }

                    Thread.Sleep(500);
                }

                Console.WriteLine("Documents queued/received rate: {0}/{1}", docsList.Count, resList.Count);

                // Sets original primary configuration back after the test.
                session.UpdateConfigurations(new List<Configuration>() { primaryConf });
            }

            Console.ReadKey(false);
        }
    }
}
