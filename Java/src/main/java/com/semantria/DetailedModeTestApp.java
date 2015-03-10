package com.semantria;

import com.semantria.interfaces.ISerializer;
import com.semantria.mapping.Document;
import com.semantria.mapping.output.*;
import com.semantria.serializer.XmlSerializer;

import java.util.*;

public class DetailedModeTestApp
{
    private static final int TIMEOUT_BEFORE_GETTING_RESPONSE = 500; //in millisec

	public static void main(String args[])
	{
		// Use correct Semantria API credentias here
		String key = "";
		String secret = "";

		if( args != null && args.length == 2 )
		{
			key = args[0];
			secret = args[1];
		}

        Hashtable<String, TaskStatus> docsTracker = new Hashtable<String, TaskStatus>();

		// Initial texts for processing
		List<String> initialTexts = new ArrayList<String>();
		initialTexts.add("Lisa - there's 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh's). I couldn't find them instore and i'm not going to walmart before the 19th. Oh well sounds like i'm not missing much ...lol");
		initialTexts.add("In Lake Louise - a ₤ guided walk for the family with Great Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The \"must-do\" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.");
		initialTexts.add("On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time.");
		initialTexts.add("I'm going to go buy the new Flotsam 5000 when it's released on Wednesday.");
		
		System.out.println("Semantria service demo.");
		
		// Creates JSON serializer instance
		//ISerializer jsonSerializer = new JsonSerializer();
		// Initializes new session with the serializer object and the keys.
        ISerializer serializer = new XmlSerializer();
		Session session = Session.createSession(key, secret, serializer, true);
		session.setCallbackHandler(new CallbackHandler());

        List<Document> outgoingBatch = new ArrayList<Document>(initialTexts.size());
        for(String text : initialTexts) {
            // Creates a sample document which need to be processed on Semantria
            String uid = UUID.randomUUID().toString();
            Document doc = new Document(uid, text);
            outgoingBatch.add(doc);
            docsTracker.put(uid, TaskStatus.QUEUED);
        }

		if( session.queueBatch(outgoingBatch) == 202) {
            System.out.println("\"" + outgoingBatch.size() + "\" documents queued successfully.");
        }
		System.out.println();
		try
		{
			List<DocAnalyticData> processed = new ArrayList<DocAnalyticData>();
			
			while(docsTracker.containsValue(TaskStatus.QUEUED))
			{
                // As Semantria isn't real-time solution you need to wait some time before getting of the processed results
                // In real application here can be implemented two separate jobs, one for queuing of source data another one for retrieving
                // Wait half of second while Semantria process queued document
                Thread.currentThread().sleep(TIMEOUT_BEFORE_GETTING_RESPONSE);

				// Requests processed results from Semantria service
				List<DocAnalyticData> temp = session.getProcessedDocuments();
                for(Iterator<DocAnalyticData> i = temp.iterator(); i.hasNext(); ) {
                    DocAnalyticData item = i.next();

                    if (docsTracker.containsKey(item.getId())) {
                        processed.add(item);
                        docsTracker.put(item.getId(), TaskStatus.PROCESSED);
                    }
                }

                System.out.println("Retrieving your processed results...");
			}

			// Output results
			for(DocAnalyticData doc : processed)
			{
				System.out.println("Document:\n\tid: " + doc.getId() + "\n\tsentiment score: " + Float.toString(doc.getSentimentScore()) + "\n\tsentiment polarity: " + doc.getSentimentPolarity());
				System.out.println();
				if(doc.getAutoCategories() != null)
				{
					System.out.println("\tdocument categories:");
					for(DocCategory category : doc.getAutoCategories())
					{
						System.out.println("\t\ttopic: " + category.getTitle() + " \n\t\tStrength score: " + Float.toString(category.getStrengthScore()));
						System.out.println();
					}
				}
				if(doc.getThemes() != null)
				{
					System.out.println("\tdocument themes:");
					for(DocTheme theme : doc.getThemes())
					{
						System.out.println("\t\ttitle: " + theme.getTitle() + " \n\t\tsentiment: " + Float.toString(theme.getSentimentScore()) + "\n\t\tsentiment polarity: " + theme.getSentimentPolarity());
						System.out.println();
					}
				}
				if(doc.getEntities() != null)
				{
					System.out.println("\tentities:");
					for(DocEntity entity : doc.getEntities())
					{
						System.out.println("\t\ttitle: " + entity.getTitle() + "\n\t\tsentiment: " + Float.toString(entity.getSentimentScore()) + "\n\t\tsentiment polarity: " + entity.getSentimentPolarity());
						System.out.println();
					}
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
		}
	}
}
