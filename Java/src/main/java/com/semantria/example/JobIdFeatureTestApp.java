package com.semantria.example;

import com.google.common.base.Strings;
import com.semantria.Session;
import com.semantria.auth.CredentialException;
import com.semantria.mapping.Document;
import com.semantria.mapping.output.DocAnalyticData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Example application that shows using the job id feature to separate
 * results for different applications using the same config.
 * <p/>
 * Jobid id is not intended to be used as a general routing key or
 * annotation. See the tag and metadata features which can are more
 * general.
 * <p/>
 * Be aware that if you run this program more than once, and an error
 * occurs the first run, then there may be unretrieved analysis results
 * that will be retrieved by the second run. This may then give
 * unexpected document counts. This is because the job ids are reused
 * from one run to the next: "job-1", "job-2", ...
 * <p/>
 * Note that this is a very contrived example which shows the mechanism
 * for using job id. It is <b>not</b> intended to show the best practice
 * in building a real application. Please contact Lexalytics for
 * guidance, if you plan to build your own application.
 * <p/>
 * This example creates several "worker" threads, each with a unique job id.
 * Each worker represents a separate application.
 */

public class JobIdFeatureTestApp {

    public static void main(String args[]) throws InterruptedException {
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

        JobIdFeatureTestApp app = new JobIdFeatureTestApp(key, secret);
        app.processDocs(data);
    }

    private static void usage() {
        System.out.format("\nJobIdFeatureTestApp [<file> [<key> <secret>]].\n\n");
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

    int BATCH_SIZE = 50;                // Max number of docs to send in one batch
	final int jobIdCount = 4;			// number of different job ids to use
	Random rand = new Random();

	List<Worker> workers = new ArrayList<Worker>(jobIdCount);

    public JobIdFeatureTestApp(String key, String secret) throws InterruptedException {
        for (int i = 1; i <= jobIdCount; ++i) {
			String jobId = String.format("job-%d", i);
			workers.add(new Worker(key, secret, jobId));
		}
    }

    private void processDocs(List<String> data) throws InterruptedException {
        for (String text : data) {
            Worker w = workers.get(rand.nextInt(jobIdCount));
            w.addDocument(text);
        }
        ExecutorService executor = Executors.newFixedThreadPool(jobIdCount);
        executor.invokeAll(workers);
        executor.shutdown();
    }

	public class Worker implements Callable<Object> {
		
		Session session = null;
		String jobId;
        List<Document> documents = new ArrayList<Document>();
		List<DocAnalyticData> analysisResults = new ArrayList<DocAnalyticData>( );

		public Worker(String key, String secret, String jobId) {
			this.jobId = jobId;
			session = new Session().withKey(key).withSecret(secret)
				.withCallbackHandler(new CallbackHandler());
		}

		public Object call() throws CredentialException {
			sendDocs();
			pollForResults();
			printResults();
            return null;
        }

		public void addDocument(String text) {
            documents.add(createDocWithJobId(text));
		}

        Document createDocWithJobId(String text) {
            String docId = UUID.randomUUID().toString();
            Document doc = new Document(docId, text);
            doc.setJobId(jobId);
            return doc;
        }

		private void sendDocs() throws CredentialException {
            int limit = documents.size();
            for (int start = 0; start < limit; start += BATCH_SIZE) {
                List<Document> outgoingBatch = documents.subList(start, Math.min(start + BATCH_SIZE, limit));
                session.QueueBatchOfDocuments(outgoingBatch, null);
            }
            System.out.format("%s: queued total %d docs\n", jobId, limit);
        }

		private void pollForResults() throws CredentialException {
            while (documents.size() > analysisResults.size()) {
                Utils.sleep(DELAY_BEFORE_GETTING_RESPONSE);
                List<DocAnalyticData> processedDocs = session.getProcessedDocumentsByJobId(jobId);
                System.out.format("%s: received %d docs\n", jobId, processedDocs.size());
                addAnalysisResults(processedDocs);
            }
            System.out.format("%s: received total %d docs\n", jobId, analysisResults.size());
		}

		void addAnalysisResults(Collection<DocAnalyticData> results) {
			for (DocAnalyticData item : results) {
				analysisResults.add(item);
			}
		}

		private void printResults() {
            System.out.format("Results for %s:\n", jobId);
            for (DocAnalyticData doc : analysisResults) {
                System.out.format("  %s\n", doc.getId());
            }
        }

    }

}
