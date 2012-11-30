package com.semantria.interfaces;

import java.util.List;

import com.semantria.objects.output.CollAnalyticData;
import com.semantria.objects.output.DocAnalyticData;
import com.semantria.objects.RequestArgs;
import com.semantria.objects.ResponseArgs;

public interface ICallbackHandler
{
	public void onResponse(Object sender, ResponseArgs responseArgs);
	public void onRequest(Object sender, RequestArgs requestArgs);
	public void onError(Object sender, ResponseArgs errorArgs);
	public void onDocsAutoResponse(Object sender, List<DocAnalyticData> processedData);
	public void onCollsAutoResponse(Object sender, List<CollAnalyticData> processedData);

}
