var SemantriaSession = require("../").Session;
var promise = require('promise');
var config = require('../test-config');
try { config = require('../test-config.override') } catch(e) {}

var consumerKey = config.consumerKey,
	consumerSecret = config.consumerSecret,
	appConfigurationId = false,
	appConfigurationName = "DetailedModeTestApp Configuration",
	docsTracker = {},
	SemantriaActiveSession = new SemantriaSession(consumerKey, consumerSecret, "myApp");

if (config.apiHost) {
	SemantriaActiveSession.API_HOST = config.apiHost
}

console.log("Semantria Document processing mode demo.");

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
	return SemantriaActiveSession.getSubscription(true);
})
.then(function(subscription){
	var outgoingBatches = getOutgoingBatches(subscription.basic_settings.batch_limit);
	var requests = [];
	for (var i=0; i<outgoingBatches.length; i++) {
		(function(outgoingBatch){
			var rq = SemantriaActiveSession.queueBatchOfDocuments(outgoingBatches[i], appConfigurationId, true)
				.then(function(res){
					console.log("%d documents queued successfully", outgoingBatch.length);
				});
			requests.push(rq);
		})(outgoingBatches[i]);
	}
	return promise.all(requests);
})
.then(function(){
	//wait processing of all documents
	return new promise(function(resolve, reject) {
		var analyticData = [];
		var wait_fn = function () {
			console.log("Retrieving your processed results...");
			SemantriaActiveSession.getProcessedDocuments(appConfigurationId, true)
			.then(function(processedDocuments) {

				if (processedDocuments && processedDocuments.length) {
					for (var i=0, res; res=processedDocuments[i]; i++) {
						if (res.id in docsTracker) {
							docsTracker[res.id] = true;
							analyticData.push(res);
						}
					}
				}

				var flag = true;
				for (var item in docsTracker) {
					if (!docsTracker[item]) {
						flag = false;
						break;
					}
				}

				if (flag) {
					return resolve(analyticData);
				}

				setTimeout(wait_fn, 2000);
			});
		}
		setTimeout(wait_fn, 2000);
	});
})
.then(function(analyticData) {
	for(var i=0, data;data=analyticData[i];i++) {
		// Printing of document sentiment score
		console.log("Document %s. Sentiment score: %s", data.id, data.sentiment_score);

		// Printing of document themes
		console.log("  Document themes:");
		if (data.themes) {
			for(var j=0, theme; theme=data.themes[j]; j++) {
				console.log("    %s (sentiment: %s)", theme.title, theme.sentiment_score);
			}
		} else {
			console.log("    No themes were extracted for this text");
		}

		// Printing of document entities
		console.log("  Entities:");
		if (data['entities']) {
			for(var j=0, entity; entity=data['entities'][j]; j++) {
				console.log("    %s (sentiment: %s)", entity.entity_type, entity.sentiment_score);
			}
		} else {
			console.log("    No entities were extracted for this text");
		}

		// Printing of document entities
		console.log("  Topics:");
		if (data.topics) {
			for(var j=0, topic; topic=data.topics[j]; j++) {
				console.log("    %s : %s (strength: %s)", topic.title, topic.type, topic.sentiment_score);
			}
		} else {
			console.log("    No topics were extracted for this text");
		}
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

function getOutgoingBatches(batch_limit) {
	var initialTexts = require('./source.json');
	var batch = [], batches = [];
	for(var i=0, item; item=initialTexts[i]; i++) {
		// Creates a sample document which need to be processed on Semantria
		var id = Math.floor(Math.random() * 10000000);
		batch.push({
			id: '' + id,
			text: item
		});
		docsTracker[id] = false;
		if (batch.length == batch_limit) {
			batches.push(batch);
			batch = [];
		}
	}

	if (batch.length > 0) {
		batches.push(batch);
	}

	return batches;
}

