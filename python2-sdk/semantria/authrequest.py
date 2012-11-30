# -*- coding: utf-8 -*-
import httplib
import base64
import urllib
import time
import random
import urlparse
import hmac
import binascii
import hashlib

OAuthVersion = "1.0"
OAuthParameterPrefix = "oauth_"
OAuthConsumerKeyKey = "oauth_consumer_key"
OAuthVersionKey = "oauth_version"
OAuthSignatureMethodKey = "oauth_signature_method"
OAuthSignatureKey = "oauth_signature"
OAuthTimestampKey = "oauth_timestamp"
OAuthNonceKey = "oauth_nonce"

class AuthRequest:
    def __init__(self, consumerKey, consumerSecret, applicationName):
        self.consumerKey = consumerKey
        self.consumerSecret = consumerSecret
        self.applicationName = applicationName;

    def authWebRequest(self, method, url, postData):
        nonce = self.generateNonce()
        timestamp = self.generateTimestamp()
        query = self.generateQuery(method, url, timestamp, nonce)
        authheader = self.generateAuthHeader(query, timestamp, nonce)

        headers = {"Authorization": authheader}
        if (method == "POST"):
            headers["Content-type"] = "application/x-www-form-urlencoded"
        headers["x-api-version"] = "2"
        headers["x-app-name"] = self.applicationName
        
        qparts = urlparse.urlparse(query)
        qscheme, qnetloc, qpath, qparams, qquery, qfragment = qparts[:6]
        conn = httplib.HTTPSConnection(qnetloc)
        host = '%s?%s' % (qpath, qquery)
        conn.request(method, host, postData, headers)
        response = conn.getresponse()
        result = { "status":response.status, "reason":response.reason, "data":response.read() }
        conn.close()
        return result

    def generateQuery(self, method, url, timestamp, nonce):
        parts = urlparse.urlparse(url)
        scheme, netloc, path, params, query, fragment = parts[:6]
        np = self.getNormalizedParameters(timestamp, nonce)

        if query:
            query = '%s&oauth_verifier=%s' % (query, np)
        else:
            query = '%s' % np
        
        return urlparse.urlunparse((scheme, netloc, path, params, query, fragment))         

    def generateAuthHeader(self, query, timestamp, nonce):
        md5cs = self.getMD5Hash(self.consumerSecret)
        escquery = self.escape(query)
        hash = self.getSHA1(md5cs, escquery)
        hash = self.escape(hash)

        items = []
        items.append(("OAuth realm", ""))
        items.append((OAuthVersionKey, "%s" % OAuthVersion))
        items.append((OAuthTimestampKey, "%s" % timestamp))
        items.append((OAuthNonceKey, "%s" % nonce))
        items.append((OAuthSignatureMethodKey, "HMAC-SHA1"))
        items.append((OAuthConsumerKeyKey, "%s" % self.consumerKey))
        items.append((OAuthSignatureKey, "%s" % hash))
        items.sort()

        params = []
        for k, v in items:
            params.append('%s="%s"' % (k, v))

        return ",".join(params)

    def getNormalizedParameters(self, timestamp, nonce):
        items = []
        items.append((OAuthVersionKey, OAuthVersion))
        items.append((OAuthTimestampKey, timestamp))
        items.append((OAuthNonceKey, nonce))
        items.append((OAuthSignatureMethodKey, "HMAC-SHA1"))
        items.append((OAuthConsumerKeyKey, self.consumerKey))
        items.sort()
        
        encoded_str = urllib.urlencode(items)
        # Encode signature parameters per Oauth Core 1.0 protocol
        # Spaces must be encoded with "%20" instead of "+"
        return encoded_str.replace('+', '%20').replace('%7E', '~')

    def getMD5Hash(self, str):
        #bytestr = bytearray(str) 
        md5h = hashlib.md5()
        #md5h.update(bytestr)
        md5h.update(str)
        md5sig = md5h.hexdigest()
        return md5sig
 
    def getSHA1(self, md5cs, query):
        sha1res = hmac.new(md5cs, query, hashlib.sha1).digest()
        sha1sig = binascii.b2a_base64(sha1res)[:-1]
        return sha1sig

    def generateTimestamp(self):
        return int(time.time())

    def generateNonce(self, length=20):
        return ''.join([str(random.randint(0, 9)) for i in range(length)])
    
    def escape(self, s):
        return urllib.quote(s.encode('utf-8'), safe='~')
