//var SemantriaSession = require("semantria-sdk").Session;
var SemantriaSession = require("../").Session;
var promise = require('promise');
var config = require('../test-config');
try { config = require('../test-config.override') } catch(e) {}

var consumerKey = config.consumerKey,
	consumerSecret = config.consumerSecret,
	docResultList = [],
	totalReceivedDocs = 0,
	queuedDocsCount = 0,
	outgoingDocs = getOutgoingDocs(),
//	oldPrimaryConfig = null,
//	autoResponseTestConfig = null,
	appConfigurationId = false,
	appConfigurationName = "AutoResponseFeatureModeTestApp Configuration",
	SemantriaActiveSession = new SemantriaSession(consumerKey, consumerSecret, "myApp");

if (config.apiHost) {
	SemantriaActiveSession.API_HOST = config.apiHost
}

SemantriaActiveSession.onError = function (err) {
	console.log("Error: " + err.status + " " + err.message);
}

SemantriaActiveSession.onAfterResponse = processDocuments;

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
		auto_response: true,
		language: "English"
	}]);
})
.then(function(result){
	appConfigurationId = result[0].id;

	// Queues documents for analysis one by one
	console.log("Queues documents for analysis one by one.\n");
	return runForEachCrossDelay(outgoingDocs, 250, function(doc) {
		var res = SemantriaActiveSession.queueDocument(doc, appConfigurationId);
		queuedDocsCount++;
		console.log("Documents queued/received: " + queuedDocsCount + "/" + docResultList.length);
		return res;
	});
})
.then(function() {
	//wait processing of all documents
	if (docResultList.length >= outgoingDocs.length) {
		return promise.resolve(true); //all documents already processed
	}
	return new promise(function(resolve, reject) {
		var wait_fn = function () {
			SemantriaActiveSession.getProcessedDocuments(appConfigurationId)
			.then(function(result) {
				processDocuments(result);
				console.log("Polling: Documents queued/received: " + queuedDocsCount + "/" + docResultList.length);
				if (docResultList.length >= outgoingDocs.length) {
					return resolve(true); //all documents already processed
				}
				setTimeout(wait_fn, 2000);
			});
		}
		setTimeout(wait_fn, 2000);
	});
})
.then(function() {
	console.log("\nDone: Documents queued/received: " + queuedDocsCount + "/" + docResultList.length + "\n");
})
.catch(function(err) {
	console.log("nAutoResponseFeatureModeTestApp faild\n");
	console.log(err);
	console.log(err.stack);
})
.then(function() {
	if (!appConfigurationId) return;
	return SemantriaActiveSession.removeConfigurations([appConfigurationId]);
});


function runForEachCrossDelay(items, delay, fn) {
	return new promise(function (resolve, reject) {
		var interval,
			is_completed = false,
			calls = [],
			i = 0;

		var complete = function() {
			if (is_completed) return;
			is_completed = true;
			clearInterval(interval);
			promise.all(calls).then(resolve, reject);
		}

		interval = setInterval(function () {
			if (i >= items.length) {
				return complete();
			}
			calls.push(fn(items[i]).catch(
				function (err) {
					complete();
					return promise.reject(err);
				}
			)); //complete on first error
			i++;
		}, delay);
	});
}

function getOutgoingDocs() {
	var outgoingDocs = [];
	var initialTexts = require('./source.json');
	for(var i=0, item; item=initialTexts[i]; i++) {
		// Creates a sample document which need to be processed on Semantria
		var id = Math.floor(Math.random() * 10000000);
		outgoingDocs.push({
			id: '' + id,
			text: item
		});
	}

	return outgoingDocs;
}

function processDocuments(result) {
	if (result && result.length) {
		totalReceivedDocs += result.length;
		docResultList = docResultList.concat(result);
	}
}

