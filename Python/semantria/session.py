# -*- coding: utf-8 -*-

try:
    import http.client as httplib
except ImportError:
    import httplib

try:
    from urllib.parse import quote as url_quote
except ImportError:
    from urllib import quote as url_quote

import json
import os
from semantria.authrequest import *
from semantria.error import SemantriaError
from semantria.jsonserializer import *
from semantria.version import WRAPPER_VERSION


class Session(object):

    host = 'https://api.semantria.com'
    wrapperName = 'Python/' + WRAPPER_VERSION
    default_key_url='https://semantria.com/auth/session',
    default_app_key='8f46c3c2-ca89-01aa-aad3-f437ea98cf7f'

    def __init__(self, consumerKey=None, consumerSecret=None,
                 serializer=None, applicationName=None, use_compression=False,
                 username=None, password=None,
                 session_file ='/tmp/session.dat',
                 auth_key_url=default_key_url,
                 auth_app_key=default_app_key):

        if username and password:
            consumerKey, consumerSecret = self.obtainKeys(
                username, password, session_file, auth_key_url, auth_app_key)
            if not consumerKey or not consumerSecret:
                raise Exception('Cannot obtain Semantria keys. Wrong username or password.')

        self.consumerKey = consumerKey
        self.consumerSecret = consumerSecret
        self.use_compression = use_compression
        self.http_headers = {}

        if applicationName:
            self.applicationName = '{0}/{1}'.format(applicationName, self.wrapperName)
        else:
            self.applicationName = self.wrapperName

        if serializer:
            self.serializer = serializer
        else:
            # set default json serializer
            self.serializer = JsonSerializer()
        self.format = self.serializer.gettype()

        self.applicationName = self.applicationName + "/" + self.format.upper()

        self._request = AuthRequest(
            self.consumerKey,
            self.consumerSecret,
            self.applicationName,
            self.use_compression)

        # Events
        self.Request = SessionEvent(self)
        self.Response = SessionEvent(self)
        self.Error = SessionEvent(self)
        self.DocsAutoResponse = SessionEvent(self)
        self.CollsAutoResponse = SessionEvent(self)

    def obtainKeys(self, username, password,
                   session_file='/tmp/semantria-session.dat',
                   auth_key_url=default_key_url,
                   auth_app_key=default_app_key):
        import requests

        if os.path.exists(session_file):
            with open(session_file) as f:
                f.seek(0)
                session_id = f.readline()

            url = '{0}/{1}.json?appkey={2}'.format(auth_key_url, session_id, auth_app_key)
            res = requests.get(url)

            if res.status_code == 200:
                json_res = res.json()
                return json_res['custom_params']['key'], json_res['custom_params']['secret']

        url = '{0}.json?appkey={1}'.format(auth_key_url, auth_app_key)
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

    # Sets an HTTP header for all new HTTP requests. If a header with
    # the key already exists, overwrite its value with the new value.
    # Any existing requests will not be affected.
    # 
    # NOTE: HTTP requires all request properties which can legally have
    # multiple instances with the same key to use a comma-separated list
    # syntax which enables multiple properties to be appended into a
    # single property.
    def setHttpHeader(self, key, value):
        self.http_headers[key] = value

    def setHttpHeaders(self, new_headers):
        self.http_headers = new_headers.copy() if new_headers else {}

    def getApiVersion(self):
        return self._request.apiVersion

    def setApiVersion(self, apiVersion):
        self._request.apiVersion = apiVersion

    def getStatus(self):
        url = self.makeUrl('/status')
        return self._runRequest("GET", url, "get_status")

    def getSupportedFeatures(self, language=None):
        url = self.makeUrl('/features', params={'language': language})
        return self._runRequest("GET", url, "get_features")

    def getSubscription(self):
        url = self.makeUrl('/subscription')
        return self._runRequest("GET", url, "get_subscription")

    def getStatistics(self, configId=None, interval=None, start=None, end=None,
                      config_name=None, language=None, app=None, group=None):
        """ Returns usage statistics
        """
        if not (interval or start or end):
            raise RuntimeError('One of interval, start or end must be specified')
        if interval and (interval.lower() not in ["hour", "day", "week", "month", "year"]):
            raise RuntimeError('Unknown value for interval: {}. Expected hour, day, week, month or year'
                               .format(interval))

        params = {'config_id': configId,
                  'config_name': config_name,
                  'interval': interval,
                  'from': start,
                  'to': end,
                  'language': language,
                  'app': app,
                  'group': group} 
        url = self.makeUrl('/statistics', params=params)

        return self._runRequest("GET", url, "get_statistics")

    def getConfigurations(self):
        url = self.makeUrl('/configurations')
        result = self._runRequest("GET", url, "get_configurations")
        if result is None:
            result = []
        return result

    def addConfigurations(self, items):
        return self.updateConfigurations(items, create=True)

    def updateConfigurations(self, items, create=False):
        if not isinstance(items, list):
            items = [items]

        url = self.makeUrl('/configurations')
        data = self.serializer.serialize(items)
        return self._runRequest(("POST" if create else "PUT"), url, None, data)

    def cloneConfiguration(self, name, template):
        if name is None:
            name = ''
        items = [name, template]
        return self.updateConfigurations(items)

    def removeConfigurations(self, config_id):
        url = self.makeUrl('/configurations')
        data = []
        if not isinstance(config_id, list):
            data.append(config_id)
        else:
            data = config_id
        data = self.serializer.serialize(data)
        return self._runRequest("DELETE", url=url, postData=data)

    def getBlacklist(self, config_id=None):
        url = self.makeUrl('/blacklist', config_id)
        result = self._runRequest("GET", url, "get_blacklist")
        if result is None:
            result = []
        return result

    def addBlacklist(self, items, config_id=None):
        return self.updateBlacklist(items, config_id, create=True)

    def updateBlacklist(self, items, config_id=None, create=False):
        if not isinstance(items, list):
            items = [items]

        url = self.makeUrl('/blacklist', config_id)
        data = self.serializer.serialize(items)
        return self._runRequest(("POST" if create else "PUT"), url=url, postData=data)

    def removeBlacklist(self, items, config_id=None):
        if not isinstance(items, list):
            items = [items]

        url = self.makeUrl('/blacklist', config_id)
        data = self.serializer.serialize(items)
        return self._runRequest("DELETE", url=url, postData=data)

    def getCategories(self, config_id=None):
        url = self.makeUrl('/categories', config_id)
        result = self._runRequest("GET", url, "get_categories")
        if result is None:
            result = []
        return result

    def addCategories(self, items, config_id=None):
        return self.updateCategories(items, config_id, create=True)

    def updateCategories(self, items, config_id=None, create=False):
        if not isinstance(items, list):
            items = [items]

        url = self.makeUrl('/categories', config_id)
        data = self.serializer.serialize(items)
        return self._runRequest(("POST" if create else "PUT"), url=url, postData=data)

    def removeCategories(self, items, config_id=None):
        if not isinstance(items, list):
            items = [items]

        url = self.makeUrl('/categories', config_id)
        data = self.serializer.serialize(items)
        return self._runRequest("DELETE", url=url, postData=data)

    def getQueries(self, config_id=None):
        url = self.makeUrl('/queries', config_id)
        result = self._runRequest("GET", url, "get_queries")
        if result is None:
            result = []
        return result

    def addQueries(self, items, config_id=None):
        return self.updateQueries(items, config_id, create=True)

    def updateQueries(self, items, config_id=None, create=False):
        if not isinstance(items, list):
            items = [items]

        url = self.makeUrl('/queries', config_id)
        data = self.serializer.serialize(items)
        return self._runRequest(("POST" if create else "PUT"), url=url, postData=data)

    def removeQueries(self, items, config_id=None):
        if not isinstance(items, list):
            items = [items]

        url = self.makeUrl('/queries', config_id)
        data = self.serializer.serialize(items)
        return self._runRequest("DELETE", url=url, postData=data)

    def getPhrases(self, config_id=None):
        url = self.makeUrl('/phrases', config_id)
        result = self._runRequest("GET", url, "get_sentiment_phrases")
        if result is None:
            result = []
        return result

    def addPhrases(self, items, config_id=None):
        return self.updatePhrases(items, config_id, create=True)

    def updatePhrases(self, items, config_id=None, create=False):
        if not isinstance(items, list):
            items = [items]

        url = self.makeUrl('/phrases', config_id)
        data = self.serializer.serialize(items)
        return self._runRequest(("POST" if create else "PUT"), url=url, postData=data)

    def removePhrases(self, items, config_id=None):
        if not isinstance(items, list):
            items = [items]

        url = self.makeUrl('/phrases', config_id)
        data = self.serializer.serialize(items)
        return self._runRequest("DELETE", url=url, postData=data)

    def getEntities(self, config_id=None):
        url = self.makeUrl('/entities', config_id)
        result = self._runRequest("GET", url, "get_entities")
        if result is None:
            result = []
        return result

    def addEntities(self, items, config_id=None):
        return self.updateEntities(items, config_id, create=True)

    def updateEntities(self, items, config_id=None, create=False):
        if not isinstance(items, list):
            items = [items]

        url = self.makeUrl('/entities', config_id)
        data = self.serializer.serialize(items)
        return self._runRequest(("POST" if create else "PUT"), url, None, data)

    def removeEntities(self, items, config_id=None):
        if not isinstance(items, list):
            items = [items]

        url = self.makeUrl('/entities', config_id)
        data = self.serializer.serialize(items)
        return self._runRequest("DELETE", url, None, data)

    def getTaxonomy(self, config_id=None):
        url = self.makeUrl('/taxonomy', config_id)
        result = self._runRequest("GET", url, "get_taxonomy")
        if result is None:
            result = []
        return result

    def addTaxonomy(self, items, config_id=None):
        return self.updateTaxonomy(items, config_id, create=True)

    def updateTaxonomy(self, items, config_id=None, create=False):
        if not isinstance(items, list):
            items = [items]

        url = self.makeUrl('/taxonomy', config_id)
        data = self.serializer.serialize(items)
        return self._runRequest(("POST" if create else "PUT"), url, None, data)

    def removeTaxonomy(self, items, config_id=None):
        if not isinstance(items, list):
            items = [items]

        url = self.makeUrl('/taxonomy', config_id)
        data = self.serializer.serialize(items)
        return self._runRequest("DELETE", url, None, data)

    def queueDocument(self, task, config_id=None):
        url = self.makeUrl('/document', config_id)
        data = self.serializer.serialize(task)
        result = self._runRequest("POST", url, "get_processed_documents", data)
        if result is not None and not isinstance(result, int):
            self.DocsAutoResponse(result)
            return 200
        else:
            return result

    def queueBatch(self, batch, config_id=None):
        url = self.makeUrl('/document/batch', config_id)
        data = self.serializer.serialize(batch)

        result = self._runRequest("POST", url, "get_processed_documents", data)
        if result is not None and not isinstance(result, int):
            self.DocsAutoResponse(result)
            return 200
        else:
            return result

    def getDocument(self, doc_id, config_id=None):
        if not doc_id:
            raise SemantriaError('Parameter not found: %s' % doc_id)
        doc_id = url_quote(doc_id, safe='')
        url = self.makeUrl('/document/{0}'.format(doc_id), config_id)
        return self._runRequest("GET", url, "get_document")

    def cancelDocument(self, doc_id, config_id=None):
        if not doc_id:
            raise SemantriaError('Parameter not found: %s' % doc_id)
        doc_id = url_quote(doc_id, safe='')
        url = self.makeUrl('/document/{0}'.format(doc_id), config_id)
        return self._runRequest("DELETE", url)

    def getProcessedDocuments(self, config_id=None):
        url = self.makeUrl('/document/processed', config_id)

        result = self._runRequest("GET", url, "get_processed_documents")
        if result is None:
            result = []
        return result

    def getProcessedDocumentsByJobId(self, job_id):
        url = self.makeUrl('/document/processed', params={'job_id': job_id})

        result = self._runRequest("GET", url, "get_processed_documents_by_job_id")
        if result is None:
            result = []
        return result

    def queueCollection(self, task, config_id=None):
        url = self.makeUrl('/collection', config_id)
        data = self.serializer.serialize(task)
        result = self._runRequest("POST", url, "get_processed_collections", data)
        if result is not None and not isinstance(result, int):
            self.CollsAutoResponse(result)
            return 200
        else:
            return result

    def getCollection(self, collection_id, config_id=None):
        if not collection_id:
            raise SemantriaError('Parameter not found: %s' % collection_id)

        collection_id = url_quote(collection_id, safe='')
        url = self.makeUrl('/collection/{0}'.format(collection_id), config_id)
        return self._runRequest("GET", url, "get_collection")

    def cancelCollection(self, collection_id, config_id=None):
        if not collection_id:
            raise SemantriaError('Parameter not found: %s' % collection_id)

        collection_id = url_quote(collection_id, safe='')
        url = self.makeUrl('/collection/{0}'.format(collection_id), config_id)
        return self._runRequest("DELETE", url)

    def getProcessedCollections(self, config_id=None):
        url = self.makeUrl('/collection/processed', config_id)

        result = self._runRequest("GET", url, "get_processed_collections")
        if result is None:
            result = []
        return result

    def getProcessedCollectionsByJobId(self, job_id):
        url = self.makeUrl('/collection/processed', params={'job_id': job_id})
        result = self._runRequest("GET", url, "get_processed_collections_by_job_id")
        if result is None:
            result = []
        return result

    def makeUrl(self, path, config_id=None, params=None):
        url = '{0}{1}.{2}'.format(self.host, path, self.format)
        if params:
            param_string = '&'.join(['{0}={1}'.format(k, params[k])
                                     for k in params if params[k]])
            if config_id:
                if param_string:
                    param_string += '&'
                param_string += 'config_id={0}'.format(config_id)
            url += '?{0}'.format(param_string)
        elif config_id:
            url += '?config_id={0}'.format(config_id)
        return url


    def _runRequest(self, method, url, type_=None, postData=None):
        self.Request({"method": method, "url": url, "message": postData})
        response = self._request.authWebRequest(method, url, postData,
                                                headers=self.http_headers)
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
                self._resolveError(response)
                return status
        else:
            if status == 200:
                return self.serializer.deserialize(response["data"])
            elif status == 202:
                return status if method == "POST" else None
            else:
                self._resolveError(response)

    def _resolveError(self, response):
        status = response["status"]
        if response["data"]:
            message = '{}: {}'.format(response["reason"], response["data"])
        else:
            message = response["reason"]
        if status in [400, 401, 402, 403, 406, 500]:
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

