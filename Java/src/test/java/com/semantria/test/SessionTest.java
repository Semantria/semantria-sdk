package com.semantria.test;

import com.google.common.base.Joiner;
import com.semantria.Session;
import com.semantria.auth.CredentialException;
import com.semantria.mapping.Collection;
import com.semantria.mapping.Document;
import com.semantria.mapping.configuration.BlacklistItem;
import com.semantria.mapping.configuration.Category;
import com.semantria.mapping.configuration.CollectionConfiguration;
import com.semantria.mapping.configuration.Configuration;
import com.semantria.mapping.configuration.DocumentConfiguration;
import com.semantria.mapping.configuration.Query;
import com.semantria.mapping.configuration.SentimentPhrase;
import com.semantria.mapping.configuration.TaxonomyNode;
import com.semantria.mapping.configuration.TaxonomyTopic;
import com.semantria.mapping.configuration.UserEntity;
import com.semantria.mapping.output.CollAnalyticData;
import com.semantria.mapping.output.DocAnalyticData;
import com.semantria.mapping.output.FeaturesSet;
import com.semantria.mapping.output.ServiceStatus;
import com.semantria.mapping.output.Subscription;
import com.semantria.mapping.output.TaskStatus;
import com.semantria.mapping.output.statistics.StatisticsGrouped;
import com.semantria.mapping.output.statistics.StatisticsOverall;
import com.semantria.mapping.output.statistics.StatsInterval;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

// Note that several environment variables must be set for these tests
// to run successfully:
//
//    SEMANTRIA_KEY
//    SEMANTRIA_SECRET
//    SEMANTRIA_USERNAME
//    SEMANTRIA_PASSWORD

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SessionTest {

    private static Logger log = LoggerFactory.getLogger(SessionTest.class);

    // Get default credentials from environment vars
    String key = System.getenv("SEMANTRIA_KEY");
    String secret = System.getenv("SEMANTRIA_SECRET");

    // NOTE: these are only used for testing username/password authentication
    private String username = System.getenv("SEMANTRIA_USERNAME");
    private String password = System.getenv("SEMANTRIA_PASSWORD");

    private Session session = null;
    private CallbackHandler callbackHandler = null;

    private final String SEMANTRIA_API_VERSION = "4.2";
    private final String TEST_CONFIG_NAME = "TEST_CONFIG";
    private final String CLONED_TEST_CONFIG_NAME = "CLONED_TEST_CONFIG";

    private void createTestSession() {
        assertNotNull("Missing key", key);
        assertNotNull("Missing secret", secret);
        callbackHandler = new CallbackHandler();
        session = new Session().withKey(key).withSecret(secret)
                .withApiVersion(SEMANTRIA_API_VERSION)
                .withCallbackHandler(callbackHandler);
    }

    private void createUserTestSession() {
        assertNotNull("Missing username", username);
        assertNotNull("Missing password", password);
        callbackHandler = new CallbackHandler();
        session = new Session().withUsername(username).withPassword(password)
                .withApiVersion(SEMANTRIA_API_VERSION)
                .withCallbackHandler(callbackHandler);
    }

    private Configuration createConfig(String name) {
        Configuration conf = new Configuration();
        conf.setIsPrimary(false);
        conf.setName(name);
        conf.setLanguage("English");
        conf.setDocument(new DocumentConfiguration(5, false, "", false, false, false, false, false, false, false, false, false, false, false, false));
        conf.setCollection(new CollectionConfiguration(false, false, false, false, false, false, false, false));
        List<Configuration> new_configs = session.addConfigurations(Arrays.asList(conf));
        assertEquals(1, new_configs.size());
        assertEquals(name, new_configs.get(0).getName());
        return new_configs.get(0);
    }

    private Configuration getOrCreateConfig(String name) {
        Configuration config = getConfigByName(name);
        return (config != null) ? config : createConfig(name);
    }

    private Configuration getConfigByName(String name) {
        for (Configuration config : session.getConfigurations()) {
            if (config.getName().equals(name)) {
                return config;
            }
        }
        return null;
    }

    private Configuration getConfigById(String id) {
        for (Configuration config : session.getConfigurations()) {
            if (config.getId().equals(id)) {
                return config;
            }
        }
        return null;
    }

    @Test
    public void test01CreateSession() {
        createTestSession();
        assertEquals(Session.class, session.getClass());
    }

    @Test
    public void test02GetStatus() {
        createTestSession();
        ServiceStatus status = session.getStatus();
        assertEquals("available", status.getServiceStatus());
    }

    @Test
    public void test03GetSupportedFeatures() {
        createTestSession();
        List<FeaturesSet> features = session.getSupportedFeatures("English");
        assertEquals("English", features.get(0).getLanguage());
    }

    @Test
    public void test04GetSubscription() {
        createTestSession();
        Subscription subscription = session.getSubscription();
        assertEquals("active", subscription.getStatus());
    }

    @Test
    public void test05StatisticsOverall() {
        createTestSession();
        StatisticsOverall statistics = session.getStatistics(StatsInterval.day);
        assertNotNull(statistics.getLatestUsedApp());
    }

    @Test
    public void test06StatisticsOverallWithDates() {
        createTestSession();
        StatisticsOverall statistics = session.getStatistics(null, new Date());
        assertNotNull(statistics.getLatestUsedApp());
    }

    @Test
    public void test07StatisticsGrouped() {
        createTestSession();
        List<StatisticsGrouped> statistics = session.getStatistics(StatsInterval.day, "config");
        assertTrue(statistics.size() > 0);
        assertNotNull(statistics.get(0).getConfig_id());
        assertTrue(statistics.get(0).getValues().size() > 0);
    }

    @Test
    public void test08StatisticsGroupedWithDates() {
        createTestSession();
        List<StatisticsGrouped> statistics = session.getStatistics(null, new Date(), "app");
        assertTrue(statistics.size() > 0);
        assertNotNull(statistics.get(0).getApp_group());
        assertTrue(statistics.get(0).getValues().size() > 0);
    }

    @Test
    public void test09CreateUpdateCloneConfiguration() {
        createTestSession();

        Configuration new_config = createConfig(TEST_CONFIG_NAME);
        String new_config_id = new_config.getId();
        assertEquals("English", new_config.getLanguage());
        assertEquals(new Integer(5), new_config.getDocument().getSummarySize());

        new_config.getDocument().setSummarySize(20);
        List<Configuration> updated_configs = session.updateConfigurations(Arrays.asList(new_config));

        assertEquals(1, updated_configs.size());
        assertEquals(TEST_CONFIG_NAME, updated_configs.get(0).getName());
        assertEquals(new_config_id, updated_configs.get(0).getId());
        assertEquals(new Integer(20), updated_configs.get(0).getDocument().getSummarySize());

        Configuration updated_config = getConfigById(new_config_id);
        assertNotNull(updated_config);
        assertEquals(new Integer(20), updated_config.getDocument().getSummarySize());

        Configuration cloned_config = session.cloneConfiguration(CLONED_TEST_CONFIG_NAME, updated_config.getId());
        assertEquals(CLONED_TEST_CONFIG_NAME, cloned_config.getName());
        assertEquals(new Integer(20), cloned_config.getDocument().getSummarySize());

        Configuration found_config = getConfigById(cloned_config.getId());
        assertNotNull(found_config);
        assertEquals(CLONED_TEST_CONFIG_NAME, found_config.getName());
        assertEquals(new Integer(20), found_config.getDocument().getSummarySize());
    }

    @Test
    public void test10CRUDCategory() {
        createTestSession();
        Configuration config = getOrCreateConfig(TEST_CONFIG_NAME);
        String configId = config.getId();

        Category category = new Category();
        category.setName("TEST_CATEGORY_NAME");
        List<String> samples = new ArrayList<String>();
        samples.add("TEST_CATEGORY_SAMPLE");
        category.setSamples(samples);

        List<Category> categories1 = session.addCategories(Arrays.asList(category), configId);

        category = null;
        for (Category cat : categories1) {
            if (cat.getName().equals("TEST_CATEGORY_NAME") && cat.getId() != null) {
                category = cat;
            }
        }
        assertNotNull(category);

        category = null;
        for (Category cat : session.getCategories(configId)) {
            if (cat.getName().equals("TEST_CATEGORY_NAME")) {
                category = cat;
            }
        }
        assertNotNull(category);

        category.setWeight(1.0F);
        List<Category> categories2 = session.updateCategories(Arrays.asList(category), configId);

        category = null;
        for (Category cat : categories2) {
            if (cat.getName().equals("TEST_CATEGORY_NAME") && cat.getId() != null) {
                category = cat;
            }
        }
        assertEquals(new Float(1.0), category.getWeight());

        category = null;
        for (Category cat : session.getCategories(configId)) {
            if (cat.getName().equals("TEST_CATEGORY_NAME")) {
                category = cat;
            }
        }
        assertEquals(new Float(1.0), category.getWeight());

        session.removeCategories(Arrays.asList(category), configId);
        category = null;
        for (Category cat : session.getCategories(configId)) {
            if (cat.getName().equals("TEST_CATEGORY_NAME")) {
                category = cat;
            }
        }
        assertNull(category);
    }

    @Test
    public void test11CRUDQuery() {
        createTestSession();
        Configuration config = getOrCreateConfig(TEST_CONFIG_NAME);
        String configId = config.getId();

        Query query = new Query();
        query.setName("TEST_QUERY_NAME");
        query.setQuery("TEST AND QUERY");

        List<Query> queries1 = session.addQueries(Arrays.asList(query), configId);

        query = null;
        for (Query qry : queries1) {
            if (qry.getName().equals("TEST_QUERY_NAME") && qry.getId() != null) {
                query = qry;
            }
        }
        assertNotNull(query);

        query = null;
        for (Query qry : session.getQueries(configId)) {
            if (qry.getName().equals("TEST_QUERY_NAME")) {
                query = qry;
            }
        }
        assertNotNull(query);

        query.setQuery("TEST AND QUERY AND UPDATE");
        List<Query> queries2 = session.updateQueries(Arrays.asList(query), configId);

        query = null;
        for (Query qry : queries2) {
            if (qry.getName().equals("TEST_QUERY_NAME") && qry.getId() != null) {
                query = qry;
            }
        }
        assertNotNull(query);
        assertEquals("TEST AND QUERY AND UPDATE", query.getQuery());

        query = null;
        for (Query qry : session.getQueries(configId)) {
            if (qry.getName().equals("TEST_QUERY_NAME")) {
                query = qry;
            }
        }
        assertNotNull(query);
        assertEquals("TEST AND QUERY AND UPDATE", query.getQuery());

        session.removeQueries(Arrays.asList(query), configId);

        query = null;
        for (Query qry : session.getQueries(configId)) {
            if (qry.getName().equals("TEST_QUERY_NAME")) {
                query = qry;
            }
        }
        assertNull(query);
    }

    @Test
    public void test12CRUDSentimentPhrase() {
        String configId = null;
        createTestSession();
        SentimentPhrase sentimentPhrase = new SentimentPhrase();
        sentimentPhrase.setName("TEST_NAME");
        sentimentPhrase.setWeight(0.1f);

        List<SentimentPhrase> phrases1 = session.addSentimentPhrases(Arrays.asList(sentimentPhrase), configId);
        for (SentimentPhrase phrase : phrases1) {
            if (phrase.getName().equals("TEST_NAME") && phrase.getId() != null) {
                sentimentPhrase = phrase;
            }
        }
        assertNotNull(sentimentPhrase);

        sentimentPhrase = null;
        List<SentimentPhrase> phrases = session.getSentimentPhrases(configId);
        assertNotNull(phrases);
        for (SentimentPhrase phrase : phrases) {
            if (phrase.getName().equals("TEST_NAME")) {
                sentimentPhrase = phrase;
            }
        }
        assertNotNull(sentimentPhrase);

        session.removeSentimentPhrases(Arrays.asList(sentimentPhrase), configId);

        sentimentPhrase = null;
        for (SentimentPhrase phrase : session.getSentimentPhrases(configId)) {
            if (phrase.getName().equals("TEST_NAME")) {
                sentimentPhrase = phrase;
            }
        }
        assertNull(sentimentPhrase);
    }

    @Test
    public void test13CRUDBlacklist() {
        createTestSession();
        Configuration config = getOrCreateConfig(TEST_CONFIG_NAME);
        String configId = config.getId();

        BlacklistItem blacklistItem = new BlacklistItem();
        blacklistItem.setName("TEST_BLACKLISTED");
        List<BlacklistItem> blacklistItems1 = session.addBlacklist(Arrays.asList(blacklistItem), configId);

        BlacklistItem item = null;
        for (BlacklistItem bl : blacklistItems1) {
            if (bl.getName().equals("TEST_BLACKLISTED") && bl.getId() != null) {
                item = bl;
            }
        }
        assertNotNull(item);

        item = null;
        for (BlacklistItem bl : session.getBlacklist(configId)) {
            if (bl.getName().equals("TEST_BLACKLISTED") && bl.getId() != null) {
                item = bl;
            }
        }
        assertNotNull(item);

        session.removeBlacklist(Arrays.asList(item), configId);

        item = null;
        for (BlacklistItem bl : session.getBlacklist(configId)) {
            if (bl.getName().equals("TEST_BLACKLISTED")) {
                item = bl;
            }
        }
        assertNull(item);
    }

    @Test
    public void test14CRUDEntities() {
        createTestSession();
        Configuration config = getOrCreateConfig(TEST_CONFIG_NAME);
        String configId = config.getId();

        UserEntity ue = new UserEntity();
        ue.setName("TEST_USER_ENTITY");
        ue.setType("TEST_USER_ENTITY_TYPE");

        List<UserEntity> entities1 = session.addEntities(Arrays.asList(ue), configId);

        UserEntity item = null;
        for (UserEntity en : entities1) {
            if (en.getName().equals("TEST_USER_ENTITY") && en.getId() != null) {
                item = en;
            }
        }
        assertNotNull(item);

        item = null;
        for (UserEntity en : session.getEntities(configId)) {
            if (en.getName().equals("TEST_USER_ENTITY")) {
                item = en;
            }
        }
        assertNotNull(item);

        item.setType("TEST_USER_ENTITY_TYPE_UPDATED");
        List<UserEntity> entities2 = session.updateEntities(Arrays.asList(item), configId);

        ue = null;
        for (UserEntity el : entities2) {
            if (el.getName().equals("TEST_USER_ENTITY") && el.getId() != null) {
                ue = el;
            }
        }
        assertNotNull(ue);
        assertEquals("TEST_USER_ENTITY_TYPE_UPDATED", ue.getType());

        ue = null;
        for (UserEntity el : session.getEntities(configId)) {
            if (el.getName().equals("TEST_USER_ENTITY")) {
                ue = el;
            }
        }
        assertNotNull(ue);
        assertEquals("TEST_USER_ENTITY_TYPE_UPDATED", ue.getType());

        session.removeEntities(Arrays.asList(ue), configId);

        item = null;
        for (UserEntity el : session.getEntities(configId)) {
            if (el.getName().equals("TEST_USER_ENTITY")) {
                item = el;
            }
        }
        assertNull(item);
    }

    @Test
    public void test15CRUDTaxonomy() {
        createTestSession();
        Configuration config = getOrCreateConfig(TEST_CONFIG_NAME);
        String configId = config.getId();

        TaxonomyNode taxonomyNode = new TaxonomyNode();
        taxonomyNode.setName("TEST_TAXONOMY_NAME");

        List<TaxonomyNode> taxonomies1 = session.addTaxonomy(Arrays.asList(taxonomyNode), configId);

        taxonomyNode = null;
        for (TaxonomyNode tax : taxonomies1) {
            if (tax.getName().equals("TEST_TAXONOMY_NAME") && tax.getId() != null) {
                taxonomyNode = tax;
            }
        }
        assertNotNull(taxonomyNode);

        taxonomyNode = null;
        for (TaxonomyNode tax : session.getTaxonomy(configId)) {
            if (tax.getName().equals("TEST_TAXONOMY_NAME")) {
                taxonomyNode = tax;
            }
        }
        assertNotNull(taxonomyNode);

        TaxonomyNode childTaxonomyNode = new TaxonomyNode();
        childTaxonomyNode.setName("CHILD TAXONOMY");
        childTaxonomyNode.setEnforceParentMatching(false);

        TaxonomyTopic topic = new TaxonomyTopic();
        topic.setType("query");

        taxonomyNode.setName("UPDATED_TEST_TAXONOMY_NAME");
        taxonomyNode.setNodes(Arrays.asList(childTaxonomyNode));
        List<TaxonomyNode> taxonomies2 = session.updateTaxonomy(Arrays.asList(taxonomyNode), configId);

        taxonomyNode = null;
        for (TaxonomyNode tax : taxonomies2) {
            if (tax.getName().equals("UPDATED_TEST_TAXONOMY_NAME") && tax.getId() != null) {
                taxonomyNode = tax;
            }
        }
        assertNotNull(taxonomyNode);
        assertEquals(1, taxonomyNode.getNodes().size());
        assertFalse(taxonomyNode.getNodes().get(0).getEnforceParentMatching());

        taxonomyNode = null;
        for (TaxonomyNode tax : session.getTaxonomy(configId)) {
            if (tax.getName().equals("UPDATED_TEST_TAXONOMY_NAME")) {
                taxonomyNode = tax;
            }
        }
        assertNotNull(taxonomyNode);

        session.removeTaxonomy(Arrays.asList(taxonomyNode), configId);
        taxonomyNode = null;
        for (TaxonomyNode tax : session.getTaxonomy(configId)) {
            if (tax.getName().equals("UPDATED_TEST_TAXONOMY_NAME")) {
                taxonomyNode = tax;
            }
        }
        assertNull(taxonomyNode);
    }

    @Test
    public void test16AnalyzeSingleDocument() {
        createTestSession();
        Configuration config = getOrCreateConfig(TEST_CONFIG_NAME);
        String configId = config.getId();

        session.queueDocument(new Document("TEST_ID_1", "Amazon Web Services has announced a new feature called VM?Ware Import, which allows IT departments to move virtual machine images from their internal data centers to the cloud. It will cost 30?", "tag"), configId);

        DocAnalyticData task = null;
        for (int i = 0; i < 5; i++) {
            task = session.getDocument("TEST_ID_1", configId);
            if ((task != null) && task.getStatus().equals(TaskStatus.PROCESSED)) {
                break;
            }
            sleep(5);
        }
        if ((task == null) || (! task.getStatus().equals(TaskStatus.PROCESSED))) {
            log.info("task = {}", task);
            fail("gave up waiting for processed task");
        }
        // Get result from outgoing API queue to clear the queue for remaining tests.
        session.getProcessedDocuments(configId);

        assertEquals(TaskStatus.PROCESSED, task.getStatus());
    }

    @Test
    public void test17AnalyzeBatchOfDocuments() {
        createTestSession();
        Configuration config = getOrCreateConfig(TEST_CONFIG_NAME);
        String configId = config.getId();

        List<Document> tasks = new ArrayList<Document>();
        tasks.add(new Document("BATCH_1", "DUMMY_TEXT"));
        tasks.add(new Document("BATCH_2", "DUMMY_TEXT"));

        session.QueueBatchOfDocuments(tasks, configId);

        List<DocAnalyticData> data = null;
        for (int i = 0; i < 5; i++) {
            data = session.getProcessedDocuments(configId);
            if (data != null && !data.isEmpty()) {
                break;
            }
            sleep(5);
        }
        if ((data == null) || data.isEmpty()) {
            fail("gave up waiting for processed docs");
        }

        DocAnalyticData doc = null;
        for (DocAnalyticData docAnalyticData : data) {
            log.info("docAnalyticData = {}", docAnalyticData);
            if (docAnalyticData.getId().equals("BATCH_1") || docAnalyticData.getId().equals("BATCH_2")) {
                doc = docAnalyticData;
            }
        }

        assertNotNull("expected a doc from the batch", doc);
    }

    @Test
    public void test18AnalyzeCollection() {
        createTestSession();
        Configuration config = getOrCreateConfig(TEST_CONFIG_NAME);
        String configId = config.getId();

        Collection coll = new Collection();
        coll.setId("TEST_COLLECTION_ID");
        List<String> documents = new ArrayList<String>();
        documents.add("test test for processing - 1");
        documents.add("test test for processing - 2");
        documents.add("test test for processing - 3");
        coll.setDocuments(documents);

        session.queueCollection(coll, configId);

        List<CollAnalyticData> data = null;
        for (int i = 0; i < 5; i++) {
            log.info("data = {}", data);
            data = session.getProcessedCollections(configId);
            if ((data != null) && !data.isEmpty()) {
                break;
            }
            sleep(5);
        }

        if ((data == null) || data.isEmpty()) {
            fail("gave up waiting for processed collection");
        }

        CollAnalyticData col = null;
        for (CollAnalyticData colAnalyticData : data) {
            if (colAnalyticData.getId().equals("TEST_COLLECTION_ID")) {
                col = colAnalyticData;
            }
        }

        assertNotNull("expected a collection result", col);
    }

    @Test
    public void test19Cleanup() {
        createTestSession();

        for (Configuration config : session.getConfigurations()) {
            if (config.getName().equals(TEST_CONFIG_NAME) || config.getName().equals(CLONED_TEST_CONFIG_NAME)) {
                session.removeConfigurations(Arrays.asList(config));
            }
        }

        for (Configuration conf : session.getConfigurations()) {
            if (conf.getName().equals(TEST_CONFIG_NAME) || conf.getName().equals(CLONED_TEST_CONFIG_NAME)) {
                fail("found a test config -- these should all have been deleted.");
            }
        }
    }

    @Test
    public void test20AuthWithUsernamePassword() {
        createUserTestSession();
        ServiceStatus status = session.getStatus();
        assertEquals("available", status.getServiceStatus());
        if (! callbackHandler.getErrors().isEmpty()) {
            fail("Can't authenticate with username/password: "
                    + Joiner.on("\n  ").join(callbackHandler.getErrors()));
        }
    }

    @Test
    public void test21ReauthenticateUsernameSession() {
        createUserTestSession();
        ServiceStatus status = session.getStatus();
        assertEquals("available", status.getServiceStatus());
        // Small hack to simulate session expiration. Change key (which for username/password
        // is the user session key). This will cause the first attempt to get status to fail
        // and thus force an auth session refresh.
        session.withKey("bad-key");
        status = session.getStatus();
        assertEquals("available", status.getServiceStatus());
    }

    @Test
    public void test22AuthServiceAuthenticate() {
        try {
            createUserTestSession();
            session.authenticate();
        } catch (CredentialException e) {
            fail("Can't authenticate with username/password: " + e.toString());
        }
    }


    private void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            // Ignore -- expected exception
        }
    }

}
