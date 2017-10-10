package com.semantria.example;

import com.google.common.base.Strings;
import com.semantria.Session;
import com.semantria.auth.CredentialException;
import com.semantria.mapping.Document;
import com.semantria.mapping.output.DocAnalyticData;
import com.semantria.mapping.output.DocCategory;
import com.semantria.mapping.output.DocEntity;
import com.semantria.mapping.output.DocTheme;
import com.semantria.mapping.output.DocTopic;
import com.semantria.mapping.output.MentionPhrase;
import com.semantria.mapping.output.SentimentMentionPhrase;
import com.semantria.mapping.output.Subscription;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Example application that shows using the Semantria API to run
 * detailed analysis on documents from a file.
 * <p>
 * Note that this is intended as a simple example of how to use the Java
 * SDK. It is <b>not</b> intended to show the best practice in building
 * a real application. Please contact Lexalytics for guidance, if you
 * plan to build your own application.
 */
public class DetailedModeTestApp {

    public static void main(String args[]) throws CredentialException {
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

        DetailedModeTestApp app = new DetailedModeTestApp(key, secret);
        app.processDocs(data);
    }

    private static void usage() {
        System.out.format("\nDetailedModeTestApp [<file> [<key> <secret>]].\n\n");
        System.out.format("This example reads data from a file, one document per line,\n");
        System.out.format("processes it through the Semantria API, and prints some of\n");
        System.out.format("the results.\n\n");
        System.out.format("Arguments:\n");
        System.out.format("  <file>     name of datafile to read (default: source.txt)\n");
        System.out.format("  <key>      Semantria API key (default: value of SEMANTRIA_KEY environment variable)\n");
        System.out.format("  <secret>   Semantria API secret (default: value of SEMANTRIA_SECRET environment variable)\n");
        System.exit(0);
    }


    // How long to wait between poll requests to Semantria API in seconds
    private static final int DELAY_BEFORE_GETTING_RESPONSE = 2;

    // The Semantria session. Used to communicate with the Semantria api
    Session session = null;

    // Map of doc id to analysis status, used to keep track of docs sent
    Map<String, DocStatus> docsTracker = new Hashtable<String, DocStatus>();

    // Results of doc analysis
    List<DocAnalyticData> analysisResults = new ArrayList<DocAnalyticData>(100);


    public DetailedModeTestApp(String key, String secret) {
        session = new Session().withKey(key).withSecret(secret)
			.withCallbackHandler(new CallbackHandler());
    }

    void processDocs(List<String> data) throws CredentialException {
        sendDocs(data);
        if (! (docsTracker.containsValue(DocStatus.QUEUED) || docsTracker.containsValue(DocStatus.PROCESSED))) {
            System.out.format("All docs failed!\n");
            return;
        }
        pollForResults();
        printResults();
    }

    private void sendDocs(List<String> data) throws CredentialException {
        int batchSize = getBatchSize();
        List<Document> outgoingBatch = new ArrayList<Document>(batchSize);

        for (String text : data) {
            String uid = UUID.randomUUID().toString();
            Document doc = new Document(uid, text);
            doc.setMetadata(String.format("[{\"size\": %d, \"color\": \"blue\"}, \"test-string\"]", text.length()));
            outgoingBatch.add(doc);
            if (outgoingBatch.size() == batchSize) {
                queueBatch(outgoingBatch);
            }
        }

        // Send last partial batch
        if (outgoingBatch.size() > 0) {
            queueBatch(outgoingBatch);
        }
    }

    int getBatchSize() throws CredentialException {
        Subscription subscription = session.getSubscription();
        if (subscription == null) {
            System.err.format("Error: Can't get subscription. HTTP status: %d; Error message: %s\n",
                    session.getLastRequestStatus(), session.getLastRequestErrorMessage());
            System.exit(1);
        }
        return subscription.getBasicSettings().getIncomingBatchLimit();
    }

    private void pollForResults() {
        System.out.println();
        try {
            while (docsTracker.containsValue(DocStatus.QUEUED)) {
                // As Semantria isn't a real-time solution you need to wait some time before getting of the processed results
                // A real application would likely be implemented as two separate jobs, one for queuing source data another one for retrieving results.
                // Do not poll repeatedly without a wait. You will exceed you polling limit.
                Utils.sleep(DELAY_BEFORE_GETTING_RESPONSE);

                System.out.println("Retrieving your processed results...");
                // Requests processed results from Semantria service
                List<DocAnalyticData> processedDocs = getProcessedDocuments();
                System.out.format("Received %d docs from polling\n", processedDocs.size());
                addAnalysisResults(processedDocs);
            }
        } catch (Exception e) {
            System.err.format("Error while polling for results: %s\n", e.toString());
        }
    }

    void addAnalysisResults(Collection<DocAnalyticData> results) {
        for (DocAnalyticData item : results) {
            addAnalysisResult(item);
        }
    }

    void addAnalysisResult(DocAnalyticData item) {
        if (docsTracker.containsKey(item.getId())) {
            analysisResults.add(item);
            docsTracker.put(item.getId(), DocStatus.PROCESSED);
        } else {
            // If another job sends docs to the same configuration at the same time you may get result from the other job
            // See the job id feature for how to keep results separate if you need to do that.
            System.out.format("Unexpected result received - ignored. doc id = %s\n", item.getId());
        }
    }

    private void printResults() {
        // There is a huge amount of info that you can get from your analysis.
        // This is just a small selection.
        for (DocAnalyticData doc : analysisResults) {
            System.out.format("---- Document %s -----"
                            + "\nsentiment score: %.2f"
                            + "\nsentiment polarity: %s\n",
                    doc.getId(), doc.getSentimentScore(), doc.getSentimentPolarity());
            showMetaData(doc);
            showEntities(doc);
            showQueryTopics(doc);
            showThemes(doc);
            showAutoCategories(doc);
        }
    }

    private void showEntities(DocAnalyticData doc) {
        if (doc.getEntities() != null) {
            System.out.println("entities:");
            for (DocEntity entity : doc.getEntities()) {
                System.out.format("\ttitle: %s"
                                + "\n\tsentiment: %.2f"
                                + "\n\tsentiment polarity: %s\n\n",
                        entity.getTitle(), entity.getSentimentScore(),
                        entity.getSentimentPolarity());
            }
        }
    }

    private void showThemes(DocAnalyticData doc) {
        if (doc.getThemes() != null) {
            System.out.println("document themes:");
            for (DocTheme theme : doc.getThemes()) {
                System.out.format("\ttitle: %s"
                                + "\n\tsentiment: %.2f"
                                + "\n\tsentiment polarity: %s\n\n",
                        theme.getTitle(), theme.getSentimentScore(),
                        theme.getSentimentPolarity());
            }
        }
    }

    private void showQueryTopics(DocAnalyticData doc) {
        if (doc.getTopics() == null) {
            return;
        }
        System.out.println("query topics:");
        for (DocTopic topic : doc.getTopics()) {
            System.out.format("\ttopic: %s"
                            + "\n\tSentiment score: %.2f\n",
                    topic.getTitle(), topic.getSentimentScore());
            if ((topic.getSentimentPhrases() != null) && !topic.getSentimentPhrases().isEmpty()) {
                System.out.format("\tSentiment phrases:\n");
                for (SentimentMentionPhrase phrase : topic.getSentimentPhrases()) {
                    System.out.format("\t\t%s    type: %d, modified: %d, sentiment: %.2f\n",
                            phrase.getTitle(), phrase.getType(), phrase.getModified(), phrase.getSentimentScore());
                    showSentimentMentionPhrase(phrase.getPhrase(), "");
                    if (phrase.getSupportingPhrases() != null) {
                        for (MentionPhrase sp : phrase.getSupportingPhrases()) {
                            showSentimentMentionPhrase(sp, "[supporting]");
                        }
                    }
                }
            }
        }
        System.out.println();
    }

    private void showSentimentMentionPhrase(MentionPhrase phrase, String prefix) {
        System.out.format("\t\t\t%s%s  type: %d, negated: %b, negator: %s, word: %d:%d, byte: %d:%d, %d/%d/%d\n",
                prefix, phrase.getTitle(), phrase.getType(), phrase.isNegated(), phrase.getNegator(),
                phrase.getWord(), phrase.getLength(), phrase.getByteOffset(), phrase.getByteLength(),
                phrase.getDocument(), phrase.getSection(), phrase.getSentence());
    }

    private void showAutoCategories(DocAnalyticData doc) {
        if (doc.getAutoCategories() != null) {
            System.out.println("\tdocument categories:");
            for (DocCategory category : doc.getAutoCategories()) {
                System.out.format("\ttopic: %s"
                                + "\n\tStrength score: %.2f\n\n",
                        category.getTitle(), category.getStrengthScore());
            }
        }
    }

    private void showMetaData(DocAnalyticData doc) {
        if (doc.getMetadata() != null) {
            System.out.format("\tmetadata: %s\n", doc.getMetadata().toString());
        }
        System.out.println();
    }

    List<DocAnalyticData> getProcessedDocuments() throws CredentialException {
        return getProcessedDocuments(null);
    }

    List<DocAnalyticData> getProcessedDocuments(String configId) throws CredentialException {
        return session.getProcessedDocuments(configId);
    }

    void queueBatch(List<Document> batch) throws CredentialException {
        queueBatch(batch, null);
    }

    void queueBatch(List<Document> batch, String configId) throws CredentialException {
		for (Document doc : batch) {
			docsTracker.put(doc.getId(), DocStatus.QUEUED);
		}
        int status = session.QueueBatchOfDocuments(batch, configId);
        if (status < 300) {
            System.out.format("%d documents queued successfully\n", batch.size());
        } else {
            System.err.format("Error: Queuing %d docs failed: HTTP Status: %d; Error message: %s\n",
                    batch.size(), status, session.getLastRequestErrorMessage());
			for (Document doc : batch) {
				docsTracker.put(doc.getId(), DocStatus.ERROR);
			}
        }
		batch.clear();
    }

	enum DocStatus {
		ERROR, QUEUED, PROCESSED
	}


}
