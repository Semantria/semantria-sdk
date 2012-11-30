public abstract class CallbackHandler {
	public abstract void onResponse(Object sender, ResponseArgs responseArgs);
	public abstract void onRequest(Object sender, RequestArgs requestArgs);  
	public abstract void onError(Object sender, ResponseArgs errorArgs);
	public abstract void onDocsAutoResponse(Object sender, List<DocAnalyticData> processedData);
	public abstract void onCollsAutoResponse(Object sender, List<CollAnalyticData> processedData);
}