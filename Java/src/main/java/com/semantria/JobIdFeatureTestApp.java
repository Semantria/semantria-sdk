package com.semantria;

import com.semantria.interfaces.ISerializer;
import com.semantria.mapping.Document;
import com.semantria.mapping.output.DocAnalyticData;
import com.semantria.serializer.JsonSerializer;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;

public class JobIdFeatureTestApp {
    private static final int TIMEOUT_BEFORE_GETTING_RESPONSE = 10000; //in millisec

    public static void main(String args[]) {
        final String key = "";
        final String secret = "";
        List<String> initialTexts = new ArrayList<String>();

        System.out.println("Semantria service demo.");

        File file = new File( DetailedModeTestApp.class.getProtectionDomain().getCodeSource().getLocation().toString().replace("/target/classes", "/src/main/java/" + JobIdFeatureTestApp.class.getPackage().getName().replace(".","/")).replace("file:/", "")+ "/source.txt");

        if( !file.exists() ) {
            System.out.println("Source file isn't available.");
            return;
        }

        //Reads dataset from the source file
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

                initialTexts.add(strLine);
            }
            in.close();
        }
        catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }

        ISerializer serializer = new JsonSerializer();
        Session session = Session.createSession(key, secret, serializer, true);
        session.setCallbackHandler(new CallbackHandler());

        // null - send every single document separately
        // false - send uniqueJobIdCount batches
        // true - send all documents in single batch
        Boolean dataSendingMode = true;
        int uniqueJobIdCount = 4;
        Map<Integer, Integer> jobIds = new HashMap<Integer, Integer>();
        Map<Integer, List<Document>> documents = new HashMap<Integer, List<Document>>();
        Random randomGenerator = new Random();
        int max = 10000000;
        int min = 0;

        //Generates N unique jobId values
        for (int index = 0; index < uniqueJobIdCount; index++) {
            Integer id = randomGenerator.nextInt((max - min) + 1) + min;

            jobIds.put(id, 0);
            documents.put(id, new ArrayList<Document>());
        }

        List<Integer> jobIdsKeys = new ArrayList<Integer>(jobIds.keySet());
        for (int i = 0; i < initialTexts.size(); i++) {
            String docText = initialTexts.get(i);

            // Creates a sample document which need to be processed on Semantria
            Integer jobId = jobIdsKeys.get(randomGenerator.nextInt(jobIds.size()));

            jobIds.put(jobId, jobIds.get(jobId) + 1);
            List<Document> docs = documents.get(jobId);
            String docId = String.valueOf(randomGenerator.nextInt((max - min) + 1) + min);
            Document doc = new Document(docId, docText);
            doc.setJobId(String.valueOf(jobId));
            docs.add(doc);
        }

        System.out.println("Queues document for processing on Semantria service...");
        for (Map.Entry<Integer, Integer> entry : jobIds.entrySet())  {
            System.out.println(entry.getValue() + " documents for " + entry.getKey() + " jobId");
        }

        if (dataSendingMode == null) {
            for (Map.Entry<Integer, List<Document>> entry : documents.entrySet())  {
                List<Document> docsForJob = entry.getValue();
                for (Document doc : docsForJob) {
                    // Queues document for processing on Semantria service
                    session.queueDocument(doc);
                }
                System.out.println(docsForJob.size() + " documents queued for " + entry.getKey() + " job ID");
            }
        } else if (!dataSendingMode) {
            for (Map.Entry<Integer, List<Document>> entry : documents.entrySet())  {
                // Queues document for processing on Semantria service
                session.queueBatch(entry.getValue());
                System.out.println(entry.getValue().size() + " documents queued for " + entry.getKey() + " job ID");
            }
        } else {
            List<Document> batch = new ArrayList<Document>();

            for (Map.Entry<Integer, List<Document>> entry : documents.entrySet())  {
                batch.addAll(entry.getValue());
            }
            session.queueBatch(batch);
            System.out.println(batch.size() + " documents queued in single batch");
        }


        // Receive response
        boolean allJobIdsProcessed;
        try {
            do {
                Thread.currentThread().sleep(TIMEOUT_BEFORE_GETTING_RESPONSE);

                System.out.println("\nRetrieving processed results...");
                allJobIdsProcessed = true;

                // Requests processed results from Semantria service
                for (Map.Entry<Integer, Integer> entry : jobIds.entrySet()) {
                    int count = 0;

                    if (entry.getValue() > 0) {
                        allJobIdsProcessed = false;
                        List<DocAnalyticData> processedDocuments = session.getProcessedDocumentsByJobId(String.valueOf(entry.getKey()));
                        if (processedDocuments.size() > 0) {
                            jobIds.put(entry.getKey(), entry.getValue() - processedDocuments.size());
                            count += processedDocuments.size();
                        }

                        if (count > 0) {
                            System.out.println(count + " documents received for " + entry.getKey() + " job ID");
                        }
                    }
                }

                if (allJobIdsProcessed) {
                    System.out.println("\nDone. All documents received.");
                }
            } while (!allJobIdsProcessed);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
