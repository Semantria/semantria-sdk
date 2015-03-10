package com.semantria;

import com.semantria.interfaces.ICallbackHandler;
import com.semantria.mapping.Document;
import com.semantria.mapping.configuration.Configuration;
import com.semantria.mapping.output.CollAnalyticData;
import com.semantria.mapping.output.DocAnalyticData;
import com.semantria.serializer.JsonSerializer;
import com.semantria.utils.RequestArgs;
import com.semantria.utils.ResponseArgs;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;

public class AutoResponseFeatureTestApp {
	
	public static void main(String [] args) throws InterruptedException 
	{
		final String consumerKey = "";
		final String consumerSecret = "";
				
		
		List<Document> docList = new ArrayList<Document>(100);
		final List<DocAnalyticData> resList = new ArrayList<DocAnalyticData>(100);
		
		System.out.println("Semantria Auto-response feature demo.");
		System.out.println();
		
		File file = new File( System.getProperty("user.dir") + "\\src\\main\\java\\" + DiscoveryModeTestApp.class.getPackage().getName().replace(".","\\") + "\\source.txt");
        
        if( !file.exists() )
        {
            System.out.println("Source file isn't available.");
            return;
        }
        
        //Reads collection from the source file
        BufferedReader br =null;
        
        try
        {
       
        	br = new BufferedReader(new FileReader(file));
            String strLine;

            while ((strLine = br.readLine()) != null)
            {	
            	
            	String uid = UUID.randomUUID().toString();
             
                if (StringUtils.isEmpty(strLine) || strLine.length() < 3)
                {
                    continue;
                }

                docList.add(new Document(uid, strLine));
            }
           
        }
        catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }finally {
        	
        	 if(br!=null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	
        }
        
        
        Session session = Session.createSession(consumerKey, consumerSecret, new JsonSerializer());
        session.setCallbackHandler(new ICallbackHandler() {
            @Override
            public void onResponse(Object sender, ResponseArgs responseArgs) {
            }

            @Override
            public void onRequest(Object sender, RequestArgs requestArgs) {
            }

            @Override
            public void onError(Object sender, ResponseArgs errorArgs) {
                System.out.println(String.format("%d: %s", (int) errorArgs.getStatus(), errorArgs.getMessage()));
            }

            @Override
            public void onDocsAutoResponse(Object sender, List<DocAnalyticData> processedData) {
            	
                for (DocAnalyticData data : processedData)
                {
                	resList.add(data);
                }
            	
            }

            @Override
            public void onCollsAutoResponse(Object sender, List<CollAnalyticData> processedData) {
            }
        });
        
        
     // Remembers primary configuration to set it back after the test.
        List<Configuration> configsList = session.getConfigurations();
        Configuration primaryConf = getPrimaryConfig(configsList);
        
       
        
     // Updates or Creates new configuration for the test purposes.
	    if(!compareName(configsList, "AutoResponseTest"))
	    {
	    	Configuration config = new Configuration();
	    	config.setName("AutoResponseTest");
	    	config.setIsPrimary(true);
	    	config.setLanguage("English");
	    	config.setAutoResponse(true);
	    	
	    	List<Configuration> newConfig = new ArrayList<Configuration>();
	    	newConfig.add(config);
	    	
	    	System.out.println(session.addConfigurations(newConfig));
	    	
	    	
	    }
	    else
	    {
	    	
	    	
	    	Configuration config = getConfigByName(configsList, "AutoResponseTest");
	    	config.setIsPrimary(true);
	    	List<Configuration> newConfig = new ArrayList<Configuration>();
	    	newConfig.add(config);
	    	session.updateConfigurations(newConfig);
	    	
	    	
	    }
	    
	    
	    
        // Queues documents for analysis one by one
        for (int i = 0; i < docList.size(); i++)
        {

            session.queueDocument(docList.get(i));
            Thread.sleep(100);

           System.out.println("Documents queued/received rate: " + (i + 1) + "/" + resList.size());
        }
        
        
        // The final call to get remained data from server, Just for demo purposes.
        Thread.sleep(1000);
        while (docList.size() != resList.size())
        {
            List<DocAnalyticData> lastResults = session.getProcessedDocuments();
            for(DocAnalyticData data : lastResults)
            {
                resList.add(data);
            }

            Thread.sleep(500);
        }
        
        System.out.println("Documents queued/received rate: " + docList.size()  + "/" +  resList.size());

        // Sets original primary configuration back after the test.
    	List<Configuration> newConfig = new ArrayList<Configuration>();
    	newConfig.add(primaryConf);
    	session.updateConfigurations(newConfig);
 			
	}
	
    public static boolean compareName(List<Configuration> config, String compare)
    {
    	boolean configExist = false;
    	for(Configuration configuration : config)
    	{
    		if(configuration.getName().equals(compare))
    		{
    			configExist = true;
    			break;
    			
    		}
    		
    	}
    	
    	return configExist;	
    }
    
    
    public static Configuration getPrimaryConfig(List<Configuration> config)
    {
    	
    	Configuration getConfig = null;
    	for(Configuration configuration : config)
    	{
    		if(configuration.getIsPrimary() == true)
    		{
    			getConfig =  configuration;
    			break;
    			
    		}
    			
    	}
    	
    	return getConfig;
    	
    }
    
    
    public static Configuration getConfigByName(List<Configuration> config, String name)
    {
    	
    	Configuration getConfig = null;
    	
    	for(Configuration configuration : config)
    	{
    		if(configuration.getName().equals(name))
    		{
    			getConfig =  configuration;
    			break;
    			
    		} 		
    		
    	}
    	   	
    	return getConfig;
    	
    }

}
