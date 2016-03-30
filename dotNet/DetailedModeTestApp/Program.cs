using System;
using System.Collections.Generic;
using System.IO;

using Semantria.Com;
using Semantria.Com.Serializers;
using Semantria.Com.Mapping;
using Semantria.Com.Mapping.Output;

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
            Dictionary<string, TaskStatus> docsTracker = new Dictionary<string, TaskStatus>(4);
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

            // Creates JSON serializer instance
			ISerializer serializer = new JsonSerializer();

            // Initializes new session with the serializer object and the keys.
            using (Session session = Session.CreateSession(consumerKey, consumerSecret, serializer))
            {
                // Error callback handler. This event will occur in case of server-side error
                session.Error += new Session.ErrorHandler(delegate(object sender, ResponseErrorEventArgs ea)
                {
                    Console.WriteLine(string.Format("{0}: {1}", (int)ea.Status, ea.Message));
                });

                //Obtaining subscription object to get user limits applicable on server side
                Subscription subscription = session.GetSubscription();

                List<Document> outgoingBatch = new List<Document>(subscription.BasicSettings.BatchLimit);
                IEnumerator<string> iterrator = initialTexts.GetEnumerator();
                while (iterrator.MoveNext())
                {
                    string docId = Guid.NewGuid().ToString();
                    Document doc = new Document()
                    {
                        Id = docId,
                        Text = iterrator.Current
                    };

                    outgoingBatch.Add(doc);
                    docsTracker.Add(docId, TaskStatus.QUEUED);

                    if (outgoingBatch.Count == subscription.BasicSettings.BatchLimit)
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
                List<DocAnalyticData> results = new List<DocAnalyticData>();
                while (docsTracker.Any(item => item.Value == TaskStatus.QUEUED))
                {
                    System.Threading.Thread.Sleep(500);

                    // Requests processed results from Semantria service
                    Console.WriteLine("Retrieving your processed results...");
                    IList<DocAnalyticData> incomingBatch = session.GetProcessedDocuments();

                    foreach (DocAnalyticData item in incomingBatch)
                    {
                        if (docsTracker.ContainsKey(item.Id))
                        {
                            docsTracker[item.Id] = item.Status;
                            results.Add(item);
                        }
                    }
                }
                Console.WriteLine();

                foreach (DocAnalyticData data in results)
                {
                    // Printing of document sentiment score
                    Console.WriteLine(string.Format("Document {0}. Sentiment score: {1}", data.Id, data.SentimentScore));

                    // Printing of intentions
                    if (data.Topics != null && data.Topics.Count > 0)
                    {
                        Console.WriteLine("Document topics:");
                        foreach (Topic topic in data.Topics)
                            Console.WriteLine(string.Format("\t{0} (type: {1}) (strength: {2})", topic.Title, topic.Type, topic.SentimentScore));
                    }

                    // Printing of intentions
                    if (data.AutoCategories != null && data.AutoCategories.Count > 0)
                    {
                        Console.WriteLine("Document categories:");
                        foreach (Topic category in data.AutoCategories)
                            Console.WriteLine(string.Format("\t{0} (strength: {1})", category.Title, category.StrengthScore));
                    }

                    // Printing of document themes
                    if (data.Themes != null && data.Themes.Count > 0)
                    {
                        Console.WriteLine("Document themes:");
                        foreach (DocTheme theme in data.Themes)
                            Console.WriteLine(string.Format("\t{0} (sentiment: {1})", theme.Title, theme.SentimentScore));
                    }

                    // Printing of document entities
                    if (data.Entities != null && data.Entities.Count > 0)
                    {
                        Console.WriteLine("Entities:");
                        foreach (DocEntity entity in data.Entities)
                            Console.WriteLine(string.Format("\t{0} : {1} (sentiment: {2})", entity.Title, entity.EntityType, entity.SentimentScore));
                    }

                    Console.WriteLine();
                }
            }

            Console.ReadKey(false);
        }
    }
}
