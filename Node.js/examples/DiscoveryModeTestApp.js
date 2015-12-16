var SemantriaSession = require("../").Session;
var promise = require('promise');
var config = require('../test-config');
try { config = require('../test-config.override') } catch(e) {}

var consumerKey = config.consumerKey,
	consumerSecret = config.consumerSecret,
	appConfigurationId = false,
	appConfigurationName = "DiscoveryModeTestApp Configuration",
	collectionId = false,
	SemantriaActiveSession = new SemantriaSession(consumerKey, consumerSecret, "myApp");

if (config.apiHost) {
	SemantriaActiveSession.API_HOST = config.apiHost
}

console.log("Semantria Discovery mode demo.");

//get or create test app configuration
SemantriaActiveSession.getConfigurations(true)
.then(function(configurations) {
	for (var i=0; i<configurations.length; i++) {
		if (configurations[i].name == appConfigurationName) {
			return promise.resolve([configurations[i]]);
		}
	}
	return SemantriaActiveSession.addConfigurations([{
		name: appConfigurationName,
		is_primary: false,
		auto_response: false,
		language: "English"
	}], true);
})
.then(function(result){
	appConfigurationId = result[0].id;

	// Creates a sample collection which need to be processed on Semantria
	collectionId = '' + Math.floor(Math.random() * 10000000);

	// Queues collection for processing on Semantria service
	return SemantriaActiveSession.queueCollection({
		id: collectionId,
		documents: getCollectionDocuments()
	}, appConfigurationId, true);
})
.then(function() {
	console.log("Collection #" + collectionId + " queued successfully.");

	//Wait while Semantria process queued collection
	return new promise(function(resolve, reject) {
		var wait_fn = function () {
			console.log("Retrieving your processed results...");
			SemantriaActiveSession.getCollection(collectionId, appConfigurationId, true)
			.then(function(processedCollection) {
				if (processedCollection && processedCollection.status == 'PROCESSED') {
					return resolve(processedCollection)
				}
				setTimeout(wait_fn, 5000);
			});
		}
		setTimeout(wait_fn, 5000);
	});
})
.then(function(analyticData) {
	// Printing collection's facets
	console.log("\nCollection facets:");
	if (analyticData.facets) {
		for(var i=0, facet; facet=analyticData.facets[i]; i++) {
			console.log("  %s : %s", facet.label, facet.count);

			if (facet.attributes) {
				for(var j=0, attr; attr=facet.attributes[j]; j++) {
					console.log("    %s : %s", attr.label, attr.count);
				}
			}
		}
	} else {
		console.log("  No facets were extracted for this text");
	}
})
.catch(function(err) {
	console.log("DetailedModeTestApp faild\n");
	console.log(err);
	console.log(err.stack);
})
.then(function() {
	if (!appConfigurationId) return;
	return SemantriaActiveSession.removeConfigurations([appConfigurationId], true);
});

function getCollectionDocuments() {
	var initialTexts = require('./source.json');
	return initialTexts;
}
