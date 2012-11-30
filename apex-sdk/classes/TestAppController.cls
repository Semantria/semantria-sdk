public class TestAppController {
	private static final Integer REQUESTS_INTERVAL = 10000;
	private static final String DOCUMENTS_SEPARATOR = '\n';
        
    public String key {get; set;}
    public String secret {get; set;}
    public String sourceText {get; set;}
    public String message {get; set;}
    
    public Boolean isAppRunning {get; set;}
    public Boolean doneWithExecution {get; set;}
    
    private Session sessionObj;
    private List<String> initialTexts;
    
    public TestAppController() {
        key = '';
        secret = '';
        sourceText = getDefaultSourceText();
        message = 'Application is not running';
    
        isAppRunning = false;
        doneWithExecution = true; 
    }
    
    public PageReference startExecution() {
        sessionObj = Session.createSession(key, secret);

        isAppRunning = true;
        doneWithExecution = false;
        message = 'Starting execution...<br/>';
        
        try {
        	if (key.equals('') || secret.equals('')) {
        		message = 'You should fill license properties';
	        	stopApplication();
	        	return null;
        	}
        	
	        // Initial texts for processing
	        if (!initTextForProcessing()) {
	        	message = 'Source text is empty.';
	        	stopApplication();
	        	return null;
	        }
	        
	        // Creates XML serializer instance
	        Serializer jsonSerializer = new XmlSerializer();
	        // Initializes new session with the serializer object and the keys.
	        sessionObj = Session.createSession(key, secret, jsonSerializer);
	        for (String text : initialTexts) {
	            String uid = generateGUID();
	            // Creates a sample document which need to be processed on Semantria
	            // Queues document for processing on Semantria service
	            Integer status = sessionObj.queueDocument(new Document(uid,text));
	            if (status <= 202) {
	                message += '<br/>Document ' + uid + ' queued succsessfully.';
	            } else {
	                message += '<br/>Invalid status: ' + status + '. Execution is stopped.';
	                stopApplication();
	                return null;
	            }
	        }  
	        
	        message += '<br/><br/>Waiting for results.';
        } catch(Exception ex) {
            message += '<br/><br/>Exception: ' + ex.getMessage() + '. Trace: ' + ex.getStackTraceString();
            stopApplication();
        }    
        
        return null;
    }
    
    private Boolean initTextForProcessing() {
    	if (null != sourceText && sourceText.trim().length() != 0) {
    		message += '<br/>Data for processing:<br/>';
    		initialTexts = new List<String>(sourceText.split(DOCUMENTS_SEPARATOR));
    		for (Integer i = 0; i < initialTexts.size(); ++i ) {
    			message += (i + 1) + ') ' + initialTexts.get(i) + '<br/>';
    		}
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public PageReference getResults() {
        try {
            // As Semantria isn't real-time solution you need to wait some time before getting of the processed results
            List<DocAnalyticData> processed = new List<DocAnalyticData>();
            message += '<br/><br/>Requesting of the processed results...';
            
            while (processed.size() < initialTexts.size() && Limits.getCallouts() < Limits.getLimitCallouts()) {
                // Requests processed results from Semantria service
                List<DocAnalyticData> temp = sessionObj.getProcessedDocuments(); 
                processed.addAll(temp); 
                
                if (processed.size() >= initialTexts.size()) {
                        doneWithExecution = true;
                        break;
                }
            }
            
            if (doneWithExecution) {
                // Requests processed results from Semantria service
                Integer i = 1;
                for(DocAnalyticData doc : processed) {
                    message += '<br/><br/>' + i+ ') Document ' + doc.getId() + '. Sentiment score: ' + String.valueOf(doc.getSentimentScore());
                    
                    // Printing of document themes
                    if (null != doc.getThemes() && doc.getThemes().size() > 0) {
                            message += '<br/><br/>Document themes:';
                            for (DocTheme themeObj : doc.getThemes()) {
                                message += '<br/>' + themeObj.getTitle() + ' (sentiment: ' + String.valueOf(themeObj.getSentimentScore()) +')';
                            }
                    }
                        
                    // Printing of document entities
                    if (null != doc.getEntities() && doc.getEntities().size() > 0) {
                            message += '<br/><br/>Entities:';
                            for (DocEntity entityObj : doc.getEntities()) {
                            message += '<br/>' + entityObj.getTitle() + ' : ' + entityObj.getEntityType()  
                                    + ' (sentiment: ' + entityObj.getSentimentScore() + ')';
                            }
                    }
                    ++i;
                }
                stopApplication();
            } else {
                message += '<br/>Waiting for results...';
            }
            
        } catch(Exception ex) {
            message += '<br/><br/>Exception: ' + ex.getMessage() + '. Trace: ' + ex.getStackTraceString();
            stopApplication();
        }       
        
        return null;
    }
    
    private void stopApplication() {
        isAppRunning = false;
        doneWithExecution = true;
    }
    
    private static String generateGUID() {
        return EncodingUtil.convertToHex(Crypto.generateDigest('MD5', Blob.valueOf(String.valueOf(Crypto.getRandomLong())))).substring(0,18);
    }
    
    private String getDefaultSourceText() {
    	return 'Lisa - there\'s 2 Skinny cow coupons available 5₤ skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh\'s). I couldn\'t find them instore and i\'m not going to walmart before the 19th. Oh well sounds like i\'m not missing much ...lol'
			+ '\nIn Lake Louise - a guided walk for the family with Great ₤ Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The "must-do" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.'
			+ '\nOn this day in 1786 - In New ₤ York City  commercial ice cream was manufactured for the first time.';
    }
    
    static testMethod void testExecutingTestApp() {
        TestAppController controller = new TestAppController();
        try {
            controller.startExecution();
            controller.getResults();
            System.assert(true);
        } catch (TypeException ex) {
                System.assert(true);
        } catch (Exception ex) {
            System.debug(Logginglevel.ERROR, 'Exception: ' + ex.getMessage() + '. Trace: ' 
                  + ex.getStackTraceString() + '. Type: ' + ex.getTypeName());
                System.assert(false);
        }
    }
}