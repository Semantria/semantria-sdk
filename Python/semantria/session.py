# -*- coding: utf-8 -*-

try:
    from http.client import HTTPException
except ImportError:
    from httplib import HTTPException

try:
    from urllib.parse import quote as url_quote
except ImportError:
    from urllib import quote as url_quote

import json
import os, os.path
from semantria.authrequest import *
from semantria.error import SemantriaError
from semantria.jsonserializer import *
from semantria.version import WRAPPER_VERSION


class Session(object):

    host = 'https://api.semantria.com'
    wrapperName = 'Python{}.{}/{}'.format(sys.version_info[0], sys.version_info[1], WRAPPER_VERSION)
    default_key_url='https://semantria.com/auth/session'
    default_app_key='8f46c3c2-ca89-01aa-aad3-f437ea98cf7f'

    def __init__(self, consumerKey=None, consumerSecret=None,
                 serializer=None, applicationName=None, use_compression=True,
                 username=None, password=None,
                 session_file = None,
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

    def __repr__(self):
        return ("{}({!r})".format(self.__class__.__name__, self.host))
   

    def obtainKeys(self, username, password,
                   session_file=None,
                   auth_key_url=default_key_url,
                   auth_app_key=default_app_key):
        import requests

        if not session_file:
            session_file = getSessionDataFile(username)
        session_id = self.getSavedSessionId(session_file, username)

        if session_id:
            url = '{0}/{1}.json?appkey={2}'.format(auth_key_url, session_id, auth_app_key)
            response = requests.get(url)
            if response.status_code == 200:
                json_data = response.json()
                return json_data['custom_params']['key'], json_data['custom_params']['secret']

        url = '{0}.json?appkey={1}'.format(auth_key_url, auth_app_key)
        data = {'username': username, 'password': password}

        response = requests.post(url, data=json.dumps(data))
        if response.status_code != 200:
            return False, False

        json_data = response.json()
        self.saveSessionId(session_file, username, json_data['id'])

        return json_data['custom_params']['key'], json_data['custom_params']['secret']

    # Cache session id in a simple two line format:
    #   username
    #   session id
    def saveSessionId(self, session_file, username, session_id):
        if not session_file:
            return
        with open(session_file, mode='w') as f:
            f.seek(0)
            f.write(username)
            f.write('\n')
            f.write(session_id)
            f.write('\n')

    # Returns session id if a reasonable one is found
    def getSavedSessionId(self, session_file, username):
        if (not session_file) or (not os.path.exists(session_file)):
            return None
        with open(session_file, mode='r') as f:
            u = f.readline().strip()
            if u != username:
                return None
            return f.readline().strip()
                

    def registerSerializer(self, serializer):
        if not serializer:
            raise SemantriaError('Parameter \'serializer\' must not be empty')
        self.serializer = serializer
        self.format = serializer.gettype()

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
            raise SemantriaError('One of interval, start or end must be specified')
        if interval and (interval.lower() not in ["hour", "day", "week", "month", "year"]):
            raise SemantriaError('Unknown value for interval: {}. Expected hour, day, week, month or year'
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
        if not name:
            raise SemantriaError('Parameter \'name\' must not be empty')
        items = {"name": name, "template": template}
        return self.addConfigurations(items)

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
            raise SemantriaError('Parameter \'doc_id\' must not be empty')
        doc_id = url_quote(doc_id, safe='')
        url = self.makeUrl('/document/{0}'.format(doc_id), config_id)
        return self._runRequest("GET", url, "get_document")

    def cancelDocument(self, doc_id, config_id=None):
        if not doc_id:
            raise SemantriaError('Parameter \'doc_id\' must not be empty')
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
            raise SemantriaError('Parameter \'collection_id\' must not be empty')
        collection_id = url_quote(collection_id, safe='')
        url = self.makeUrl('/collection/{0}'.format(collection_id), config_id)
        return self._runRequest("GET", url, "get_collection")

    def cancelCollection(self, collection_id, config_id=None):
        if not collection_id:
            raise SemantriaError('Parameter \'collection_id\' must not be empty')
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

    # Gets the salience user data directory files for a config as a tar
    # or zip archive. Writes the archive as a file if path is given
    # otherwise returns a byte array. If there is an error getting the
    # archive data, then any error handlers are called and this function
    # returns the HTTP status. If there is an error writing the archive
    # file then an exception is raised.
    def getUserDirectory(self, config_id=None, path=None, format=None):
        format = self.getArchiveFormat(path, format)
        url = self.makeUrl('/salience/user-directory', config_id, format=format)
        response = self._request.authWebRequest("GET", url, None,
                                                headers=self.http_headers)
        if not isSuccess(response['status']):
            self._resolveError(response)
            return response['status']
        if path:
            with open(path, mode='wb') as stream:
                stream.write(response['data'])
            return response['status']
        else:
            return response['data']

    # Determines the archive format. If an unknown format is given or no
    # format is given then use zip format. Format is determined first
    # from the path (if given) then from the the format parameter.
    def getArchiveFormat(self, path, format):
        if path:
            path_lower = path.lower()
            if path_lower.endswith('.tar.gz'):
                return 'tar.gz'
            if path_lower.endswith('.tar'):
                return 'tar'
            if path_lower.endswith('.zip'):
                return 'zip'
        if format:
            if format in ['tar.gz', 'tar', 'zip']:
                return format
            if format in ['.tar.gz', '.tar', '.zip']:
                return format[1:]
        # zip format is the default
        return 'zip'


    def makeUrl(self, path, config_id=None, params=None, format=None):
        # allow null string to indicate no extra format extension on url
        if format is None:
            format = self.format
        if self.host.endswith('/'):
            self.host = self.host[:-1]
        if not path.startswith('/'):
            path = '/' + path
        url = self.host + path
        if format:
            url += '.' + format
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
            if isSuccess(status):
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
            raise HTTPException(status, message)


class SessionEvent:
    def __init__(self, sender): 
        self.handlers = set()
        self.sender = sender 

    def __repr__(self):
        return ("{}({!r})".format(self.__class__.__name__, self.handlers))
   
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


def isSuccess(status):
    try:
        code = int(status)
        return ((code >= 200) and (code < 300))
    except:
        sys.stderr.write("ERROR: Bad status from request: {}. {}\n".format(status, sys.exc_info()))
        traceback.print_tb(sys.exc_info()[2])
        sys.stderr.write("\n")
        return False

# Look in several directories for place to put the session data file.
# Return the first found or None if no suitable directoris are found.
def getSessionDataFile(username):
    for path in [os.getenv('TEMP'), '/tmp', '/temp']:
        if path and os.path.isdir(path):
            safe_username = ''.join(ch for ch in username if (ch.isalnum() or (ch in '-_.')))
            return os.path.join(path, 'semantria-session-{}.dat'.format(safe_username))
    return None


