# -*- coding: utf-8 -*-

try:
    from urllib.parse import urlparse, urlunparse, urlencode, quote
except ImportError:
    from urlparse import urlparse, urlunparse
    from urllib import urlencode, quote

import requests
import json
import time
import random
import hmac
import binascii
import hashlib
import sys
from semantria.version import DEFAULT_API_VERSION

OAuthVersion = "1.0"
OAuthParameterPrefix = "oauth_"
OAuthConsumerKeyKey = "oauth_consumer_key"
OAuthVersionKey = "oauth_version"
OAuthSignatureMethodKey = "oauth_signature_method"
OAuthSignatureKey = "oauth_signature"
OAuthTimestampKey = "oauth_timestamp"
OAuthNonceKey = "oauth_nonce"


def isPython3():
    return sys.version_info[0] >= 3

class AuthRequest:

    def __init__(self, consumerKey, consumerSecret, applicationName, use_compression=True):
        self.consumerKey = consumerKey
        self.consumerSecret = consumerSecret
        self.applicationName = applicationName
        self.use_compression = use_compression
        self.apiVersion = DEFAULT_API_VERSION

    def authWebRequest(self, method, url, postData, headers=None):
        nonce = self.generateNonce()
        timestamp = self.generateTimestamp()
        request_url = self.generateAuthUrl(method, url, timestamp, nonce)
        auth_header = self.generateAuthHeader(request_url, timestamp, nonce)
        request_headers = self.generateHeaders(method, headers, auth_header)
        request_data = self.getRequestDataBytes(postData)

        if not postData:
            request_data = None
        elif isinstance(postData, str) or ((not isPython3()) and isinstance(postData, unicode)):
            request_data = toBytes(postData)
        elif isinstance(postData, dict):
            request_data = toBytes(jsons.dumps(postData))
        else:
            request_data = postData
            
        response = requests.request(method, request_url, data=request_data, headers=request_headers)
        response_data = response.content

        try:
            # Deserialize to json, then serialize again. 
            # Protects against possible char encoding problems.
            response_data = json.dumps(response.json())
        except ValueError:
            pass

        result = {"status": response.status_code, "reason": response.reason, "data": response_data}
        return result

    def getApiVersion(self):
        return self.apiVersion

    def setApiVersion(self, apiVersion):
        self.apiVersion = apiVersion

    def generateHeaders(self, method, headers, auth_header):
        request_headers = headers.copy() if headers else dict()
        request_headers["Authorization"] = auth_header
        #if method == "POST":
        #    request_headers["Content-type"] = "application/json"
        if self.apiVersion:
            request_headers["x-api-version"] = self.apiVersion
        if self.applicationName:
            request_headers["x-app-name"] = self.applicationName
        if self.use_compression:
            request_headers["Accept-encoding"] = "gzip"
        return request_headers

    def generateAuthUrl(self, method, url, timestamp, nonce):
        parts = urlparse(url)
        scheme, netloc, path, params, query, fragment = parts[:6]
        np = self.getNormalizedParameters(timestamp, nonce)

        if query:
            query = '%s&oauth_verifier=%s' % (query, np)
        else:
            query = '%s' % np
        
        return urlunparse((scheme, netloc, path, params, query, fragment))

    def generateAuthHeader(self, url, timestamp, nonce):
        md5cs = self.getMD5Hash(self.consumerSecret)
        escaped_url = escape(url)
        hash_ = self.getSHA1(md5cs, escaped_url)
        hash_ = escape(hash_)

        items = [
            ("OAuth realm", ""),
            (OAuthVersionKey, "%s" % OAuthVersion),
            (OAuthTimestampKey, "%s" % timestamp),
            (OAuthNonceKey, "%s" % nonce),
            (OAuthSignatureMethodKey, "HMAC-SHA1"),
            (OAuthConsumerKeyKey, "%s" % self.consumerKey),
            (OAuthSignatureKey, "%s" % hash_)
        ]
        items.sort()

        params = []
        for k, v in items:
            params.append('%s="%s"' % (k, v))

        return ",".join(params)

    def getNormalizedParameters(self, timestamp, nonce):
        items = [
            (OAuthVersionKey, OAuthVersion),
            (OAuthTimestampKey, timestamp),
            (OAuthNonceKey, nonce),
            (OAuthSignatureMethodKey, "HMAC-SHA1"),
            (OAuthConsumerKeyKey, self.consumerKey)
        ]
        items.sort()
        
        encoded_str = urlencode(items)
        # Encode signature parameters per Oauth Core 1.0 protocol
        # Spaces must be encoded with "%20" instead of "+"
        return encoded_str.replace('+', '%20').replace('%7E', '~')

    def getMD5Hash(self, str_):
        md5h = hashlib.md5()
        md5h.update(toBytes(str_))
        md5sig = md5h.hexdigest()
        return md5sig
 
    def getSHA1(self, md5cs, query):
        sha1res = hmac.new(toBytes(md5cs), toBytes(query), hashlib.sha1).digest()
        sha1sig = binascii.b2a_base64(sha1res)[:-1]
        return sha1sig

    def generateTimestamp(self):
        return int(time.time())

    def generateNonce(self, length=20):
        return ''.join([str(random.randint(0, 9)) for i in range(length)])
    
    def getRequestDataBytes(self, postData):
        if not postData:
            return None
        elif isinstance(postData, str) or ((not isPython3()) and isinstance(postData, unicode)):
            return toBytes(postData)
        elif isinstance(postData, dict):
            return toBytes(jsons.dumps(postData))
        else:
            return postData
            

def escape(s):
    return quote(s, safe='~')

def toBytes(x):
    if isPython3():
        return bytes(x, 'utf-8')
    else:
        return x.encode('utf-8')

