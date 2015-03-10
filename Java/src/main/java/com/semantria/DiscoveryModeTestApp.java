package com.semantria;

import com.semantria.interfaces.ICallbackHandler;
import com.semantria.mapping.Collection;
import com.semantria.mapping.output.*;
import com.semantria.serializer.JsonSerializer;
import com.semantria.utils.RequestArgs;
import com.semantria.utils.ResponseArgs;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DiscoveryModeTestApp
{
    private static final int TIMEOUT_BEFORE_GETTING_RESPONSE = 5000; //in millisec

	public static void main(String args[])
	{
		final String key = "";
		final String secret = "";

        final String collectionId = UUID.randomUUID().toString();

        Collection collection = new Collection(collectionId);
        collection.setDocuments(new ArrayList<String>());

        System.out.println("Semantria Collection processing mode demo.");

        File file = new File( DetailedModeTestApp.class.getProtectionDomain().getCodeSource().getLocation().toString().replace("/target/classes", "/src/main/java/" + DiscoveryModeTestApp.class.getPackage().getName().replace(".","/")).replace("file:/", "")+ "/source.txt");

        if( !file.exists() )
        {
            System.out.println("Source file isn't available.");
            return;
        }

        //Reads collection from the source file
        try
        {
            FileInputStream fstream = new FileInputStream(file);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null)
            {
                if (StringUtils.isEmpty(strLine) || strLine.length() < 3)
                {
                    continue;
                }

                collection.getDocuments().add(strLine);
            }
            in.close();
        }
        catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }

        CollAnalyticData result = null;

        //Initializes Semantria Session
        Session session = Session.createSession(key, secret, new JsonSerializer());
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
            }

            @Override
            public void onCollsAutoResponse(Object sender, List<CollAnalyticData> processedData) {
            }
        });

        //Queues collection for analysis using default configuration
        int status = session.queueCollection(collection);
        if ( status == 200 || status == 2002 )
        {
            System.out.println(String.format("\"%s\" collection queued successfully.", collection.getId()));
        }

        do {
            try
            {
                Thread.currentThread().sleep(1000);
            }
            catch (InterruptedException e) {
                System.err.println("Error: " + e.getMessage());
            }

            System.out.println("Retrieving your processed results...");
            //Retreives analysis results for queued collection
            result = session.getCollection(collectionId);
        } while (result.getStatus() == TaskStatus.QUEUED);

        System.out.println();

        //Prints analysis results
        for (Facet facet : result.getFacets())
        {
            System.out.println(String.format("%s : %s", facet.getLabel(), facet.getCount()));
            if (facet.getAttributes() == null)
                continue;

            for (Attribute attr : facet.getAttributes())
            {
                System.out.println(String.format("\t%s : %s", attr.getLabel(), attr.getCount()));
            }
        }

	}
}


