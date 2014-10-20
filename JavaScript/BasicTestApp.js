;(function (){
	// the consumer key and secret
	var consumerKey = "",
		consumerSecret = "";

	function log(message) {
		document.body.innerHTML += message + "<br/>";
	}

	function getInitialText() {
		var result = [];

		result.push("Lisa - there's 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh's). I couldn't find them instore and i'm not going to walmart before the 19th. Oh well sounds like i'm not missing much ...lol");
		result.push("In Lake Louise - a guided walk for the family with Great Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The \"must-do\" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.");
		result.push("On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time.");

		return result;
	}

	function processResponse(analyticData) {
		for(var i=0, data;data=analyticData[i];i++) {
			// Printing of document sentiment score
			log(SemantriaActiveSession.tpl("Document {id}. Sentiment score: {sentiment_score}", data));
			// Printing of document themes
			log("<div style='margin-left: 15px;'/>Document themes:");
			if (data.themes) {
				for(var j=0, theme; theme=data.themes[j]; j++) {
					log(SemantriaActiveSession.tpl("<div style='margin-left: 30px;'/>{title} (sentiment: {sentiment_score})", theme));
				}
			} else {
				log("<div style='margin-left: 30px;'/>No themes were extracted for this text");
			}

			// Printing of document entities
			log("<div style='margin-left: 15px;'/>Entities:");
			if (data['entities']) {
				for(var j=0, entity; entity=data['entities'][j]; j++) {
					log(SemantriaActiveSession.tpl(
						"<div style='margin-left: 30px;'/>{title} : {entity_type} (sentiment: {sentiment_score})", entity
					));
				}
			} else {
				log("<div style='margin-left: 30px;'/>No entities were extracted for this text");
			}
			
			// Printing of document entities
			log("<div style='margin-left: 15px;'/>Topics:");
			if (data.topics) {
				for(var j=0, topic; topic=data.topics[j]; j++) {
					log(SemantriaActiveSession.tpl(
						"<div style='margin-left: 30px;'/>{title} : {type} (strength: {sentiment_score})", topic
					));
				}
			} else {
				log("<div style='margin-left: 30px;'/>No topics were extracted for this text");
			}
		}
	}

	function receiveResponse(entitiesCount) {
		// As Semantria isn't real-time solution you need to wait some time before getting of the processed results
		// Wait ten seconds while Semantria process queued document

		var analyticData = [];
		var timeout = setInterval(function() {
			log("Retrieving your processed results...");
			// Requests processed results from Semantria service
			var processedDocuments = SemantriaActiveSession.getProcessedDocuments();

			if (processedDocuments && processedDocuments.length) {
				analyticData = analyticData.concat(processedDocuments);
			}

			if(analyticData.length == entitiesCount) {
				clearInterval(timeout);
				processResponse(analyticData);
			} 
		}, 2000);
	}

	window.runTestApp = function() {
		var initialTexts = getInitialText();

		log("<h1>Semantria Document processing mode demo.</h1>");
		// session is a global object
		SemantriaActiveSession = new Semantria.Session(consumerKey, consumerSecret, "myApp");
		SemantriaActiveSession.override({
			onError: function() {
				console.warn("onError:");
				console.warn(arguments);
			},

			onRequest: function() {
				console.log("onRequest:");
				console.log(arguments);
			},

			onResponse: function() {
				console.log("onResponse:");
				console.log(arguments);
			},

			onAfterResponse: function() {
				console.log("onAfterResponse:");
				console.log(arguments);
			}
		});
		
		for(var i=0,text; text=initialTexts[i]; i++) {
			// Creates a sample document which need to be processed on Semantria
			var id = Math.floor(Math.random() * 10000000);
			// Queues document for processing on Semantria service
			var status = SemantriaActiveSession.queueDocument({
				id: id,
				text: text
			});
				
			// Check status from Semantria service
			if (status == 202) {
				log("Document# " + id + " queued successfully");
			}
		}

		receiveResponse(initialTexts.length);
	}
})();