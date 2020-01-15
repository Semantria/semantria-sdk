using System;
using System.Collections.Generic;
using System.IO;

using Semantria.Com;
using Semantria.Com.Mapping;
using Semantria.Com.Mapping.Output;

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
            Dictionary<string, TaskStatus> docsTracker = new Dictionary<string, TaskStatus>(4);
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

                // Get subscription object to get user limits
                Subscription subscription = session.GetSubscription();

                // Queue all docs to be analyzed
                List<Document> outgoingBatch = new List<Document>(subscription.BasicSettings.IncomingBatchLimit);
                IEnumerator<string> iterator = initialTexts.GetEnumerator();
                while (iterator.MoveNext())
                {
                    string docId = Guid.NewGuid().ToString();
                    Document doc = new Document()
                    {
                        Id = docId,
                        Text = iterator.Current,
                        // metadata must be json
                        Metadata = String.Format("{{\"color\":\"red\",\"size\":{0}}}", iterator.Current.Length)
                    };

                    outgoingBatch.Add(doc);
                    docsTracker.Add(docId, TaskStatus.QUEUED);

                    if (outgoingBatch.Count == subscription.BasicSettings.IncomingBatchLimit)
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
                List<DocAnalyticData> results = new List<DocAnalyticData>();
                while (docsTracker.Any(item => item.Value == TaskStatus.QUEUED))
                {
                    // Wait a bit between polling requests.
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

                // Print sample of analysis results for each doc. (There's lots more in there!)
                foreach (DocAnalyticData data in results)
                {
                    // print document sentiment score
                    Console.WriteLine(string.Format("Document {0}. Sentiment score: {1}", data.Id, data.SentimentScore));
                    Console.WriteLine(string.Format("  metadata: {0}", data.Metadata));

                    // print intentions
                    if (data.Topics != null && data.Topics.Count > 0)
                    {
                        Console.WriteLine("Document topics:");
                        foreach (Topic topic in data.Topics) {
                            Console.WriteLine(string.Format("  {0} (type: {1}) (strength: {2})", topic.Title, topic.Type, topic.SentimentScore));
                            foreach (SentimentMentionPhrase sentiment_phrase in topic.SentimentPhrases) {
                                Console.WriteLine(string.Format("    sentiment phrase: {0} (type: {1}) (score: {2}) (negated: {3}) (negator: {4})",
                                    sentiment_phrase.Phrase.Title, sentiment_phrase.Type, sentiment_phrase.SentimentScore,
                                    sentiment_phrase.Phrase.IsNegated, sentiment_phrase.Phrase.Negator));
                                foreach (MentionPhrase supporting_phrase in sentiment_phrase.SupportingPhrases) {
                                    Console.WriteLine(string.Format("      supporting phrase: {0} (type: {1}) (word: {2}) (length: {3})",
                                        supporting_phrase.Title, supporting_phrase.Type, supporting_phrase.Word, supporting_phrase.Length));
                                }
                            }
                        }
                    }

                    // print intentions
                    if (data.AutoCategories != null && data.AutoCategories.Count > 0)
                    {
                        Console.WriteLine("Document categories:");
                        foreach (Topic category in data.AutoCategories)
                            Console.WriteLine(string.Format("  {0} (strength: {1})", category.Title, category.StrengthScore));
                    }

                    // print document themes
                    if (data.Themes != null && data.Themes.Count > 0)
                    {
                        Console.WriteLine("Document themes:");
                        foreach (DocTheme theme in data.Themes)
                            Console.WriteLine(string.Format("  {0} (sentiment: {1})", theme.Title, theme.SentimentScore));
                    }

                    // print document entities
                    if (data.Entities != null && data.Entities.Count > 0)
                    {
                        Console.WriteLine("Entities:");
                        foreach (DocEntity entity in data.Entities)
                            Console.WriteLine(string.Format("  {0} : {1} (sentiment: {2})", entity.Title, entity.EntityType, entity.SentimentScore));
                    }

                    Console.WriteLine();
                }
            }

            Console.WriteLine("Hit any key to exit.");
            Console.ReadKey(false);
        }
    }
}
