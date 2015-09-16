using System;
using System.IO;
using System.Threading;
using System.Collections.Generic;

using Semantria.Com;

namespace DiscoveryModeTestApp
{
	class Program
	{
		static void Main(string[] args)
		{
            const string consumerKey = "";
            const string consumerSecret = "";

			dynamic collection = new { id = Guid.NewGuid().ToString(), documents = new List<dynamic>() };

			Console.WriteLine("Semantria Discovery processing mode demo.");
            Console.WriteLine();

			string path = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "source.txt");
			if (!File.Exists(path))
			{
				Console.WriteLine("Source file isn't available.");
				return;
			}

			//Reads collection from the source file
			Console.WriteLine("Reading collection from file...");
			using (StreamReader stream = new StreamReader(path))
			{
				while (!stream.EndOfStream)
				{
					string line = stream.ReadLine();
					if (string.IsNullOrEmpty(line) || line.Length < 3)
						continue;

                    collection.documents.Add(line);
				}
			}

			dynamic result = null;

			//Initializes Semantria Session
			using (Session session = Session.CreateSession(consumerKey, consumerSecret))
			{
                // Error callback handler. This event will occur in case of server-side error
				session.Error += new Session.ErrorHandler(delegate(object sender, ResponseErrorEventArgs ea)
				{
					Console.WriteLine(string.Format("{0}: {1}", (int)ea.Status, ea.Message));
				});

				//Queues collection for analysis using default configuration
				if (session.QueueCollection(collection) != -1)
					Console.WriteLine(string.Format("\"{0}\" collection queued successfully.", collection.id));

				do
				{
					Thread.Sleep(1000);
					Console.WriteLine("Retrieving your processed results...");
					//Retreives analysis results for queued collection
					result = session.GetCollection(collection.id);
				}
				while (result.status == "QUEUED");
			}

			//Prints analysis results
			Console.WriteLine();
            Console.WriteLine("Facets and attributes:");
            if (result.facets != null)
            {
                foreach (dynamic facet in result.facets)
                {
                    Console.WriteLine("{0} : {1}", facet.label, facet.count);
                    if (facet.attributes == null)
                        continue;

                    foreach (dynamic attr in facet.attributes)
                        Console.WriteLine("\t{0} : {1}", attr.label, attr.count);
                }
            }

			Console.ReadKey(false);
		}
	}
}
