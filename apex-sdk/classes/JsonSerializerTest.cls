@isTest
private class JsonSerializerTest {
	static testMethod void testSerializingBlacklistItem() {
		Serializer serializerObj = new JsonSerializer();
        BlacklistUpdateProxy proxy = new BlacklistUpdateProxy();
        proxy.addItem(new BlacklistItem('.*@.*com'));
        proxy.addItem(new BlacklistItem('.*@com\\.net'));
        proxy.removeItem(new BlacklistItem('http://www\\..*\\.com'));
        
        String expectedResult = 
        '{' +
            '"removed":["http://www\\\\..*\\\\.com"],' + 
		    '"added":[".*@.*com",".*@com\\\\.net"]' + 
		'}'; 
        
        System.assertEquals(expectedResult, serializerObj.serialize(proxy, null));
	}
	
	static testMethod void testSerializingQuery() {
        Serializer serializerObj = new JsonSerializer();
        QueryUpdateProxy proxy = new QueryUpdateProxy();
        proxy.addItem(new Query('Feature: Cloud service', 'Amazon AND EC2 AND Cloud'));
        proxy.removeItem(new Query('Features', 'query 2'));
        
        String expectedResult = 
        '{' +
            '"removed":["Features"],' + 
            '"added":[' +
		         '{"query":"Amazon AND EC2 AND Cloud",' +
		          '"name":"Feature: Cloud service"}' +
		      ']' +
	      '}'; 
        
        System.assertEquals(expectedResult, serializerObj.serialize(proxy, null));
    }
    
    static testMethod void testSerializingEntity() {
        Serializer serializerObj = new JsonSerializer();
        UserEntityUpdateProxy proxy = new UserEntityUpdateProxy();
        proxy.addItem(new UserEntity('chair', 'furniture'));
        proxy.removeItem(new UserEntity('table', 'type 2'));
        
        String expectedResult = 
        '{' +
           '"removed":["table"],' +
		   '"added":[' +
		      '{' +
		         '"type":"furniture",' +
		         '"name":"chair"' +
		      '}' +
		   ']' +
		'}'; 
        
        System.assertEquals(expectedResult, serializerObj.serialize(proxy, null));
    }
    
    static testMethod void testSerializingSentimentPhrase() {
        Serializer serializerObj = new JsonSerializer();
        SentimentPhraseUpdateProxy proxy = new SentimentPhraseUpdateProxy();
        proxy.addItem(new SentimentPhrase('chair', 0.2d));
        proxy.removeItem(new SentimentPhrase('table', -0.1d));
        
        String expectedResult = 
        '{' +
           '"removed":["table"],' +
           '"added":[' +
              '{' +
                 '"weight":0.2,' +
                 '"title":"chair"' +
              '}' +
           ']' +
        '}'; 
        
        System.assertEquals(expectedResult, serializerObj.serialize(proxy, null));
    }
    
    static testMethod void testSerializingCategory() {
        Serializer serializerObj = new JsonSerializer();
        CategoryUpdateProxy proxy = new CategoryUpdateProxy();
        Category c1 = new Category('Feature: Cloud service', (0.0).doubleValue());
        c1.setSamples(new List<String>());
        
        proxy.addItem(c1);
        proxy.removeItem(new Category('Features', (0.0).doubleValue()));
        
        String expectedResult = 
        '{' + 
            '"removed":["Features"],' + 
		    '"added":[' + 
		         '{' +
		            '"weight":0.0,' +
		            '"samples":[],' +   
		            '"name":"Feature: Cloud service"' + 
		         '}' + 
		    ']' + 
		'}';
        
        System.assertEquals(expectedResult, serializerObj.serialize(proxy, null));
    }
    
    static testMethod void testSerializingConfiguration() {
        Serializer serializerObj = new JsonSerializer();
        ConfigurationUpdateProxy proxy = new ConfigurationUpdateProxy();
        Configuration c1 = new Configuration();
        
        c1.setConfigId('23498367');
        c1.setName('A test configuration');
        c1.setAutoResponding(true);
        c1.setIsPrimary(true);
        c1.setOneSentence(false);
        c1.setDocConceptTopicsLimit(5);
        c1.setDocQueryTopicsLimit(5);
        c1.setDocNamedEntitiesLimit(5);
        c1.setDocUserEntitiesLimit(5);
        c1.setDocThemesLimit(0);
        c1.setEntityThemesLimit(5);
        c1.setFacetsLimit(15);
        c1.setFacetAttsLimit(5);
        c1.setCollConceptTopicsLimit(5);
        c1.setCollQueryTopicsLimit(5);
        c1.setCollNamedEntitiesLimit(5);
        c1.setCollThemesLimit(0);
        c1.setPhrasesLimit(0);
        c1.setSummaryLimit(0);
        c1.setCharsThreshold(80);
        c1.setCallbackUrl('https://anyapi.anydomain.com/processed/docs.json');
        c1.setLanguage('English'); 
        
        proxy.addItem(c1);
        
        Configuration c2 = new Configuration();
        c2.setConfigId('45699836');
        proxy.removeItem(c2); 
        
        Configuration c3 = new Configuration();
        c3.setConfigId('cloned_config_id');
        c3.setName('Cloned configuration');
        c3.setAutoResponding(true);
        c3.setIsPrimary(true);
        c3.setOneSentence(true);
        proxy.cloneItem(c3);
        
        String expectedResult = 
        '{' +
           '"removed":["45699836"],' +
		   '"added":[' +
		      '{' +
		      	'"template":null,' +
            	'"one_sentence":false,' +
		      	'"name":"A test configuration",' +
		      	'"language":"English",' +
		      	'"is_primary":true,' +
		      	'"document":{' +
		      		'"user_entities_limit":5,' +
		      		'"themes_limit":0,' +
		      		'"summary_limit":0,' +
		      		'"query_topics_limit":5,' +
		      		'"phrases_limit":0,' +
		      		'"named_entities_limit":5,' +
		            '"entity_themes_limit":5,' +
		            '"concept_topics_limit":5' +
		         '},' +
		         '"config_id":"23498367",' +
				 '"collection":{' +
				 	'"themes_limit":0,' +
				 	'"query_topics_limit":5,' +
				 	'"named_entities_limit":5,' +
		            '"facets_limit":15,' +
		            '"facet_atts_limit":5,' +
		            '"concept_topics_limit":5' +
		         '},' +
		         '"chars_threshold":80,' +		
		         '"callback":"https://anyapi.anydomain.com/processed/docs.json",' +         
		         '"auto_responding":true' +
		      '},' +
		      '{' +
	            '"template":"cloned_config_id",' +
	            '"one_sentence":true,' +
	            '"name":"Cloned configuration",' +
	            '"language":null,' +
	            '"is_primary":true,' +
	            '"document":null,' +
	            '"config_id":null,' +
	            '"collection":null,' +
	            '"chars_threshold":null,' +
	            '"callback":null,' +
	            '"auto_responding":true' +
	          '}' +
		   ']' +
		'}';
        
        System.assertEquals(expectedResult, serializerObj.serialize(proxy, null));
    }
	
    static testMethod void testSerializingDocument() {
    	Serializer serializerObj = new JsonSerializer();
    	Document d = new Document('6F9619FF8B86D011B42D00CF4FC964FF', 'A chunk of text for processing');
    	String expectedResult = 
    	'{' +
		   '"text":"A chunk of text for processing",' +
		   '"id":"6F9619FF8B86D011B42D00CF4FC964FF"' +
		'}';
		
		System.assertEquals(expectedResult, serializerObj.serialize(d, null));
    }
    
    static testMethod void testSerializingDocumentList() {
        Serializer serializerObj = new JsonSerializer();
        List<Document> l = new List<Document>();
        l.add(new Document('6F9619FF8B86D011B42D00CF4FC964FF', 'A chunk of text for processing'));
        
        String expectedResult = 
        '[{' +
           '"text":"A chunk of text for processing",' +
           '"id":"6F9619FF8B86D011B42D00CF4FC964FF"' +
        '}]';
        
        System.assertEquals(expectedResult, serializerObj.serialize(l, null));
    }
     
    static testMethod void testSerializingCollection() {
        Serializer serializerObj = new JsonSerializer();
        Collection c = new Collection();
        c.setId('6F9619FF8B86D011B42D00CF4FC964FF');
        List<String> documents = new List<String>();
        documents.add('A chunk of text for processing');
        documents.add('A chunk of text for processing'); 
        c.setDocuments(documents);
        
        String expectedResult = 
        '{' +
            '"id":"6F9619FF8B86D011B42D00CF4FC964FF",' +
           '"documents":["A chunk of text for processing","A chunk of text for processing"]' +
        '}';
        
        System.assertEquals(expectedResult, serializerObj.serialize(c, null));
    }
    
    static testMethod void testDeserializingBlacklistItem() {
        JsonSerializer serializerObj = new JsonSerializer();
        String source = '[' +
						   '".*@.*com",' +
						   '".*@com\\.net",' +
						   '"http://www\\.*\\.com"' +
						']'; 
                        
        List<String> blacklistItemList = (List<String>)serializerObj.deserialize(source, 'BlacklistItem');
        System.assert(null != blacklistItemList); 
        System.assertEquals(3, blacklistItemList.size());
    }
    
    static testMethod void testDeserializingQuery() {
        JsonSerializer serializerObj = new JsonSerializer();
        String source = '[' +
					         '{' +
					            '"name":"Feature: Cloud service",' +
					            '"query":"Amazon AND EC2 AND Cloud"' +
					         '}' +
						 ']'; 
                        
        List<Query> queryList = (List<Query>)serializerObj.deserialize(source, 'Query');
        System.assert(null != queryList); 
        System.assertEquals(1, queryList.size());
        System.assertEquals('Feature: Cloud service', queryList.get(0).getName());
        System.assertEquals('Amazon AND EC2 AND Cloud', queryList.get(0).getQuery());
    }
    
    static testMethod void testDeserializingEntity() {
		Serializer serializerObj = new JsonSerializer();
		String source = '[' +
						   '{' +
						      '"name":"chair",' +
						      '"type":"furniture"' +
						   '}' +
						']';
		
		List<UserEntity> entityList = (List<UserEntity>)serializerObj.deserialize(source, 'UserEntity');
		System.assert(null != entityList); 
        System.assertEquals(1, entityList.size());
        System.assertEquals('chair', entityList.get(0).getName());
        System.assertEquals('furniture', entityList.get(0).getType());
	}
	
	static testMethod void testDeserializingSentimentPhrase() {
        Serializer serializerObj = new JsonSerializer();
        String source = '[' +
                           '{' +
                              '"title":"chair",' +
                              '"weight":0.3' +
                           '}' +
                        ']';
        
        List<SentimentPhrase> sentimentPhraseList = (List<SentimentPhrase>)serializerObj.deserialize(source, 'SentimentPhrase');
        System.assert(null != sentimentPhraseList); 
        System.assertEquals(1, sentimentPhraseList.size());
        System.assertEquals('chair', sentimentPhraseList.get(0).getTitle());
        System.assertEquals(0.3d, sentimentPhraseList.get(0).getWeight());
    }
	
	static testMethod void testDeserializingCategory() {
		Serializer serializerObj = new JsonSerializer();
		String source = '[' +
						      '{' +
						         '"name":"Feature: Cloud service",' +
						         '"weight":0.75,' +
						         '"samples":["Amazon","EC2"]' +
						      '}' +
						 ']';
		
		List<Category> categoryList = (List<Category>)serializerObj.deserialize(source, 'Category');
		System.assert(null != categoryList); 
        System.assertEquals(1, categoryList.size());
        System.assertEquals('Feature: Cloud service', categoryList.get(0).getName());
        System.assertEquals(0.75, categoryList.get(0).getWeight());
        System.assertEquals(2, categoryList.get(0).getSamples().size());
        System.assert('Amazon'.equals(categoryList.get(0).getSamples().get(0)) || 'EC2'.equals(categoryList.get(0).getSamples().get(0)));
	}
	
	static testMethod void testDeserializingConfiguration() {
		Serializer serializerObj = new JsonSerializer();
		String source = '[' +
						   '{' +
						      '"config_id" : "23498367",' +
						      '"name" : "A test configuration",' +
						      '"is_primary" : true,' +
						      '"auto_responding" : true,' +
						      '"language" : "English",' +
						      '"chars_threshold" : 80,' +
						      '"callback" : "https://anyapi.anydomain.com/processed/docs.json",' +
						      '"document" : {' +
						         '"concept_topics_limit" : 5,' +
						         '"query_topics_limit" : 5,' +
						         '"named_entities_limit" : 5,' + 
						         '"user_entities_limit" : 5,' +
						         '"entity_themes_limit" : 5,' +
						         '"themes_limit" : 5,' +
						         '"phrases_limit" : 5,' +
						         '"summary_limit" : 0' +
						      '},' +
						      '"collection" : {' +
						         '"facets_limit" : 15,' +
						         '"facet_atts_limit" : 5,' +
						         '"concept_topics_limit" : 5,' +
						         '"query_topics_limit" : 5,' +
						         '"named_entities_limit" : 5,' +
						         '"themes_limit" : 5' +
						      '}' +
						   '}' +
						']';
						
		List<Configuration> configurationList = (List<Configuration>)serializerObj.deserialize(source, 'Configuration');
		System.assert(null != configurationList);
		System.assertEquals(1, configurationList.size());
		
		Configuration configurationObj = configurationList.get(0);
		System.assertEquals('23498367', configurationObj.getConfigId());
		System.assertEquals('A test configuration', configurationObj.getName());
		System.assert(configurationObj.isPrimary());
		System.assert(configurationObj.isAutoResponding());
		System.assertEquals('English', configurationObj.getLanguage());
		System.assertEquals('https://anyapi.anydomain.com/processed/docs.json', configurationObj.getCallbackUrl());
		System.assertEquals(80, configurationObj.getCharsThreshold());
		System.assertEquals(5, configurationObj.getDocConceptTopicsLimit());
		System.assertEquals(5, configurationObj.getDocQueryTopicsLimit());
		System.assertEquals(5, configurationObj.getDocNamedEntitiesLimit());
		System.assertEquals(5, configurationObj.getDocUserEntitiesLimit());
		System.assertEquals(5, configurationObj.getDocThemesLimit());
		System.assertEquals(5, configurationObj.getEntityThemesLimit());
		System.assertEquals(5, configurationObj.getPhrasesLimit());
		System.assertEquals(0, configurationObj.getSummarylimit());
		System.assertEquals(15, configurationObj.getFacetsLimit());
		System.assertEquals(5, configurationObj.getFacetAttsLimit());
		System.assertEquals(5, configurationObj.getCollConceptTopicsLimit());
		System.assertEquals(5, configurationObj.getCollQueryTopicsLimit());
		System.assertEquals(5, configurationObj.getCollNamedEntitiesLimit());
		System.assertEquals(5, configurationObj.getCollThemesLimit());
	} 
	
	static testMethod void testDeserializingAnalyticServiceStatus() {
		Serializer serializerObj = new JsonSerializer();
		String source = '{'+
						   '"service_status":"available",'+
						   '"api_version":"2.0",'+
						   '"service_version":"1.0.2.63",'+
						   '"supported_languages":['+
						      '"English",'+
						      '"French"'+
						   ']'+
						'}';
						
		ServiceStatus serviceStatusObj = (ServiceStatus)serializerObj.deserialize(source, 'ServiceStatus');
		System.assert( null != serviceStatusObj );
		System.assertEquals('available', serviceStatusObj.getServiceStatus());
		System.assertEquals('2.0', serviceStatusObj.getApiVersion());
		System.assertEquals('1.0.2.63', serviceStatusObj.getServiceVersion());
		System.assertEquals(2, serviceStatusObj.getSupportedLanguages().size());
	}
	
	static testMethod void testDeserializingSubscription() {
		Serializer serializerObj = new JsonSerializer();
		String source = '{'+
						   '"name":"Subscriber",'+
						   '"status":"active",'+
						   '"priority":"normal",'+
						   '"expiration_date":1293883200,'+
						   '"batch_limit":1,'+
						   '"collection_limit":10,'+
						   '"auto_response_limit":2,'+
						   '"processed_batch_limit":100,'+
						   '"callback_batch_limit":100,'+
						   '"limit_type":"type limit"'+
						'}';
						
		Subscription subscriptionObj = (Subscription)serializerObj.deserialize(source, 'Subscription');
		System.assert(null != subscriptionObj);
		System.assertEquals('Subscriber', subscriptionObj.getName());
		System.assertEquals('active', subscriptionObj.getStatus());
		System.assertEquals('normal', subscriptionObj.getPriority());
		System.assertEquals(1293883200, subscriptionObj.getExpirationDate());
		System.assertEquals(1, subscriptionObj.getBatchLimit());
		System.assertEquals(10, subscriptionObj.getCollectionLimit());
		System.assertEquals(2, subscriptionObj.getAutoResponseLimit());
		System.assertEquals(100, subscriptionObj.getProcessedBatchLimit());
		System.assertEquals(100, subscriptionObj.getCallbackBatchLimit());
		System.assertEquals('type limit', subscriptionObj.getLimitType());
	}
	
	static testMethod void testDeserializingDocumentAnalyticData() {
		Serializer serializerObj = new JsonSerializer();
		String source = '{'+
						   '"id":"6F9619FF8B86D011B42D00CF4FC964FF",'+
						   '"config_id":"23498367",'+
						   '"status":"PROCESSED",'+
						   '"sentiment_score":0.8295653,'+
						   '"summary":"Summary of the document’s text.",'+
						   '"themes":['+
						      '{'+ 
						         '"evidence":1,'+
						         '"is_about":true,'+
						         '"strength_score":0.0,'+
						         '"sentiment_score":0.0,'+
						         '"title":"republican moderates"'+
						      '}'+
						   '],'+
						   '"entities":['+
						      '{'+
						         '"type":"named",'+
						         '"evidence":0,'+
						         '"is_about":true,'+
						         '"confident":true,'+
						         '"entity_type":"Place",'+
						         '"title":"WASHINGTON",'+
						         '"sentiment_score":1.0542796,'+
						         '"themes":['+
						            '{'+
						               '"evidence":1,'+
						               '"is_about":true,'+
						               '"strength_score":0.0,'+
						               '"sentiment_score":0.0,'+
						               '"title":"republican moderates"'+
						            '}'+
						         ']'+
						      '}'+
						   '],'+
						   '"topics":['+
						      '{'+
						         '"title":"Something",'+
						         '"type":"concept",'+
						         '"hitcount":0,'+
						         '"strength_score":0.0,'+
						         '"sentiment_score":0.6133076'+
						      '}'+
						   '],'+
						   '"phrases":['+
						      '{'+
						         '"title":"Something",'+
						         '"is_negated":true,'+
						         '"negating_phrase":"not",'+
						         '"sentiment_score":0.6133076'+
						      '}'+
						   ']'+
						'}';
		
		DocAnalyticData analyticData = (DocAnalyticData)serializerObj.deserialize(source, 'DocAnalyticData');
		System.assert(null != analyticData);
		System.assertEquals('23498367', analyticData.getConfigId());
		System.assertEquals('6F9619FF8B86D011B42D00CF4FC964FF', analyticData.getId());
		System.assertEquals('PROCESSED', analyticData.getStatus());
		System.assertEquals(0.8295653, analyticData.getSentimentScore());
		System.assertEquals('Summary of the document’s text.', analyticData.getSummary());
		System.assertEquals(1, analyticData.getThemes().size());
		System.assertEquals(1, analyticData.getEntities().size());
		System.assertEquals(1, analyticData.getTopics().size());	
		
		//Check Document Themes
		DocTheme themeObj = analyticData.getThemes().get(0);
		System.assertEquals(1, themeObj.getEvidence());
		System.assertEquals(0, themeObj.getSentimentScore());
		System.assertEquals(0, themeObj.getStrengthScore());
		System.assertEquals('republican moderates', themeObj.getTitle());
		System.assert(themeObj.getIsAbout());
		
		//Check Document Entities
		DocEntity entityObj = analyticData.getEntities().get(0);
		System.assertEquals(0, entityObj.getEvidence());
		System.assert(entityObj.getIsAbout());
		System.assert(entityObj.getConfident()); 
		System.assertEquals('WASHINGTON', entityObj.getTitle());
		System.assertEquals(1.0542796, entityObj.getSentimentScore());
		System.assertEquals('Place', entityObj.getEntityType());
		System.assertEquals('named', entityObj.getType());
		
		//Check Entity Themes
		System.assertEquals(1, entityObj.getThemes().size());
		DocTheme entityThemeObj = entityObj.getThemes().get(0);
		System.assertEquals(1, themeObj.getEvidence());
		System.assertEquals(0, themeObj.getSentimentScore());
		System.assertEquals(0, themeObj.getStrengthScore());
		System.assertEquals('republican moderates', themeObj.getTitle());
		System.assert(themeObj.getIsAbout());
		
		//Check Document Topics
		System.assertEquals(1, analyticData.getTopics().size());
		DocTopic topicObj = analyticData.getTopics().get(0);
		System.assertEquals(0, topicObj.getHitCount());
		System.assertEquals(0.6133076, topicObj.getSentimentScore());
		System.assertEquals(0, topicObj.getStrengthScore());
		System.assertEquals('Something', topicObj.getTitle());
		System.assertEquals('concept', topicObj.getType());
		
		//Check Document Phrases
		System.assertEquals(1, analyticData.getPhrases().size());
		DocPhrase phraseObj = analyticData.getPhrases().get(0);
		System.assert(phraseObj.getIsNegated());
		System.assertEquals(0.6133076, phraseObj.getSentimentScore());
		System.assertEquals('Something', phraseObj.getTitle());
		System.assertEquals('not', phraseObj.getNegatingPhrase());
	}
	
	static testMethod void testDeserializingCollectionAnalyticData() {
		Serializer serializerObj = new JsonSerializer();
		String source = '{'+ 
						   '"id":"6F9619FF8B86D011B42D00CF4FC964FF",'+ 
						   '"config_id":"23498367",'+ 
						   '"status":"PROCESSED",'+ 
						   '"facets":['+ 
						      '{'+ 
						         '"label":"Something",'+ 
						         '"count":10,'+ 
						         '"negative_count":2,'+ 
						         '"positive_count":1,'+ 
						         '"neutral_count":7,'+ 
						         '"attributes":['+ 
						            '{'+ 
						               '"label":"Attribute",'+ 
						               '"count":5'+ 
						            '}'+ 
						         ']'+ 
						      '}'+ 
						   '],'+ 
						   '"themes":['+
						      '{'+ 
						         '"phrases_count":1,'+
						         '"themes_count":1,'+
						         '"sentiment_score":0.0,'+
						         '"title":"republican moderates"'+
						      '}'+
						   '],'+
						   '"entities":['+
						      '{'+
						         '"type":"named",'+
						         '"count":1,'+
						         '"negative_count":1,'+
						         '"neutral_count":1,'+
						         '"positive_count":1,'+
						         '"entity_type":"Place",'+
						         '"title":"WASHINGTON"'+
						      '}'+
						   '],'+
						   '"topics":['+
						      '{'+
						         '"title":"Something",'+
						         '"type":"concept",'+
						         '"hitcount":0,'+
						         '"sentiment_score":0.6133076'+
						      '}'+
						   ']'+
						'}';
		
		CollAnalyticData analyticData = (CollAnalyticData)serializerObj.deserialize(source, 'CollAnalyticData');
        System.assert(null != analyticData);
        System.assertEquals('23498367', analyticData.getConfigId());
        System.assertEquals('6F9619FF8B86D011B42D00CF4FC964FF', analyticData.getId());
        System.assertEquals('PROCESSED', analyticData.getStatus());
        System.assertEquals(1, analyticData.getFacets().size()); 
        
        Facet facetObj = analyticData.getFacets().get(0);
        System.assertEquals('Something', facetObj.getLabel());
        System.assertEquals(10, facetObj.getCount());
        System.assertEquals(2, facetObj.getNegativeCount());
        System.assertEquals(1, facetObj.getPositiveCount());
        System.assertEquals(7, facetObj.getNeutralCount());
        System.assertEquals(1, facetObj.getAttributes().size());
        
        Attribute attributeObj = facetObj.getAttributes().get(0);
        System.assertEquals('Attribute', attributeObj.getLabel());
        System.assertEquals(5, attributeObj.getCount());
        
        //Check Document Themes
        CollTheme themeObj = analyticData.getThemes().get(0);
        System.assertEquals(1, themeObj.getPhrasesCount());
        System.assertEquals(1, themeObj.getThemesCount());
        System.assertEquals(0, themeObj.getSentimentScore());
        System.assertEquals('republican moderates', themeObj.getTitle());
        
        //Check Document Entities
        CollEntity entityObj = analyticData.getEntities().get(0);
        System.assertEquals('WASHINGTON', entityObj.getTitle());
        System.assertEquals('named', entityObj.getType());
        System.assertEquals('Place', entityObj.getEntityType());
        System.assertEquals(1, entityObj.getCount());
        System.assertEquals(1, entityObj.getNegativeCount());
        System.assertEquals(1, entityObj.getNeutralCount());
        System.assertEquals(1, entityObj.getPositiveCount());
        
        //Check Document Topics
        System.assertEquals(1, analyticData.getTopics().size());
        CollTopic topicObj = analyticData.getTopics().get(0);
        System.assertEquals(0, topicObj.getHitCount());
        System.assertEquals(0.6133076, topicObj.getSentimentScore());
        System.assertEquals('Something', topicObj.getTitle());
        System.assertEquals('concept', topicObj.getType());
	}
	
	static testMethod void testDeserializingDocumentAnalyticDataList() {
		Serializer serializerObj = new JsonSerializer();
		String source = '[{'+
						   '"id":"6F9619FF8B86D011B42D00CF4FC964FF",'+
						   '"config_id":"23498367",'+
						   '"status":"PROCESSED",'+
						   '"sentiment_score":0.8295653,'+
						   '"summary":"Summary of the document’s text.",'+
						   '"themes":['+
						      '{'+ 
						         '"evidence":1,'+
						         '"is_about":true,'+
						         '"strength_score":0.0,'+
						         '"sentiment_score":0.0,'+
						         '"title":"republican moderates"'+
						      '}'+
						   '],'+
						   '"entities":['+
						      '{'+
						         '"type":"named",'+
						         '"evidence":0,'+
						         '"is_about":true,'+
						         '"confident":true,'+
						         '"entity_type":"Place",'+
						         '"title":"WASHINGTON",'+
						         '"sentiment_score":1.0542796,'+
						         '"themes":['+
						            '{'+
						               '"evidence":1,'+
						               '"is_about":true,'+
						               '"strength_score":0.0,'+
						               '"sentiment_score":0.0,'+
						               '"title":"republican moderates"'+
						            '}'+
						         ']'+
						      '}'+
						   '],'+
						   '"topics":['+
						      '{'+
						         '"title":"Something",'+
						         '"type":"concept",'+
						         '"hitcount":0,'+
						         '"strength_score":0.0,'+
						         '"sentiment_score":0.6133076'+
						      '}'+
						   ']'+
						'}]';
		
		List<DocAnalyticData> analyticDataList = (List<DocAnalyticData>)serializerObj.deserialize(source, 'DocAnalyticDataList');
		System.assert(null != analyticDataList);
		System.assertEquals(1, analyticDataList.size());
		DocAnalyticData analyticData = analyticDataList.get(0);
		System.assertEquals('23498367', analyticData.getConfigId());
		System.assertEquals('6F9619FF8B86D011B42D00CF4FC964FF', analyticData.getId());
		System.assertEquals('PROCESSED', analyticData.getStatus());
		System.assertEquals(0.8295653, analyticData.getSentimentScore());
		System.assertEquals('Summary of the document’s text.', analyticData.getSummary());
		System.assertEquals(1, analyticData.getThemes().size());
		System.assertEquals(1, analyticData.getEntities().size());
		System.assertEquals(1, analyticData.getTopics().size());	
		
		//Check Document Themes
		DocTheme themeObj = analyticData.getThemes().get(0);
		System.assertEquals(1, themeObj.getEvidence());
		System.assertEquals(0, themeObj.getSentimentScore());
		System.assertEquals(0, themeObj.getStrengthScore());
		System.assertEquals('republican moderates', themeObj.getTitle());
		System.assert(themeObj.getIsAbout());
		
		//Check Document Entities
		DocEntity entityObj = analyticData.getEntities().get(0);
		System.assertEquals(0, entityObj.getEvidence());
		System.assert(entityObj.getIsAbout());
		System.assert(entityObj.getConfident()); 
		System.assertEquals('WASHINGTON', entityObj.getTitle());
		System.assertEquals(1.0542796, entityObj.getSentimentScore());
		System.assertEquals('Place', entityObj.getEntityType());
		System.assertEquals('named', entityObj.getType());
		
		//Check Entity Themes
		System.assertEquals(1, entityObj.getThemes().size());
		DocTheme entityThemeObj = entityObj.getThemes().get(0);
		System.assertEquals(1, themeObj.getEvidence());
		System.assertEquals(0, themeObj.getSentimentScore());
		System.assertEquals(0, themeObj.getStrengthScore());
		System.assertEquals('republican moderates', themeObj.getTitle());
		System.assert(themeObj.getIsAbout());
		
		//Check Document Topics
		System.assertEquals(1, analyticData.getTopics().size());
		DocTopic topicObj = analyticData.getTopics().get(0);
		System.assertEquals(0, topicObj.getHitCount());
		System.assertEquals(0.6133076, topicObj.getSentimentScore());
		System.assertEquals(0, topicObj.getStrengthScore());
		System.assertEquals('Something', topicObj.getTitle());
		System.assertEquals('concept', topicObj.getType());
	}
	
	static testMethod void testDeserializingCollectionAnalyticDataList() {
		Serializer serializerObj = new JsonSerializer();
		String source = '[{'+ 
						   '"id":"6F9619FF8B86D011B42D00CF4FC964FF",'+ 
						   '"config_id":"23498367",'+ 
						   '"status":"PROCESSED",'+ 
						   '"facets":['+ 
						      '{'+ 
						         '"label":"Something",'+ 
						         '"count":10,'+ 
						         '"negative_count":2,'+ 
						         '"positive_count":1,'+ 
						         '"neutral_count":7,'+ 
						         '"attributes":['+ 
						            '{'+ 
						               '"label":"Attribute",'+ 
						               '"count":5'+ 
						            '}'+ 
						         ']'+ 
						      '}'+ 
						   '],'+ 
						   '"themes":['+
						      '{'+ 
						         '"phrases_count":1,'+
						         '"themes_count":1,'+
						         '"sentiment_score":0.0,'+
						         '"title":"republican moderates"'+
						      '}'+
						   '],'+
						   '"entities":['+
						      '{'+
						         '"type":"named",'+
						         '"entity_type":"Place",'+
						         '"title":"WASHINGTON",'+
						         '"count":1,'+
						         '"negative_count":1,'+
						         '"neutral_count":1,'+
						         '"positive_count":1'+
						      '}'+
						   '],'+
						   '"topics":['+
						      '{'+
						         '"title":"Something",'+
						         '"type":"concept",'+
						         '"hitcount":0,'+
						         '"sentiment_score":0.6133076'+
						      '}'+
						   ']'+
						'}]';
		
		List<CollAnalyticData> analyticDataList = (List<CollAnalyticData>)serializerObj.deserialize(source, 'CollAnalyticDataList');
		System.assert(null != analyticDataList);
		System.assertEquals(1, analyticDataList.size());
		CollAnalyticData analyticData = analyticDataList.get(0); 
		System.assertEquals('23498367', analyticData.getConfigId());
		System.assertEquals('6F9619FF8B86D011B42D00CF4FC964FF', analyticData.getId());
		System.assertEquals('PROCESSED', analyticData.getStatus());
		System.assertEquals(1, analyticData.getFacets().size()); 
		
		Facet facetObj = analyticData.getFacets().get(0);
		System.assertEquals('Something', facetObj.getLabel());
		System.assertEquals(10, facetObj.getCount());
		System.assertEquals(2, facetObj.getNegativeCount());
		System.assertEquals(1, facetObj.getPositiveCount());
		System.assertEquals(7, facetObj.getNeutralCount());
		System.assertEquals(1, facetObj.getAttributes().size());
		
		Attribute attributeObj = facetObj.getAttributes().get(0);
		System.assertEquals('Attribute', attributeObj.getLabel());
		System.assertEquals(5, attributeObj.getCount());
		
		//Check Document Themes
		CollTheme themeObj = analyticData.getThemes().get(0);
		System.assertEquals(1, themeObj.getPhrasesCount());
        System.assertEquals(1, themeObj.getThemesCount());
        System.assertEquals(0, themeObj.getSentimentScore());
        System.assertEquals('republican moderates', themeObj.getTitle());
		
		//Check Document Entities
		CollEntity entityObj = analyticData.getEntities().get(0);
		System.assertEquals('WASHINGTON', entityObj.getTitle());
        System.assertEquals('named', entityObj.getType());
        System.assertEquals('Place', entityObj.getEntityType());
        System.assertEquals(1, entityObj.getCount());
        System.assertEquals(1, entityObj.getNegativeCount());
        System.assertEquals(1, entityObj.getNeutralCount());
        System.assertEquals(1, entityObj.getPositiveCount());
		
		//Check Document Topics
		System.assertEquals(1, analyticData.getTopics().size());
        CollTopic topicObj = analyticData.getTopics().get(0);
        System.assertEquals(0, topicObj.getHitCount());
        System.assertEquals(0.6133076, topicObj.getSentimentScore());
        System.assertEquals('Something', topicObj.getTitle());
        System.assertEquals('concept', topicObj.getType());
	}
}