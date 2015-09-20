# -*- coding: utf-8 -*-


from io import BytesIO
import gzip


import urllib3.request, urllib3.parse, urllib3.error
import http.client
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
    def __init__(self, consumerKey, consumerSecret, applicationName, use_compression=False):
        self.consumerKey = consumerKey
        self.consumerSecret = consumerSecret
        self.applicationName = applicationName
        self.use_compression = use_compression

    def authWebRequest(self, method, url, postData):
        nonce = self.generateNonce()
        timestamp = self.generateTimestamp()
        query = self.generateQuery(method, url, timestamp, nonce)
        authheader = self.generateAuthHeader(query, timestamp, nonce)

        headers = {"Authorization": authheader}
        if method == "POST":
            headers["Content-type"] = "application/x-www-form-urlencoded"
        headers["x-api-version"] = "3.8"
        headers["x-app-name"] = self.applicationName
        if self.use_compression:
            headers["Accept-encoding"] = "gzip"
        
        qparts = urllib.parse.urlparse(query)
        qscheme, qnetloc, qpath, qparams, qquery, qfragment = qparts[:6]

        # httplib.HTTPConnection.debuglevel = 1
        conn = http.client.HTTPSConnection(qnetloc)
        host = '%s?%s' % (qpath, qquery)

        conn.request(method, host, postData, headers)
        response = conn.getresponse()

        encoding = response.getheader('Content-Encoding', None)
        if encoding is not None and encoding == 'gzip':
            data = response.read()
            # print "Compressed data size: ", len(data)
            buf = BytesIO(data)
            data = gzip.GzipFile(fileobj=buf).read()
            # print "Uncompressed data size: ", len(data)
        else:
            data = response.read()

        result = {"status": response.status, "reason": response.reason, "data": data}
        conn.close()
        return result

    def generateQuery(self, method, url, timestamp, nonce):
        parts = urllib.parse.urlparse(url)
        scheme, netloc, path, params, query, fragment = parts[:6]
        np = self.getNormalizedParameters(timestamp, nonce)

        if query:
            query = '%s&oauth_verifier=%s' % (query, np)
        else:
            query = '%s' % np
        
        return urllib.parse.urlunparse((scheme, netloc, path, params, query, fragment))         

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
        
        encoded_str = urllib.parse.urlencode(items)
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
        return urllib.parse.quote(s, safe='~')
