var SemantriaSession = require("../").Session;
var promise = require('promise');
var config = require('../test-config');
var fs = require('fs');
var path = require('path');

try { config = require('../test-config.override') } catch(e) {}

var new_config = Object.assign({}, config);
new_config.consumerKey = config.consumerKey || process.env.SEMANTRIA_KEY;
new_config.consumerSecret = config.consumerSecret || process.env.SEMANTRIA_SECRET;
var appConfigurationId = false,
	appConfigurationName = "DiscoveryModeTestApp Configuration",
	collectionId = false,
	SemantriaActiveSession = new SemantriaSession(new_config, "DiscoveryTest");

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
	}]);
})
.then(function(result){
	appConfigurationId = result[0].id;

	// Creates a sample collection which need to be processed on Semantria
	collectionId = '' + Math.floor(Math.random() * 10000000);

	// Queues collection for processing on Semantria service
	return SemantriaActiveSession.queueCollection({
		id: collectionId,
		documents: getTestDocuments()
	}, appConfigurationId);
})
.then(function() {
	console.log("Collection #" + collectionId + " queued successfully.");

	//Wait while Semantria process queued collection
	return new promise(function(resolve, reject) {
		var wait_fn = function () {
			console.log("Retrieving your processed results...");
			SemantriaActiveSession.getCollection(collectionId, appConfigurationId)
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
	console.log("DetailedModeTestApp failed\n");
	console.log(err);
	console.log(err.stack);
})
.then(function() {
	if (!appConfigurationId) return;
	return SemantriaActiveSession.removeConfigurations([appConfigurationId]);
});

function getTestDocuments() {
    var filename = path.resolve(__dirname, 'bellagio-data-100.utf8');
    var lines = fs.readFileSync(filename, 'utf8').toString().split("\n");
    // Filter out any empty or very short docs
    lines = lines.filter(function(item) { return item.length > 3; });
    return lines;
}
