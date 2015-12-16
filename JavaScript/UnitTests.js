(function(){
	var consumerKey = "",
		consumerSecret = "",
		session = new Semantria.Session(consumerKey, consumerSecret, "myApp"),
		config_id = false;

	session.API_HOST = "https://api.semantria.com";

	QUnit.module( "SemantriaJavaScriptSDK Tests", {
		beforeEach: function() {
			var result = session.addConfigurations([{
				name: "JAVASCRIPT_TEST_CONFIG",
				is_primary: false,
				auto_response: false,
				language: "English"
			}]);
			if(result instanceof Array && result[0] instanceof Object) {
				config_id = result[0].id;
			}
		},
		afterEach: function() {
			session.removeConfigurations([config_id]);
			config_id = false;
		}
	});

	QUnit.test( "getStatistics", function( assert ) {
		var statistics = session.getStatistics()
		assert.ok( statistics instanceof Object  && typeof statistics.name != "undefined" );
	});

	QUnit.test( "getSubscription", function( assert ) {
		var subscription = session.getSubscription();
		assert.ok( subscription instanceof Object  && typeof subscription.name != "undefined" );
	});

	QUnit.test( "getStatus", function( assert ) {
		var status = session.getStatus();
		assert.ok( status instanceof Object && typeof status.api_version != "undefined" );
	});

	QUnit.test( "getSupportedFeatures", function( assert ) {
		var features = session.getSupportedFeatures("English");
		assert.ok( features instanceof Array && features[0].language == "English" );
	});

	QUnit.test( "[get/add/update/remove] Configurations ", function( assert ) {
		//getConfigurations
		var configurations = session.getConfigurations();
		assert.ok( configurations instanceof Array, "getConfigurations()");

		//addConfigurations
		var configuration = null,
			configuration_id = false,
			configuration_name = "Test configuration",
			configuration2 = null,
			configuration2_id = false,
			configuration2_name = "Test configuration - clone";

		var result = session.addConfigurations([{
			name: configuration_name,
			is_primary: false,
			auto_response: false,
			language: "English"
		}]);
		assert.ok(
			result instanceof Array && result[0] instanceof Object && typeof result[0].id === 'string',
			"addConfigurations()"
		);
		configuration = result[0];
		configuration_id = configuration.id

		//check: configuration exists
		configurations = session.getConfigurations();
		var find_configuration = false;
		for (var i=0; i<configurations.length; i++) {
			if (configurations[i].id == configuration_id) {
				find_configuration = configurations[i];
			}
		}
		assert.ok( find_configuration instanceof Object, "test configuration exists" );
		assert.strictEqual(find_configuration.name, configuration_name, "test configuration have correct name");

		//updateConfigurations
		delete configuration.timestamp;
		configuration_name = "Test configuration renamed";
		configuration.name = configuration_name;
		var result = session.updateConfigurations([configuration]);
		assert.ok( result instanceof Array && result[0] instanceof Object, "updateConfigurations()" );

		//check: configuration was updated
		configurations = session.getConfigurations();
		find_configuration = false;
		for (var i=0; i<configurations.length; i++) {
			if (configurations[i].id == configuration_id) {
				find_configuration = configurations[i];
			}
		}
		assert.ok( find_configuration instanceof Object, "test configuration exists" );
		assert.strictEqual(find_configuration.name, configuration_name, "test configuration have correct name");

		//cloneConfiguration
		var result = session.cloneConfiguration(configuration2_name, configuration_id);
		assert.ok(
			result instanceof Array && result[0] instanceof Object && typeof result[0].id === 'string',
			'cloneConfiguration() response'
		);
		configuration2 = result[0];
		delete configuration2.timestamp;
		configuration2_id = configuration2.id

		//check: configuration was updated
		var configurations = session.getConfigurations();
		var find_configuration = false;
		for (var i=0; i<configurations.length; i++) {
			if (configurations[i].id == configuration2_id) {
				find_configuration = configurations[i];
			}
		}
		assert.ok( find_configuration instanceof Object, "test configuration not exists" );
		assert.strictEqual(find_configuration.name, configuration2_name, "test configuration has not correct name");

		//removeConfigurations
		var ids = [configuration.id];
		if (configuration2_id) ids.push(configuration2_id);
		var result = session.removeConfigurations(ids);
		assert.equal(result, 202, "removeConfigurations()");

		//check: configuration was removed
		configurations = session.getConfigurations();
		find_configuration = false;
		for (var i=0; i<configurations.length; i++) {
			if (configurations[i].id == configuration_id) {
				find_configuration = configurations[i];
			}
		}
		assert.strictEqual(find_configuration, false, "test configuration was removed");
	});

	QUnit.test( "[get/add/update/remove] Blacklist", function( assert ) {
		assert.notEqual(config_id, false, "Test configuration exists");
		var blacklists = session.getBlacklist(config_id);
		assert.equal(blacklists, 202, "getBlacklist() - empty");

		//addBlacklist
		var blacklist = null,
			blacklist_id = false,
			blacklist_name = "test blacklist 1";
		var result = session.addBlacklist([{
			name:"test blacklist 1"
		}], config_id);
		assert.ok(
			result instanceof Array && result[0] instanceof Object && typeof result[0].id === 'string',
			"addBlacklist()"
		);
		blacklist = result[0];
		blacklist_id = blacklist.id

		//check: blacklist exists
		blacklists = session.getBlacklist(config_id);
		assert.ok(blacklists instanceof Array, "getBlacklist() - return array");
		assert.strictEqual(blacklists.length, 1, "  array has one test item");
		assert.strictEqual(blacklists[0].id, blacklist_id, "  test item has correct id");
		assert.strictEqual(blacklists[0].name, blacklist_name, "  test item has correct name");

		//updateBlacklist
		delete blacklist.timestamp;
		blacklist_name = "test blacklist 2";
		blacklist.name = blacklist_name;
		result = session.updateBlacklist([blacklist], config_id);
		assert.ok( result instanceof Array && result[0] instanceof Object, "updateBlacklist()" );

		//check: blacklist was updated
		blacklists = session.getBlacklist(config_id);
		assert.ok(blacklists instanceof Array, "getBlacklist() - return array");
		assert.strictEqual(blacklists.length, 1, "  array has one test item");
		assert.strictEqual(blacklists[0].id, blacklist_id, "  test item has correct id");
		assert.strictEqual(blacklists[0].name, blacklist_name, "  test item has correct name");

		//removeBlacklist
		var result = session.removeBlacklist([blacklist.id], config_id);
		assert.equal(result, 202, "removeBlacklist()");
		blacklists = session.getBlacklist(config_id);
		assert.equal(blacklists, 202, "getBlacklist() - empty; test item was removed");
	});

	QUnit.test( "[get/add/update/remove] Categories", function( assert ) {
		assert.notEqual(config_id, false, "Test configuration exists");
		var categories = session.getCategories(config_id);
		assert.equal(categories, 202, "getCategories() - empty");

		//addCategories
		var categoriy = null,
			categoriy_id = false,
			categoriy_name = "Test Category";
		var result = session.addCategories([{
			name: categoriy_name,
			weight: 0.75,
			samples: ["EC2", "AWS"]
		}], config_id);
		assert.ok(
			result instanceof Array && result[0] instanceof Object && typeof result[0].id === 'string',
			"addCategories()"
		);
		categoriy = result[0];
		categoriy_id = categoriy.id

		//check: categoriy exists
		categories = session.getCategories(config_id);
		assert.ok(categories instanceof Array, "getCategories() - return array");
		assert.strictEqual(categories.length, 1, "  array has one test item");
		assert.strictEqual(categories[0].id, categoriy_id, "  test item has correct id");
		assert.strictEqual(categories[0].name, categoriy_name, "  test item has correct name");

		//updateCategories
		delete categoriy.timestamp;
		categoriy_name = "Test Category - renamed";
		categoriy.name = categoriy_name;
		result = session.updateCategories([categoriy], config_id);
		assert.ok( result instanceof Array && result[0] instanceof Object, "updateCategories()" );

		//check: categoriy exists
		categories = session.getCategories(config_id);
		assert.ok(categories instanceof Array, "getCategories() - return array");
		assert.strictEqual(categories.length, 1, "  array has one test item");
		assert.strictEqual(categories[0].id, categoriy_id, "  test item has correct id");
		assert.strictEqual(categories[0].name, categoriy_name, "  test item has correct name");

		//removeCategories
		var result = session.removeCategories([categoriy.id], config_id);
		assert.equal(result, 202, "removeCategories()");
		categories = session.getCategories(config_id);
		assert.equal(categories, 202, "getCategories() - empty; test item was removed");
	});

	QUnit.test( "[get/add/update/remove] Queries", function( assert ) {
		assert.notEqual(config_id, false, "Test configuration exists");
		var queries = session.getQueries(config_id);
		assert.equal(queries, 202, "getQueries() - empty");

		//addQueries
		var query = null,
			query_id = false,
			query_name = "Test Query",
			query_query = "Amazon AND (EC2 OR AWS)";
		var result = session.addQueries([{
			name: query_name,
			query: query_query
		}], config_id);
		assert.ok(
			result instanceof Array && result[0] instanceof Object && typeof result[0].id === 'string',
			"addQueries()"
		);
		query = result[0];
		query_id = query.id

		//check: query exists
		queries = session.getQueries(config_id);
		assert.ok(queries instanceof Array, "getQueries() - return array");
		assert.strictEqual(queries.length, 1, "  array has one test item");
		assert.strictEqual(queries[0].id, query_id, "  test item has correct id");
		assert.strictEqual(queries[0].name, query_name, "  test item has correct name");
		assert.strictEqual(queries[0].query, query_query, "  test item has correct query");

		//updateQueries
		delete query.timestamp;
		query_query = "Amazon AND (EC2 OR ec2 OR AWS OR aws)";
		query.query = query_query;
		result = session.updateQueries([query], config_id);
		assert.ok( result instanceof Array && result[0] instanceof Object, "updateQueries()" );

		//check: query exists
		queries = session.getQueries(config_id);
		assert.ok(queries instanceof Array, "getQueries() - return array");
		assert.strictEqual(queries.length, 1, "  array has one test item");
		assert.strictEqual(queries[0].id, query_id, "  test item has correct id");
		assert.strictEqual(queries[0].name, query_name, "  test item has correct name");
		assert.strictEqual(queries[0].query, query_query, "  test item has correct query");

		//removeQueries
		var result = session.removeQueries([query.id], config_id);
		assert.equal(result, 202, "removeQueries()");
		queries = session.getQueries(config_id);
		assert.equal(queries, 202, "getQueries() - empty; test item was removed");
	});

	QUnit.test( "[get/add/update/remove] Entities", function( assert ) {
		assert.notEqual(config_id, false, "Test configuration exists");
		var entities = session.getEntities(config_id);
		assert.equal(entities, 202, "getEntities() - empty");

		//addEntities
		var entity = null,
			entity_id = false,
			entity_name = "Test Enery";
		var result = session.addEntities([{
			name: entity_name,
			type: "furniture"
		}], config_id);
		assert.ok(
			result instanceof Array && result[0] instanceof Object && typeof result[0].id === 'string',
			"addEntities()"
		);
		entity = result[0];
		entity_id = entity.id

		//check: entity exists
		entities = session.getEntities(config_id);
		assert.ok(entities instanceof Array, "getEntities() - return array");
		assert.strictEqual(entities.length, 1, "  array has one test item");
		assert.strictEqual(entities[0].id, entity_id, "  test item has correct id");
		assert.strictEqual(entities[0].name, entity_name, "  test item has correct name");

		//updateEntities
		delete entity.timestamp;
		entity_name = "Test Category - renamed";
		entity.name = entity_name;
		result = session.updateEntities([entity], config_id);
		assert.ok( result instanceof Array && result[0] instanceof Object, "updateEntities()" );

		//check: entity exists
		entities = session.getEntities(config_id);
		assert.ok(entities instanceof Array, "getEntities() - return array");
		assert.strictEqual(entities.length, 1, "  array has one test item");
		assert.strictEqual(entities[0].id, entity_id, "  test item has correct id");
		assert.strictEqual(entities[0].name, entity_name, "  test item has correct name");

		//removeEntities
		var result = session.removeEntities([entity.id], config_id);
		assert.equal(result, 202, "removeEntities()");
		entities = session.getEntities(config_id);
		assert.equal(entities, 202, "getEntities() - empty; test item was removed");
	});

	QUnit.test( "[get/add/update/remove] Phrases", function( assert ) {
		assert.notEqual(config_id, false, "Test configuration exists");
		var phrases = session.getPhrases(config_id);
		assert.equal(phrases, 202, "getPhrases() - empty");

		//addPhrases
		var phrase = null,
			phrase_id = false,
			phrase_name = "Test Phrase";
		var result = session.addPhrases([{
			name: phrase_name,
			weight: "0.3"
		}], config_id);
		assert.ok(
			result instanceof Array && result[0] instanceof Object && typeof result[0].id === 'string',
			"addPhrases()"
		);
		phrase = result[0];
		phrase_id = phrase.id

		//check: phrase exists
		phrases = session.getPhrases(config_id);
		assert.ok(phrases instanceof Array, "getPhrases() - return array");
		assert.strictEqual(phrases.length, 1, "  array has one test item");
		assert.strictEqual(phrases[0].id, phrase_id, "  test item has correct id");
		assert.strictEqual(phrases[0].name, phrase_name, "  test item has correct name");

		//updatePhrases
		delete phrase.timestamp;
		phrase_name = "Test Phrase - renamed";
		phrase.name = phrase_name;
		result = session.updatePhrases([phrase], config_id);
		assert.ok( result instanceof Array && result[0] instanceof Object, "updatePhrases()" );

		//check: phrase exists
		phrases = session.getPhrases(config_id);
		assert.ok(phrases instanceof Array, "getPhrases() - return array");
		assert.strictEqual(phrases.length, 1, "  array has one test item");
		assert.strictEqual(phrases[0].id, phrase_id, "  test item has correct id");
		assert.strictEqual(phrases[0].name, phrase_name, "  test item has correct name");

		//removePhrases
		var result = session.removePhrases([phrase.id], config_id);
		assert.equal(result, 202, "removePhrases()");
		phrases = session.getPhrases(config_id);
		assert.equal(phrases, 202, "getPhrases() - empty; test item was removed");
	});
	QUnit.test( "[get/add/update/remove] Taxonomy", function( assert ) {
		assert.notEqual(config_id, false, "Test configuration exists");
		var taxonomies = session.getTaxonomy(config_id);
		assert.equal(taxonomies, 202, "getTaxonomy() - empty");

		//addTaxonomy
		var taxonomy = null,
			taxonomy_id = false,
			taxonomy_name = "Test Taxonomy";
		var result = session.addTaxonomy([{
			name: taxonomy_name
		}], config_id);
		assert.ok(
			result instanceof Array && result[0] instanceof Object && typeof result[0].id === 'string',
			"addTaxonomy()"
		);
		taxonomy = result[0];

		taxonomy_id = taxonomy.id

		//check: taxonomy exists
		taxonomies = session.getTaxonomy(config_id);
		assert.ok(taxonomies instanceof Array, "getTaxonomy() - return array");
		assert.strictEqual(taxonomies.length, 1, "  array has one test item");
		assert.strictEqual(taxonomies[0].id, taxonomy_id, "  test item has correct id");
		assert.strictEqual(taxonomies[0].name, taxonomy_name, "  test item has correct name");

		//updateTaxonomy
		delete taxonomy.timestamp;
		taxonomy_name = "Test Taxonomy - renamed";
		taxonomy.name = taxonomy_name;
		result = session.updateTaxonomy([taxonomy], config_id);
		assert.ok( result instanceof Array && result[0] instanceof Object, "updateTaxonomy()" );

		//check: taxonomy exists
		taxonomies = session.getTaxonomy(config_id);
		assert.ok(taxonomies instanceof Array, "getTaxonomy() - return array");
		assert.strictEqual(taxonomies.length, 1, "  array has one test item");
		assert.strictEqual(taxonomies[0].id, taxonomy_id, "  test item has correct id");
		assert.strictEqual(taxonomies[0].name, taxonomy_name, "  test item has correct name");

		//removeTaxonomy
		var result = session.removeTaxonomy([taxonomy.id], config_id);
		assert.equal(result, 202, "removeTaxonomy()");
		taxonomies = session.getTaxonomy(config_id);
		assert.equal(taxonomies, 202, "getTaxonomy() - empty; test item was removed");
	});

	QUnit.test( "Document functions (getProcessedDocuments, queueDocument, cancelDocument, queueBatchOfDocuments, getDocument)", function( assert ) {
		//getProcessedDocuments
		assert.notEqual(config_id, false, "Test configuration exists");
		var items = session.getProcessedDocuments(config_id);
		assert.ok(items == 202 || items instanceof Array, "getProcessedDocuments()");

		//queueDocument
		var result = session.queueDocument({
			id: "TEST_DOCUMENT_1",
			text: "it works"
		}, config_id);
		assert.equal(result, 202, "queueDocument()");

		//cancelDocument
		session.queueDocument({
			id: "TEST_DOCUMENT_2",
			text: "it works"
		}, config_id);
		result = session.cancelDocument("TEST_DOCUMENT_2", config_id);
		assert.ok(result instanceof Object, "cancelDocument()");

		//getDocument
		var getDocumentDone = assert.async();
		setTimeout(function() {
			var result = session.getDocument("TEST_DOCUMENT_1", config_id);
			assert.ok(result instanceof Object, "getDocument()");
			getDocumentDone();
		}, 2000); //wait for document-processing

		////queueBatchOfDocuments
		result = session.queueBatchOfDocuments([{
			id: "TEST_DOCUMENT_3",
			text: "Some text goes here"
		}], config_id);
		assert.equal(result, 202, "queueBatchOfDocuments()");
	});

	QUnit.test( "Collection functions (getProcessedCollections, queueCollection, cancelCollection, getCollection)", function( assert ) {
		//getProcessedCollections
		assert.notEqual(config_id, false, "Test configuration exists");
		var items = session.getProcessedCollections(config_id);
		assert.equal(items, 202, "getProcessedCollections() - empty");

		//queueCollection
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
		assert.equal(result, 202, "queueCollection()");

		//cancelDocument
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
		result = session.cancelCollection("TEST_COLLECTION_2", config_id);
		assert.ok(result instanceof Object, "cancelCollection()");


		var getCollectionDone = assert.async();
		//getCollection
		setTimeout(function() {
			var result = session.getCollection("TEST_COLLECTION_1", config_id);
			console.log(result);
			assert.ok(result instanceof Object, "getCollection()");
			getCollectionDone();
		}, 2000); //wait for document-processing

	});
})();
