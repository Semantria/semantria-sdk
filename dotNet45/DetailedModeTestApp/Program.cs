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
            // Use correct Semantria API credentias here
            string consumerKey = "";
            string consumerSecret = "";

            // A dictionary that keeps IDs of sent documents and their statuses. It's required to make sure that we get correct documents from the API.
            Dictionary<string, dynamic> docsTracker = new Dictionary<string, dynamic>(4);
            List<string> initialTexts = new List<string>();

            Console.WriteLine("Semantria Detailed processing mode demo.");

            string path = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "source.txt");
            if (!File.Exists(path))
            {
                Console.WriteLine("Source file isn't available.");
                return;
            }

            //Reads collection from the source file
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

            // Initializes new session with the serializer object and the keys.
            using (Session session = Session.CreateSession(consumerKey, consumerSecret))
            {
                // Error callback handler. This event will occur in case of server-side error
                session.Error += new Session.ErrorHandler(delegate(object sender, ResponseErrorEventArgs ea)
                {
                    Console.WriteLine(string.Format("{0}: {1}", (int)ea.Status, ea.Message));
                });

                //Obtaining subscription object to get user limits applicable on server side
                dynamic subscription = session.GetSubscription();

                List<dynamic> outgoingBatch = new List<dynamic>(subscription.basic_settings.batch_limit);
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

                    if (outgoingBatch.Count == subscription.basic_settings.batch_limit)
                    {
                        // Queues batch of documents for processing on Semantria service
                        if (session.QueueBatchOfDocuments(outgoingBatch) != -1)
                        {
                            Console.WriteLine(string.Format("{0} documents queued successfully.", outgoingBatch.Count));
                            outgoingBatch.Clear();
                        }
                    }
                }

                if (outgoingBatch.Count > 0)
                {
                    // Queues batch of documents for processing on Semantria service
                    if (session.QueueBatchOfDocuments(outgoingBatch) != -1)
                    {
                        Console.WriteLine(string.Format("{0} documents queued successfully.", outgoingBatch.Count));
                    }
                }

                Console.WriteLine();

                // As Semantria isn't real-time solution you need to wait some time before getting of the processed results
                // In real application here can be implemented two separate jobs, one for queuing of source data another one for retreiving
                // Wait ten seconds while Semantria process queued document
                List<dynamic> results = new List<dynamic>();
                while (docsTracker.Any(item => item.Value == "QUEUED"))
                {
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

                foreach (dynamic data in results)
                {
                    // Printing of document sentiment score
                    Console.WriteLine(string.Format("Document {0}. Sentiment score: {1}", data.id, data.sentiment_score));

                    // Printing of intentions
                    if (data.auto_categories != null && data.auto_categories.Count > 0)
                    {
                        Console.WriteLine("Document categories:");
                        foreach (dynamic category in data.auto_categories)
                            Console.WriteLine(string.Format("\t{0} (strength: {1})", category.title, category.strength_score));
                    }

                    // Printing of document themes
                    if (data.themes != null && data.themes.Count > 0)
                    {
                        Console.WriteLine("Document themes:");
                        foreach (dynamic theme in data.themes)
                            Console.WriteLine(string.Format("\t{0} (sentiment: {1})", theme.title, theme.sentiment_score));
                    }

                    // Printing of document entities
                    if (data.entities != null && data.entities.Count > 0)
                    {
                        Console.WriteLine("Entities:");
                        foreach (dynamic entity in data.entities)
                            Console.WriteLine(string.Format("\t{0} : {1} (sentiment: {2})", entity.title, entity.entity_type, entity.sentiment_score));
                    }

                    Console.WriteLine();
                }
            }

            Console.ReadKey(false);
        }
    }
}