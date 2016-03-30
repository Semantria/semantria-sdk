package com.semantria.interfaces;

import com.semantria.utils.RequestArgs;
import com.semantria.utils.ResponseArgs;
import com.semantria.mapping.output.CollAnalyticData;
import com.semantria.mapping.output.DocAnalyticData;

import java.util.List;

public interface ICallbackHandler
{
	public void onResponse(Object sender, ResponseArgs responseArgs);
	public void onRequest(Object sender, RequestArgs requestArgs);
	public void onError(Object sender, ResponseArgs errorArgs);
	public void onDocsAutoResponse(Object sender, List<DocAnalyticData> processedData);
	public void onCollsAutoResponse(Object sender, List<CollAnalyticData> processedData);

}
