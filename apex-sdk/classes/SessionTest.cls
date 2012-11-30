@isTest
private class SessionTest {
    static testMethod void testInitializingSession() {
    	Session sessionObj = Session.createSession('key', 'secret');
        System.assert(null != sessionObj);
        
        try {
        	sessionObj = Session.createSession('key', 'secret', null, '');
        	System.assert(false);
        } catch (Exception ex) {
        	System.assert(true);
        }
        
        sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        System.assert(null != sessionObj);
         
        try {
            sessionObj.registerSerializer(null);
            System.assert(false); 
        } catch (Exception ex) {
            System.assert(true);
        }
        
        sessionObj.setCallbackHandler(new CallbackHandlerImpl());
    }
    
    static testMethod void testCategoryUpdateProxy() {
    	Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
    	UpdateProxy proxy = sessionObj.createCategoriesUpdateProxy();
    	proxy.addItem(new Category('new category', (0.2).doubleValue()));
    	proxy.updateItem(new Category('updated category', (0.0).doubleValue()));
    	proxy.removeItem(new Category('removed category', (0.0).doubleValue()));
    	
    	System.assertEquals(2/*added + updated*/, ((CategoryUpdateProxy)proxy).getAdded().size());
    	System.assertEquals(1, ((CategoryUpdateProxy)proxy).getRemoved().size());
    	
    	System.assert(null != sessionObj.updateCategories((CategoryUpdateProxy)proxy));
    }
    
    static testMethod void testQueryUpdateProxy() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        UpdateProxy proxy = sessionObj.createQueriesUpdateProxy();
        proxy.addItem(new Query('new query', 'query body'));
        proxy.updateItem(new Query('updated query', 'query body'));
        proxy.removeItem(new Query('removed query', 'query body'));
        
        System.assertEquals(2/*added + updated*/, ((QueryUpdateProxy)proxy).getAdded().size());
        System.assertEquals(1, ((QueryUpdateProxy)proxy).getRemoved().size());
        
        System.assert(null != sessionObj.updateQueries((QueryUpdateProxy)proxy));
    }
    
    static testMethod void testBlacklistUpdateProxy() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        UpdateProxy proxy = sessionObj.createBlacklistUpdateProxy();
        proxy.addItem(new BlacklistItem('new filter'));
        proxy.updateItem(new BlacklistItem('updated filter'));
        proxy.removeItem(new BlacklistItem('removed filter'));
        
        System.assertEquals(2/*added + updated*/, ((BlacklistUpdateProxy)proxy).getAdded().size());
        System.assertEquals(1, ((BlacklistUpdateProxy)proxy).getRemoved().size());
        
        System.assert(null != sessionObj.updateBlacklist((BlacklistUpdateProxy)proxy));
    }
    
    static testMethod void testConfigurationUpdateProxy() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        UpdateProxy proxy = sessionObj.createConfigurationsUpdateProxy();
        
        Configuration config = new Configuration(); 
        config.setConfigId('123456');
	    config.setName('config_name'); 
	    config.setIsPrimary(true);
	    config.setAutoResponding(true);
	    config.setLanguage('English'); 
	    config.setCharsThreshold(10);
	    config.setDocConceptTopicsLimit(10);
	    config.setDocQueryTopicsLimit(10);
	    config.setDocUserEntitiesLimit(10);
	    config.setDocNamedEntitiesLimit(10);
	    config.setDocThemesLimit(10);
	    config.setEntityThemesLimit(10);
	    config.setPhrasesLimit(10);
	    config.setSummaryLimit(5);
	    config.setFacetsLimit(5);
	    config.setFacetAttsLimit(5);
	    config.setCollConceptTopicsLimit(10);
	    config.setCollQueryTopicsLimit(10);
	    config.setCollNamedEntitiesLimit(10);
	    config.setCollThemesLimit(10);
        proxy.addItem(config);
        
        Configuration removedConfig = new Configuration(); 
        removedConfig.setConfigId('45644');
        proxy.removeItem(removedConfig);
        
        proxy.updateItem(config);
        
        System.assertEquals(2/*added + updated*/, ((ConfigurationUpdateProxy)proxy).getAdded().size());
        System.assertEquals(1, ((ConfigurationUpdateProxy)proxy).getRemoved().size());
        
        System.assert(null != sessionObj.updateConfigurations((ConfigurationUpdateProxy)proxy));
    }
    
    static testMethod void testEntitiesUpdateProxy() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        UpdateProxy proxy = sessionObj.createEntitiesUpdateProxy();
        proxy.addItem(new UserEntity('new entity', 'type'));
        proxy.updateItem(new UserEntity('updated entity', 'type'));
        proxy.removeItem(new UserEntity('removed entity', 'type'));
        
        System.assertEquals(2/*added + updated*/, ((UserEntityUpdateProxy)proxy).getAdded().size());
        System.assertEquals(1, ((UserEntityUpdateProxy)proxy).getRemoved().size());
        
        System.assert(null != sessionObj.updateEntities((UserEntityUpdateProxy)proxy));
    }
    
    static testMethod void testReceivingAnalyticServiceStatus() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        ServiceStatus status = sessionObj.getStatus();
        System.assert(null != status);
    }
    
    static testMethod void testReceivingSubscription() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        Subscription subscriptionObj = sessionObj.verifySubscription();
        System.assert(null != subscriptionObj);
    }
    
    static testMethod void testReceivingBlacklist() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        List<String> blacklist = sessionObj.getBlacklist();
        System.assert(null != blacklist);
    }
    
    static testMethod void testReceivingCategories() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        List<Category> categories = sessionObj.getCategories();
        System.assert(null != categories);
    }
    
    static testMethod void testReceivingConfigurations() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        List<Configuration> configurations = sessionObj.getConfigurations();
        System.assert(null != configurations);
    }
    
    static testMethod void testReceivingQueries() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        List<Query> queries = sessionObj.getQueries();
        System.assert(null != queries);
    }
    
    static testMethod void testReceivingEntities() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        List<UserEntity> entities = sessionObj.getEntities();
        System.assert(null != entities);
    }
     
    static testMethod void testReceivingProcessedDocuments() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        List<DocAnalyticData> analyticData = sessionObj.getProcessedDocuments();
        System.assert(null != analyticData);
    }
    
    static testMethod void testReceivingProcessedCollections() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        List<CollAnalyticData> analyticData = sessionObj.getProcessedCollections();
        System.assert(null != analyticData);
    }
    
    static testMethod void testReceivingDocument() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        DocAnalyticData analyticData = sessionObj.getDocument('1234567');
        System.assert(null == analyticData);
    }
    
    static testMethod void testReceivingCollection() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        CollAnalyticData analyticData = sessionObj.getCollection('1234567');
        System.assert(null == analyticData);
    }
    
    static testMethod void testCancelingDocument() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        System.assert(null != sessionObj.cancelDocument('1234567'));
    }
    
    static testMethod void testCancelingCollection() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        System.assert(null != sessionObj.cancelCollection('1234567'));
    }
    
    static testMethod void testQueueingDocument() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        System.assert(null != sessionObj.queueDocument(new Document('1234356', 'Some text')));
    }
    
    static testMethod void testQueueingDocumentBatch() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        List<Document> docs = new List<Document>();
        docs.add(new Document('1234356', 'Some text 1'));
        Document d = new Document();
        d.setId('2234356'); 
        d.setText('Some text 2'); 
        docs.add(d);
        System.assert(null != sessionObj.queueBatchOfDocuments(docs));
    }
    
    static testMethod void testQueueingCollection() {
        Session sessionObj = Session.createSession('key', 'secret', new XmlSerializer());
        List<String> docs = new List<String>();
        docs.add('Some text 1');
        docs.add('Some text 2');
        Collection coll = new Collection();
        coll.setId('123345');
        coll.setDocuments(docs);
        System.assert(null != sessionObj.queueCollection(coll));
    }
}