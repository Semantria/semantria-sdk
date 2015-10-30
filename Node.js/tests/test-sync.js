var assert = require("assert");
var SemantriaSession = require("../").Session;
var config = require('../test-config');
try { config = require('../test-config.override') } catch(e) {}

describe('semantria-sdk [sync]', function() {
	var consumerKey = config.consumerKey,
		consumerSecret = config.consumerSecret,
		session = new SemantriaSession(consumerKey, consumerSecret, "myApp"),
		config_id = false;

	if (config.apiHost) {
		session.API_HOST = config.apiHost
	}

	this.timeout(20000);
	this.slow(10000);

	before(function() {
		var testConfigName = "NODEJS_TEST_CONFIG_" + (new Date()).getTime();
		session.addConfigurations([{
			name: testConfigName,
			is_primary: false,
			auto_response: false,
			language: "English"
		}]);
		var configurations = session.getConfigurations();
		for (var i=0; i<configurations.length; i++) {
			if (configurations[i].name == testConfigName) {
				config_id = configurations[i].config_id;
			}
		}
	});

	after(function() {
		session.removeConfigurations([config_id]);
		config_id = false;
	});

	it('getStatistics()', function () {
		var statistics = session.getStatistics();
		assert.ok( statistics instanceof Object  && typeof statistics.name != "undefined" );
	});

	it('getSubscription()', function () {
		var subscription = session.getSubscription();
		assert.ok( subscription instanceof Object  && typeof subscription.name != "undefined" );
	});

	it( "getStatus()", function() {
		var status = session.getStatus();
		assert.ok( status instanceof Object && typeof status.api_version != "undefined" );
	});

	it( "getSupportedFeatures()", function() {
		var features = session.getSupportedFeatures("English");
		assert.ok( features instanceof Array && features[0].language == "English" );
	});

	describe('[get/add/update/remove] Configurations', function () {
		var configuration = null,
			configuration_id = false,
			configuration_name = "Test configuration",
			configuration2 = null,
			configuration2_id = false,
			configuration2_name = "Test configuration - clone";

		it('getConfigurations()', function () {
			var configurations = session.getConfigurations();
			assert.ok( configurations instanceof Array);
		});

		it('addConfigurations()', function () {
			var result = session.addConfigurations([{
				name: configuration_name,
				is_primary: false,
				auto_response: false,
				language: "English"
			}]);
			assert.equal(result, 202, "addConfigurations() response");

			//check: configuration exists
			var configurations = session.getConfigurations();
			var find_configuration = false;
			for (var i=0; i<configurations.length; i++) {
				if (configurations[i].name == configuration_name) {
					find_configuration = configurations[i];
					configuration_id = configurations[i].config_id;
					configuration = configurations[i];
				}
			}
			assert.ok( find_configuration instanceof Object, "test configuration not exists" );
		});

		it('updateConfigurations()', function () {
			if (!configuration_id) throw 'Test item not was added';
			configuration_name = "Test configuration - renamed";
			configuration.name = configuration_name;
			var result = session.updateConfigurations([configuration]);
			assert.equal(result, 202, "updateConfigurations() response");

			//check: configuration was updated
			var configurations = session.getConfigurations();
			var find_configuration = false;
			for (var i=0; i<configurations.length; i++) {
				if (configurations[i].config_id == configuration_id) {
					find_configuration = configurations[i];
				}
			}
			assert.ok( find_configuration instanceof Object, "test configuration not exists" );
			assert.strictEqual(find_configuration.name, configuration_name, "test configuration has not correct name");
		});

		it('cloneConfiguration()', function () {
			if (!configuration_id) throw 'Test item not was added';
			var result = session.cloneConfiguration(configuration2_name, configuration_id);
			assert.equal(result, 202, "cloneConfiguration() response");

			//check: configuration exists
			var configurations = session.getConfigurations();
			var find_configuration = false;
			for (var i=0; i<configurations.length; i++) {
				if (configurations[i].name == configuration2_name) {
					find_configuration = configurations[i];
					configuration2_id = configurations[i].config_id;
					configuration2 = configurations[i];
				}
			}
			assert.ok( find_configuration instanceof Object, "test configuration not exists" );
		});

		it('removeConfigurations()', function () {
			if (!configuration_id) throw 'Test item not was added';
			var ids = [configuration.config_id];
			if (configuration2_id) ids.push(configuration2_id);
			var result = session.removeConfigurations(ids);
			assert.equal(result, 202, "removeConfigurations() response");

			//check: configuration was removed
			var configurations = session.getConfigurations();
			var find_configuration = false;
			for (var i=0; i<configurations.length; i++) {
				if (configurations[i].config_id == configuration_id) {
					find_configuration = configurations[i];
				}
			}
			assert.strictEqual(find_configuration, false, "test configuration was not removed");
		});
	});

	describe('[get/add/update/remove] Blacklist', function () {
		var blacklist = 'test*';

		it('getBlacklist()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var blacklists = session.getBlacklist(config_id);
			assert.equal(blacklists, 202, "getBlacklist() is expected to return empty result");
		});

		it('addBlacklist()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.addBlacklist([blacklist], config_id);
			assert.equal(result, 202, "addBlacklist() response");

			//check: blacklist exists
			var blacklists = session.getBlacklist(config_id);
			assert.ok(blacklists instanceof Array, "getBlacklist() is expected to return an array");
			assert.strictEqual(blacklists.length, 1, "result array is expected to contain one item");
			assert.strictEqual(blacklists[0], blacklist, "test item has not value");
		});

		it('removeBlacklist()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.removeBlacklist([blacklist], config_id);
			assert.equal(result, 202, "removeBlacklist() response");
			var blacklists = session.getBlacklist(config_id);
			assert.equal(blacklists, 202, "getBlacklist() is expected to return empty result");
		});
	});

	describe('[get/add/update/remove] Categories', function () {
		var category = null,
			category_name = "test category";

		it('getCategories()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var categories = session.getCategories(config_id);
			assert.equal(categories, 202, "getCategories() is expected to return empty result");
		});

		it('addCategories()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.addCategories([{
				name: category_name,
				weight: 0.75,
				samples: ["EC2", "AWS"]
			}], config_id);
			assert.equal(result, 202, "addCategories() response");

			//check: category exists
			var categories = session.getCategories(config_id);
			assert.ok(categories instanceof Array, "getCategories() is expected to return an array");
			assert.strictEqual(categories.length, 1, "result array is expected to contain one item");
			assert.equal(categories[0].name, category_name, "test item has not correct name");
			assert.equal(categories[0].weight, 0.75, "test item has not correct weight");
		});

		it('updateCategories()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.updateCategories([{
				name: category_name,
				weight: 0.85,
				samples: ["EC2", "AWS"]
			}], config_id);
			assert.equal(result, 202, "updateCategories() response");

			//check: category was updated
			var categories = session.getCategories(config_id);
			assert.ok(categories instanceof Array, "getCategories() is expected to return an array");
			assert.strictEqual(categories.length, 1, "result array is expected to contain one item");
			assert.equal(categories[0].name, category_name, "test item has not correct name");
			assert.equal(categories[0].weight, 0.85, "test item has not correct weight");
		});

		it('removeCategories()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.removeCategories([category_name], config_id);
			assert.equal(result, 202, "removeCategories() response");
			var categories = session.getCategories(config_id);
			assert.equal(categories, 202, "getCategories() is expected to return empty result");
		});
	});

	describe('[get/add/update/remove] Queries', function () {
		var query_name = "test query",
			query_query = "Amazon AND (EC2 OR AWS)";

		it('getQueries()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var queries = session.getQueries(config_id);
			assert.equal(queries, 202, "getQueries() is expected to return empty result");
		});

		it('addQueries()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.addQueries([{
				name: query_name,
				query: query_query
			}], config_id);
			assert.equal(result, 202, "addQueries() response");

			//check: query exists
			var queries = session.getQueries(config_id);
			assert.ok(queries instanceof Array, "getQueries() is expected to return an array");
			assert.strictEqual(queries.length, 1, "result array is expected to contain one item");
			assert.strictEqual(queries[0].name, query_name, "test item has not correct name");
			assert.strictEqual(queries[0].query, query_query, "test item has not correct query");
		});

		it('updateQueries()', function () {
			if (!config_id) throw 'Test configuration not exists';
			query_query = "Amazon AND (EC2 OR ec2 OR AWS OR aws)";
			var result = session.updateQueries([{
				name: query_name,
				query: query_query
			}], config_id);
			assert.equal(result, 202, "updateQueries() response");

			//check: query was updated
			var queries = session.getQueries(config_id);
			assert.ok(queries instanceof Array, "getQueries() is expected to return an array");
			assert.strictEqual(queries.length, 1, "result array is expected to contain one item");
			assert.strictEqual(queries[0].name, query_name, "test item has not correct name");
			assert.strictEqual(queries[0].query, query_query, "test item has not correct query");
		});

		it('removeQueries()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.removeQueries([query_name], config_id);
			assert.equal(result, 202, "removeQueries() response");
			var queries = session.getQueries(config_id);
			assert.equal(queries, 202, "getQueries() is expected to return empty result");
		});
	});

	describe('[get/add/update/remove] Entities', function () {
		var entity_name = "test entity",
			entity_type = "testType";

		it('getEntities()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var entities = session.getEntities(config_id);
			assert.equal(entities, 202, "getEntities() is expected to return empty result");
		});

		it('addEntities()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.addEntities([{
				name: entity_name,
				type: entity_type
			}], config_id);
			assert.equal(result, 202, "addEntities() response");

			//check: entity exists
			var entities = session.getEntities(config_id);
			assert.ok(entities instanceof Array, "getEntities() is expected to return an array");
			assert.strictEqual(entities.length, 1, "result array is expected to contain one item");
			assert.strictEqual(entities[0].name, entity_name, "test item has not correct name");
			assert.strictEqual(entities[0].type, entity_type, "test item has not correct type");
		});

		it('updateEntities()', function () {
			if (!config_id) throw 'Test configuration not exists';
			entity_type = "TestType2";
			var result = session.updateEntities([{
				name: entity_name,
				type: entity_type
			}], config_id);
			assert.equal(result, 202, "updateEntities() response");

			//check: entity was updated
			var entities = session.getEntities(config_id);
			assert.ok(entities instanceof Array, "getEntities() is expected to return an array");
			assert.strictEqual(entities.length, 1, "result array is expected to contain one item");
			assert.strictEqual(entities[0].name, entity_name, "test item has not correct name");
			assert.strictEqual(entities[0].type, entity_type, "test item has not correct type");
		});

		it('removeEntities()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.removeEntities([entity_name], config_id);
			assert.equal(result, 202, "removeEntities() response");
			var entities = session.getEntities(config_id);
			assert.equal(entities, 202, "getEntities() is expected to return empty result");
		});
	});

	describe('[get/add/update/remove] Phrases', function () {
		var phrase_name = "test phrase";

		it('getPhrases()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var phrases = session.getPhrases(config_id);
			assert.equal(phrases, 202, "getPhrases() is expected to return empty result");
		});

		it('addPhrases()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.addPhrases([{
				name: phrase_name,
				weight: 0.3
			}], config_id);
			assert.equal(result, 202, "addPhrases() response");

			//check: phrase exists
			var phrases = session.getPhrases(config_id);
			assert.ok(phrases instanceof Array, "getPhrases() is expected to return an array");
			assert.strictEqual(phrases.length, 1, "result array is expected to contain one item");
			assert.strictEqual(phrases[0].name, phrase_name, "test item has not correct name");
			assert.equal(phrases[0].weight, 0.3, "test item has not correct weight");
		});

		it('updatePhrases()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.updatePhrases([{
				name: phrase_name,
				weight: 0.5
			}], config_id);
			assert.equal(result, 202, "updatePhrases() response");

			//check: phrase was updated
			var phrases = session.getPhrases(config_id);
			assert.ok(phrases instanceof Array, "getPhrases() is expected to return an array");
			assert.strictEqual(phrases.length, 1, "result array is expected to contain one item");
			assert.strictEqual(phrases[0].name, phrase_name, "test item has not correct name");
			assert.equal(phrases[0].weight, 0.5, "test item has not correct weight");
		});

		it('removePhrases()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.removePhrases([phrase_name], config_id);
			assert.equal(result, 202, "removePhrases() response");
			var phrases = session.getPhrases(config_id);
			assert.equal(phrases, 202, "getPhrases() is expected to return empty result");
		});
	});

	describe('Document functions', function () {
		it('getProcessedDocuments() - empty', function () {
			if (!config_id) throw 'Test configuration not exists';
			var items = session.getProcessedDocuments(config_id);
			assert.equal(items, 202);
		});

		it('queueDocument()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.queueDocument({
				id: "TEST_DOCUMENT_1",
				text: "it works"
			}, config_id);
			assert.equal(result, 202);
		});

		it('cancelDocument()', function () {
			if (!config_id) throw 'Test configuration not exists';
			session.queueDocument({
				id: "TEST_DOCUMENT_2",
				text: "it works"
			}, config_id);
			var result = session.cancelDocument("TEST_DOCUMENT_2", config_id);
			assert.ok(result instanceof Object);
		});

		it('queueBatchOfDocuments()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.queueBatchOfDocuments([
				{
					id: "TEST_DOCUMENT_3",
					text: "Some text goes here"
				},
				{
					id: "TEST_DOCUMENT_4",
					text: "Some other text goes here"
				}
			], config_id);
			assert.equal(result, 202);
		});

		it('getDocument()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			setTimeout(function() {
				var result = session.getDocument("TEST_DOCUMENT_1", config_id);
				assert.ok(result instanceof Object);
				done();
			}, 2000); //wait for document-processing
		});

		it('getProcessedDocuments() - after processing test items', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var trys = 4;
			var processed = {};
			var testFn = function() {
				var items = session.getProcessedDocuments(config_id);
				if (items instanceof Array) {
					for (var i=0; i<items.length; i++) {
						processed[items[i].id] = true;
					}
				}
				if (Object.keys(processed).length >= 3) {
					var ids = items.map(function(i){return i.id});
					assert.ok(processed['TEST_DOCUMENT_1'], 'TEST_DOCUMENT_1 not present in processed documents');
					assert.ok(processed['TEST_DOCUMENT_3'], 'TEST_DOCUMENT_3 not present in processed documents');
					assert.ok(processed['TEST_DOCUMENT_4'], 'TEST_DOCUMENT_4 not present in processed documents');
					assert.ok(typeof processed['TEST_DOCUMENT_2'] == 'undefined', 'TEST_DOCUMENT_2 present in processed documents')
					done();
					return;
				}
				if (trys > 0) {
					trys--;
					setTimeout(testFn, 1000);
					return;
				}
				assert.ok(items instanceof Array, "getProcessedDocuments() is expected to return an array");
				assert.strictEqual(items.length, 3);
				done();
			}
			setTimeout(testFn, 1000);
		});
	});

	describe('Collection functions', function () {
		it('getProcessedCollections() - empty', function () {
			if (!config_id) throw 'Test configuration not exists';
			var items = session.getProcessedCollections(config_id);
			assert.equal(items, 202);
		});

		it('queueCollection()', function () {
			if (!config_id) throw 'Test configuration not exists';
			var result = session.queueCollection({
				id: "TEST_COLLECTION_1",
				documents: [
					"it works",
					"it works",
					"it works",
					"it works",
					"it works"
				]
			}, config_id);
			assert.equal(result, 202);
		});

		it('cancelCollection()', function () {
			if (!config_id) throw 'Test configuration not exists';
			session.queueCollection({
				id: "TEST_COLLECTION_2",
				documents: [
					"it works",
					"it works",
					"it works",
					"it works",
					"it works"
				]
			}, config_id);
			var result = session.cancelCollection("TEST_COLLECTION_2", config_id);
			assert.ok(result instanceof Object);
		});

		it('getCollection()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			setTimeout(function() {
				var result = session.getCollection("TEST_COLLECTION_1", config_id);
				assert.ok(result instanceof Object);
				done();
			}, 2000); //wait for document-processing
		});

		it('getProcessedCollections() - after processing test items', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var trys = 4;
			var testFn = function() {
				var items = session.getProcessedCollections(config_id);
				if (items instanceof Array && items.length == 1) {
					var ids = items.map(function(i){return i.id});
					assert.notEqual(-1, ids.indexOf('TEST_COLLECTION_1'), 'TEST_COLLECTION_1 not present in processed collections')
					assert.equal(-1, ids.indexOf('TEST_COLLECTION_2'), 'TEST_COLLECTION_2 present in processed collections')
					done();
					return;
				}
				if (trys > 0) {
					trys--;
					setTimeout(testFn, 1000);
					return;
				}
				assert.ok(items instanceof Array, "getProcessedCollections() is expected to return an array");
				assert.strictEqual(items.length, 1);
				done();
			}
			setTimeout(testFn, 1000);
		});
	})
});
