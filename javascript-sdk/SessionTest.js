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

SessionTest.prototype.testGetStatus = function() {
	var status = session.getStatus();
	assert(null != status);
	assertEquals("available", status["service_status"]);
};

SessionTest.prototype.testVerifySubscription = function() {
	var subscription = session.verifySubscription();
	assertEquals("active", subscription["status"]);
};

SessionTest.prototype.testGetConfigurations = function() {
	var configurations = session.getConfigurations();
	assert(configurations.length >= 1);
};

SessionTest.prototype.testUpdateConfiguration = function() {
	var configurations = session.getConfigurations();
	assert(configurations.length >= 1);

	var defaultConfiguration = null;
	for (var key in configurations) {
		if (configurations[key]["is_primary"]) {
			defaultConfiguration = configurations[key];
			defaultConfiguration["auto_responding"] = false;
			break;
		}
	}

	assert(null != defaultConfiguration);

	var proxy = session.createUpdateProxy();
	proxy["added"].push(defaultConfiguration);
	var result = session.updateConfigurations(proxy);
	assertEquals(202, result);
};

SessionTest.prototype.testUpdateBlacklist = function() {
	var proxy = session.createUpdateProxy();
	proxy["added"].push("added_test_item");
	var result = session.updateBlacklist(proxy);
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

	proxy = session.createUpdateProxy();
	proxy["removed"].push("added_test_item");
	var result = session.updateBlacklist(proxy);
	assertEquals(202, result);
};

SessionTest.prototype.testUpdateCategories = function() {
	var proxy = session.createUpdateProxy();
	proxy["added"].push({"name":"added_test_categories", "weight":1.3, "samples":["Sample1", "Sample2"]});
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

	proxy = session.createUpdateProxy();
	proxy["removed"].push("added_test_categories");
	var result = session.updateCategories(proxy);
	assertEquals(202, result);
};

SessionTest.prototype.testUpdateQueries = function() {
	var proxy = session.createUpdateProxy();
	proxy["added"].push({"name":"added_test_query", "query":"something"});
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

	proxy = session.createUpdateProxy();
	proxy["removed"].push("added_test_query");
	var result = session.updateQueries(proxy);
	assertEquals(202, result);
};

SessionTest.prototype.testUpdateEntities = function() {
	var proxy = session.createUpdateProxy();
	proxy["added"].push({"name":"added_test_entity", "type":"something"});
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

	proxy = session.createUpdateProxy();
	proxy["removed"].push("added_test_entity");
	var result = session.updateEntities(proxy);
	assertEquals(202, result);
};

SessionTest.prototype.testUpdateSentimentPhrase = function() {
	var proxy = session.createUpdateProxy();
	proxy["added"].push({"title":"added_phrase", "weight":1.4});
	var result = session.updateSentimentPhrases(proxy);
	assertEquals(202, result);

	var phrases = session.getSentimentPhrases();
	var addedItem = null;
	for (var key in phrases) {
		if (phrases[key]["title"] == "added_phrase") {
			addedItem = phrases[key];
			break;
		}
	}

	assert(null != addedItem);

	proxy = session.createUpdateProxy();
	proxy["removed"].push("added_phrase");
	var result = session.updateSentimentPhrases(proxy);
	assertEquals(202, result);
};

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