package com.semantria.test;

import com.semantria.utils.ObjProxy;
import com.semantria.interfaces.ISerializer;
import com.semantria.mapping.Batch;
import com.semantria.mapping.Document;
import com.semantria.mapping.configuration.*;
import com.semantria.mapping.configuration.stub.*;
import com.semantria.mapping.output.*;
import com.semantria.mapping.output.statistics.Details;
import com.semantria.mapping.output.stub.CollsAnalyticData;
import com.semantria.mapping.output.stub.DocsAnalyticData;
import com.semantria.serializer.JsonSerializer;
import com.semantria.serializer.XmlSerializer;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SerializerTest {

	@Test
	public void testSerializingConfiguration() {
		ISerializer serializer = new XmlSerializer();
		ISerializer JsonSerializer = new JsonSerializer();

		Configuration config = new Configuration();
		config.setName("New configuration");
		config.setIsPrimary(false);
		config.setOneSentence(true);
		config.setAutoResponse(true);
		config.setLanguage("English");
		config.setCharsThreshold(80);
		config.setCallback("https://anyapi.anydomain.com/processed/docs.json");

		DocConfiguration doc = new DocConfiguration();
		doc.setConceptTopicsLimit(5);
		doc.setQueryTopicsLimit(5);
		doc.setNamedEntitiesLimit(5);
		doc.setUserEntitiesLimit(5);
		doc.setThemesLimit(0);
		doc.setEntityThemesLimit(5);
		doc.setPhrasesLimit(0);
		doc.setSummaryLimit(0);
		doc.setPosTypes("Noun");
		doc.setNamedRelationsLimit(0);
		doc.setUserRelationsLimit(0);
		doc.setDetectLanguage(true);
		doc.setPossiblePhrasesLimit(0);
		config.setDocument(doc);

		CollConfiguration coll = new CollConfiguration();
		coll.setFacetsLimit(15);
		coll.setFacetAttributesLimit(20);
		coll.setConceptTopicsLimit(5);
		coll.setQueryTopicsLimit(5);
		coll.setNamedEntitiesLimit(5);
		coll.setThemesLimit(0);
		coll.setFacetMentionsLimit(0);
		config.setCollection(coll);

		Configuration clonedConfig = new Configuration();
		clonedConfig.setTemplate("45699836");
		clonedConfig.setName("Cloned config");

		Configuration updateConfig = new Configuration();
		updateConfig.setId("45699836");
		updateConfig.setName("Updated config");

		Assert.assertEquals(ExpectedResult.XML.addConfig, serializer.serialize(ObjProxy.wrap(Arrays.asList(config), Configurations.class, "POST")));
		assertEquals(ExpectedResult.XML.cloneConfig, serializer.serialize( ObjProxy.wrap(Arrays.asList( clonedConfig ), Configurations.class, "POST" ) ));
		assertEquals(ExpectedResult.XML.updateConfig, serializer.serialize( ObjProxy.wrap(Arrays.asList( updateConfig ), Configurations.class, "POST" ) ));
		assertEquals(ExpectedResult.XML.removeConfig, serializer.serialize( ObjProxy.wrap(Arrays.asList( updateConfig ), Configurations.class, "DELETE" ) ));

		assertEquals(ExpectedResult.JSON.addConfig, JsonSerializer.serialize( Arrays.asList(config) ));
		assertEquals(ExpectedResult.JSON.cloneConfig, JsonSerializer.serialize( Arrays.asList( clonedConfig ) ));
		assertEquals(ExpectedResult.JSON.updateConfig, JsonSerializer.serialize( Arrays.asList( updateConfig ) ));
		assertEquals(ExpectedResult.JSON.removeConfig, JsonSerializer.serialize( ObjProxy.wrap( Arrays.asList( updateConfig ), Configurations.class, "DELETE", "Json") ));
	}

	@Test
	public void testXmlDeserializingConfiguration()
	{
		ISerializer serializer = new XmlSerializer();
		Configuration config = ((Configurations)serializer.deserialize(ExpectedResult.XML.addConfig, Configurations.class)).getConfigurations().get(0);
		testConfigDeserialization(config);
	}

	@Test
	public void testJsonDeserializingConfiguration()
	{
		ISerializer serializer = new JsonSerializer();
		Configuration config =  ((Configurations)(serializer.deserialize(ExpectedResult.JSON.addConfig, Configurations.class))).getConfigurations().get(0);
		testConfigDeserialization(config);
	}

	@Test
	public void testXmlSerializingBlacklist()
	{
		ISerializer serializer = new XmlSerializer();
		ISerializer jsonSerializer = new JsonSerializer();

		assertEquals(ExpectedResult.XML.blackList, serializer.serialize(ObjProxy.wrap(Arrays.asList("Added Filter 1"), Blacklists.class, "POST")));
		assertEquals(ExpectedResult.JSON.blackList,jsonSerializer.serialize(Arrays.asList("Added Filter 1")));
	}


	@Test
	public void testXmlDeserializingBlacklist() {
		ISerializer serializer = new XmlSerializer();

		List<String> blacklists = ((Blacklists)serializer.deserialize(ExpectedResult.XML.blackList, Blacklists.class)).getBlacklist();
		assertEquals("Added Filter 1", blacklists.get(0));
	}

	@Test
	public void testJsonDeserializingBlacklist()
	{
		ISerializer serializer = new JsonSerializer();
		List<String> blacklists = ((Blacklists)serializer.deserialize(ExpectedResult.JSON.blackList, Blacklists.class)).getBlacklist();
		assertEquals("Added Filter 1", blacklists.get(0));
	}


	@Test
	public void testSerializingQueries()
	{
		ISerializer serializer = new XmlSerializer();
		ISerializer jsonSerializer = new JsonSerializer();

		assertEquals(ExpectedResult.XML.addQuery, serializer.serialize( ObjProxy.wrap(Arrays.asList(new Query("Query 2", "Something AND something")), Queries.class, "POST")));
		assertEquals(ExpectedResult.XML.removeQuery, serializer.serialize( ObjProxy.wrap(Arrays.asList(new Query("Query 2", "Something AND something")), Queries.class, "DELETE")));

		assertEquals(ExpectedResult.JSON.addQuery, jsonSerializer.serialize( Arrays.asList(  new Query("Feature: Cloud service", "Amazon AND EC2 AND Cloud") )));
		assertEquals(ExpectedResult.JSON.removeQuery, jsonSerializer.serialize( ObjProxy.wrap(Arrays.asList(new Query("Feature: Cloud service", "Something AND something")), Queries.class, "DELETE", "Json")));
	}


	@Test
	public void testXmlDeserializingQueries() {
		ISerializer serializer = new XmlSerializer();

		List<Query> queries = ((Queries)(serializer.deserialize(ExpectedResult.XML.addQuery, Queries.class))).getQueries();
		assertEquals(1, queries.size());
		assertEquals("Query 2", queries.get(0).getName());
	}

	@Test
	public void testJsonDeserializingQueries() {
		ISerializer serializer = new JsonSerializer();

		List<Query> queries = ((Queries)serializer.deserialize(ExpectedResult.JSON.addQuery, Queries.class)).getQueries();
		assertEquals(1, queries.size());
		assertEquals("Feature: Cloud service", queries.get(0).getName());
		assertEquals("Amazon AND EC2 AND Cloud", queries.get(0).getQuery());
	}

	@Test
	public void testSerializingEntities()
	{
		ISerializer serializer = new XmlSerializer();
		ISerializer jsonSerializer = new JsonSerializer();
		assertEquals(ExpectedResult.XML.addEntity, serializer.serialize( ObjProxy.wrap( Arrays.asList( new UserEntity("name 1", "type 1") ), UserEntities.class, "POST")));
		assertEquals(ExpectedResult.JSON.addEntity, jsonSerializer.serialize( Arrays.asList(new UserEntity("name 1", "type 1")) ));
	}


	public void testXmlDeserializingEntities()
	{
		ISerializer serializer = new XmlSerializer();

		List<UserEntity> entities = ((UserEntities)serializer.deserialize(ExpectedResult.JSON.addEntity, UserEntities.class)).getEntities();
		assertEquals(1, entities.size());
		assertEquals("name 1", entities.get(0).getName());
		assertEquals("type 1", entities.get(0).getType());
	}

	@Test
	public void testJsonDeserializingEntities()
	{
		ISerializer serializer = new JsonSerializer();

		List<UserEntity> entities = ((UserEntities)serializer.deserialize(ExpectedResult.JSON.addEntity, UserEntities.class)).getEntities();
		assertEquals(1, entities.size());
		assertEquals("name 1", entities.get(0).getName());
		assertEquals("type 1", entities.get(0).getType());
	}

	@Test
	public void testSerializingCategories()
	{
		ISerializer serializer = new XmlSerializer();
		ISerializer jsonSerializer = new JsonSerializer();

		Category addedCategory = new Category();
				 addedCategory.setName("Added Category 1");
				 addedCategory.setWeight(0.2f);
		List<String> samples = new ArrayList<String>();
					 samples.add("Entity 1");
					 samples.add("Entity 2");
					 samples.add("Entity 3");
		addedCategory.setSamples(samples);

		assertEquals(ExpectedResult.XML.addCategory, serializer.serialize( ObjProxy.wrap(Arrays.asList(addedCategory), Categories.class, "POST")));
		assertEquals(ExpectedResult.XML.removeCategory, serializer.serialize( ObjProxy.wrap(Arrays.asList(addedCategory), Categories.class, "DELETE")));

		assertEquals(ExpectedResult.JSON.addCategory, jsonSerializer.serialize( Arrays.asList(addedCategory)));
		assertEquals(ExpectedResult.JSON.removeCategory, jsonSerializer.serialize( ObjProxy.wrap(Arrays.asList(addedCategory), Categories.class, "DELETE", "Json")));
	}

	@Test
	public void testXmlDeserializingCategories()
	{
		ISerializer serializer = new XmlSerializer();

		List<Category> categories = ((Categories)serializer.deserialize(ExpectedResult.XML.addCategory, Categories.class)).getCategories();
		assertEquals(1, categories.size());
		assertEquals("Added Category 1", categories.get(0).getName());
		assertEquals(new Float(0.2), categories.get(0).getWeight());
		assertEquals(3, categories.get(0).getSamples().size());
	}

	@Test
	public void testJsonDeserializingCategories()
	{
		ISerializer serializer = new JsonSerializer();

		List<Category> categories = ((Categories)serializer.deserialize(ExpectedResult.JSON.addCategory, Categories.class)).getCategories();

		assertEquals(1, categories.size());
		assertEquals("Added Category 1", categories.get(0).getName());
		assertEquals(new Float(0.2), categories.get(0).getWeight());
		assertEquals(3, categories.get(0).getSamples().size());
	}

	@Test
	public void testSerializingSentimentPhrases()
	{
		ISerializer serializer = new XmlSerializer();
		assertEquals(ExpectedResult.XML.addSentimentPhrase, serializer.serialize( ObjProxy.wrap(Arrays.asList(new SentimentPhrase("name 1", 0.3f)), SentimentPhrases.class, "POST") ));
		assertEquals(ExpectedResult.XML.removeSentimentPhrase, serializer.serialize( ObjProxy.wrap(Arrays.asList(new SentimentPhrase("name 1", 0.3f)), SentimentPhrases.class, "DELETE") ));
	}

	@Test
	public void testJsonSerializingSentimentPhrases() {
		ISerializer serializer = new JsonSerializer();

		assertEquals(ExpectedResult.JSON.addSentimentPhrase, serializer.serialize(Arrays.asList( new SentimentPhrase("name 1", 0.3f) )));
		assertEquals(ExpectedResult.JSON.removeSentimentPhrase, serializer.serialize( ObjProxy.wrap( Arrays.asList(new SentimentPhrase("name 1", 0.3f)), SentimentPhrases.class, "DELETE", "Json" )));
	}


	public void testXmlDeserializingSentimentPhrases()
	{
		ISerializer serializer = new XmlSerializer();
		List<SentimentPhrase> sentimentPhrases = ((SentimentPhrases)serializer.deserialize(ExpectedResult.XML.addSentimentPhrase, SentimentPhrases.class)).getSentimentPhrases();
		assertEquals(1, sentimentPhrases.size());
		assertEquals("name 1", sentimentPhrases.get(0).getName());
		assertEquals(new Float(0.3), sentimentPhrases.get(0).getWeight());
	}

	@Test
	public void testJsonDeserializingSentimentPhrases()
	{
		ISerializer serializer = new JsonSerializer();

		List<SentimentPhrase> sentimentPhrases = ((SentimentPhrases)serializer.deserialize(ExpectedResult.JSON.addSentimentPhrase, SentimentPhrases.class)).getSentimentPhrases();
		assertEquals(1, sentimentPhrases.size());
		assertEquals("name 1", sentimentPhrases.get(0).getName());
		assertEquals(new Float(0.3), sentimentPhrases.get(0).getWeight());
	}

	@Test
	public void testXmlDeserializingAnalyticServiceStatus() {
		ISerializer serializer = new XmlSerializer();

		ServiceStatus status = (ServiceStatus)serializer.deserialize(ExpectedResult.XML.serviceStatus, ServiceStatus.class);
		assertNotNull(status);
		assertEquals("online", status.getServiceStatus());
		assertEquals("3.0", status.getApiVersion());
		assertEquals("3.0.0.1251", status.getServiceVersion());
		assertEquals("UTF-8", status.getSupportedEncoding());
		assertEquals("gzip,deflate", status.getSupportedCompression());
		assertEquals(5, status.getSupportedLanguages().size());
		assertTrue("English".equals(status.getSupportedLanguages().get(0))
				|| "French".equals(status.getSupportedLanguages().get(1))
				|| "Spanish".equals(status.getSupportedLanguages().get(1))
				|| "Portuguese".equals(status.getSupportedLanguages().get(1))
				|| "German".equals(status.getSupportedLanguages().get(1))
		);
	}

	@Test
	public void testJsonDeserializingAnalyticServiceStatus() {
		ISerializer serializer = new JsonSerializer();

		ServiceStatus status = (ServiceStatus)serializer.deserialize(ExpectedResult.JSON.serviceStatus, ServiceStatus.class);
		assertNotNull(status);
		assertEquals("online", status.getServiceStatus());
		assertEquals("3.0", status.getApiVersion());
		assertEquals("3.0.0.1251", status.getServiceVersion());
		assertEquals("UTF-8", status.getSupportedEncoding());
		assertEquals("gzip,deflate", status.getSupportedCompression());
		assertEquals(5, status.getSupportedLanguages().size());
		assertTrue("English".equals(status.getSupportedLanguages().get(0))
				|| "French".equals(status.getSupportedLanguages().get(1))
				|| "Spanish".equals(status.getSupportedLanguages().get(1))
				|| "Portuguese".equals(status.getSupportedLanguages().get(1))
				|| "German".equals(status.getSupportedLanguages().get(1))
		);
	}

	@Test
	public void testXmlDeserializingSubscription() {
		ISerializer serializer = new XmlSerializer();

		Subscription subscriptionObj = (Subscription)serializer.deserialize(ExpectedResult.XML.subscription, Subscription.class);
		testSubscription( subscriptionObj );
	}

	@Test
	public void testJsonDeserializingSubscription()
	{
		ISerializer serializer = new JsonSerializer();

		Subscription subscription = (Subscription)serializer.deserialize(ExpectedResult.JSON.subscription, Subscription.class);
		testSubscription(subscription);
	}

	@Test
	public void testXmlDeserializingDocumentAnalyticData() {
		ISerializer serializer = new XmlSerializer();

		DocAnalyticData analyticData = (DocAnalyticData)serializer.deserialize(ExpectedResult.XML.docAnalyticData, DocAnalyticData.class);
		checkDocAnalyticData( analyticData );
	}

	@Test
	public void testJsonDeserializingDocumentAnalyticData() {
		ISerializer serializer = new JsonSerializer();
		DocAnalyticData analyticDataList = (DocAnalyticData)serializer.deserialize(ExpectedResult.JSON.docAnalyticData, DocAnalyticData.class);
		checkDocAnalyticData( analyticDataList );
	}

	@Test
	public void testXmlDeserializingCollectionAnalyticData()
	{
		ISerializer serializer = new XmlSerializer();

		CollAnalyticData analyticData = (CollAnalyticData)serializer.deserialize(ExpectedResult.XML.collAnalyticData, CollAnalyticData.class);
		checkCollAnalyticData(analyticData);
	}

	@Test
	public void testJsonDeserializingCollectionAnalyticData()
	{
		ISerializer serializer = new JsonSerializer();

		CollAnalyticData analyticData = (CollAnalyticData)serializer.deserialize(ExpectedResult.JSON.collAnalyticData, CollAnalyticData.class);
		checkCollAnalyticData(analyticData);
	}

	@Test
	public void testXmlDeserializingCollectionsAnalyticData()
	{
		ISerializer serializer = new XmlSerializer();

		CollsAnalyticData analyticsData = (CollsAnalyticData)serializer.deserialize(ExpectedResult.XML.collsAnalyticData, CollsAnalyticData.class);
		assertTrue( null != analyticsData );
		assertTrue( null != analyticsData.getDocuments() );
		assertTrue( null != analyticsData.getDocuments().get(0) );
		checkCollAnalyticData(analyticsData.getDocuments().get(0));
	}

	@Test
	public void testJsonDeserializingCollectionsAnalyticData()
	{
		ISerializer serializer = new JsonSerializer();

		List<CollAnalyticData> analyticsData = ((CollsAnalyticData)serializer.deserialize(ExpectedResult.JSON.collsAnalyticData, CollsAnalyticData.class)).getDocuments();
		assertTrue( null != analyticsData );
		assertTrue(null != analyticsData.get(0));
		checkCollAnalyticData(analyticsData.get(0));
	}


	@Test
	public void testXmlDeserializingDocumentsAnalyticData()
	{
		ISerializer serializer = new XmlSerializer();

		DocsAnalyticData analyticsData = (DocsAnalyticData)serializer.deserialize(ExpectedResult.XML.docsAnalyticData, DocsAnalyticData.class);
		assertTrue( null != analyticsData );
		assertTrue( null != analyticsData.getDocuments() );
		assertTrue( null != analyticsData.getDocuments().get(0) );
		checkDocAnalyticData(analyticsData.getDocuments().get(0));
	}


	@Test
	public void testJsonDeserializingDocumentsAnalyticData()
	{
		ISerializer serializer = new JsonSerializer();

		List<DocAnalyticData> analyticsData = ((DocsAnalyticData)serializer.deserialize(ExpectedResult.JSON.docsAnalyticData, DocsAnalyticData.class)).getDocuments();
		assertTrue( null != analyticsData );
		assertTrue( null != analyticsData.get(0) );
		checkDocAnalyticData(analyticsData.get(0));
	}

	@Test
	public void testXmlDeserializingStatistics()
	{
		ISerializer serializer = new XmlSerializer();

		Statistics statistics = (Statistics)serializer.deserialize(ExpectedResult.XML.statistics, Statistics.class);
		checkStatistics(statistics);
	}

	@Test
	public void testJsonDeserializingStatistics()
	{
		ISerializer serializer = new JsonSerializer();

		Statistics statistics = (Statistics)serializer.deserialize(ExpectedResult.JSON.statistics, Statistics.class);
		checkStatistics(statistics);
	}

	@Test
	public void testXmlSerializationBatch()
	{
		ISerializer serializer = new XmlSerializer();
		assertEquals( ExpectedResult.XML.batch, serializer.serialize( ObjProxy.wrap(Arrays.asList( new Document("1232142", "Text for analizing") ) , Batch.class, "POST")));
	}

	@Test
	public void testXmlSerializationDocument()
	{
		ISerializer serializer = new XmlSerializer();
		assertEquals( ExpectedResult.XML.document, serializer.serialize( new Document("1232142", "Text for analizing")));

	}

	private void testConfigDeserialization(Configuration config)
	{
		assertEquals("New configuration", config.getName());
		assertFalse(config.getIsPrimary());
		assertTrue(config.getOneSentence());
		assertTrue(config.getAutoResponse());
		assertEquals("English", config.getLanguage());
		assertEquals(new Integer(80), config.getCharsThreshold());
		assertEquals("https://anyapi.anydomain.com/processed/docs.json", config.getCallback());

		DocConfiguration doc = config.getDocument();
		assertEquals(new Integer(5), doc.getConceptTopicsLimit());
		assertEquals(new Integer(5), doc.getQueryTopicsLimit());
		assertEquals(new Integer(5), doc.getNamedEntitiesLimit());
		assertEquals(new Integer(5), doc.getUserEntitiesLimit());
		assertEquals(new Integer(0), doc.getThemesLimit());
		assertEquals(new Integer(5), doc.getEntityThemesLimit());
		assertEquals(new Integer(0), doc.getPhrasesLimit());
		assertEquals(new Integer(0), doc.getPossiblePhrasesLimit());
		assertEquals(new Integer(0), doc.getNamedRelationsLimit());
		assertEquals(new Integer(0), doc.getUserRelationsLimit());
		assertTrue(doc.getDetectLanguage());
		assertEquals(new Integer(0), doc.getSummaryLimit());
		assertEquals("Noun", doc.getPosTypes());

		CollConfiguration coll = config.getCollection();
		assertEquals(new Integer(5), coll.getConceptTopicsLimit());
		assertEquals(new Integer(5), coll.getQueryTopicsLimit());
		assertEquals(new Integer(5), coll.getNamedEntitiesLimit());
		assertEquals(new Integer(0), coll.getThemesLimit());
		assertEquals(new Integer(15), coll.getFacetsLimit());
		assertEquals(new Integer(20), coll.getFacetAttributesLimit());
	}

	private void testSubscription(Subscription subscriptionObj)
	{
		assertNotNull(subscriptionObj);
		assertEquals("Subscriber", subscriptionObj.getName());
		assertEquals("active", subscriptionObj.getStatus());

		assertEquals("normal", subscriptionObj.getBillingSettings().getPriority());
		assertEquals(1293883200, subscriptionObj.getBillingSettings().getExpirationDate().intValue());
		assertEquals(87, subscriptionObj.getBillingSettings().getCallsBalance().intValue());
		assertEquals(100, subscriptionObj.getBillingSettings().getCallsLimit().intValue());
		assertEquals(60, subscriptionObj.getBillingSettings().getCallsLimitInterval().intValue());
		assertEquals(49897, subscriptionObj.getBillingSettings().getDocsBalance().intValue());
		assertEquals(0, subscriptionObj.getBillingSettings().getDocsLimit().intValue());
		assertEquals(0, subscriptionObj.getBillingSettings().getDocsLimitInterval().intValue());
		assertEquals("pay-as-you-go", subscriptionObj.getBillingSettings().getLimitType());

		assertEquals(10, subscriptionObj.getBasicSettings().getConfigurationsLimit().intValue());
		assertEquals(100, subscriptionObj.getBasicSettings().getBlacklistLimit().intValue());
		assertEquals(100, subscriptionObj.getBasicSettings().getCategoriesLimit().intValue());
		assertEquals(100, subscriptionObj.getBasicSettings().getQueriesLimit().intValue());
		assertEquals(1000, subscriptionObj.getBasicSettings().getEntitiesLimit().intValue());
		assertEquals(1000, subscriptionObj.getBasicSettings().getSentimentLimit().intValue());
		assertEquals(8192, subscriptionObj.getBasicSettings().getCharactersLimit().intValue());
		assertEquals(10, subscriptionObj.getBasicSettings().getBatchLimit().intValue());
		assertEquals(100, subscriptionObj.getBasicSettings().getCollectionLimit().intValue());
		assertEquals(2, subscriptionObj.getBasicSettings().getAutoResponseLimit().intValue());
		assertEquals(100, subscriptionObj.getBasicSettings().getProcessedBatchLimit().intValue());
		assertEquals(100, subscriptionObj.getBasicSettings().getCallbackBatchLimit().intValue());
		assertEquals(10, subscriptionObj.getBasicSettings().getCategorySamplesLimit().intValue());
		assertFalse(subscriptionObj.getBasicSettings().getReturnSourceText().booleanValue());

		assertTrue(subscriptionObj.getFeatureSettings().getDocument().getSummary());
		assertTrue( subscriptionObj.getFeatureSettings().getDocument().getThemes() );
		assertTrue( subscriptionObj.getFeatureSettings().getDocument().getNamedEntities() );
		assertTrue( subscriptionObj.getFeatureSettings().getDocument().getUserEntities() );
		assertTrue( subscriptionObj.getFeatureSettings().getDocument().getQueryTopics() );
		assertFalse(subscriptionObj.getFeatureSettings().getDocument().getConceptTopics());
		assertTrue( subscriptionObj.getFeatureSettings().getDocument().getSentimentPhrases() );
		assertFalse(subscriptionObj.getFeatureSettings().getDocument().getPhrasesDetection());
		assertFalse(subscriptionObj.getFeatureSettings().getDocument().getPosTagging());
		assertFalse(subscriptionObj.getFeatureSettings().getDocument().getLanguageDetection());
		assertFalse(subscriptionObj.getFeatureSettings().getDocument().getEntityThemes());
		assertFalse(subscriptionObj.getFeatureSettings().getDocument().getUserRelations());
		assertFalse(subscriptionObj.getFeatureSettings().getDocument().getNamedRelations());

		assertTrue( subscriptionObj.getFeatureSettings().getCollection().getFacets());
		assertTrue( subscriptionObj.getFeatureSettings().getCollection().getThemes());
		assertTrue( subscriptionObj.getFeatureSettings().getCollection().getNamedEntities());
		assertTrue( subscriptionObj.getFeatureSettings().getCollection().getQueryTopics());
		assertFalse(subscriptionObj.getFeatureSettings().getCollection().getConceptTopics());
		assertEquals("English, French, Spanish", subscriptionObj.getFeatureSettings().getSupportedLanguages());
	}

	private void checkDocAnalyticData(DocAnalyticData analyticData)
	{
		assertTrue(null != analyticData);
		assertEquals("cd2e7341-a3c2-4fb4-9d3a-779e8b0a5eff", analyticData.getConfigId());
		assertEquals("6F9619FF8B86D011B42D00CF4FC964FF", analyticData.getId());
		assertEquals(TaskStatus.PROCESSED, analyticData.getStatus());
		assertEquals(new Float(0.2398756), analyticData.getSentimentScore());
		assertEquals("positive", analyticData.getSentimentPolarity());
		assertEquals("English", analyticData.getLanguage());
		assertEquals("See Output Data Details chapter", analyticData.getSourceText());
		assertEquals("Summary of the document's text.", analyticData.getSummary());
		assertEquals(1, analyticData.getThemes().size());
		assertEquals(1, analyticData.getEntities().size());
		assertEquals(1, analyticData.getTopics().size());
		assertEquals(1, analyticData.getPhrases().size());

		//Check Document Themes
		DocTheme themeObj = analyticData.getThemes().get(0);
		assertEquals(new Integer(1), themeObj.getEvidence());
		assertEquals(new Float(0.0), themeObj.getSentimentScore());
		assertEquals(new Float(0), themeObj.getStrengthScore());
		assertEquals("republican moderates", themeObj.getTitle());
		assertTrue(themeObj.getIsAbout());
		assertEquals("neutral", themeObj.getSentimentPolarity());

		//Check Document Entities
		DocEntity entityObj = analyticData.getEntities().get(0);
		assertEquals(new Integer(0), entityObj.getEvidence());
		assertTrue(entityObj.getIsAbout());
		assertFalse(entityObj.getConfident());
		assertEquals("WASHINGTON", entityObj.getTitle());
		assertEquals(new Float(1.0542796), entityObj.getSentimentScore());
		assertEquals("Place", entityObj.getEntityType());
		assertEquals("named", entityObj.getType());
		assertEquals("positive", entityObj.getSentimentPolarity());

		//Check Entity Themes
		assertEquals(1, entityObj.getThemes().size());
		DocTheme entityThemeObj = entityObj.getThemes().get(0);
		assertEquals(new Integer(1), themeObj.getEvidence());
		assertEquals(new Float(0.0), themeObj.getSentimentScore());
		assertEquals(new Float(0.0), themeObj.getStrengthScore());
		assertEquals("republican moderates", themeObj.getTitle());
		assertTrue(themeObj.getIsAbout());
		assertEquals("neutral", entityThemeObj.getSentimentPolarity());

		Relation relation = analyticData.getRelations().get(0);
		assertEquals("named", relation.getType());
		assertEquals("Occupation", relation.getRelationType());
		assertEquals(new Float(1.0), relation.getConfidenceScore());
		assertEquals("took", relation.getExtra());
		assertEquals(2, relation.getEntities().size());

		RelationEntity relationEntity = relation.getEntities().get(0);
		assertTrue( null != relationEntity );
		assertEquals("head judge", relationEntity.getTitle());
		assertEquals("Job Title", relationEntity.getEntityType());

		//Check Document Topics
		DocTopic topicObj = analyticData.getTopics().get(0);
		assertEquals(new Integer(0), topicObj.getHitCount());
		assertEquals(new Float(0.6133076), topicObj.getSentimentScore());
		assertEquals(new Float(0.6133076), topicObj.getStrengthScore());
		assertEquals("Something", topicObj.getTitle());
		assertEquals("concept", topicObj.getType());

		//Check Document Phrases
		DocPhrase phraseObj = analyticData.getPhrases().get(0);
		assertNotNull(phraseObj);
		assertTrue(phraseObj.getIsNegated());
		assertEquals(new Float(-0.4), phraseObj.getSentimentScore());
		assertEquals("friendly", phraseObj.getTitle());
		assertEquals("not", phraseObj.getNegatingPhrase());
		assertEquals("detected", phraseObj.getType());
		assertEquals("negative", phraseObj.getSentimentPolarity());

	 	//Check Details
		assertTrue( null != analyticData.getDetails());
		Sentence sentence = analyticData.getDetails().get(0);
		assertTrue( null != sentence);
		assertFalse(sentence.getIsImperative());
		assertFalse(sentence.getIsPolar());
		assertTrue( null != sentence.getWords() );
		assertEquals(1, sentence.getWords().size());
		Word word = sentence.getWords().get(0);
		assertEquals("Noun", word.getType());
		assertEquals("NNP", word.getTag());
		assertEquals("Aaron", word.getTitle());
		assertEquals("Aaron", word.getStemmed());
		assertEquals( new Float(0.569) , word.getSentimentScore());
	}

	private void checkCollAnalyticData(CollAnalyticData analyticData)
	{
		assert(null != analyticData);
		assertEquals("23498367", analyticData.getConfigId());
		assertEquals("6F9619FF8B86D011B42D00CF4FC964FF", analyticData.getId());
		assertEquals(TaskStatus.PROCESSED, analyticData.getStatus());
		assertEquals(1, analyticData.getFacets().size());

		Facet facetObj = analyticData.getFacets().get(0);
		assertEquals("Something", facetObj.getLabel());
		assertEquals(new Integer(10), facetObj.getCount());
		assertEquals(new Integer(2), facetObj.getNegativeCount());
		assertEquals(new Integer(1), facetObj.getPositiveCount());
		assertEquals(new Integer(7), facetObj.getNeutralCount());
		assertEquals(1, facetObj.getAttributes().size());
		assertEquals(1, facetObj.getMentions().size());

		Attribute attributeObj = facetObj.getAttributes().get(0);
		Mention mentionObj = facetObj.getMentions().get(0);

		assertEquals("Attribute", attributeObj.getLabel());
		assertEquals(new Integer(5), attributeObj.getCount());
		assertEquals("something", mentionObj.getLabel());

		//Check Collection Themes
		CollTheme themeObj = analyticData.getThemes().get(0);
		assertEquals(new Integer(1), themeObj.getPhrasesCount());
		assertEquals(new Integer(1), themeObj.getThemesCount());
		assertEquals(new Float(0.0), themeObj.getSentimentScore());
		assertEquals("republican moderates", themeObj.getTitle());
		assertEquals("positive", themeObj.getSentimentPolarity());

		//Check Collection Entities
		CollEntity entityObj = analyticData.getEntities().get(0);
		assertEquals("WASHINGTON", entityObj.getTitle());
		assertEquals("named", entityObj.getType());
		assertEquals("Place", entityObj.getEntityType());
		assertEquals(new Integer(1), entityObj.getCount());
		assertEquals(new Integer(1), entityObj.getNegativeCount());
		assertEquals(new Integer(1), entityObj.getNeutralCount());
		assertEquals(new Integer(1), entityObj.getPositiveCount());

		//Check Collection Topics
		assertEquals(1, analyticData.getTopics().size());
		CollTopic topicObj = analyticData.getTopics().get(0);
		assertEquals(new Integer(0), topicObj.getHitCount());
		assertEquals(new Float(0.6133076), topicObj.getSentimentScore());
		assertEquals("Something", topicObj.getTitle());
		assertEquals("concept", topicObj.getType());
		assertEquals("positive", topicObj.getSentimentPolarity());
	}

	private void checkStatistics(Statistics statistics)
	{
		assertTrue( null != statistics );
		assertEquals("Subscriber", statistics.getName());
		assertEquals("active", statistics.getStatus());
		assertEquals( new Long(129863), statistics.getOverallTexts());
		assertEquals( new Long(1300), statistics.getOverallBatches());
		assertEquals( new Long(13769), statistics.getOverallCalls());
		assertEquals( new Long(21), statistics.getCallsSystem());
		assertEquals( new Long(13748), statistics.getCallsData());
		assertEquals( new Long(121863), statistics.getOverallDocs());
		assertEquals( new Long(121863), statistics.getOverallDocs());
		assertEquals( new Long(121860), statistics.getDocsProcessed());
		assertEquals( new Long(3), statistics.getDocsFailed());
		assertEquals( new Long(121863), statistics.getDocsResponded());
		assertEquals( new Long(8), statistics.getOverallColls());
		assertEquals( new Long(8), statistics.getCollsProcessed());
		assertEquals( new Long(0), statistics.getCollsFailed());
		assertEquals( new Long(8), statistics.getCollsResponded());
		assertEquals( new Long(8000), statistics.getCollsDocuments());
		assertEquals( ".Net", statistics.getLatestUsedApp());
		assertEquals( ".Net,Excel add-in x86,Python", statistics.getUsedApps());

		assertTrue( null != statistics.getConfigurations() );
		assertEquals( 1, statistics.getConfigurations().size() );
		assertTrue( null != statistics.getConfigurations().get(0) );

		Details detail = statistics.getConfigurations().get(0);

		assertEquals("A test configuration", detail.getName());
		assertEquals("cd2e7341-a3c2-4fb4-9d3a-779e8b0a5eff", detail.getConfigId());
		assertEquals( new Long(129863), detail.getOverallTexts());
		assertEquals( new Long(1300), detail.getOverallBatches());
		assertEquals( new Long(13769), detail.getOverallCalls());
		assertEquals( new Long(21), detail.getCallsSystem());
		assertEquals( new Long(13748), detail.getCallsData());
		assertEquals( new Long(121863), detail.getOverallDocs());
		assertEquals( new Long(121863), detail.getOverallDocs());
		assertEquals( new Long(121860), detail.getDocsProcessed());
		assertEquals( new Long(3), detail.getDocsFailed());
		assertEquals( new Long(121863), detail.getDocsResponded());
		assertEquals( new Long(8), detail.getOverallColls());
		assertEquals( new Long(8), detail.getCollsProcessed());
		assertEquals( new Long(0), detail.getCollsFailed());
		assertEquals( new Long(8), detail.getCollsResponded());
		assertEquals( new Long(8000), detail.getCollsDocuments());
		assertEquals( ".Net", detail.getLatestUsedApp());
		assertEquals( ".Net,Excel add-in x86,Python", detail.getUsedApps());
	}

}
