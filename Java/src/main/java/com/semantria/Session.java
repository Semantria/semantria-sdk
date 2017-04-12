package com.semantria;

import com.semantria.auth.AuthService;
import com.semantria.auth.CredentialException;
import com.semantria.interfaces.ICallbackHandler;
import com.semantria.interfaces.ISerializer;
import com.semantria.mapping.Batch;
import com.semantria.mapping.Collection;
import com.semantria.mapping.Document;
import com.semantria.mapping.configuration.BlacklistItem;
import com.semantria.mapping.configuration.Category;
import com.semantria.mapping.configuration.Configuration;
import com.semantria.mapping.configuration.Query;
import com.semantria.mapping.configuration.SentimentPhrase;
import com.semantria.mapping.configuration.TaxonomyNode;
import com.semantria.mapping.configuration.UserEntity;
import com.semantria.mapping.configuration.stub.Blacklists;
import com.semantria.mapping.configuration.stub.Categories;
import com.semantria.mapping.configuration.stub.Configurations;
import com.semantria.mapping.configuration.stub.Queries;
import com.semantria.mapping.configuration.stub.SentimentPhrases;
import com.semantria.mapping.configuration.stub.Taxonomies;
import com.semantria.mapping.configuration.stub.UserEntities;
import com.semantria.mapping.output.CollAnalyticData;
import com.semantria.mapping.output.DocAnalyticData;
import com.semantria.mapping.output.FeaturesSet;
import com.semantria.mapping.output.ServiceStatus;
import com.semantria.mapping.output.Subscription;
import com.semantria.mapping.output.statistics.StatisticsGrouped;
import com.semantria.mapping.output.statistics.StatisticsOverall;
import com.semantria.mapping.output.statistics.StatsInterval;
import com.semantria.mapping.output.stub.CollsAnalyticData;
import com.semantria.mapping.output.stub.DocsAnalyticData;
import com.semantria.mapping.output.stub.FeaturesList;
import com.semantria.mapping.output.stub.StatisticsGroupedList;
import com.semantria.mapping.output.stub.StatisticsOverallList;
import com.semantria.serializer.JsonSerializer;
import com.semantria.serializer.XmlSerializer;
import com.semantria.utils.AuthRequest;
import com.semantria.utils.ObjProxy;
import com.semantria.utils.RequestArgs;
import com.semantria.utils.ResponseArgs;
import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public final class Session
{
    //<editor-fold desc="Private fields">

    private String key = "";
    private String secret = "";
    private String apiVersion = "4.2";
    private ISerializer serializer = null;
    private String requestFormat = "json";
    private ICallbackHandler callback = null;
    private String serviceUrl = "https://api.semantria.com/";
    private boolean useCompression = false;
    private Integer lastRequestStatus = 0;
    private String lastRequestErrorMessage = null;

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
     * @param serializer Either XML or JSON serializer to use the specific data format while access the API.
     */
    public void registerSerializer(ISerializer serializer)
    {
        this.serializer = serializer;
        if (null != serializer) {
            requestFormat = serializer.getType();
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
     * Sets host to use for Semantria API service.
     * @param new_url
     */
    public void setServiceUrl(String new_url) {
        if (! new_url.toLowerCase().startsWith("http")) {
            new_url = "https://" + new_url;
        }
        if (! new_url.endsWith("/")) {
            new_url = new_url + "/";
        }
        serviceUrl = new_url;
    }

    /**
     * Forces Session to use specific API version.
     * @param apiVersion API version (4.2 used by default).
     */
    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }


    /**
     * Registers a callback instance to be called in case of corresponding events.
     * @param handler ICallbackHandler implementer.
     */
    public void setCallbackHandler(ICallbackHandler handler)
    {
        callback = handler;
    }

    /**
     * Returns the error message, if any, from the last request; return null if there was no error.
     */
    public String getLastRequestErrorMessage() {
        return lastRequestErrorMessage;
    }

    /**
     * Returns HTTP status of last request.
     */
    public Integer getLastRequestStatus() {
        return lastRequestStatus;
    }

    //</editor-fold>

    //<editor-fold desc="Basic API methods">

    /**
     * Retrieves Semantria API status.
     * @return Status object with a bunch of fields related to the API status.
     */
    public ServiceStatus getStatus()
    {
        AuthRequest req = makeAuthRequest("status", "GET");
        Integer status = doRequest(req);
        if (status <= 202) {
            return (ServiceStatus) serializer.deserialize(req.getResponse(), ServiceStatus.class);
        } else {
            return null;
        }
    }

    /**
     * Retrieves Semantria Subscription.
     * @return Subscription object with a bunch of subscription related fields.
     */
    public Subscription getSubscription()
    {
        AuthRequest req = makeAuthRequest("subscription", "GET");
        Integer status = doRequest(req);
        return (Subscription)serializer.deserialize(req.getResponse(), Subscription.class);
    }

    /**
     * Retrieves usage statistics for the given interval.
     * @param interval - Time interval.
     * @return Statistics object with a bunch of fields.
     */
    public StatisticsOverall getStatistics(StatsInterval interval)
    {
        return getStatisticsOverall(interval, null, null);
    }

    /**
     * Retrieves usage statistics for the given interval.
     * @param from - Start DateTime point to get usage statistics for.
     * @param to - End DateTime point to get usage statistics for.
     * @return Statistics object with a bunch of fields.
     */
    public StatisticsOverall getStatistics(Date from, Date to)
    {
        return getStatisticsOverall(null, from, to);
    }

    /**
     * Retrieves usage statistics for the given time interval grouped by the given criteria.
     * @param interval - Time interval.
     * @param groupBy - config_id, config_name, user_id, user_email, language, app and time values or their combinations supported.
     * If several parameters use, they should be separated by, like "language,user_name".
     * Time value can't be less than one minute - 1m. Supported following time units: m - minutes, h - hours, d - days, w - weeks.
     * Combining time values and grouping parameters allowed: "language,user_name,10m"
     * @return Statistics object with a bunch of fields.
     */
    public List<StatisticsGrouped> getStatistics(StatsInterval interval, String groupBy)
    {
        return getStatisticsGrouped(interval, null, null, groupBy);
    }

    /**
     * Retrieves usage statistics for the given time interval grouped by the given criteria.
     * @param from - Start DateTime point to get usage statistics for.
     * @param to - End DateTime point to get usage statistics for.
     * @param groupBy - config_id, config_name, user_id, user_email, language, app and time values or their combinations supported.
     * If several parameters use, they should be separated by, like "language,user_name".
     * Time value can't be less than one minute - 1m. Supported following time units: m - minutes, h - hours, d - days, w - weeks.
     * Combining time values and grouping parameters allowed: "language,user_name,10m"
     * @return Statistics object with a bunch of fields.
     */
    public List<StatisticsGrouped> getStatistics(Date from, Date to, String groupBy)
    {
        return getStatisticsGrouped(null, from, to, groupBy);
    }

    private StatisticsOverall getStatisticsOverall(StatsInterval interval, Date from, Date to)
    {
        AuthRequest req = getStatisticsRequest(interval, from, to, null);

        Integer status = doRequest(req);
        StatisticsOverallList statisticsList = (StatisticsOverallList)serializer.deserialize(req.getResponse(), StatisticsOverallList.class);

        List<StatisticsOverall> result = statisticsList.getStatistics();

        if (!result.isEmpty()) {
            return result.get(0);
        } else {
            return null;
        }
    }

    private List<StatisticsGrouped> getStatisticsGrouped(StatsInterval interval, Date from, Date to, String groupBy)
    {
        AuthRequest req = getStatisticsRequest(interval, from, to, groupBy);
        Integer status = doRequest(req);
        StatisticsGroupedList statisticsList = (StatisticsGroupedList)serializer.deserialize(req.getResponse(), StatisticsGroupedList.class);

        return statisticsList.getStatistics();
    }

    private AuthRequest getStatisticsRequest(StatsInterval interval, Date from, Date to, String groupBy)
    {
        String fromStr = null;
        String toStr = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

        if (from != null) {
            fromStr = format.format(from);
        }

        if (to != null) {
            toStr = format.format(to);
        }

		return makeAuthRequest("statistics", "GET")
                .interval(interval)
                .from(fromStr)
                .to(toStr)
                .groupBy(groupBy);
    }

    /**
     * Retrieves the list of supported features grouped by languages.
     * @param language Language name to get supported features for the certain language.
     * @return The list of supported features for each language offered by Semantria.
     */
    public List<FeaturesSet> getSupportedFeatures(final String language)
    {
        AuthRequest req = makeAuthRequest("features", "GET")
                .language(language);
        Integer status = doRequest(req);
        FeaturesList supportedFeatures = (FeaturesList)serializer.deserialize(req.getResponse(), FeaturesList.class);

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
        AuthRequest req = makeAuthRequest("categories", "GET", config_id);
        Integer status = doRequest(req);
        Categories list = (Categories)serializer.deserialize(req.getResponse(), Categories.class);

        if (list != null) {
            return list.getCategories();
        }

        return Collections.emptyList();
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
            return Collections.emptyList();
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
            return Collections.emptyList();
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
        AuthRequest req = makeAuthRequest("queries", "GET", config_id);
        Integer status = doRequest(req);
        Queries list = (Queries)serializer.deserialize(req.getResponse(), Queries.class);

        if (list != null) {
            return list.getQueries();
        }

        return Collections.emptyList();
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
        }
        return Collections.emptyList();
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
        }
        return Collections.emptyList();
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
        AuthRequest req = makeAuthRequest("phrases", "GET", config_id);
        Integer status = doRequest(req);
        SentimentPhrases list = (SentimentPhrases)serializer.deserialize(req.getResponse(), SentimentPhrases.class);

        if (list != null) {
            return list.getSentimentPhrases();
        }

        return Collections.emptyList();
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
        }
        return Collections.emptyList();
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
        }
        return Collections.emptyList();
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
        AuthRequest req = makeAuthRequest("blacklist", "GET", config_id);
        Integer status = doRequest(req);
        Blacklists list = (Blacklists)serializer.deserialize(req.getResponse(), Blacklists.class);

        if (list != null) {
            return list.getBlacklist();
        }
        return Collections.emptyList();
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
        }
        return Collections.emptyList();
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
        }
        return Collections.emptyList();
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
        AuthRequest req = makeAuthRequest("entities", "GET", config_id);
        Integer status = doRequest(req);
        UserEntities list = (UserEntities)serializer.deserialize(req.getResponse(), UserEntities.class);

        if (list != null) {
            return list.getEntities();
        }
        return Collections.emptyList();
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
        }
        return Collections.emptyList();
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
        }
        return Collections.emptyList();
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
        AuthRequest req = makeAuthRequest("taxonomy", "GET", config_id);
        Integer status = doRequest(req);
        Taxonomies list = (Taxonomies)serializer.deserialize(req.getResponse(), Taxonomies.class);

        if (list != null) {
            return list.getTaxonomies();
        }
        return Collections.emptyList();
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
        }
        return Collections.emptyList();
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
        }
        return Collections.emptyList();
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
        AuthRequest req = makeAuthRequest("configurations", "GET");
        Integer status = doRequest(req);
        Configurations list = (Configurations)serializer.deserialize(req.getResponse(), Configurations.class);

        if (list != null) {
            return list.getConfigurations();
        }
        return Collections.emptyList();
    }

    /**
     * Adds configurations to the server.
     * @param configurations The list of configurations to be added to the server.
     * @return The list of just added configurations (with auto-generated IDs, modified timestamps, etc.)
     */
    public List<Configuration> addConfigurations(List<Configuration> configurations)
    {
        if( configurations != null && !configurations.isEmpty() ) {
            for (Configuration configuration : configurations) {
                if( configuration.getTemplate() != null ) {
					configuration.setId( null );
				}
            }
        }

        AuthRequest req = add(configurations, "configurations", null, Configurations.class);
        Configurations list = (Configurations)serializer.deserialize(req.getResponse(), Configurations.class);
        if (list != null) {
            return list.getConfigurations();
        }
        return Collections.emptyList();
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
        }
        return Collections.emptyList();
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
        String body = serializer.serialize(task);
        AuthRequest req = makeAuthRequest("document", "POST", config_id)
                .body(body);
        Integer status = doRequest(req, body, true, false);
        return status;
    }

    /**
     * Retrieves docuemnt analysis results by the certain document/configuration ID from the server.
     * @param id Document ID to retrieve the anlaysis results.
     * @param config_id Optional configuration ID. If not provided, primary configuration will be used.
     * @return Docuemnt analysis results object with a bunch of result fields.
     */
    public DocAnalyticData getDocument(String id, String config_id)
    {
        String path = generateRequestPathWithId("document", id);
        AuthRequest req = makeAuthRequest(path, "GET", config_id);
        Integer status = doRequest(req);

        DocAnalyticData result = null;
        if (200 == status) {
            result = (DocAnalyticData)serializer.deserialize(req.getResponse(), DocAnalyticData.class);
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
        String path = generateRequestPathWithId("document", id);
        AuthRequest req = makeAuthRequest(path, "DELETE", config_id);
        Integer status = doRequest(req);

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
        String body = null;
        if (serializer instanceof JsonSerializer) {
            body = serializer.serialize(tasks);
        } else if (serializer instanceof XmlSerializer) {
            body = serializer.serialize(ObjProxy.wrap(tasks, Batch.class, "POST"));
        }

        AuthRequest req = makeAuthRequest("document/batch", "POST", config_id)
                .body(body);
        Integer status = doRequest(req, body, true, false);
        return status;
    }

    /**
     * Retrieves document analysis results from the server by the given configuration.
     * @param config_id Optional configuration ID. If not provided, primary configuration will be used.
     * @return The list of document analysis results retrieved from the server for recently queued documents.
     */
    public List<DocAnalyticData> getProcessedDocuments(String config_id)
    {
        AuthRequest req = makeAuthRequest("document/processed", "GET", config_id);
        Integer status = doRequest(req);

        if (200 == status) {
			DocsAnalyticData taskList = (DocsAnalyticData)serializer.deserialize(req.getResponse(), DocsAnalyticData.class);
			if (taskList != null) {
				return taskList.getDocuments();
			}
		}
        return Collections.emptyList();
    }

    /**
     * Retrieves document analysis results by the given Job identifier.
     * @param jobId Unique Job identifier used while documents queuing.
     * @return The list of document analysis results retrieved from the server for the given Job ID.
     */
    public List<DocAnalyticData> getProcessedDocumentsByJobId(final String jobId)
    {
        AuthRequest req = makeAuthRequest("document/processed", "GET")
                .job_id(jobId);
        Integer status = doRequest(req);

        if (200 == status) {
			DocsAnalyticData taskList = (DocsAnalyticData)serializer.deserialize(req.getResponse(), DocsAnalyticData.class);
			if (taskList != null) {
				return taskList.getDocuments();
			}
		}
        return Collections.emptyList();
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
        String body = serializer.serialize(collection);
        AuthRequest req = makeAuthRequest("collection", "POST", config_id)
                .body(body);
        Integer status = doRequest(req, body, true, true);
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
        String path = generateRequestPathWithId("collection", id);
        AuthRequest req = makeAuthRequest(path, "GET", config_id);
		Integer status = doRequest(req);

        CollAnalyticData result = null;
        if (200 == status) {
            result = (CollAnalyticData)serializer.deserialize(req.getResponse(), CollAnalyticData.class);

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
        String path = generateRequestPathWithId("collection", id);
        AuthRequest req = makeAuthRequest(path, "DELETE", config_id);
        Integer status = doRequest(req);

        return status;
    }

    /**
     * Retrieves collection analysis results from the server by the given configuration.
     * @param config_id Optional configuration ID. If not provided, primary configuration will be used.
     * @return The list of collection analysis results retrieved from the server for recently queued collections.
     */
    public List<CollAnalyticData> getProcessedCollections(String config_id)
    {
        AuthRequest req = makeAuthRequest("collection/processed", "GET", config_id);
        Integer status = doRequest(req);

        if (200 == status) {
            CollsAnalyticData taskList = (CollsAnalyticData)serializer.deserialize(req.getResponse(), CollsAnalyticData.class);
            if (taskList != null) {
                return taskList.getDocuments();
            }
        }

        return Collections.emptyList();
    }

    /**
     * Retrieves collection analysis results by the given Job identifier.
     * @param jobId Unique Job identifier used while collections queuing.
     * @return The list of collection analysis results retrieved from the server for the given Job ID.
     */
    public List<CollAnalyticData> getProcessedCollectionsByJobId(final String jobId)
    {
        AuthRequest req = makeAuthRequest("collection/processed", "GET")
                .job_id(jobId);
        Integer status = doRequest(req);

        if (200 == status) {
			CollsAnalyticData taskList = (CollsAnalyticData)serializer.deserialize(req.getResponse(), CollsAnalyticData.class);
			if (taskList != null) {
				return taskList.getDocuments();
			}
		}

        return Collections.emptyList();
    }

    //</editor-fold>

    //<editor-fold desc="Private methods">

	private Integer doRequest(AuthRequest req) {
		return doRequest(req, null);
	}

	private Integer doRequest(AuthRequest req, String message) {
        return doRequest(req, message, false, false);
    }

    private Integer doRequest(AuthRequest req, String message, boolean do_autoresponse, boolean is_collection) {
        handleRequest(req, message);
        Integer status = req.doRequest();
        lastRequestStatus = status;
        if (do_autoresponse) {
            onAutoResponse(status, req, is_collection);
        } else {
            handleResponse(status, req);
        }
        lastRequestErrorMessage = (status < 300) ? null : req.getErrorMessage();
		return status;
	}


	private AuthRequest makeAuthRequest(String path, String method) {
        String url = generateRequestUrl(path);
		return AuthRequest.getInstance(url, method)
			.key(key)
			.secret(secret)
			.apiVersion(apiVersion)
			.useCompression(useCompression);
	}

	private AuthRequest makeAuthRequest(String path, String method, String config_id) {
		return makeAuthRequest(path, method)
			.config_id(config_id);
	}

    private <T> AuthRequest update(List<?> items, String action, String config_id, Class<?> type) {
        return update(items, action, config_id, type, "POST");
    }

    private <T> AuthRequest update(List<?> items, String action, String config_id, Class<?> type, String requestMethod) {
        return update(requestMethod, items, action, config_id, type);
    }

    private <T> AuthRequest add(List<?> items, String action, String config_id, Class<?> type) {
        return update("POST", items, action, config_id, type);
    }

    private <T> Integer remove(List<?> items, String action, String config_id, Class<?> type) {
        AuthRequest req = update("DELETE", items, action, config_id, type);
        return req.getStatus();
    }

    private <T> AuthRequest update(String method, List<?> items, String action, String config_id, Class<?> type) {
        String body = null;
        if (serializer instanceof JsonSerializer) {
            body = serializer.serialize(ObjProxy.wrap(items, type, method, "Json"));
        } else {
            body = serializer.serialize(ObjProxy.wrap(items, type, method));
        }

        AuthRequest req = makeAuthRequest(action, method)
			.body(body)
			.config_id(config_id);

        handleRequest(req, body);
        Integer status = req.doRequest();
        handleResponse(status, req);

        return req;
    }

    private void onAutoResponse(Integer status, AuthRequest req, Boolean isCollection) {
        String message = req.getResponse();
        if (callback == null) {
            return;
        }
        if (status <= 202) {
            callback.onResponse(this, new ResponseArgs(status, message));
        } else {
            String error_message = req.getErrorMessage();
            if (StringUtils.isEmpty(error_message)) {
                error_message = message;
            }
            callback.onError(this, new ResponseArgs(status, error_message));
        }
        if (!message.isEmpty() && status < 202) {
            if (isCollection) {
                CollsAnalyticData taskList = (CollsAnalyticData) serializer.deserialize(message, CollsAnalyticData.class);
                callback.onCollsAutoResponse(this, taskList.getDocuments());
            } else {
                DocsAnalyticData taskList = (DocsAnalyticData) serializer.deserialize(message, DocsAnalyticData.class);
                callback.onDocsAutoResponse(this, taskList.getDocuments());
            }
        }

    }

    private String generateRequestUrl(String path) {
        return serviceUrl + path + "." + requestFormat;
    }

    private String generateRequestPathWithId(String path, String id) {
        try {
            return path + "/" + URLEncoder.encode(id, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.format("Error generating URL for %s: %s. Attempting to continue.", path, e.toString());
            return path + "/" + id;
        }
    }

    private void handleResponse(Integer status, AuthRequest req) {
        if (callback != null) {
            if (status <= 202) {
                callback.onResponse(this, new ResponseArgs(status, req.getResponse()));
            }
            if (status > 202) {
                callback.onError(this, new ResponseArgs(status, req.getErrorMessage()));
            }
        }
    }

    private void handleRequest(AuthRequest req) {
        handleRequest(req, null);
    }

    private void handleRequest(AuthRequest req, String message) {
        if (callback != null) {
            callback.onRequest(this, new RequestArgs(
				 req.getMethod(), req.getRequestUrl(), message));
        }
    }

    //</editor-fold>
}
