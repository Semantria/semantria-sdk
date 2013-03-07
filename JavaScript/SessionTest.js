SessionTest = AsyncTestCase("SessionTest");
var key = "";
var secret = "";
var serializer = new JsonSerializer();
var session = new Session(key, secret, serializer);
var id = "4532341656s";

function SimpleCallbackHandler() {};
SimpleCallbackHandler.prototype = CallbackHandler;

SimpleCallbackHandler.prototype.onRequest = function(request) {};
SimpleCallbackHandler.prototype.onResponse = function(request) {};
SimpleCallbackHandler.prototype.onError = function(request) {};
SimpleCallbackHandler.prototype.onDocsAutoResponse = function(request) {};
SimpleCallbackHandler.prototype.onCollsAutoResponse = function(request) {};

SessionTest.prototype.testInit = function() {
	session.setCallbackHandler(new SimpleCallbackHandler());
	assert(null != session.consumerKey);
	assert(null != session.consumerSecret);
	assertEquals(serializer.getType(), session.format);
};

//Info calls tests
SessionTest.prototype.testGetStatus = function() {
	var status = session.getStatus();
	assert(null != status);
	assertEquals("available", status["service_status"]);
};

SessionTest.prototype.testGetSubscription = function() {
	var subscription = session.getSubscription();
	assertEquals("active", subscription["status"]);
};

SessionTest.prototype.testGetStatistics = function() {
	var statistics = session.getStatistics();
	assertEquals("active", statistics["status"]);
};

//Config calls
	//Configuration calls
SessionTest.prototype.testGetConfigurations = function() {
	var configurations = session.getConfigurations();
	assert(configurations.length >= 1);
};
SessionTest.prototype.testAddConfiguration = function() {
	var proxy = [ {"name" : "A test configuration", "is_primary" : false, "auto_response" : false, "language" : "English", "chars_threshold" : 80, "one_sentence" : false, "callback" : "https://anyapi.anydomain.com/processed.json", "document" : { "pos_types" : "Noun,Verb,Adjective", "phrases_limit" : 0, "possible_phrases_limit" : 0, "concept_topics_limit" : 5, "query_topics_limit" : 5, "named_entities_limit" : 5, "user_entities_limit" : 5, "entity_themes_limit" : 0, "named_relations_limit" : 0, "user_relations_limit" : 0, "themes_limit" : 0, "summary_limit" : 0, "detect_language" : true }, "collection" : { "facets_limit" : 15, "facet_atts_limit" : 5, "concept_topics_limit" : 5,"query_topics_limit" : 5, "named_entities_limit" : 5, "themes_limit" : 0 } } ];
	var result = session.addConfigurations(proxy);
	assertEquals(202, result);
};
SessionTest.prototype.testUpdateConfiguration = function() {
	var configurations = session.getConfigurations();
	assert(configurations.length >= 1);

	var configurationId = null;
	for (var key in configurations) {
		if (configurations[key]["name"] == "A test configuration") {
			configurationId = configurations[key]["config_id"];
			break;
		}
	}

	assert(null != configurationId);

	var proxy = [ {"config_id" : configurationId, "name" : "A test configuration updated"} ];
	var result = session.updateConfigurations(proxy);
	assertEquals(202, result);
};
SessionTest.prototype.testRemoveConfiguration = function() {
	var configurations = session.getConfigurations();
	assert(configurations.length >= 1);

	var configurationId = null;
	for (var key in configurations) {
		if (configurations[key]["name"] == "A test configuration updated") {
			configurationId = configurations[key]["config_id"];
			break;
		}
	}

	assert(null != configurationId);

	var proxy = [configurationId];
	var result = session.removeConfigurations(proxy);
	assertEquals(202, result);
};
	//Blacklist calls
SessionTest.prototype.testAddBlacklist = function() {
	var proxy = ["added_test_item"];
	var result = session.addBlacklist(proxy);
	assertEquals(202, result);
	
	var blacklists = session.getBlacklist();
	var addedItem = null;
	for (var key in blacklists) {
		if (blacklists[key] == "added_test_item") {
			addedItem = blacklists[key];
			break;
		}
	}

	assert(null != addedItem);	
};
SessionTest.prototype.testRemoveBlacklist = function() {
	var proxy = ["added_test_item"];
	var result = session.removeBlacklist(proxy);
	assertEquals(202, result);
};
	//Categories calls
SessionTest.prototype.testAddCategories = function() {
	var proxy = [{"name":"added_test_categories", "weight":1.3, "samples":["Sample1", "Sample2"]}];
	var result = session.addCategories(proxy);
	assertEquals(202, result);

	var categories = session.getCategories();
	var addedItem = null;
	for (var key in categories) {
		if (categories[key]["name"] == "added_test_categories") {
			addedItem = categories[key];
			break;
		}
	}

	assert(null != addedItem);
};
SessionTest.prototype.testUpdateCategories = function() {
	var proxy = [{"name":"added_test_categories", "weight":1.5}];
	var result = session.updateCategories(proxy);
	assertEquals(202, result);

	var categories = session.getCategories();
	var addedItem = null;
	for (var key in categories) {
		if (categories[key]["name"] == "added_test_categories") {
			addedItem = categories[key];
			break;
		}
	}

	assert(null != addedItem);
};
SessionTest.prototype.testRemoveCategories = function() {
	var proxy = ["added_test_categories"];
	var result = session.removeCategories(proxy);
	assertEquals(202, result);
};
	//Queries calls
SessionTest.prototype.testAddQueries = function() {
	var proxy = [{"name":"added_test_query", "query":"Amazon AND EC2 AND Cloud"}];
	var result = session.addQueries(proxy);
	assertEquals(202, result);

	var queries = session.getQueries();
	var addedItem = null;
	for (var key in queries) {
		if (queries[key]["name"] == "added_test_query") {
			addedItem = queries[key];
			break;
		}
	}
	assert(null != addedItem);
};
SessionTest.prototype.testUpdateQueries = function() {
	var proxy = [{"name":"added_test_query", "query":"Amazon AND EC2 AND Cloud AND Cdn"}];
	var result = session.updateQueries(proxy);
	assertEquals(202, result);

	var queries = session.getQueries();
	var addedItem = null;
	for (var key in queries) {
		if (queries[key]["name"] == "added_test_query") {
			addedItem = queries[key];
			break;
		}
	}
	assert(null != addedItem);
};
SessionTest.prototype.testRemoveQueries = function() {
	var proxy = ["added_test_query"];
	var result = session.removeQueries(proxy);
	assertEquals(202, result);
};
	//Entities calls
SessionTest.prototype.testAddEntities = function() {
	var proxy = [{"name":"added_test_entity", "type":"something"}];
	var result = session.addEntities(proxy);
	assertEquals(202, result);

	var entities = session.getEntities();
	var addedItem = null;
	for (var key in entities) {
		if (entities[key]["name"] == "added_test_entity") {
			addedItem = entities[key];
			break;
		}
	}

	assert(null != addedItem);
};
SessionTest.prototype.testUpdateEntities = function() {
	var proxy = [{"name":"added_test_entity", "type":"something_new"}];
	var result = session.updateEntities(proxy);
	assertEquals(202, result);

	var entities = session.getEntities();
	var addedItem = null;
	for (var key in entities) {
		if (entities[key]["name"] == "added_test_entity") {
			addedItem = entities[key];
			break;
		}
	}

	assert(null != addedItem);
};
SessionTest.prototype.testRemoveEntities = function() {
	var proxy = ["added_test_entity"];
	var result = session.removeEntities(proxy);
	assertEquals(202, result);
};
	//Phrases calls
SessionTest.prototype.testAddPhrases = function() {
	var proxy = [{"name":"added_phrase", "weight":1.4}];
	var result = session.addPhrases(proxy);
	assertEquals(202, result);

	var phrases = session.getPhrases();
	var addedItem = null;
	for (var key in phrases) {
		if (phrases[key]["name"] == "added_phrase") {
			addedItem = phrases[key];
			break;
		}
	}

	assert(null != addedItem);
};
SessionTest.prototype.testUpdatePhrases = function() {
	var proxy = [{"name":"added_phrase", "weight":1.8}];
	var result = session.updatePhrases(proxy);
	assertEquals(202, result);

	var phrases = session.getPhrases();
	var addedItem = null;
	for (var key in phrases) {
		if (phrases[key]["name"] == "added_phrase") {
			addedItem = phrases[key];
			break;
		}
	}

	assert(null != addedItem);
};
SessionTest.prototype.testRemovePhrases = function() {
	var proxy = ["added_phrase"];
	var result = session.removePhrases(proxy);
	assertEquals(202, result);
};

//Data calls
SessionTest.prototype.testQueueDocument = function() {
	var result = session.queueDocument({"id":id, "text":"Some interesting text document"});
	assert(202 >= result);
};

SessionTest.prototype.testGetDocument = function() {
	var result = session.getDocument(id);
	assert(null != result);
};

SessionTest.prototype.testCancelDocument = function() {
	var result = session.cancelDocument(id);
	assert(null != result);
};

SessionTest.prototype.testQueueBatchOfDocuments = function() {
	var batch = [];
	batch.push({"id":id + 1, "text":"Some interesting text document"});
	batch.push({"id":id + 2, "text":"Some interesting text document"});
	batch.push({"id":id + 3, "text":"Some interesting text document"});

	var result = session.queueBatchOfDocuments(batch);
	assert(202 >= result);
};

SessionTest.prototype.testGetProcessedDocuments = function() {
	var result = session.getProcessedDocuments();
	assert(null != result);
};

SessionTest.prototype.testQueueCollection = function() {
	var result = session.queueCollection({"id":id, "documents":["Some interesting text document", "Another interesting text document"]});
	assert(202 >= result);
};

SessionTest.prototype.testGetCollection = function() {
	var result = session.getCollection(id);
	assert(null != result);
};

SessionTest.prototype.testCancelCollection = function() {
	var result = session.cancelCollection(id);
	assert(202 >= result);
};

SessionTest.prototype.testGetProcessedCollection = function() {
	var result = session.getProcessedCollections();
};