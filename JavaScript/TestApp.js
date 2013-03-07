var outputElement = document.getElementById("output");

// Implement custom callback handler if needed
function SimpleCallbackHandler() {};
SimpleCallbackHandler.prototype = CallbackHandler;

SimpleCallbackHandler.prototype.onRequest = function(request) {
	//output('onRequest: ' + request);
};
SimpleCallbackHandler.prototype.onResponse = function(request) {
	//output('onResponse: ' + request);
};
SimpleCallbackHandler.prototype.onError = function(request) {
	//output('onError: ' + request);
};
SimpleCallbackHandler.prototype.onDocsAutoResponse = function(request) {};
SimpleCallbackHandler.prototype.onCollsAutoResponse = function(request) {};


// Main function of our test application
function runTestApp() {

	// the consumer key and secret
	var consumerKey = "";
	var consumerSecret = "";

	var initialTexts = new Array();
	initialTexts.push("Lisa - there's 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh's). I couldn't find them instore and i'm not going to walmart before the 19th. Oh well sounds like i'm not missing much ...lol");
	initialTexts.push("In Lake Louise - a guided walk for the family with Great Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The \"must-do\" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.");
	initialTexts.push("On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time.");

	output('Semantria service demo.<br/><br/>');
	
	// Creates JSON serializer instance
    var serializer = new JsonSerializer();
	// Initializes new session with the keys, the serializer object, application name and compression condition.
	var session = new Session(consumerKey, consumerSecret, serializer, 'myApp', true);
	// Initialize session callback handler
	var callback = new SimpleCallbackHandler();
	session.setCallbackHandler(callback);

	// Push text for analysis
	for (var key in initialTexts) {
		// Creates a sample document which need to be processed on Semantria
		var doc = {"id" : Math.floor(Math.random()*9999999), "text" : initialTexts[key]};
		
		// Queues document for processing on Semantria service
		var status = session.queueDocument(doc);
			
		// Check status from Semantria service
		if (status == 202) {
			output("\"" + doc["id"] + "\" document queued successfully.<br/>");
		}
	}

	// As Semantria isn't real-time solution you need to wait some time before getting of the processed results
	// Wait ten seconds while Semantria process queued document
	var analyticData = new Array();
	while(analyticData.length < initialTexts.length) {
		output("Retrieving your processed results...<br/>");
		// Give Semantria time to process queued documents
		// You can use setTimeout or setInterval solutions instead
		wait(2000);

		// Requests processed results from Semantria service
		var processedDocuments = session.getProcessedDocuments();
		if (processedDocuments && processedDocuments.constructor == Array) {
			for (var i in processedDocuments) {
				analyticData.push(processedDocuments[i]);
			}
		}
	}

	// Output results
	for (var key in analyticData) {
		var data = analyticData[key];

		// Printing of document sentiment score
		output("<br/>Document " + data["id"] + ". Sentiment score: " + data["sentiment_score"]);

		// Printing of document themes
		output("<div style='margin-left: 15px;'/>Document themes:");
		if (data["themes"]) {
			for (var i in data["themes"]) {
				var theme = data["themes"][i];
				output("<div style='margin-left: 30px;'/>" + theme["title"] + " (sentiment: " + theme["sentiment_score"] + ")");
			}
		} else {
			output("<div style='margin-left: 30px;'/>No themes were extracted for this text");
		}

		// Printing of document entities
		output("<div style='margin-left: 15px;'/>Entities:");
		if (data["entities"]) {
			for (var i in data["entities"]) {
				var entity = data["entities"][i];
				output("<div style='margin-left: 30px;'/>" + entity["title"] + " : " + entity["entity_type"]
					+ " (sentiment: " + entity["sentiment_score"] + ")");
			}
		} else {
			output("<div style='margin-left: 30px;'/>No entities were extracted for this text");
		}
		
		// Printing of document topics
		output("<div style='margin-left: 15px;'/>Topics:");
		if (data["topics"]) {
			for (var i in data["topics"]) {
				var topic = data["topics"][i];
				output("<div style='margin-left: 30px;'/>" + topic["title"] + " : " + topic["type"]
					+ " (strength: " + topic["strength_score"] + ")");
			}
		} else {
			output("<div style='margin-left: 30px;'/>No topics were extracted for this text");
		}

	}
}

function wait(milliseconds) {
	milliseconds += new Date().getTime();
	while (new Date() < milliseconds){}
}

function output(message) {
	if (outputElement) {
		outputElement.innerHTML += message;
	} else {
		console.log(message);
	}
}