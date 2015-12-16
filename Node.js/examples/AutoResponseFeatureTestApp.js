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
	}], true);
})
.then(function(result){
	appConfigurationId = result[0].id;

	// Queues documents for analysis one by one
	console.log("Queues documents for analysis one by one.\n");
	return runForEachCrossDealay(outgoingDocs, 2000, function(doc) {
		var res = SemantriaActiveSession.queueDocument(doc, appConfigurationId, true);
		queuedDocsCount++;
		console.log("Documents queued/received rate: " + queuedDocsCount + "/" + docResultList.length);
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
			SemantriaActiveSession.getProcessedDocuments(appConfigurationId, true)
			.then(function(result) {
				processDocuments(result);
				console.log("Documents queued/received rate: " + queuedDocsCount + "/" + docResultList.length);
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
	console.log("\nAutoResponseFeatureModeTestApp successful complete\n");
})
.catch(function(err) {
	console.log("nAutoResponseFeatureModeTestApp faild\n");
	console.log(err);
	console.log(err.stack);
})
.then(function() {
	if (!appConfigurationId) return;
	return SemantriaActiveSession.removeConfigurations([appConfigurationId], true);
});


function runForEachCrossDealay(items, dalay, fn) {
	return new promise(function (resolve, reject) {
		var interval,
			is_complited = false,
			calls = [],
			i = 0;

		var complite = function() {
			if (is_complited) return;
			is_complited = true;
			clearInterval(interval);
			promise.all(calls).then(resolve, reject);
		}

		interval = setInterval(function () {
			if (i >= items.length) {
				return complite();
			}
			calls.push(fn(items[i]).catch(
				function (err) {
					complite();
					return promise.reject(err);
				}
			)); //complite on first error
			i++;
		}, dalay);
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

