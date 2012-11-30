public class AnalyticServiceRequestHelper {
	public static final Integer REQUEST_TIMEOUT = 25000; // in milliseconds = 25 sec  
	
    public static final String NOT_AUTHORIZED_USER_MESSAGE = 'Incorrect credentials.';
    public static final String WRONG_REQUEST_FORMAT_MESSAGE = 'Wrong request format. Contact your administrator.';
    public static final String ENDPOINT_DOESNT_EXIST_MESSAGE = 'Requested end-point does not exist.';
     
    private static final String XML_CONTENT_TYPE = 'application/xml';
    private static final String JSON_CONTENT_TYPE = 'application/json';
    
    private String requestFormat;
    private String requestFormatSuffix;
    private String contentType;
    
	private AuthHelper authHelperObj;
	private String responseBody;
	private String currentUrl;
	private String requestMethod;
	private Integer requestStatusCode;
	
	public AnalyticServiceRequestHelper(String key, String secret, String requestFormat, String configId, String applicationName) {
		this.requestFormat = requestFormat;
		defineRequestFormatSuffix();
		defineContentType();
		this.authHelperObj = new AuthHelper(key, secret, getConfigIdAsParametersMap(configId), applicationName);
	}
	
	private void defineRequestFormatSuffix() {
		requestFormatSuffix = '.' + requestFormat;
	}
	
	private void defineContentType() {
		if ( 'xml'.equalsIgnoreCase(requestFormat) ) {
            this.contentType = XML_CONTENT_TYPE;
        } else if ( 'json'.equalsIgnoreCase(requestFormat) ) {  
            this.contentType = JSON_CONTENT_TYPE;
        } else {
            throw new IllegalStateException('Unsupported request format: ' + requestFormat);
        }
	}
	
	private Map<String, String> getConfigIdAsParametersMap(String configId) {
		Map<String, String> params = new Map<String, String>();
		
		if ( null != configId) {
            params.put('config_id', configId);
        } 
        
        return params;
	}
	
    public Integer doRequest(String url, String requestMethod) {
    	return doRequest(url, requestMethod, null);
    }
    
    public Integer doRequest(String url, String requestMethod, String requestBody) {
    	this.currentUrl = url;
    	this.requestMethod = requestMethod;
    	//System.debug(Logginglevel.ERROR, 'REQUEST=>' + requestBody); 
    	Http http = new Http();
        HttpRequest req = authHelperObj.getSignedRequest(currentUrl, requestMethod, contentType);
        if (null != requestBody) {
            req.setBody(requestBody);
        }
        return processResponse(http.send(req));
    }
    
    private Integer processResponse(HTTPResponse res) {
    	responseBody = res.getBody();
    	//System.debug(Logginglevel.ERROR, 'RESPONSE=>' + responseBody);
        requestStatusCode = res.getStatusCode();
        return requestStatusCode;
    } 
    
    public String getResponseBody() {
    	return responseBody;
    }
    
    public String getRequestUrl() { 
    	return currentUrl;
    }
    
    public Integer getRequestStatusCode() {
    	return requestStatusCode;
    }
    
    public String getRequestMethod() {
    	return requestMethod;
    } 
    
    public Boolean isItQueueingDocumentRequest() {
    	return currentUrl.equals(Session.ANALYTIC_SERVICE_URL + 'document' + requestFormatSuffix);
    }
    
    public Boolean isItQueueingDocumentBatchRequest() {
        return currentUrl.equals(Session.ANALYTIC_SERVICE_URL + 'document/batch' + requestFormatSuffix);
    }
    
    public Boolean isItQueueingCollectionRequest() {
        return currentUrl.equals(Session.ANALYTIC_SERVICE_URL + 'collection' + requestFormatSuffix);
    }
    
    static testMethod void testReceivingSignedRequest() {
        try {
            AuthHelper authHelper = new AuthHelper('KEY', 'SECRET', null);
            authHelper.getSignedRequest('http://127.0.0.1:8080/batch/BATCH.json', 'POST', AnalyticServiceRequestHelper.XML_CONTENT_TYPE);
            System.assert(true);
        } catch ( Exception ex ) {
            System.assert(false);  
        } 
    }
    
	class AuthHelper {  
	    private final String CONTENT_TYPE_PARAM = 'Content-Type';
	    private final String HMAC_SHA1_METHOD = 'HMAC-SHA1';
	    
	    private String customerKey;
	    private String customerSecret;
	    private String applicationName;
	    private String serviceUrl;
	    private String signature;
	    private String method;
	    private String contentType;
	    private Map<String,String> userParameters;
	    
	    public AuthHelper(String customerKey, String customerSecret, String applicationName) {
			this(customerKey, customerSecret, null, applicationName);	    	
	    }
	    
	    public AuthHelper(String customerKey, String customerSecret, Map<String,String> userParameters, String applicationName) {
	        this.customerKey = customerKey;
	        this.customerSecret = customerSecret;
	        if ( null != userParameters ) {
	        	this.userParameters = userParameters;
	        } else {
	        	this.userParameters = new Map<String, String>();
	        }
	    }
	    
	    public HttpRequest getSignedRequest(String serviceUrl, String method, String contentType) {
	        checkConfigParameters();
	        try {
	            this.serviceUrl = serviceUrl;
	            this.method = method;
	            this.contentType = contentType;
	            return buildRequest();
	        } catch (Exception ex) { 
	            System.debug(Logginglevel.ERROR, 'Receiving signed request: ' + ex.getMessage() + '\nTrace: ' + ex.getStackTraceString());
	            return null;
	        }
	    }
	    
	    
	    private void checkConfigParameters() {
	        if ( null == customerKey || ''.equals(customerKey) 
	                || null == customerSecret || ''.equals(customerSecret) ) {
	            throw new IllegalStateException();
	        }
	    }
	    
	    
	    public HttpRequest buildRequest() {
	        HttpRequest req = new HttpRequest();
	        req.setEndpoint(serviceUrl);
	        req.setMethod(method);
	        req.setHeader(CONTENT_TYPE_PARAM, contentType);
	        req.setHeader('charset', 'utf-8');
	        req.setHeader('Connection', 'close');
	        req.setHeader('x-api-version', '2');
	        if (null != applicationName) {
	        	req.setHeader('x-app-name', applicationName);	
	        }
	        req.setTimeout(AnalyticServiceRequestHelper.REQUEST_TIMEOUT);
	        signRequest(req);
	        return req;
	    }
	    
	
	    private void signRequest(HttpRequest req) {
	        Map<String, String> parameters = refreshAndReturnParameters();
	
	        String s = createBaseString(parameters, req);
	        
	        Blob sig = Crypto.generateMac('hmacSHA1', Blob.valueOf(s), 
	                Blob.valueOf(EncodingUtil.convertToHex(Crypto.generateDigest('MD5', Blob.valueOf(customerSecret)))));
	        signature = EncodingUtil.urlEncode(EncodingUtil.base64encode(sig), 'UTF-8');
	        
	        String header = 'OAuth ';
	        for (String key : parameters.keySet()) {
	                header = header + key + '="'+parameters.get(key)+'", ';
	        }
	    
	        header = header + 'oauth_signature="'+signature+'"';
	        
	        req.setHeader('Authorization',header); 
	    }   
	    
	    
	    private Map<String, String> refreshAndReturnParameters() {
	    	Map<String, String> result = new Map<String, String>(this.userParameters);
	        result.put('oauth_consumer_key', customerKey);
	        result.put('oauth_signature_method', HMAC_SHA1_METHOD);
	        result.put('oauth_timestamp', String.valueOf(DateTime.now().getTime()/1000));
	        result.put('oauth_nonce', String.valueOf(Crypto.getRandomLong()));
	        result.put('app_name', Session.APPLICATION_NAME);
	        
	        return result;
	    }
	    
	    
	    private String createBaseString(Map<String,String> oauthParams, HttpRequest req) {
	        Map<String,String> p = oauthParams.clone();
	
	        String host = req.getEndpoint();
	        List<String> keys = new List<String>(p.keySet());
	
	        String s = keys.get(0) + '=' + p.get(keys.get(0));
	        for(Integer i = 1; i < keys.size(); ++i) {
	            s = s + '&' + keys.get(i) + '=' + p.get(keys.get(i));
	        }
	        
	        req.setEndpoint(host + '?' + s);
	
	        return EncodingUtil.urlEncode(host + '?', 'UTF-8')  + 
	               EncodingUtil.urlEncode(s, 'UTF-8');
	    }
	}
}