package com.semantria.example;

import com.semantria.interfaces.ICallbackHandler;
import com.semantria.mapping.output.CollAnalyticData;
import com.semantria.mapping.output.DocAnalyticData;
import com.semantria.utils.RequestArgs;
import com.semantria.utils.ResponseArgs;

import java.util.List;

/**
 * Example callback handler. The callback handler has several roles including:
 * <ul>
 *     <li>Handling analysis errors</li>
 *     <li>Handling results returned via AutoResponse</li>
 *     <li>Debugging your app by using the onResponse() and onRequest() methods
 *     to see your raw outgoing data and incoming results</li>
 * </ul>
 * <p>
 * Note, that in a real application you would want to do more than simply print
 * that an error has occurred!
 */
public class CallbackHandler implements ICallbackHandler {
	// Place to store results received via AutoResponse
	final List<DocAnalyticData> analysed_docs;
	final List<CollAnalyticData> analysed_collections;

	public CallbackHandler() {
		this(null, null);
	}

	/**
	 * This constructor provides a place to store analysis results
	 * returned via auto response. In this case it's a simple list. In a
	 * real application be sure that adding results is done in a thread
	 * safe manner.
	 */
	public CallbackHandler(List<DocAnalyticData> analysed_docs, List<CollAnalyticData> analysed_collections) {
		this.analysed_docs = analysed_docs;
		this.analysed_collections = analysed_collections;
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
		if (analysed_docs != null) {
			analysed_docs.addAll(processedData);
		}
		for (DocAnalyticData aData : processedData) {
			System.out.format("AutoResponse result: %s = %s\n",	aData.getId(), aData.getStatus());
		}
	}

	@Override
	public void onCollsAutoResponse(Object sender, List<CollAnalyticData> processedData) {
		// Save analytis results, if a place is provided, and also print message
		if (analysed_collections != null) {
			analysed_collections.addAll(processedData);
		}
		for (CollAnalyticData aData : processedData) {
			System.out.format("AutoResponse result: %s = %s\n", aData.getId(), aData.getStatus());
		}
	}

}
