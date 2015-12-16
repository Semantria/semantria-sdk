var SemantriaSession = require("../").Session;
var promise = require('promise');
var config = require('../test-config');
try { config = require('../test-config.override') } catch(e) {}

var consumerKey = config.consumerKey,
	consumerSecret = config.consumerSecret,
	/**
	 * @type {(boolean|null)} dataSendingMode
	 *   null - send every single document separately
	 *   false - send uniqueJobIdCount batches
	 *   true - send all documents in single batch
	 */
	dataSendingMode = false,
	appConfigurationId = null,
	appConfigurationName = "JobIdFeatureModeTestApp Configuration",
	jobIds = {},
	uniqueJobIdCount = 4,
	documents = {},
	SemantriaActiveSession = new SemantriaSession(consumerKey, consumerSecret, "myApp");

if (config.apiHost) {
	SemantriaActiveSession.API_HOST = config.apiHost
}

console.log("Semantria JobId feature demo.");

//Generates N unique jobId values
for (var index = 0; index < uniqueJobIdCount; index++) {
	var id = Math.floor(Math.random() * 10000000);

	jobIds[id] = 0;
	documents[id] = [];
}

var initialTexts = require('./source.json');
for (var i=0, item; item=initialTexts[i]; i++) {
	// Creates a sample document which need to be processed on Semantria
	var id = Math.floor(Math.random() * 10000000),
		jobId = randomProperty(jobIds);

	jobIds[jobId]++;
	documents[jobId].push({
		id: '' + id,
		text: item,
		job_id: jobId
	});
}

console.log("Queues document for processing on Semantria service...");
for (var jobId in jobIds) {
	console.log(jobIds[jobId] + " documents for " + jobId + " jobId");
}

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
.then(function (configuration) {
	appConfigurationId = configuration[0].id;

	//queued documents
	if (dataSendingMode === null) return queueDocumentSingle();
	if (dataSendingMode === false) return queueDocumentBatches();
	if (dataSendingMode === true) return queueDocumentSingleBatch();

	return new promise.reject("incorrect dataSendingMode");
})
.then(function(){
	//Wait while Semantria process queued document
	return new promise(function(resolve, reject) {
		var analyticData = [];
		var wait_fn = function () {
			console.log("Retrieving your processed results...");

			var jobId, jobs = [];
			for (jobId in jobIds) {
				jobs.push(SemantriaActiveSession.getProcessedDocumentsByJobId(jobId, true));
			}


			promise.all(jobs)
			.then(function(result) {

				var i = 0;
				for (jobId in jobIds) {
					var processedDocuments = result[i];
					if (processedDocuments && processedDocuments.length) {
						jobIds[jobId] -= processedDocuments.length;
						console.log("%d documents received for %s job ID", processedDocuments.length, jobId);
						analyticData.concat(processedDocuments);
					}
					i++;
				}

				var waits = [];
				for (jobId in jobIds) {
					if (jobIds[jobId] > 0) {
						waits.push('jod#'+jobId+' - '+ jobIds[jobId]);
					}
				}

				if (waits.length) {
					//console.log(" wait: " + waits.join(', '));
					setTimeout(wait_fn, 5000);
				} else {
					resolve(analyticData);
				}
			})
			.catch(reject);
		}
		setTimeout(wait_fn, 5000);
	});

})
.then(function(analyticData){
	console.log("Done. All documents received.");
})
.catch(function(err) {
	console.log("JobIdFeatureModeTestApp faild\n");
	console.log(err);
	console.log(err.stack);
})
.then(function() {
	if (!appConfigurationId) return;
	return SemantriaActiveSession.removeConfigurations([appConfigurationId], true);
});

function queueDocumentSingle() {
	var jobs = [];
	for (var jobId in documents) {
		var docsForJob = documents[jobId];
		var docs = []
		for (index in docsForJob) {
			// Queues document for processing on Semantria service
			docs.push(SemantriaActiveSession.queueDocument(docsForJob[index], appConfigurationId, true));
		}
		(function(count, jobId) {
			var job = promise.all(docs).then(function() {
				console.log("%d documents queued for %s job ID", count, jobId);
			});
			jobs.push(job);
		})(Object.keys(docsForJob).length, jobId);
	}

	return promise.all(jobs);
}

function queueDocumentBatches() {
	var jobs = [];
	for (var jobId in documents) {
		// Queues document for processing on Semantria service
		(function (jobId) {
			SemantriaActiveSession.queueBatchOfDocuments(documents[jobId], appConfigurationId, true).then(function () {
				console.log("%d documents queued for %s job ID", Object.keys(documents[jobId]).length, jobId);
			});
		})(jobId);
	}

	return promise.all(jobs);
}

function queueDocumentSingleBatch() {
	var batch = [];

	for (var jobId in documents) {
		batch = batch.concat(documents[jobId]);
	}
	return SemantriaActiveSession.queueBatchOfDocuments(batch, appConfigurationId, true).then(function() {
		console.log("%d documents queued in single batch", batch.length);

	});
}

function randomProperty(obj) {
	var keys = Object.keys(obj)
	return keys[ keys.length * Math.random() << 0];
}
