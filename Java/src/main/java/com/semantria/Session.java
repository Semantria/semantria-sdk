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
import java.util.List;

public final class Session
{
	private static final String WRAPPER_NAME = "Java";
	private String key = "";
	private String secret = "";
	private String appName = "";
    private String apiVersion = "3.9";
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

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getApiVersion() {
        return this.apiVersion;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getServiceUrl() {
        return this.serviceUrl;
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
        String url = generateRequestUrl("status");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

        Integer status = req.doRequest();
		ServiceStatus serviceStatus = null;

		if (status <= 202)
            serviceStatus = (ServiceStatus)serializer.deserialize(req.getResponse(), ServiceStatus.class);

		handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);

		return serviceStatus;
	}

    private String generateRequestUrl(String path) {
        return serviceUrl + "/" + path + "." + requestFormat;
    }

	public List<String> getBlacklist()
	{
		return getBlacklist(null);
	}

	public List<String> getBlacklist(String config_id)
	{
		String method = "GET";
        String url = generateRequestUrl("blacklist");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();
		Blacklists list = (Blacklists)serializer.deserialize(req.getResponse(), Blacklists.class);

        List<String> res = new ArrayList<String>();
        if (list != null) {
			res = list.getBlacklist();
		}

		handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);

		return res;
	}

    public List<Category> getCategories()
    {
        return getCategories(null);
    }

	public List<Category> getCategories(String config_id)
	{
		String method = "GET";
        String url = generateRequestUrl("categories");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .config_id(config_id)
                .appName(appName)
                .useCompression(useCompression);

		Integer status = req.doRequest();
		Categories list = (Categories)serializer.deserialize(req.getResponse(), Categories.class);

        List<Category> res = new ArrayList<Category>();
        if (list != null) {
			res = list.getCategories();
		}

		handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);

        return res;
	}

	public List<Configuration> getConfigurations()
	{
		String method = "GET";
        String url = generateRequestUrl("configurations");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();
		Configurations list = (Configurations)serializer.deserialize(req.getResponse(), Configurations.class);

        List<Configuration> res = new ArrayList<Configuration>();
		if (list != null) {
			res = list.getConfigurations();
		}

		handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);

		return res;
	}

    public List<Query> getQueries()
    {
        return getQueries(null);
    }

	public List<Query> getQueries(String config_id)
	{
		String method = "GET";
        String url = generateRequestUrl("queries");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();
		Queries list = (Queries)serializer.deserialize(req.getResponse(), Queries.class);

        List<Query> res = new ArrayList<Query>();
        if (list != null) {
			res = list.getQueries();
		}

        handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);

        return res;
	}

    public List<SentimentPhrase> getSentimentPhrases()
    {
        return getSentimentPhrases(null);
    }

    public List<SentimentPhrase> getSentimentPhrases(String config_id)
    {
        String method = "GET";
        String url = generateRequestUrl("sentiment");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

        Integer status = req.doRequest();
        SentimentPhrases list = (SentimentPhrases)serializer.deserialize(req.getResponse(), SentimentPhrases.class);

        List<SentimentPhrase> res = new ArrayList<SentimentPhrase>();
        if (list != null) {
            res = list.getSentimentPhrases();
        }

        handleRequest(method, req.getRequestUrl(), null);
        handleResponse(status, req);

        return res;
    }

    public List<UserEntity> getEntities()
    {
        return getEntities(null);
    }

	public List<UserEntity> getEntities(String config_id)
	{
		String method = "GET";
        String url = generateRequestUrl("entities");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();
		UserEntities list = (UserEntities)serializer.deserialize(req.getResponse(), UserEntities.class);

        List<UserEntity> res = new ArrayList<UserEntity>();
        if (list != null) {
			res = list.getEntities();
		}

		handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);

        return res;
	}

    public List<com.semantria.mapping.output.DocAnalyticData> getProcessedDocuments()
    {
        return getProcessedDocuments(null);
    }

	public List<com.semantria.mapping.output.DocAnalyticData> getProcessedDocuments(String config_id)
	{
		String method = "GET";
        String url = generateRequestUrl("document/processed");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

        DocsAnalyticData taskList = new DocsAnalyticData();
        Integer status = req.doRequest();
		if (200 == status) {
			taskList = (DocsAnalyticData)serializer.deserialize(req.getResponse(), DocsAnalyticData.class);
		}

        List<com.semantria.mapping.output.DocAnalyticData> result = new ArrayList<com.semantria.mapping.output.DocAnalyticData>();
		if (taskList != null) {
			result = taskList.getDocuments();
		}

		handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);

        return result;
	}

    public List<com.semantria.mapping.output.DocAnalyticData> getProcessedDocumentsByJobId(final String jobId)
    {
        String method = "GET";
        String url = generateRequestUrl("document/processed");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .job_id(jobId)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

        Integer status = req.doRequest();
        DocsAnalyticData taskList = new DocsAnalyticData();
        if (200 == status) {
            taskList = (DocsAnalyticData)serializer.deserialize(req.getResponse(), DocsAnalyticData.class);
        }

        List<com.semantria.mapping.output.DocAnalyticData> result = new ArrayList<com.semantria.mapping.output.DocAnalyticData>();
        if (taskList != null) {
            result = taskList.getDocuments();
        }

        handleRequest(method, req.getRequestUrl(), null);
        handleResponse(status, req);

        return result;
    }

    public List<com.semantria.mapping.output.CollAnalyticData> getProcessedCollections()
    {
        return getProcessedCollections(null);
    }

	public List<com.semantria.mapping.output.CollAnalyticData> getProcessedCollections(String config_id)
	{
		String method = "GET";
        String url = generateRequestUrl("collection/processed");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();
		CollsAnalyticData taskList = new CollsAnalyticData();
		if (200 == status) {
			taskList = (CollsAnalyticData)serializer.deserialize(req.getResponse(), CollsAnalyticData.class);
		}

        List<com.semantria.mapping.output.CollAnalyticData> result = new ArrayList<com.semantria.mapping.output.CollAnalyticData>();
		if (taskList != null) {
			result = taskList.getDocuments();
		}

        handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);

		return result;
	}

    public List<com.semantria.mapping.output.CollAnalyticData> getProcessedCollectionsByJobId(final String jobId)
    {
        String method = "GET";
        String url = generateRequestUrl("collection/processed");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .job_id(jobId)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

        Integer status = req.doRequest();
        CollsAnalyticData taskList = new CollsAnalyticData();
        if (200 == status) {
            taskList = (CollsAnalyticData)serializer.deserialize(req.getResponse(), CollsAnalyticData.class);
        }

        List<com.semantria.mapping.output.CollAnalyticData> adata = new ArrayList<com.semantria.mapping.output.CollAnalyticData>();
        if (taskList != null) {
            adata = taskList.getDocuments();
        }

        handleRequest(method, req.getRequestUrl(), null);
        handleResponse(status, req);

        return adata;
    }

    public com.semantria.mapping.output.DocAnalyticData getDocument(String id)
    {
        return getDocument(id, null);
    }

	public com.semantria.mapping.output.DocAnalyticData getDocument(String id, String config_id)
	{
		String method = "GET";
        String url = generateRequestUrl("document/" + id);

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();
        com.semantria.mapping.output.DocAnalyticData result = null;
        if (200 == status) {
			result = (com.semantria.mapping.output.DocAnalyticData)serializer.deserialize(req.getResponse(), com.semantria.mapping.output.DocAnalyticData.class);

            handleRequest(method, req.getRequestUrl(), null);
			handleResponse(status, req);
		}

        return result;
	}

	public CollAnalyticData getCollection(String id)
	{
		return getCollection(id, null);
	}

	public CollAnalyticData getCollection(String id, String config_id)
	{
		String method = "GET";
        String url = generateRequestUrl("collection/" + id);

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();
        com.semantria.mapping.output.CollAnalyticData result = null;
        if (200 == status) {
			result = (com.semantria.mapping.output.CollAnalyticData)serializer.deserialize(req.getResponse(), com.semantria.mapping.output.CollAnalyticData.class);

			handleRequest(method, req.getRequestUrl(), null);
			handleResponse(status, req);
		}

        return result;
	}

	public Subscription getSubscription()
	{
		String method = "GET";
        String url = generateRequestUrl("subscription");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();
		Subscription subscription = (Subscription)serializer.deserialize(req.getResponse(), Subscription.class);

        handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);

        return subscription;
	}

	public Statistics getStatistics(String interval)
	{
		return getStatistics(interval, null);
	}

	public Statistics getStatistics(String interval, String configId)
	{
		String method = "GET";
        String url = generateRequestUrl("statistics");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .config_id(configId)
                .interval(interval)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();
		Statistics statistics = (Statistics)serializer.deserialize(req.getResponse(), Statistics.class);

        handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);

        return statistics;
	}

    public List<FeaturesSet> getSupportedFeatures(final String language)
    {
        String method = "GET";
        String url = generateRequestUrl("features");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .language(language)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

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

    public Integer cloneConfigurations(String name, String template)
    {
        Configuration configuration = new Configuration();
        configuration.setName(name);
        configuration.setTemplate(template);

        List<Configuration> configurations = new ArrayList<Configuration>();
        configurations.add(configuration);

        return add(configurations, "configurations", null, Configurations.class);
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
		String body = null;

		if (serializer instanceof JsonSerializer) {
			body = serializer.serialize(ObjProxy.wrap(items, type, method, "Json"));
		} else {
			body = serializer.serialize(ObjProxy.wrap(items, type, method));
		}

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .body(body)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();

        handleRequest(method, req.getRequestUrl(), body);
		handleResponse(status, req);

		return status;
	}

	//--------end of update

	//--------queue methods
    public Integer queueDocument(Document task)
    {
        return queueDocument(task, null);
    }

	public Integer queueDocument(Document task, String config_id)
	{
		String method = "POST";
        String url = generateRequestUrl("document");
		String body = serializer.serialize(task);
//        System.out.println("> " + body.getBytes().length);

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .body(body)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();
		if (callback != null)
		{
			if (status <= 202)
			{
				callback.onResponse(this, new ResponseArgs(status, req.getResponse()));
			}
			if (status > 202)
			{
				callback.onError(this, new ResponseArgs(status, req.getErrorMessage()));
			}
			if (!req.getResponse().isEmpty() && status < 202)
			{
				DocsAnalyticData taskList = (DocsAnalyticData)serializer.deserialize(req.getResponse(), DocsAnalyticData.class);
				callback.onDocsAutoResponse(this, taskList.getDocuments());
			}
		}

		handleRequest(method, req.getRequestUrl(), body);

        return status;
	}

    public Integer queueBatch(List<Document> tasks)
    {
        return queueBatch(tasks, null);
    }

	public Integer queueBatch(List<Document> tasks, String config_id)
	{
		String method = "POST";
        String url = generateRequestUrl("document/batch");
        String body = null;
        if (serializer instanceof JsonSerializer)
        {
            body = serializer.serialize(tasks);
        }
        else if (serializer instanceof XmlSerializer)
        {
            body = serializer.serialize(ObjProxy.wrap(tasks, Batch.class, "POST"));
        }

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .body(body)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();
		if (callback != null)
		{
			if (status <= 202)
			{
				callback.onResponse(this, new ResponseArgs(status, req.getResponse()));
			}
			if (status > 202)
			{
				callback.onError(this, new ResponseArgs(status, req.getErrorMessage()));
			}
			if (!req.getResponse().isEmpty() && status < 202)
			{
				DocsAnalyticData taskList = (DocsAnalyticData)serializer.deserialize(req.getResponse(), DocsAnalyticData.class);
				callback.onDocsAutoResponse(this, taskList.getDocuments());
			}
		}

		handleRequest(method, req.getRequestUrl(), body);

        return status;
	}

    public Integer queueCollection(Collection collection)
    {
        return queueCollection(collection, null);
    }

	public Integer queueCollection(Collection collection, String config_id)
	{
		String method = "POST";
        String url = generateRequestUrl("collection");
        String body = serializer.serialize(collection);

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .body(body)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();
		if (callback != null)
		{
			if (status <= 202)
			{
				callback.onResponse(this, new ResponseArgs(status, req.getResponse()));
			}
			if (status > 202)
			{
				callback.onError(this, new ResponseArgs(status, req.getErrorMessage()));
			}
			if (!req.getResponse().isEmpty() && status < 202)
			{
				CollsAnalyticData taskList = (CollsAnalyticData)serializer.deserialize(req.getResponse(), CollsAnalyticData.class);
				callback.onCollsAutoResponse(this, taskList.getDocuments());
			}
		}

		handleRequest(method, req.getRequestUrl(), body);

		return status;
	}

    public void cancelDocument(String id)
    {
        cancelDocument(id, null);
    }

	public Integer cancelDocument(String id, String config_id)
	{
		String method = "DELETE";
        String url = generateRequestUrl("document/" + id);

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();

        handleRequest(method, req.getRequestUrl(), null);
		handleResponse(status, req);

        return status;
	}

	public Integer cancelCollection(String id)
	{
		return cancelCollection(id, null);
	}

	public Integer cancelCollection(String id, String config_id)
	{
		String method = "DELETE";
        String url = generateRequestUrl("collection/" + id);

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

		Integer status = req.doRequest();

        handleRequest(method, req.getRequestUrl());
		handleResponse(status, req);

        return status;
	}

	public void handleResponse(Integer status, AuthRequest ar)
	{
		if (callback != null)
		{
			if (status <= 202)
			{
				callback.onResponse(this, new ResponseArgs(status, ar.getResponse()));
			}
			if (status > 202)
			{
				callback.onError(this, new ResponseArgs(status, ar.getErrorMessage()));
			}
		}
	}

    public void handleRequest(String method, String url) {
        handleRequest(method, url, null);
    }

	public void handleRequest(String method, String url, String message)
	{
		if (callback != null)
		{
			callback.onRequest(this, new RequestArgs(method, url, message));
		}
	}
}
