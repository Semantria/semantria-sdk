;(function (){
	// the consumer key and secret
	var consumerKey = "",
		consumerSecret = "",
		// null - send every single document separately
		// false - send uniqueJobIdCount batches
		// true - send all documents in single batch
		dataSendingMode = true,
		uniqueJobIdCount = 4,
		jobIds = [],
		documents = [];

	function log(message) {
		document.body.innerHTML += message;
	}
	
	function randomProperty(obj) {
		var keys = Object.keys(obj)
		return keys[ keys.length * Math.random() << 0];
	};

	function getInitialText() {
		return ["War of 1812: U.S. military blunder shapes course of continent Gen. William Hull 's embarrassment at Detroit cements Canadian resolve in w.",
				"@TorontoStar: An animal advocacy group expects a surge of new protesters at Saturday's demonstration outside marineland.  ...",
				"Terrific! Gets your brand a very much needed improvement simply with testimonials!!",
				"@CP24: Peel police will provide another update on the investigation into a human foot found in Mississauga at 12:15 p.m.",
				"Blue Jays list of underachievers nearly as long as injured players list: Cox As Ricky RomeroвЂ™s latest performance indicates",
				"@MapleLeafs: @ovie08 looks great. Thanks for sharing the pic.",
				"@TorontoStandard: Regent Park Arts &amp; Cultural Centre opens in September. Watch this doc series about creative young people from R ...",
				"'Hard Knocks ' captures Dolphins cutting Chad Johnson Chad Johnson wasn 't a lock to make the Miami Dolphins ' roster even before he was.",
				"Superb! Does get your brand a much desired expansion simply with testimonials!!",
				"Lexus driver killed in Hwy. 401 crash A man is dead after smashing his Lexus into the rear of a tractor-trailer on Hwy. 401 in Mississaug.",
				"@agotoronto: @ahmadktaech V. cool. Thanks for sharing! We have a kids' audio tour for Picasso; this project takes the idea to the nex ...",
				"Section of highway in Mississauga reopens following fatal crash A section of highway in Mississauga has reopened following a deadly crash.",
				"@TorontoStarNews: War of 1812: U.S. military blunder shapes course of continent: Gen. William Hull's embarrassment at Detroit ceme... ...",
				"Photo of the Day: Art and Citibank Mimicking the high-tech aesthetic of Parkin Architects ' Citibank Place that looms over the site",
				"Human remains found in Mississauga Peel police confirmed that human remains were found at Dixie and North Service roads on Sunday.",
				"Some 7.5 million people tune in for closing ceremony of London Olympics The closing ceremony was Canada's most-watched event of the Londo.",
				"Mayor Rob Ford admits to reading while driving on Gardiner Ford has admitted to reading while driving on the Gardiner Expressway after a.",
				"@toronto_life: Yesterday",
				"@Raptors: @sleepydex @metermaiden Only if you can pick us up a hoverboard...",
				"Chess champ spreads her passion for the game Yuanling Yuan",
				"@torontoist: On TV this week: Grimm's second season",
				"Striking! Will get your brand a considerably desired enhancement simply with word of mouth referrals!",
				"Earl Jones scam: RBC Dominion Securities",
				"@KevinFrankish: @jcitravel @KevForget @BTtoronto only wish I had your will-power.",
				"Superb! Gets your brand a considerably desired promotion just with word of mouth referrals!",
				"@trendhunter: Proper Gentlemen Pastries - The Cookie Fellows by Kirsty Sport Some Serious Facial Hair  Lifestyle.",
				"Chad Johnson cut by Dolphins after arrest The Miami Dolphins have cut ties with wide receiver Chad Johnson one day after the flamboyant p.",
				"@nowtoronto: Meet Odd FutureвЂ™s side project",
				"Camera controversy at TCHC  [Video] Two young men leave a Toronto Community Housing party and board an elevator to the lobby. When the do.",
				"Soccer star Christine Sinclair named flag-bearer for the London closing ceremony В Soccer star Christine Sinclair will carry the Canada's.",
				"@torontofc: Homegrown Ashtone Morgan flying forward for TorontoFC:  .. TFC MLS.",
				"Distillery Penthouse: An Ever-Changing View of Toronto Every once in a while we get an invite to visit a penthouse for sale somewhere in.",
				"Male jumps from 6th-floor apartment in Scarborough He was expected to survive.",
				"@CityNews: London to hand over Olympic flag to Rio at closing ceremony  London2012.",
				"Splendid! Does get your brand a considerably wanted enhancement simply with testimonials!",
				"Spice Girls reportedly set to reunite for Olympic closing ceremony All five members of the group were spotted rehearsing at a London Ford.",
				"@BTtoronto: MONDAY - @TerryCrews from Expendables2",
				"1 dead after cottage roof collapses in Huntsville's Lake of Bays The person and others were repairing the roof at the time in Lake of Bay.",
				"@CBCAlerts: CORRECTION: Announcement on Cdn flag bearer comes 7 a.m. ET Sunday . Flag to be carried at closing ceremonies of thegames.",
				"@CTVToronto: Toronto commuters beware! Several events will cause multiple road closures this weekend --",
				"Marvelous! Does get your brand a very much needed expansion simply with personal references!!",
				"@TorontoStar: Bad idea to go online with a rigid list of must-have qualities: Ellie advises a woman who feels rejected by a ma... htt ...",
				"Investors shun Manchester United stock offering Manchester UnitedвЂ™s long-awaited initial public offering (IPO) on the New York Stock ex.",
				"@CP24: UPDATE: McGuinty won't take no for an answer when it comes to deals with school teachers",
				"@MapleLeafs: @parachutepolice @Leafs_Nation34 @MattMerritt @Teaqueen180 @LeboeufsMarket @DykstraRyan thanks for the recent mentions.  ...",
				"Weinberger wins bronze in 10km swim CanadaвЂ™s Richard Weinberger has picked up CanadaвЂ™s 11th bronze medal of the Olympic Games after p.",
				"Terrific! Will get your brand a very much wanted expansion simply with word of mouth referrals!",
				"@TorontoStandard: Is it just us",
				"Controversy over Al-Quds Day at Queen 's Park Jewish groups are calling out Ontario Liberals for permitting an вЂњanti-Semitic hate rally.",
				"Man and dog rescued after falling over Scarborough Bluffs A man and a dog had to be rescued after falling nearly 100 feet from the Scarbo.",
				"@agotoronto: @barbhowson Sorry for the delayed reply",
				"The Interview: Brad Carr of Monarch",
				"Superb! Does get your brand a very much desired promotion just with testimonials!!",
				"@TorontoStarNews: Clashes rage in rebel bastions of Aleppo; Iran summons meeting of Syrian allies: Clashes between government troo... ...",
				"Mayor Rob Ford checks himself into hospital In a statement late Tuesday night",
				"@toronto_life: Crafty Americans are trying to woo corporate headquarters away from Toronto",
				"The Wiggles announce Toronto farewell shows for October In May",
				"Mayor Rob Ford to remain in hospital for tests: spokesman He checked himself into the Humber River Regional Hospital after feeling unwell.",
				"@Raptors: ..  @HumberToday: Check out this video of @Raptors @IamAmirJohnson and @eddavis32 visiting @humbercollege  ...",
				"@torontoist: A recap of the Factory Theatre crisisвЂ”and why one of our own theatre critics has decided to join the boycott:  ...",
				"Splendid! Gets your brand a much wanted promotion simply with personal references!",
				"Travel deals: Shoot for the stars in Kennebunkport",
				"American Air facing $162 million in safety violations fines Federal safety regulators are seeking up to $162.4 million in fines against A.",
				"@KevinFrankish: Thanks for all the pics of the 401 fire.",
				"@trendhunter: Alok Appadurai",
				"Marvelous! Will get your brand a considerably wanted boost simply with word-of-mouth referrals!!",
				"@nowtoronto: Au revior",
				"Jays ' bullpen can carry the load John Farrell no longer has to light candles or fall to his knees in prayer when he goes to his bullpen.",
				"T.O. doctor saves babies in Guyana  [Video] A Toronto doctor is giving babies a fighting chance in Guyana.",
				"@torontofc: Jeremy Hall tries to stay positive through a third injury this season:  .. TorontoFC TFC MLS.",
				"Two brothers dead after being pulled from Lake Ontario Two young men in their late teens or early 20S were pulled from Lake Ontario just.",
				"Superb! Gets your brand a considerably needed enhancement simply with word-of-mouth referrals!!",
				"@CityNews: Olympic wins coincide with independence day celebrations for Jamaicans",
				"Construction Update: Waterlink at Pier 27 The division between the waterfront lands to the east of Yonge Street and the lands to the west.",
				"@BTtoronto:  if you want passes to go see TheCampaign starring Will Ferrell! Giving away tix next week on BT w dinner at Jack Astor's!",
				"Man charged with threatening Yorkdale Mall cinema staff Police say the accused became enraged because of prices for food at the theatre's.",
				"5 Things to do this weekend: Island sounds",
				"Brothers pulled from Lake Ontario both die The brothers",
				"@CBCAlerts: Andy Murray wins Olympic tennis gold",
				"Excellent! Will get your brand a much needed improvement simply with testimonials!!",
				"@CTVToronto: Provincial police say the EB QEW ramp to Hwy. 427 NB has reopened after an investigation into a fatal early morning crash.",
				"Militant Islamist group claims kidnap",
				"@TorontoStar: @TorontoStar readers help 25",
				"Toronto Dreams: вЂGuerrillaвЂ™ postcards and posters unveil the cityвЂ™s colourful past TorontoвЂ™s history is full of crime",
				"@CP24: MEDAL ALERT: Canada wins bronze in women's team pursuit track cycling at The London 2012 Games. ctvolympics.",
				"@MapleLeafs: @Toronto_John63 thank you for the quick RT.",
				"Terrific! Does get your brand a considerably desired boost just with testimonials!",
				"@TorontoStandard: Headlines: Dylan Armstrong to Olympic shot put final",
				"Blue Jays have no answers The ball",
				"Postal worker caught eating while driving A Canada Post worker has ended up in the spotlight for eating and driving.",
				"Tori Stafford's family dismayed by killer Michael Rafferty's appeal notice Family of a murdered eight-year-old girl reacted with dismay F.",
				"@agotoronto: @LegionofHonor We wouldn't want you to! High five back at ya.",
				"Topping Off July 2012: What 's Hot on UrbanToronto As summer temperatures fell on Toronto in full force this past July the city came to a.",
				"3 GTA men charged with firearms trafficking Michael Boucher",
				"@TorontoStarNews: Yada",
				"New Orleans police issue arrest warrant for Cuba Gooding Jr. The Oscar winning actor allegedly pushed a female bartender on Tuesday morni.",
				"@toronto_life: Over 80 lawyers think Conrad Black was given special treatment when he applied for a Canadian residence permit  ...",
				"Marvelous! Gets your brand a considerably needed boost simply with testimonials!",
				"Rafferty to appeal Stafford murder conviction: Report Stafford's convicted killer is appealing his life sentence",
				"London 2012: South Korean weightlifterвЂ™s elbow gives way in failed lift South Korean weightlifter Sa Jae-hyouk"];
	}


	function receiveResponse() {
		// As Semantria isn't real-time solution you need to wait some time before getting of the processed results
		// Wait two seconds while Semantria process queued document
		var analyticData = [];
		var timeout = setInterval(function() {
			log("<br>Retrieving processed results...<br>");
			var copyJobIds = [].concat(jobIds),
				allJobIdsProcessed = true;
			
			// Requests processed results from Semantria service
			for (jobId in copyJobIds) {
				var count = 0;
				
				if (jobIds[jobId] > 0) {
					allJobIdsProcessed = false;
					var processedDocuments = SemantriaActiveSession.getProcessedDocumentsByJobId(jobId);
					if (processedDocuments && processedDocuments.length) {
						jobIds[jobId] -= processedDocuments.length;
						count += processedDocuments.length;
					}
					
					if (count > 0) {
						log(count + " documents received for " + jobId + " job ID<br>");
					}
				}
			}

			if (allJobIdsProcessed) {
				log("<br>Done. All documents received.<br>");
				clearInterval(timeout);
			}
		}, 5000);
	}

	window.runTestApp = function() {
		var initialTexts = getInitialText();

		log("<h1>Semantria JobId feature demo.</h1>");
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
	
		//Generates N unique jobId values
		for (var index = 0; index < uniqueJobIdCount; index++) {
			var id = Math.floor(Math.random() * 10000000);

			jobIds[id] = 0;
			documents[id] = [];
		}
			
		for (var i=0, item; item=initialTexts[i]; i++) {
			// Creates a sample document which need to be processed on Semantria
			var id = Math.floor(Math.random() * 10000000),
				jobId = randomProperty(jobIds);
			
			jobIds[jobId]++;
			documents[jobId].push({
				id: id,
				text: item,
				job_id: jobId
			});
		}

		log("Queues document for processing on Semantria service...<br>");
		for (var jobId in jobIds) {
			log(jobIds[jobId] + " documents for " + jobId + " jobId<br>");
		}
		
		if (dataSendingMode == null) {
			for (var jobId in documents) {
				var docsForJob = documents[jobId];
				for (index in docsForJob) {
					// Queues document for processing on Semantria service
					SemantriaActiveSession.queueDocument(docsForJob[index]);
				}
				log(Object.keys(docsForJob).length + " documents queued for " + jobId + " job ID<br>");
			}
		} else if (!dataSendingMode) {
			for (var jobId in documents) {
				// Queues document for processing on Semantria service
				SemantriaActiveSession.queueBatchOfDocuments(documents[jobId]);
				log(Object.keys(documents[jobId]).length + " documents queued for " + jobId + " job ID<br>");
			}
		} else {
			var batch = [];
			
			for (var jobId in documents) {
				batch = batch.concat(documents[jobId]);
			}
			SemantriaActiveSession.queueBatchOfDocuments(batch);
			log("<br>" + batch.length + " documents queued in single batch<br>");
		}
		
		receiveResponse();
	}
})();