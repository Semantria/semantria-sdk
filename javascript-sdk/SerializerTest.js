SerializerTest = TestCase("SerializerTest");

SerializerTest.prototype.testXmlSerializingConfiguration = function() {
	var expectedResult = '<configurations>' +
	    '<added>' +
	       '<configuration>' +
	          '<name>A test configuration</name>' +
	          '<is_primary>true</is_primary>' +
			   '<one_sentence>true</one_sentence>' +
	          '<auto_responding>true</auto_responding>' +
	          '<language>English</language>' +
	          '<chars_threshold>80</chars_threshold>' +
	          '<callback>https://anyapi.anydomain.com/processed/docs.json</callback>' +
	          '<document>' +
	             '<concept_topics_limit>5</concept_topics_limit>' +
	             '<query_topics_limit>5</query_topics_limit>' +
	             '<named_entities_limit>5</named_entities_limit>' +
	             '<user_entities_limit>5</user_entities_limit>' +
	             '<themes_limit>0</themes_limit>' +
	             '<entity_themes_limit>5</entity_themes_limit>' +
	             '<summary_limit>0</summary_limit>' +
	             '<phrases_limit>0</phrases_limit>' +
	          '</document>' +
	          '<collection>' +
	             '<facets_limit>15</facets_limit>' +
	             '<facet_atts_limit>5</facet_atts_limit>' +
	             '<concept_topics_limit>5</concept_topics_limit>' +
	             '<query_topics_limit>5</query_topics_limit>' +
	             '<named_entities_limit>5</named_entities_limit>' +
	             '<user_entities_limit>5</user_entities_limit>' +
	             '<themes_limit>0</themes_limit>' +
	          '</collection>' +
	       '</configuration>' +
			'<configuration>' +
	          '<name>Cloned configuration</name>' +
	          '<is_primary>true</is_primary>' +
			   '<one_sentence>false</one_sentence>' +
			   '<template>123</template>' +
			'</configuration>' +
	    '</added>' +
	    '<removed>' +
	       '<configuration>45699836</configuration>' +
	    '</removed>' +
	 '</configurations>';

	var config = {};
	config["name"] = "A test configuration";
	config["is_primary"] = true;
	config["one_sentence"] = true;
	config["auto_responding"] = true;
	config["language"] = "English";
	config["chars_threshold"] = 80;
	config["callback"] = "https://anyapi.anydomain.com/processed/docs.json";

	config["document"] = {};
	config["document"]["concept_topics_limit"] = 5;
	config["document"]["query_topics_limit"] = 5;
	config["document"]["named_entities_limit"] = 5;
	config["document"]["user_entities_limit"] = 5;
	config["document"]["themes_limit"] = 0;
	config["document"]["entity_themes_limit"] = 5;
	config["document"]["summary_limit"] = 0;
	config["document"]["phrases_limit"] = 0;

	config["collection"] = {};
	config["collection"]["facets_limit"] = 15;
	config["collection"]["facet_atts_limit"] = 5;
	config["collection"]["concept_topics_limit"] = 5;
	config["collection"]["query_topics_limit"] = 5;
	config["collection"]["named_entities_limit"] = 5;
	config["collection"]["user_entities_limit"] = 5;
	config["collection"]["themes_limit"] = 0;

	clonedConfig = {};
	clonedConfig["config_id"] = "123";
	clonedConfig["name"] = "Cloned configuration";
	clonedConfig["is_primary"] = true;
	clonedConfig["one_sentence"] = false;

	var serializer = new XmlSerializer();
	var session = new Session();
	var proxy = session.createUpdateProxy();
	proxy["added"].push(config);
	proxy["removed"].push("45699836");
	proxy["cloned"].push(clonedConfig);

	assertEquals(expectedResult, serializer.serialize(proxy, {"root":"configurations", "added":"configuration", "removed":"configuration"}));
};


SerializerTest.prototype.testXmlDeserializingConfiguration1 = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
			    "<configurations>" +
				    "<configuration>" +
					    "<auto_responding>true</auto_responding>" +
					    "<callback>google.com</callback>" +
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
					        "<named_entities_limit>100</named_entities_limit>" +
					        "<phrases_limit>1</phrases_limit>" +
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

	var serializer = new XmlSerializer();
	var configs = serializer.deserialize(source, ["configurations"]);
	assertEquals(1, configs.length);

	var config = configs[0];
	assertEquals("A test configuration", config["name"]);

    assertEquals("true", config['is_primary']);
    assertEquals("true", config['one_sentence']);
    assertEquals("true", config['auto_responding']);
    assertEquals("English", config['language']);
    assertEquals(80, config['chars_threshold']);
    assertEquals("google.com", config['callback']);

    assertEquals(5, config['document']['concept_topics_limit']);
    assertEquals(5, config['document']['query_topics_limit']);
    assertEquals(100, config['document']['named_entities_limit']);
    assertEquals(5, config['document']['user_entities_limit']);
    assertEquals(0, config['document']['themes_limit']);
    assertEquals(5, config['document']['entity_themes_limit']);
    assertEquals(1, config['document']['phrases_limit']);
    assertEquals(0, config['document']['summary_limit']);

    assertEquals(5, config['collection']['concept_topics_limit']);
    assertEquals(5, config['collection']['query_topics_limit']);
    assertEquals(5, config['collection']['named_entities_limit']);
    assertEquals(0, config['collection']['themes_limit']);
    assertEquals(15, config['collection']['facets_limit']);
    assertEquals(20, config['collection']['facet_atts_limit']);
};

SerializerTest.prototype.testXmlDeserializingConfiguration2 = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
			    "<configurations>" +
				    "<configuration>" +
					    "<name>A test configuration 1</name>" +
				    "</configuration>" +
				    "<configuration>" +
					    "<name>A test configuration 2</name>" +
				    "</configuration>" +
				"</configurations>";

	var serializer = new XmlSerializer();
	var configs = serializer.deserialize(source, ["configurations"]);

	assertEquals(2, configs.length);
}; 

SerializerTest.prototype.testXmlSerializingBlacklist = function() {
	var expectedResult = 
		"<blacklist>" +
			"<added>" +
				"<item>Added Filter 1</item>" +
				"<item>Added Filter 2</item>" +
			"</added>" +
			"<removed>" +
				"<item>Removed Filter 1</item>" +
			"</removed>" +
		"</blacklist>";
	
	var serializer = new XmlSerializer();
	var session = new Session();
	var proxy = session.createUpdateProxy();
	proxy["added"].push('Added Filter 1');
	proxy["added"].push('Added Filter 2');
	proxy["removed"].push("Removed Filter 1");

	assertEquals(expectedResult, serializer.serialize(proxy, {"root":"blacklist", "added":"item", "removed":"item"}));
};

SerializerTest.prototype.testXmlDeserializingBlacklist1 = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<blacklist>" +
				    "<item>Filter 1</item>" +
				    "<item>Filter 2</item>" +
				"</blacklist>";
	
	var serializer = new XmlSerializer();
	var blacklists = serializer.deserialize(source, ["blacklist"]);

	assertEquals(2, blacklists.length);
	assertEquals("Filter 1", blacklists[0]);
	assertEquals("Filter 2", blacklists[1]);
};

SerializerTest.prototype.testXmlDeserializingBlacklist2 = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<blacklist>" +
				    "<item>Filter 1</item>" +
				"</blacklist>";
	
	var serializer = new XmlSerializer();
	var blacklists = serializer.deserialize(source, ["blacklist"]);

	assertEquals(1, blacklists.length);
	assertEquals("Filter 1", blacklists[0]);
};

SerializerTest.prototype.testXmlSerializingQueries = function() {
	var expectedResult =  
		"<queries>" +
			"<added>" +
				"<query><name>Query 1</name><query>Something AND something</query></query>" +
			"</added>" +
			"<removed>" +
				"<query>Query 2</query>" +
			"</removed>" +
		"</queries>";
	
	var serializer = new XmlSerializer();
	var session = new Session();
	var proxy = session.createUpdateProxy();
	proxy["added"].push({"name":'Query 1', 'query':'Something AND something'});
	proxy["removed"].push("Query 2");

	assertEquals(expectedResult, serializer.serialize(proxy, {"root":"queries", "added":"query", "removed":"query"}));
};

SerializerTest.prototype.testXmDeserializingQueries1 = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
	    "<queries>" +
			"<query><name>Query 1</name><query>Something AND something</query></query>" +
			"<query><name>Query 2</name><query>Something AND something</query></query>" +
		"</queries>";
	
	var serializer = new XmlSerializer();
	var queries = serializer.deserialize(source, ["queries"]);

	assertEquals(2, queries.length);
	assertEquals("Query 1", queries[0]['name']);
	assertEquals("Something AND something", queries[0]['query']);
	assertEquals("Query 2", queries[1]['name']);
	assertEquals("Something AND something", queries[1]['query']);
};

SerializerTest.prototype.testXmlDeserializingQueries2 = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
	    "<queries>" +
			"<query><name>Query 1</name><query>Something AND something</query></query>" +
		"</queries>";
	
	var serializer = new XmlSerializer();
	var queries = serializer.deserialize(source, ["queries"]);

	assertEquals(1, queries.length);
};

SerializerTest.prototype.testXmlSerializingEntities = function() {
	var expectedResult =  
		"<entities>" +
			"<added>" +
				"<entity>" +
					"<name>name 1</name>" +
					"<type>type 1</type>" +
				"</entity>" +
			"</added>" +
			"<removed>" +
				"<entity>name 2</entity>" +
				"<entity>name 3</entity>" +
			"</removed>" +
		"</entities>";
	
	var serializer = new XmlSerializer();
	var proxy = session.createUpdateProxy();
	proxy["added"].push({"name":'name 1', 'type':'type 1'});
	proxy["removed"].push("name 2");
	proxy["removed"].push("name 3");
	
	assertEquals(expectedResult, serializer.serialize(proxy, {"root":"entities", "added":"entity", "removed":"entity"}));
};

SerializerTest.prototype.testXmDeserializingEntities1 = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<entities>" +
				    "<entity>" +
				        "<name>chair</name>" +
				        "<type>furniture</type>" +
				    "</entity>" +
				    "<entity>" +
				        "<name>Cool</name>" +
				        "<type>book</type>" +
				    "</entity>" +
				"</entities>";
	
	var serializer = new XmlSerializer();
	var entities = serializer.deserialize(source, ["entities"]);

	assertEquals(2, entities.length);
	assertEquals("chair", entities[0]['name']);
	assertEquals("furniture", entities[0]['type']);
	assertEquals("Cool", entities[1]['name']);
	assertEquals("book", entities[1]['type']);
};

SerializerTest.prototype.testXmDeserializingEntities2 = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<entities>" +
				    "<entity>" +
				        "<name>chair</name>" +
				        "<type>furniture</type>" +
				    "</entity>" +
				"</entities>";
	
	var serializer = new XmlSerializer();
	var entities = serializer.deserialize(source, ["entities"]);
	assertEquals(1, entities.length);
};

SerializerTest.prototype.testXmlSerializingCategories = function() {
	var expectedResult =  
		"<categories>" +
	        "<added>" +
	            "<category>" +
	                "<name>Added Category 1</name>" +
					"<weight>0.2</weight>" +
	                "<samples>" +
	                    "<sample>Entity 1</sample>" +
	                    "<sample>Entity 2</sample>" +
	                    "<sample>Entity 3</sample>" +
	                "</samples>" +
	            "</category>" +
	        "</added>" +
	        "<removed>" +
	            "<category>Removed Category 1</category>" +
	        "</removed>" +
	    "</categories>";
	
	var serializer = new XmlSerializer();
	var proxy = session.createUpdateProxy();
	proxy["added"].push({"name":'Added Category 1', 'weight':0.2, "samples":['Entity 1', 'Entity 2', 'Entity 3']});
	proxy["removed"].push("Removed Category 1");
	
	assertEquals(expectedResult, serializer.serialize(proxy, {"root":"categories", "added":"category", "removed":"category", "samples":"sample"}));
};

SerializerTest.prototype.testXmDeserializingCategories1 = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<categories>" +
				    "<category>" +
				        "<name>Feature: Cloud service</name>" +
				        "<weight>0.75</weight>" +
				        "<samples>" +
				            "<sample>Amazon</sample>" +
				            "<sample>EC2</sample>" +
				        "</samples>" +
				    "</category>" +
				    "<category>" +
				        "<name>Book</name>" +
				        "<weight>0.1</weight>" +
				        "<samples/>" +
				    "</category>" +
				"</categories>";
	
	var serializer = new XmlSerializer();
	var categories = serializer.deserialize(source, ["categories", "samples"]);

	assertEquals(2, categories.length);
	assertEquals("Feature: Cloud service", categories[0]['name']);
	assertEquals(0.75, categories[0]['weight']);
	assertEquals(2, categories[0]['samples'].length);
	assertEquals('Amazon', categories[0]['samples'][0]);
	assertEquals('EC2', categories[0]['samples'][1]);
	assertEquals("Book", categories[1]['name']);
	assertEquals(0.1, categories[1]['weight']);
	assertEquals(null, categories[1]['samples']);
};

SerializerTest.prototype.testXmDeserializingCategories2 = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
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
	
	var serializer = new XmlSerializer();
	var categories = serializer.deserialize(source, ["categories", "samples"]);
	assertEquals(1, categories.length);
};

SerializerTest.prototype.testXmlSerializingSentimentPhrases = function() {
	var expectedResult =  
		"<phrases>" +
			"<added>" +
				"<phrase>" +
					"<title>name 1</title>" +
					"<weight>0.3</weight>" +
				"</phrase>" +
				"<phrase>" +
					"<title>name 2</title>" +
					"<weight>1.3</weight>" +
				"</phrase>" +
			"</added>" +
			"<removed>" +
				"<phrase>name 3</phrase>" +
			"</removed>" +
		"</phrases>";
	
	var serializer = new XmlSerializer();
	var proxy = session.createUpdateProxy();
	proxy["added"].push({"title":'name 1', 'weight':0.3});
	proxy["added"].push({"title":'name 2', 'weight':1.3});
	proxy["removed"].push("name 3");
	
	assertEquals(expectedResult, serializer.serialize(proxy, {"root":"phrases", "added":"phrase", "removed":"phrase"}));
};

SerializerTest.prototype.testXmDeserializingSentimentPhrases1 = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<phrases>" +
				    "<phrase>" +
				        "<title>chair</title>" +
				        "<weight>0.3</weight> " +
				    "</phrase>" +
				    "<phrase>" +
				        "<title>book</title>" +
				        "<weight>1.3</weight> " +
				    "</phrase>" +
				"</phrases>";
	
	var serializer = new XmlSerializer();
	var phrases = serializer.deserialize(source, ["phrases"]);

	assertEquals(2, phrases.length);
	assertEquals("chair", phrases[0]['title']);
	assertEquals(0.3, phrases[0]['weight']);
	assertEquals("book", phrases[1]['title']);
	assertEquals(1.3, phrases[1]['weight']);
};

SerializerTest.prototype.testXmDeserializingSentimentPhrases2 = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<phrases>" +
				    "<phrase>" +
				        "<title>chair</title>" +
				        "<weight>0.3</weight> " +
				    "</phrase>" +
				"</phrases>";
	
	var serializer = new XmlSerializer();
	var phrases = serializer.deserialize(source, ["phrases"]);
	assertEquals(1, phrases.length);
};

SerializerTest.prototype.testXmDeserializingServiceStatus = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
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

	
	var serializer = new XmlSerializer();
	var status = serializer.deserialize(source, ['supported_languages']);
	
	assertEquals("online", status['service_status']);
	assertEquals("2.0", status['api_version']);
	assertEquals("1.0.2.63", status['service_version']);
	assertEquals("UTF-8", status['supported_encoding']);
	assertEquals("gzip", status['supported_compression']);
	assertEquals(2, status['supported_languages'].length);
	assertEquals("English", status['supported_languages'][0]);
	assertEquals("French", status['supported_languages'][1]);
};

SerializerTest.prototype.testXmDeserializingSubscription = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
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

	
	var serializer = new XmlSerializer();
	var subscription = serializer.deserialize(source);
	
	assertEquals("name", subscription['name']);
	assertEquals("active", subscription['status']);
	assertEquals("normal", subscription['priority']);
	assertEquals(1293883200, subscription['expiration_date']);
	assertEquals(87, subscription['calls_balance']);
	assertEquals(100, subscription['calls_limit']);
	assertEquals(60, subscription['calls_limit_interval']);
	assertEquals(49897, subscription['docs_balance']);
	assertEquals(0, subscription['docs_limit']);
	assertEquals(0, subscription['docs_limit_interval']);
	assertEquals(10, subscription['configurations_limit']);
	assertEquals(100, subscription['blacklist_limit']);
	assertEquals(100, subscription['categories_limit']);
	assertEquals(100, subscription['queries_limit']);
	assertEquals(1000, subscription['entities_limit']);
	assertEquals(1000, subscription['sentiment_limit']);
	assertEquals(8192, subscription['characters_limit']);
	assertEquals(1, subscription['batch_limit']);
	assertEquals(10, subscription['collection_limit']);
	assertEquals(2, subscription['auto_response_limit']);
	assertEquals(100, subscription['processed_batch_limit']);
	assertEquals(100, subscription['callback_batch_limit']);
	assertEquals("type limit", subscription['limit_type']);
};

SerializerTest.prototype.testXmDeserializingDocumentAnalyticData = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
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


	var serializer = new XmlSerializer();
	var doc = serializer.deserialize(source, ['documents', 'themes', 'entities', 'topics', 'phrases']);
	
	// main
	assertEquals("23498367", doc['config_id']);
	assertEquals("6F9619FF8B86D011B42D00CF4FC964FF", doc['id']);
	assertEquals("PROCESSED", doc['status']);
	assertEquals(0.2398756, doc['sentiment_score']);
	assertEquals("Summary of the document's text.", doc['summary']);
	// themes
	assertEquals(1, doc['themes'].length);
	assertEquals(1, doc['themes'][0]['evidence']);
	assert(doc['themes'][0]['is_about'] == 'true');
	assertEquals(0, doc['themes'][0]['strength_score']);
	assertEquals(0, doc['themes'][0]['sentiment_score']);
	assertEquals('republican moderates', doc['themes'][0]['title']);
	// entities
	assertEquals(1, doc['entities'].length);
	assertEquals(0, doc['entities'][0]['evidence']);
	assert(doc['entities'][0]['is_about'] == 'true');
	assert(doc['entities'][0]['confident'] == 'true');
	assertEquals('named', doc['entities'][0]['type']);
	assertEquals(1.0542796, doc['entities'][0]['sentiment_score']);
	assertEquals('WASHINGTON', doc['entities'][0]['title']);
	// entity themes
	assertEquals(1, doc['entities'][0]['themes'].length);
	theme = doc['entities'][0]['themes'][0];
	assertEquals(1, theme['evidence']);
	assert(theme['is_about'] == 'true');
	assertEquals(0, theme['strength_score']);
	assertEquals(0, theme['sentiment_score']);
	assertEquals('republican moderates', theme['title']);
	// topics
	assertEquals(1, doc['topics'].length);
	assertEquals(0, doc['topics'][0]['hitcount']);
	assertEquals('concept', doc['topics'][0]['type']);
	assertEquals(0.6133076, doc['topics'][0]['strength_score']);
	assertEquals(0.6133076, doc['topics'][0]['sentiment_score']);
	assertEquals('Something', doc['topics'][0]['title']);
	// phrases
	assertEquals(1, doc['phrases'].length);
	assertEquals('not', doc['phrases'][0]['negating_phrase']);
	assert(doc['phrases'][0]['is_negated'] == 'true');
	assertEquals(0.6133076, doc['phrases'][0]['sentiment_score']);
	assertEquals('Something', doc['phrases'][0]['title']);
};

SerializerTest.prototype.testXmDeserializingDocumentAnalyticDatas = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
				"<documents>" +
					"<document>" +
						"<config_id>23498367</config_id>" +
						"<id>6F9619FF8B86D011B42D00CF4FC964FF</id>" +
						"<status>PROCESSED</status>" +
						"<sentiment_score>0.2398756</sentiment_score>" +
						"<summary>Summary of the document's text.</summary>" +
					"</document>" +
					"<document>" +
						"<config_id>23498368</config_id>" +
						"<id>6F9619FF8B86D011B42D00CF4FC964FF</id>" +
						"<status>PROCESSED</status>" +
						"<sentiment_score>0.2398756</sentiment_score>" +
						"<summary>Summary of the document's text.</summary>" +
					"</document>" +
				"</documents>";


	var serializer = new XmlSerializer();
	var docs = serializer.deserialize(source, ['documents', 'themes', 'entities', 'topics', 'phrases']);
	assertEquals(2, docs.length);
};


SerializerTest.prototype.testXmDeserializingCollectionAnalyticData = function() {
	var source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>" +
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

	var serializer = new XmlSerializer();
	var coll = serializer.deserialize(source, ['collections', 'themes', 'entities', 'topics', 'facets', 'attributes']);
	
	// main
	assertEquals("23498367", coll['config_id']);
	assertEquals("6F9619FF8B86D011B42D00CF4FC964FF", coll['id']);
	assertEquals("PROCESSED", coll['status']);
	// facets
	assertEquals(1, coll['facets'].length);
	facet = coll['facets'][0];
	assertEquals('Something', facet['label']);
	assertEquals(10, facet['count']);
	assertEquals(2, facet['negative_count']);
	assertEquals(1, facet['positive_count']);
	assertEquals(7, facet['neutral_count']);
	assertEquals(1, facet['attributes'].length);
	assertEquals('Attribute', facet['attributes'][0]['label']);
	assertEquals(5, facet['attributes'][0]['count']);
	// themes
	assertEquals(1, coll['themes'].length);
	assertEquals(1, coll['themes'][0]['phrases_count']); 
	assertEquals(1, coll['themes'][0]['themes_count']); 
	assertEquals(0.0, coll['themes'][0]['sentiment_score']); 
	assertEquals('republican moderates', coll['themes'][0]['title']); 
	// entities
	assertEquals(1, coll['entities'].length);
	assertEquals('WASHINGTON', coll['entities'][0]['title']);
	assertEquals('named', coll['entities'][0]['type']);
	assertEquals('Place', coll['entities'][0]['entity_type']);
	assertEquals(1, coll['entities'][0]['count']);
	assertEquals(1, coll['entities'][0]['negative_count']);
	assertEquals(1, coll['entities'][0]['positive_count']);
	assertEquals(1, coll['entities'][0]['neutral_count']);
	// topics
	assertEquals(1, coll['topics'].length);
	assertEquals(0, coll['topics'][0]['hitcount']);
	assertEquals('concept', coll['topics'][0]['type']);
	assertEquals(0.6133076, coll['topics'][0]['sentiment_score']);
	assertEquals('Something', coll['topics'][0]['title']);
};


SerializerTest.prototype.testJsonSerializingConfiguration = function() {
	var expectedResult = "{" +
			    "\"added\":[" +
				    "{" +
				        "\"name\":\"A test configuration\"," +
				        "\"is_primary\":true," +
				        "\"one_sentence\":true," +
				        "\"auto_responding\":true," +
				        "\"language\":\"English\"," +
				        "\"chars_threshold\":80," +
				        "\"callback\":\"https://anyapi.anydomain.com/processed/docs.json\"," +
				        "\"document\":{" +
				            "\"concept_topics_limit\":5," +
				            "\"query_topics_limit\":5," +
				            "\"named_entities_limit\":5," +
				            "\"user_entities_limit\":5," +
				            "\"themes_limit\":0," +
				            "\"entity_themes_limit\":5," +
				            "\"summary_limit\":0," +
				            "\"phrases_limit\":0" +
				        "}," +
				        "\"collection\":{" +
				            "\"facets_limit\":15," +
				            "\"facet_atts_limit\":5," +
				            "\"concept_topics_limit\":5," +
				            "\"query_topics_limit\":5," +
				            "\"named_entities_limit\":5," +
				            "\"user_entities_limit\":5," +
				            "\"themes_limit\":0" +
				        "}" +
				    "}," +
				    "{" +
				        "\"name\":\"Cloned configuration\"," +
				        "\"is_primary\":true," +
				        "\"one_sentence\":false," +
				        "\"template\":\"123\"" +
				    "}" +
				"]," +
				"\"removed\":[" +
				    "\"45699836\"" +
				"]" +
			"}";

	var config = {};
	config["name"] = "A test configuration";
	config["is_primary"] = true;
	config["one_sentence"] = true;
	config["auto_responding"] = true;
	config["language"] = "English";
	config["chars_threshold"] = 80;
	config["callback"] = "https://anyapi.anydomain.com/processed/docs.json";

	config["document"] = {};
	config["document"]["concept_topics_limit"] = 5;
	config["document"]["query_topics_limit"] = 5;
	config["document"]["named_entities_limit"] = 5;
	config["document"]["user_entities_limit"] = 5;
	config["document"]["themes_limit"] = 0;
	config["document"]["entity_themes_limit"] = 5;
	config["document"]["summary_limit"] = 0;
	config["document"]["phrases_limit"] = 0;

	config["collection"] = {};
	config["collection"]["facets_limit"] = 15;
	config["collection"]["facet_atts_limit"] = 5;
	config["collection"]["concept_topics_limit"] = 5;
	config["collection"]["query_topics_limit"] = 5;
	config["collection"]["named_entities_limit"] = 5;
	config["collection"]["user_entities_limit"] = 5;
	config["collection"]["themes_limit"] = 0;

	clonedConfig = {};
	clonedConfig["config_id"] = "123";
	clonedConfig["name"] = "Cloned configuration";
	clonedConfig["is_primary"] = true;
	clonedConfig["one_sentence"] = false;

	var serializer = new JsonSerializer();
	var proxy = new Session().createUpdateProxy();
	proxy["added"].push(config);
	proxy["removed"].push("45699836");
	proxy["cloned"].push(clonedConfig);

	assertEquals(expectedResult, serializer.serialize(proxy));
};

SerializerTest.prototype.testJsonDeserializingConfiguration = function() {
	var source = "[" +
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
						    "\"phrases_limit\":1," +
						    "\"themes_limit\":10," +
						    "\"query_topics_limit\":25," +
						    "\"concept_topics_limit\":5," +
						    "\"named_entities_limit\":100," +
						    "\"user_entities_limit\":5" +
					    "}," +
					    "\"collection\":{" +
						    "\"facets_limit\":15," +
						    "\"facet_atts_limit\":20," +
						    "\"themes_limit\":0," +
						    "\"query_topics_limit\":5," +
						    "\"concept_topics_limit\":5," +
						    "\"named_entities_limit\":5" +
					    "}," +
					    "\"callback\":\"https://anyapi.anydomain.com/processed/docs.json\"" +
				    "}" +
				"]";

	var serializer = new JsonSerializer();
	var configs = serializer.deserialize(source);

	assertEquals(1, configs.length);
	var config = configs[0];
	assertEquals("A test configuration", config["name"]);

    assert(config['is_primary']);
	assert(config['one_sentence']);
    assert(config['auto_responding']);
    assertEquals("English", config['language']);
    assertEquals(80, config['chars_threshold']);
    assertEquals("https://anyapi.anydomain.com/processed/docs.json", config['callback']);

    assertEquals(5, config['document']['concept_topics_limit']);
    assertEquals(25, config['document']['query_topics_limit']);
    assertEquals(100, config['document']['named_entities_limit']);
    assertEquals(5, config['document']['user_entities_limit']);
    assertEquals(10, config['document']['themes_limit']);
    assertEquals(5, config['document']['entity_themes_limit']);
    assertEquals(1, config['document']['phrases_limit']);
    assertEquals(0, config['document']['summary_limit']);

    assertEquals(5, config['collection']['concept_topics_limit']);
    assertEquals(5, config['collection']['query_topics_limit']);
    assertEquals(5, config['collection']['named_entities_limit']);
    assertEquals(0, config['collection']['themes_limit']);
    assertEquals(15, config['collection']['facets_limit']);
    assertEquals(20, config['collection']['facet_atts_limit']);
};

SerializerTest.prototype.testJsonSerializingBlacklist = function() {
	var expectedResult = "{" +
						    "\"added\":[\".*@.*com\",\".*@com\\\\.net\"]," +
						    "\"removed\":[\"http://www\\\\..*\\\\.com\"]" +
						"}";

	var serializer = new JsonSerializer();
	var proxy = new Session().createUpdateProxy();
	proxy["added"].push(".*@.*com");
	proxy["added"].push(".*@com\\.net");
	proxy["removed"].push("http://www\\..*\\.com");

	assertEquals(expectedResult, serializer.serialize(proxy));
};

SerializerTest.prototype.testJsonDeserializingBlacklist = function() {
	var source = "[" +
				    "\".*@.*com\"," +
				    "\".*@com\\\\.net\"" +
				"]";

	var serializer = new JsonSerializer();
	var items = serializer.deserialize(source);

	assertEquals(2, items.length);
	assertEquals(".*@.*com", items[0]);
	assertEquals(".*@com\\.net", items[1]);
};

SerializerTest.prototype.testJsonSerializingQueries = function() {
	var expectedResult = "{" +
						    "\"added\":[" +
						    "{" +
						        "\"name\":\"Feature: Cloud service\"," +
						        "\"query\":\"Amazon AND EC2 AND Cloud\"" +
						    "}" +
						"]," +
						"\"removed\":[\"Features\"]" +
						"}";

	var serializer = new JsonSerializer();
	var proxy = new Session().createUpdateProxy();
	proxy["added"].push({"name":"Feature: Cloud service", "query":"Amazon AND EC2 AND Cloud"});
	proxy["removed"].push("Features");

	assertEquals(expectedResult, serializer.serialize(proxy));
};

SerializerTest.prototype.testJsonDeserializingQueries = function() {
	var source = "[" +
				    "{" +
					    "\"name\":\"Feature: Cloud service\"," +
					    "\"query\":\"Amazon AND EC2 AND Cloud\"" +
					"}" +
				"]";

	var serializer = new JsonSerializer();
	var queries = serializer.deserialize(source);

	assertEquals(1, queries.length);
	assertEquals("Feature: Cloud service", queries[0]["name"]);
	assertEquals("Amazon AND EC2 AND Cloud", queries[0]["query"]);
};

SerializerTest.prototype.testJsonSerializingEntities = function() {
	var expectedResult = "{" +
						    "\"added\":[" +
						    "{" +
						      "\"name\":\"chair\"," +
						      "\"type\":\"furniture\"" +
						    "}" +
						  "]," +
						  "\"removed\":[\"table\"]" +
						"}";

	var serializer = new JsonSerializer();
	var proxy = new Session().createUpdateProxy();
	proxy["added"].push({"name":"chair", "type":"furniture"});
	proxy["removed"].push("table");

	assertEquals(expectedResult, serializer.serialize(proxy));
};

SerializerTest.prototype.testJsonDeserializingEntities = function() {
	var source = "[" +
				    "{" +
					    "\"name\":\"chair\"," +
					    "\"type\":\"furniture\"" +
					"}" +
				"]";

	var serializer = new JsonSerializer();
	var entities = serializer.deserialize(source);

	assertEquals(1, entities.length);
	assertEquals("chair", entities[0]["name"]);
	assertEquals("furniture", entities[0]["type"]);
};

SerializerTest.prototype.testJsonSerializingCategories = function() {
	var expectedResult = "{" +
						    "\"added\":[" +
						    "{" +
						        "\"name\":\"Feature: Cloud service\"," +
						        "\"weight\":0," +
						        "\"samples\":[]" +
						    "}" +
						"]," +
						"\"removed\":[\"Features\"]" +
						"}";

	var serializer = new JsonSerializer();
	var proxy = new Session().createUpdateProxy();
	proxy["added"].push({"name":"Feature: Cloud service", "weight":0.0, "samples":[]});
	proxy["removed"].push("Features");

	assertEquals(expectedResult, serializer.serialize(proxy));
};

SerializerTest.prototype.testJsonDeserializingCategories = function() {
	var source = "[" +
				    "{" +
					    "\"name\":\"Feature: Cloud service\"," +
					    "\"weight\":0.75," +
					    "\"samples\":[\"Amazon\",\"EC2\"]" +
					"}" +
				"]";

	var serializer = new JsonSerializer();
	var entities = serializer.deserialize(source);

	assertEquals(1, entities.length);
	assertEquals("Feature: Cloud service", entities[0]["name"]);
	assertEquals(0.75, entities[0]["weight"]);
	assertEquals(2, entities[0]["samples"].length);
	assertEquals("Amazon", entities[0]["samples"][0]);
	assertEquals("EC2", entities[0]["samples"][1]);
};

SerializerTest.prototype.testJsonSerializingSentimentPhrases = function() {
	var expectedResult = "{" +
						    "\"added\":[" +
						    "{" +
						        "\"title\":\"Feature: Cloud service\"," +
						        "\"weight\":0" +
						    "}" +
						"]," +
						"\"removed\":[\"Features\"]" +
						"}";

	var serializer = new JsonSerializer();
	var proxy = new Session().createUpdateProxy();
	proxy["added"].push({"title":"Feature: Cloud service", "weight":0.0});
	proxy["removed"].push("Features");

	assertEquals(expectedResult, serializer.serialize(proxy));
};

SerializerTest.prototype.testJsonDeserializingSentimentPhrases = function() {
	var source = "[" +
				    "{" +
					    "\"title\":\"chair\"," +
					    "\"weight\":0.75" +
					"}" +
				"]";

	var serializer = new JsonSerializer();
	var phrases = serializer.deserialize(source);

	assertEquals(1, phrases.length);
	assertEquals("chair", phrases[0]["title"]);
	assertEquals(0.75, phrases[0]["weight"]);
};

SerializerTest.prototype.testJsonDeserializingServiceStatus = function() {
	var source = "{"+
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

	var serializer = new JsonSerializer();
	var status = serializer.deserialize(source);

	assertEquals("online", status["service_status"]);
	assertEquals("2.0", status["api_version"]);
	assertEquals("1.0.2.63", status["service_version"]);
	assertEquals("UTF-8", status["supported_encoding"]);
	assertEquals("gzip", status["supported_compression"]);
	assertEquals(2, status["supported_languages"].length);
	assertEquals("English", status["supported_languages"][0]);
	assertEquals("French", status["supported_languages"][1]);
};

SerializerTest.prototype.testJsonDeserializingSubscription = function() {
	var source = "{"+
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

	var serializer = new JsonSerializer();
	var subscription = serializer.deserialize(source);

	assertEquals("Subscriber", subscription["name"]);
    assertEquals("active", subscription["status"]);
    assertEquals("normal", subscription["priority"]);
    assertEquals(1293883200, subscription["expiration_date"]);
    assertEquals(87, subscription["calls_balance"]);
    assertEquals(100, subscription["calls_limit"]);
    assertEquals(60, subscription["calls_limit_interval"]);
    assertEquals(49897, subscription["docs_balance"]);
    assertEquals(0, subscription["docs_limit"]);
    assertEquals(0, subscription["docs_limit_interval"]);
    assertEquals(10, subscription["configurations_limit"]);
    assertEquals(100, subscription["blacklist_limit"]);
    assertEquals(100, subscription["categories_limit"]);
    assertEquals(100, subscription["queries_limit"]);
    assertEquals(1000, subscription["entities_limit"]);
    assertEquals(1000, subscription["sentiment_limit"]);
    assertEquals(8192, subscription["characters_limit"]);
    assertEquals(10, subscription["batch_limit"]);
    assertEquals(10, subscription["collection_limit"]);
    assertEquals(2, subscription["auto_response_limit"]);
    assertEquals(100, subscription["processed_batch_limit"]);
    assertEquals(100, subscription["callback_batch_limit"]);
	assertEquals("type limit", subscription["limit_type"]);
};

SerializerTest.prototype.testJsonDeserializingDocumentAnalyticData = function() {
	var source = "[{"+
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
				            "\"hitlength\":0,"+
				            "\"strength_score\":0.6133076,"+
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

	var serializer = new JsonSerializer();
	var analyticDatas = serializer.deserialize(source);

	assertEquals(1, analyticDatas.length);
	var doc = analyticDatas[0];

	// Main
    assertEquals("23498367", doc['config_id']);
    assertEquals("6F9619FF8B86D011B42D00CF4FC964FF", doc['id']);
    assertEquals("PROCESSED", doc['status']);
    assertEquals(0.8295653, doc['sentiment_score']);
    assertEquals("Summary of the document's text.", doc['summary']);
    // Themes
    assertEquals(1, doc['themes'].length);
    assertEquals(1, doc['themes'][0]['evidence']);
    assert(doc['themes'][0]['is_about']);
    assertEquals(0, doc['themes'][0]['strength_score']);
    assertEquals(0, doc['themes'][0]['sentiment_score']);
    assertEquals('republican moderates', doc['themes'][0]['title']);
    // Entities
    assertEquals(1, doc['entities'].length);
    assertEquals(0, doc['entities'][0]['evidence']);
    assert(doc['entities'][0]['is_about']);
    assert(doc['entities'][0]['confident']);
    assertEquals('named', doc['entities'][0]['type']);
    assertEquals(1.0542796, doc['entities'][0]['sentiment_score']);
    assertEquals('WASHINGTON', doc['entities'][0]['title']);
    // Entity themes
    assertEquals(1, doc['entities'][0]['themes'].length);
    theme = doc['entities'][0]['themes'][0];
    assertEquals(1, theme['evidence']);
    assert(theme['is_about']);
    assertEquals(0, theme['strength_score']);
    assertEquals(0, theme['sentiment_score']);
    assertEquals('republican moderates', theme['title']);
    // Topics
    assertEquals(1, doc['topics'].length);
    assertEquals(0, doc['topics'][0]['hitlength']);
    assertEquals('concept', doc['topics'][0]['type']);
    assertEquals(0.6133076, doc['topics'][0]['strength_score']);
    assertEquals(0.6133076, doc['topics'][0]['sentiment_score']);
    assertEquals('Something', doc['topics'][0]['title']);
    // Phrases
    assertEquals(1, doc['phrases'].length);
    assertEquals('not', doc['phrases'][0]['negating_phrase']);
    assert(doc['phrases'][0]['is_negated']);
    assertEquals(0.6133076, doc['phrases'][0]['sentiment_score']);
    assertEquals('Something', doc['phrases'][0]['title']);
};

SerializerTest.prototype.testJsonDeserializingCollectionAnalyticData = function() {
	var source = "{"+
				    "\"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\","+
				    "\"config_id\":\"23498367\","+
				    "\"status\":\"PROCESSED\","+
				    "\"facets\":["+
				        "{"+
				            "\"label\":\"Something\","+
				            "\"length\":10,"+
				            "\"negative_length\":2,"+
				            "\"positive_length\":1,"+
				            "\"neutral_length\":7,"+
				            "\"attributes\":["+
				                "{"+
				                    "\"label\":\"Attribute\","+
				                    "\"length\":5"+
				                "}"+
				            "]"+
				        "}"+
				    "],"+
				    "\"themes\":["+
				        "{"+
				            "\"phrases_length\":1,"+
				            "\"themes_length\":1,"+
				            "\"sentiment_score\":0.0,"+
				            "\"title\":\"republican moderates\""+
				        "}"+
				    "],"+
				    "\"entities\":["+
				        "{"+
				            "\"type\":\"named\","+
				            "\"entity_type\":\"Place\","+
				            "\"title\":\"WASHINGTON\","+
				            "\"length\":1,"+
				            "\"negative_length\":1,"+
				            "\"neutral_length\":1,"+
				            "\"positive_length\":1"+
				        "}"+
				    "],"+
				    "\"topics\":["+
				        "{"+
				            "\"title\":\"Something\","+
				            "\"type\":\"concept\","+
				            "\"hitlength\":0,"+
				            "\"sentiment_score\":0.6133076"+
				        "}"+
				    "]"+
			    "}";

	var serializer = new JsonSerializer();
	var coll = serializer.deserialize(source);

	// Main
    assertEquals("23498367", coll['config_id']);
    assertEquals("6F9619FF8B86D011B42D00CF4FC964FF", coll['id']);
    assertEquals("PROCESSED", coll['status']);
    // Facets
    assertEquals(1, coll['facets'].length);
    facet = coll['facets'][0];
    assertEquals('Something', facet['label']);
    assertEquals(10, facet['length']);
    assertEquals(2, facet['negative_length']);
    assertEquals(1, facet['positive_length']);
    assertEquals(7, facet['neutral_length']);
    assertEquals(1, facet['attributes'].length);
    assertEquals('Attribute', facet['attributes'][0]['label']);
    assertEquals(5, facet['attributes'][0]['length']);
    // Themes
    assertEquals(1, coll['themes'].length);
    assertEquals(1, coll['themes'][0]['phrases_length']);
    assertEquals(1, coll['themes'][0]['themes_length']);
    assertEquals(0.0, coll['themes'][0]['sentiment_score']);
    assertEquals('republican moderates', coll['themes'][0]['title']);
    // Entities
    assertEquals(1, coll['entities'].length);
    assertEquals('WASHINGTON', coll['entities'][0]['title']);
    assertEquals('named', coll['entities'][0]['type']);
    assertEquals('Place', coll['entities'][0]['entity_type']);
    assertEquals(1, coll['entities'][0]['length']);
    assertEquals(1, coll['entities'][0]['negative_length']);
    assertEquals(1, coll['entities'][0]['positive_length']);
    assertEquals(1, coll['entities'][0]['neutral_length']);
    // Topics
    assertEquals(1, coll['topics'].length);
    assertEquals(0, coll['topics'][0]['hitlength']);
    assertEquals('concept', coll['topics'][0]['type']);
    assertEquals(0.6133076, coll['topics'][0]['sentiment_score']);
    assertEquals('Something', coll['topics'][0]['title']);
};

SerializerTest.prototype.testXmlSerializingDocument = function() {
	var expectedResult = 
		'<document>' +
	        '<id>6F9619FF8B86D011B42D00CF4FC964FF</id>' +
	        '<text>A chunk of text for processing</text>' +
	     '</document>';
	
	var serializer = new XmlSerializer();
	var session = new Session();
	var doc = {"id":"6F9619FF8B86D011B42D00CF4FC964FF", "text":"A chunk of text for processing"};

	assertEquals(expectedResult, serializer.serialize(doc, {'root':'document'}));
};

SerializerTest.prototype.testXmlSerializingBatchOfDocuments = function() {
	var expectedResult = 
		'<documents>' +
		    '<document>' +
		       '<id>6F9619FF8B86D011B42D00CF4FC964FF</id>' +
		       '<text>A chunk of text for processing</text>' +
		    '</document>' +
		    '<document>' +
		       '<id>7F9619FF8B86D011B42D00CF4FC964FF</id>' +
		       '<text>A chunk of text for processing</text>' +
		    '</document>' + 
		'</documents>';
	
	var serializer = new XmlSerializer();
	var session = new Session();
	var docs = new Array();
	docs.push({"id":"6F9619FF8B86D011B42D00CF4FC964FF", "text":"A chunk of text for processing"});
	docs.push({"id":"7F9619FF8B86D011B42D00CF4FC964FF", "text":"A chunk of text for processing"});

	assertEquals(expectedResult, serializer.serialize(docs, {'root':'documents', 'element':'document'}));
};

SerializerTest.prototype.testXmlSerializingCollection = function() {
	var expectedResult = 
		'<collection>' +
		   '<id>6F9619FF8B86D011B42D00CF4FC964FF</id>' +
		   '<documents>' +
		      '<document>A chunk of text for processing</document>' +
		      '<document>A chunk of text for processing</document>' +
		      '<document>A chunk of text for processing</document>' +
		   '</documents>' +
		'</collection>';
	
	var serializer = new XmlSerializer();
	var session = new Session();
	var coll = {"id":"6F9619FF8B86D011B42D00CF4FC964FF", "documents":["A chunk of text for processing", "A chunk of text for processing", "A chunk of text for processing"]};

	assertEquals(expectedResult, serializer.serialize(coll, {'root':'collection', 'collectionElement':'document'}));
};