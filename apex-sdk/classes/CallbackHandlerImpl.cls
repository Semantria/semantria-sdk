public class CallbackHandlerImpl extends CallbackHandler {
	public override void onResponse(Object sender, ResponseArgs responseArgs) {
		System.debug(Logginglevel.ERROR, 'rawResponse = ' + responseArgs.getMessage());
	}

	public override void onRequest(Object sender, RequestArgs requestArgs) {
		System.debug(Logginglevel.ERROR, 'requestUrl = ' + requestArgs.getUrl());
		System.debug(Logginglevel.ERROR, 'rawRequest = ' + requestArgs.getMessage());
	}

	public override void onError(Object sender, ResponseArgs errorArgs) {
		System.debug(Logginglevel.ERROR, 'HTTP status ' + String.valueOf(errorArgs.getStatus()) + ' error message: ' + errorArgs.getMessage());
	}

	public override void onDocsAutoResponse(Object sender, List<DocAnalyticData> processedData) {
		for (DocAnalyticData aData : processedData) {
            System.debug(aData.getId() + ' = ' + aData.getStatus());
        }
	}
	
    public override void onCollsAutoResponse(Object sender, List<CollAnalyticData> processedData) {
    	for (CollAnalyticData aData : processedData) {
            System.debug(aData.getId() + ' = ' + aData.getStatus());
        }
    }
    
    static testMethod void testCallbackHandlerFunctions() {
    	CallbackHandler handler = new CallbackHandlerImpl();
    	System.assert(null != handler);
    	try {
    		ResponseArgs responseDetails = new ResponseArgs(200, 'Success');
    		handler.onResponse(null, responseDetails);
    		
    		RequestArgs requestDetails = new RequestArgs('POST', 'http://habrahabr.ru/', 'Success');
            handler.onRequest(null, requestDetails);
            
            responseDetails = new ResponseArgs(null, null);
            responseDetails.setStatus(200);
            responseDetails.setMessage('Success');
            handler.onError(null, responseDetails);
            
            requestDetails = new RequestArgs(null, null, null);
            requestDetails.setMethod('POST');
            requestDetails.setUrl('http://habrahabr.ru/');
            requestDetails.setMessage('Success');
            handler.onRequest(null, requestDetails);
            
            List<DocAnalyticData> docAnalyticDataList = new List<DocAnalyticData>();
            DocAnalyticData docAnalyticData = new DocAnalyticData();
            docAnalyticData.setId('23456');
            docAnalyticData.setStatus('PROCESSED');
            docAnalyticDataList.add(docAnalyticData);
            handler.onDocsAutoResponse(null, docAnalyticDataList);
            
            List<CollAnalyticData> collAnalyticDataList = new List<CollAnalyticData>();
            CollAnalyticData collAnalyticData = new CollAnalyticData();
            collAnalyticData.setId('23456');
            collAnalyticData.setStatus('PROCESSED');
            collAnalyticDataList.add(collAnalyticData);
            handler.onCollsAutoResponse(null, collAnalyticDataList);
    	} catch (Exception ex) {
    		System.debug(Logginglevel.ERROR, 'Exception: ' + ex.getStackTraceString());
    		System.assert(false);
    	}
    }
}