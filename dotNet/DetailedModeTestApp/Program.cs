using System;
using System.Collections.Generic;

using Semantria.Com;
using Semantria.Com.Serializers;
using Semantria.Com.Mapping;
using Semantria.Com.Mapping.Output;

using System.Linq;
using Semantria.Com.Mapping.Configuration;

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

            // Initial texts for processing
            List<string> initialTexts = new List<string>() {
                @"Lisa - there's 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh's). I couldn't find them instore and i'm not going to walmart before the 19th. Oh well sounds like i'm not missing much ...lol",
                @"In Lake Louise - a guided walk for the family with Great Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The ""must-do"" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.",
                @"On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time.",
                @"I’m going to go buy the new Flotsam 5000 when it’s released on Wednesday."
            };

            Console.WriteLine("Semantria Detailed processing mode demo.");

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

                List<Document> outgoingBatch = new List<Document>(initialTexts.Count);
                foreach (string text in initialTexts)
                {
                    string docId = Guid.NewGuid().ToString();
                    // Creates a sample document which need to be processed on Semantria
                    Document doc = new Document()
                    {
                        Id = docId,
                        Text = text
                    };

                    docsTracker.Add(docId, TaskStatus.QUEUED);
                    outgoingBatch.Add(doc);
                }

                // Queues document for processing on Semantria service
                if (session.QueueBatchOfDocuments(outgoingBatch) != -1)
                {
                    Console.WriteLine(string.Format("{0} documents queued successfully.", outgoingBatch.Count));
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
                    if (data.AutoCategories != null && data.AutoCategories.Count > 0)
                    {
                        Console.WriteLine("Document categories:");
                        foreach (DocCategory category in data.AutoCategories)
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
