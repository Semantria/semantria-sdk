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
    //<editor-fold desc="Private fields">

    private static final String WRAPPER_NAME = "Java";

    private String key = "";
    private String secret = "";
    private String appName = "";
    private String apiVersion = "4.2";
    private ISerializer serializer = null;
    private String requestFormat = "json";
    private ICallbackHandler callback = null;
    private String serviceUrl = "https://api.semantria.com";
    public  boolean useCompression = false;

    //</editor-fold>

    //<editor-fold desc="Constructor">

    private Session(String key, String secret) {
        this.key = key;
        this.secret = secret;
        registerSerializer(new JsonSerializer());
    }

    //</editor-fold>

    //<editor-fold desc="Public static">

    /**
     * Creates new Semantria session using API keys.
     * @param key API key
     * @param secret API secret
     * @return An instance of Session object.
     */
    public static Session createSession(String key, String secret) {
        return new Session(key, secret);
    }

    /**
     * Creates Semantria session using user's credentials.
     * @param username Username
     * @param password Password
     * @return An instance of Session object.
     * @throws CredentialException
     */
    public static Session createUserSession(String username, String password) throws CredentialException {
        return Session.createUserSession(username, password, true);
    }

    /**
     * Creates Semantria session using user's credentials.
     * @param username Username
     * @param password Password
     * @param reuseExisting Indicates whether to re-use previously created session or create a new one.
     * @return An instance of Session object.
     * @throws CredentialException
     */
    public static Session createUserSession(String username, String password, boolean reuseExisting) throws CredentialException  {
        AuthService authService = new AuthService();
        authService.authWithUsernameAndPassword(username, password, reuseExisting);

        return new Session(authService.getKey(), authService.getSecret());
    }

    //</editor-fold>

    //<editor-fold desc="Public properties">

    /**
     * Registers serializer for already instantiated session to use another data format if necessary.
     * @param cserializer Either XML or JSON serializer to use the specific data format while access the API.
     */
    public void registerSerializer(ISerializer cserializer)
    {
        serializer = cserializer;
        if (null != cserializer) {
            requestFormat = cserializer.getType();
        }
    }

    /**
     * Indicates whether to use HTTP compression or not.
     * @param useCompression
     */
    public void useCompression(boolean useCompression) {
        this.useCompression = useCompression;
    }

    /**
     * Sets the name of the application.
     * @param appName Name of the current application to be reported to the server.
     */
    private void setAppName(String appName) {
        if (null != appName) {
            this.appName = appName + "." + WRAPPER_NAME;
        } else {
            this.appName = WRAPPER_NAME;
        }
    }

    /**
     * Forces Session to use specific API version.
     * @param apiVersion API version (4.2 used by default).
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }


    /**
     * Registers ICallbackHandler implementer that will be executed in case of corresponding events.
     * @param handler ICallbackHandler implementer.
     */
    public void setCallbackHandler(ICallbackHandler handler)
    {
        callback = handler;
    }

    //</editor-fold>

    //<editor-fold desc="Basic API methods">

    /**
     * Retrieves Semantria API status.
     * @return Status object with a bunch of fields related to the API status.
     */
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

    /**
     * Retrieves Semantria Subscription.
     * @return Subscription object with a bunch of subscription related fields.
     */
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

    /**
     * Retrieves usage statistics for the current subscription and given configuration.
     * @param interval Time interval to filter usage statistics.
     * @param configId Configuration ID to get usage statistics for.
     * @return Statistics object with a bunch of fields related to the API usage.
     */
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

    /**
     * Retrieves the list of supported features grouped by languages.
     * @param language Language name to get supported features for the certain language.
     * @return The list of supported features for each language offered by Semantria.
     */
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

    //</editor-fold>

    //<editor-fold desc="Categories methods">

    /**
     * Retrieves categories from the server.
     * @param config_id Optional configuration ID the categories are associated with. If not provided, primary configuration will be used.
     * @return The list of categories.
     */
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

    /**
     * Adds categories to the server.
     * @param categories The list of categories to be added to the server.
     * @param config_id Optional configuration ID the categories are associated with. If not provided, primary configuration will be used.
     * @return The list of just added categories (with auto-generated IDs, modified timestamps, etc.)
     */
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

    /**
     * Updates categories on the server.
     * @param categories The list of categories to be updated on the server.
     * @param config_id Optional configuration ID the categories are associated with. If not provided, primary configuration will be used.
     * @return The list of just updated categories.
     */
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

    /**
     * Removes categories from the server.
     * @param categories The list of categories to be removed from the server.
     * @param config_id Optional configuration ID the categories are associated with. If not provided, primary configuration will be used.
     * @return Operation execution result, actually HTTP status code.
     */
    public Integer removeCategories(List<Category> categories, String config_id)
    {
        return remove(categories, "categories", config_id, Categories.class);
    }

    //</editor-fold>

    //<editor-fold desc="Queries methods">

    /**
     * Retrieves queries from the server.
     * @param config_id Optional configuration ID the queries are associated with. If not provided, primary configuration will be used.
     * @return The list of queries.
     */
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

    /**
     * Add queries to the server.
     * @param queries The list of queries to be added to the server.
     * @param config_id Optional configuration ID the queries are associated with. If not provided, primary configuration will be used.
     * @return The list of just added queries (with auto-generated IDs, modified timestamps, etc.)
     */
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

    /**
     * Updates queries on the server.
     * @param queries The list of queries to be updated on the server.
     * @param config_id Optional configuration ID the queries are associated with. If not provided, primary configuration will be used.
     * @return The list of just updated queries.
     */
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

    /**
     * Removes queries from the server.
     * @param queries The list of queries to be removed from the server.
     * @param config_id Optional configuration ID the queries are associated with. If not provided, primary configuration will be used.
     * @return Operation execution result, actually HTTP status code.
     */
    public Integer removeQueries(List<Query> queries, String config_id)
    {
        return remove(queries, "queries", config_id, Queries.class);
    }

    //</editor-fold>

    //<editor-fold desc="Sentiment phrases methods">

    /**
     * Retrieves sentiment-bearing phrases from the server.
     * @param config_id Optional configuration ID the phrases are associated with. If not provided, primary configuration will be used.
     * @return The list of sentiment-bearing phrases.
     */
    public List<SentimentPhrase> getSentimentPhrases(String config_id)
    {
        String method = "GET";
        String url = generateRequestUrl("phrases");

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

    /**
     * Adds sentiment-bearing phrases to the server.
     * @param phrases The list of sentiment-bearing phrases to be added to the server.
     * @param config_id Optional configuration ID the sentiment-bearing phrases are associated with. If not provided, primary configuration will be used.
     * @return The list of just added sentiment-bearing phrases (with auto-generated IDs, modified timestamps, etc.)
     */
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

    /**
     * Updates sentiment-bearing phrases on the server.
     * @param phrases The list of sentiment-bearing phrases to be updated on the server.
     * @param config_id Optional configuration ID the sentiment-bearing phrases are associated with. If not provided, primary configuration will be used.
     * @return The list of just updated sentiment-bearing phrases.
     */
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

    /**
     * Removes sentiment-bearing phrases from the server.
     * @param phrases The list of sentiment-bearing phrases to be removed from the server.
     * @param config_id Optional configuration ID the sentiment-bearing phrases are associated with. If not provided, primary configuration will be used.
     * @return Operation execution result, actually HTTP status code.
     */
    public Integer removeSentimentPhrases(List<SentimentPhrase> phrases, String config_id)
    {
        return remove(phrases, "phrases", config_id, SentimentPhrases.class);
    }

    //</editor-fold>

    //<editor-fold desc="Blacklist methods">

    /**
     * Retrieves the list of blacklisted items from the server.
     * @param config_id Optional configuration ID the blacklist is associated with. If not provided, primary configuration will be used.
     * @return The list of Blacklisted items.
     */
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

    /**
     * Adds blacklisted items to the server.
     * @param blacklistItems The list of blacklisted items to be added to the server.
     * @param config_id Optional configuration ID the blacklist is associated with. If not provided, primary configuration will be used.
     * @return The list of just added blacklisted items (with auto-generated IDs, modified timestamps, etc.)
     */
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

    /**
     * Updates blacklisted items on the server.
     * @param blacklistItems The list of blacklisted items to be updated on the server.
     * @param config_id Optional configuration ID the blacklist is associated with. If not provided, primary configuration will be used.
     * @return The list of just updated blacklisted items.
     */
    public List<BlacklistItem> updateBlacklist(List<BlacklistItem> blacklistItems, String config_id)
    {
        AuthRequest req = update(blacklistItems, "blacklist", config_id, Blacklists.class, "PUT");
        Blacklists list = (Blacklists)serializer.deserialize(req.getResponse(), Blacklists.class);
        if (list != null) {
            return list.getBlacklist();
        } else {
            return new ArrayList<BlacklistItem>();
        }
    }

    /**
     * Removes blacklisted items from the server.
     * @param blacklistItems The list of blacklisted items to be removed from the server.
     * @param config_id Optional configuration ID the blacklist is associated with. If not provided, primary configuration will be used.
     * @return Operation execution result, actually HTTP status code.
     */
    public Integer removeBlacklist(List<BlacklistItem> blacklistItems, String config_id)
    {
        return remove(blacklistItems, "blacklist", config_id, Blacklists.class);
    }

    //</editor-fold>

    //<editor-fold desc="Entities methods">

    /**
     * Retrieves entities from the server.
     * @param config_id Optional configuration ID the entities are associated with. If not provided, primary configuration will be used.
     * @return The list of entities.
     */
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

    /**
     * Adds entities to the server.
     * @param entities The list of entities to be added to the server.
     * @param config_id Optional configuration ID the entities are associated with. If not provided, primary configuration will be used.
     * @return The list of just added entities (with auto-generated IDs, modified timestamps, etc.)
     */
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

    /**
     * Updates entities on the server.
     * @param entities The list of entities to be updated on the server.
     * @param config_id Optional configuration ID the entities are associated with. If not provided, primary configuration will be used.
     * @return The list of just updated entities.
     */
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

    /**
     * Removes entities from the server.
     * @param entities The list of entities to be removed from the server.
     * @param config_id Optional configuration ID the entities are associated with. If not provided, primary configuration will be used.
     * @return Operation execution result, actually HTTP status code.
     */
    public Integer removeEntities(List<UserEntity> entities, String config_id)
    {
        return remove(entities, "entities", config_id, UserEntities.class);
    }

    //</editor-fold>

    //<editor-fold desc="Taxonomy methods">

    /**
     * Retrieves taxonomy from the server.
     * @param config_id Optional configuration ID the taxonomy is associated with. If not provided, primary configuration will be used.
     * @return The list of taxonomy nodes.
     */
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

    /**
     * Adds taxonomy nodes to the server.
     * @param nodes The list of taxonomy nodes to be added to the server.
     * @param config_id Optional configuration ID the taxonomy is associated with. If not provided, primary configuration will be used.
     * @return The list of just added taxonomy nodes (with auto-generated IDs, modified timestamps, etc.)
     */
    public List<TaxonomyNode> addTaxonomy(List<TaxonomyNode> nodes, String config_id)
    {
        AuthRequest req = add(nodes, "taxonomy", config_id, Taxonomies.class);
        Taxonomies list = (Taxonomies)serializer.deserialize(req.getResponse(), Taxonomies.class);
        if (list != null) {
            return list.getTaxonomies();
        } else {
            return new ArrayList<TaxonomyNode>();
        }
    }

    /**
     * Updates taxonomy nodes on the server.
     * @param nodes The list of taxonomy nodes to be updated on the server.
     * @param config_id Optional configuration ID the taxonomy is associated with. If not provided, primary configuration will be used.
     * @return The list of just updated taxonomy nodes.
     */
    public List<TaxonomyNode> updateTaxonomy(List<TaxonomyNode> nodes, String config_id)
    {
        AuthRequest req = update(nodes, "taxonomy", config_id, Taxonomies.class , "PUT");
        Taxonomies list = (Taxonomies)serializer.deserialize(req.getResponse(), Taxonomies.class);
        if (list != null) {
            return list.getTaxonomies();
        } else {
            return new ArrayList<TaxonomyNode>();
        }
    }

    /**
     * Removes taxonomy nodes from the server.
     * @param taxonomies The list of taxonomy nodes to be removed from the server.
     * @param config_id Optional configuration ID the taxonomy is associated with. If not provided, primary configuration will be used.
     * @return Operation execution result, actually HTTP status code.
     */
    public Integer removeTaxonomy(List<TaxonomyNode> taxonomies, String config_id)
    {
        return remove(taxonomies, "taxonomy", config_id, Taxonomies.class);
    }

    //</editor-fold>

    //<editor-fold desc="Configurations methods">

    /**
     * Retrieves the list of configurations from the server.
     * @return The list of configurations.
     */
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

    /**
     * Adds configurations to the server.
     * @param configurations The list of configurations to be added to the server.
     * @return The list of just added configurations (with auto-generated IDs, modified timestamps, etc.)
     */
    public List<Configuration> addConfigurations(List<Configuration> configurations)
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

        AuthRequest req = add(configurations, "configurations", null, Configurations.class);
        Configurations list = (Configurations)serializer.deserialize(req.getResponse(), Configurations.class);
        if (list != null) {
            return list.getConfigurations();
        } else {
            return new ArrayList<Configuration>();
        }
    }

    /**
     * Clones configuration from the given template and entitles to the given name.
     * @param name New configuration name.
     * @param template Configuration ID to be cloned and used as template.
     * @return New configuration just created using the given template.
     */
    public Configuration cloneConfiguration(String name, String template)
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

    /**
     * Updates configurations on the server side.
     * @param configurations The list of configurations to be updated on the server.
     * @return The list of just updated configurations.
     */
    public List<Configuration> updateConfigurations(List<Configuration> configurations)
    {
        AuthRequest req = update(configurations, "configurations", null, Configurations.class, "PUT");
        Configurations list = (Configurations)serializer.deserialize(req.getResponse(), Configurations.class);
        if (list != null) {
            return list.getConfigurations();
        } else {
            return new ArrayList<Configuration>();
        }
    }

    /**
     * Removes configurations from the server.
     * @param configurations The list of configuration IDs to be removed.
     * @return Operation execution result, actually HTTP status code.
     */
    public Integer removeConfigurations(List<Configuration> configurations)
    {
        return remove(configurations, "configurations", null, Configurations.class);
    }

    //</editor-fold>

    //<editor-fold desc="Documents analysis methods">

    /**
     * Queues the document for analysis using given configuration.
     * @param task Document to be analyzed.
     * @param config_id Optional configuration ID. If not provided, primary configuration will be used for analysis.
     * @return Operation execution result, actually HTTP status code.
     */
    public Integer queueDocument(Document task, String config_id)
    {
        String method = "POST";
        String url = generateRequestUrl("document");
        String body = serializer.serialize(task);

        AuthRequest req = AuthRequest.getInstance(url, method)
                .key(key)
                .secret(secret)
                .body(body)
                .config_id(config_id)
                .appName(appName)
                .apiVersion(apiVersion)
                .useCompression(useCompression);

        Integer status = req.doRequest();
        onAutoResponse(status, req.getResponse(), false);

        handleRequest(method, req.getRequestUrl(), body);

        return status;
    }

    /**
     * Retrieves docuemnt analysis results by the certain document/configuration ID from the server.
     * @param id Document ID to retrieve the anlaysis results.
     * @param config_id Optional configuration ID. If not provided, primary configuration will be used.
     * @return Docuemnt analysis results object with a bunch of result fields.
     */
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

    /**
     * Cancels specific document on the server side.
     * @param id Document ID to be cancel.
     * @param config_id Optional configuration ID. If not provided, primary configuration will be used.
     * @return Operation execution result, actually HTTP status code.
     */
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

    /**
     * Queues a batch of documents for analysis using given given configuration.
     * @param tasks Batch of documents to be analyzed.
     * @param config_id Optional configuration ID. If not provided, primary configuration will be used for analysis.
     * @return Operation execution result, actually HTTP status code.
     */
    public Integer QueueBatchOfDocuments(List<Document> tasks, String config_id)
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
        onAutoResponse(status, req.getResponse(), false);

        handleRequest(method, req.getRequestUrl(), body);

        return status;
    }

    /**
     * Retrieves document analysis results from the server by the given configuration.
     * @param config_id Optional configuration ID. If not provided, primary configuration will be used.
     * @return The list of document analysis results retrieved from the server for recently queued documents.
     */
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

    /**
     * Retrieves document analysis results by the given Job identifier.
     * @param jobId Unique Job identifier used while documents queuing.
     * @return The list of document analysis results retrieved from the server for the given Job ID.
     */
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

    //</editor-fold>

    //<editor-fold desc="Collections analysis methods">
    /**
     * Queues the collection for analysis using given configuration.
     * @param collection Collection to be analyzed.
     * @param config_id Optional configuration ID. If not provided, primary configuration will be used for analysis.
     * @return Operation execution result, actually HTTP status code.
     */
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
        onAutoResponse(status, req.getResponse(), true);

        handleRequest(method, req.getRequestUrl(), body);

        return status;
    }

    /**
     * Retrieves collection analysis results by the certain collection/configuration ID from the server.
     * @param id Document ID to retrieve the anlaysis results.
     * @param config_id Optional configuration ID. If not provided, primary configuration will be used for analysis.
     * @return Collection analysis results object with a bunch of result fields.
     */
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

    /**
     * Cancels specific collection on the server side.
     * @param id Collection ID to be canceled.
     * @param config_id Optional configuration ID. If not provided, primary configuration will be used for analysis.
     * @return Operation execution result, actually HTTP status code.
     */
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

    /**
     * Retrieves collection analysis results from the server by the given configuration.
     * @param config_id Optional configuration ID. If not provided, primary configuration will be used.
     * @return The list of collection analysis results retrieved from the server for recently queued collections.
     */
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

    /**
     * Retrieves collection analysis results by the given Job identifier.
     * @param jobId Unique Job identifier used while collections queuing.
     * @return The list of collection analysis results retrieved from the server for the given Job ID.
     */
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

    //</editor-fold>

    //<editor-fold desc="Private methods">

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

    private void onAutoResponse(Integer status, String message, Boolean isCollection) {
        if (callback != null) {
            if (status <= 202) {
                callback.onResponse(this, new ResponseArgs(status, message));
            }
            if (status > 202) {
                callback.onError(this, new ResponseArgs(status, message));
            }
            if (!message.isEmpty() && status < 202) {
                if (!isCollection) {
                    DocsAnalyticData taskList = (DocsAnalyticData) serializer.deserialize(message, DocsAnalyticData.class);
                    callback.onDocsAutoResponse(this, taskList.getDocuments());
                } else {
                    CollsAnalyticData taskList = (CollsAnalyticData) serializer.deserialize(message, CollsAnalyticData.class);
                    callback.onCollsAutoResponse(this, taskList.getDocuments());
                }
            }
        }
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

    private void handleResponse(Integer status, AuthRequest ar)
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

    private void handleRequest(String method, String url) {
        handleRequest(method, url, null);
    }

    private void handleRequest(String method, String url, String message)
    {
        if (callback != null)
        {
            callback.onRequest(this, new RequestArgs(method, url, message));
        }
    }

    //</editor-fold>
}
