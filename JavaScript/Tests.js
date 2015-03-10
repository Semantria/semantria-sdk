;(function() {
	var success = function(method) {
		console.log("%cpassed " + method, "color: green; font-weight: bold;");
	}

	var error = function(method, expected, result) {
		console.log(
			"%cfailure " + method + "\n" + 
			expected + " expected, but server returned\n" + 
			JSON.stringify(result), 
			"color: red; font-weight: bold;");
	}

	// the consumer key and secret
	var consumerKey = "",
		consumerSecret = "";

	var session = new Semantria.Session(consumerKey, consumerSecret, "myApp");

	/**
	 * @test getStatistics
	 */
	(function() {
		var statistics = session.getStatistics();

		if(statistics instanceof Object && 
			typeof statistics.name != "undefined") {
			success("getStatistics()");
		} else {
			error("getStatistics()", "Object", statistics);
		}
	})();

	/**
	 * @test getSubscription
	 */
	(function() {
		var subscription = session.getSubscription();

		if(subscription instanceof Object && 
			typeof subscription.name != "undefined") {
			success("getSubscription()");
		} else {
			error("getSubscription()", "Object", subscription);
		}
	})();

	/**
	 * @test getStatus
	 */
	(function() {
		var status = session.getStatus();
		
		if(status instanceof Object && 
			typeof status.api_version != "undefined") {
			success("getStatus()");
		} else {
			error("getStatus()", "Object", status);
		}
	})();
	
	/**
	 * @test getSupportedFeatures
	 */
	(function() {
		var features = session.getSupportedFeatures("English");
		
		if(features instanceof Array &&
			features[0].language == "English") {
			success("getSupportedFeatures()");
		} else {
			error("getSupportedFeatures()", "Object", features);
		}
	})();

	/** 
	 * /////////////
	 * BLACKLIST
	 * /////////////
	 */

	/**
	 * @test getBlacklist
	 */
	(function() {
		var blacklist = session.getBlacklist();

		if(blacklist == 202) {
			// black list is empty
			return success("getBlacklist()");
		}
		
		if(blacklist instanceof Object || blacklist instanceof Array) {
			success("getBlacklist()");	
		} else {
			error("getBlacklist()", "Object or Array", blacklist);
		}
	})();

	/**
	 * @test addBlacklist
	 */
	(function() {
		var result = session.addBlacklist([".net"]);

		if(result == 202) {
			return success("addBlacklist()");
		} else {
			error("getBlacklist()", 202, result);
		}
	})();

	/**
	 * @test removeBlacklist
	 */
	(function() {
		var result = session.removeBlacklist([".net"]);

		if(result == 202) {
			return success("removeBlacklist()");
		} else {
			error("removeBlacklist()", 202, result);
		}
	})();

	/** 
	 * ////////////////
	 * CONFIGURATIONS
	 * ////////////////
	 */

	/**
	 * @test getConfigurations
	 */
	(function() {
		var configurations = session.getConfigurations();

		if(configurations instanceof Array) {
			success("getConfigurations()");
		} else {
			error("getConfigurations()", "Array", configurations);
		}
	})();

	/**
	 * @test addConfigurations
	 */
	(function() {
		var result = session.addConfigurations([{
			name: "A test configuration",
			is_primary: false,
			auto_response: false,
			language: "English"
		}]);

		if(result == 202) {
			success("addConfigurations()");
		} else {
			error("addConfigurations()", 202, result);
		}
	})();

	/**
	 * @test updateConfigurations
	 */
	(function() {
		var configurations = session.getConfigurations();
		var requiredConfiguration;
		
		for(var i=0,item=configurations[0];item=configurations[i];i++) {
			if(item.name == "A test configuration") {
				requiredConfiguration = item;
				break;
			}
		}

		if(!requiredConfiguration) {
			// we cannot test this method, as adding configuration has been failed
			return ;
		}
		
		requiredConfiguration.one_sentence = true;

		var result = session.updateConfigurations([requiredConfiguration]);

		if(result == 202) {
			success("updateConfigurations()");
		} else {
			error("updateConfigurations()", 202, result);
		}
	})();

	/**
	 * @test removeConfigurations
	 */
	(function() {
		var configurations = session.getConfigurations();
		var requiredConfiguration;

		for(var i=0,item=configurations[0];item=configurations[i];i++) {
			if(item.name == "A test configuration") {
				requiredConfiguration = item;
				break;
			}
		}

		if(!requiredConfiguration) {
			// we cannot test this method, as adding configuration has been failed
			return ;
		}

		var result = session.removeConfigurations([
			requiredConfiguration.config_id
		]);

		if(result == 202) {
			success("removeConfigurations()");
		} else {
			error("removeConfigurations()", 202, result);
		}
	})();


	/** 
	 * /////////////
	 * CATEGORIES
	 * /////////////
	 */

	/**
	 * @test getCategories
	 */
	(function() {
		var categories = session.getCategories();

		if(categories instanceof Array) {
			return success("getCategories()");
		} else {
			error("getCategories()", "Array", categories);
		}
	})();

	/**
	 * @test addCategories
	 */
	(function() {
		var result = session.addCategories([{
			name: "Amazon EC2",
			weight: 0.75,
			samples: ["EC2", "AWS"]
		}]);
		
		if(result == 202) {
			return success("addCategories()");
		} else {
			error("addCategories()", 202, result);
		}
	})();

	/**
	 * @test removeCategories
	 */
	(function() {
		var result = session.removeCategories([{
			name: "Amazon EC2"
		}]);
		
		if(result == 202) {
			return success("removeCategories()");
		} else {
			error("removeCategories()", 202, result);
		}
	})();

	/**
	 * @test updateCategories
	 */
	(function() {
		var result = session.updateCategories([{
			name: "Amazon EC2",
			weight: 0.81
		}]);
		
		if(result == 202) {
			return success("updateCategories()");
		} else {
			error("updateCategories()", 202, result);
		}
	})();

	/** 
	 * /////////////
	 * QUERIES
	 * /////////////
	 */

	/**
	 * @test getQueries
	 */
	(function() {
		var queries = session.getQueries();
		
		if(queries instanceof Array || queries == 202) {
			return success("getQueries()");
		} else {
			error("getQueries()", "Array", queries);
		}
	})();

	/**
	 * @test addQueries
	 */
	(function() {
		var result = session.addQueries([{
			name: "Amazon EC2",
			query: "Amazon AND (EC2 OR AWS)"
		}]);
		
		if(result == 202) {
			return success("addQueries()");
		} else {
			error("addQueries()", 202, result);
		}
	})();

	/**
	 * @test updateQueries
	 */
	(function() {
		var result = session.updateQueries([{
			name: "Amazon EC2",
			query: "Amazon AND (EC2 OR ec2 OR AWS OR aws)"
		}]);
		
		if(result == 202) {
			return success("updateQueries()");
		} else {
			error("updateQueries()", 202, result);
		}
	})();

	/**
	 * @test removeQueries
	 */
	(function() {
		var result = session.removeQueries([{
			name: "Amazon EC2"
		}]);
		
		if(result == 202) {
			return success("removeQueries()");
		} else {
			error("removeQueries()", 202, result);
		}
	})();

	/** 
	 * /////////////
	 * ENTITIES
	 * /////////////
	 */

	/**
	 * @test getEntities
	 */
	(function() {
		var entities = session.getEntities();
		
		if(entities instanceof Array) {
			return success("getEntities()");
		} else {
			error("getEntities()", "Array", entities);
		}
	})();

	/**
	 * @test addEntities
	 */
	(function() {
		var result = session.addEntities([{
			name: "chair",
			type: "furniture"
		}]);
		
		if(result == 202) {
			return success("addEntities()");
		} else {
			error("addEntities()", 202, result);
		}
	})();

	/**
	 * @test updateEntities
	 */
	(function() {
		var result = session.updateEntities([{
			name: "chair",
			type: "furniture 2"
		}]);
		
		if(result == 202) {
			return success("updateEntities()");
		} else {
			error("updateEntities()", 202, result);
		}
	})();

	/**
	 * @test removeEntities
	 */
	(function() {
		var result = session.removeEntities(["chair"]);
		
		if(result == 202) {
			return success("removeEntities()");
		} else {
			error("removeEntities()", 202, result);
		}
	})();

	/** 
	 * /////////////
	 * PHRASES
	 * /////////////
	 */

	/**
	 * @test getPhrases
	 */
	(function() {
		var phrases = session.getPhrases();
		if(phrases instanceof Array) {
			return success("getPhrases()");
		} else {
			error("getPhrases()", "Array", phrases);
		}
	})();

	/**
	 * @test addPhrases
	 */
	(function() {
		var result = session.addPhrases([{
			name: "awesome",
			weight: "0.3"
		}]);
		
		if(result == 202) {
			return success("addPhrases()");
		} else {
			error("addPhrases()", 202, result);
		}
	})();

	/**
	 * @test updatePhrases
	 */
	(function() {
		var result = session.updatePhrases([{
			name: "awesome",
			weight: "0.4"
		}]);
		
		if(result == 202) {
			return success("updatePhrases()");
		} else {
			error("updatePhrases()", 202, result);
		}
	})();
	
	/**
	 * @test removePhrases
	 */
	(function() {
		var result = session.removePhrases(["awesome"]);
		
		if(result == 202) {
			return success("removePhrases()");
		} else {
			error("removePhrases()", 202, result);
		}
	})();

	/** 
	 * /////////////
	 * DOCUMENTS && COLLECTIONS
	 * /////////////
	 */

	/**
	 * @test getProcessedDocuments
	 */
	(function() {
		var items = session.getProcessedDocuments();
		
		if(items == 202 || items instanceof Array) {
			return success("getProcessedDocuments()");
		} else {
			error("getProcessedDocuments()", "Array", items);
		}
	})();

	/**
	 * @test getProcessedCollections
	 */
	(function() {
		var items = session.getProcessedCollections();
		
		if(items == 202 || items instanceof Array) {
			return success("getProcessedCollections()");
		} else {
			error("getProcessedCollections()", "Array", items);
		}
	})();

	var documentId = (new Date).getTime();

	/**
	 * @test queueDocument
	 */
	(function() {
		var result = session.queueDocument({
			id: documentId,
			text: "it works"
		});
		
		if(result == 202) {
			return success("queueDocument()");
		} else {
			error("queueDocument()", 202, result);
		}
	})();

	/**
	 * @test getDocument
	 */
	(function() {
		setTimeout(function() {
			var result = session.getDocument(documentId);
			
			if(result instanceof Object) {
				return success("getDocument()");
			} else {
				error("getDocument()", "Object", result);
			}
		}, 2000); //wait for document-processing
	})();

	/**
	 * @test cancelDocument
	 */
	(function() {
		var id = documentId + 1;

		var result = session.queueDocument({
			id: id,
			text: "it works"
		});
		
		var result = session.cancelDocument(id);
		
		if(result instanceof Object) {
			return success("cancelDocument()");
		} else {
			error("cancelDocument()", "Object", result);
		}
	})();

	var collectionId = (new Date).getTime();

	/**
	 * @test queueCollection
	 */
	(function() {
		var result = session.queueCollection({
			id: collectionId,
			documents: [
				"it works",
				"it works",
				"it works",
				"it works",
				"it works"
			]
		});
		
		if(result == 202) {
			return success("queueCollection()");
		} else {
			error("queueCollection()", 202, result);
		}
	})();

	/**
	 * @test getCollection
	 */
	(function() {
		setTimeout(function() {
			var result = session.getCollection(collectionId);
			
			if(result instanceof Object) {
				return success("getCollection()");
			} else {
				error("getCollection()", "Object", result);
			}
		}, 2000); //wait for document-processing
	})();

	/**
	 * @test cancelCollection
	 */
	(function() {
		var id = collectionId + 1;

		var result = session.queueCollection({
			id: id,
			documents: [
				"it works",
				"it works",
				"it works",
				"it works",
				"it works"
			]
		});
		
		var result = session.cancelCollection(id);
		
		if(result instanceof Object) {
			return success("cancelCollection()");
		} else {
			error("cancelCollection()", "Object", result);
		}
	})();

	/**
	 * @test queueBatchOfDocuments
	 */
	(function() {
		var id = (new Date).getTime();

		var result = session.queueBatchOfDocuments([{
			id: id,
			text: "Some text goes here"
		}]);
		
		if(result == 202) {
			return success("queueBatchOfDocuments()");
		} else {
			error("queueBatchOfDocuments()", 202, result);
		}
	})();

})();