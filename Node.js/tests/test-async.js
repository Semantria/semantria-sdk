var assert = require("assert");
var SemantriaSession = require("../").Session;
var config = require('../test-config');
try { config = require('../test-config.override') } catch(e) {}

describe('semantria-sdk [async]', function() {
	var consumerKey = config.consumerKey,
		consumerSecret = config.consumerSecret,
		session = new SemantriaSession(consumerKey, consumerSecret, "myApp"),
		config_id = false;

	if (config.apiHost) {
		session.API_HOST = config.apiHost
	}

	this.timeout(20000);
	this.slow(10000);

	before(function( done ) {
		var testConfigName = "NODEJS_TEST_CONFIG_" + (new Date()).getTime();
		session.addConfigurations([{
			name: testConfigName,
			is_primary: false,
			auto_response: false,
			language: "English"
		}], function () {
			session.getConfigurations(function (configurations) {
				for (var i=0; i<configurations.length; i++) {
					if (configurations[i].name == testConfigName) {
						config_id = configurations[i].config_id;
					}
				}
				done();
			});
		});
	});

	after(function( done ) {
		session.removeConfigurations([config_id], function() {
			config_id = false;
			done();
		});
	});

	it('getStatistics()', function ( done ) {
		var async_wait = true;
		session.getStatistics(function(statistics) {
			async_wait = false;
			assert.ok( statistics instanceof Object  && typeof statistics.name != "undefined" );
			done();
		});
		assert.ok(async_wait, 'synchronous call for getStatistics()');
	});

	it('getSubscription()', function ( done ) {
		var async_wait = true;
		session.getSubscription(function(subscription){
			async_wait = false;
			assert.ok( subscription instanceof Object  && typeof subscription.name != "undefined" );
			done();
		});
		assert.ok(async_wait, 'synchronous call for getSubscription()');
	});

	it( "getStatus()", function( done ) {
		var async_wait = true;
		session.getStatus(function(status){
			async_wait = false;
			assert.ok( status instanceof Object && typeof status.api_version != "undefined" );
			done();
		});
		assert.ok(async_wait, 'synchronous call for getStatus()');
	});

	it( "getSupportedFeatures()", function( done ) {
		var async_wait = true;
		session.getSupportedFeatures("English", function(features){
			async_wait = false;
			assert.ok( features instanceof Array && features[0].language == "English" );
			done();
		});
		assert.ok(async_wait, 'synchronous call for getSupportedFeatures()');
	});

	describe('[get/add/update/remove] Configurations', function () {
		var configuration = null,
			configuration_id = false,
			configuration_name = "Test configuration";

		it('getConfigurations()', function ( done ) {
			var async_wait = true;
			session.getConfigurations(function(configurations){
				async_wait = false;
				assert.ok( configurations instanceof Array);
				done();
			});
			assert.ok(async_wait, 'synchronous call for getConfigurations()');
		});

		it('addConfigurations()', function ( done ) {
			var async_wait = true;
			session.addConfigurations(
				[{
					name: configuration_name,
					is_primary: false,
					auto_response: false,
					language: "English"
				}],
				function(result) {
					async_wait = false;
					assert.equal(result, 202, "addConfigurations() response");

					//check: configuration exists
					session.getConfigurations(function(configurations){
						var find_configuration = false;
						for (var i=0; i<configurations.length; i++) {
							if (configurations[i].name == configuration_name) {
								find_configuration = configurations[i];
								configuration_id = configurations[i].config_id;
								configuration = configurations[i];
							}
						}
						assert.ok( find_configuration instanceof Object, "test configuration not exists" );
						done();
					});
				}
			);
			assert.ok(async_wait, 'synchronous call for addConfigurations()');
		});

		it('updateConfigurations()', function ( done ) {
			if (!configuration_id) throw 'Test item not was added';
			var async_wait = true;
			configuration_name = "Test configuration - renamed";
			configuration.name = configuration_name;
			session.updateConfigurations([configuration], function (result) {
				async_wait = false;
				assert.equal(result, 202, "updateConfigurations() response");

				//check: configuration was updated
				session.getConfigurations(function(configurations){
					var find_configuration = false;
					for (var i=0; i<configurations.length; i++) {
						if (configurations[i].config_id == configuration_id) {
							find_configuration = configurations[i];
						}
					}
					assert.ok( find_configuration instanceof Object, "test configuration not exists" );
					assert.strictEqual(find_configuration.name, configuration_name, "test configuration has not correct name");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for updateConfigurations()');
		});

		it('removeConfigurations()', function ( done ) {
			if (!configuration_id) throw 'Test item not was added';
			var async_wait = true;
			session.removeConfigurations([configuration_id], function (result) {
				async_wait = false;
				assert.equal(result, 202, "removeConfigurations() response");

				//check: configuration was removed
				session.getConfigurations(function(configurations){
					var find_configuration = false;
					for (var i=0; i<configurations.length; i++) {
						if (configurations[i].config_id == configuration_id) {
							find_configuration = configurations[i];
						}
					}
					assert.strictEqual(find_configuration, false, "test configuration was not removed");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for removeConfigurations()');
		});
	});

	describe('[get/add/update/remove] Blacklist', function () {
		var blacklist = 'test*';

		it('getBlacklist()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists'
			var async_wait = true;
			session.getBlacklist(config_id, function (blacklists) {
				async_wait = false;
				assert.equal(blacklists, 202, "getBlacklist() is expected to return empty result");
				done();
			});
			assert.ok(async_wait, 'synchronous call for getBlacklist()');
		});

		it('addBlacklist()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.addBlacklist([blacklist], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "removeBlacklist() response");

				//check: blacklist exists
				session.getBlacklist(config_id, function (blacklists) {
					assert.ok(blacklists instanceof Array, "getBlacklist() is expected to return an array");
					assert.strictEqual(blacklists.length, 1, "result array is expected to contain one item");
					assert.strictEqual(blacklists[0], blacklist, "test item has not value");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for addBlacklist()');
		});

		it('removeBlacklist()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.removeBlacklist([blacklist], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "removeBlacklist() response");
				session.getBlacklist(config_id, function (blacklists) {
					assert.equal(blacklists, 202, "getBlacklist() is expected to return empty result");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for removeBlacklist()');
		});
	});

	describe('[get/add/update/remove] Categories', function () {
		var category = null,
			category_name = "test category";

		it('getCategories()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists'
			var async_wait = true;
			session.getCategories(config_id, function (categories) {
				async_wait = false;
				assert.equal(categories, 202, "getCategories() is expected to return empty result");
				done();
			});
			assert.ok(async_wait, 'synchronous call for getCategories()');
		});

		it('addCategories()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.addCategories([{
				name: category_name,
				weight: 0.75,
				samples: ["EC2", "AWS"]
			}], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "addCategories() response");

				//check: category exists
				session.getCategories(config_id, function (categories) {
					assert.ok(categories instanceof Array, "getCategories() is expected to return an array");
					assert.strictEqual(categories.length, 1, "result array is expected to contain one item");
					assert.equal(categories[0].name, category_name, "test item has not correct name");
					assert.equal(categories[0].weight, 0.75, "test item has not correct weight");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for addCategories()');
		});

		it('updateCategories()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.updateCategories([{
				name: category_name,
				weight: 0.85,
				samples: ["EC2", "AWS"]
			}], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "updateCategories() response");

				//check: category was updated
				session.getCategories(config_id, function (categories) {
					assert.ok(categories instanceof Array, "getCategories() is expected to return an array");
					assert.strictEqual(categories.length, 1, "result array is expected to contain one item");
					assert.equal(categories[0].name, category_name, "test item has not correct name");
					assert.equal(categories[0].weight, 0.85, "test item has not correct weight");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for updateCategories()');
		});

		it('removeCategories()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.removeCategories([category_name], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "removeCategories() response");
				session.getCategories(config_id, function (categories) {
					assert.equal(categories, 202, "getCategories() is expected to return empty result");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for removeCategories()');
		});
	});

	describe('[get/add/update/remove] Queries', function () {
		var query_name = "test query",
			query_query = "Amazon AND (EC2 OR AWS)";

		it('getQueries()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists'
			var async_wait = true;
			session.getQueries(config_id, function (queries) {
				async_wait = false;
				assert.equal(queries, 202, "getQueries() is expected to return empty result");
				done();
			});
			assert.ok(async_wait, 'synchronous call for getQueries()');
		});

		it('addQueries()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.addQueries([{
				name: query_name,
				query: query_query
			}], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "addQueries() response");

				//check: query exists
				session.getQueries(config_id, function (queries) {
					assert.ok(queries instanceof Array, "getQueries() is expected to return an array");
					assert.strictEqual(queries.length, 1, "result array is expected to contain one item");
					assert.strictEqual(queries[0].name, query_name, "test item has not correct name");
					assert.strictEqual(queries[0].query, query_query, "test item has not correct query");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for addQueries()');
		});

		it('updateQueries()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			query_query = "Amazon AND (EC2 OR ec2 OR AWS OR aws)";
			session.updateQueries([{
				name: query_name,
				query: query_query
			}], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "updateQueries() response");

				//check: query was updated
				session.getQueries(config_id, function (queries) {
					assert.ok(queries instanceof Array, "getQueries() is expected to return an array");
					assert.strictEqual(queries.length, 1, "result array is expected to contain one item");
					assert.strictEqual(queries[0].name, query_name, "test item has not correct name");
					assert.strictEqual(queries[0].query, query_query, "test item has not correct query");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for updateQueries()');
		});

		it('removeQueries()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.removeQueries([query_name], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "removeQueries() response");
				session.getQueries(config_id, function (queries) {
					assert.equal(queries, 202, "getQueries() is expected to return empty result");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for removeQueries()');
		});
	});

	describe('[get/add/update/remove] Entities', function () {
		var entity_name = "test entity",
			entity_type = "testType";

		it('getEntities()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists'
			var async_wait = true;
			session.getEntities(config_id, function (entities) {
				async_wait = false;
				assert.equal(entities, 202, "getEntities() is expected to return empty result");
				done();
			});
			assert.ok(async_wait, 'synchronous call for getEntities()');
		});

		it('addEntities()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.addEntities([{
				name: entity_name,
				type: entity_type
			}], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "addEntities() response");

				//check: entity exists
				session.getEntities(config_id, function (entities) {
					assert.ok(entities instanceof Array, "getEntities() is expected to return an array");
					assert.strictEqual(entities.length, 1, "result array is expected to contain one item");
					assert.strictEqual(entities[0].name, entity_name, "test item has not correct name");
					assert.strictEqual(entities[0].type, entity_type, "test item has not correct type");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for addEntities()');
		});

		it('updateEntities()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			entity_type = "TestType2";
			session.updateEntities([{
				name: entity_name,
				type: entity_type
			}], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "updateEntities() response");

				//check: entity was updated
				session.getEntities(config_id, function (entities) {
					assert.ok(entities instanceof Array, "getEntities() is expected to return an array");
					assert.strictEqual(entities.length, 1, "result array is expected to contain one item");
					assert.strictEqual(entities[0].name, entity_name, "test item has not correct name");
					assert.strictEqual(entities[0].type, entity_type, "test item has not correct type");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for updateEntities()');
		});

		it('removeEntities()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.removeEntities([entity_name], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "removeEntities() response");
				session.getEntities(config_id, function (entities) {
					assert.equal(entities, 202, "getEntities() is expected to return empty result");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for removeEntities()');
		});
	});

	describe('[get/add/update/remove] Phrases', function () {
		var phrase_name = "test phrase";

		it('getPhrases()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists'
			var async_wait = true;
			session.getPhrases(config_id, function (phrases) {
				async_wait = false;
				assert.equal(phrases, 202, "getPhrases() is expected to return empty result");
				done();
			});
			assert.ok(async_wait, 'synchronous call for getPhrases()');
		});

		it('addPhrases()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.addPhrases([{
				name: phrase_name,
				weight: 0.3
			}], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "addPhrases() response");

				//check: phrase exists
				session.getPhrases(config_id, function (phrases) {
					assert.ok(phrases instanceof Array, "getPhrases() is expected to return an array");
					assert.strictEqual(phrases.length, 1, "result array is expected to contain one item");
					assert.strictEqual(phrases[0].name, phrase_name, "test item has not correct name");
					assert.equal(phrases[0].weight, 0.3, "test item has not correct weight");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for addPhrases()');
		});

		it('updatePhrases()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.updatePhrases([{
				name: phrase_name,
				weight: 0.5
			}], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "updatePhrases() response");

				//check: phrase was updated
				session.getPhrases(config_id, function (phrases) {
					assert.ok(phrases instanceof Array, "getPhrases() is expected to return an array");
					assert.strictEqual(phrases.length, 1, "result array is expected to contain one item");
					assert.strictEqual(phrases[0].name, phrase_name, "test item has not correct name");
					assert.equal(phrases[0].weight, 0.5, "test item has not correct weight");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for updatePhrases()');
		});

		it('removePhrases()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.removePhrases([phrase_name], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202, "removePhrases() response");
				session.getPhrases(config_id, function (phrases) {
					assert.equal(phrases, 202, "getPhrases() is expected to return empty result");
					done();
				});
			});
			assert.ok(async_wait, 'synchronous call for removePhrases()');
		});
	});

	describe('Document functions', function () {
		it('getProcessedDocuments() - empty; ', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.getProcessedDocuments(config_id, function (items) {
				async_wait = false;
				assert.equal(items, 202);
				done();
			});
			assert.ok(async_wait, 'synchronous call for getProcessedDocuments()');
		});

		it('queueDocument()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.queueDocument({
				id: "TEST_DOCUMENT_1",
				text: "it works"
			}, config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202);
				done();
			});
			assert.ok(async_wait, 'synchronous call for queueDocument()');
		});

		it('cancelDocument()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			session.queueDocument({
				id: "TEST_DOCUMENT_2",
				text: "it works"
			}, config_id, function() {
				var async_wait = true;
				session.cancelDocument("TEST_DOCUMENT_2", config_id, function(result) {
					async_wait = false;
					assert.ok(result instanceof Object);
					done();
				});
				assert.ok(async_wait, 'synchronous call for cancelDocument()');
			});
		});

		it('queueBatchOfDocuments()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.queueBatchOfDocuments([
				{
					id: "TEST_DOCUMENT_3",
					text: "Some text goes here"
				},
				{
					id: "TEST_DOCUMENT_4",
					text: "Some other text goes here"
				}
			], config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202);
				done();
			});
			assert.ok(async_wait, 'synchronous call for queueBatchOfDocuments()');
		});

		it('getDocument()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			setTimeout(function() {
				var async_wait = true;
				session.getDocument("TEST_DOCUMENT_1", config_id, function (result) {
					async_wait = false;
					assert.ok(result instanceof Object);
					done();
				});
				assert.ok(async_wait, 'synchronous call for getDocument()');
			}, 2000); //wait for document-processing
		});

		it('getProcessedDocuments() - after processing test items', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var trys = 4;
			var processed = {};
			var testFn = function() {
				session.getProcessedDocuments(config_id, function(items) {
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
				});
			}
			setTimeout(testFn, 1000);
		});
	});

	describe('Collection functions', function () {
		it('getProcessedCollections() - empty', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.getProcessedCollections(config_id, function (items) {
				async_wait = false;
				assert.equal(items, 202);
				done()
			});
			assert.ok(async_wait, 'synchronous call for getProcessedCollections()');
		});

		it('queueCollection()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var async_wait = true;
			session.queueCollection({
				id: "TEST_COLLECTION_1",
				documents: [
					"it works",
					"it works",
					"it works",
					"it works",
					"it works"
				]
			}, config_id, function (result) {
				async_wait = false;
				assert.equal(result, 202);
				done();
			});
			assert.ok(async_wait, 'synchronous call for queueCollection()');
		});

		it('cancelCollection()', function ( done ) {
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
			}, config_id, function () {
				var async_wait = true;
				 session.cancelCollection("TEST_COLLECTION_2", config_id, function (result) {
					 async_wait = false;
					 assert.ok(result instanceof Object);
					 done();
				});
				assert.ok(async_wait, 'synchronous call for cancelCollection()');
			});
		});

		it('getCollection()', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			setTimeout(function() {
				var async_wait = true;
				session.getCollection("TEST_COLLECTION_1", config_id, function (result) {
					async_wait = false;
					assert.ok(result instanceof Object);
					done();
				});
				assert.ok(async_wait, 'synchronous call for getCollection()');
			}, 2000); //wait for document-processing
		});

		it('getProcessedCollections() - after processing test items', function ( done ) {
			if (!config_id) throw 'Test configuration not exists';
			var trys = 4;
			var testFn = function() {
				session.getProcessedCollections(config_id, function (items) {
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
				});
			}
			setTimeout(testFn, 1000);
		});
	})
});
