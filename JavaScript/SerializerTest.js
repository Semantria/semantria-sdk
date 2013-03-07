SerializerTest = TestCase("SerializerTest");

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