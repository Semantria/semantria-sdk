package com.semantria.example;

import com.google.common.base.Strings;
import com.semantria.Session;
import com.semantria.auth.CredentialException;
import com.semantria.mapping.Collection;
import com.semantria.mapping.output.Attribute;
import com.semantria.mapping.output.CollAnalyticData;
import com.semantria.mapping.output.Facet;

import java.util.List;
import java.util.UUID;

/**
 * Example application that shows using the Semantria API to run
 * discovery analysis on documents from a file.
 * <p>
 * Note that this is intended as a simple example of how to use the Java
 * SDK. It is <b>not</b> intended to show the best practice in building
 * a real application. Please contact Lexalytics for guidance, if you
 * plan to build your own application.
 */
public class DiscoveryModeTestApp {

    public static void main(String args[]) throws CredentialException {

        String filename = "bellagio-data-100.utf8";
        String key = System.getenv("SEMANTRIA_KEY");
        String secret = System.getenv("SEMANTRIA_SECRET");

        if (args.length > 0) {
            if (args[0].equals("--help") || args[0].equals("-h")) {
                usage();
            }
            filename = args[0];
        }
        if (args.length > 1) {
            key = args[1];
        }
        if (args.length > 2) {
            secret = args[2];
        }

        if (Strings.isNullOrEmpty(key) || Strings.isNullOrEmpty(secret)) {
            System.err.println("Error: Missing key and/or secret\n");
            usage();
        }

        System.out.format("Reading %s ...\n", filename);

        List<String> data = Utils.readTextFile(filename);
        if (data.isEmpty()) {
            System.err.format("Data file, %s, is empty or missing", filename);
            usage();
        }

        DiscoveryModeTestApp app = new DiscoveryModeTestApp(key, secret);
        app.processDocs(data);
    }

    private static void usage() {
        System.out.format("\nDiscoveryModeTestApp [<file> [<key> <secret>]].\n\n");
        System.out.format("This example reads data from a file, one document per line,\n");
        System.out.format("processes it through the Semantria API, and prints some of\n");
        System.out.format("the results.\n\n");
        System.out.format("Arguments:\n");
        System.out.format("  <file>     name of datafile to read (default: source.txt)\n");
        System.out.format("  <key>      Semantria API key (default: value of SEMANTRIA_KEY environment variable)\n");
        System.out.format("  <secret>   Semantria API secret (default: value of SEMANTRIA_SECRET environment variable)\n");
        System.exit(0);
    }


    /** How long to wait between poll requests to Semantria API in seconds */
    private static final int DELAY_BEFORE_GETTING_RESPONSE = 2;

    /** The Semantria session. Used to communicate with the semantria service */
    private Session session = null;
    /** Result(s) of collection analysis */
    List<CollAnalyticData> analysisResults = null;

    public DiscoveryModeTestApp(String key, String secret) {
        session = new Session().withKey(key).withSecret(secret)
			.withCallbackHandler(new CallbackHandler());
    }

    public void processDocs(List<String> data) throws CredentialException {
        sendDocs(data);
        pollForResults();
        printResults();
    }

    private void sendDocs(List<String> data) throws CredentialException {
        final String collectionId = UUID.randomUUID().toString();
        Collection collection = new Collection(collectionId);
        collection.setDocuments(data);

        // Queue collection for analysis using default configuration
        int status = session.queueCollection(collection, null);
        if (status < 300) {
            System.out.format("\"%s\" collection queued successfully.\n", collection.getId());
        } else {
            System.err.format("Error: queueing collection failed. Status = %d", status);
            System.exit(1);
        }
    }

    private void pollForResults() throws CredentialException {
        System.out.println();
        do {
            Utils.sleep(DELAY_BEFORE_GETTING_RESPONSE);
            System.out.println("Retrieving your processed results...");
            analysisResults = session.getProcessedCollections(null);
        } while (analysisResults.isEmpty());

        System.out.println();
    }

    private void printResults() {
        for (CollAnalyticData result : analysisResults) {
            for (Facet facet : result.getFacets()) {
                // There is much more that you can get from your analysis results.
                // This is just a brief example.
                System.out.format("%s \t pos: %d, neg: %d, neutral: %d\n",
                        facet.getLabel(), facet.getCount(),
                        facet.getNegativeCount(), facet.getNeutralCount());
                if (facet.getAttributes() == null)
                    continue;
                for (Attribute attr : facet.getAttributes()) {
                    System.out.format("\t%s \t %s\n", attr.getLabel(), attr.getCount());
                }
            }
        }
    }


}


