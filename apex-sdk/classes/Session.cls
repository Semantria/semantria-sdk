public class Session {
	public static final String ANALYTIC_SERVICE_URL = 'https://api21.semantria.com/';
	
	private static final String POST_METHOD = 'POST';
    private static final String GET_METHOD = 'GET';
    private static final String DELETE_METHOD = 'DELETE';
    private static final String APPLICATION_NAME_SUFFIX = 'APEX';

    public static String APPLICATION_NAME;
    	
	private String key;
	private String secret;
	private String applicationName;  
	private Serializer serializerObj;
	private String requestFormat;
	private CallbackHandler callbackHandlerObj;
	
	private Session(String key, String secret, String applicationName) {
		this.key = key;
		this.secret = secret;
		if (null == applicationName || '' == applicationName) {
			this.applicationName = APPLICATION_NAME_SUFFIX;
		} else {
			this.applicationName = applicationName + '.' + APPLICATION_NAME_SUFFIX;
		}
	}
	
	private Session(String key, String secret, Serializer serializerObj, String applicationName) {
		this(key, secret, applicationName);
		this.serializerObj = serializerObj;
		this.requestFormat = serializerObj.getType();
	}
	
	public static Session createSession(String key, String secret) {
		return new Session(key, secret, null);
	}
	
	public static Session createSession(String key, String secret, String applicationName) {
		return new Session(key, secret, applicationName);
	}
	
	public static Session createSession(String key, String secret, Serializer serializerObj) {
		return new Session(key, secret, serializerObj, null);
	}
	
	public static Session createSession(String key, String secret, Serializer serializerObj, String applicationName) {
		return new Session(key, secret, serializerObj, applicationName);
	}
	
	public void registerSerializer(Serializer serializerObj) {
		this.serializerObj = serializerObj;
		this.requestFormat = serializerObj.getType();
	}
	
	public void setCallbackHandler(CallbackHandler callbackHandlerObj) {
		this.callbackHandlerObj = callbackHandlerObj;
	}
	
	public UpdateProxy createCategoriesUpdateProxy() {
		return new CategoryUpdateProxy();
	}
	 
	public UpdateProxy createQueriesUpdateProxy() {
		return new QueryUpdateProxy();
	}
	
	public UpdateProxy createBlacklistUpdateProxy() {
		return new BlacklistUpdateProxy();
	}
	
	public UpdateProxy createConfigurationsUpdateProxy() {
		return new ConfigurationUpdateProxy();
	}
	
	public UpdateProxy createEntitiesUpdateProxy() {
        return new UserEntityUpdateProxy();
    }
    
    public UpdateProxy createSentimentPhrasesUpdateProxy() {
        return new SentimentPhraseUpdateProxy();
    }
	 
	public ServiceStatus getStatus() {
        Object result = serializerObj.deserialize(getDataFromService(ANALYTIC_SERVICE_URL + 'status' + getRequestFormatSuffix(), null), 'ServiceStatus');
        if (null != result) {
            return (ServiceStatus)result;
        } else {
            return null;
        }
    }
    
    private String getRequestFormatSuffix() { 
        return '.' + requestFormat; 
    }
    
    private String getDataFromService(String url, String configId) {
    	AnalyticServiceRequestHelper requestHelper = new AnalyticServiceRequestHelper(key, secret, requestFormat, configId, applicationName);
        Integer requestStatusCode = requestHelper.doRequest(url, GET_METHOD);
        passDataToHandler(requestHelper);
        return requestHelper.getResponseBody();
    }
    
    private Integer deleteDataFromService(String url, String configId) {
        AnalyticServiceRequestHelper requestHelper = new AnalyticServiceRequestHelper(key, secret, requestFormat, configId, applicationName);
        Integer requestStatusCode = requestHelper.doRequest(url, DELETE_METHOD);
        passDataToHandler(requestHelper);
        return requestStatusCode;
    }
    
    private Integer pushDataToService(String url, String requestBody, String configId) {
        AnalyticServiceRequestHelper requestHelper = new AnalyticServiceRequestHelper(key, secret, requestFormat, configId, applicationName);
        Integer requestStatusCode = requestHelper.doRequest(url, POST_METHOD, requestBody);
        passDataToHandler(requestHelper);
        return requestStatusCode;
    }
    
    private void passDataToHandler(AnalyticServiceRequestHelper requestHelper) {
    	handleRequest(requestHelper);
        handleResponse(requestHelper);
    }
    
    private void handleResponse(AnalyticServiceRequestHelper requestHelper) {
		if (null != callbackHandlerObj) {
			Integer requestStatusCode = requestHelper.getRequestStatusCode();
			if (requestStatusCode <= 202) {
				if ( requestStatusCode == 200 ) {
					if (requestHelper.isItQueueingDocumentRequest() && requestHelper.isItQueueingDocumentBatchRequest()) {
	                	callbackHandlerObj.onDocsAutoResponse(this, (List<DocAnalyticData>)serializerObj.deserialize(requestHelper.getResponseBody(), 'DocAnalyticDataList'));
	                } else if (requestHelper.isItQueueingCollectionRequest()) {
	                	callbackHandlerObj.onCollsAutoResponse(this, (List<CollAnalyticData>)serializerObj.deserialize(requestHelper.getResponseBody(), 'CollAnalyticDataList'));
	                }
				}
			    callbackHandlerObj.onResponse(this, new ResponseArgs(requestStatusCode, requestHelper.getResponseBody()));
			} else if (requestStatusCode > 202) {
				callbackHandlerObj.onError(this, new ResponseArgs(requestStatusCode, requestHelper.getResponseBody()));
			}
		}
	}
	
	public void handleRequest(AnalyticServiceRequestHelper requestHelper) {
		if (null != callbackHandlerObj) {
			callbackHandlerObj.onRequest(this, new RequestArgs(requestHelper.getRequestMethod(), requestHelper.getRequestUrl(), requestHelper.getResponseBody()));
		}
	}
	
	public Subscription verifySubscription() {
        return verifySubscription(null);
	}
	
	public Subscription verifySubscription(String applicationName) {
        APPLICATION_NAME = (null != applicationName ? (applicationName + '.') : '') + APPLICATION_NAME_SUFFIX;
        
        Object result = serializerObj.deserialize(getDataFromService(ANALYTIC_SERVICE_URL + 'subscription' + getRequestFormatSuffix(), null), 'Subscription');
        if (null != result) {
            return (Subscription)result;
        } else {
            return null;
        }
    }
	
	public List<String> getBlacklist() {
		return getBlacklist(null);
	}
	
	public List<String> getBlacklist(String configId) {
        Object result = serializerObj.deserialize(getDataFromService(ANALYTIC_SERVICE_URL + 'blacklist' + getRequestFormatSuffix(), configId), 'BlacklistItem');
        if (null != result) {
            return (List<String>)result;
        } else {
            return new List<String>();
        }
	}
	
	public List<Category> getCategories() {
		return getCategories(null);
	}
	
    public List<Category> getCategories(String configId) {
        Object result = serializerObj.deserialize(getDataFromService(ANALYTIC_SERVICE_URL + 'categories' + getRequestFormatSuffix(), configId), 'Category');
        if (null != result) {
            return (List<Category>)result;
        } else {
            return new List<Category>();
        }
	}
	
    public List<Configuration> getConfigurations() {
        Object result = serializerObj.deserialize(getDataFromService(ANALYTIC_SERVICE_URL + 'configurations' + getRequestFormatSuffix(), null), 'Configuration');
        if (null != result) {
            return (List<Configuration>)result;
        } else {
            return new List<Configuration>();
        }
	}
	
	public List<Query> getQueries() {
		return getQueries(null);
	}
	
    public List<Query> getQueries(String configId) {
        Object result = serializerObj.deserialize(getDataFromService(ANALYTIC_SERVICE_URL + 'queries' + getRequestFormatSuffix(), configId), 'Query');
        if (null != result) {
            return (List<Query>)result;
        } else {
            return new List<Query>();
        }
	}
	
	public List<UserEntity> getEntities() {
		return getEntities(null);
	}
	
    public List<UserEntity> getEntities(String configId) {
        Object result = serializerObj.deserialize(getDataFromService(ANALYTIC_SERVICE_URL + 'entities' + getRequestFormatSuffix(), configId), 'UserEntity');
        if (null != result) {
            return (List<UserEntity>)result;
        } else {
            return new List<UserEntity>();
        }
	}
	
	public List<SentimentPhrase> getSentimentPhrases() {
        return getSentimentPhrases(null);
    }
    
    public List<SentimentPhrase> getSentimentPhrases(String configId) {
        Object result = serializerObj.deserialize(getDataFromService(ANALYTIC_SERVICE_URL + 'sentiment' + getRequestFormatSuffix(), configId), 'SentimentPhrase');
        if (null != result) {
            return (List<SentimentPhrase>)result;
        } else {
            return new List<SentimentPhrase>();
        }
    }
	
	public List<DocAnalyticData> getProcessedDocuments() {
		return getProcessedDocuments(null);
	}
	
    public List<DocAnalyticData> getProcessedDocuments(String configId) {
        Object result = serializerObj.deserialize(getDataFromService(ANALYTIC_SERVICE_URL + 'document/processed' + getRequestFormatSuffix(), 
                                                                        configId), 'DocAnalyticDataList');
        if (null != result) {
            return (List<DocAnalyticData>)result;
        } else {
            return new List<DocAnalyticData>();
        }
	}
	
	public List<CollAnalyticData> getProcessedCollections() {
        return getProcessedCollections(null);
    }
    
    public List<CollAnalyticData> getProcessedCollections(String configId) {
        Object result = serializerObj.deserialize(getDataFromService(ANALYTIC_SERVICE_URL + 'collection/processed' + getRequestFormatSuffix(), 
                                                                        configId), 'CollAnalyticDataList');
        if (null != result) {
            return (List<CollAnalyticData>)result;
        } else {
            return new List<CollAnalyticData>();
        }
    }
	
	public DocAnalyticData getDocument(String documentId) {
		return getDocument(documentId, null);
	}
	
    public DocAnalyticData getDocument(String documentId, String configId) {
        Object result = serializerObj.deserialize(getDataFromService(ANALYTIC_SERVICE_URL + 'document/' + documentId + getRequestFormatSuffix(), 
                                                                        configId), 'DocAnalyticData');
        if (null != result) {
            return (DocAnalyticData)result;
        } else {
            return null;
        }
	}
	
	public CollAnalyticData getCollection(String collectionId) {
		return getCollection(collectionId, null);
	}
	
    public CollAnalyticData getCollection(String collectionId, String configId) {
        Object result = serializerObj.deserialize(getDataFromService(ANALYTIC_SERVICE_URL + 'collection/' + collectionId + getRequestFormatSuffix(), 
                                                                        configId), 'CollAnalyticData');
        if (null != result) {
            return (CollAnalyticData)result;
        } else {
            return null;
        }
	}
	
	public Integer updateCategories(UpdateProxy updateProxy) {
		return updateCategories(updateProxy, null);
	}
	
	public Integer updateCategories(UpdateProxy updateProxy, String configId) {
		return pushDataToService(ANALYTIC_SERVICE_URL + 'categories' + getRequestFormatSuffix(), 
              serializerObj.serialize(updateProxy, 'Category'), configId); 
	}
	
	public Integer updateQueries(UpdateProxy updateProxy) {
		return updateQueries(updateProxy, null);
	}
	
	public Integer updateQueries(UpdateProxy updateProxy, String configId) {
		return pushDataToService(ANALYTIC_SERVICE_URL + 'queries' + getRequestFormatSuffix(), 
		      serializerObj.serialize(updateProxy, 'Query'), configId);
	}
	
	public Integer updateBlacklist(UpdateProxy updateProxy) {
		return updateBlacklist(updateProxy, null);
	}
	
	public Integer updateBlacklist(UpdateProxy updateProxy, String configId) {
		return pushDataToService(ANALYTIC_SERVICE_URL + 'blacklist' + getRequestFormatSuffix(), 
	          serializerObj.serialize(updateProxy, 'BlacklistItem'), configId);
	}
	
	public Integer updateEntities(UpdateProxy updateProxy) {
		return updateEntities(updateProxy, null);
	}
	
	public Integer updateEntities(UpdateProxy updateProxy, String configId) {
		return pushDataToService(ANALYTIC_SERVICE_URL + 'entities' + getRequestFormatSuffix(), 
		      serializerObj.serialize(updateProxy, 'UserEntity'), configId);
	}
	
	public Integer updateSentimentPhrases(UpdateProxy updateProxy) {
        return updateSentimentPhrases(updateProxy, null);
    }
    
    public Integer updateSentimentPhrases(UpdateProxy updateProxy, String configId) {
        return pushDataToService(ANALYTIC_SERVICE_URL + 'sentiment' + getRequestFormatSuffix(), 
              serializerObj.serialize(updateProxy, 'SentimentPhrase'), configId);
    }
	
	public Integer updateConfigurations(UpdateProxy updateProxy) {
		return updateConfigurations(updateProxy, null);
	}
	
	public Integer updateConfigurations(UpdateProxy updateProxy, String configId) {
		return pushDataToService(ANALYTIC_SERVICE_URL + 'configurations' + getRequestFormatSuffix(), 
		      serializerObj.serialize(updateProxy, 'Configuration'), configId);
	}
	
	public Integer queueDocument(Document doc) {
        return queueDocument(doc, null);
    }
    
    public Integer queueDocument(Document doc, String configId) {
    	return pushDataToService(ANALYTIC_SERVICE_URL + 'document' + getRequestFormatSuffix(), serializerObj.serialize(doc, 'Document'), configId);
    }
    
    public Integer queueBatchOfDocuments(List<Document> docs) {
        return queueBatchOfDocuments(docs, null);
    }
    
    public Integer queueBatchOfDocuments(List<Document> docs, String configId) {
    	return pushDataToService(ANALYTIC_SERVICE_URL + 'document/batch' + getRequestFormatSuffix(), serializerObj.serialize(docs, 'DocumentList'), configId);
    }
    
    public Integer queueCollection(Collection collection) {
    	return queueCollection(collection, null);
    }
    
    public Integer queueCollection(Collection collection, String configId){
    	return pushDataToService(ANALYTIC_SERVICE_URL + 'collection' + getRequestFormatSuffix(), serializerObj.serialize(collection, 'Collection'), configId);
    }
	
	public Integer cancelDocument(String documentId) {
		return cancelDocument(documentId, null);
	}
	
	public Integer cancelDocument(String documentId, String configId) {
		return deleteDataFromService(ANALYTIC_SERVICE_URL + 'document/' + documentId, configId);
	}
	
	public Integer cancelCollection(String collectionId) {
		return cancelCollection(collectionId, null);
	}
	
	public Integer cancelCollection(String collectionId, String configId) {
		return deleteDataFromService(ANALYTIC_SERVICE_URL + 'collection/' + collectionId, configId);
	}
}