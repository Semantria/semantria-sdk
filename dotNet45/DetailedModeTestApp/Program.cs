using System;
using System.Collections.Generic;
using System.IO;

using Semantria.Com;
using System.Linq;

namespace DetailedModeTestApp
{
    class Program
    {
        static void Main(string[] args)
        {
            // Set environment vars before calling this program
            // or edit this file and put your key and secret here.
            string consumerKey = Environment.GetEnvironmentVariable("SEMANTRIA_KEY");
            string consumerSecret = Environment.GetEnvironmentVariable("SEMANTRIA_SECRET");

            // docsTracker keeps IDs of sent documents and their statuses.
            // It's needed to match docs with their analysis results.
            Dictionary <string, dynamic> docsTracker = new Dictionary<string, dynamic>(4);
            List<string> initialTexts = new List<string>();

            Console.WriteLine("Semantria Detailed processing mode demo.");

            string path = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "source.txt");
            if (!File.Exists(path))
            {
                Console.WriteLine("Source file isn't available.");
                return;
            }

            // Read collection from the source file
            Console.WriteLine("Reading dataset from file...");
            using (StreamReader stream = new StreamReader(path))
            {
                while (!stream.EndOfStream)
                {
                    string line = stream.ReadLine();
                    if (string.IsNullOrEmpty(line) || line.Length < 3)
                        continue;

                    initialTexts.Add(line);
                }
            }

            using (Session session = Session.CreateSession(consumerKey, consumerSecret))
            {
                // Error callback handler. This event will occur in case of server-side error
                session.Error += new Session.ErrorHandler(delegate(object sender, ResponseErrorEventArgs ea)
                {
                    Console.WriteLine(string.Format("{0}: {1}", (int)ea.Status, ea.Message));
                });

                dynamic configList = session.GetConfigurations();

                // Get subscription object to get user limits
                dynamic subscription = session.GetSubscription();

                // Queue all docs to be analyzed
                List<dynamic> outgoingBatch = new List<dynamic>(subscription.basic_settings.incoming_batch_limit);
                IEnumerator<string> iterrator = initialTexts.GetEnumerator();
                while (iterrator.MoveNext())
                {
                    string docId = Guid.NewGuid().ToString();
                    dynamic doc = new
                    {
                        id = docId,
                        text = iterrator.Current
                    };

                    outgoingBatch.Add(doc);
                    docsTracker.Add(docId, "QUEUED");

                    if (outgoingBatch.Count == subscription.basic_settings.incoming_batch_limit)
                    {
                        // Queue batch of documents for processing on Semantria service
                        if (session.QueueBatchOfDocuments(outgoingBatch) != -1)
                        {
                            Console.WriteLine(string.Format("{0} documents queued successfully.", outgoingBatch.Count));
                            outgoingBatch.Clear();
                        }
                    }
                }

                // Check for last batch, which may not be a full size batch.
                if (outgoingBatch.Count > 0)
                {
                    if (session.QueueBatchOfDocuments(outgoingBatch) != -1)
                    {
                        Console.WriteLine(string.Format("{0} documents queued successfully.", outgoingBatch.Count));
                    }
                }

                Console.WriteLine();

                // Now, get the  results.
                // In a real application you would typically implement two separate
                // jobs, one for queuing docs to analyze and the other one for
                // retreiving analysis results.
                List<dynamic> results = new List<dynamic>();
                while (docsTracker.Any(item => item.Value == "QUEUED"))
                {
                    // Wait a bit between polling requests.
                    System.Threading.Thread.Sleep(500);

                    // Requests processed results from Semantria service
                    Console.WriteLine("Retrieving your processed results...");
                    dynamic incomingBatch = session.GetProcessedDocuments();

                    foreach (dynamic item in incomingBatch)
                    {
                        if (docsTracker.ContainsKey(item.id))
                        {
                            docsTracker[item.id] = item.status;
                            results.Add(item);
                        }
                    }
                }
                Console.WriteLine();

                // Print sample of analysis results for each doc. (There's lots more in there!)
                foreach (dynamic data in results)
                {
                    // print document sentiment score
                    Console.WriteLine(string.Format("Document {0}. Sentiment score: {1}", data.id, data.sentiment_score));

                    // print intentions
                    if (data.auto_categories != null && data.auto_categories.Count > 0)
                    {
                        Console.WriteLine("Document categories:");
                        foreach (dynamic category in data.auto_categories)
                            Console.WriteLine(string.Format("\t{0} (strength: {1})", category.title, category.strength_score));
                    }

                    // Print document themes
                    if (data.themes != null && data.themes.Count > 0)
                    {
                        Console.WriteLine("Document themes:");
                        foreach (dynamic theme in data.themes)
                            Console.WriteLine(string.Format("\t{0} (sentiment: {1})", theme.title, theme.sentiment_score));
                    }

                    // Print document entities
                    if (data.entities != null && data.entities.Count > 0)
                    {
                        Console.WriteLine("Entities:");
                        foreach (dynamic entity in data.entities)
                            Console.WriteLine(string.Format("\t{0} : {1} (sentiment: {2})", entity.title, entity.entity_type, entity.sentiment_score));
                    }

                    Console.WriteLine();
                }
            }

            Console.WriteLine("Hit any key to exit.");
            Console.ReadKey(false);
        }
    }
}
