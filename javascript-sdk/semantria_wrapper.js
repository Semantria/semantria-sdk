/**
 * CallbackHandlers "abstract" class
 */
function CallbackHandler(){};
CallbackHandler.prototype.onRequest = function(request) {
	throw "Abstract method onRequest";
};

CallbackHandler.prototype.onResponse = function(request) {
	throw "Abstract method onResponse";
};

CallbackHandler.prototype.onError = function(request) {
	throw "Abstract method onError";
};

CallbackHandler.prototype.onDocsAutoResponse = function(request) {
	throw "Abstract method onDocsAutoResponse";
};

CallbackHandler.prototype.onCollsAutoResponse = function(request) {
	throw "Abstract method onCollsAutoResponse";
};

/**
 * Main Session class
 */
function Session(consumerKey, consumerSecret, serializer, applicationName) {
	this.consumerKey = consumerKey || '';
	this.consumerSecret = consumerSecret || '';
	this.applicationName = applicationName ? applicationName + '.' : '';
	this.applicationName += 'JavaScript';

	this.serializer = serializer || new JsonSerializer();
	this.format = this.serializer.getType();
	this.callbackHandler = null;

	this.host = 'https://api21.semantria.com';
}

Session.prototype.registerSerializer = function(serializer) {
	if (!serializer) {
		throw "Parameter is null";
	}
	this.serializer = serializer;
	this.format = serializer.getType();
};

Session.prototype.setCallbackHandler = function(callbackHandler) {
	if (!callbackHandler) {
		throw "Parameter is null";
	}
	this.callbackHandler = callbackHandler;
};

// Proxies
Session.prototype.createUpdateProxy = function() {
	return {"added": [], "removed": [], "cloned": []};
};

// API Calls
Session.prototype.getStatus = function() {
	var url = this.host + '/status.' + this.format;
	return this.runRequest("GET", url, null, ['supported_languages']);
};

Session.prototype.runRequest = function(method, url, postData, deserializeElements) {
	var request = new this.AuthRequest(this.consumerKey, this.consumerSecret, this.applicationName);
	this.onRequest({"method":method, "url":url, "message":postData});
	response = request.authWebRequest(method, url, postData);
	this.onRequest({"status":response["status"], "reason":response["reason"], "message":response["data"]});

	var status = response["status"];
    var message = response["reason"];

    if (response["data"] != "") {
      message = response["data"];
    }

    if ("DELETE" == method) {
    	if (202 < status) {
    		this.onError({"status":status, "message":message});
    	}
    	return status;
    } else {
    	if (200 == status) {
			return this.serializer.deserialize(response["data"], deserializeElements);
    	} else if (202 == status) {
    		if ("POST" == method) {
    			return status;
    		} else {
    			return null;
    		}
    	} else {
    		this.onError({"status":status, "message":message});
    	}
    }

	return response;
};

Session.prototype.verifySubscription = function() {
	var url = this.host + '/subscription.' + this.format;
	return this.runRequest("GET", url);
};

Session.prototype.getConfigurations = function() {
	var url = this.host + '/configurations.' + this.format;
	return this.runRequest("GET", url, null, ["configurations"]);
};

Session.prototype.updateConfigurations = function(proxy) {
	var url = this.host + '/configurations.' + this.format;
	return this.runRequest("POST", url, this.serializer.serialize(proxy, {"root":"configurations", "added":"configuration", "removed":"configuration"}));
};

Session.prototype.getBlacklist = function(configId) {
	var url = '';
	if (configId) {
		url = this.host + '/blacklist.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/blacklist.' + this.format;
	}
	return this.runRequest("GET", url, null, ["blacklist"]);
};

Session.prototype.updateBlacklist = function(proxy, configId) {
	var url = '';
	if (configId) {
		url = this.host + '/blacklist.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/blacklist.' + this.format;
	}
	return this.runRequest("POST", url, this.serializer.serialize(proxy, {"root":"blacklist", "added":"item", "removed":"item"}));
};

Session.prototype.getCategories = function(configId) {
	var url = '';
	if (configId) {
		url = this.host + '/categories.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/categories.' + this.format;
	}
	return this.runRequest("GET", url, null, ["categories", "samples"]);
};

Session.prototype.updateCategories = function(proxy, configId) {
	var url = '';
	if (configId) {
		url = this.host + '/categories.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/categories.' + this.format;
	}
	return this.runRequest("POST", url, this.serializer.serialize(proxy, {"root":"categories", "added":"category", "removed":"category", "samples":"sample"}));
};

Session.prototype.getQueries = function(configId) {
	var url = '';
	if (configId) {
		url = this.host + '/queries.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/queries.' + this.format;
	}
	return this.runRequest("GET", url, null, ["queries"]);
};

Session.prototype.updateQueries = function(proxy, configId) {
	var url = '';
	if (configId) {
		url = this.host + '/queries.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/queries.' + this.format;
	}
	return this.runRequest("POST", url, this.serializer.serialize(proxy, {"root":"queries", "added":"query", "removed":"query"}));
};

Session.prototype.getSentimentPhrases = function(configId) {
	var url = '';
	if (configId) {
		url = this.host + '/sentiment.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/sentiment.' + this.format;
	}
	return this.runRequest("GET", url, null, ["phrases"]);
};

Session.prototype.updateSentimentPhrases = function(proxy, configId) {
	var url = '';
	if (configId) {
		url = this.host + '/sentiment.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/sentiment.' + this.format;
	}
	return this.runRequest("POST", url, this.serializer.serialize(proxy, {"root":"phrases", "added":"phrase", "removed":"phrase"}));
};

Session.prototype.getEntities = function(configId) {
	var url = '';
	if (configId) {
		url = this.host + '/entities.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/entities.' + this.format;
	}
	return this.runRequest("GET", url, null, ["entities"]);
};

Session.prototype.updateEntities = function(proxy, configId) {
	var url = '';
	if (configId) {
		url = this.host + '/entities.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/entities.' + this.format;
	}
	return this.runRequest("POST", url, this.serializer.serialize(proxy, {"root":"entities", "added":"entity", "removed":"entity"}));
};

Session.prototype.queueDocument = function(doc, configId) {
	var url = '';
	if (configId) {
		url = this.host + '/document.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/document.' + this.format;
	}

	result = this.runRequest("POST", url,
							this.serializer.serialize(doc, {'root':'document'}),
							['documents', 'themes', 'entities', 'topics', 'phrases']);
    if (result && result.constructor == Array) {
      this.onDocsAutoResponse(result);
      return 200;
    } else {
      return result;
    }
};

Session.prototype.queueBatchOfDocuments = function(batch, configId) {
	var url = '';
	if (configId) {
		url = this.host + '/document/batch.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/document/batch.' + this.format;
	}

	result = this.runRequest("POST", url,
							this.serializer.serialize(batch, {'root':'documents', 'element':'document'}),
							['documents', 'themes', 'entities', 'topics', 'phrases']);
    if (result && result.constructor == Array) {
      this.onDocsAutoResponse(result);
      return 200;
    } else {
      return result;
    }
};

Session.prototype.getDocument = function(id, configId) {
	if (!id || '' == id) {
		throw "Specified document's ID is empty";
	}

	var url = '';
	if (configId) {
		url = this.host + '/document/' + id + '.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/document/' + id + '.' + this.format;
	}
	return this.runRequest("GET", url, null, ['documents', 'themes', 'entities', 'topics', 'phrases']);
};

Session.prototype.cancelDocument = function(id, configId) {
	if (!id || '' == id) {
		throw "Specified document's ID is empty";
	}

	var url = '';
	if (configId) {
		url = this.host + '/document/' + id + '.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/document/' + id + '.' + this.format;
	}
	return this.runRequest("DELETE", url);
};

Session.prototype.getProcessedDocuments = function(configId) {
	var url = '';
	if (configId) {
		url = this.host + '/document/processed.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/document/processed.' + this.format;
	}

	return this.runRequest("GET", url, null, ['documents', 'themes', 'entities', 'topics', 'phrases']);
};

Session.prototype.queueCollection = function(coll, configId) {
	var url = '';
	if (configId) {
		url = this.host + '/collection.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/collection.' + this.format;
	}

	result = this.runRequest("POST", url,
							this.serializer.serialize(coll, {'root':'collection', 'collectionElement':'document'}),
							['collections', 'themes', 'entities', 'topics', 'facets', 'attributes']);
    if (result && result.constructor == Array) {
      this.onCollsAutoResponse(result);
      return 200;
    } else {
      return result;
    }
};

Session.prototype.getCollection = function(id, configId) {
	if (!id || '' == id) {
		throw "Specified collection's ID is empty";
	}

	var url = '';
	if (configId) {
		url = this.host + '/collection/' + id + '.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/collection/' + id + '.' + this.format;
	}
	return this.runRequest("GET", url, null, ['collections', 'themes', 'entities', 'topics', 'facets', 'attributes']);
};

Session.prototype.cancelCollection = function(id, configId) {
	if (!id || '' == id) {
		throw "Specified collection's ID is empty";
	}

	var url = '';
	if (configId) {
		url = this.host + '/collection/' + id + '.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/collection/' + id + '.' + this.format;
	}
	return this.runRequest("DELETE", url);
};

Session.prototype.getProcessedCollections = function(configId) {
	var url = '';
	if (configId) {
		url = this.host + '/collection/processed.' + this.format + '?config_id=' + configId;
	} else {
		url = this.host + '/collection/processed.' + this.format;
	}

	return this.runRequest("GET", url, null, ['collections', 'themes', 'entities', 'topics', 'facets', 'attributes']);
};

// Callback reactions
Session.prototype.onRequest = function(request) {
	if (this.callbackHandler) {
		this.callbackHandler.onRequest(request);
	}
};

Session.prototype.onResponse = function(request) {
	if (this.callbackHandler) {
		this.callbackHandler.onResponse(request);
	}
};

Session.prototype.onError = function(request) {
	if (this.callbackHandler) {
		this.callbackHandler.onError(request);
	}
};

Session.prototype.onDocsAutoResponse = function(request) {
	if (this.callbackHandler) {
		this.callbackHandler.onDocsAutoResponse(request);
	}
};

Session.prototype.onCollsAutoResponse = function(request) {
	if (this.callbackHandler) {
		this.callbackHandler.onCollsAutoResponse(request);
	}
};

/**
 * AuthRequest class
 */
Session.prototype.AuthRequest = function(consumerKey, consumerSecret, applicationName) {
	this.oAuthVersion = "1.0";
	this.oAuthParameterPrefix = "oauth_";
	this.oAuthConsumerKeyKey = "oauth_consumer_key";
	this.oAuthVersionKey = "oauth_version";
	this.oAuthSignatureMethodKey = "oauth_signature_method";
	this.oAuthSignatureKey = "oauth_signature";
	this.oAuthTimestampKey = "oauth_timestamp";
	this.oAuthNonceKey = "oauth_nonce";

	this.consumerKey = consumerKey;
	this.consumerSecret = consumerSecret;
	this.applicationName = applicationName;
};

Session.prototype.AuthRequest.prototype.generateNonce = function () {
	return Math.floor(Math.random() * 9999999);
};

Session.prototype.AuthRequest.prototype.generateTimestamp = function() {
	return new Date().getTime();
};

Session.prototype.AuthRequest.prototype.getNormalizedParameters = function(timestamp, nonce) {
	var items = {};
	items[this.oAuthConsumerKeyKey] = this.consumerKey;
	items[this.oAuthNonceKey] = nonce;
	items[this.oAuthSignatureMethodKey] = "HMAC-SHA1";
	items[this.oAuthTimestampKey] = timestamp;
	items[this.oAuthVersionKey] = this.oAuthVersion;

	var parameters = [];
	for (key in items) {
		parameters.push(key + "=" + items[key]);
	}
	return parameters.join('&');
};

Session.prototype.AuthRequest.prototype.generateQuery = function(method, url, timestamp, nonce) {
	var normalizedParameters = this.getNormalizedParameters(timestamp, nonce);

	if (url.indexOf("?") != -1) {
		url += '&';
	} else {
		url += '?';
	}

	return url + normalizedParameters;
};

Session.prototype.AuthRequest.prototype.generateAuthHeader = function(query, timestamp, nonce) {
	var md5cs = this.getMD5Hash(this.consumerSecret);
	var escquery = encodeURIComponent(query);
	var hash = encodeURIComponent(this.getHmacSha1(md5cs, escquery));

	var items = {};
	items["OAuth"] = "";
	items[this.oAuthVersionKey] = this.oAuthVersion;
	items[this.oAuthSignatureMethodKey] = "HMAC-SHA1";
	items[this.oAuthNonceKey] = "\"" + nonce + "\"";
	items[this.oAuthConsumerKeyKey] = "\"" + this.consumerKey + "\"";
	items[this.oAuthTimestampKey] = "\"" + timestamp + "\"";
	items[this.oAuthSignatureKey] = "\"" + hash + "\"";

	var parameters = [];
	for (key in items) {
		if (items[key] != '') {
			parameters.push(key + "=" + items[key]);
		} else {
			parameters.push(key);
		}

	}

	return parameters.join(',');
};

Session.prototype.AuthRequest.prototype.getRequestHeaders = function(method, nonce, timestamp, query) {
	var headers = [];
	headers["Authorization"] = this.generateAuthHeader(query, timestamp, nonce);

	if (method == "POST") {
		headers["Content-type"] = "application/x-www-form-urlencoded";
	}

	headers["x-api-version"] = "2";
	headers["x-app-name"] = this.applicationName;

	return headers;
};

Session.prototype.AuthRequest.prototype.runRequest = function(method, url, headers, postData) {
	var xmlHttp = null;

	xmlHttp = new XMLHttpRequest();
	xmlHttp.open(method, url, false);

	for (key in headers) {
		xmlHttp.setRequestHeader(key, headers[key]);
	}

	xmlHttp.send(postData);

//	console.log("URL=>" + url);
//	console.log("STATUS=>" + xmlHttp.status);
//	console.log("RESPONSE=>" + xmlHttp.responseText);

	return {"status":xmlHttp.status, "reason":xmlHttp.statusText, "data":xmlHttp.responseText};
};

Session.prototype.AuthRequest.prototype.authWebRequest = function(method, url, postData) {
	var nonce = this.generateNonce();
	var timestamp = this.generateTimestamp();
	var query = this.generateQuery(method, url, timestamp, nonce);
	var headers = this.getRequestHeaders(method, nonce, timestamp, query);

	return this.runRequest(method, query, headers, postData);
};

Session.prototype.AuthRequest.prototype.getMD5Hash = function(source) {
	/*
	 * A JavaScript implementation of the RSA Data Security, Inc. MD5 Message
	 * Digest Algorithm, as defined in RFC 1321.
	 * Copyright (C) Paul Johnston 1999 - 2000.
	 * Updated by Greg Holt 2000 - 2001.
	 * See http://pajhome.org.uk/site/legal.html for details.
	 */

	/*
	 * Convert a 32-bit number to a hex string with ls-byte first
	 */
	var hex_chr = "0123456789abcdef";
	function rhex(num)
	{
	  str = "";
	  for(j = 0; j <= 3; j++)
		str += hex_chr.charAt((num >> (j * 8 + 4)) & 0x0F) +
			   hex_chr.charAt((num >> (j * 8)) & 0x0F);
	  return str;
	}

	/*
	 * Convert a string to a sequence of 16-word blocks, stored as an array.
	 * Append padding bits and the length, as described in the MD5 standard.
	 */
	function str2blks_MD5(str)
	{
	  nblk = ((str.length + 8) >> 6) + 1;
	  blks = new Array(nblk * 16);
	  for(i = 0; i < nblk * 16; i++) blks[i] = 0;
	  for(i = 0; i < str.length; i++)
		blks[i >> 2] |= str.charCodeAt(i) << ((i % 4) * 8);
	  blks[i >> 2] |= 0x80 << ((i % 4) * 8);
	  blks[nblk * 16 - 2] = str.length * 8;
	  return blks;
	}

	/*
	 * Add integers, wrapping at 2^32. This uses 16-bit operations internally
	 * to work around bugs in some JS interpreters.
	 */
	function add(x, y)
	{
	  var lsw = (x & 0xFFFF) + (y & 0xFFFF);
	  var msw = (x >> 16) + (y >> 16) + (lsw >> 16);
	  return (msw << 16) | (lsw & 0xFFFF);
	}

	/*
	 * Bitwise rotate a 32-bit number to the left
	 */
	function rol(num, cnt)
	{
	  return (num << cnt) | (num >>> (32 - cnt));
	}

	/*
	 * These functions implement the basic operation for each round of the
	 * algorithm.
	 */
	function cmn(q, a, b, x, s, t)
	{
	  return add(rol(add(add(a, q), add(x, t)), s), b);
	}
	function ff(a, b, c, d, x, s, t)
	{
	  return cmn((b & c) | ((~b) & d), a, b, x, s, t);
	}
	function gg(a, b, c, d, x, s, t)
	{
	  return cmn((b & d) | (c & (~d)), a, b, x, s, t);
	}
	function hh(a, b, c, d, x, s, t)
	{
	  return cmn(b ^ c ^ d, a, b, x, s, t);
	}
	function ii(a, b, c, d, x, s, t)
	{
	  return cmn(c ^ (b | (~d)), a, b, x, s, t);
	}

	/*
	 * Take a string and return the hex representation of its MD5.
	 */
	function calcMD5(str)
	{
	  x = str2blks_MD5(str);
	  a =  1732584193;
	  b = -271733879;
	  c = -1732584194;
	  d =  271733878;

	  for(i = 0; i < x.length; i += 16)
	  {
		olda = a;
		oldb = b;
		oldc = c;
		oldd = d;

		a = ff(a, b, c, d, x[i+ 0], 7 , -680876936);
		d = ff(d, a, b, c, x[i+ 1], 12, -389564586);
		c = ff(c, d, a, b, x[i+ 2], 17,  606105819);
		b = ff(b, c, d, a, x[i+ 3], 22, -1044525330);
		a = ff(a, b, c, d, x[i+ 4], 7 , -176418897);
		d = ff(d, a, b, c, x[i+ 5], 12,  1200080426);
		c = ff(c, d, a, b, x[i+ 6], 17, -1473231341);
		b = ff(b, c, d, a, x[i+ 7], 22, -45705983);
		a = ff(a, b, c, d, x[i+ 8], 7 ,  1770035416);
		d = ff(d, a, b, c, x[i+ 9], 12, -1958414417);
		c = ff(c, d, a, b, x[i+10], 17, -42063);
		b = ff(b, c, d, a, x[i+11], 22, -1990404162);
		a = ff(a, b, c, d, x[i+12], 7 ,  1804603682);
		d = ff(d, a, b, c, x[i+13], 12, -40341101);
		c = ff(c, d, a, b, x[i+14], 17, -1502002290);
		b = ff(b, c, d, a, x[i+15], 22,  1236535329);

		a = gg(a, b, c, d, x[i+ 1], 5 , -165796510);
		d = gg(d, a, b, c, x[i+ 6], 9 , -1069501632);
		c = gg(c, d, a, b, x[i+11], 14,  643717713);
		b = gg(b, c, d, a, x[i+ 0], 20, -373897302);
		a = gg(a, b, c, d, x[i+ 5], 5 , -701558691);
		d = gg(d, a, b, c, x[i+10], 9 ,  38016083);
		c = gg(c, d, a, b, x[i+15], 14, -660478335);
		b = gg(b, c, d, a, x[i+ 4], 20, -405537848);
		a = gg(a, b, c, d, x[i+ 9], 5 ,  568446438);
		d = gg(d, a, b, c, x[i+14], 9 , -1019803690);
		c = gg(c, d, a, b, x[i+ 3], 14, -187363961);
		b = gg(b, c, d, a, x[i+ 8], 20,  1163531501);
		a = gg(a, b, c, d, x[i+13], 5 , -1444681467);
		d = gg(d, a, b, c, x[i+ 2], 9 , -51403784);
		c = gg(c, d, a, b, x[i+ 7], 14,  1735328473);
		b = gg(b, c, d, a, x[i+12], 20, -1926607734);

		a = hh(a, b, c, d, x[i+ 5], 4 , -378558);
		d = hh(d, a, b, c, x[i+ 8], 11, -2022574463);
		c = hh(c, d, a, b, x[i+11], 16,  1839030562);
		b = hh(b, c, d, a, x[i+14], 23, -35309556);
		a = hh(a, b, c, d, x[i+ 1], 4 , -1530992060);
		d = hh(d, a, b, c, x[i+ 4], 11,  1272893353);
		c = hh(c, d, a, b, x[i+ 7], 16, -155497632);
		b = hh(b, c, d, a, x[i+10], 23, -1094730640);
		a = hh(a, b, c, d, x[i+13], 4 ,  681279174);
		d = hh(d, a, b, c, x[i+ 0], 11, -358537222);
		c = hh(c, d, a, b, x[i+ 3], 16, -722521979);
		b = hh(b, c, d, a, x[i+ 6], 23,  76029189);
		a = hh(a, b, c, d, x[i+ 9], 4 , -640364487);
		d = hh(d, a, b, c, x[i+12], 11, -421815835);
		c = hh(c, d, a, b, x[i+15], 16,  530742520);
		b = hh(b, c, d, a, x[i+ 2], 23, -995338651);

		a = ii(a, b, c, d, x[i+ 0], 6 , -198630844);
		d = ii(d, a, b, c, x[i+ 7], 10,  1126891415);
		c = ii(c, d, a, b, x[i+14], 15, -1416354905);
		b = ii(b, c, d, a, x[i+ 5], 21, -57434055);
		a = ii(a, b, c, d, x[i+12], 6 ,  1700485571);
		d = ii(d, a, b, c, x[i+ 3], 10, -1894986606);
		c = ii(c, d, a, b, x[i+10], 15, -1051523);
		b = ii(b, c, d, a, x[i+ 1], 21, -2054922799);
		a = ii(a, b, c, d, x[i+ 8], 6 ,  1873313359);
		d = ii(d, a, b, c, x[i+15], 10, -30611744);
		c = ii(c, d, a, b, x[i+ 6], 15, -1560198380);
		b = ii(b, c, d, a, x[i+13], 21,  1309151649);
		a = ii(a, b, c, d, x[i+ 4], 6 , -145523070);
		d = ii(d, a, b, c, x[i+11], 10, -1120210379);
		c = ii(c, d, a, b, x[i+ 2], 15,  718787259);
		b = ii(b, c, d, a, x[i+ 9], 21, -343485551);

		a = add(a, olda);
		b = add(b, oldb);
		c = add(c, oldc);
		d = add(d, oldd);
	  }
	  return rhex(a) + rhex(b) + rhex(c) + rhex(d);
	}

	return calcMD5(source);
};

Session.prototype.AuthRequest.prototype.base64 = function(wordArray) {
	// Shortcuts
    var words = wordArray.words;
    var sigBytes = wordArray.sigBytes;
    var map = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';

    // Clamp excess bits
    wordArray.clamp();

    // Convert
    var base64Chars = [];
    for (var i = 0; i < sigBytes; i += 3) {
        var byte1 = (words[i >>> 2]       >>> (24 - (i % 4) * 8))       & 0xff;
        var byte2 = (words[(i + 1) >>> 2] >>> (24 - ((i + 1) % 4) * 8)) & 0xff;
        var byte3 = (words[(i + 2) >>> 2] >>> (24 - ((i + 2) % 4) * 8)) & 0xff;

        var triplet = (byte1 << 16) | (byte2 << 8) | byte3;

        for (var j = 0; (j < 4) && (i + j * 0.75 < sigBytes); j++) {
            base64Chars.push(map.charAt((triplet >>> (6 * (3 - j))) & 0x3f));
        }
    }

    // Add padding
    var paddingChar = map.charAt(64);
    if (paddingChar) {
        while (base64Chars.length % 4) {
            base64Chars.push(paddingChar);
        }
    }

    return base64Chars.join('');
};

Session.prototype.AuthRequest.prototype.getHmacSha1 = function(key, message) {

	/*
		CryptoJS v3.0.2
		code.google.com/p/crypto-js
		(c) 2009-2012 by Jeff Mott. All rights reserved.
		code.google.com/p/crypto-js/wiki/License
	*/
	var CryptoJS=CryptoJS||function(i,j){var f={},b=f.lib={},m=b.Base=function(){function a(){}return{extend:function(e){a.prototype=this;var c=new a;e&&c.mixIn(e);c.$super=this;return c},create:function(){var a=this.extend();a.init.apply(a,arguments);return a},init:function(){},mixIn:function(a){for(var c in a)a.hasOwnProperty(c)&&(this[c]=a[c]);a.hasOwnProperty("toString")&&(this.toString=a.toString)},clone:function(){return this.$super.extend(this)}}}(),l=b.WordArray=m.extend({init:function(a,e){a=
	this.words=a||[];this.sigBytes=e!=j?e:4*a.length},toString:function(a){return(a||d).stringify(this)},concat:function(a){var e=this.words,c=a.words,o=this.sigBytes,a=a.sigBytes;this.clamp();if(o%4)for(var b=0;b<a;b++)e[o+b>>>2]|=(c[b>>>2]>>>24-8*(b%4)&255)<<24-8*((o+b)%4);else if(65535<c.length)for(b=0;b<a;b+=4)e[o+b>>>2]=c[b>>>2];else e.push.apply(e,c);this.sigBytes+=a;return this},clamp:function(){var a=this.words,e=this.sigBytes;a[e>>>2]&=4294967295<<32-8*(e%4);a.length=i.ceil(e/4)},clone:function(){var a=
	m.clone.call(this);a.words=this.words.slice(0);return a},random:function(a){for(var e=[],c=0;c<a;c+=4)e.push(4294967296*i.random()|0);return l.create(e,a)}}),n=f.enc={},d=n.Hex={stringify:function(a){for(var e=a.words,a=a.sigBytes,c=[],b=0;b<a;b++){var d=e[b>>>2]>>>24-8*(b%4)&255;c.push((d>>>4).toString(16));c.push((d&15).toString(16))}return c.join("")},parse:function(a){for(var e=a.length,c=[],b=0;b<e;b+=2)c[b>>>3]|=parseInt(a.substr(b,2),16)<<24-4*(b%8);return l.create(c,e/2)}},h=n.Latin1={stringify:function(a){for(var e=
	a.words,a=a.sigBytes,b=[],d=0;d<a;d++)b.push(String.fromCharCode(e[d>>>2]>>>24-8*(d%4)&255));return b.join("")},parse:function(a){for(var b=a.length,c=[],d=0;d<b;d++)c[d>>>2]|=(a.charCodeAt(d)&255)<<24-8*(d%4);return l.create(c,b)}},k=n.Utf8={stringify:function(a){try{return decodeURIComponent(escape(h.stringify(a)))}catch(b){throw Error("Malformed UTF-8 data");}},parse:function(a){return h.parse(unescape(encodeURIComponent(a)))}},g=b.BufferedBlockAlgorithm=m.extend({reset:function(){this._data=l.create();
	this._nDataBytes=0},_append:function(a){"string"==typeof a&&(a=k.parse(a));this._data.concat(a);this._nDataBytes+=a.sigBytes},_process:function(a){var b=this._data,c=b.words,d=b.sigBytes,f=this.blockSize,g=d/(4*f),g=a?i.ceil(g):i.max((g|0)-this._minBufferSize,0),a=g*f,d=i.min(4*a,d);if(a){for(var h=0;h<a;h+=f)this._doProcessBlock(c,h);h=c.splice(0,a);b.sigBytes-=d}return l.create(h,d)},clone:function(){var a=m.clone.call(this);a._data=this._data.clone();return a},_minBufferSize:0});b.Hasher=g.extend({init:function(){this.reset()},
	reset:function(){g.reset.call(this);this._doReset()},update:function(a){this._append(a);this._process();return this},finalize:function(a){a&&this._append(a);this._doFinalize();return this._hash},clone:function(){var a=g.clone.call(this);a._hash=this._hash.clone();return a},blockSize:16,_createHelper:function(a){return function(b,c){return a.create(c).finalize(b)}},_createHmacHelper:function(a){return function(b,c){return p.HMAC.create(a,c).finalize(b)}}});var p=f.algo={};return f}(Math);
	(function(){var i=CryptoJS,j=i.lib,f=j.WordArray,j=j.Hasher,b=[],m=i.algo.SHA1=j.extend({_doReset:function(){this._hash=f.create([1732584193,4023233417,2562383102,271733878,3285377520])},_doProcessBlock:function(f,i){for(var d=this._hash.words,h=d[0],k=d[1],g=d[2],j=d[3],a=d[4],e=0;80>e;e++){if(16>e)b[e]=f[i+e]|0;else{var c=b[e-3]^b[e-8]^b[e-14]^b[e-16];b[e]=c<<1|c>>>31}c=(h<<5|h>>>27)+a+b[e];c=20>e?c+((k&g|~k&j)+1518500249):40>e?c+((k^g^j)+1859775393):60>e?c+((k&g|k&j|g&j)-1894007588):c+((k^g^j)-
	899497514);a=j;j=g;g=k<<30|k>>>2;k=h;h=c}d[0]=d[0]+h|0;d[1]=d[1]+k|0;d[2]=d[2]+g|0;d[3]=d[3]+j|0;d[4]=d[4]+a|0},_doFinalize:function(){var b=this._data,f=b.words,d=8*this._nDataBytes,h=8*b.sigBytes;f[h>>>5]|=128<<24-h%32;f[(h+64>>>9<<4)+15]=d;b.sigBytes=4*f.length;this._process()}});i.SHA1=j._createHelper(m);i.HmacSHA1=j._createHmacHelper(m)})();
	(function(){var i=CryptoJS,j=i.enc.Utf8;i.algo.HMAC=i.lib.Base.extend({init:function(f,b){f=this._hasher=f.create();"string"==typeof b&&(b=j.parse(b));var i=f.blockSize,l=4*i;b.sigBytes>l&&(b=f.finalize(b));for(var n=this._oKey=b.clone(),d=this._iKey=b.clone(),h=n.words,k=d.words,g=0;g<i;g++)h[g]^=1549556828,k[g]^=909522486;n.sigBytes=d.sigBytes=l;this.reset()},reset:function(){var f=this._hasher;f.reset();f.update(this._iKey)},update:function(f){this._hasher.update(f);return this},finalize:function(f){var b=
	this._hasher,f=b.finalize(f);b.reset();return b.finalize(this._oKey.clone().concat(f))}})})();

	return this.base64(CryptoJS.HmacSHA1(message, key));
};

/**
 * Utils
 */
function encode_utf8(s) {
  return UnescapeUTF8(encodeURIComponent(s));
}

function escape_utf8(data) {
    if (data == '' || data == null){
           return '';
    }
   data = data.toString();
   var buffer = '';
   for(var i=0; i<data.length; i++){
           var c = data.charCodeAt(i);
           var bs = new Array();
          if (c > 0x10000){
                   // 4 bytes
                   bs[0] = 0xF0 | ((c & 0x1C0000) >>> 18);
                   bs[1] = 0x80 | ((c & 0x3F000) >>> 12);
                   bs[2] = 0x80 | ((c & 0xFC0) >>> 6);
               bs[3] = 0x80 | (c & 0x3F);
           }else if (c > 0x800){
                    // 3 bytes
                    bs[0] = 0xE0 | ((c & 0xF000) >>> 12);
                    bs[1] = 0x80 | ((c & 0xFC0) >>> 6);
                   bs[2] = 0x80 | (c & 0x3F);
         }else if (c > 0x80){
                  // 2 bytes
                   bs[0] = 0xC0 | ((c & 0x7C0) >>> 6);
                  bs[1] = 0x80 | (c & 0x3F);
           }else{
                   // 1 byte
                bs[0] = c;
          }
         for(var j=0; j<bs.length; j++){
                  var b = bs[j];
                   var hex = nibble_to_hex((b & 0xF0) >>> 4)
                  + nibble_to_hex(b &0x0F);buffer += '%'+hex;
          }
	}
	return buffer;
}

function nibble_to_hex(nibble){
    var chars = '0123456789ABCDEF';
    return chars.charAt(nibble);
}

function EscapeUTF8(str){
	return str.replace(/[^*+.-9A-Z_a-z-]/g,function(s){
		var c=s.charCodeAt(0);
		return (c<16?"%0"+c.toString(16):c<128?"%"+c.toString(16):c<2048?"%"+(c>>6|192).toString(16)+"%"+(c&63|128).toString(16):"%"+(c>>12|224).toString(16)+"%"+(c>>6&63|128).toString(16)+"%"+(c&63|128).toString(16)).toUpperCase()
	});
};

function UnescapeUTF8(str){
	return str.replace(/%(E(0%[AB]|[1-CEF]%[89AB]|D%[89])[0-9A-F]|C[2-9A-F]|D[0-9A-F])%[89AB][0-9A-F]|%[0-7][0-9A-F]/ig,function(s){
		var c=parseInt(s.substring(1),16);
		return String.fromCharCode(c<128?c:c<224?(c&31)<<6|parseInt(s.substring(4),16)&63:((c&15)<<6|parseInt(s.substring(4),16)&63)<<6|parseInt(s.substring(7),16)&63)
	});
};

function decode_utf8(s) {
  return decodeURIComponent(EscapeUTF8(s));
}

/**
 * Serializers
 */
function JsonSerializer(){};
JsonSerializer.prototype.getType = function() {
	return 'json';
};

JsonSerializer.prototype.serialize = function(obj) {
	if (obj["cloned"]) {
		for (var i = 0; i < obj["cloned"].length; ++i) {
			var item = obj["cloned"][i];
			item["template"] = item["config_id"];
			delete item["config_id"];
			obj["added"].push(item);
		}
		delete obj["cloned"];
	}

	return encode_utf8(JSON.stringify(obj));
};

JsonSerializer.prototype.deserialize = function(jsonSource) {
	return JSON.parse(decode_utf8(jsonSource));
};

function XmlSerializer(){};

XmlSerializer.prototype.getType = function() {
	return 'xml';
};

XmlSerializer.prototype.json2xml = function(o, tab, elements) {
   var toXml = function(v, name, ind, skipRoot) {
      var xml = "";

      if (v instanceof Array) {

    	  if (elements) {
    		 if (elements["added"] != undefined && name == "added") {
	    		 skipRoot = true;
		        xml += "<added>";
		     } else if (elements["removed"] != undefined && name == "removed") {
		    	 skipRoot = true;
	            xml += "<removed>";
	         } else if (elements["samples"] != undefined && name == "samples") {
		    	 skipRoot = true;
	             xml += "<samples>";
	         } else if (elements["collectionElement"] != undefined && name == "documents") {
		    	 skipRoot = true;
	             xml += "<documents>";
	        	 name = elements["collectionElement"];
	         }
    	  }

         for (var i=0, n=v.length; i<n; i++) {
        	 xml += ind + toXml(v[i], name, ind+"\t", skipRoot) + "\n";
         }

         if (elements != undefined) {
        	 if (elements["added"] != undefined && name == "added") {
     	    	xml += "</added>";
     	     } else if (elements["removed"] != undefined && name == "removed") {
                 xml += "</removed>";
             } else if (elements["samples"] != undefined && name == "samples") {
                 xml += "</samples>";
             } else if (elements["collectionElement"] != undefined && name == "document") {
	             xml += "</documents>";
	         }
         }

      }
      else if (typeof(v) == "object") {
         var hasChild = false;
         if (!skipRoot) {
        	 xml += ind + "<" + name;
             for (var m in v) {
                if (m.charAt(0) == "@")
                   xml += " " + m.substr(1) + "=\"" + v[m].toString() + "\"";
                else
                   hasChild = true;
             }
             xml += hasChild ? ">" : "/>";
         } else {
        	 hasChild = true;
         }

         if (hasChild) {
        	 if (elements) {
        		 if (elements["added"] != undefined && name == "added") {
      	    	   xml += "<" + elements["added"] + ">";
      	         } else if (elements["removed"] != undefined && name == "removed") {
                     xml += "<" + elements["removed"] + ">";
                 } else if (elements["samples"] != undefined && name == "samples") {
                     xml += "<" + elements["samples"] + ">";
                 }
        	 }

            for (var m in v) {
               if (m == "#text")
                  xml += v[m];
               else if (m == "#cdata")
                  xml += "<![CDATA[" + v[m] + "]]>";
               else if (m.charAt(0) != "@")
                  xml += toXml(v[m], m, ind+"\t");
            }

            if (elements) {
            	if (elements["added"] != undefined && name == "added") {
     	    	   xml += "</" + elements["added"] + ">";
     	        } else if (elements["removed"] != undefined && name == "removed") {
                   xml += "</" + elements["removed"] + ">";
                } else if (elements["samples"] != undefined && name == "samples") {
                    xml += "</" + elements["samples"] + ">";
                }
            }

            if (!skipRoot) {
            	xml += (xml.charAt(xml.length-1)=="\n"?ind:"") + "</" + name + ">";
            }
         }
      }
      else {
    	  if (elements) {
    		  if (elements["added"] != undefined && name == "added") {
        		  xml += ind + "<" + elements["added"] + ">" + v.toString() +  "</" + elements["added"] + ">";
    	      } else if (elements["removed"] != undefined && name == "removed") {
                 xml += ind + "<" + elements["removed"] + ">" + v.toString() +  "</" + elements["removed"] + ">";
              } else if (elements["samples"] != undefined && name == "samples") {
                 xml += ind + "<" + elements["samples"] + ">" + v.toString() +  "</" + elements["samples"] + ">";
              } else {
            	  xml += ind + "<" + name + ">" + v.toString() +  "</" + name + ">";
              }
    	  }
      }

      return xml;
   }, xml="";

   for (var m in o) {
	   if (elements && elements["element"] != undefined) {
		   xml += toXml(o[m], elements["element"], "");
	   } else {
		   xml += toXml(o[m], m, "");
	   }
   }

   if (elements && elements["root"] != undefined) {
	   xml = toXml(xml, elements["root"], "");
   }

   return tab ? xml.replace(/\t/g, tab) : xml.replace(/\t|\n/g, "");
};

XmlSerializer.prototype.xml2json = function(xml, tab, rootArrayElementNames) {
   var X = {
      toObj: function(xml) {
         var o = {};
         if (xml.nodeType==1) {   // element node ..
            if (xml.attributes.length)   // element with attributes  ..
               for (var i=0; i<xml.attributes.length; i++)
                  o["@"+xml.attributes[i].nodeName] = (xml.attributes[i].nodeValue||"").toString();
            if (xml.firstChild) { // element has child nodes ..
               var textChild=0, cdataChild=0, hasElementChild=false;
               for (var n=xml.firstChild; n; n=n.nextSibling) {
                  if (n.nodeType==1) hasElementChild = true;
                  else if (n.nodeType==3 && n.nodeValue.match(/[^ \f\n\r\t\v]/)) textChild++; // non-whitespace text
                  else if (n.nodeType==4) cdataChild++; // cdata section node
               }
               if (hasElementChild) {
                  if (textChild < 2 && cdataChild < 2) { // structured element with evtl. a single text or/and cdata node ..
                     X.removeWhite(xml);
                     for (var n=xml.firstChild; n; n=n.nextSibling) {
                        if (n.nodeType == 3)  // text node
                           o["#text"] = X.escape(n.nodeValue);
                        else if (n.nodeType == 4)  // cdata node
                           o["#cdata"] = X.escape(n.nodeValue);
                        else if (o[n.nodeName]) {  // multiple occurence of element ..
                           if (o[n.nodeName] instanceof Array)
                              o[n.nodeName][o[n.nodeName].length] = X.toObj(n);
                           else
                              o[n.nodeName] = [o[n.nodeName], X.toObj(n)];
                        }
                        else  // first occurence of element..
                           o[n.nodeName] = X.toObj(n);
                     }
                  }
                  else { // mixed content
                     if (!xml.attributes.length)
                        o = X.escape(X.innerXml(xml));
                     else
                        o["#text"] = X.escape(X.innerXml(xml));
                  }
               }
               else if (textChild) { // pure text
                  if (!xml.attributes.length)
                     o = X.escape(X.innerXml(xml));
                  else
                     o["#text"] = X.escape(X.innerXml(xml));
               }
               else if (cdataChild) { // cdata
                  if (cdataChild > 1)
                     o = X.escape(X.innerXml(xml));
                  else
                     for (var n=xml.firstChild; n; n=n.nextSibling)
                        o["#cdata"] = X.escape(n.nodeValue);
               }
            }
            if (!xml.attributes.length && !xml.firstChild) o = null;
         }
         else if (xml.nodeType==9) { // document.node
            o = X.toObj(xml.documentElement);
         }
         else
            alert("unhandled node type: " + xml.nodeType);
         return o;
      },
      toJson: function(o, name, ind, rootArrayElementNames) {
         var json = name ? ("\""+name+"\"") : "";

         if (rootArrayElementNames != undefined
        		 && !(o instanceof Array)
        		 && o != null) {

        	 var rootArrayElementFound = false;
        	 for (var i in rootArrayElementNames) {
        		 if (rootArrayElementNames[i] == name) {
        			 rootArrayElementFound = true;
        			 break;
        		 }
        	 }

        	 if (rootArrayElementFound) {
        		 for (var rootElement in o) break;
                 if (o[rootElement] instanceof Array) {
                	 o = o[rootElement];
                 }

            	 var ar = new Array();
                 for (var rootElement in o) {
                	 ar.push(o[rootElement]);
                 }
                 o = ar;
        	 }
         }

         if (o instanceof Array) {
            for (var i=0,n=o.length; i<n; i++)
               o[i] = X.toJson(o[i], "", ind+"\t", rootArrayElementNames);
            json += (name?":[":"[") + (o.length > 1 ? ("\n"+ind+"\t"+o.join(",\n"+ind+"\t")+"\n"+ind) : o.join("")) + "]";
         }
         else if (o == null)
            json += (name&&":") + "null";
         else if (typeof(o) == "object") {
            var arr = [];
            for (var m in o)
               arr[arr.length] = X.toJson(o[m], m, ind+"\t", rootArrayElementNames);
            json += (name?":{":"{") + (arr.length > 1 ? ("\n"+ind+"\t"+arr.join(",\n"+ind+"\t")+"\n"+ind) : arr.join("")) + "}";
         }
         else if (typeof(o) == "string")
            json += (name&&":") + "\"" + o.toString() + "\"";
         else
            json += (name&&":") + o.toString();
         return json;
      },
      innerXml: function(node) {
         var s = "";
         if ("innerHTML" in node)
            s = node.innerHTML;
         else {
            var asXml = function(n) {
               var s = "";
               if (n.nodeType == 1) {
                  s += "<" + n.nodeName;
                  for (var i=0; i<n.attributes.length;i++)
                     s += " " + n.attributes[i].nodeName + "=\"" + (n.attributes[i].nodeValue||"").toString() + "\"";
                  if (n.firstChild) {
                     s += ">";
                     for (var c=n.firstChild; c; c=c.nextSibling)
                        s += asXml(c);
                     s += "</"+n.nodeName+">";
                  }
                  else
                     s += "/>";
               }
               else if (n.nodeType == 3)
                  s += n.nodeValue;
               else if (n.nodeType == 4)
                  s += "<![CDATA[" + n.nodeValue + "]]>";
               return s;
            };
            for (var c=node.firstChild; c; c=c.nextSibling)
               s += asXml(c);
         }
         return s;
      },
      escape: function(txt) {
         return txt.replace(/[\\]/g, "\\\\")
                   .replace(/[\"]/g, '\\"')
                   .replace(/[\n]/g, '\\n')
                   .replace(/[\r]/g, '\\r');
      },
      removeWhite: function(e) {
         e.normalize();
         for (var n = e.firstChild; n; ) {
            if (n.nodeType == 3) {  // text node
               if (!n.nodeValue.match(/[^ \f\n\r\t\v]/)) { // pure whitespace text node
                  var nxt = n.nextSibling;
                  e.removeChild(n);
                  n = nxt;
               }
               else
                  n = n.nextSibling;
            }
            else if (n.nodeType == 1) {  // element node
               X.removeWhite(n);
               n = n.nextSibling;
            }
            else                      // any other node
               n = n.nextSibling;
         }
         return e;
      }
   };
   if (xml.nodeType == 9) // document node
      xml = xml.documentElement;
   var json = X.toJson(X.toObj(X.removeWhite(xml)), xml.nodeName, "\t", rootArrayElementNames);
   return "{\n" + (tab ? json.replace(/\t/g, tab) : json.replace(/\t|\n/g, "")) + "\n}";
};

XmlSerializer.prototype.parseXml = function(xmlSource) {
   var dom = null;
   if (window.DOMParser) {
      try {
         dom = (new DOMParser()).parseFromString(xmlSource, "text/xml");
      }
      catch (e) {
    	  dom = null;
	  }
   }
   else if (window.ActiveXObject) {
      try {
         dom = new ActiveXObject('Microsoft.XMLDOM');
         dom.async = false;
         if (!dom.loadXML(xmlSource)) // parse error ..
            window.alert(dom.parseError.reason + dom.parseError.srcText);
      }
      catch (e) { dom = null; }
   }
   else
      alert("oops");
   return dom;
};

XmlSerializer.prototype.serialize = function(obj, elements) {
	if (obj["cloned"]) {
		for (var i = 0; i < obj["cloned"].length; ++i) {
			var item = obj["cloned"][i];
			item["template"] = item["config_id"];
			delete item["config_id"];
			obj["added"].push(item);
		}
		delete obj["cloned"];
	}

	return encode_utf8(this.json2xml(obj, '', elements));
};

XmlSerializer.prototype.deserialize = function(xmlSource, rootArrayElementNames) {
	var jsonSource = this.xml2json(this.parseXml(decode_utf8(xmlSource)), '', rootArrayElementNames);
	var obj = JSON.parse(jsonSource);
	for (var rootElement in obj) break;
	return obj[rootElement];
};