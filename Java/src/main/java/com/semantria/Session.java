package com.semantria;


import com.semantria.mapping.output.*;
import com.semantria.mapping.output.stub.FeaturesList;
import com.semantria.utils.AuthRequest;
import com.semantria.utils.ObjProxy;
import com.semantria.interfaces.ICallbackHandler;
import com.semantria.interfaces.ISerializer;
import com.semantria.utils.RequestArgs;
import com.semantria.utils.ResponseArgs;
import com.semantria.mapping.Batch;
import com.semantria.mapping.Collection;
import com.semantria.mapping.Document;
import com.semantria.mapping.configuration.*;
import com.semantria.mapping.configuration.stub.*;
import com.semantria.mapping.output.stub.CollsAnalyticData;
import com.semantria.mapping.output.stub.DocsAnalyticData;
import com.semantria.serializer.JsonSerializer;
import com.semantria.serializer.XmlSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Session 
{
	private static final String WRAPPER_NAME = "Java";
	private String key = "";
	private String secret = "";
	private String appName = "";
	private ISerializer serializer = null;
	private String requestFormat = "json";
	private ICallbackHandler callback = null;
	private String serviceUrl = "https://api.semantria.com";
	public  boolean useCompression = false;


	private Session(String ckey, String csecret, ISerializer cserializer, String appName, boolean useCompression)
	{
		key = ckey;
		secret = csecret;
		if( cserializer == null )
		{
			cserializer = new JsonSerializer();
		}
		registerSerializer(cserializer);
		setAppName(appName);
		this.useCompression = useCompression;
	}

	private void setAppName(String appName) {
		if (null != appName) {
			this.appName = appName + "." + WRAPPER_NAME;
		} else {
			this.appName = WRAPPER_NAME;
		}
	}

	//create new session
	public static Session createSession(String key, String secret)
	{
		return createSession(key, secret, (String)null);
	}

	public static Session createSession(String key, String secret, boolean useCompression)
	{
		return createSession(key, secret, (String)null, useCompression);
	}

	public static Session createSession(String key, String secret, String appName)
	{
		return createSession(key, secret, null, appName);
	}

	public static Session createSession(String key, String secret, String appName, boolean useCompression)
	{
		return createSession(key, secret, null, appName, useCompression);
	}

	public static Session createSession(String key, String secret, ISerializer cserializer)
	{
		return createSession(key, secret, cserializer, null);
	}

	public static Session createSession(String key, String secret, ISerializer cserializer, boolean useCompression)
	{
		return createSession(key, secret, cserializer, null, useCompression);
	}

	public static Session createSession(String key, String secret, ISerializer cserializer, String appName)
	{
		return new Session(key, secret, cserializer, appName, false);
	}

	public static Session createSession(String key, String secret, ISerializer cserializer, String appName, boolean useCompression)
	{
		return new Session(key, secret, cserializer, appName, useCompression);

	}
	
	public void registerSerializer(ISerializer cserializer)
	{
		serializer = cserializer;
		if (null != cserializer) {
			requestFormat = cserializer.getType();
		}
	}
	
	public void setCallbackHandler(ICallbackHandler handler)
	{
		callback = handler;
	}

	//--------getters
	
	public ServiceStatus getStatus()
	{
		String method = "GET";
		AuthRequest req = new AuthRequest(serviceUrl + "/status."+ requestFormat, method, key, secret, null, null, appName, useCompression);
		Integer status = req.doRequest();

		ServiceStatus serviceStatus = null;
		if(status <= 202) serviceStatus = (ServiceStatus)serializer.deserialize(req.getResponse(), ServiceStatus.class);

		handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);

		return serviceStatus;
	}
	
	public List<String> getBlacklist()
	{
		return getBlacklist(null);
	}

	public List<String> getBlacklist(String config_id)
	{
		String method = "GET";
		String url = serviceUrl + "/blacklist."+ requestFormat;

		List<String> res = new ArrayList<String>();
		AuthRequest req = new AuthRequest(url, method, key, secret, null, config_id, appName,useCompression);
		
		Integer status = req.doRequest();
		Blacklists list = (Blacklists)serializer.deserialize(req.getResponse(), Blacklists.class);
		if( list != null )
		{
			res = list.getBlacklist();
		}

		handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);

		return res;
	}
	
	
	public List<Category> getCategories(String config_id)
	{
		String method = "GET";
		String url = serviceUrl + "/categories."+ requestFormat;
		List<Category> res = new ArrayList<Category>();
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName,useCompression);
		
		Integer status = ar.doRequest();
		Categories list = (Categories)serializer.deserialize(ar.getResponse(), Categories.class);
		if(list != null)
		{
			res = list.getCategories();
		}
		handleRequest(method, ar.getRequestUrl(), null);
		handleResponse(status, ar);
		return res;
	}
	
	public List<Category> getCategories()
	{
		return getCategories(null);
	}

	public List<Configuration> getConfigurations()
	{
		List<Configuration> res = new ArrayList<Configuration>();

		String method = "GET";
		String url = serviceUrl + "/configurations."+ requestFormat;
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, null, appName, useCompression);
		
		Integer status = ar.doRequest();
		Configurations list = (Configurations)serializer.deserialize(ar.getResponse(), Configurations.class);
		if(list != null)
		{
			res = list.getConfigurations();
		}
		handleRequest(method, ar.getRequestUrl(), null);
		handleResponse(status, ar);

		return res;
	}
	
	public List<Query> getQueries(String config_id)
	{
		String method = "GET";
		String url = serviceUrl + "/queries."+ requestFormat;
		List<Query> res = new ArrayList<Query>();
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName, useCompression);
		Integer status = ar.doRequest();
		Queries list = (Queries)serializer.deserialize(ar.getResponse(), Queries.class);
		if(list != null)
		{
			res = list.getQueries();
		}
		handleRequest(method, ar.getRequestUrl(), null);
		handleResponse(status, ar);
		return res;
	}
	
	public List<Query> getQueries()
	{
		return getQueries(null);
	}

    public List<SentimentPhrase> getSentimentPhrases(String config_id)
    {
        String method = "GET";
        String url = serviceUrl + "/sentiment."+ requestFormat;
        List<SentimentPhrase> res = new ArrayList<SentimentPhrase>();
        AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName, useCompression);
        Integer status = ar.doRequest();
        SentimentPhrases list = (SentimentPhrases)serializer.deserialize(ar.getResponse(), SentimentPhrases.class);
        if(list != null)
        {
            res = list.getSentimentPhrases();
        }
        handleRequest(method, ar.getRequestUrl(), null);
        handleResponse(status, ar);
        return res;
    }

    public List<SentimentPhrase> getSentimentPhrases()
    {
        return getSentimentPhrases(null);
    }
	
	public List<UserEntity> getEntities(String config_id)
	{
		String method = "GET";
		String url = serviceUrl + "/entities."+ requestFormat;
		List<UserEntity> res = new ArrayList<UserEntity>();
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName, useCompression);
		Integer status = ar.doRequest();
		UserEntities list = (UserEntities)serializer.deserialize(ar.getResponse(), UserEntities.class);
		if(list != null)
		{
			res = list.getEntities();
		}
		handleRequest(method, ar.getRequestUrl(), null);
		handleResponse(status, ar);
		return res;
	}
	
	public List<UserEntity> getEntities()
	{
		return getEntities(null);
	}
	
	public List<com.semantria.mapping.output.DocAnalyticData> getProcessedDocuments(String config_id)
	{
		String method = "GET";
		String url = serviceUrl + "/document/processed."+ requestFormat;
		List<com.semantria.mapping.output.DocAnalyticData> adata = new ArrayList<com.semantria.mapping.output.DocAnalyticData>();
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName, useCompression);
		Integer status = ar.doRequest();
		DocsAnalyticData taskList = new DocsAnalyticData();
		if (200 == status) {
			taskList = (DocsAnalyticData)serializer.deserialize(ar.getResponse(), DocsAnalyticData.class);
		}

		if(taskList != null)
		{
			adata = taskList.getDocuments();
		}
		handleRequest(method, ar.getRequestUrl(), null);
		handleResponse(status, ar);
		return adata;
	}

    public List<com.semantria.mapping.output.DocAnalyticData> getProcessedDocumentsByJobId(final String jobId)
    {
        String method = "GET";
        String url = serviceUrl + "/document/processed."+ requestFormat;
        List<com.semantria.mapping.output.DocAnalyticData> adata = new ArrayList<com.semantria.mapping.output.DocAnalyticData>();
        AuthRequest ar = new AuthRequest(url, method, key, secret, null, appName, useCompression, new HashMap<String, String>() {{ put("job_id", jobId); }});
        Integer status = ar.doRequest();
        DocsAnalyticData taskList = new DocsAnalyticData();
        if (200 == status) {
            taskList = (DocsAnalyticData)serializer.deserialize(ar.getResponse(), DocsAnalyticData.class);
        }

        if(taskList != null)
        {
            adata = taskList.getDocuments();
        }
        handleRequest(method, ar.getRequestUrl(), null);
        handleResponse(status, ar);
        return adata;
    }
	
	public List<com.semantria.mapping.output.DocAnalyticData> getProcessedDocuments()
	{
		return getProcessedDocuments(null);
	}
	
	public List<com.semantria.mapping.output.CollAnalyticData> getProcessedCollections(String config_id)
	{
		String method = "GET";
		String url = serviceUrl + "/collection/processed."+ requestFormat;
		List<com.semantria.mapping.output.CollAnalyticData> adata = new ArrayList<com.semantria.mapping.output.CollAnalyticData>();
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName, useCompression);
		Integer status = ar.doRequest();
		CollsAnalyticData taskList = new CollsAnalyticData();
		if (200 == status) {
			taskList = (CollsAnalyticData)serializer.deserialize(ar.getResponse(), CollsAnalyticData.class);
		}

		if(taskList != null)
		{
			adata = taskList.getDocuments();
		}
		handleRequest(method, ar.getRequestUrl(), null);
		handleResponse(status, ar);
		return adata;
	}

    public List<com.semantria.mapping.output.CollAnalyticData> getProcessedCollectionsByJobId(final String jobId)
    {
        String method = "GET";
        String url = serviceUrl + "/collection/processed."+ requestFormat;
        List<com.semantria.mapping.output.CollAnalyticData> adata = new ArrayList<com.semantria.mapping.output.CollAnalyticData>();
        AuthRequest ar = new AuthRequest(url, method, key, secret, null, appName, useCompression, new HashMap<String, String>() {{ put("job_id", jobId); }});
        Integer status = ar.doRequest();
        CollsAnalyticData taskList = new CollsAnalyticData();
        if (200 == status) {
            taskList = (CollsAnalyticData)serializer.deserialize(ar.getResponse(), CollsAnalyticData.class);
        }

        if(taskList != null)
        {
            adata = taskList.getDocuments();
        }
        handleRequest(method, ar.getRequestUrl(), null);
        handleResponse(status, ar);
        return adata;
    }
	
	public List<com.semantria.mapping.output.CollAnalyticData> getProcessedCollections()
	{
		return getProcessedCollections(null);
	}
	
	public com.semantria.mapping.output.DocAnalyticData getDocument(String id, String config_id)
	{
		String method = "GET";
		String url = serviceUrl + "/document/" + id + "." + requestFormat;
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName, useCompression);
		Integer status = ar.doRequest();
		if (200 == status) {
			com.semantria.mapping.output.DocAnalyticData aData = (com.semantria.mapping.output.DocAnalyticData)serializer.deserialize(ar.getResponse(), com.semantria.mapping.output.DocAnalyticData.class);
			handleRequest(method, ar.getRequestUrl(), null);
			handleResponse(status, ar);
			return aData;
		} else {
			return null;
		}
	}
	
	public com.semantria.mapping.output.DocAnalyticData getDocument(String id)
	{
		return getDocument(id, null);
	}

	public CollAnalyticData getCollection(String id)
	{
		return getCollection(id, null);
	}
	public CollAnalyticData getCollection(String id, String config_id)
	{
		String method = "GET";
		String url = serviceUrl + "/collection/" + id + "." + requestFormat;
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName, useCompression);
		Integer status = ar.doRequest();
		if (200 == status) {
			com.semantria.mapping.output.CollAnalyticData cData = (com.semantria.mapping.output.CollAnalyticData)serializer.deserialize(ar.getResponse(), com.semantria.mapping.output.CollAnalyticData.class);
			handleRequest(method, ar.getRequestUrl(), null);
			handleResponse(status, ar);
			return cData;
		} else {
			return null;
		}
	}
	
	public Subscription getSubscription()
	{
		String method = "GET";
		String url = serviceUrl + "/subscription."+ requestFormat;
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, null, appName, useCompression);
		Integer status = ar.doRequest();
		Subscription subscription = (Subscription)serializer.deserialize(ar.getResponse(), Subscription.class);
		handleRequest(method, ar.getRequestUrl(), null);
		handleResponse(status, ar);
		return subscription;
	}

	public Statistics getStatistics(String interval)
	{
		return getStatistics(interval, null);
	}

	public Statistics getStatistics(String interval, String configId)
	{
		String method = "GET";
		String url = serviceUrl + "/statistics."+ requestFormat;
		AuthRequest req = new AuthRequest(url, method, key, secret, null, configId, appName, interval, useCompression);
		Integer status = req.doRequest();

		Statistics statistics = (Statistics)serializer.deserialize(req.getResponse(), Statistics.class);
		handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);
		return statistics;
	};

    public List<FeaturesSet> getSupportedFeatures(final String language)
    {
        String method = "GET";
        String url = serviceUrl + "/features."+ requestFormat;
        AuthRequest req = null;

        if (language == null) {
            req = new AuthRequest(url, method, key, secret, null, appName, useCompression);
        }
        else {
            req = new AuthRequest(url, method, key, secret, null, appName, useCompression, new HashMap<String, String>() {{ put("language", language); }});
        }

        Integer status = req.doRequest();
        FeaturesList supportedFeatures = (FeaturesList)serializer.deserialize(req.getResponse(), FeaturesList.class);
        handleRequest(method, req.getRequestUrl(), null);
        handleResponse(status, req);
        return supportedFeatures.getFeatures();
    }

	//--------end of getters
	
	//--------update methods

	public Integer addCategories(List<Category> categories)
	{
		return addCategories(categories, null);
	}

	public Integer addCategories(List<Category> categories, String config_id)
	{
		return add(categories, "categories", config_id, Categories.class);
	}

	public Integer updateCategories(List<Category> categories)
	{
		return updateCategories(categories, null);
	}

	public Integer updateCategories(List<Category> categories, String config_id)
	{
		return update(categories, "categories", config_id, Categories.class);
	}

	public Integer removeCategories(List<Category> categories, String config_id)
	{
		return remove(categories, "categories", config_id, Categories.class);
	}

	public Integer removeCategories(List<Category> categories)
	{
		return removeCategories(categories, null);
	}

	public Integer addQueries(List<Query> queries, String config_id)
	{
		return add(queries, "queries", config_id, Queries.class);
	}

	public Integer addQueries(List<Query> queries)
	{
		return addQueries(queries, null);
	}

	public Integer updateQueries(List<Query> queries, String config_id)
	{
		return update(queries, "queries", config_id, Queries.class);
	}

	public Integer updateQueries(List<Query> queries)
	{
		return updateQueries(queries, null);
	}
	
	public Integer removeQueries(List<Query> queries)
	{
		return removeQueries(queries, null);
	}

	public Integer removeQueries(List<Query> queries, String config_id)
	{
		return remove(queries, "queries", config_id, Queries.class);
	}


	public Integer addSentimentPhrases(List<SentimentPhrase> phrases, String config_id)
	{
		return add(phrases, "phrases", config_id, SentimentPhrases.class);
	}

	public Integer addSentimentPhrases(List<SentimentPhrase> phrases)
	{
		return addSentimentPhrases(phrases, null);
	}

	public Integer updateSentimentPhrases(List<SentimentPhrase> phrases, String config_id)
    {
        return update(phrases, "phrases", config_id, SentimentPhrases.class );
    }

	public Integer updateSentimentPhrases(List<SentimentPhrase> phrases)
	{
		return updateSentimentPhrases(phrases, null);
	}

	public Integer removeSentimentPhrases(List<SentimentPhrase> phrases, String config_id)
	{
		return remove(phrases, "phrases", config_id, SentimentPhrases.class);
	}

	public Integer removeSentimentPhrases(List<SentimentPhrase> phrases)
	{
		return removeSentimentPhrases(phrases, null);
	}

	public Integer addBlacklist(List<String> blackList, String config_id)
	{
		return add(blackList, "blacklist", config_id, Blacklists.class);
	}

	public Integer addBlacklist(List<String> blackList)
	{
		return addBlacklist(blackList, null);
	}

	public Integer removeBlacklist(List<String> blackList, String config_id)
	{
		return remove(blackList, "blacklist", config_id, Blacklists.class);
	}

	public Integer removeBlacklist(List<String> blackList)
	{
		return removeBlacklist(blackList, null);
	}

	public Integer addEntities(List<UserEntity> entities, String config_id)
	{
		return add(entities, "entities", config_id, UserEntities.class);
	}

	public Integer addEntities(List<UserEntity> entities)
	{
		return addEntities(entities, null);
	}

	public Integer updateEntities(List<UserEntity> entities)
	{
		return updateEntities(entities, null);
	}

	public Integer updateEntities(List<UserEntity> entities, String config_id)
	{
		return update(entities, "entities", config_id, UserEntities.class);
	}
	
	public Integer removeEntities(List<UserEntity> entities)
	{
		return removeEntities(entities, null);
	}

	public Integer removeEntities(List<UserEntity> entities, String config_id)
	{
		return remove(entities, "entities", config_id, UserEntities.class);
	}

	public Integer addConfigurations(List<Configuration> configurations)
	{
		return addConfigurations(configurations, null);
	}

	private Integer addConfigurations(List<Configuration> configurations, String config_id)
	{
		if( configurations != null && !configurations.isEmpty() )
		{
			for (Configuration configuration : configurations)
			{
				if( configuration.getTemplate() != null )
				{
					configuration.setId( null );
				}
			}
		};

		return add(configurations, "configurations", config_id, Configurations.class);
	}

	public Integer updateConfigurations(List<Configuration> configurations)
	{
		return updateConfigurations(configurations, null);
	}

	private Integer updateConfigurations(List<Configuration> configurations, String config_id)
	{
		return update(configurations, "configurations", config_id, Configurations.class);
	}

	public Integer removeConfigurations(List<Configuration> configurations)
	{
		return removeConfigurations(configurations, null);
	}
	private Integer removeConfigurations(List<Configuration> configurations, String config_id)
	{
		return remove(configurations, "configurations", config_id, Configurations.class);
	}

	private <T> Integer update(List<?> items, String action, String config_id, Class<?> type)
	{
		return update("POST", items, action, config_id, type);
	}

	private <T> Integer add(List<?> items, String action, String config_id, Class<?> type)
	{
		return update("POST", items, action, config_id, type);
	}

	private <T> Integer remove(List<?> items, String action, String config_id, Class<?> type)
	{
		return update("DELETE", items, action, config_id, type);
	}

	private <T> Integer update(String method, List<?> items, String action, String config_id, Class<?> type)
	{
		String url = String.format("%s/%s.%s",  serviceUrl,  action, requestFormat);

		String body = "";

		if( serializer instanceof JsonSerializer)
		{
			body = serializer.serialize( ObjProxy.wrap(items, type, method, "Json") );
		}
		else
		{
			body = serializer.serialize( ObjProxy.wrap(items, type, method) );
		}

		AuthRequest req = new AuthRequest(url, method, key, secret, body, config_id, appName, useCompression);

		Integer status = req.doRequest();
		handleRequest(method, req.getRequestUrl(), body);
		handleResponse(status, req);
		return status;
	}
	
	//--------end of update
	
	//--------queue methods
	public Integer queueDocument(Document task, String config_id)
	{
		String method = "POST";
		String url = serviceUrl + "/document." + requestFormat;
		String body = serializer.serialize(task);
        System.out.println("> " + body.getBytes().length);
		AuthRequest ar = new AuthRequest(url, method, key, secret, body, config_id, appName, useCompression);
		Integer status = ar.doRequest();
		if(callback != null)
		{
			if(status <= 202)
			{
				callback.onResponse(this, new ResponseArgs(status, ar.getResponse()));
			}
			if(status > 202)
			{
				callback.onError(this, new ResponseArgs(status, ar.getErrorMessage()));
			}
			if(!ar.getResponse().isEmpty() && status < 202)
			{
				DocsAnalyticData taskList = (DocsAnalyticData)serializer.deserialize(ar.getResponse(), DocsAnalyticData.class);
				callback.onDocsAutoResponse(this, taskList.getDocuments());
			}
		}
		handleRequest(method, ar.getRequestUrl(), body);
		return status;
	}
	
	public Integer queueDocument(Document task)
	{
		return queueDocument(task, null);
	}
	
	public Integer queueBatch(List<Document> tasks, String config_id)
	{
		String body = null;
		if( serializer instanceof JsonSerializer )
		{
			body = serializer.serialize(tasks);
		}
		else if( serializer instanceof XmlSerializer)
		{
			body = serializer.serialize( ObjProxy.wrap(tasks, Batch.class, "POST") );
		};

		String method = "POST";
		String url = serviceUrl + "/document/batch." + requestFormat;
		AuthRequest ar = new AuthRequest(url, method, key, secret, body, config_id, appName, useCompression);
		Integer status = ar.doRequest();
		if(callback != null)
		{
			if(status <= 202)
			{
				callback.onResponse(this, new ResponseArgs(status, ar.getResponse()));
			}
			if(status > 202)
			{
				callback.onError(this, new ResponseArgs(status, ar.getErrorMessage()));
			}
			if(!ar.getResponse().isEmpty() && status < 202)
			{
				DocsAnalyticData taskList = (DocsAnalyticData)serializer.deserialize(ar.getResponse(), DocsAnalyticData.class);
				callback.onDocsAutoResponse(this, taskList.getDocuments());
			}
		}
		handleRequest(method, ar.getRequestUrl(), body);
		return status;
	}
	
	public Integer queueBatch(List<Document> tasks)
	{
		return queueBatch(tasks, null);
	}

    public Integer queueCollection(Collection collection)
    {
        return queueCollection(collection, null);
    }
        
	public Integer queueCollection(Collection collection, String config_id)
	{
		String body = serializer.serialize(collection);
		String method = "POST";
		String url = serviceUrl + "/collection." + requestFormat;
		AuthRequest ar = new AuthRequest(url, method, key, secret, body, config_id, appName, useCompression);
		Integer status = ar.doRequest();
		if(callback != null)
		{
			if(status <= 202)
			{
				callback.onResponse(this, new ResponseArgs(status, ar.getResponse()));
			}
			if(status > 202)
			{
				callback.onError(this, new ResponseArgs(status, ar.getErrorMessage()));
			}
			if(!ar.getResponse().isEmpty() && status < 202)
			{
				CollsAnalyticData taskList = (CollsAnalyticData)serializer.deserialize(ar.getResponse(), CollsAnalyticData.class);
				callback.onCollsAutoResponse(this, taskList.getDocuments());
			}
		}
		handleRequest(method, ar.getRequestUrl(), body);
		return status;
	}
	
	public Integer cancelDocument(String id, String config_id)
	{
		String body = null;
		String method = "DELETE";
		String url = serviceUrl + "/document/" + id + "." + requestFormat;
		AuthRequest ar = new AuthRequest(url, method, key, secret, body, config_id, appName, useCompression);
		Integer status = ar.doRequest();
		handleRequest(method, ar.getRequestUrl(), body);
		handleResponse(status, ar);
		return status;
	}

	public Integer cancelCollection(String id)
	{
		return cancelCollection(id, null);
	}
	public Integer cancelCollection(String id, String config_id)
	{
		String body = null;
		String method = "DELETE";
		String url = serviceUrl + "/collection/" + id + "." + requestFormat;
		AuthRequest ar = new AuthRequest(url, method, key, secret, body, config_id, appName, useCompression);
		Integer status = ar.doRequest();
		handleRequest(method, ar.getRequestUrl(), body);
		handleResponse(status, ar);
		return status;
	}
	
	public void handleResponse(Integer status, AuthRequest ar)
	{
		if(callback != null)
		{
			if(status <= 202)
			{
				callback.onResponse(this, new ResponseArgs(status, ar.getResponse()));
			}
			if(status > 202)
			{
				callback.onError(this, new ResponseArgs(status, ar.getErrorMessage()));
			}
		}
	}
	
	public void handleRequest(String method, String url, String message)
	{
		if(callback != null)
		{
			callback.onRequest(this, new RequestArgs(method, url, message));
		}
	}
	
	public void cancelDocument(String id)
	{
		cancelDocument(id, null);
	}
}
