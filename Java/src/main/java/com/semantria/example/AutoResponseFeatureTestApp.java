package com.semantria.example;

import com.google.common.base.Strings;
import com.semantria.auth.CredentialException;
import com.semantria.interfaces.ICallbackHandler;
import com.semantria.mapping.Document;
import com.semantria.mapping.configuration.Configuration;
import com.semantria.mapping.output.CollAnalyticData;
import com.semantria.mapping.output.DocAnalyticData;
import com.semantria.utils.RequestArgs;
import com.semantria.utils.ResponseArgs;

import java.util.Arrays;
import java.util.List;

/**
 * Example application that shows using the auto response feature of the
 * Semantria API.
 * <p/>
 * This extends DetailedModeTestApp since most of the behavior is the
 * same. The only difference is that waiting analysis results may be
 * returned in the response body when sending more documents to process.
 * <p/>
 * This class is the application as well as the callback handler. This
 * makes it easy to handle both polling results and autoresponse results.
 * <p/>
 * Note that this is intended as a simple example of how to use the Java
 * SDK. It is <b>not</b> intended to show the best practice in building
 * a real application. Please contact Lexalytics for guidance, if you
 * plan to build your own application.
 */
public class AutoResponseFeatureTestApp extends DetailedModeTestApp implements ICallbackHandler {

    public static void main(String[] args) throws CredentialException {
        String filename = "source.txt";
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
            System.err.format("Data file, %s, is empty or missing\n", filename);
            usage();
        }

        AutoResponseFeatureTestApp app = new AutoResponseFeatureTestApp(key, secret);
        app.processDocs(data);
    }

    private static void usage() {
        System.out.format("\nAutoResponseFeatureTestApp [<file> [<key> <secret>]].\n\n");
        System.out.format("This example reads data from a file, one document per line,\n");
        System.out.format("processes it through the Semantria API, and prints some of\n");
        System.out.format("the results.\n\n");
        System.out.format("Arguments:\n");
        System.out.format("  <file>     name of datafile to read (default: source.txt)\n");
        System.out.format("  <key>      Semantria API key (default: value of SEMANTRIA_KEY environment variable)\n");
        System.out.format("  <secret>   Semantria API secret (default: value of SEMANTRIA_SECRET environment variable)\n");
        System.exit(0);
    }


    // autoResponseConfig with auto response option enabled
    Configuration autoResponseConfig = null;
    private static String AUTO_RESPONSE_TEST_CONFIG_NAME = "AutoResponseTest";

    public AutoResponseFeatureTestApp(String key, String secret) throws CredentialException {
        super(key, secret);
        session.withCallbackHandler(this);
        autoResponseConfig = getOrCreateAutoResponseConfig();
        System.out.format("Using autoresponse config id %s\n", autoResponseConfig.getId());
    }

    /**
     * Override getProcessdDocuments to use the autoresponse config rather than the primary config.
     */
    @Override
    List<DocAnalyticData> getProcessedDocuments() throws CredentialException {
        return getProcessedDocuments(autoResponseConfig.getId());
    }

    /**
     * Override queueBatch to use the autoresponse config rather than the primary config.
     */
    @Override
    void queueBatch(List<Document> batch) throws CredentialException {
        queueBatch(batch, autoResponseConfig.getId());
        // This is a little bit contrived, but we wait a bit after queueing.
        // This is so some analysis data will be ready when queueing later batches during the run of the
        // example application to demonstrate the auto response feature.
        Utils.sleep(.1f);
    }

    /**
     * Override batch size to so that there will be more than one batch sent.
     */
    @Override
    int getBatchSize() {
        return 10;
    }

    private Configuration getOrCreateAutoResponseConfig() throws CredentialException {
        Configuration config = getConfigByName(AUTO_RESPONSE_TEST_CONFIG_NAME);
        if (config != null) {
            return config;
        }
        return createAutoResponseConfig();
    }

    private Configuration getConfigByName(String name) throws CredentialException {
        for (Configuration config : session.getConfigurations()) {
            if (config.getName().equalsIgnoreCase(name)) {
                return config;
            }
        }
        return null;
    }


    Configuration createAutoResponseConfig() throws CredentialException {
        Configuration config = new Configuration();
        config.setName(AUTO_RESPONSE_TEST_CONFIG_NAME);
        config.setLanguage("English");
        config.setAutoResponse(true);

        List<Configuration> addedConfigs = session.addConfigurations(Arrays.asList(config));
        if (addedConfigs.isEmpty()) {
            System.err.println("Error creating autoresponse config");
            System.exit(1);
        }
        return addedConfigs.get(0);
    }


	@Override
	public void onResponse(Object sender, ResponseArgs responseArgs) {
		// The response is quite verbose so this is commented out by default.
		// Uncomment this to see the raw response data.
		//System.out.format("rawResponse = %s\n", responseArgs.getMessage());
	}

	@Override
	public void onRequest(Object sender, RequestArgs requestArgs) {
		// The request can be quite verbose so this is commented out by default.
		// Uncomment this to see the raw request data.
		//System.out.format("requestUrl[%s] = %s\n", requestArgs.getMethod(), requestArgs.getUrl());
		//System.out.format("rawRequest = %s\n", requestArgs.getMessage());
	}

	@Override
	public void onError(Object sender, ResponseArgs errorArgs) {
		System.err.format("ERROR: HTTP status: %d; message: %s\n",
				errorArgs.getStatus(), errorArgs.getMessage());
	}

	@Override
	public void onDocsAutoResponse(Object sender, List<DocAnalyticData> processedData) {
		// Save analytis results, if a place is provided, and also print message
        System.out.format("Received %d docs from autoresponse\n", processedData.size());
        addAnalysisResults(processedData);
	}

	@Override
	public void onCollsAutoResponse(Object sender, List<CollAnalyticData> processedData) {
		for (CollAnalyticData aData : processedData) {
			System.out.format("AutoResponse result: %s = %s\n", aData.getId(), aData.getStatus());
		}
	}

}
