package com.semantria;


import com.semantria.auth.AuthService;
import com.semantria.auth.CredentialException;
import com.semantria.mapping.output.*;
import com.semantria.mapping.output.stub.FeaturesList;
import com.semantria.utils.*;
import com.semantria.interfaces.ICallbackHandler;
import com.semantria.interfaces.ISerializer;
import com.semantria.mapping.Batch;
import com.semantria.mapping.Collection;
import com.semantria.mapping.Document;
import com.semantria.mapping.configuration.*;
import com.semantria.mapping.configuration.stub.*;
import com.semantria.mapping.output.stub.CollsAnalyticData;
import com.semantria.mapping.output.stub.DocsAnalyticData;
import com.semantria.serializer.JsonSerializer;
import com.semantria.serializer.XmlSerializer;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public final class Session
{
    private static final String WRAPPER_NAME = "Java";
    private static final String AUTH_SERVICE_URL = "https://semantria.com";

    private String key = "";
    private String secret = "";
    private String appName = "";
    private String apiVersion = "4.0";
    private ISerializer serializer = null;
    private String requestFormat = "json";
    private ICallbackHandler callback = null;
    private String serviceUrl = "https://api.semantria.com";
    public  boolean useCompression = false;

    private Session(String key, String secret) {
        this.key = key;
        this.secret = secret;
        registerSerializer(new JsonSerializer());
    }

    //create new session
    public static Session createSession(String key, String secret) {
        return new Session(key, secret);
    }

    public static Session createUserSession(String username, String password, String appKey) throws CredentialException {
        return Session.createUserSession(username, password, appKey, true);
    }

    public static Session createUserSession(String username, String password, String appKey, boolean reuseExisting) throws CredentialException  {
        AuthService authService = new AuthService(appKey, Session.AUTH_SERVICE_URL);
        authService.authWithUsernameAndPassword(username, password, reuseExisting);

        return new Session(authService.getKey(), authService.getSecret());
    }

    public void useCompression(boolean useCompression) {
        this.useCompression = useCompression;
    }

    public void registerSerializer(ISerializer cserializer)
    {
        serializer = cserializer;
        if (null != cserializer) {
            requestFormat = cserializer.getType();
        }
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

    private String generateRequestUrl(String path, String id) {
        try {
            id = URLEncoder.encode(id, "UTF-8");
        } catch (Exception ex) {
            System.err.println("generateRequestUrl exception: " + ex.getMessage());
        }

        return this.generateRequestUrl(path + id);
    }

    public List<BlacklistItem> getBlacklist()
    {
        return getBlacklist(null);
    }

    public List<BlacklistItem> getBlacklist(String config_id)
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

        List<BlacklistItem> res = new ArrayList<BlacklistItem>();
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

    public List<TaxonomyNode> getTaxonomy()
    {
        return getTaxonomy(null);
    }

    public List<TaxonomyNode> getTaxonomy(String config_id)
    {
        String method = "GET";
        String url = generateRequestUrl("taxonomy");

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

        Integer status = req.doRequest();
        Taxonomies list = (Taxonomies)serializer.deserialize(req.getResponse(), Taxonomies.class);

        List<TaxonomyNode> res = new ArrayList<TaxonomyNode>();
        if (list != null) {
            res = list.getTaxonomies();
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
        String url = generateRequestUrl("document/", id);

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
        String url = generateRequestUrl("collection/", id);

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

    public List<Category> addCategories(List<Category> categories)
    {
        return addCategories(categories, null);
    }

    public List<Category> addCategories(List<Category> categories, String config_id)
    {
        AuthRequest req = add(categories, "categories", config_id, Categories.class);
        Categories list = (Categories)serializer.deserialize(req.getResponse(), Categories.class);
        if (list != null) {
            return list.getCategories();
        } else {
            return new ArrayList<Category>();
        }
    }

    public List<Category> updateCategories(List<Category> categories)
    {
        return updateCategories(categories, null);
    }

    public List<Category> updateCategories(List<Category> categories, String config_id)
    {
        AuthRequest req = update(categories, "categories", config_id, Categories.class, "PUT");
        Categories list = (Categories)serializer.deserialize(req.getResponse(), Categories.class);
        if (list != null) {
            return list.getCategories();
        } else {
            return new ArrayList<Category>();
        }
    }

    public Integer removeCategories(List<Category> categories, String config_id)
    {
        return remove(categories, "categories", config_id, Categories.class);
    }

    public Integer removeCategories(List<Category> categories)
    {
        return removeCategories(categories, null);
    }

    public List<Query> addQueries(List<Query> queries, String config_id)
    {
        AuthRequest req = add(queries, "queries", config_id, Queries.class);
        Queries list = (Queries)serializer.deserialize(req.getResponse(), Queries.class);
        if (list != null) {
            return list.getQueries();
        } else {
            return new ArrayList<Query>();
        }
    }

    public List<Query> addQueries(List<Query> queries)
    {
        return addQueries(queries, null);
    }

    public List<Query> updateQueries(List<Query> queries, String config_id)
    {
        AuthRequest req = update(queries, "queries", config_id, Queries.class, "PUT");
        Queries list = (Queries)serializer.deserialize(req.getResponse(), Queries.class);
        if (list != null) {
            return list.getQueries();
        } else {
            return new ArrayList<Query>();
        }
    }

    public List<Query> updateQueries(List<Query> queries)
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


    public List<SentimentPhrase> addSentimentPhrases(List<SentimentPhrase> phrases, String config_id)
    {
        AuthRequest req = add(phrases, "phrases", config_id, SentimentPhrases.class);
        SentimentPhrases list = (SentimentPhrases)serializer.deserialize(req.getResponse(), SentimentPhrases.class);
        if (list != null) {
            return list.getSentimentPhrases();
        } else {
            return new ArrayList<SentimentPhrase>();
        }
    }

    public List<SentimentPhrase> addSentimentPhrases(List<SentimentPhrase> phrases)
    {
        return addSentimentPhrases(phrases, null);
    }

    public List<SentimentPhrase> updateSentimentPhrases(List<SentimentPhrase> phrases, String config_id)
    {
        AuthRequest req = update(phrases, "phrases", config_id, SentimentPhrases.class, "PUT");
        SentimentPhrases list = (SentimentPhrases)serializer.deserialize(req.getResponse(), SentimentPhrases.class);
        if (list != null) {
            return list.getSentimentPhrases();
        } else {
            return new ArrayList<SentimentPhrase>();
        }
    }

    public List<SentimentPhrase> updateSentimentPhrases(List<SentimentPhrase> phrases)
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

    public List<BlacklistItem> addBlacklist(List<BlacklistItem> blacklistItems, String config_id)
    {
        AuthRequest req = add(blacklistItems, "blacklist", config_id, Blacklists.class);
        Blacklists list = (Blacklists)serializer.deserialize(req.getResponse(), Blacklists.class);
        if (list != null) {
            return list.getBlacklist();
        } else {
            return new ArrayList<BlacklistItem>();
        }
    }

    public List<BlacklistItem> addBlacklist(List<BlacklistItem> blackList)
    {
        return addBlacklist(blackList, null);
    }

    public List<BlacklistItem> updateBlacklist(List<BlacklistItem> blackList, String config_id)
    {
        AuthRequest req = update(blackList, "blacklist", config_id, Blacklists.class, "PUT");
        Blacklists list = (Blacklists)serializer.deserialize(req.getResponse(), Blacklists.class);
        if (list != null) {
            return list.getBlacklist();
        } else {
            return new ArrayList<BlacklistItem>();
        }
    }

    public List<BlacklistItem> updateBlacklist(List<BlacklistItem> blackList)
    {
        return updateBlacklist(blackList, null);
    }

    public Integer removeBlacklist(List<BlacklistItem> blacklistItems, String config_id)
    {
        return remove(blacklistItems, "blacklist", config_id, Blacklists.class);
    }

    public Integer removeBlacklist(List<BlacklistItem> blacklistItems)
    {
        return removeBlacklist(blacklistItems, null);
    }

    public List<UserEntity> addEntities(List<UserEntity> entities, String config_id)
    {
        AuthRequest req = add(entities, "entities", config_id, UserEntities.class);
        UserEntities list = (UserEntities)serializer.deserialize(req.getResponse(), UserEntities.class);
        if (list != null) {
            return list.getEntities();
        } else {
            return new ArrayList<UserEntity>();
        }
    }

    public List<UserEntity> addEntities(List<UserEntity> entities)
    {
        return addEntities(entities, null);
    }

    public List<UserEntity> updateEntities(List<UserEntity> entities)
    {
        return updateEntities(entities, null);
    }

    public List<UserEntity> updateEntities(List<UserEntity> entities, String config_id)
    {
        AuthRequest req = update(entities, "entities", config_id, UserEntities.class, "PUT");
        UserEntities list = (UserEntities)serializer.deserialize(req.getResponse(), UserEntities.class);
        if (list != null) {
            return list.getEntities();
        } else {
            return new ArrayList<UserEntity>();
        }
    }

    public Integer removeEntities(List<UserEntity> entities)
    {
        return removeEntities(entities, null);
    }

    public Integer removeEntities(List<UserEntity> entities, String config_id)
    {
        return remove(entities, "entities", config_id, UserEntities.class);
    }

    public List<TaxonomyNode> addTaxonomy(List<TaxonomyNode> taxonomies, String config_id)
    {
        AuthRequest req = add(taxonomies, "taxonomy", config_id, Taxonomies.class);
        Taxonomies list = (Taxonomies)serializer.deserialize(req.getResponse(), Taxonomies.class);
        if (list != null) {
            return list.getTaxonomies();
        } else {
            return new ArrayList<TaxonomyNode>();
        }
    }

    public List<TaxonomyNode> addTaxonomy(List<TaxonomyNode> taxonomies)
    {
        return addTaxonomy(taxonomies, null);
    }

    public List<TaxonomyNode> updateTaxonomy(List<TaxonomyNode> taxonomies)
    {
        return updateTaxonomy(taxonomies, null);
    }

    public List<TaxonomyNode> updateTaxonomy(List<TaxonomyNode> taxonomies, String config_id)
    {
        AuthRequest req = update(taxonomies, "taxonomy", config_id, Taxonomies.class , "PUT");
        Taxonomies list = (Taxonomies)serializer.deserialize(req.getResponse(), Taxonomies.class);
        if (list != null) {
            return list.getTaxonomies();
        } else {
            return new ArrayList<TaxonomyNode>();
        }
    }

    public Integer removeTaxonomy(List<TaxonomyNode> taxonomies)
    {
        return removeTaxonomy(taxonomies, null);
    }

    public Integer removeTaxonomy(List<TaxonomyNode> taxonomies, String config_id)
    {
        return remove(taxonomies, "taxonomy", config_id, Taxonomies.class);
    }

    public List<Configuration> addConfigurations(List<Configuration> configurations)
    {
        return addConfigurations(configurations, null);
    }

    private List<Configuration> addConfigurations(List<Configuration> configurations, String config_id)
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

        AuthRequest req = add(configurations, "configurations", config_id, Configurations.class);
        Configurations list = (Configurations)serializer.deserialize(req.getResponse(), Configurations.class);
        if (list != null) {
            return list.getConfigurations();
        } else {
            return new ArrayList<Configuration>();
        }
    }

    public Configuration cloneConfigurations(String name, String template)
    {
        Configuration configuration = new Configuration();
        configuration.setName(name);
        configuration.setTemplate(template);

        List<Configuration> configurations = new ArrayList<Configuration>();
        configurations.add(configuration);

        AuthRequest req = add(configurations, "configurations", null, Configurations.class);
        Configurations list = (Configurations)serializer.deserialize(req.getResponse(), Configurations.class);
        if (list != null) {
            return list.getConfigurations().get(0);
        } else {
            return new Configuration();
        }
    }

    public List<Configuration> updateConfigurations(List<Configuration> configurations)
    {
        return updateConfigurations(configurations, null);
    }

    private List<Configuration> updateConfigurations(List<Configuration> configurations, String config_id)
    {
        AuthRequest req = update(configurations, "configurations", config_id, Configurations.class, "PUT");
        Configurations list = (Configurations)serializer.deserialize(req.getResponse(), Configurations.class);
        if (list != null) {
            return list.getConfigurations();
        } else {
            return new ArrayList<Configuration>();
        }
    }

    public Integer removeConfigurations(List<Configuration> configurations)
    {
        return removeConfigurations(configurations, null);
    }
    private Integer removeConfigurations(List<Configuration> configurations, String config_id)
    {
        return remove(configurations, "configurations", config_id, Configurations.class);
    }

    private <T> AuthRequest update(List<?> items, String action, String config_id, Class<?> type)
    {
        return update(items, action, config_id, type, "POST");
    }

    private <T> AuthRequest update(List<?> items, String action, String config_id, Class<?> type, String requestMethod)
    {
        return update(requestMethod, items, action, config_id, type);
    }

    private <T> AuthRequest add(List<?> items, String action, String config_id, Class<?> type)
    {
        return update("POST", items, action, config_id, type);
    }

    private <T> Integer remove(List<?> items, String action, String config_id, Class<?> type)
    {
        AuthRequest req = update("DELETE", items, action, config_id, type);
        return req.getStatus();
    }

    private <T> AuthRequest update(String method, List<?> items, String action, String config_id, Class<?> type)
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

        return req  ;
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
        String url = generateRequestUrl("document/", id);

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
        String url = generateRequestUrl("collection/", id);

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
