public class XmlSerializer extends Serializer {
	/**
	 * static fields are located in the end of the class
	 */
	
	public override String getType() {
		return SERIALIZER_TYPE;
	} 
	
	public override String serialize(Object obj, String className) {
		if ('BlacklistItem'.equals(className)) {
			return generateXmlFromBlacklistItems(obj);
		} else if ('Query'.equals(className)) {
			return generateXmlFromQueries(obj);
		} else if ('UserEntity'.equals(className)) {
			return generateXmlFromUserEntities(obj);
		} else if ('SentimentPhrase'.equals(className)) {
            return generateXmlFromSentimentPhrases(obj);
        } else if ('Category'.equals(className)) {
			return generateXmlFromCategories(obj);
		} else if ('Configuration'.equals(className)) {
			return generateXmlFromConfigurations(obj);
		} else if ('Document'.equals(className)) {
			return generateXmlFromDocument(obj);
		} else if ('DocumentList'.equals(className)) {
            return generateXmlFromDocumentList(obj);
        } else if ('Collection'.equals(className)) {
            return generateXmlFromCollection(obj);
        } else {
			throw new IllegalStateException('Unsupported class: ' + className);
		}
		return null;
	} 
	
	private String generateXmlFromBlacklistItems(Object obj) {
		XmlStreamWriter w = getXmlStreamWriter();
        w.writeStartElement(null, BLACK_LIST_FIELD, null);

        //removed
        writeListOfElements(ADDED_FIELD, ITEM_FIELD, ((BlacklistUpdateProxy)obj).getAdded(), w);
        //removed
        writeListOfElements(REMOVED_FIELD, ITEM_FIELD, ((BlacklistUpdateProxy)obj).getRemoved(), w);
        
        w.writeEndElement();// end black list
        return getResultAndCloseXmlStreamWriter(w);
	}
	
	private XmlStreamWriter getXmlStreamWriter() {
        XmlStreamWriter w = new XmlStreamWriter();
        w.writeStartDocument('UTF-8', '1.0');
        return w;
    }
    
    private void writeListOfElements(String rootElementName, String childElementName, List<String> elements, XmlStreamWriter w) {
        if ( null != elements && !elements.isEmpty() ) {
            w.writeStartElement(null, rootElementName, null);
            for ( String element : elements ) {
                writeElement(childElementName, element, w);
            }   
            w.writeEndElement();
        } else {
    		w.writeEmptyElement(null, rootElementName, null);
    	}   
    }
    
    private void writeElement(String tagName, String elementValue, XmlStreamWriter w) {
    	if (null != elementValue && !''.equals(elementValue)) {
	        w.writeStartElement(null, tagName, null);
	        w.writeCharacters(elementValue);
	        w.writeEndElement();
    	} else {
    		w.writeEmptyElement(null, tagName, null);
    	}
    }
    
    private void writeElementIfExist(String tagName, String elementValue, XmlStreamWriter w) {
    	if (null != elementValue && !''.equals(elementValue)) {
	        w.writeStartElement(null, tagName, null);
	        w.writeCharacters(elementValue);
	        w.writeEndElement();
    	}
    }
    
	private String getResultAndCloseXmlStreamWriter(XmlStreamWriter w) {
        w.writeEndDocument();
        String xmlOutput = w.getXmlString();
        w.close();
        return xmlOutput;
    }
    
	private String generateXmlFromQueries(Object obj) {
		XmlStreamWriter w = getXmlStreamWriter();
        w.writeStartElement(null, QUERIES_FIELD, null);
        List<Query> addedQueries = ((QueryUpdateProxy)obj).getAdded();
        List<String> removedQueries = ((QueryUpdateProxy)obj).getRemoved();
        
        // added elements
        if ( null != addedQueries && !addedQueries.isEmpty() ) {
            w.writeStartElement(null, ADDED_FIELD, null);
            for ( Query queryObj : addedQueries ) {
                w.writeStartElement(null, QUERY_FIELD, null);
                writeElement(NAME_FIELD, queryObj.getName(), w);
                writeElement(QUERY_FIELD, queryObj.getQuery(), w);
                w.writeEndElement();//end query
            }
            w.writeEndElement();// end added elements
        }
        
        // removed elements
        writeListOfElements(REMOVED_FIELD, QUERY_FIELD, removedQueries, w);
        
        w.writeEndElement();// end queries
        return getResultAndCloseXmlStreamWriter(w);
	}
	
	private String generateXmlFromUserEntities(Object obj) {
		XmlStreamWriter w = getXmlStreamWriter();
        w.writeStartElement(null, ENTITIES_FIELD, null);
        List<UserEntity> addedEntities = ((UserEntityUpdateProxy)obj).getAdded();
        List<String> removedEntities = ((UserEntityUpdateProxy)obj).getRemoved();
        
        // added elements
        if ( null != addedEntities && !addedEntities.isEmpty() ) {
            w.writeStartElement(null, ADDED_FIELD, null);
            for ( UserEntity entityObj : addedEntities ) {
                w.writeStartElement(null, ENTITY_FIELD, null);
                writeElement(NAME_FIELD, entityObj.getName(), w);
                writeElement(TYPE_FIELD, entityObj.getType(), w);
                w.writeEndElement();//end entity
            }
            w.writeEndElement();// end added elements
        }
        
        // removed elements
        writeListOfElements(REMOVED_FIELD, ENTITY_FIELD, removedEntities, w);
        
        w.writeEndElement();// end entities
        return getResultAndCloseXmlStreamWriter(w);
	}
	
	private String generateXmlFromSentimentPhrases(Object obj) {
        XmlStreamWriter w = getXmlStreamWriter();
        w.writeStartElement(null, PHRASES_FIELD, null);
        List<SentimentPhrase> addedSentimentPhrase = ((SentimentPhraseUpdateProxy)obj).getAdded();
        List<String> removedSentimentPhrase = ((SentimentPhraseUpdateProxy)obj).getRemoved();
        
        // added elements
        if ( null != addedSentimentPhrase && !addedSentimentPhrase.isEmpty() ) {
            w.writeStartElement(null, ADDED_FIELD, null);
            for ( SentimentPhrase sentimentPhraseObj : addedSentimentPhrase ) {
                w.writeStartElement(null, PHRASE_FIELD, null);
                writeElement(TITLE_FIELD, sentimentPhraseObj.getTitle(), w);
                writeElement(WEIGHT_FIELD, String.valueOf(sentimentPhraseObj.getWeight()), w);
                w.writeEndElement();//end entity
            }
            w.writeEndElement();// end added elements
        }
        
        // removed elements
        writeListOfElements(REMOVED_FIELD, PHRASE_FIELD, removedSentimentPhrase, w);
        
        w.writeEndElement();// end entities
        return getResultAndCloseXmlStreamWriter(w);
    }
	 
	private String generateXmlFromCategories(Object obj) {
		XmlStreamWriter w = getXmlStreamWriter();
        w.writeStartElement(null, CATEGORIES_FIELD, null);
        List<Category> addedCategories = ((CategoryUpdateProxy)obj).getAdded();
        List<String> removedCategories = ((CategoryUpdateProxy)obj).getRemoved();
        
        //added elements
        if ( null != addedCategories && !addedCategories.isEmpty() ) {
            w.writeStartElement(null, ADDED_FIELD, null);
            for ( Category categoryObj : addedCategories ) {
                w.writeStartElement(null, CATEGORY_FIELD, null);
                writeElement(NAME_FIELD, categoryObj.getName(), w);
                writeElement(WEIGHT_FIELD, String.valueOf(categoryObj.getWeight()), w);
                
                //write samples
                writeListOfElements(SAMPLES_FIELD, SAMPLE_FIELD, categoryObj.getSamples(), w);
                
                w.writeEndElement();//end category
            }
            w.writeEndElement();//end added elements
        }
        
        //removed elements
        writeListOfElements(REMOVED_FIELD, CATEGORY_FIELD, removedCategories, w);
        
        w.writeEndElement();//end categories 
        return getResultAndCloseXmlStreamWriter(w);
	}
	
	private String generateXmlFromConfigurations(Object obj) {
		XmlStreamWriter w = getXmlStreamWriter();
        w.writeStartElement(null, CONFIGURATIONS_FIELD, null);
        List<Configuration> addedConfigurations = ((ConfigurationUpdateProxy)obj).getAdded();
        List<String> removedConfigurations = ((ConfigurationUpdateProxy)obj).getRemoved();
        
        // added elements
        if ( null != addedConfigurations && !addedConfigurations.isEmpty() ) {
            w.writeStartElement(null, ADDED_FIELD, null);
        
            for ( Configuration configurationObj : addedConfigurations ) {
                w.writeStartElement(null, CONFIGURATION_FIELD, null);
                
                writeElementIfExist(TEMPLATE_FIELD, configurationObj.getTemplate(), w);
                writeElementIfExist(NAME_FIELD, configurationObj.getName(), w);
                writeElementIfExist(ONE_SENTENCE_FIELD, String.valueOf(configurationObj.isOneSentence()), w);
                writeElementIfExist(IS_PRIMARY_FIELD, String.valueOf(configurationObj.isPrimary()), w);
                writeElementIfExist(AUTO_RESPONDING_FIELD, String.valueOf(configurationObj.isAutoResponding()), w);
                writeElementIfExist(LANGUAGE_FIELD, configurationObj.getLanguage(), w);
                writeElementIfExist(CHARS_THRESHOLD_FIELD, String.valueOf(configurationObj.getCharsThreshold()), w);
                writeElementIfExist(CALLBACK_FIELD, configurationObj.getCallbackUrl(), w);
                
                if (configurationObj.isDocumentParametersSetted()) {
	                w.writeStartElement(null, DOCUMENT_FIELD, null);
	                writeElementIfExist(CONCEPT_TOPICS_LIMIT_FIELD, String.valueOf(configurationObj.getDocConceptTopicsLimit()), w);
	                writeElementIfExist(QUERY_TOPICS_LIMIT_FIELD, String.valueOf(configurationObj.getDocQueryTopicsLimit()), w);
	                writeElementIfExist(NAMED_ENTITIES_LIMIT_FIELD, String.valueOf(configurationObj.getDocNamedEntitiesLimit()), w);
	                writeElementIfExist(USER_ENTITIES_LIMIT_FIELD, String.valueOf(configurationObj.getDocUserEntitiesLimit()), w);
	                writeElementIfExist(THEMES_LIMIT_FIELD, String.valueOf(configurationObj.getDocThemesLimit()), w);
	                writeElementIfExist(ENTITY_THEMES_LIMIT_FIELD, String.valueOf(configurationObj.getEntityThemesLimit()), w);
	                writeElementIfExist(PHRASES_LIMIT_FIELD, String.valueOf(configurationObj.getPhrasesLimit()), w);
	                writeElementIfExist(SUMMARY_LIMIT_FIELD, String.valueOf(configurationObj.getSummaryLimit()), w);
	                w.writeEndElement();//end document section
                }
                
                if (configurationObj.isCollectionParametersSetted()) {
	                w.writeStartElement(null, COLLECTION_FIELD, null);
	                writeElementIfExist(FACETS_LIMIT_FIELD, String.valueOf(configurationObj.getFacetsLimit()), w);
	                writeElementIfExist(FACET_ATTS_LIMIT_FIELD, String.valueOf(configurationObj.getFacetAttsLimit()), w);
	                writeElementIfExist(CONCEPT_TOPICS_LIMIT_FIELD, String.valueOf(configurationObj.getCollConceptTopicsLimit()), w);
	                writeElementIfExist(QUERY_TOPICS_LIMIT_FIELD, String.valueOf(configurationObj.getCollQueryTopicsLimit()), w);
	                writeElementIfExist(NAMED_ENTITIES_LIMIT_FIELD, String.valueOf(configurationObj.getCollNamedEntitiesLimit()), w);
	                writeElementIfExist(THEMES_LIMIT_FIELD, String.valueOf(configurationObj.getCollThemesLimit()), w);
	                w.writeEndElement();//end collection section
                }
                
                w.writeEndElement();//end configuration
            }
            w.writeEndElement();// end added elements
        }
        
        // removed elements
        writeListOfElements(REMOVED_FIELD, CONFIGURATION_FIELD, removedConfigurations, w);

        w.writeEndElement();// end configurations
        return getResultAndCloseXmlStreamWriter(w);
	}
	
	private String generateXmlFromDocument(Object obj) {
        XmlStreamWriter w = getXmlStreamWriter();
        proseccDocument((Document)obj, w);
        return getResultAndCloseXmlStreamWriter(w);
    }
    
    private void proseccDocument(Document documentObj, XmlStreamWriter w) {
    	w.writeStartElement(null, DOCUMENT_FIELD, null);
        writeElement(ID_FIELD, documentObj.getId(), w);        
        writeElement(TEXT_FIELD, documentObj.getText(), w);
        w.writeEndElement();
    }
    
    private String generateXmlFromDocumentList(Object obj) {
        XmlStreamWriter w = getXmlStreamWriter();
        w.writeStartElement(null, DOCUMENTS_FIELD, null);
        List<Document> documentList = (List<Document>) obj;
        for (Document documentObj : documentList) {
        	proseccDocument(documentObj, w);
        }
        w.writeEndElement();
        return getResultAndCloseXmlStreamWriter(w);
    }
    
    private String generateXmlFromCollection(Object obj) {
        XmlStreamWriter w = getXmlStreamWriter();
        w.writeStartElement(null, COLLECTION_FIELD, null);
        Collection collectionObj = (Collection)obj;
        writeElement(ID_FIELD, collectionObj.getId(), w);
        writeListOfElements(DOCUMENTS_FIELD, DOCUMENT_FIELD, collectionObj.getDocuments(), w);  
        w.writeEndElement();
        return getResultAndCloseXmlStreamWriter(w);
    }
	
	public override Object deserialize(String source, String className) {
		if ('BlacklistItem'.equals(className)) {
			return parseBlacklistItem(source);
		} else if ('Query'.equals(className)) {
			return parseQuery(source);
		} else if ('UserEntity'.equals(className)) {
			return parseEntity(source);
		} else if ('SentimentPhrase'.equals(className)) {
            return parseSentimentPhrase(source);
        } else if ('Category'.equals(className)) {
			return parseCategory(source);
		} else if ('Configuration'.equals(className)) {
			return parseConfiguration(source);
		} else if ('ServiceStatus'.equals(className)) {
			return parseAnalyticServiceStatus(source);
		} else if ('Subscription'.equals(className)) {
			return parseSubscription(source);
		} else if ('CollAnalyticData'.equals(className)) {
			return parseCollectionAnalyticData(source);
		} else if ('DocAnalyticData'.equals(className)) {
			return parseDocumentAnalyticData(source);
		} else if ('CollAnalyticDataList'.equals(className)) {
			return parseCollectionAnalyticDataList(source);
		} else if ('DocAnalyticDataList'.equals(className)) {
			return parseDocumentAnalyticDataList(source);
		} else {
			throw new IllegalStateException('Unsupported class: ' + className);
		}
		return null;
	}
	
	private List<String> parseBlacklistItem(String source) {
		System.debug(Logginglevel.ERROR, 'Deserializing BlacklistItem object');
		List<String> result = new List<String>();
		try {
	        Dom.Document doc = parseXMLSource(source);
	        for ( Dom.XmlNode filter : doc.getRootElement().getChildElements() ) {
	            result.add(filter.getText());
	        }
	        return result;
		} catch (Exception ex) {
			System.debug(Logginglevel.ERROR, 'Exception in parseBlacklistItem: ' + ex.getMessage());
			return result;
		}
    }
    
    private Dom.Document parseXMLSource(String source) {
    	Dom.Document doc = new Dom.Document();
        doc.load(source);
        return doc;
    }
    
    private List<Query> parseQuery(String source) {
    	System.debug(Logginglevel.ERROR, 'Deserializing Queue object');
    	List<Query> result = new List<Query>();
    	try {
	        Dom.Document doc = parseXMLSource(source);
	        for ( Dom.XmlNode query : doc.getRootElement().getChildElements() ) {
	            result.add(new Query(getNodeStringValue(query.getChildElement(NAME_FIELD, null)), getNodeStringValue(query.getChildElement(QUERY_FIELD, null))));
	        }  
	        return result;
    	} catch (Exception ex) {
    		System.debug(Logginglevel.ERROR, 'Exception in parseQuery: ' + ex.getMessage());
			return result;
    	}  
    }
    
    private String getNodeStringValue(Dom.XmlNode node) {
    	if (null != node) {
    		return node.getText();
    	} else {
    		return null;
    	}
    }
    
    private Integer getNodeIntegerValue(Dom.XmlNode node) {
    	if (null != node) {
            return Integer.valueOf(node.getText());
        } else {
            return null;
        }
    }
    
    private Long getNodeLongValue(Dom.XmlNode node) {
        if (null != node) {
            return Long.valueOf(node.getText());
        } else {
            return null;
        }
    }
    
    private Double getNodeDoubleValue(Dom.XmlNode node) {
        if (null != node) {
            return Double.valueOf(node.getText());
        } else {
            return null;
        }
    }
    
    private Boolean getNodeBooleanValue(Dom.XmlNode node) {
        if (null != node) {
            return Boolean.valueOf(node.getText());
        } else {
            return null;
        }
    }
    
    private List<UserEntity> parseEntity(String source) {
    	System.debug(Logginglevel.ERROR, 'Deserializing Entity object');
    	List<UserEntity> result = new List<UserEntity>();
    	try {
	        Dom.Document doc = parseXMLSource(source);
	        for ( Dom.XmlNode entity : doc.getRootElement().getChildElements() ) {
	            result.add(new UserEntity(getNodeStringValue(entity.getChildElement(NAME_FIELD, null)), getNodeStringValue(entity.getChildElement(TYPE_FIELD, null))));
	        }  
	        return result;
    	} catch (Exception ex) {
    		System.debug(Logginglevel.ERROR, 'Exception in parseEntity: ' + ex.getMessage());
			return result;
    	}  
    }
    
    private List<SentimentPhrase> parseSentimentPhrase(String source) {
        System.debug(Logginglevel.ERROR, 'Deserializing SentimentPhrase object');
        List<SentimentPhrase> result = new List<SentimentPhrase>();
        try {
            Dom.Document doc = parseXMLSource(source);
            for ( Dom.XmlNode sentimentPhraseObj : doc.getRootElement().getChildElements() ) {
                result.add(new SentimentPhrase(getNodeStringValue(sentimentPhraseObj.getChildElement(TITLE_FIELD, null)), 
                                                getNodeDoubleValue(sentimentPhraseObj.getChildElement(WEIGHT_FIELD, null))));
            }  
            return result;
        } catch (Exception ex) {
            System.debug(Logginglevel.ERROR, 'Exception in parseSentimentPhrase: ' + ex.getMessage());
            return result;
        }  
    }
    
    private List<Category> parseCategory(String source) {
    	System.debug(Logginglevel.ERROR, 'Deserializing Category object');
    	List<Category> result = new List<Category>();
    	try {
	        Dom.Document doc = parseXMLSource(source);
	        for ( Dom.XmlNode category : doc.getRootElement().getChildElements() ) {
	            Category categoryObj = new Category(getNodeStringValue(category.getChildElement(NAME_FIELD, null)), getNodeDoubleValue(category.getChildElement(WEIGHT_FIELD, null)));
	            categoryObj.setSamples(getSampleListFromNode(category.getChildElement(SAMPLES_FIELD, null)));
	            result.add(categoryObj);
	        }  
	        return result;
    	} catch (Exception ex) {
    		System.debug(Logginglevel.ERROR, 'Exception in parseCategory: ' + ex.getMessage());
			return result;
    	}  
    }
    
    private List<String> getSampleListFromNode(Dom.XmlNode samplesNode) {
    	List<String> result = new List<String>();
    	if (null != samplesNode) {
    		for (Dom.XmlNode sample : samplesNode.getChildElements()) {
    			result.add(sample.getText());
    		}
    	}
    	return result;
    }
	
	private List<Configuration> parseConfiguration(String source) {
		System.debug(Logginglevel.ERROR, 'Deserializing Configuration object');
		Dom.Document doc = new Dom.Document();
		List<Configuration> result = new List<Configuration>();
        try {
            doc.load(source);
            Dom.XmlNode rootElement = doc.getRootElement();
            for (Dom.XmlNode configNode : rootElement.getChildElements()) {
            	Configuration config = new Configuration();
	            config.setConfigId(getNodeStringValue(configNode.getChildElement(CONFIG_ID_FIELD, null)));
	            config.setName(getNodeStringValue(configNode.getChildElement(NAME_FIELD, null)));
	            config.setIsPrimary(getNodeBooleanValue(configNode.getChildElement(IS_PRIMARY_FIELD, null)));
	            config.setAutoResponding(getNodeBooleanValue(configNode.getChildElement(AUTO_RESPONDING_FIELD, null)));
	            config.setLanguage(getNodeStringValue(configNode.getChildElement(LANGUAGE_FIELD, null))); 
	            config.setCharsThreshold(getNodeIntegerValue(configNode.getChildElement(CHARS_THRESHOLD_FIELD, null)));
	            config.setCallbackUrl(getNodeStringValue(configNode.getChildElement(CALLBACK_FIELD, null)));
	            
	            Dom.XmlNode documentNode = configNode.getChildElement(DOCUMENT_FIELD, null);
	            config.setDocConceptTopicsLimit(getNodeIntegerValue(documentNode.getChildElement(CONCEPT_TOPICS_LIMIT_FIELD, null)));
	            config.setDocQueryTopicsLimit(getNodeIntegerValue(documentNode.getChildElement(QUERY_TOPICS_LIMIT_FIELD, null)));
	            config.setDocUserEntitiesLimit(getNodeIntegerValue(documentNode.getChildElement(USER_ENTITIES_LIMIT_FIELD, null)));
	            config.setDocNamedEntitiesLimit(getNodeIntegerValue(documentNode.getChildElement(NAMED_ENTITIES_LIMIT_FIELD, null)));
	            config.setDocThemesLimit(getNodeIntegerValue(documentNode.getChildElement(THEMES_LIMIT_FIELD, null)));
	            config.setEntityThemesLimit(getNodeIntegerValue(documentNode.getChildElement(ENTITY_THEMES_LIMIT_FIELD, null)));
	            config.setPhrasesLimit(getNodeIntegerValue(documentNode.getChildElement(PHRASES_LIMIT_FIELD, null)));
	            config.setSummaryLimit(getNodeIntegerValue(documentNode.getChildElement(SUMMARY_LIMIT_FIELD, null)));
	            
	            Dom.XmlNode collectionNode = configNode.getChildElement(COLLECTION_FIELD, null);
	            config.setFacetsLimit(getNodeIntegerValue(collectionNode.getChildElement(FACETS_LIMIT_FIELD, null)));
	            config.setFacetAttsLimit(getNodeIntegerValue(collectionNode.getChildElement(FACET_ATTS_LIMIT_FIELD, null)));
	            config.setCollConceptTopicsLimit(getNodeIntegerValue(documentNode.getChildElement(CONCEPT_TOPICS_LIMIT_FIELD, null)));
	            config.setCollQueryTopicsLimit(getNodeIntegerValue(documentNode.getChildElement(QUERY_TOPICS_LIMIT_FIELD, null)));
	            config.setCollNamedEntitiesLimit(getNodeIntegerValue(documentNode.getChildElement(NAMED_ENTITIES_LIMIT_FIELD, null)));
	            config.setCollThemesLimit(getNodeIntegerValue(documentNode.getChildElement(THEMES_LIMIT_FIELD, null)));
	            result.add(config);
            }
            
            return result;
        } catch (Exception ex) {
        	System.debug(Logginglevel.ERROR, 'Exception in parseConfiguration: ' + ex.getMessage());
            return result;
        }
	}
	
	private ServiceStatus parseAnalyticServiceStatus(String source) {
		System.debug(Logginglevel.ERROR, 'Deserializing ServiceStatus object');
		Dom.Document doc = new Dom.Document();
        try {
            doc.load(source);
            ServiceStatus result = new ServiceStatus();
            Dom.XmlNode rootElement = doc.getRootElement();
            result.setServiceStatus(getNodeStringValue(rootElement.getChildElement(SERVICE_STATUS_FIELD, null)));
            result.setApiVersion(getNodeStringValue(rootElement.getChildElement(API_VERSION_FIELD, null)));
            result.setServiceVersion(getNodeStringValue(rootElement.getChildElement(SERVICE_VERSION_FIELD, null)));
            result.setSupportedEncoding(getNodeStringValue(rootElement.getChildElement(SUPPORTED_ENCODING_FIELD, null)));
            result.setSupportedCompression(getNodeStringValue(rootElement.getChildElement(SUPPORTED_COMPRESSION_FIELD, null)));
            result.setSupportedLanguages(getSupportedLanguagesListFromNode(rootElement.getChildElement(SUPPORTED_LANGUAGES_FIELD, null)));
            
            return result;
        } catch (Exception ex) {
        	System.debug(Logginglevel.ERROR, 'Exception in parseAnalyticServiceStatus: ' + ex.getMessage());
            return null;
        }
	}
	
	private List<String> getSupportedLanguagesListFromNode(Dom.XmlNode supportedLanguagesNode) {
		List<String> result = new List<String>();
		if ( null != supportedLanguagesNode ) {
			for (Dom.XmlNode supportedLanguageNode : supportedLanguagesNode.getChildElements()) {
				result.add(supportedLanguageNode.getText());
			}
		}
		return result;
	}
	
	private Subscription parseSubscription(String source) {
		System.debug(Logginglevel.ERROR, 'Deserializing Subscription object');
		Dom.Document doc = new Dom.Document();
        try {
            doc.load(source);  
            Subscription result = new Subscription();
            Dom.XmlNode rootElement = doc.getRootElement();
            result.setName(getNodeStringValue(rootElement.getChildElement(NAME_FIELD, null)));
            result.setStatus(getNodeStringValue(rootElement.getChildElement(STATUS_FIELD, null)));
            result.setPriority(getNodeStringValue(rootElement.getChildElement(PRIORITY_FIELD, null)));
            result.setCallsBalance(getNodeIntegerValue(rootElement.getChildElement(CALLS_BALANCE_FIELD, null)));
            result.setCallsLimit(getNodeIntegerValue(rootElement.getChildElement(CALLS_LIMIT_FIELD, null)));
            result.setCallsLimitInterval(getNodeIntegerValue(rootElement.getChildElement(CALLS_LIMIT_INTERVAL_FIELD, null)));
            result.setDocsBalance(getNodeIntegerValue(rootElement.getChildElement(DOCS_BALANCE_FIELD, null)));
            result.setDocsLimit(getNodeIntegerValue(rootElement.getChildElement(DOCS_LIMIT_FIELD, null)));
            result.setDocsLimitInterval(getNodeIntegerValue(rootElement.getChildElement(DOCS_LIMIT_INTERVAL_FIELD, null)));
            result.setConfigurationsLimit(getNodeIntegerValue(rootElement.getChildElement(CONFIGURATIONS_LIMIT_FIELD, null)));
            result.setBlacklistLimit(getNodeIntegerValue(rootElement.getChildElement(BLACKLIST_LIMIT_FIELD, null)));
            result.setCategoriesLimit(getNodeIntegerValue(rootElement.getChildElement(CATEGORIES_LIMIT_FIELD, null)));
			result.setQueriesLimit(getNodeIntegerValue(rootElement.getChildElement(QUERIES_LIMIT_FIELD, null)));
			result.setEntitiesLimit(getNodeIntegerValue(rootElement.getChildElement(ENTITIES_LIMIT_FIELD, null)));
			result.setSentimentLimit(getNodeIntegerValue(rootElement.getChildElement(SENTIMENT_LIMIT_FIELD, null)));
			result.setCharactersLimit(getNodeIntegerValue(rootElement.getChildElement(CHARACTERS_LIMIT_FIELD, null)));
            result.setExpirationDate(getNodeLongValue(rootElement.getChildElement(EXPIRATION_DATE_FIELD, null)));
            result.setBatchLimit(getNodeIntegerValue(rootElement.getChildElement(BATCH_LIMIT_FIELD, null)));
            result.setCollectionLimit(getNodeIntegerValue(rootElement.getChildElement(COLLECTION_LIMIT_FIELD, null)));
            result.setAutoResponseLimit(getNodeIntegerValue(rootElement.getChildElement(AUTO_RESPONSE_LIMIT_FIELD, null)));
            result.setProcessedBatchLimit(getNodeIntegerValue(rootElement.getChildElement(PROCESSED_BATCH_LIMIT_FIELD, null)));
            result.setCallbackBatchLimit(getNodeIntegerValue(rootElement.getChildElement(CALLBACK_BATCH_LIMIT_FIELD, null)));
            result.setLimitType(getNodeStringValue(rootElement.getChildElement(LIMIT_TYPE_FIELD, null)));
            
            return result;  
        } catch (Exception ex) {
        	System.debug(Logginglevel.ERROR, 'Exception in parseSubscription: ' + ex.getMessage() 
        		+ '. Stack: ' + ex.getStackTraceString());
            return null;
        }
	}
	
	private CollAnalyticData parseCollectionAnalyticData(String source) {
		System.debug(Logginglevel.ERROR, 'Deserializing CollAnalyticData object');
		Dom.Document doc = new Dom.Document();
		try {
			doc.load(source);
			return processCollAnalyticData(doc.getRootElement());
		} catch (Exception ex) {
			System.debug(Logginglevel.ERROR, 'Exception in parseCollectionAnalyticData: ' + ex.getMessage() 
				+ '. Stack: ' + ex.getStackTraceString());
            return null;
		}
	}
	
	private CollAnalyticData processCollAnalyticData(Dom.XmlNode rootElement) {
		CollAnalyticData result = new CollAnalyticData();
		
		processCollectionDetails(result, rootElement);
		processCollectionFacets(result, rootElement.getChildElement(FACETS_FIELD, null));
		processCollectionTopics(result, rootElement.getChildElement(TOPICS_FIELD, null));
		processCollectionThemes(result, rootElement.getChildElement(THEMES_FIELD, null));
		processCollectionEntities(result, rootElement.getChildElement(ENTITIES_FIELD, null));
		return result;
	}
	
	private void processCollectionDetails(CollAnalyticData analyticData, Dom.XmlNode rootElement) {
		analyticData.setConfigId(getNodeStringValue(rootElement.getChildElement(CONFIG_ID_FIELD, null)));
		analyticData.setId(getNodeStringValue(rootElement.getChildElement(ID_FIELD, null)));
		analyticData.setStatus(getNodeStringValue(rootElement.getChildElement(STATUS_FIELD, null)));
	}
	
	private void processCollectionFacets(CollAnalyticData analyticData, Dom.XmlNode facetsRootElement) {
		if (null != facetsRootElement) {
			List<Facet> facets = new List<Facet>();
			for (Dom.XmlNode facetNode : facetsRootElement.getChildElements()) {
				Facet facetObj = new Facet();
				facetObj.setLabel(getNodeStringValue(facetNode.getChildElement(LABEL_FIELD, null)));
				facetObj.setCount(getNodeIntegerValue(facetNode.getChildElement(COUNT_FIELD, null)));
				facetObj.setNegativeCount(getNodeIntegerValue(facetNode.getChildElement(NEGATIVE_COUNT_FIELD, null)));
				facetObj.setPositiveCount(getNodeIntegerValue(facetNode.getChildElement(POSITIVE_COUNT_FIELD, null)));
				facetObj.setNeutralCount(getNodeIntegerValue(facetNode.getChildElement(NEUTRAL_COUNT_FIELD, null)));
				facetObj.setAttributes(getAttributesListFromNode(facetNode.getChildElement(ATTRIBUTES_FIELD, null)));
				facets.add(facetObj);
			}
			analyticData.setFacets(facets);
		} 
	}
	
	private List<Attribute> getAttributesListFromNode(Dom.XmlNode attributesRootElement) {
		List<Attribute> result = new List<Attribute>();  
		for (Dom.XmlNode attribute : attributesRootElement.getChildElements()) {
			result.add(new Attribute(getNodeStringValue(attribute.getChildElement(LABEL_FIELD, null)), getNodeIntegerValue(attribute.getChildElement(COUNT_FIELD, null))));
		}
		return result;
	}
	
	private void processCollectionTopics(CollAnalyticData analyticData, Dom.XmlNode topicsRootElement) {
		List<CollTopic> topics = new List<CollTopic>();
        if (null != topicsRootElement) {
            for (Dom.XmlNode topicNode : topicsRootElement.getChildElements()) { 
                CollTopic topicObj = new CollTopic();
                topicObj.setHitCount(getNodeIntegerValue(topicNode.getChildElement(HIT_COUNT_FIELD, null)));
                topicObj.setSentimentScore(getNodeDoubleValue(topicNode.getChildElement(SENTIMENT_SCORE_FIELD, null)));
                topicObj.setTitle(getNodeStringValue(topicNode.getChildElement(TITLE_FIELD, null)));
                topicObj.setType(getNodeStringValue(topicNode.getChildElement(TYPE_FIELD, null))); 
                topics.add(topicObj); 
            }
        }
    	analyticData.setTopics(topics);
  	}
  	
	private void processCollectionThemes(CollAnalyticData analyticData, Dom.XmlNode themesRootElement) {
		List<CollTheme> themes = new List<CollTheme>();
        if (null != themesRootElement) {
            for (Dom.XmlNode themeNode : themesRootElement.getChildElements()) { 
                CollTheme themeObj = new CollTheme();
                themeObj.setPhrasesCount(getNodeIntegerValue(themeNode.getChildElement(PHRASES_COUNT_FIELD, null)));
                themeObj.setThemesCount(getNodeIntegerValue(themeNode.getChildElement(THEMES_COUNT_FIELD, null)));
                themeObj.setSentimentScore(getNodeDoubleValue(themeNode.getChildElement(SENTIMENT_SCORE_FIELD, null)));
                themeObj.setTitle(getNodeStringValue(themeNode.getChildElement(TITLE_FIELD, null)));
                themes.add(themeObj);
            }
        }
    	analyticData.setThemes(themes);
  	}
  	
  	private void processCollectionEntities(CollAnalyticData analyticData, Dom.XmlNode entitiesRootElement) {
  		List<CollEntity> entities = new List<CollEntity>();
        if (null != entitiesRootElement) {  
            for (Dom.XmlNode entityNode : entitiesRootElement.getChildElements()) { 
                CollEntity entityObj = new CollEntity();
                entityObj.setTitle(getNodeStringValue(entityNode.getChildElement(TITLE_FIELD, null)));
                entityObj.setType(getNodeStringValue(entityNode.getChildElement(TYPE_FIELD, null)));
                entityObj.setEntityType(getNodeStringValue(entityNode.getChildElement(ENTITY_TYPE_FIELD, null)));
                entityObj.setCount(getNodeIntegerValue(entityNode.getChildElement(COUNT_FIELD, null)));
                entityObj.setNegativeCount(getNodeIntegerValue(entityNode.getChildElement(NEGATIVE_COUNT_FIELD, null)));
                entityObj.setNeutralCount(getNodeIntegerValue(entityNode.getChildElement(NEUTRAL_COUNT_FIELD, null)));
                entityObj.setPositiveCount(getNodeIntegerValue(entityNode.getChildElement(POSITIVE_COUNT_FIELD, null)));
                entities.add(entityObj); 
            }
        }
    	analyticData.setEntities(entities);
  	}
  	
	private List<CollAnalyticData> parseCollectionAnalyticDataList(String source) {
		System.debug(Logginglevel.ERROR, 'Deserializing CollAnalyticDataList object');
		Dom.Document doc = new Dom.Document();
		List<CollAnalyticData> result = new List<CollAnalyticData>();
		try {
			doc.load(source);
			for (Dom.XmlNode collAnalyticDataRootNode : doc.getRootElement().getChildElements()) {
				result.add(processCollAnalyticData(collAnalyticDataRootNode));
			}
			return result;
		} catch (Exception ex) {
			System.debug(Logginglevel.ERROR, 'Exception in parseCollectionAnalyticDataList: ' + ex.getMessage() 
				+ '. Stack: ' + ex.getStackTraceString());
            return result;
		}
	}
	
	private DocAnalyticData parseDocumentAnalyticData(String source) {
		System.debug(Logginglevel.ERROR, 'Deserializing DocAnalyticData object');
		Dom.Document doc = new Dom.Document();
		try {
			doc.load(source);
			return processDocAnalyticData(doc.getRootElement());
		} catch (Exception ex) {
			System.debug(Logginglevel.ERROR, 'Exception in parseDocumentAnalyticData: ' + ex.getMessage() 
				+ '. Stack: ' + ex.getStackTraceString());
            return null;
		}
	}
	
	private DocAnalyticData processDocAnalyticData(Dom.XmlNode rootElement) {
		DocAnalyticData result = new DocAnalyticData();
		processDocumentDetails(result, rootElement);
		processDocumentComponents(result, rootElement);
		return result;
	}
	
	private void processDocumentDetails(DocAnalyticData analyticData, Dom.XmlNode rootElement) {
		analyticData.setConfigId(getNodeStringValue(rootElement.getChildElement(CONFIG_ID_FIELD, null)));
		analyticData.setId(getNodeStringValue(rootElement.getChildElement(ID_FIELD, null)));
		analyticData.setStatus(getNodeStringValue(rootElement.getChildElement(STATUS_FIELD, null))); 
		analyticData.setSentimentScore(getNodeDoubleValue(rootElement.getChildElement(SENTIMENT_SCORE_FIELD, null)));
		analyticData.setSummary(getNodeStringValue(rootElement.getChildElement(SUMMARY_FIELD, null)));
	}
	
	private void processDocumentComponents(DocAnalyticData analyticData, Dom.XmlNode rootElement) {
		processDocumentThemes(analyticData, rootElement.getChildElement(THEMES_FIELD, null));
		processDocumentEntities(analyticData, rootElement.getChildElement(ENTITIES_FIELD, null));
		processDocumentTopics(analyticData, rootElement.getChildElement(TOPICS_FIELD, null));
		processDocumentPhrases(analyticData, rootElement.getChildElement(PHRASES_FIELD, null));
	}
	 
	private void processDocumentThemes(DocAnalyticData analyticData, Dom.XmlNode themesRootElement) {
    	analyticData.setThemes(processAndReturnDocThemes(themesRootElement));
  	}
  	
  	private List<DocTheme> processAndReturnDocThemes(Dom.XmlNode themesRootElement) {
  		List<DocTheme> result = new List<DocTheme>();
        if (null != themesRootElement) {
            for (Dom.XmlNode themeNode : themesRootElement.getChildElements()) { 
                DocTheme themeObj = new DocTheme();
                themeObj.setEvidence(getNodeIntegerValue(themeNode.getChildElement(EVIDENCE_FIELD, null)));
                themeObj.setStrengthScore(getNodeDoubleValue(themeNode.getChildElement(STRENGTH_SCORE_FIELD, null)));
                themeObj.setIsAbout(getNodeBooleanValue(themeNode.getChildElement(IS_ABOUT_FIELD, null)));
                themeObj.setSentimentScore(getNodeDoubleValue(themeNode.getChildElement(SENTIMENT_SCORE_FIELD, null)));
                themeObj.setTitle(getNodeStringValue(themeNode.getChildElement(TITLE_FIELD, null)));
                result.add(themeObj);
            }
        }
        return result;
  	}
  
	private void processDocumentEntities(DocAnalyticData analyticData, Dom.XmlNode entitiesRootElement) {
		List<DocEntity> entities = new List<DocEntity>();
        if (null != entitiesRootElement) {  
            for (Dom.XmlNode entityNode : entitiesRootElement.getChildElements()) { 
                DocEntity entityObj = new DocEntity();
                entityObj.setEvidence(getNodeIntegerValue(entityNode.getChildElement(EVIDENCE_FIELD, null)));
                entityObj.setIsAbout(getNodeBooleanValue(entityNode.getChildElement(IS_ABOUT_FIELD, null)));
                entityObj.setConfident(getNodeBooleanValue(entityNode.getChildElement(CONFIDENT_FIELD, null)));
                entityObj.setSentimentScore(getNodeDoubleValue(entityNode.getChildElement(SENTIMENT_SCORE_FIELD, null)));
                entityObj.setTitle(getNodeStringValue(entityNode.getChildElement(TITLE_FIELD, null)));
                entityObj.setType(getNodeStringValue(entityNode.getChildElement(TYPE_FIELD, null)));
                entityObj.setEntityType(getNodeStringValue(entityNode.getChildElement(ENTITY_TYPE_FIELD, null)));
                entityObj.setThemes(processAndReturnDocThemes(entityNode.getChildElement(THEMES_FIELD, null)));
                entities.add(entityObj); 
            }
        }
		analyticData.setEntities(entities);
	}
	
	private void processDocumentTopics(DocAnalyticData analyticData, Dom.XmlNode topicsRootElement) {
		List<DocTopic> topics = new List<DocTopic>();
        if (null != topicsRootElement) {
            for (Dom.XmlNode topicNode : topicsRootElement.getChildElements()) { 
                DocTopic topicObj = new DocTopic();
                topicObj.setHitCount(getNodeIntegerValue(topicNode.getChildElement(HIT_COUNT_FIELD, null)));
                topicObj.setSentimentScore(getNodeDoubleValue(topicNode.getChildElement(SENTIMENT_SCORE_FIELD, null)));
                topicObj.setStrengthScore(getNodeDoubleValue(topicNode.getChildElement(STRENGTH_SCORE_FIELD, null)));
                topicObj.setTitle(getNodeStringValue(topicNode.getChildElement(TITLE_FIELD, null)));
                topicObj.setType(getNodeStringValue(topicNode.getChildElement(TYPE_FIELD, null))); 
                topics.add(topicObj); 
            }
        }
        analyticData.setTopics(topics);
	}
	
	private void processDocumentPhrases(DocAnalyticData analyticData, Dom.XmlNode phrasesRootElement) {
		List<DocPhrase> phrases = new List<DocPhrase>();
		if (null != phrasesRootElement) {
			for (Dom.XmlNode phraseNode : phrasesRootElement.getChildElements()) { 
				DocPhrase phraseObj = new DocPhrase();
				phraseObj.setNegatingPhrase(getNodeStringValue(phraseNode.getChildElement(NEGATING_PHRASE_FIELD, null)));
				phraseObj.setIsNegated(getNodeBooleanValue(phraseNode.getChildElement(IS_NEGATED_FIELD, null)));
				phraseObj.setSentimentScore(getNodeDoubleValue(phraseNode.getChildElement(SENTIMENT_SCORE_FIELD, null)));
				phraseObj.setTitle(getNodeStringValue(phraseNode.getChildElement(TITLE_FIELD, null)));
				phrases.add(phraseObj);
			}
		}
		analyticData.setPhrases(phrases);
	}
	
	private List<DocAnalyticData> parseDocumentAnalyticDataList(String source) {
		System.debug(Logginglevel.ERROR, 'Deserializing DocAnalyticDataList object');
		Dom.Document doc = new Dom.Document();
		List<DocAnalyticData> result = new List<DocAnalyticData>();
		try {
			doc.load(source);
			for(Dom.XmlNode docAnalyticDataRootNode : doc.getRootElement().getChildElements()) {
				result.add(processDocAnalyticData(docAnalyticDataRootNode));
			}
		} catch (Exception ex) {
			System.debug(Logginglevel.ERROR, 'Exception in parseDocumentAnalyticDataList: ' + ex.getMessage()
				+ '. Stack: ' + ex.getStackTraceString());
            return result;
		}
		
		return result;
	}
	
	private static final String SERIALIZER_TYPE = 'xml';
    private static final String SERVICE_STATUS_FIELD = 'service_status';
    private static final String API_VERSION_FIELD = 'api_version';
    private static final String SERVICE_VERSION_FIELD = 'service_version';
    private static final String SUPPORTED_ENCODING_FIELD = 'supported_encoding';
    private static final String SUPPORTED_COMPRESSION_FIELD = 'supported_compression';
    private static final String SUPPORTED_LANGUAGES_FIELD = 'supported_languages';
    private static final String STATUS_FIELD = 'status';
    private static final String PRIORITY_FIELD = 'priority';
    private static final String EXPIRATION_DATE_FIELD = 'expiration_date';
    private static final String CALLS_BALANCE_FIELD = 'calls_balance';
    private static final String CALLS_LIMIT_FIELD = 'calls_limit';
    private static final String CALLS_LIMIT_INTERVAL_FIELD = 'calls_limit_interval';
    private static final String DOCS_BALANCE_FIELD = 'docs_balance';
    private static final String DOCS_LIMIT_FIELD = 'docs_limit';
    private static final String DOCS_LIMIT_INTERVAL_FIELD = 'docs_limit_interval';
    private static final String CONFIGURATIONS_LIMIT_FIELD = 'configurations_limit';
    private static final String BLACKLIST_LIMIT_FIELD = 'blacklist_limit';
    private static final String CATEGORIES_LIMIT_FIELD = 'categories_limit';
    private static final String QUERIES_LIMIT_FIELD = 'queries_limit';
    private static final String ENTITIES_LIMIT_FIELD = 'entities_limit';
    private static final String SENTIMENT_LIMIT_FIELD = 'sentiment_limit';
    private static final String CHARACTERS_LIMIT_FIELD = 'characters_limit';
    private static final String BATCH_LIMIT_FIELD = 'batch_limit';
    private static final String COLLECTION_LIMIT_FIELD = 'collection_limit';
    private static final String AUTO_RESPONSE_LIMIT_FIELD = 'auto_response_limit';
    private static final String PROCESSED_BATCH_LIMIT_FIELD = 'processed_batch_limit';
    private static final String CALLBACK_BATCH_LIMIT_FIELD = 'callback_batch_limit';
    private static final String CONFIG_ID_FIELD = 'config_id';
    private static final String NAME_FIELD = 'name';
    private static final String TEMPLATE_FIELD = 'template';
    private static final String ONE_SENTENCE_FIELD = 'one_sentence';
    private static final String IS_PRIMARY_FIELD = 'is_primary';
    private static final String AUTO_RESPONDING_FIELD = 'auto_responding';
    private static final String LANGUAGE_FIELD = 'language';
    private static final String CHARS_THRESHOLD_FIELD = 'chars_threshold';
    private static final String DOCUMENT_FIELD = 'document';
    private static final String DOCUMENTS_FIELD = 'documents';
    private static final String COLLECTION_FIELD = 'collection';
    private static final String CONCEPT_TOPICS_LIMIT_FIELD = 'concept_topics_limit';
    private static final String QUERY_TOPICS_LIMIT_FIELD = 'query_topics_limit';
    private static final String NAMED_ENTITIES_LIMIT_FIELD = 'named_entities_limit';
    private static final String USER_ENTITIES_LIMIT_FIELD = 'user_entities_limit';
    private static final String ENTITY_THEMES_LIMIT_FIELD = 'entity_themes_limit';
    private static final String THEMES_LIMIT_FIELD = 'themes_limit';
    private static final String PHRASES_LIMIT_FIELD = 'phrases_limit';
    private static final String SUMMARY_LIMIT_FIELD = 'summary_limit';
    private static final String FACETS_LIMIT_FIELD = 'facets_limit';
    private static final String FACET_ATTS_LIMIT_FIELD = 'facet_atts_limit';
    private static final String QUERY_FIELD = 'query';
    private static final String TYPE_FIELD = 'type';
    private static final String WEIGHT_FIELD = 'weight';
    private static final String SAMPLES_FIELD = 'samples';
    private static final String ID_FIELD = 'id';
    private static final String SENTIMENT_SCORE_FIELD = 'sentiment_score';
    private static final String SUMMARY_FIELD = 'summary';
    private static final String EVIDENCE_FIELD = 'evidence';
    private static final String IS_ABOUT_FIELD = 'is_about';
    private static final String NEGATING_PHRASE_FIELD = 'negating_phrase'; 
    private static final String IS_NEGATED_FIELD = 'is_negated';
    private static final String CONFIDENT_FIELD = 'confident';
    private static final String STRENGTH_SCORE_FIELD = 'strength_score';
    private static final String TITLE_FIELD = 'title';
    private static final String ENTITY_TYPE_FIELD = 'entity_type';
    private static final String HIT_COUNT_FIELD = 'hitcount';
    private static final String ENTITIES_FIELD = 'entities'; 
    private static final String TOPICS_FIELD = 'topics';
    private static final String SCORE_FIELD = 'score';
    private static final String THEMES_FIELD = 'themes';
    private static final String FACETS_FIELD = 'facets';
    private static final String LABEL_FIELD = 'label';
    private static final String COUNT_FIELD = 'count';
    private static final String NEGATIVE_COUNT_FIELD = 'negative_count';
    private static final String POSITIVE_COUNT_FIELD = 'positive_count';
    private static final String NEUTRAL_COUNT_FIELD = 'neutral_count';
    private static final String ATTRIBUTES_FIELD = 'attributes';
    private static final String ATTRIBUTE_FIELD = 'attribute';
    private static final String ADDED_FIELD = 'added';
    private static final String REMOVED_FIELD = 'removed';
    private static final String ITEM_FIELD = 'item'; 
    private static final String BLACK_LIST_FIELD = 'blacklist';
    private static final String QUERIES_FIELD = 'queries';
    private static final String ENTITY_FIELD = 'entity';
    private static final String CATEGORIES_FIELD = 'categories';
    private static final String CATEGORY_FIELD = 'category';
    private static final String SAMPLE_FIELD = 'sample';
    private static final String CONFIGURATIONS_FIELD = 'configurations';
    private static final String CONFIGURATION_FIELD = 'configuration';
    private static final String CALLBACK_FIELD = 'callback';
    private static final String TEXT_FIELD = 'text';
    private static final String PHRASE_FIELD = 'phrase';
    private static final String PHRASES_FIELD = 'phrases';
    private static final String PHRASES_COUNT_FIELD = 'phrases_count';
    private static final String THEMES_COUNT_FIELD = 'themes_count';
    private static final String LIMIT_TYPE_FIELD = 'limit_type';
}