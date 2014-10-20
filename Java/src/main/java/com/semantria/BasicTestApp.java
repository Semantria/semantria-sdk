package com.semantria;

import com.semantria.mapping.Document;
import com.semantria.mapping.output.DocAnalyticData;
import com.semantria.mapping.output.DocEntity;
import com.semantria.mapping.output.DocTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BasicTestApp
{
    private static final int TIMEOUT_BEFORE_GETTING_RESPONSE = 5000; //in millisec

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
		
		// Initial texts for processing
		List<String> initialTexts = new ArrayList<String>();
		initialTexts.add("Lisa - there's 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh's). I couldn't find them instore and i'm not going to walmart before the 19th. Oh well sounds like i'm not missing much ...lol");
		initialTexts.add("In Lake Louise - a ₤ guided walk for the family with Great Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The \"must-do\" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.");
		initialTexts.add("On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time.");
		
		System.out.println("Semantria service demo.");
		
		// Creates JSON serializer instance
		//ISerializer jsonSerializer = new JsonSerializer();
		// Initializes new session with the serializer object and the keys.
		Session session = Session.createSession(key, secret, true);
		session.setCallbackHandler(new CallbackHandler());
		for(String text : initialTexts)
		{
			String uid = UUID.randomUUID().toString();
			// Creates a sample document which need to be processed on Semantria
			// Queues document for processing on Semantria service
			if( session.queueDocument( new Document( uid, text )) == 202)
			{
				System.out.println("\"" + uid + "\" document queued succsessfully.");
			}
		}
		System.out.println();
		try
		{
			// As Semantria isn't real-time solution you need to wait some time before getting of the processed results
            // In real application here can be implemented two separate jobs, one for queuing of source data another one for retreiving
            // Wait ten seconds while Semantria process queued document
			Thread.sleep(TIMEOUT_BEFORE_GETTING_RESPONSE);
			List<DocAnalyticData> processed = new ArrayList<DocAnalyticData>();
			
			while(processed.size() < initialTexts.size())
			{
				// Requests processed results from Semantria service
				List<DocAnalyticData> temp = session.getProcessedDocuments();
				processed.addAll(temp);
				if(processed.size() >= initialTexts.size()) break;
                System.out.println("Retrieving your processed results...");
				Thread.currentThread().sleep(TIMEOUT_BEFORE_GETTING_RESPONSE);
			}
			// Output results
			for(DocAnalyticData doc : processed)
			{
				System.out.println("Document:\n\tid: " + doc.getId() + "\n\tsentiment score: " + Float.toString(doc.getSentimentScore()) + "\n\tsentiment polarity: " + doc.getSentimentPolarity());
				System.out.println();
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
						if( entity.getThemes() != null )
						{
							System.out.println("\t\tentity themes:");
							for (DocTheme theme : entity.getThemes())
							{
								System.out.println("\t\t\ttitle: " + theme.getTitle() + " \n\t\t\tsentiment: " + Float.toString(theme.getSentimentScore()) + "\n\t\t\tsentiment polarity: " + theme.getSentimentPolarity());
								System.out.println();
							}
						}

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
