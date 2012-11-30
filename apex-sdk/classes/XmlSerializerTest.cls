@isTest
private class XmlSerializerTest {
	static testMethod void testSerializingBlacklistItem() {
		Serializer serializerObj = new XmlSerializer();
		BlacklistUpdateProxy proxy = new BlacklistUpdateProxy();
        proxy.addItem(new BlacklistItem('Added Filter 1'));
        proxy.removeItem(new BlacklistItem('Removed Filter 1'));
        
        String expectedResult = 
        '<?xml version="1.0" encoding="UTF-8"?>' + 
        '<blacklist>' +
            '<added>' +
                '<item>Added Filter 1</item>' +
            '</added>' +  
            '<removed>' + 
                '<item>Removed Filter 1</item>' + 
            '</removed>' + 
        '</blacklist>'; 
         
        System.assertEquals(expectedResult, serializerObj.serialize(proxy, 'BlacklistItem'));
	}
	
	static testMethod void testSerializingQuery() {
		Serializer serializerObj = new XmlSerializer();
		QueryUpdateProxy proxy = new QueryUpdateProxy();
        proxy.addItem(new Query('name 1', 'query 1'));
        proxy.removeItem(new Query('name 2', 'query 2'));
        
        String expectedResult = 
        '<?xml version="1.0" encoding="UTF-8"?>' + 
        '<queries>' +
            '<added>' +
            	'<query>' +
                	'<name>name 1</name>' +
                	'<query>query 1</query>' +
            	'</query>' +
            '</added>' +  
            '<removed>' + 
                '<query>name 2</query>' + 
            '</removed>' +
        '</queries>'; 
        
        System.assertEquals(expectedResult, serializerObj.serialize(proxy, 'Query'));
	}
	
	static testMethod void testSerializingEntity() {
        Serializer serializerObj = new XmlSerializer();
        UserEntityUpdateProxy proxy = new UserEntityUpdateProxy();
        proxy.addItem(new UserEntity('name 1', 'type 1'));
        proxy.removeItem(new UserEntity('name 2', 'type 2'));
        
        String expectedResult = 
        '<?xml version="1.0" encoding="UTF-8"?>' + 
        '<entities>' +
            '<added>' +
                '<entity>' +
                    '<name>name 1</name>' +
                    '<type>type 1</type>' +
                '</entity>' +
            '</added>' +  
            '<removed>' + 
                '<entity>name 2</entity>' + 
            '</removed>' +
        '</entities>'; 
        
        System.assertEquals(expectedResult, serializerObj.serialize(proxy, 'UserEntity'));
    }
    
    static testMethod void testSerializingSentimentPhrase() {
        Serializer serializerObj = new XmlSerializer();
        SentimentPhraseUpdateProxy proxy = new SentimentPhraseUpdateProxy();
        proxy.addItem(new SentimentPhrase('name 1', 0.3d));
        proxy.removeItem(new SentimentPhrase('name 2', 0.1d));
        
        String expectedResult = 
        '<?xml version="1.0" encoding="UTF-8"?>' + 
        '<phrases>' +
            '<added>' +
                '<phrase>' +
                    '<title>name 1</title>' +
                    '<weight>0.3</weight>' +
                '</phrase>' +
            '</added>' +  
            '<removed>' + 
                '<phrase>name 2</phrase>' + 
            '</removed>' +
        '</phrases>'; 
        
        System.assertEquals(expectedResult, serializerObj.serialize(proxy, 'SentimentPhrase'));
    }
    
    static testMethod void testSerializingCategory() {
        Serializer serializerObj = new XmlSerializer(); 
        CategoryUpdateProxy proxy = new CategoryUpdateProxy();
        Category c1 = new Category('Added Category 1', (0.2).doubleValue());
        List<String> samples = new List<String>();
        samples.add('Entity 1');
        samples.add('Entity 2');
        samples.add('Entity 3');
        c1.setSamples(samples);
        
        proxy.addItem(c1);
        proxy.removeItem(new Category('Removed Category 1', (0.0).doubleValue()));
        
        String expectedResult = 
        '<?xml version="1.0" encoding="UTF-8"?>' + 
        '<categories>' +
            '<added>' +
                '<category>' +
                    '<name>Added Category 1</name>' +
                    '<weight>0.2</weight>' +
                    '<samples>' +
                        '<sample>Entity 1</sample>' +
                        '<sample>Entity 2</sample>' +
                        '<sample>Entity 3</sample>' +
                    '</samples>' +
                '</category>' +
            '</added>' +  
            '<removed>' + 
                '<category>Removed Category 1</category>' + 
            '</removed>' +
        '</categories>';
        
        System.assertEquals(expectedResult, serializerObj.serialize(proxy, 'Category'));
    }
    
    static testMethod void testSerializingConfiguration() {
        Serializer serializerObj = new XmlSerializer();
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
        '<?xml version="1.0" encoding="UTF-8"?>' + 
        '<configurations>' +
		   '<added>' +
		      '<configuration>' +
		         '<name>A test configuration</name>' +
		         '<one_sentence>false</one_sentence>' +
		         '<is_primary>true</is_primary>' +
		         '<auto_responding>true</auto_responding>' +
		         '<language>English</language>' +
		         '<chars_threshold>80</chars_threshold>' +
		         '<callback>https://anyapi.anydomain.com/processed/docs.json</callback>' +
		         '<document>' +
		            '<concept_topics_limit>5</concept_topics_limit>' + 
		            '<query_topics_limit>5</query_topics_limit>' +
		            '<named_entities_limit>5</named_entities_limit>' +
		            '<user_entities_limit>5</user_entities_limit>' +
		            '<themes_limit>0</themes_limit>' +
		            '<entity_themes_limit>5</entity_themes_limit>' +
		            '<phrases_limit>0</phrases_limit>' +
		            '<summary_limit>0</summary_limit>' +
		         '</document>' +
		         '<collection>' +
		            '<facets_limit>15</facets_limit>' +
		            '<facet_atts_limit>5</facet_atts_limit>' +
		            '<concept_topics_limit>5</concept_topics_limit>' + 
		            '<query_topics_limit>5</query_topics_limit>' +
		            '<named_entities_limit>5</named_entities_limit>' +
		            '<themes_limit>0</themes_limit>' +
		         '</collection>' +
		      '</configuration>' +
		      '<configuration>' +
		      	 '<template>cloned_config_id</template>' +
		         '<name>Cloned configuration</name>' +
		         '<one_sentence>true</one_sentence>' +
		         '<is_primary>true</is_primary>' +
		         '<auto_responding>true</auto_responding>' +
		      '</configuration>' +
		   '</added>' +
		   '<removed>' +
		      '<configuration>45699836</configuration>' +
		   '</removed>' +
		'</configurations>';
        
        System.assertEquals(expectedResult, serializerObj.serialize(proxy, 'Configuration'));
    }
    
    static testMethod void testSerializingDocument() {
        Serializer serializerObj = new XmlSerializer();
        Document c1 = new Document();
        c1.setId('6F9619FF8B86D011B42D00CF4FC964FF');
        c1.setText('A chunk of text for processing');
        
        String expectedResult = 
        '<?xml version="1.0" encoding="UTF-8"?>' + 
        '<document>' +
           '<id>6F9619FF8B86D011B42D00CF4FC964FF</id>' +
           '<text>A chunk of text for processing</text>' +
        '</document>';
        
        System.assertEquals(expectedResult, serializerObj.serialize(c1, 'Document'));
    }
    
    static testMethod void testSerializingDocumentList() {
        Serializer serializerObj = new XmlSerializer();
        List<Document> documents = new List<Document>(); 
        Document c1 = new Document();
        c1.setId('6F9619FF8B86D011B42D00CF4FC964FF');
        c1.setText('A chunk of text for processing');
        documents.add(c1);
        documents.add(new Document('7F9619FF8B86D011B42D00CF4FC964FF', 'A chunk of text for processing'));
        
        String expectedResult = 
        '<?xml version="1.0" encoding="UTF-8"?>' + 
        '<documents>' +
	        '<document>' +
	           '<id>6F9619FF8B86D011B42D00CF4FC964FF</id>' +
	           '<text>A chunk of text for processing</text>' +
	        '</document>' +
	        '<document>' +
               '<id>7F9619FF8B86D011B42D00CF4FC964FF</id>' +
               '<text>A chunk of text for processing</text>' +
            '</document>' + 
	    '</documents>';
        
        System.assertEquals(expectedResult, serializerObj.serialize(documents, 'DocumentList'));
    }
    
    static testMethod void testSerializingCollection() {
        Serializer serializerObj = new XmlSerializer();
        Collection c1 = new Collection();
        c1.setId('6F9619FF8B86D011B42D00CF4FC964FF');
        List<String> documents = new List<String>();
        documents.add('A chunk of text for processing');
        documents.add('A chunk of text for processing');
        documents.add('A chunk of text for processing');
        c1.setDocuments(documents);
        
        String expectedResult = 
        '<?xml version="1.0" encoding="UTF-8"?>' + 
        '<collection>' +
		   '<id>6F9619FF8B86D011B42D00CF4FC964FF</id>' +
		   '<documents>' +
		      '<document>A chunk of text for processing</document>' +
		      '<document>A chunk of text for processing</document>' +
		      '<document>A chunk of text for processing</document>' +
		   '</documents>' +
		'</collection>';
        
        System.assertEquals(expectedResult, serializerObj.serialize(c1, 'Collection'));
    }
	
	static testMethod void testDeserializingBlacklistItem() {
		Serializer serializerObj = new XmlSerializer();
		String source = '<?xml version="1.0" encoding="UTF-8"?>' + 
				        '<blacklist>' +
				            '<item>Filter 1</item>' +
				            '<item>Filter 2</item>' +
				            '<item>Filter 3</item>' +
				        '</blacklist>'; 
						
		List<String> blacklistItemList = (List<String>)serializerObj.deserialize(source, 'BlacklistItem');
		System.assert(null != blacklistItemList); 
        System.assertEquals(3, blacklistItemList.size());
	}
	
	static testMethod void testDeserializingQuery() {
		Serializer serializerObj = new XmlSerializer();
		String source = '<?xml version="1.0" encoding="UTF-8"?>' + 
				        '<queries>' +
				            '<query>' +
				                '<name>Topic 1</name>' +
				                '<query>Query itself 1</query>' +
				            '</query>' +
				            '<query>' +
				                '<name>Topic 2</name>' +
				                '<query>Query itself 2</query>' +
				            '</query>' +
				        '</queries>'; 
				        
        List<Query> queryList = (List<Query>)serializerObj.deserialize(source, 'Query');
        System.assert(null != queryList);
        System.assertEquals(2, queryList.size());
        System.assert('Topic 1'.equals(queryList.get(0).getName()) && 'Query itself 1'.equals(queryList.get(0).getQuery()) 
        				|| 'Topic 2'.equals(queryList.get(0).getName()) && 'Query itself 2'.equals(queryList.get(0).getQuery()));
        System.assert('Topic 1'.equals(queryList.get(1).getName()) && 'Query itself 1'.equals(queryList.get(1).getQuery()) 
        				|| 'Topic 2'.equals(queryList.get(1).getName()) && 'Query itself 2'.equals(queryList.get(1).getQuery()));
	}
	
	static testMethod void testDeserializingEntity() {
		Serializer serializerObj = new XmlSerializer();
		String source = '<?xml version="1.0" encoding="UTF-8"?>' + 
						'<entities>' + 
						   '<entity>' +
						      '<name>chair</name>' +
						      '<type>furniture</type> ' +
						   '</entity>' +
						'</entities>';
		
		List<UserEntity> entityList = (List<UserEntity>)serializerObj.deserialize(source, 'UserEntity');
		System.assert(null != entityList); 
        System.assertEquals(1, entityList.size());
        System.assertEquals('chair', entityList.get(0).getName());
        System.assertEquals('furniture', entityList.get(0).getType());
	}
	
	static testMethod void testDeserializingSentimentPhrase() {
        Serializer serializerObj = new XmlSerializer();
        String source = '<?xml version="1.0" encoding="UTF-8"?>' + 
                        '<phrases>' + 
                           '<phrase>' +
                              '<title>chair</title>' +
                              '<weight>0.3</weight> ' +
                           '</phrase>' +
                        '</phrases>';
        
        List<SentimentPhrase> sentimentPhraseList = (List<SentimentPhrase>)serializerObj.deserialize(source, 'SentimentPhrase');
        System.assert(null != sentimentPhraseList); 
        System.assertEquals(1, sentimentPhraseList.size());
        System.assertEquals('chair', sentimentPhraseList.get(0).getTitle());
        System.assertEquals(0.3d, sentimentPhraseList.get(0).getWeight());
    }
	
	static testMethod void testDeserializingCategory() {
		Serializer serializerObj = new XmlSerializer();
		String source = '<?xml version="1.0" encoding="UTF-8"?>' + 
						'<categories>' + 
						   '<category>' + 
						      '<name>Feature: Cloud service</name>' + 
						      '<weight>0.75</weight>' + 
						      '<samples>' + 
						         '<sample>Amazon</sample>' + 
						         '<sample>EC2</sample>' + 
						      '</samples>' + 
						   '</category>' + 
						'</categories>';
		
		List<Category> categoryList = (List<Category>)serializerObj.deserialize(source, 'Category');
		System.assert(null != categoryList); 
        System.assertEquals(1, categoryList.size());
        System.assertEquals('Feature: Cloud service', categoryList.get(0).getName());
        System.assertEquals(0.75, categoryList.get(0).getWeight());
        System.assertEquals(2, categoryList.get(0).getSamples().size());
	}
	 
	static testMethod void testDeserializingConfiguration() {
		Serializer serializerObj = new XmlSerializer();
		String source = '<?xml version="1.0" encoding="UTF-8"?>' +
	                    '<configurations>' +
						   '<configuration>' +
						      '<config_id>23498367</config_id>' +
						      '<name>A test configuration</name>' +
						      '<is_primary>true</is_primary>' +
						      '<auto_responding>true</auto_responding>' +
						      '<language>English</language>' + 
						      '<chars_threshold>80</chars_threshold>' +
						      '<callback>callback_url</callback>' +
						      '<document>' +
						         '<concept_topics_limit>5</concept_topics_limit>' +
						         '<query_topics_limit>5</query_topics_limit>' +
						         '<named_entities_limit>5</named_entities_limit>' +
						         '<user_entities_limit>5</user_entities_limit>' +
						         '<entity_themes_limit>5</entity_themes_limit>' +
						         '<themes_limit>5</themes_limit>' +
						         '<phrases_limit>5</phrases_limit>' +
						         '<summary_limit>0</summary_limit>' +
						      '</document>' +
						      '<collection>' +
						         '<facets_limit>15</facets_limit>' +
						         '<facet_atts_limit>5</facet_atts_limit>' +
						         '<concept_topics_limit>5</concept_topics_limit>' + 
					             '<query_topics_limit>5</query_topics_limit>' +
					             '<named_entities_limit>5</named_entities_limit>' +
					             '<themes_limit>0</themes_limit>' +
						      '</collection>' +
						   '</configuration>' +
						'</configurations>';
						
		List<Configuration> configurationList = (List<Configuration>)serializerObj.deserialize(source, 'Configuration');
		System.assert(null != configurationList);
		System.assertEquals(1, configurationList.size());
		
		Configuration configurationObj = configurationList.get(0);
		System.assertEquals('23498367', configurationObj.getConfigId());
		System.assertEquals('A test configuration', configurationObj.getName());
		System.assert(configurationObj.isPrimary());
		System.assert(configurationObj.isAutoResponding());
		System.assertEquals('English', configurationObj.getLanguage());
		System.assertEquals(80, configurationObj.getCharsThreshold());
		System.assertEquals('callback_url', configurationObj.getCallbackUrl());
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
		Serializer serializerObj = new XmlSerializer();
		String source = '<?xml version="1.0" encoding="UTF-8"?>' +
	                    '<status>' + 
						   '<service_status>online</service_status>' +
						   '<api_version>2.0</api_version>' +
						   '<service_version>1.0.2.63</service_version>' +
						   '<supported_encoding>UTF-8</supported_encoding>' +
						   '<supported_compression>gzip</supported_compression>' +
						   '<supported_languages>' +
						      '<language>English</language>' +
						      '<language>French</language>' +
						   '</supported_languages>' +
						'</status>';
						
		ServiceStatus serviceStatusObj = (ServiceStatus)serializerObj.deserialize(source, 'ServiceStatus');
		System.assert( null != serviceStatusObj );
		System.assertEquals('online', serviceStatusObj.getServiceStatus());
		System.assertEquals('2.0', serviceStatusObj.getApiVersion());
		System.assertEquals('1.0.2.63', serviceStatusObj.getServiceVersion());
		System.assertEquals('UTF-8', serviceStatusObj.getSupportedEncoding());
		System.assertEquals('gzip', serviceStatusObj.getSupportedCompression());
		System.assertEquals(2, serviceStatusObj.getSupportedLanguages().size());
	}
	
	static testMethod void testDeserializingSubscription() {
		Serializer serializerObj = new XmlSerializer();
		String source = '<?xml version="1.0" encoding="UTF-8"?>' +
	                    '<subscription>' +
	                       '<name>name</name>' +
						   '<status>active</status>' +
						   '<priority>normal</priority>' +
						   '<expiration_date>1293883200</expiration_date>' +
					       '<calls_balance>87</calls_balance>' +
						   '<calls_limit>100</calls_limit>' +
						   '<calls_limit_interval>60</calls_limit_interval>' +
						   '<docs_balance>49897</docs_balance>' +
						   '<docs_limit>0</docs_limit>' +
						   '<docs_limit_interval>0</docs_limit_interval>' +
						   '<configurations_limit>10</configurations_limit>' +
						   '<blacklist_limit>100</blacklist_limit>' +
						   '<categories_limit>100</categories_limit>' +
						   '<queries_limit>100</queries_limit>' +
						   '<entities_limit>1000</entities_limit>' +
						   '<sentiment_limit>1000</sentiment_limit>' +
						   '<characters_limit>8192</characters_limit>' +
						   '<batch_limit>1</batch_limit>' +
						   '<collection_limit>10</collection_limit>' +
						   '<auto_response_limit>2</auto_response_limit>' +
						   '<processed_batch_limit>100</processed_batch_limit>' +
						   '<callback_batch_limit>100</callback_batch_limit>' +
						   '<limit_type>type limit</limit_type>' +
						'</subscription>';
						
		Subscription subscriptionObj = (Subscription)serializerObj.deserialize(source, 'Subscription');
		System.assert(null != subscriptionObj);
		System.assertEquals('name', subscriptionObj.getName());
		System.assertEquals('active', subscriptionObj.getStatus());
		System.assertEquals('normal', subscriptionObj.getPriority());
		System.assertEquals(1293883200, subscriptionObj.getExpirationDate());
		System.assertEquals(87, subscriptionObj.getCallsBalance());
		System.assertEquals(100, subscriptionObj.getCallsLimit());
		System.assertEquals(60, subscriptionObj.getCallsLimitInterval());
		System.assertEquals(49897, subscriptionObj.getDocsBalance()); 
		System.assertEquals(0, subscriptionObj.getDocsLimit()); 
		System.assertEquals(0, subscriptionObj.getDocsLimitInterval());
		System.assertEquals(10, subscriptionObj.getConfigurationsLimit());
		System.assertEquals(100, subscriptionObj.getBlacklistLimit());
		System.assertEquals(100, subscriptionObj.getCategoriesLimit());
		System.assertEquals(100, subscriptionObj.getQueriesLimit());
		System.assertEquals(1000, subscriptionObj.getEntitiesLimit());
		System.assertEquals(1000, subscriptionObj.getSentimentLimit());
		System.assertEquals(8192, subscriptionObj.getCharactersLimit());
		System.assertEquals(1, subscriptionObj.getBatchLimit());
		System.assertEquals(10, subscriptionObj.getCollectionLimit());
		System.assertEquals(2, subscriptionObj.getAutoResponseLimit());
		System.assertEquals(100, subscriptionObj.getProcessedBatchLimit());
		System.assertEquals(100, subscriptionObj.getCallbackBatchLimit());
		System.assertEquals('type limit', subscriptionObj.getlimitType());
	} 
	
	static testMethod void testDeserializingDocumentAnalyticData() {
		Serializer serializerObj = new XmlSerializer();
		String source = '<?xml version="1.0" encoding="UTF-8"?>' +
						'<document>' +
						   '<config_id>23498367</config_id>' +
						   '<id>6F9619FF8B86D011B42D00CF4FC964FF</id>' +
						   '<status>PROCESSED</status>' +
						   '<sentiment_score>0.2398756</sentiment_score>' +
						   '<summary>Summary of the document’s text.</summary>' +
						   '<themes>' +
						      '<theme>' +
						         '<evidence>1</evidence>' +
						         '<is_about>true</is_about>' +
						         '<strength_score>0.0</strength_score>' +
						         '<sentiment_score>0.0</sentiment_score>' +
						         '<title>republican moderates</title>' +
						      '</theme>' +
						   '</themes>' +
						   '<entities>' +
						      '<entity>' +
						         '<evidence>0</evidence>' +
						         '<is_about>true</is_about>' +
						         '<confident>true</confident>' +
						         '<title>WASHINGTON</title>' +
						         '<sentiment_score>1.0542796</sentiment_score>' +
						         '<type>named</type>' +
						         '<entity_type>Place</entity_type>' +
						         '<themes>' +
						            '<theme>' +
						               '<evidence>1</evidence>' +
						               '<is_about>true</is_about>' +
						               '<strength_score>0.0</strength_score>' +
						               '<sentiment_score>0.0</sentiment_score>' +
						               '<title>republican moderates</title>' +
						            '</theme>' +
						         '</themes>' +
						      '</entity>' +
						   '</entities>' +
						   '<topics>' +
						      '<topic>' +
						         '<title>Something</title>' +
						         '<hitcount>0</hitcount>' +
						         '<sentiment_score>0.6133076</sentiment_score>' +
						         '<strength_score>0.6133076</strength_score>' +
						         '<type>concept</type>' +
						      '</topic>' +
						   '</topics>' +
						   '<phrases>' +
						      '<phrase>' +
						         '<title>Something</title>' +
						         '<sentiment_score>0.6133076</sentiment_score>' +
						         '<is_negated>true</is_negated>' +
						         '<negating_phrase>not</negating_phrase>' +
						      '</phrase>' +
						   '</phrases>' +
						'</document>';
		
		DocAnalyticData analyticData = (DocAnalyticData)serializerObj.deserialize(source, 'DocAnalyticData');
		System.assert(null != analyticData);
		System.assertEquals('23498367', analyticData.getConfigId());
		System.assertEquals('6F9619FF8B86D011B42D00CF4FC964FF', analyticData.getId());
		System.assertEquals('PROCESSED', analyticData.getStatus());
		System.assertEquals(0.2398756, analyticData.getSentimentScore());
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
		System.assertEquals(0.6133076, topicObj.getStrengthScore());
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
		Serializer serializerObj = new XmlSerializer();
		String source = '<?xml version="1.0" encoding="UTF-8"?>' +
						'<collection>' +
						   '<config_id>23498367</config_id>' +
						   '<id>6F9619FF8B86D011B42D00CF4FC964FF</id>' +
						   '<status>PROCESSED</status>' +
						   '<facets>' +
						      '<facet>' +
						         '<label>Something</label>' +
						         '<count>10</count>' +
						         '<negative_count>2</negative_count>' +
						         '<positive_count>1</positive_count>' +
						         '<neutral_count>7</neutral_count>' +
						         '<attributes>' +
						            '<attribute>' +
						               '<label>Attribute</label>' +
						               '<count>5</count>' +
						            '</attribute>' +
						         '</attributes>' +
						      '</facet>' +
						   '</facets>' +
						   '<themes>' +
						      '<theme>' +
						         '<phrases_count>1</phrases_count>' +
						         '<themes_count>1</themes_count>' +
						         '<sentiment_score>0.0</sentiment_score>' +
						         '<title>republican moderates</title>' +
						      '</theme>' +
						   '</themes>' +
						   '<entities>' +
						      '<entity>' +
						         '<title>WASHINGTON</title>' +
						         '<type>named</type>' +
						         '<entity_type>Place</entity_type>' +
						         '<count>1</count>' +
						         '<negative_count>1</negative_count>' +
						         '<neutral_count>1</neutral_count>' +
						         '<positive_count>1</positive_count>' +
						      '</entity>' +
						   '</entities>' +
						   '<topics>' +
						      '<topic>' +
						         '<title>Something</title>' +
						         '<hitcount>0</hitcount>' +
						         '<sentiment_score>0.6133076</sentiment_score>' +
						         '<type>concept</type>' +
						      '</topic>' +
						   '</topics>' +
						'</collection>';
		
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
}