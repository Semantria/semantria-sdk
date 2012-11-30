# -*- coding: utf-8 -*-

from semantria.authrequest import *
from semantria.jsonserializer import * 
from semantria.mapping import *

class Session:
    host = 'https://api21.semantria.com'
    wrapperName = 'Python_3.x'
    
    def __init__(self, consumerKey, consumerSecret, serializer=None, applicationName=None):
        self.consumerKey = consumerKey
        self.consumerSecret = consumerSecret

        if applicationName:
            self.applicationName = '{0}.{1}'.format(applicationName, self.wrapperName)
        else:
            self.applicationName = self.wrapperName
            
        if serializer:
            self.serializer = serializer
            self.format = serializer.gettype()
        else:
            #set default json serializer
            self.serializer = JsonSerializer()
            self.format = self.serializer.gettype()

        #Events
        self.Request = SessionEvent(self) 
        self.Response = SessionEvent(self) 
        self.Error = SessionEvent(self) 
        self.DocsAutoResponse = SessionEvent(self) 
        self.CollsAutoResponse = SessionEvent(self) 

    def registerSerializer(self, serializer):
        if serializer:
            self.serializer = serializer
            self.format = serializer.gettype()
        else :
            raise Error('Parameter not found: %s' % serializer)
    
    def createUpdateProxy(self):
        return {"added":[], "removed":[], "cloned":[]}

    def getStatus(self):
        url = '{0}/status.{1}'.format(self.host, self.format)
        return self._runRequest("GET", url, "get_status")

    def verifySubscription(self):
        url = '{0}/subscription.{1}'.format(self.host, self.format)
        return self._runRequest("GET", url, "get_subscription")
        
    def getConfigurations(self):
        url = '{0}/configurations.{1}'.format(self.host, self.format)
        result = self._runRequest("GET", url, "get_configurations")
        if result is None:
            result = []
        return result
    
    def updateConfigurations(self, updates):
        url = '{0}/configurations.{1}'.format(self.host, self.format)
        wrapper = self._getTypeWrapper("update_configurations")
        data = self.serializer.serialize(updates, wrapper);
        return self._runRequest("POST", url, None, data)
           
    def getBlacklist(self, configId=None):
        if configId:
            url = '{0}/blacklist.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/blacklist.{1}'.format(self.host, self.format)
        
        result = self._runRequest("GET", url, "get_blacklist")
        if result is None:
            result = []
        return result
    
    def updateBlacklist(self, updates, configId=None):
        if configId:
            url = '{0}/blacklist.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/blacklist.{1}'.format(self.host, self.format)
        
        wrapper = self._getTypeWrapper("update_blacklist")
        data = self.serializer.serialize(updates, wrapper);
        return self._runRequest("POST", url, None, data)
         
    def getCategories(self, configId=None):
        if configId:
            url = '{0}/categories.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/categories.{1}'.format(self.host, self.format)
        
        result = self._runRequest("GET", url, "get_categories")
        if result is None:
            result = []
        return result
   
    def updateCategories(self, updates, configId=None):
        if configId:
            url = '{0}/categories.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/categories.{1}'.format(self.host, self.format)
        
        wrapper = self._getTypeWrapper("update_categories")
        data = self.serializer.serialize(updates, wrapper);
        return self._runRequest("POST", url, None, data)

    def getQueries(self, configId=None):
        if configId:
            url = '{0}/queries.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/queries.{1}'.format(self.host, self.format)
        
        result = self._runRequest("GET", url, "get_queries")
        if result is None:
            result = []
        return result
    
    def updateQueries(self, updates, configId=None):
        if configId:
            url = '{0}/queries.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/queries.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("update_queries")
        data = self.serializer.serialize(updates, wrapper);
        return self._runRequest("POST", url, None, data)

    def getSentimentPhrases(self, configId=None):
        if configId:
            url = '{0}/sentiment.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/sentiment.{1}'.format(self.host, self.format)
        
        result = self._runRequest("GET", url, "get_sentiment_phrases")
        if result is None:
            result = []
        return result
    
    def updateSentimentPhrases(self, updates, configId=None):
        if configId:
            url = '{0}/sentiment.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/sentiment.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("update_sentiment_phrases")
        data = self.serializer.serialize(updates, wrapper);
        return self._runRequest("POST", url, None, data)

    def getEntities(self, configId=None):
        if configId:
            url = '{0}/entities.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/entities.{1}'.format(self.host, self.format)
        
        result = self._runRequest("GET", url, "get_entities")
        if result is None:
            result = []
        return result
    
    def updateEntities(self, updates, configId=None):
        if configId:
            url = '{0}/entities.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/entities.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("update_entities")
        data = self.serializer.serialize(updates, wrapper);
        return self._runRequest("POST", url, None, data)
    
    def queueDocument(self, task, configId=None):
        if configId:
            url = '{0}/document.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/document.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("queue_document")
        data = self.serializer.serialize(task, wrapper);
        result = self._runRequest("POST", url, "get_processed_documents", data)
        if (result != None and not isinstance(result, int)):
            self.DocsAutoResponse(result)
            return 200
        else:
            return result
    
    def queueBatchOfDocuments(self, batch, configId=None):
        if configId:
            url = '{0}/document/batch.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/document/batch.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("queue_batch_documents")
        data = self.serializer.serialize(batch, wrapper);

        result = self._runRequest("POST", url, "get_processed_documents", data)
        if (result != None and not isinstance(result, int)):
            self.DocsAutoResponse(result)
            return 200
        else:
            return result

    def getDocument(self, id, configId=None):
        if id:
            if configId:
                url = '{0}/document/{1}.{2}?config_id={3}'.format(self.host, id, self.format, configId)
            else:
                url = '{0}/document/{1}.{2}'.format(self.host, id, self.format)
        else:
            raise Error('Parameter not found: %s' % id) 

        return self._runRequest("GET", url, "get_document")

    def cancelDocument(self, id, configId=None):
        if id:
            if configId:
                url = '{0}/document/{1}.{2}?config_id={3}'.format(self.host, id, self.format, configId)
            else:
                url = '{0}/document/{1}.{2}'.format(self.host, id, self.format)
        else:
            raise Error('Parameter not found: %s' % id) 

        return self._runRequest("DELETE", url)

    def getProcessedDocuments(self, configId=None):
        if configId:
            url = '{0}/document/processed.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/document/processed.{1}'.format(self.host, self.format)
        
        result = self._runRequest("GET", url, "get_processed_documents")
        if result is None:
            result = []
        return result
    
    def queueCollection(self, task, configId=None):
        if configId:
            url = '{0}/collection.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/collection.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("queue_collection")
        data = self.serializer.serialize(task, wrapper);
        result = self._runRequest("POST", url, "get_processed_collections", data)
        if (result != None and not isinstance(result, int)):
            self.CollsAutoResponse(result)
            return 200
        else:
            return result

    def getCollection(self, id, configId=None):
        if id:
            if configId:
                url = '{0}/collection/{1}.{2}?config_id={3}'.format(self.host, id, self.format, configId)
            else:
                url = '{0}/collection/{1}.{2}'.format(self.host, id, self.format)
        else:
            raise Error('Parameter not found: %s' % id) 

        return self._runRequest("GET", url, "get_collection")

    def cancelCollection(self, id, configId=None):
        if id:
            if configId:
                url = '{0}/collection/{1}.{2}?config_id={3}'.format(self.host, id, self.format, configId)
            else:
                url = '{0}/collection/{1}.{2}'.format(self.host, id, self.format)
        else:
            raise Error('Parameter not found: %s' % id) 

        return self._runRequest("DELETE", url)

    def getProcessedCollections(self, configId=None):
        if configId:
            url = '{0}/collection/processed.{1}?config_id={2}'.format(self.host, self.format, configId)
        else:
            url = '{0}/collection/processed.{1}'.format(self.host, self.format)
        
        result = self._runRequest("GET", url, "get_processed_collections")
        if result is None:
            result = []
        return result
      
    def _getTypeHandler(self, type):
        if self.serializer.gettype() == "json":
            return None

        #only for xml serializer
        if type == "get_status":
            return GetStatusHandler()
        elif type == "get_subscription":
            return GetSubscriptionHandler()
        elif type == "get_configurations":
            return GetConfigurationsHandler()
        elif type == "get_blacklist":
            return GetBlacklistHandler()
        elif type == "get_categories":
            return GetCategoriesHandler()
        elif type == "get_queries":
            return GetQueriesHandler()
        elif type == "get_sentiment_phrases":
            return GetSentimentPhrasesHandler()
        elif type == "get_entities":
            return GetEntitiesHandler()
        elif type == "get_document":
            return GetDocumentHandler()
        elif type == "get_processed_documents":
            return GetProcessedDocumentsHandler()
        elif type == "get_collection":
            return GetCollectionHandler()
        elif type == "get_processed_collections":
            return GetProcessedCollectionsHandler()
        else:
            return None

    def _getTypeWrapper(self, type):
        if self.serializer.gettype() == "json":
            return None

        #only for xml serializer
        if type == "update_configurations":
            return {"root":"configurations", "added":"configuration", "removed":"configuration"}
        elif type == "update_blacklist":
            return {"root":"blacklist", "added":"item", "removed":"item"}
        elif type == "update_categories":
            return {"root":"categories", "added":"category", "removed":"category", "samples":"sample"}
        elif type == "update_queries":
            return {"root":"queries", "added":"query", "removed":"query"}
        elif type == "update_sentiment_phrases":
            return {"root":"phrases", "added":"phrase", "removed":"phrase"}
        elif type == "update_entities":
            return {"root":"entities", "added":"entity", "removed":"entity"}
        elif type == "queue_document":
            return {"root":"document"}
        elif type == "queue_batch_documents":
            return {"root":"documents", "item":"document"}
        elif type == "queue_collection":
            return {"root":"collection", "documents":"document"}
        else:
            return None

    def _runRequest(self, method, url, type=None, postData=None):
        request = AuthRequest(self.consumerKey, self.consumerSecret, self.applicationName)
        self.Request({"method":method, "url":url, "message":postData}) 
        response = request.authWebRequest(method, url, postData);
        self.Response({"status":response["status"], "reason":response["reason"], "message":response["data"]}) 
        
        status = response["status"]
        message = response["reason"]
        
        if response["data"] != "":
            message = response["data"]

        if method == "DELETE":
            if (status == 200 or status == 202):
                return status
            else:
                self._resolveError(status, message)
                return status
        else:
            if status == 200:
                handler = self._getTypeHandler(type) 
                message = self.serializer.deserialize(response["data"], handler)
                return message
            elif status == 202:
                if method == "POST":
                    return status
                else:
                    return None
            else:
                self._resolveError(status, message)
     
    def _resolveError(self, status, message=None):
        if status == 400 or status == 401 or status == 402 or status == 403 or status == 406 or status == 500:
            self.Error({"status":status, "message":message})
        else:
            raise http.client.HTTPException(status, message)

class SessionEvent: 
    def __init__(self, sender): 
        self.handlers = set()
        self.sender = sender 
 
    def handle(self, handler): 
        self.handlers.add(handler) 
        return self 
 
    def unhandle(self, handler): 
        try: 
            self.handlers.remove(handler) 
        except: 
            raise ValueError("Handler is not handling this event, so cannot unhandle it.") 
        return self 
 
    def fire(self, *args, **kargs): 
        for handler in self.handlers: 
            handler(self.sender, *args, **kargs) 
 
    def getHandlerCount(self): 
        return len(self.handlers) 
 
    __iadd__ = handle 
    __isub__ = unhandle 
    __call__ = fire 
    __len__  = getHandlerCount
