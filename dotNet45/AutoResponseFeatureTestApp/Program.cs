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
            const string consumerKey = "";
            const string consumerSecret = "";

            bool errorFlag = false;

            dynamic docsList = new List<dynamic>(100);
            dynamic resList = new List<dynamic>(100);

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

                    docsList.Add(new { id = Guid.NewGuid().ToString(), text = line });
                }
            }

            //Initializes Semantria Session
            using (Session session = Semantria.Com.Session.CreateSession(consumerKey, consumerSecret))
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
                    //var analyticData = ea.AnalyticData as IList<dynamic>;
                    foreach (dynamic data in ea.AnalyticData)
                    {
                        resList.Add(data);
                    }
                });

                // Remembers primary configuration to set it back after the test.
                dynamic temp  = session.GetConfigurations();
                var configsList = ((IEnumerable)temp).Cast<dynamic>();
                dynamic primaryConf = configsList.First(item => item.is_primary.Equals(true));

                // Updates or Creates new configuration for the test purposes.
                if (!configsList.Any(item => item.name.Equals("AutoResponseTest")))
                {
                    dynamic config = new { name = "AutoResponseTest", language = "English", is_primary = true, auto_response = true };
                    session.AddConfigurations(new [] { config });
                }
                else
                {
                    dynamic config = configsList.First(item => item.name.Equals("AutoResponseTest"));
                    config.is_primary = true;
                    config.auto_response = true;
                    session.UpdateConfigurations(new [] { config });
                }

                // Queues documents for analysis one by one
                for (int i = 0; i < docsList.Count; i++)
                {
                    if (errorFlag)
                        break;

                    session.QueueDocument(docsList[i]);
                    Thread.Sleep(500);

                    Console.WriteLine("Documents queued/received rate: {0}/{1}", i + 1, resList.Count);
                }

                // The final call to get remained data from server, Just for demo purposes.
                Thread.Sleep(1000);
                while (docsList.Count != resList.Count)
                {
                    dynamic lastResults = session.GetProcessedDocuments();
                    foreach (dynamic data in lastResults)
                    {
                        resList.Add(data);
                    }

                    Thread.Sleep(500);
                }

                Console.WriteLine("Documents queued/received rate: {0}/{1}", docsList.Count, resList.Count);

                // Sets original primary configuration back after the test.
                session.UpdateConfigurations(new []{ primaryConf });
            }

            Console.ReadKey(false);
        }
    }
}
