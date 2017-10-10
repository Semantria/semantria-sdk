package com.semantria.interfaces;

import com.semantria.utils.RequestArgs;
import com.semantria.utils.ResponseArgs;
import com.semantria.mapping.output.CollAnalyticData;
import com.semantria.mapping.output.DocAnalyticData;

import java.util.List;

/**
 * ICallbackHaandler interface that defines method executed by Session object in case of corresponding events.
 */
public interface ICallbackHandler
{
	/**
	 * Occurs when request executed.
	 * @param sender An object that fires event, particularly Session.
	 * @param responseArgs Event arguments.
     */
	void onResponse(Object sender, ResponseArgs responseArgs);

	/**
	 * Occurs when response received.
	 * @param sender An object that fires event, particularly Session.
	 * @param requestArgs Event arguments.
     */
	void onRequest(Object sender, RequestArgs requestArgs);

	/**
	 * Occurs when server-side error reported.
	 * @param sender An object that fires event, particularly Session.
	 * @param errorArgs Event arguments.
     */
	void onError(Object sender, ResponseArgs errorArgs);

	/**
	 * Occurs when the server return documents analysis data in the response of data queuing requests (auto-response feature).
	 * @param sender An object that fires event, particularly Session.
	 * @param processedData Event arguments.
     */
	void onDocsAutoResponse(Object sender, List<DocAnalyticData> processedData);

	/**
	 * Occurs when the server return collections analysis data in the response of data queuing requests (auto-response feature).
	 * @param sender An object that fires event, particularly Session.
	 * @param processedData Event arguments.
     */
	void onCollsAutoResponse(Object sender, List<CollAnalyticData> processedData);

}
