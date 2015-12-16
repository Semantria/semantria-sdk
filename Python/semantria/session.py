# -*- coding: utf-8 -*-

try:
    import http.client as httplib

except ImportError:
    import httplib

import json
import os
from semantria.authrequest import *
from semantria.error import SemantriaError
from semantria.jsonserializer import *
from semantria.version import WRAPPER_VERSION


class Session(object):
    host = 'https://api.semantria.com'
    wrapperName = 'Python/' + WRAPPER_VERSION

    _key_url = 'https://semantria.com/auth/session'
    _app_key = '8f46c3c2-ca89-01aa-aad3-f437ea98cf7f'

    def __init__(self, consumerKey, consumerSecret, serializer=None, applicationName=None, use_compression=False, username=None, password=None, session_file ='/tmp/session.dat'):
        if consumerKey is None and consumerSecret is None:
            consumerKey, consumerSecret = self.obtainKeys(username, password, session_file)
            if not consumerKey or not consumerSecret:
                raise Exception('Cannot obtain Semantria keys. Wrong username or password.')
        self.consumerKey = consumerKey
        self.consumerSecret = consumerSecret
        self.use_compression = use_compression

        if applicationName:
            self.applicationName = '{0}/{1}'.format(applicationName, self.wrapperName)
        else:
            self.applicationName = self.wrapperName

        if serializer:
            self.serializer = serializer
            self.format = serializer.gettype()
        else:
            # set default json serializer
            self.serializer = JsonSerializer()
            self.format = self.serializer.gettype()

        self.applicationName = self.applicationName + "/" + self.format.upper()

        self._request = AuthRequest(
            self.consumerKey,
            self.consumerSecret,
            self.applicationName,
            self.use_compression
        )

        # Events
        self.Request = SessionEvent(self)
        self.Response = SessionEvent(self)
        self.Error = SessionEvent(self)
        self.DocsAutoResponse = SessionEvent(self)
        self.CollsAutoResponse = SessionEvent(self)

    def obtainKeys(self, username, password, session_file='/tmp/semantria-session.dat'):
        import requests

        if os.path.exists(session_file):
            with open(session_file) as f:
                f.seek(0)
                session_id = f.readline()

            url = '{0}/{1}.json?appkey={2}'.format(self._key_url, session_id, self._app_key)
            res = requests.get(url)

            if res.status_code == 200:
                json_res = res.json()
                return json_res['custom_params']['key'], json_res['custom_params']['secret']

        url = '{0}.json?appkey={1}'.format(self._key_url, self._app_key)
        data = {'username': username, 'password': password}

        res = requests.post(url, data=json.dumps(data))
        if res.status_code != 200:
            return False, False

        json_res = res.json()
        session_id = json_res['id']

        with open(session_file, 'w') as f:
            f.seek(0)
            f.write(session_id)

        return json_res['custom_params']['key'], json_res['custom_params']['secret']

    def registerSerializer(self, serializer):
        if serializer:
            self.serializer = serializer
            self.format = serializer.gettype()
        else:
            raise SemantriaError('Parameter not found: %s' % serializer)

    def getApiVersion(self):
        return self._request.apiVersion

    def setApiVersion(self, apiVersion):
        self._request.apiVersion = apiVersion

    def getStatus(self):
        url = '{0}/status.{1}'.format(self.host, self.format)
        return self._runRequest("GET", url, "get_status")

    def getSupportedFeatures(self, language):
        url = '{0}/features.{1}'.format(self.host, self.format)
        if language:
            url = '{0}?language={1}'.format(url, language)

        return self._runRequest("GET", url, "get_features")

    def getSubscription(self):
        url = '{0}/subscription.{1}'.format(self.host, self.format)
        return self._runRequest("GET", url, "get_subscription")

    def getStatistics(self, configId=None, interval=None):
        """ Verify subscription responds with overall documents and system API calls balance

        :param configId: Identifier of the specific configuration for usage statistics retrieving
        :type configId: str
        :param interval: Hour, Day, Week, Month, Year values are supported.
        :type interval: str
        :return: Semantria service statistics
        :rtype: dict
        """
        url = '{0}/statistics.{1}'.format(self.host, self.format)
        if configId:
            url = '{0}?config_id={1}'.format(url, configId)

        if configId and interval and interval.lower() in ["hour", "day", "week", "month", "year"]:
            url = '{0}&interval={1}'.format(url, interval)
        elif interval and interval in ["hour", "day", "week", "month", "year"]:
            url = '{0}?interval={1}'.format(url, interval)

        return self._runRequest("GET", url, "get_statistics")

    def getConfigurations(self):
        url = '{0}/configurations.{1}'.format(self.host, self.format)
        result = self._runRequest("GET", url, "get_configurations")
        if result is None:
            result = []
        return result

    def addConfigurations(self, items):
        return self.updateConfigurations(items, create=True)

    def updateConfigurations(self, items, create=False):
        if not isinstance(items, list):
            items = [items]

        url = '{0}/configurations.{1}'.format(self.host, self.format)
        wrapper = self._getTypeWrapper("update_configurations")
        data = self.serializer.serialize(items, wrapper)
        return self._runRequest(("POST" if create else "PUT"), url, None, data)

    def cloneConfiguration(self, name, template):
        if name is None:
            name = ''
        items = [name, template]
        return self.updateConfigurations(items)

    def removeConfigurations(self, config_id):
        url = '{0}/configurations.{1}'.format(self.host, self.format)
        data = []
        if not isinstance(config_id, list):
            data.append(config_id)
        else:
            data = config_id
        wrapper = self._getTypeWrapper("delete_configurations")
        data = self.serializer.serialize(data, wrapper)
        return self._runRequest("DELETE", url=url, postData=data)

    def getBlacklist(self, config_id=None):
        if config_id:
            url = '{0}/blacklist.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/blacklist.{1}'.format(self.host, self.format)

        result = self._runRequest("GET", url, "get_blacklist")
        if result is None:
            result = []
        return result

    def addBlacklist(self, items, config_id=None):
        return self.updateBlacklist(items, config_id, create=True)

    def updateBlacklist(self, items, config_id=None, create=False):
        if not isinstance(items, list):
            items = [items]

        if config_id:
            url = '{0}/blacklist.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/blacklist.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("update_blacklist")
        data = self.serializer.serialize(items, wrapper)
        return self._runRequest(("POST" if create else "PUT"), url=url, postData=data)

    def removeBlacklist(self, items, config_id=None):
        if not isinstance(items, list):
            items = [items]

        if config_id:
            url = '{0}/blacklist.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/blacklist.{1}'.format(self.host, self.format)
        wrapper = self._getTypeWrapper("delete_from_blacklist")
        data = self.serializer.serialize(items, wrapper)
        return self._runRequest("DELETE", url=url, postData=data)

    def getCategories(self, config_id=None):
        if config_id:
            url = '{0}/categories.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/categories.{1}'.format(self.host, self.format)

        result = self._runRequest("GET", url, "get_categories")
        if result is None:
            result = []
        return result

    def addCategories(self, items, config_id=None):
        return self.updateCategories(items, config_id, create=True)

    def updateCategories(self, items, config_id=None, create=False):
        if not isinstance(items, list):
            items = [items]

        if config_id:
            url = '{0}/categories.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/categories.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("update_categories")
        data = self.serializer.serialize(items, wrapper)
        return self._runRequest(("POST" if create else "PUT"), url=url, postData=data)

    def removeCategories(self, items, config_id=None):
        if not isinstance(items, list):
            items = [items]

        if config_id:
            url = '{0}/categories.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/categories.{1}'.format(self.host, self.format)
        wrapper = self._getTypeWrapper("delete_categories")
        data = self.serializer.serialize(items, wrapper)
        return self._runRequest("DELETE", url=url, postData=data)

    def getQueries(self, config_id=None):
        if config_id:
            url = '{0}/queries.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/queries.{1}'.format(self.host, self.format)

        result = self._runRequest("GET", url, "get_queries")
        if result is None:
            result = []
        return result

    def addQueries(self, items, config_id=None):
        return self.updateQueries(items, config_id, create=True)

    def updateQueries(self, items, config_id=None, create=False):
        if not isinstance(items, list):
            items = [items]

        if config_id:
            url = '{0}/queries.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/queries.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("update_queries")
        data = self.serializer.serialize(items, wrapper)
        return self._runRequest(("POST" if create else "PUT"), url=url, postData=data)

    def removeQueries(self, items, config_id=None):
        if not isinstance(items, list):
            items = [items]

        if config_id:
            url = '{0}/queries.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/queries.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("update_queries")
        data = self.serializer.serialize(items, wrapper)
        return self._runRequest("DELETE", url=url, postData=data)

    def getPhrases(self, config_id=None):
        if config_id:
            url = '{0}/phrases.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/phrases.{1}'.format(self.host, self.format)

        result = self._runRequest("GET", url, "get_sentiment_phrases")
        if result is None:
            result = []
        return result

    def addPhrases(self, items, config_id=None):
        return self.updatePhrases(items, config_id, create=True)

    def updatePhrases(self, items, config_id=None, create=False):
        if not isinstance(items, list):
            items = [items]

        if config_id:
            url = '{0}/phrases.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/phrases.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("update_sentiment_phrases")
        data = self.serializer.serialize(items, wrapper)
        return self._runRequest(("POST" if create else "PUT"), url=url, postData=data)

    def removePhrases(self, items, config_id=None):
        if not isinstance(items, list):
            items = [items]

        if config_id:
            url = '{0}/phrases.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/phrases.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("delete_sentiment_phrases")
        data = self.serializer.serialize(items, wrapper)
        return self._runRequest("DELETE", url=url, postData=data)

    def getEntities(self, config_id=None):
        if config_id:
            url = '{0}/entities.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/entities.{1}'.format(self.host, self.format)

        result = self._runRequest("GET", url, "get_entities")
        if result is None:
            result = []
        return result

    def addEntities(self, items, config_id=None):
        return self.updateEntities(items, config_id, create=True)

    def updateEntities(self, items, config_id=None, create=False):
        if not isinstance(items, list):
            items = [items]

        if config_id:
            url = '{0}/entities.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/entities.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("update_entities")
        data = self.serializer.serialize(items, wrapper)
        return self._runRequest(("POST" if create else "PUT"), url, None, data)

    def removeEntities(self, items, config_id=None):
        if not isinstance(items, list):
            items = [items]

        if config_id:
            url = '{0}/entities.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/entities.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("delete_entities")
        data = self.serializer.serialize(items, wrapper)
        return self._runRequest("DELETE", url, None, data)

    def getTaxonomy(self, config_id=None):
        if config_id:
            url = '{0}/taxonomy.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/taxonomy.{1}'.format(self.host, self.format)

        result = self._runRequest("GET", url, "get_taxonomy")
        if result is None:
            result = []
        return result

    def addTaxonomy(self, items, config_id=None):
        return self.updateTaxonomy(items, config_id, create=True)

    def updateTaxonomy(self, items, config_id=None, create=False):
        if not isinstance(items, list):
            items = [items]

        if config_id:
            url = '{0}/taxonomy.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/taxonomy.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("update_taxonomy")
        data = self.serializer.serialize(items, wrapper)
        return self._runRequest(("POST" if create else "PUT"), url, None, data)

    def removeTaxonomy(self, items, config_id=None):
        if not isinstance(items, list):
            items = [items]

        if config_id:
            url = '{0}/taxonomy.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/taxonomy.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("delete_taxonomy")
        data = self.serializer.serialize(items, wrapper)
        return self._runRequest("DELETE", url, None, data)

    def queueDocument(self, task, config_id=None):
        if config_id:
            url = '{0}/document.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/document.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("queue_document")
        data = self.serializer.serialize(task, wrapper)
        result = self._runRequest("POST", url, "get_processed_documents", data)
        if result is not None and not isinstance(result, int):
            self.DocsAutoResponse(result)
            return 200
        else:
            return result

    def queueBatch(self, batch, config_id=None):
        if config_id:
            url = '{0}/document/batch.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/document/batch.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("queue_batch_documents")
        data = self.serializer.serialize(batch, wrapper)

        result = self._runRequest("POST", url, "get_processed_documents", data)
        if result is not None and not isinstance(result, int):
            self.DocsAutoResponse(result)
            return 200
        else:
            return result

    def getDocument(self, doc_id, config_id=None):
        if not doc_id:
            raise SemantriaError('Parameter not found: %s' % doc_id)

        if config_id:
            url = '{0}/document/{1}.{2}?config_id={3}'.format(self.host, doc_id, self.format, config_id)
        else:
            url = '{0}/document/{1}.{2}'.format(self.host, doc_id, self.format)

        return self._runRequest("GET", url, "get_document")

    def cancelDocument(self, doc_id, config_id=None):
        if not doc_id:
            raise SemantriaError('Parameter not found: %s' % doc_id)

        if config_id:
            url = '{0}/document/{1}.{2}?config_id={3}'.format(self.host, doc_id, self.format, config_id)
        else:
            url = '{0}/document/{1}.{2}'.format(self.host, doc_id, self.format)

        return self._runRequest("DELETE", url)

    def getProcessedDocuments(self, config_id=None):
        if config_id:
            url = '{0}/document/processed.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/document/processed.{1}'.format(self.host, self.format)

        result = self._runRequest("GET", url, "get_processed_documents")
        if result is None:
            result = []
        return result

    def getProcessedDocumentsByJobId(self, job_id):
        url = '{0}/document/processed.{1}?job_id={2}'.format(self.host, self.format, job_id)

        result = self._runRequest("GET", url, "get_processed_documents_by_job_id")
        if result is None:
            result = []
        return result

    def queueCollection(self, task, config_id=None):
        if config_id:
            url = '{0}/collection.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/collection.{1}'.format(self.host, self.format)

        wrapper = self._getTypeWrapper("queue_collection")
        data = self.serializer.serialize(task, wrapper)
        result = self._runRequest("POST", url, "get_processed_collections", data)
        if result is not None and not isinstance(result, int):
            self.CollsAutoResponse(result)
            return 200
        else:
            return result

    def getCollection(self, coll_id, config_id=None):
        if not coll_id:
            raise SemantriaError('Parameter not found: %s' % coll_id)

        if config_id:
            url = '{0}/collection/{1}.{2}?config_id={3}'.format(self.host, coll_id, self.format, config_id)
        else:
            url = '{0}/collection/{1}.{2}'.format(self.host, coll_id, self.format)

        return self._runRequest("GET", url, "get_collection")

    def cancelCollection(self, coll_id, config_id=None):
        if not coll_id:
            raise SemantriaError('Parameter not found: %s' % coll_id)
        if config_id:
            url = '{0}/collection/{1}.{2}?config_id={3}'.format(self.host, coll_id, self.format, config_id)
        else:
            url = '{0}/collection/{1}.{2}'.format(self.host, coll_id, self.format)

        return self._runRequest("DELETE", url)

    def getProcessedCollections(self, config_id=None):
        if config_id:
            url = '{0}/collection/processed.{1}?config_id={2}'.format(self.host, self.format, config_id)
        else:
            url = '{0}/collection/processed.{1}'.format(self.host, self.format)

        result = self._runRequest("GET", url, "get_processed_collections")
        if result is None:
            result = []
        return result

    def getProcessedCollectionsByJobId(self, job_id):
        url = '{0}/collection/processed.{1}?job_id={2}'.format(self.host, self.format, job_id)

        result = self._runRequest("GET", url, "get_processed_collections_by_job_id")
        if result is None:
            result = []
        return result

    def _getTypeHandler(self, type_):
        if self.serializer.gettype() == "json":
            return None

        #only for xml serializer
        # if type_ == "get_status":
        #     return GetStatusHandler()
        # elif type_ == "get_subscription":
        #     return GetSubscriptionHandler()
        # elif type_ == "get_configurations":
        #     return GetConfigurationsHandler()
        # elif type_ == "get_blacklist":
        #     return GetBlacklistHandler()
        # elif type_ == "get_categories":
        #     return GetCategoriesHandler()
        # elif type_ == "get_queries":
        #     return GetQueriesHandler()
        # elif type_ == "get_sentiment_phrases":
        #     return GetSentimentPhrasesHandler()
        # elif type_ == "get_entities":
        #     return GetEntitiesHandler()
        # elif type_ == "get_document":
        #     return GetDocumentHandler()
        # elif type_ == "get_processed_documents":
        #     return GetProcessedDocumentsHandler()
        # elif type_ == "get_collection":
        #     return GetCollectionHandler()
        # elif type_ == "get_processed_collections":
        #     return GetProcessedCollectionsHandler()
        # else:
        #     return None

    def _getTypeWrapper(self, type_):
        if self.serializer.gettype() == "json":
            return None

        #only for xml serializer
        if type_ == "update_configurations":
            return {"root": "configurations", "added": "configuration", "removed": "configuration"}
        elif type_ == "update_blacklist":
            return {"root": "blacklist", "added": "item", "removed": "item"}
        elif type_ == "update_categories":
            return {"root": "categories", "added": "category", "removed": "category", "samples": "sample"}
        elif type_ == "update_queries":
            return {"root": "queries", "added": "query", "removed": "query"}
        elif type_ == "update_sentiment_phrases":
            return {"root": "phrases", "added": "phrase", "removed": "phrase"}
        elif type_ == "update_entities":
            return {"root": "entities", "added": "entity", "removed": "entity"}
        elif type_ == "queue_document":
            return {"root": "document"}
        elif type_ == "queue_batch_documents":
            return {"root": "documents", "item": "document"}
        elif type_ == "queue_collection":
            return {"root": "collection", "documents": "document"}
        else:
            return None

    def _runRequest(self, method, url, type_=None, postData=None):
        self.Request({"method": method, "url": url, "message": postData})
        response = self._request.authWebRequest(method, url, postData)
        self.Response({
            "status": response["status"],
            "reason": response["reason"],
            "message": response["data"]
        })

        status = response["status"]
        message = response["reason"]

        if response["data"]:
            message = response["data"]

        if method == "DELETE":
            if status == 200 or status == 202:
                return status
            else:
                self._resolveError(status, message)
                return status
        else:
            if status == 200:
                handler = self._getTypeHandler(type_)
                message = self.serializer.deserialize(response["data"], handler)
                return message
            elif status == 202:
                return status if method == "POST" else None
            else:
                self._resolveError(status, message)

    def _resolveError(self, status, message=None):
        if status == 400 or status == 401 or status == 402 or status == 403 or status == 406 or status == 500:
            self.Error({"status": status, "message": message})
        else:
            raise httplib.HTTPException(status, message)


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

    def fire(self, *args, **kwargs):
        for handler in self.handlers: 
            handler(self.sender, *args, **kwargs)

    def getHandlerCount(self): 
        return len(self.handlers) 

    __iadd__ = handle 
    __isub__ = unhandle 
    __call__ = fire 
    __len__ = getHandlerCount
