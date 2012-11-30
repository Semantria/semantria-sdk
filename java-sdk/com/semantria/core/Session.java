package com.semantria.core;


import com.semantria.interfaces.ICallbackHandler;
import com.semantria.interfaces.ISerializer;
import com.semantria.interfaces.IUpdateProxy;
import com.semantria.objects.RequestArgs;
import com.semantria.objects.ResponseArgs;
import com.semantria.objects.configuration.*;
import com.semantria.objects.mapping.*;
import com.semantria.objects.output.*;
import com.semantria.proxies.*;

import java.util.ArrayList;
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
	private String serviceUrl = "https://api21.semantria.com";

	private Session(String ckey, String csecret, ISerializer cserializer, String appName)
	{
		key = ckey;
		secret = csecret;
		registerSerializer(cserializer);
		setAppName(appName);
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

	public static Session createSession(String key, String secret, String appName)
	{
		return createSession(key, secret, null, appName);
	}

	public static Session createSession(String key, String secret, ISerializer cserializer)
	{
		return createSession(key, secret, cserializer, null);
	}

	public static Session createSession(String key, String secret, ISerializer cserializer, String appName)
	{
		return new Session(key, secret, cserializer, appName);
		
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

	//--------create proxies
	
	public IUpdateProxy<Category> createCategoriesUpdateProxy()
	{
		return new CategoryUpdateProxy();
	}
	
	public IUpdateProxy<Query> createQueriesUpdateProxy()
	{
		return new QueryUpdateProxy();
	}
	
	public IUpdateProxy<String> createBlacklistUpdateProxy()
	{
		return new BlacklistUpdateProxy();
	}
	
	public IUpdateProxy<Configuration> createConfigurationsUpdateProxy()
	{
		return new ConfigurationUpdateProxy();
	}
	
	public IUpdateProxy<UserEntity> createEntitiesUpdateProxy()
	{
		return new EntityUpdateProxy();
	}

    public IUpdateProxy<SentimentPhrase> createSentimentPhraseUpdateProxy()
    {
        return new SentimentPhraseUpdateProxy();
    }
	
	//--------end of proxy
	
	//--------getters
	
	public ServiceStatus getStatus()
	{
		String method = "GET";
		AuthRequest ar = new AuthRequest(serviceUrl + "/status."+ requestFormat, "GET", key, secret, null, null, appName);
		Integer status = ar.doRequest();
		ServiceStatus ss = null;
		if(status <= 202) ss = (ServiceStatus)serializer.deserialize(ar.getResponse(), ServiceStatus.class);
		handleRequest(method, ar.getRequestUrl(), null);
		handleResponse(status, ar);
		return ss;
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
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName);
		
		Integer status = ar.doRequest();
		Stub_Blacklist list = (Stub_Blacklist)serializer.deserialize(ar.getResponse(), Stub_Blacklist.class);
		if(list != null)
		{
			res = list.getBlacklist();
		}
		handleRequest(method, ar.getRequestUrl(), null);
		handleResponse(status, ar);
		return res;
	}
	
	
	public List<Category> getCategories(String config_id)
	{
		String method = "GET";
		String url = serviceUrl + "/categories."+ requestFormat;
		List<Category> res = new ArrayList<Category>();
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName);
		
		Integer status = ar.doRequest();
		Stub_Categories list = (Stub_Categories)serializer.deserialize(ar.getResponse(), Stub_Categories.class);
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
		String method = "GET";
		String url = serviceUrl + "/configurations."+ requestFormat;
		List<Configuration> res = new ArrayList<Configuration>();
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, null, appName);
		
		Integer status = ar.doRequest();
		Stub_Configurations list = (Stub_Configurations)serializer.deserialize(ar.getResponse(), Stub_Configurations.class);
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
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName);
		Integer status = ar.doRequest();
		Stub_Queries list = (Stub_Queries)serializer.deserialize(ar.getResponse(), Stub_Queries.class);
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
        AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName);
        Integer status = ar.doRequest();
        Stub_SentimentPhrase list = (Stub_SentimentPhrase)serializer.deserialize(ar.getResponse(), Stub_SentimentPhrase.class);
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
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName);
		Integer status = ar.doRequest();
		Stub_Entities list = (Stub_Entities)serializer.deserialize(ar.getResponse(), Stub_Entities.class);
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
	
	public List<DocAnalyticData> getProcessedDocuments(String config_id)
	{
		String method = "GET";
		String url = serviceUrl + "/document/processed."+ requestFormat;
		List<DocAnalyticData> adata = new ArrayList<DocAnalyticData>();
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName);
		Integer status = ar.doRequest();
		Stub_DocAnalyticDatas taskList = new Stub_DocAnalyticDatas();
		if (200 == status) {
			taskList = (Stub_DocAnalyticDatas)serializer.deserialize(ar.getResponse(), Stub_DocAnalyticDatas.class);
		}

		if(taskList != null)
		{
			adata = taskList.getDocuments();
		}
		handleRequest(method, ar.getRequestUrl(), null);
		handleResponse(status, ar);
		return adata;
	}
	
	public List<DocAnalyticData> getProcessedDocuments()
	{
		return getProcessedDocuments(null);
	}
	
	public List<CollAnalyticData> getProcessedCollections(String config_id)
	{
		String method = "GET";
		String url = serviceUrl + "/collection/processed."+ requestFormat;
		List<CollAnalyticData> adata = new ArrayList<CollAnalyticData>();
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName);
		Integer status = ar.doRequest();
		Stub_CollAnalyticDatas taskList = new Stub_CollAnalyticDatas();
		if (200 == status) {
			taskList = (Stub_CollAnalyticDatas)serializer.deserialize(ar.getResponse(), Stub_CollAnalyticDatas.class);
		}

		if(taskList != null)
		{
			adata = taskList.getDocuments();
		}
		handleRequest(method, ar.getRequestUrl(), null);
		handleResponse(status, ar);
		return adata;
	}
	
	public List<CollAnalyticData> getProcessedCollections()
	{
		return getProcessedCollections(null);
	}
	
	public DocAnalyticData getDocument(String id, String config_id)
	{
		String method = "GET";
		String url = serviceUrl + "/document/" + id + "." + requestFormat;
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName);
		Integer status = ar.doRequest();
		if (200 == status) {
			DocAnalyticData aData = (DocAnalyticData)serializer.deserialize(ar.getResponse(), DocAnalyticData.class);
			handleRequest(method, ar.getRequestUrl(), null);
			handleResponse(status, ar);
			return aData;
		} else {
			return null;
		}
	}
	
	public DocAnalyticData getDocument(String id)
	{
		return getDocument(id, null);
	}
	
	public CollAnalyticData getCollection(String id, String config_id)
	{
		String method = "GET";
		String url = serviceUrl + "/collection/" + id + "." + requestFormat;
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, config_id, appName);
		Integer status = ar.doRequest();
		if (200 == status) {
			CollAnalyticData cData = (CollAnalyticData)serializer.deserialize(ar.getResponse(), CollAnalyticData.class);
			handleRequest(method, ar.getRequestUrl(), null);
			handleResponse(status, ar);
			return cData;
		} else {
			return null;
		}
	}
	
	public Subscription verifySubscription()
	{
		String method = "GET";
		String url = serviceUrl + "/subscription."+ requestFormat;
		AuthRequest ar = new AuthRequest(url, method, key, secret, null, null, appName);
		Integer status = ar.doRequest();
		Subscription subscription = (Subscription)serializer.deserialize(ar.getResponse(), Subscription.class);
		handleRequest(method, ar.getRequestUrl(), null);
		handleResponse(status, ar);
		return subscription;
	}

	//--------end of getters
	
	//--------update methods
	
	public Integer updateCategories(IUpdateProxy<Category> update, String config_id)
	{
		return update(update, "categories", config_id);
	}
	
	public Integer updateCategories(IUpdateProxy<Category> update)
	{
		return updateCategories(update, null);
	}
	
	public Integer updateQueries(IUpdateProxy<Query> update, String config_id)
	{
		return update(update, "queries", config_id);
	}
	
	public Integer updateQueries(IUpdateProxy<Query> update)
	{
		return updateQueries(update, null);
	}

    public Integer updateSentimentPhrases(IUpdateProxy<SentimentPhrase> update, String config_id)
    {
        return update(update, "sentiment", config_id);
    }

    public Integer updateSentimentPhrases(IUpdateProxy<SentimentPhrase> update)
    {
        return updateSentimentPhrases(update, null);
    }
	
	public Integer updateBlacklist(IUpdateProxy<String> update, String config_id)
	{
		return update(update, "blacklist", config_id);
	}
	
	public Integer updateBlacklist(IUpdateProxy<String> update)
	{
		return updateBlacklist(update, null);
	}
	
	public Integer updateEntities(IUpdateProxy<UserEntity> update, String config_id)
	{
		return update(update, "entities", config_id);
	}
	
	public Integer updateEntities(IUpdateProxy<UserEntity> update)
	{
		return updateEntities(update, null);
	}
	
	public Integer updateConfigurations(IUpdateProxy<Configuration> update)
	{
		return update(update, "configurations", null);
	}
	
	private Integer update(IUpdateProxy update, String action, String config_id)
	{
		String method = "POST";
		String url = serviceUrl + "/" + action + "."+ requestFormat;
		String body = serializer.serialize(update);
		AuthRequest ar = new AuthRequest(url, method, key, secret, body, config_id, appName);
		Integer status = ar.doRequest();
		handleRequest(method, ar.getRequestUrl(), body);
		handleResponse(status, ar);
		return status;
	}
	
	//--------end of update
	
	//--------queue methods
	public Integer queueDocument(Document task, String config_id)
	{
		String method = "POST";
		String url = serviceUrl + "/document." + requestFormat;
		String body = serializer.serialize(task);
		AuthRequest ar = new AuthRequest(url, method, key, secret, body, config_id, appName);
		Integer status = ar.doRequest();
		if(callback != null)
		{
			
			if(status <= 202)
			{
				callback.onResponse(this, new ResponseArgs(status, ar.getResponse()));
			}
			if(status > 202)
			{
				callback.onError(this, new ResponseArgs(status, ar.getResponse()));
			}
			if(!ar.getResponse().isEmpty() && status < 202)
			{
				Stub_DocAnalyticDatas taskList = (Stub_DocAnalyticDatas)serializer.deserialize(ar.getResponse(), Stub_DocAnalyticDatas.class);
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
		String body = serializer.serialize(tasks);
		String method = "POST";
		String url = serviceUrl + "/document/batch." + requestFormat;
		AuthRequest ar = new AuthRequest(url, method, key, secret, body, config_id, appName);
		Integer status = ar.doRequest();
		if(callback != null)
		{
			if(status <= 202)
			{
				callback.onResponse(this, new ResponseArgs(status, ar.getResponse()));
			}
			if(status > 202)
			{
				callback.onError(this, new ResponseArgs(status, ar.getResponse()));
			}
			if(!ar.getResponse().isEmpty() && status < 202)
			{
				Stub_DocAnalyticDatas taskList = (Stub_DocAnalyticDatas)serializer.deserialize(ar.getResponse(), Stub_DocAnalyticDatas.class);
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
	
	public Integer queueCollection(Collection collection, String config_id)
	{
		String body = serializer.serialize(collection);
		String method = "POST";
		String url = serviceUrl + "/collection." + requestFormat;
		AuthRequest ar = new AuthRequest(url, method, key, secret, body, config_id, appName);
		Integer status = ar.doRequest();
		if(callback != null)
		{
			if(status <= 202)
			{
				callback.onResponse(this, new ResponseArgs(status, ar.getResponse()));
			}
			if(status > 202)
			{
				callback.onError(this, new ResponseArgs(status, ar.getResponse()));
			}
			if(!ar.getResponse().isEmpty() && status < 202)
			{
				Stub_CollAnalyticDatas taskList = (Stub_CollAnalyticDatas)serializer.deserialize(ar.getResponse(), Stub_CollAnalyticDatas.class);
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
		String url = serviceUrl + "/document/" + id;
		AuthRequest ar = new AuthRequest(url, method, key, secret, body, config_id, appName);
		Integer status = ar.doRequest();
		handleRequest(method, ar.getRequestUrl(), body);
		handleResponse(status, ar);
		return status;
	}
	
	public Integer cancelCollection(String id, String config_id)
	{
		String body = null;
		String method = "DELETE";
		String url = serviceUrl + "/collection/" + id;
		AuthRequest ar = new AuthRequest(url, method, key, secret, body, config_id, appName);
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
				callback.onError(this, new ResponseArgs(status, ar.getResponse()));
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
