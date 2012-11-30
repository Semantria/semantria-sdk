import com.semantria.core.Session;
import com.semantria.interfaces.ISerializer;
import com.semantria.interfaces.IUpdateProxy;
import com.semantria.objects.configuration.*;
import com.semantria.objects.mapping.*;
import com.semantria.objects.output.*;
import com.semantria.serializer.JsonSerializer;
import com.semantria.serializer.XmlSerializer;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SerializerTest {
	@Test
	public void testXmlSerializingConfiguration() {
		ISerializer serializer = new XmlSerializer();
		String expectedResult =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<configurations>" +
						"<added>" +
						"<configuration>" +
						"<auto_responding>true</auto_responding>" +
						"<callback>https://anyapi.anydomain.com/processed/docs.json</callback>" +
						"<chars_threshold>80</chars_threshold>" +
						"<collection>" +
						"<concept_topics_limit>5</concept_topics_limit>" +
						"<facet_atts_limit>20</facet_atts_limit>" +
						"<facets_limit>15</facets_limit>" +
						"<named_entities_limit>5</named_entities_limit>" +
						"<query_topics_limit>5</query_topics_limit>" +
						"<themes_limit>0</themes_limit>" +
						"</collection>" +
						"<document>" +
						"<concept_topics_limit>5</concept_topics_limit>" +
						"<entity_themes_limit>5</entity_themes_limit>" +
						"<named_entities_limit>5</named_entities_limit>" +
						"<phrases_limit>0</phrases_limit>" +
						"<query_topics_limit>5</query_topics_limit>" +
						"<summary_limit>0</summary_limit>" +
						"<themes_limit>0</themes_limit>" +
						"<user_entities_limit>5</user_entities_limit>" +
						"</document>" +
						"<is_primary>true</is_primary>" +
						"<language>English</language>" +
						"<name>A test configuration</name>" +
						"<one_sentence>true</one_sentence>" +
						"</configuration>" +
						"<configuration>" +
						"<name>Cloned config</name>" +
						"<one_sentence>false</one_sentence>" +
						"<template>45699836</template>" +
						"</configuration>" +
						"</added>" +
						"<removed>" +
						"<configuration>45699836</configuration>" +
						"</removed>" +
						"</configurations>";

		Configuration config = new Configuration();
		config.setName("A test configuration");
		config.setIsPrimary(true);
		config.setOneSentence(true);
		config.setAutoResponding(true);
		config.setLanguage("English");
		config.setCharsThreshold(80);
		config.setCallback("https://anyapi.anydomain.com/processed/docs.json");

		Stub_Document doc = new Stub_Document();
		doc.setConceptTopicsLimit(5);
		doc.setQueryTopicsLimit(5);
		doc.setNamedEntitiesLimit(5);
		doc.setUserEntitiesLimit(5);
		doc.setThemesLimit(0);
		doc.setEntityThemesLimit(5);
		doc.setPhrasesLimit(0);
		doc.setSummaryLimit(0);
		config.setDocument(doc);

		Stub_Collection coll = new Stub_Collection();
		coll.setFacetsLimit(15);
		coll.setFacetAttributesLimit(20);
		coll.setConceptTopicsLimit(5);
		coll.setQueryTopicsLimit(5);
		coll.setNamedEntitiesLimit(5);
		coll.setThemesLimit(0);
		config.setCollection(coll);

		IUpdateProxy<Configuration> proxy = Session.createSession("", "", new XmlSerializer()).createConfigurationsUpdateProxy();
		proxy.add(config);

		Configuration removedConfig = new Configuration();
		removedConfig.setId("45699836");
		proxy.remove(removedConfig);

		Configuration clonedConfig = new Configuration();
		clonedConfig.setId("45699836");
		clonedConfig.setName("Cloned config");
		clonedConfig.setOneSentence(false);
		proxy.clone(clonedConfig);

		assertEquals(expectedResult, serializer.serialize(proxy));
	}

	@Test
	public void testXmlDeserializingConfiguration() {
		ISerializer serializer = new XmlSerializer();
		String source =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<configurations>" +
						"<configuration>" +
						"<auto_responding>true</auto_responding>" +
						"<callback>https://anyapi.anydomain.com/processed/docs.json</callback>" +
						"<chars_threshold>80</chars_threshold>" +
						"<collection>" +
						"<concept_topics_limit>5</concept_topics_limit>" +
						"<facet_atts_limit>20</facet_atts_limit>" +
						"<facets_limit>15</facets_limit>" +
						"<named_entities_limit>5</named_entities_limit>" +
						"<query_topics_limit>5</query_topics_limit>" +
						"<themes_limit>0</themes_limit>" +
						"</collection>" +
						"<document>" +
						"<concept_topics_limit>5</concept_topics_limit>" +
						"<entity_themes_limit>5</entity_themes_limit>" +
						"<named_entities_limit>5</named_entities_limit>" +
						"<phrases_limit>0</phrases_limit>" +
						"<query_topics_limit>5</query_topics_limit>" +
						"<summary_limit>0</summary_limit>" +
						"<themes_limit>0</themes_limit>" +
						"<user_entities_limit>5</user_entities_limit>" +
						"</document>" +
						"<is_primary>true</is_primary>" +
						"<one_sentence>true</one_sentence>" +
						"<language>English</language>" +
						"<name>A test configuration</name>" +
						"</configuration>" +
						"</configurations>";

		Configuration config = ((Stub_Configurations)serializer.deserialize(source, Stub_Configurations.class)).getConfigurations().get(0);
		assertEquals("A test configuration", config.getName());
		assertTrue(config.getIsPrimary());
		assertTrue(config.getOneSentence());
		assertTrue(config.getAutoResponding());
		assertEquals("English", config.getLanguage());
		assertEquals(new Integer(80), config.getCharsThreshold());
		assertEquals("https://anyapi.anydomain.com/processed/docs.json", config.getCallback());

		Stub_Document doc = config.getDocument();
		assertEquals(new Integer(5), doc.getConceptTopicsLimit());
		assertEquals(new Integer(5), doc.getQueryTopicsLimit());
		assertEquals(new Integer(5), doc.getNamedEntitiesLimit());
		assertEquals(new Integer(5), doc.getUserEntitiesLimit());
		assertEquals(new Integer(0), doc.getThemesLimit());
		assertEquals(new Integer(5), doc.getEntityThemesLimit());
		assertEquals(new Integer(0), doc.getPhrasesLimit());
		assertEquals(new Integer(0), doc.getSummaryLimit());

		Stub_Collection coll = config.getCollection();
		assertEquals(new Integer(5), coll.getConceptTopicsLimit());
		assertEquals(new Integer(5), coll.getQueryTopicsLimit());
		assertEquals(new Integer(5), coll.getNamedEntitiesLimit());
		assertEquals(new Integer(0), coll.getThemesLimit());
		assertEquals(new Integer(15), coll.getFacetsLimit());
		assertEquals(new Integer(20), coll.getFacetAttributesLimit());
	}

	@Test
	public void testJsonSerializingConfiguration() {
		ISerializer serializer = new JsonSerializer();
		String expectedResult =
				"{" +
						"\"added\":[" +
						"{" +
						"\"name\":\"A test configuration\"," +
						"\"one_sentence\":true," +
						"\"auto_responding\":true," +
						"\"is_primary\":true," +
						"\"language\":\"English\"," +
						"\"chars_threshold\":80," +
						"\"document\":{" +
						"\"entity_themes_limit\":5," +
						"\"summary_limit\":0," +
						"\"phrases_limit\":0," +
						"\"themes_limit\":0," +
						"\"query_topics_limit\":5," +
						"\"concept_topics_limit\":5," +
						"\"named_entities_limit\":5," +
						"\"user_entities_limit\":5" +
						"}," +
						"\"collection\":{" +
						"\"facets_limit\":15," +
						"\"facet_atts_limit\":5," +
						"\"themes_limit\":0," +
						"\"query_topics_limit\":5," +
						"\"concept_topics_limit\":5," +
						"\"named_entities_limit\":5" +
						"}," +
						"\"callback\":\"https://anyapi.anydomain.com/processed/docs.json\"" +
						"}," +
						"{" +
						"\"name\":\"Cloned config\"," +
						"\"template\":\"45699836\"," +
						"\"one_sentence\":false" +
						"}" +
						"]," +
						"\"removed\":[\"45699836\"]" +
						"}";

		Configuration config = new Configuration();
		config.setName("A test configuration");
		config.setIsPrimary(true);
		config.setOneSentence(true);
		config.setAutoResponding(true);
		config.setLanguage("English");
		config.setCharsThreshold(80);
		config.setCallback("https://anyapi.anydomain.com/processed/docs.json");

		Stub_Document doc = new Stub_Document();
		doc.setConceptTopicsLimit(5);
		doc.setQueryTopicsLimit(5);
		doc.setNamedEntitiesLimit(5);
		doc.setUserEntitiesLimit(5);
		doc.setThemesLimit(0);
		doc.setEntityThemesLimit(5);
		doc.setPhrasesLimit(0);
		doc.setSummaryLimit(0);
		config.setDocument(doc);

		Stub_Collection coll = new Stub_Collection();
		coll.setFacetsLimit(15);
		coll.setFacetAttributesLimit(5);
		coll.setConceptTopicsLimit(5);
		coll.setQueryTopicsLimit(5);
		coll.setNamedEntitiesLimit(5);
		coll.setThemesLimit(0);
		config.setCollection(coll);

		IUpdateProxy<Configuration> proxy = Session.createSession("", "", new XmlSerializer()).createConfigurationsUpdateProxy();
		proxy.add(config);

		Configuration removedConfig = new Configuration();
		removedConfig.setId("45699836");
		proxy.remove(removedConfig);

		Configuration clonedConfig = new Configuration();
		clonedConfig.setId("45699836");
		clonedConfig.setName("Cloned config");
		clonedConfig.setOneSentence(false);
		proxy.clone(clonedConfig);

		assertEquals(expectedResult, serializer.serialize(proxy));
	}

	@Test
	public void testJsonDeserializingConfiguration() {
		ISerializer serializer = new JsonSerializer();
		String source =
				"[" +
						"{" +
						"\"name\":\"A test configuration\"," +
						"\"auto_responding\":true," +
						"\"is_primary\":true," +
						"\"one_sentence\":true," +
						"\"language\":\"English\"," +
						"\"chars_threshold\":80," +
						"\"document\":{" +
						"\"entity_themes_limit\":5," +
						"\"summary_limit\":0," +
						"\"phrases_limit\":0," +
						"\"themes_limit\":0," +
						"\"query_topics_limit\":5," +
						"\"concept_topics_limit\":5," +
						"\"named_entities_limit\":5," +
						"\"user_entities_limit\":5" +
						"}," +
						"\"collection\":{" +
						"\"facets_limit\":15," +
						"\"facet_atts_limit\":5," +
						"\"themes_limit\":0," +
						"\"query_topics_limit\":5," +
						"\"concept_topics_limit\":5," +
						"\"named_entities_limit\":5" +
						"}," +
						"\"callback\":\"https://anyapi.anydomain.com/processed/docs.json\"" +
						"}" +
						"]";

		Configuration config = ((Stub_Configurations)serializer.deserialize(source, Stub_Configurations.class)).getConfigurations().get(0);
		assertEquals("A test configuration", config.getName());
		assertTrue(config.getIsPrimary());
		assertTrue(config.getOneSentence());
		assertTrue(config.getAutoResponding());
		assertEquals("English", config.getLanguage());
		assertEquals(new Integer(80), config.getCharsThreshold());
		assertEquals("https://anyapi.anydomain.com/processed/docs.json", config.getCallback());

		Stub_Document doc = config.getDocument();
		assertEquals(new Integer(5), doc.getConceptTopicsLimit());
		assertEquals(new Integer(5), doc.getQueryTopicsLimit());
		assertEquals(new Integer(5), doc.getNamedEntitiesLimit());
		assertEquals(new Integer(5), doc.getUserEntitiesLimit());
		assertEquals(new Integer(0), doc.getThemesLimit());
		assertEquals(new Integer(5), doc.getEntityThemesLimit());
		assertEquals(new Integer(0), doc.getPhrasesLimit());
		assertEquals(new Integer(0), doc.getSummaryLimit());

		Stub_Collection coll = config.getCollection();
		assertEquals(new Integer(5), coll.getConceptTopicsLimit());
		assertEquals(new Integer(5), coll.getQueryTopicsLimit());
		assertEquals(new Integer(5), coll.getNamedEntitiesLimit());
		assertEquals(new Integer(0), coll.getThemesLimit());
		assertEquals(new Integer(15), coll.getFacetsLimit());
		assertEquals(new Integer(5), coll.getFacetAttributesLimit());
	}

	@Test
	public void testXmlSerializingBlacklist() {
		ISerializer serializer = new XmlSerializer();
		String expectedResult =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<blacklist>" +
						"<added>" +
						"<item>Added Filter 1</item>" +
						"</added>" +
						"<removed>" +
						"<item>Removed Filter 1</item>" +
						"</removed>" +
						"</blacklist>";

		IUpdateProxy<String> proxy = Session.createSession("", "", new XmlSerializer()).createBlacklistUpdateProxy();
		proxy.add("Added Filter 1");
		proxy.remove("Removed Filter 1");

		assertEquals(expectedResult, serializer.serialize(proxy));
	}

	@Test
	public void testJsonSerializingBlacklist() {
		ISerializer serializer = new JsonSerializer();
		String expectedResult =
				"{" +
						"\"added\":[\".*@.*com\",\".*@com\\\\.net\"]," +
						"\"removed\":[\"http://www\\\\..*\\\\.com\"]" +
						"}";

		IUpdateProxy<String> proxy = Session.createSession("", "", new XmlSerializer()).createBlacklistUpdateProxy();
		proxy.add(".*@.*com");
		proxy.add(".*@com\\.net");
		proxy.remove("http://www\\..*\\.com");

		assertEquals(expectedResult, serializer.serialize(proxy));
	}

	@Test
	public void testXmlDeserializingBlacklist() {
		ISerializer serializer = new XmlSerializer();
		String source =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<blacklist>" +
						"<item>Filter 1</item>" +
						"<item>Filter 2</item>" +
						"</blacklist>";


		List<String> blacklists = ((Stub_Blacklist)serializer.deserialize(source, Stub_Blacklist.class)).getBlacklist();
		assertEquals(2, blacklists.size());
	}

	@Test
	public void testJsonDeserializingBlacklist() {
		ISerializer serializer = new JsonSerializer();
		String source =
				"[" +
						"\".*@.*com\"," +
						"\".*@com\\.net\"" +
						"]";


		List<String> blacklists = ((Stub_Blacklist)serializer.deserialize(source, Stub_Blacklist.class)).getBlacklist();
		assertEquals(2, blacklists.size());
	}

	@Test
	public void testXmlSerializingQueries() {
		ISerializer serializer = new XmlSerializer();
		String expectedResult =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<queries>" +
						"<added>" +
						"<query><name>Query 1</name><query>Something AND something</query></query>" +
						"</added>" +
						"<removed>" +
						"<query>Query 2</query>" +
						"</removed>" +
						"</queries>";

		IUpdateProxy<Query> proxy = Session.createSession("", "", new XmlSerializer()).createQueriesUpdateProxy();
		proxy.add(new Query("Query 1", "Something AND something"));
		proxy.remove(new Query("Query 2", ""));

		assertEquals(expectedResult, serializer.serialize(proxy));
	}

	@Test
	public void testJsonSerializingQueries() {
		ISerializer serializer = new JsonSerializer();
		String expectedResult =
				"{" +
						"\"added\":[" +
						"{" +
						"\"name\":\"Feature: Cloud service\"," +
						"\"query\":\"Amazon AND EC2 AND Cloud\"" +
						"}" +
						"]," +
						"\"removed\":[\"Features\"]" +
						"}";

		IUpdateProxy<Query> proxy = Session.createSession("", "", new XmlSerializer()).createQueriesUpdateProxy();
		proxy.add(new Query("Feature: Cloud service", "Amazon AND EC2 AND Cloud"));
		proxy.remove(new Query("Features", "query 2"));

		assertEquals(expectedResult, serializer.serialize(proxy));
	}

	@Test
	public void testXmlDeserializingQueries() {
		ISerializer serializer = new XmlSerializer();
		String source =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<queries>" +
						"<query><name>Query 1</name><query>Something AND something</query></query>" +
						"<query><name>Query 2</name><query>Something AND something</query></query>" +
						"</queries>";


		List<Query> queries = ((Stub_Queries)serializer.deserialize(source, Stub_Queries.class)).getQueries();
		assertEquals(2, queries.size());
		assertTrue(queries.get(0).getName().startsWith("Query"));
	}

	@Test
	public void testJsonDeserializingQueries() {
		ISerializer serializer = new JsonSerializer();
		String source =
				"[" +
						"{" +
						"\"name\":\"Feature: Cloud service\"," +
						"\"query\":\"Amazon AND EC2 AND Cloud\"" +
						"}" +
						"]";

		List<Query> queries = ((Stub_Queries)serializer.deserialize(source, Stub_Queries.class)).getQueries();
		assertEquals(1, queries.size());
		assertEquals("Feature: Cloud service", queries.get(0).getName());
		assertEquals("Amazon AND EC2 AND Cloud", queries.get(0).getQuery());
	}

	@Test
	public void testXmlSerializingEntities() {
		ISerializer serializer = new XmlSerializer();
		String expectedResult =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<entities>" +
						"<added>" +
						"<entity>" +
						"<name>name 1</name>" +
						"<type>type 1</type>" +
						"</entity>" +
						"</added>" +
						"<removed>" +
						"<entity>name 2</entity>" +
						"</removed>" +
						"</entities>";

		IUpdateProxy<UserEntity> proxy = Session.createSession("", "", new XmlSerializer()).createEntitiesUpdateProxy();
		proxy.add(new UserEntity("name 1", "type 1"));
		proxy.remove(new UserEntity("name 2", "type 2"));


		assertEquals(expectedResult, serializer.serialize(proxy));
	}

	@Test
	public void testJsonSerializingEntities() {
		ISerializer serializer = new JsonSerializer();
		String expectedResult =
				"{" +
						"\"added\":[" +
						"{" +
						"\"name\":\"chair\"," +
						"\"type\":\"furniture\"" +
						"}" +
						"]," +
						"\"removed\":[\"table\"]" +
						"}";

		IUpdateProxy<UserEntity> proxy = Session.createSession("", "", new XmlSerializer()).createEntitiesUpdateProxy();
		proxy.add(new UserEntity("chair", "furniture"));
		proxy.remove(new UserEntity("table", "type 2"));

		assertEquals(expectedResult, serializer.serialize(proxy));
	}


	@Test
	public void testXmlDeserializingEntities() {
		ISerializer serializer = new XmlSerializer();
		String source =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<entities>" +
						"<entity>" +
						"<name>chair</name>" +
						"<type>furniture</type> " +
						"</entity>" +
						"</entities>";


		List<UserEntity> entities = ((Stub_Entities)serializer.deserialize(source, Stub_Entities.class)).getEntities();
		assertEquals(1, entities.size());
		assertEquals("chair", entities.get(0).getName());
		assertEquals("furniture", entities.get(0).getType());
	}

	@Test
	public void testJsonDeserializingEntities() {
		ISerializer serializer = new JsonSerializer();
		String source =
				"[" +
						"{" +
						"\"name\":\"chair\"," +
						"\"type\":\"furniture\"" +
						"}" +
						"]";

		List<UserEntity> entities = ((Stub_Entities)serializer.deserialize(source, Stub_Entities.class)).getEntities();
		assertEquals(1, entities.size());
		assertEquals("chair", entities.get(0).getName());
		assertEquals("furniture", entities.get(0).getType());
	}

	@Test
	public void testXmlSerializingCategories() {
		ISerializer serializer = new XmlSerializer();
		String expectedResult =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<categories>" +
						"<added>" +
						"<category>" +
						"<name>Added Category 1</name>" +
						"<samples>" +
						"<sample>Entity 1</sample>" +
						"<sample>Entity 2</sample>" +
						"<sample>Entity 3</sample>" +
						"</samples>" +
						"<weight>0.2</weight>" +
						"</category>" +
						"</added>" +
						"<removed>" +
						"<category>Removed Category 1</category>" +
						"</removed>" +
						"</categories>";

		IUpdateProxy<Category> proxy = Session.createSession("", "", new XmlSerializer()).createCategoriesUpdateProxy();
		Category addedCategory = new Category();
		addedCategory.setName("Added Category 1");
		addedCategory.setWeight(0.2f);
		List<String> samples = new ArrayList<String>();
		samples.add("Entity 1");
		samples.add("Entity 2");
		samples.add("Entity 3");
		addedCategory.setSamples(samples);
		proxy.add(addedCategory);

		Category removedCategory = new Category();
		removedCategory.setName("Removed Category 1");
		removedCategory.setWeight(0.0f);
		proxy.remove(removedCategory);

		assertEquals(expectedResult, serializer.serialize(proxy));
	}

	@Test
	public void testJsonSerializingCategories() {
		ISerializer serializer = new JsonSerializer();
		String expectedResult =
				"{" +
						"\"added\":[" +
						"{" +
						"\"name\":\"Feature: Cloud service\"," +
						"\"weight\":0.0," +
						"\"samples\":[]" +
						"}" +
						"]," +
						"\"removed\":[\"Features\"]" +
						"}";

		IUpdateProxy<Category> proxy = Session.createSession("", "", new XmlSerializer()).createCategoriesUpdateProxy();
		Category addedCategory = new Category();
		addedCategory.setName("Feature: Cloud service");
		addedCategory.setWeight(0.0f);
		addedCategory.setSamples(new ArrayList<String>());
		proxy.add(addedCategory);

		Category removedCategory = new Category();
		removedCategory.setName("Features");
		proxy.remove(removedCategory);

		assertEquals(expectedResult, serializer.serialize(proxy));
	}

	@Test
	public void testXmlDeserializingCategories() {
		ISerializer serializer = new XmlSerializer();
		String source =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<categories>" +
						"<category>" +
						"<name>Feature: Cloud service</name>" +
						"<weight>0.75</weight>" +
						"<samples>" +
						"<sample>Amazon</sample>" +
						"<sample>EC2</sample>" +
						"</samples>" +
						"</category>" +
						"</categories>";


		List<Category> categories = ((Stub_Categories)serializer.deserialize(source, Stub_Categories.class)).getCategories();
		assertEquals(1, categories.size());
		assertEquals("Feature: Cloud service", categories.get(0).getName());
		assertEquals(new Float(0.75), categories.get(0).getWeight());
		assertEquals(2, categories.get(0).getSamples().size());
	}

	@Test
	public void testJsonDeserializingCategories() {
		ISerializer serializer = new JsonSerializer();
		String source =
				"[" +
						"{" +
						"\"name\":\"Feature: Cloud service\"," +
						"\"weight\":0.75," +
						"\"samples\":[\"Amazon\",\"EC2\"]" +
						"}" +
						"]";

		List<Category> categories = ((Stub_Categories)serializer.deserialize(source, Stub_Categories.class)).getCategories();
		assertEquals(1, categories.size());
		assertEquals("Feature: Cloud service", categories.get(0).getName());
		assertEquals(new Float(0.75), categories.get(0).getWeight());
		assertEquals(2, categories.get(0).getSamples().size());
	}

	@Test
	public void testXmlSerializingSentimentPhrases() {
		ISerializer serializer = new XmlSerializer();
		String expectedResult =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<phrases>" +
						"<added>" +
						"<phrase>" +
						"<title>name 1</title>" +
						"<weight>0.3</weight>" +
						"</phrase>" +
						"</added>" +
						"<removed>" +
						"<phrase>name 2</phrase>" +
						"</removed>" +
						"</phrases>";

		IUpdateProxy<SentimentPhrase> proxy = Session.createSession("", "", new XmlSerializer()).createSentimentPhraseUpdateProxy();
		proxy.add(new SentimentPhrase("name 1", 0.3f));
		proxy.remove(new SentimentPhrase("name 2", 0.0f));

		assertEquals(expectedResult, serializer.serialize(proxy));
	}

	@Test
	public void testJsonSerializingSentimentPhrases() {
		ISerializer serializer = new JsonSerializer();
		String expectedResult =
				"{" +
						"\"added\":[" +
						"{" +
						"\"title\":\"Feature: Cloud service\"," +
						"\"weight\":0.0" +
						"}" +
						"]," +
						"\"removed\":[\"Features\"]" +
						"}";

		IUpdateProxy<SentimentPhrase> proxy = Session.createSession("", "", new XmlSerializer()).createSentimentPhraseUpdateProxy();
		proxy.add(new SentimentPhrase("Feature: Cloud service", 0.0f));
		proxy.remove(new SentimentPhrase("Features", 0.0f));

		assertEquals(expectedResult, serializer.serialize(proxy));
	}

	@Test
	public void testXmlDeserializingSentimentPhrases() {
		ISerializer serializer = new XmlSerializer();
		String source =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<phrases>" +
						"<phrase>" +
						"<title>chair</title>" +
						"<weight>0.3</weight> " +
						"</phrase>" +
						"</phrases>";


		List<SentimentPhrase> sentimentPhrases = ((Stub_SentimentPhrase)serializer.deserialize(source, Stub_SentimentPhrase.class)).getSentimentPhrases();
		assertEquals(1, sentimentPhrases.size());
		assertEquals("chair", sentimentPhrases.get(0).getTitle());
		assertEquals(new Float(0.3), sentimentPhrases.get(0).getWeight());
	}

	@Test
	public void testJsonDeserializingSentimentPhrases() {
		ISerializer serializer = new JsonSerializer();
		String source =
				"[" +
						"{" +
						"\"title\":\"chair\"," +
						"\"weight\":0.75" +
						"}" +
						"]";

		List<SentimentPhrase> sentimentPhrases = ((Stub_SentimentPhrase)serializer.deserialize(source, Stub_SentimentPhrase.class)).getSentimentPhrases();
		assertEquals(1, sentimentPhrases.size());
		assertEquals("chair", sentimentPhrases.get(0).getTitle());
		assertEquals(new Float(0.75), sentimentPhrases.get(0).getWeight());
	}

	@Test
	public void testXmlDeserializingAnalyticServiceStatus() {
		ISerializer serializer = new XmlSerializer();
		String source =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<status>" +
						"<service_status>online</service_status>" +
						"<api_version>2.0</api_version>" +
						"<service_version>1.0.2.63</service_version>" +
						"<supported_encoding>UTF-8</supported_encoding>" +
						"<supported_compression>gzip</supported_compression>" +
						"<supported_languages>" +
						"<language>English</language>" +
						"<language>French</language>" +
						"</supported_languages>" +
						"</status>";


		ServiceStatus status = (ServiceStatus)serializer.deserialize(source, ServiceStatus.class);
		assertNotNull(status);
		assertEquals("online", status.getServiceStatus());
		assertEquals("2.0", status.getApiVersion());
		assertEquals("1.0.2.63", status.getServiceVersion());
		assertEquals("UTF-8", status.getSupportedEncoding());
		assertEquals("gzip", status.getSupportedCompression());
		assertEquals(2, status.getSupportedLanguages().size());
		assertTrue("English".equals(status.getSupportedLanguages().get(0))
				|| "English".equals(status.getSupportedLanguages().get(1)));
	}

	@Test
	public void testJsonDeserializingAnalyticServiceStatus() {
		ISerializer serializer = new JsonSerializer();
		String source =
				"{"+
						"\"service_status\":\"online\","+
						"\"api_version\":\"2.0\","+
						"\"service_version\":\"1.0.2.63\","+
						"\"supported_encoding\":\"UTF-8\","+
						"\"supported_compression\":\"gzip\","+
						"\"supported_languages\":["+
						"\"English\","+
						"\"French\""+
						"]"+
						"}";

		ServiceStatus status = (ServiceStatus)serializer.deserialize(source, ServiceStatus.class);
		assertNotNull(status);
		assertEquals("online", status.getServiceStatus());
		assertEquals("2.0", status.getApiVersion());
		assertEquals("1.0.2.63", status.getServiceVersion());
		assertEquals("UTF-8", status.getSupportedEncoding());
		assertEquals("gzip", status.getSupportedCompression());
		assertEquals(2, status.getSupportedLanguages().size());
		assertTrue("English".equals(status.getSupportedLanguages().get(0))
				|| "English".equals(status.getSupportedLanguages().get(1)));
	}

	@Test
	public void testXmlDeserializingSubscription() {
		ISerializer serializer = new XmlSerializer();
		String source =
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
						"<subscription>" +
						"<name>name</name>" +
						"<status>active</status>" +
						"<priority>normal</priority>" +
						"<expiration_date>1293883200</expiration_date>" +
						"<calls_balance>87</calls_balance>" +
						"<calls_limit>100</calls_limit>" +
						"<calls_limit_interval>60</calls_limit_interval>" +
						"<docs_balance>49897</docs_balance>" +
						"<docs_limit>0</docs_limit>" +
						"<docs_limit_interval>0</docs_limit_interval>" +
						"<configurations_limit>10</configurations_limit>" +
						"<blacklist_limit>100</blacklist_limit>" +
						"<categories_limit>100</categories_limit>" +
						"<queries_limit>100</queries_limit>" +
						"<entities_limit>1000</entities_limit>" +
						"<sentiment_limit>1000</sentiment_limit>" +
						"<characters_limit>8192</characters_limit>" +
						"<batch_limit>1</batch_limit>" +
						"<collection_limit>10</collection_limit>" +
						"<auto_response_limit>2</auto_response_limit>" +
						"<processed_batch_limit>100</processed_batch_limit>" +
						"<callback_batch_limit>100</callback_batch_limit>" +
						"<limit_type>type limit</limit_type>" +
						"</subscription>";


		Subscription subscriptionObj = (Subscription)serializer.deserialize(source, Subscription.class);
		assertNotNull(subscriptionObj);
		assertEquals("name", subscriptionObj.getName());
		assertEquals("active", subscriptionObj.getStatus());
		assertEquals("normal", subscriptionObj.getPriority());
		assertEquals(1293883200, subscriptionObj.getExpirationDate().intValue());
		assertEquals(87, subscriptionObj.getCallsBalance().intValue());
		assertEquals(100, subscriptionObj.getCallsLimit().intValue());
		assertEquals(60, subscriptionObj.getCallsLimitInterval().intValue());
		assertEquals(49897, subscriptionObj.getDocsBalance().intValue());
		assertEquals(0, subscriptionObj.getDocsLimit().intValue());
		assertEquals(0, subscriptionObj.getDocsLimitInterval().intValue());
		assertEquals(10, subscriptionObj.getConfigurationsLimit().intValue());
		assertEquals(100, subscriptionObj.getBlacklistLimit().intValue());
		assertEquals(100, subscriptionObj.getCategoriesLimit().intValue());
		assertEquals(100, subscriptionObj.getQueriesLimit().intValue());
		assertEquals(1000, subscriptionObj.getEntitiesLimit().intValue());
		assertEquals(1000, subscriptionObj.getSentimentLimit().intValue());
		assertEquals(8192, subscriptionObj.getCharactersLimit().intValue());
		assertEquals(1, subscriptionObj.getBatchLimit().intValue());
		assertEquals(10, subscriptionObj.getCollectionLimit().intValue());
		assertEquals(2, subscriptionObj.getAutoResponseLimit().intValue());
		assertEquals(100, subscriptionObj.getProcessedBatchLimit().intValue());
		assertEquals(100, subscriptionObj.getCallbackBatchLimit().intValue());
		assertEquals("type limit", subscriptionObj.getLimitType());
	}

	@Test
	public void testJsonDeserializingSubscription() {
		ISerializer serializer = new JsonSerializer();
		String source =
				"{"+
						"\"name\" : \"Subscriber\"," +
						"\"status\" : \"active\"," +
						"\"priority\" : \"normal\"," +
						"\"expiration_date\" : 1293883200," +
						"\"calls_balance\" : 87," +
						"\"calls_limit\" : 100," +
						"\"calls_limit_interval\" : 60," +
						"\"docs_balance\" : 49897," +
						"\"docs_limit\" : 0," +
						"\"docs_limit_interval\" : 0," +
						"\"configurations_limit\" : 10," +
						"\"blacklist_limit\" : 100," +
						"\"categories_limit\" : 100," +
						"\"queries_limit\" : 100," +
						"\"entities_limit\" : 1000," +
						"\"sentiment_limit\" : 1000," +
						"\"characters_limit\" : 8192," +
						"\"batch_limit\" : 10," +
						"\"collection_limit\" : 10," +
						"\"auto_response_limit\" : 2," +
						"\"processed_batch_limit\" : 100," +
						"\"callback_batch_limit\" : 100," +
						"\"limit_type\" : \"type limit\"" +
						"}";

		Subscription subscription = (Subscription)serializer.deserialize(source, Subscription.class);
		assertNotNull(subscription);
		assertEquals("Subscriber", subscription.getName());
		assertEquals("active", subscription.getStatus());
		assertEquals("normal", subscription.getPriority());
		assertEquals(1293883200, subscription.getExpirationDate().intValue());
		assertEquals(87, subscription.getCallsBalance().intValue());
		assertEquals(100, subscription.getCallsLimit().intValue());
		assertEquals(60, subscription.getCallsLimitInterval().intValue());
		assertEquals(49897, subscription.getDocsBalance().intValue());
		assertEquals(0, subscription.getDocsLimit().intValue());
		assertEquals(0, subscription.getDocsLimitInterval().intValue());
		assertEquals(10, subscription.getConfigurationsLimit().intValue());
		assertEquals(100, subscription.getBlacklistLimit().intValue());
		assertEquals(100, subscription.getCategoriesLimit().intValue());
		assertEquals(100, subscription.getQueriesLimit().intValue());
		assertEquals(1000, subscription.getEntitiesLimit().intValue());
		assertEquals(1000, subscription.getSentimentLimit().intValue());
		assertEquals(8192, subscription.getCharactersLimit().intValue());
		assertEquals(10, subscription.getBatchLimit().intValue());
		assertEquals(10, subscription.getCollectionLimit().intValue());
		assertEquals(2, subscription.getAutoResponseLimit().intValue());
		assertEquals(100, subscription.getProcessedBatchLimit().intValue());
		assertEquals(100, subscription.getCallbackBatchLimit().intValue());
		assertEquals("type limit", subscription.getLimitType());
	}

	@Test
	public void testXmlDeserializingDocumentAnalyticData() {
		ISerializer serializer = new XmlSerializer();

		String source =
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
						"<document>" +
						"<config_id>23498367</config_id>" +
						"<id>6F9619FF8B86D011B42D00CF4FC964FF</id>" +
						"<status>PROCESSED</status>" +
						"<sentiment_score>0.2398756</sentiment_score>" +
						"<summary>Summary of the document's text.</summary>" +
						"<themes>" +
						"<theme>" +
						"<evidence>1</evidence>" +
						"<is_about>true</is_about>" +
						"<strength_score>0.0</strength_score>" +
						"<sentiment_score>0.0</sentiment_score>" +
						"<title>republican moderates</title>" +
						"</theme>" +
						"</themes>" +
						"<entities>" +
						"<entity>" +
						"<evidence>0</evidence>" +
						"<is_about>true</is_about>" +
						"<confident>true</confident>" +
						"<title>WASHINGTON</title>" +
						"<sentiment_score>1.0542796</sentiment_score>" +
						"<type>named</type>" +
						"<entity_type>Place</entity_type>" +
						"<themes>" +
						"<theme>" +
						"<evidence>1</evidence>" +
						"<is_about>true</is_about>" +
						"<strength_score>0.0</strength_score>" +
						"<sentiment_score>0.0</sentiment_score>" +
						"<title>republican moderates</title>" +
						"</theme>" +
						"</themes>" +
						"</entity>" +
						"</entities>" +
						"<topics>" +
						"<topic>" +
						"<title>Something</title>" +
						"<hitcount>0</hitcount>" +
						"<sentiment_score>0.6133076</sentiment_score>" +
						"<strength_score>0.6133076</strength_score>" +
						"<type>concept</type>" +
						"</topic>" +
						"</topics>" +
						"<phrases>" +
						"<phrase>" +
						"<title>Something</title>" +
						"<sentiment_score>0.6133076</sentiment_score>" +
						"<is_negated>true</is_negated>" +
						"<negating_phrase>not</negating_phrase>" +
						"</phrase>" +
						"</phrases>" +
						"</document>";

		DocAnalyticData analyticData = (DocAnalyticData)serializer.deserialize(source, DocAnalyticData.class);
		assertTrue(null != analyticData);
		assertEquals("23498367", analyticData.getConfigId());
		assertEquals("6F9619FF8B86D011B42D00CF4FC964FF", analyticData.getId());
		assertEquals(TaskStatus.PROCESSED, analyticData.getStatus());
		assertEquals(new Float(0.2398756), analyticData.getSentimentScore());
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

		//Check Document Entities
		DocEntity entityObj = analyticData.getEntities().get(0);
		assertEquals(new Integer(0), entityObj.getEvidence());
		assertTrue(entityObj.getIsAbout());
		assertTrue(entityObj.getConfident());
		assertEquals("WASHINGTON", entityObj.getTitle());
		assertEquals(new Float(1.0542796), entityObj.getSentimentScore());
		assertEquals("Place", entityObj.getEntityType());
		assertEquals("named", entityObj.getType());

		//Check Entity Themes
		assertEquals(1, entityObj.getThemes().size());
		DocTheme entityThemeObj = entityObj.getThemes().get(0);
		assertEquals(new Integer(1), themeObj.getEvidence());
		assertEquals(new Float(0.0), themeObj.getSentimentScore());
		assertEquals(new Float(0.0), themeObj.getStrengthScore());
		assertEquals("republican moderates", themeObj.getTitle());
		assertTrue(themeObj.getIsAbout());

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
		assertEquals(new Float(0.6133076), phraseObj.getSentimentScore());
		assertEquals("Something", phraseObj.getTitle());
		assertEquals("not", phraseObj.getNegatingPhrase());
	}

	@Test
	public void testJsonDeserializingDocumentAnalyticData() {
		ISerializer serializerObj = new JsonSerializer();
		String source = "[{"+
				"\"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\","+
				"\"config_id\":\"23498367\","+
				"\"status\":\"PROCESSED\","+
				"\"sentiment_score\":0.8295653,"+
				"\"summary\":\"Summary of the document's text.\","+
				"\"themes\":["+
				"{"+
				"\"evidence\":1,"+
				"\"is_about\":true,"+
				"\"strength_score\":0.0,"+
				"\"sentiment_score\":0.0,"+
				"\"title\":\"republican moderates\""+
				"}"+
				"],"+
				"\"entities\":["+
				"{"+
				"\"type\":\"named\","+
				"\"evidence\":0,"+
				"\"is_about\":true,"+
				"\"confident\":true,"+
				"\"entity_type\":\"Place\","+
				"\"title\":\"WASHINGTON\","+
				"\"sentiment_score\":1.0542796,"+
				"\"themes\":["+
				"{"+
				"\"evidence\":1,"+
				"\"is_about\":true,"+
				"\"strength_score\":0.0,"+
				"\"sentiment_score\":0.0,"+
				"\"title\":\"republican moderates\""+
				"}"+
				"]"+
				"}"+
				"],"+
				"\"topics\":["+
				"{"+
				"\"title\":\"Something\","+
				"\"type\":\"concept\","+
				"\"hitcount\":0,"+
				"\"strength_score\":0.0,"+
				"\"sentiment_score\":0.6133076"+
				"}"+
				"],"+
				"\"phrases\":["+
				"{"+
				"\"title\":\"Something\","+
				"\"is_negated\":true,"+
				"\"negating_phrase\":\"not\","+
				"\"sentiment_score\":0.6133076"+
				"}"+
				"]"+
				"}]";

		List<DocAnalyticData> analyticDataList = ((Stub_DocAnalyticDatas)serializerObj.deserialize(source, Stub_DocAnalyticDatas.class)).getDocuments();
		assert(null != analyticDataList);
		assertEquals(1, analyticDataList.size());
		DocAnalyticData analyticData = analyticDataList.get(0);
		assertEquals("23498367", analyticData.getConfigId());
		assertEquals("6F9619FF8B86D011B42D00CF4FC964FF", analyticData.getId());
		assertEquals(TaskStatus.PROCESSED, analyticData.getStatus());
		assertEquals(new Float(0.8295653), analyticData.getSentimentScore());
		assertEquals("Summary of the document's text.", analyticData.getSummary());
		assertEquals(1, analyticData.getThemes().size());
		assertEquals(1, analyticData.getEntities().size());
		assertEquals(1, analyticData.getTopics().size());

		//Check Document Themes
		DocTheme themeObj = analyticData.getThemes().get(0);
		assertEquals(new Integer(1), themeObj.getEvidence());
		assertEquals(new Float(0.0), themeObj.getSentimentScore());
		assertEquals(new Float(0.0), themeObj.getStrengthScore());
		assertEquals("republican moderates", themeObj.getTitle());
		assert(themeObj.getIsAbout());

		//Check Document Entities
		DocEntity entityObj = analyticData.getEntities().get(0);
		assertEquals(new Integer(0), entityObj.getEvidence());
		assert(entityObj.getIsAbout());
		assert(entityObj.getConfident());
		assertEquals("WASHINGTON", entityObj.getTitle());
		assertEquals(new Float(1.0542796), entityObj.getSentimentScore());
		assertEquals("Place", entityObj.getEntityType());
		assertEquals("named", entityObj.getType());

		//Check Entity Themes
		assertEquals(1, entityObj.getThemes().size());
		DocTheme entityThemeObj = entityObj.getThemes().get(0);
		assertEquals(new Integer(1), themeObj.getEvidence());
		assertEquals(new Float(0.0), themeObj.getSentimentScore());
		assertEquals(new Float(0.0), themeObj.getStrengthScore());
		assertEquals("republican moderates", themeObj.getTitle());
		assert(themeObj.getIsAbout());

		//Check Document Topics
		assertEquals(1, analyticData.getTopics().size());
		DocTopic topicObj = analyticData.getTopics().get(0);
		assertEquals(new Integer(0), topicObj.getHitCount());
		assertEquals(new Float(0.6133076), topicObj.getSentimentScore());
		assertEquals(new Float(0.0), topicObj.getStrengthScore());
		assertEquals("Something", topicObj.getTitle());
		assertEquals("concept", topicObj.getType());

		//Check Document Phrases
		DocPhrase phraseObj = analyticData.getPhrases().get(0);
		assertNotNull(phraseObj);
		assertTrue(phraseObj.getIsNegated());
		assertEquals(new Float(0.6133076), phraseObj.getSentimentScore());
		assertEquals("Something", phraseObj.getTitle());
		assertEquals("not", phraseObj.getNegatingPhrase());
	}

	@Test
	public void testXmlDeserializingCollectionAnalyticData() {
		ISerializer serializer = new XmlSerializer();

		String source = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
				"<collection>" +
				"<config_id>23498367</config_id>" +
				"<id>6F9619FF8B86D011B42D00CF4FC964FF</id>" +
				"<status>PROCESSED</status>" +
				"<facets>" +
				"<facet>" +
				"<label>Something</label>" +
				"<count>10</count>" +
				"<negative_count>2</negative_count>" +
				"<positive_count>1</positive_count>" +
				"<neutral_count>7</neutral_count>" +
				"<attributes>" +
				"<attribute>" +
				"<label>Attribute</label>" +
				"<count>5</count>" +
				"</attribute>" +
				"</attributes>" +
				"</facet>" +
				"</facets>" +
				"<themes>" +
				"<theme>" +
				"<phrases_count>1</phrases_count>" +
				"<themes_count>1</themes_count>" +
				"<sentiment_score>0.0</sentiment_score>" +
				"<title>republican moderates</title>" +
				"</theme>" +
				"</themes>" +
				"<entities>" +
				"<entity>" +
				"<title>WASHINGTON</title>" +
				"<type>named</type>" +
				"<entity_type>Place</entity_type>" +
				"<count>1</count>" +
				"<negative_count>1</negative_count>" +
				"<neutral_count>1</neutral_count>" +
				"<positive_count>1</positive_count>" +
				"</entity>" +
				"</entities>" +
				"<topics>" +
				"<topic>" +
				"<title>Something</title>" +
				"<hitcount>0</hitcount>" +
				"<sentiment_score>0.6133076</sentiment_score>" +
				"<type>concept</type>" +
				"</topic>" +
				"</topics>" +
				"</collection>";

		CollAnalyticData analyticData = (CollAnalyticData)serializer.deserialize(source, CollAnalyticData.class);
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

		Attribute attributeObj = facetObj.getAttributes().get(0);
		assertEquals("Attribute", attributeObj.getLabel());
		assertEquals(new Integer(5), attributeObj.getCount());

		//Check Collection Themes
		CollTheme themeObj = analyticData.getThemes().get(0);
		assertEquals(new Integer(1), themeObj.getPhrasesCount());
		assertEquals(new Integer(1), themeObj.getThemesCount());
		assertEquals(new Float(0.0), themeObj.getSentimentScore());
		assertEquals("republican moderates", themeObj.getTitle());

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
	}

	@Test
	public void testJsonDeserializingCollectionAnalyticData() {
		ISerializer serializer = new JsonSerializer();

		String source =
				"{"+
						"\"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\","+
						"\"config_id\":\"23498367\","+
						"\"status\":\"PROCESSED\","+
						"\"facets\":["+
						"{"+
						"\"label\":\"Something\","+
						"\"count\":10,"+
						"\"negative_count\":2,"+
						"\"positive_count\":1,"+
						"\"neutral_count\":7,"+
						"\"attributes\":["+
						"{"+
						"\"label\":\"Attribute\","+
						"\"count\":5"+
						"}"+
						"]"+
						"}"+
						"],"+
						"\"themes\":["+
						"{"+
						"\"phrases_count\":1,"+
						"\"themes_count\":1,"+
						"\"sentiment_score\":0.0,"+
						"\"title\":\"republican moderates\""+
						"}"+
						"],"+
						"\"entities\":["+
						"{"+
						"\"type\":\"named\","+
						"\"entity_type\":\"Place\","+
						"\"title\":\"WASHINGTON\","+
						"\"count\":1,"+
						"\"negative_count\":1,"+
						"\"neutral_count\":1,"+
						"\"positive_count\":1"+
						"}"+
						"],"+
						"\"topics\":["+
						"{"+
						"\"title\":\"Something\","+
						"\"type\":\"concept\","+
						"\"hitcount\":0,"+
						"\"sentiment_score\":0.6133076"+
						"}"+
						"]"+
						"}";

		CollAnalyticData analyticData = (CollAnalyticData)serializer.deserialize(source, CollAnalyticData.class);
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

		Attribute attributeObj = facetObj.getAttributes().get(0);
		assertEquals("Attribute", attributeObj.getLabel());
		assertEquals(new Integer(5), attributeObj.getCount());

		//Check Collection Themes
		CollTheme themeObj = analyticData.getThemes().get(0);
		assertEquals(new Integer(1), themeObj.getPhrasesCount());
		assertEquals(new Integer(1), themeObj.getThemesCount());
		assertEquals(new Float(0.0), themeObj.getSentimentScore());
		assertEquals("republican moderates", themeObj.getTitle());

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
	}
}
