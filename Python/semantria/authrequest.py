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

OAuthVersion = "1.0"
OAuthParameterPrefix = "oauth_"
OAuthConsumerKeyKey = "oauth_consumer_key"
OAuthVersionKey = "oauth_version"
OAuthSignatureMethodKey = "oauth_signature_method"
OAuthSignatureKey = "oauth_signature"
OAuthTimestampKey = "oauth_timestamp"
OAuthNonceKey = "oauth_nonce"


class AuthRequest:
    apiVersion = "4.2"

    def __init__(self, consumerKey, consumerSecret, applicationName, use_compression=False):
        self.consumerKey = consumerKey
        self.consumerSecret = consumerSecret
        self.applicationName = applicationName
        self.use_compression = use_compression

    def authWebRequest(self, method, url, postData, headers=None):
        nonce = self.generateNonce()
        timestamp = self.generateTimestamp()
        query = self.generateQuery(method, url, timestamp, nonce)
        authheader = self.generateAuthHeader(query, timestamp, nonce)

        request_headers = headers.copy() if headers else dict()
        request_headers["Authorization"] = authheader
        if method == "POST":
            request_headers["Content-type"] = "application/x-www-form-urlencoded"
        request_headers["x-api-version"] = self.apiVersion
        request_headers["x-app-name"] = self.applicationName
        if self.use_compression:
            request_headers["Accept-encoding"] = "gzip"

        qparts = urlparse(query)
        qscheme, qnetloc, qpath, qparams, qquery, qfragment = qparts[:6]

        host = 'https://%s%s?%s' % (qnetloc, qpath, qquery)

        params = {
            'data': postData,
            'headers': request_headers
        }

        response = requests.request(method, host, **params)
        data = response.content

        try:
            # Deserialize to json, then serialize again. 
            # Protects against possible char encoding problems.
            data = json.dumps(response.json())
        except ValueError:
            pass

        result = {"status": response.status_code, "reason": response.reason, "data": data}
        return result

    def getApiVersion(self):
        return self.apiVersion

    def setApiVersion(self, apiVersion):
        self.apiVersion = apiVersion

    def generateQuery(self, method, url, timestamp, nonce):
        parts = urlparse(url)
        scheme, netloc, path, params, query, fragment = parts[:6]
        np = self.getNormalizedParameters(timestamp, nonce)

        if query:
            query = '%s&oauth_verifier=%s' % (query, np)
        else:
            query = '%s' % np
        
        return urlunparse((scheme, netloc, path, params, query, fragment))

    def generateAuthHeader(self, query, timestamp, nonce):
        md5cs = self.getMD5Hash(self.consumerSecret)
        escquery = self.escape(query)
        hash_ = self.getSHA1(md5cs, escquery)
        hash_ = self.escape(hash_)

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
        if sys.version_info[0] >= 3:
            md5h.update(bytes(str_, 'utf-8'))
        else:
            md5h.update(str_)
        md5sig = md5h.hexdigest()
        return md5sig
 
    def getSHA1(self, md5cs, query):
        if sys.version_info[0] >= 3:  # Python 3
            sha1res = hmac.new(bytes(md5cs, 'utf-8'), bytes(query, 'utf-8'), hashlib.sha1).digest()
        else:
            sha1res = hmac.new(md5cs, query, hashlib.sha1).digest()

        sha1sig = binascii.b2a_base64(sha1res)[:-1]
        return sha1sig

    def generateTimestamp(self):
        return int(time.time())

    def generateNonce(self, length=20):
        return ''.join([str(random.randint(0, 9)) for i in range(length)])
    
    def escape(self, s):
        return quote(s, safe='~')
