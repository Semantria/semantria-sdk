var fs = require('fs');
var crypto = require('crypto');
var promise = require('promise');
var request = require('request');
//var syncRequest = require('sync-request');
/*
 reason: sync-request not allow make DELETE request with body
 fail with 'socket hang up' Error
 */
var console_warn = console.warn;
console.warn = function(){}
var syncRequest = require('request-sync');
console.warn = console_warn;

var session_key_url = 'https://semantria.com/auth/session'
/**
 * @param {Session} string
 * @param {Object} config
 */
function runApiRequest(session, options) {
	var api_request = {
		oAuth: {
			version: "1.0",
			parameterPrefix: "oauth_",
			consumerKeyKey: "oauth_consumer_key",
			versionKey: "oauth_version",
			signatureMethodKey: "oauth_signature_method",
			signatureKey: "oauth_signature",
			timestampKey: "oauth_timestamp",
			nonceKey: "oauth_nonce"
		},
		SDK_VERSION: session.SDK_VERSION,
		X_API_VERSION: session.X_API_VERSION,
		API_HOST: session.API_HOST,
		consumerKey: session.consumerKey,
		consumerSecret: session.consumerSecret,
		applicationName: session.applicationName,
		onRequest: session.onRequest,
		onResponse: session.onResponse,
		onError: session.onError,
		onAfterResponse: session.onAfterResponse,
		format: session.format,
		nonce: generateNonce(),
		timestamp: generateTimestamp(),
		session: session
	}
	api_request.method     = options.method     || 'GET';
	api_request.path       = options.path       || '';
	api_request.getParams  = options.getParams  || {};
	api_request.postParams = options.postParams || null;
	api_request.url        = generateUrl(api_request);
	api_request.queryUrl   = generateQueryUrl(api_request);
	api_request.headers    = getRequestHeaders(api_request);
	api_request.callAfterResponseHook = options.callAfterResponseHook;

	if(api_request.postParams) {
		api_request.postParams = Utils.encodeUtf8(JSON.stringify(api_request.postParams))
	}

	api_request.onRequest.call(api_request.session, {
		method: api_request.method,
		url: api_request.url,
		message: api_request.postParams
	});

	var request_fn;
	if (typeof options.callback == "function") {
		request_fn = runAsyncRequest;
	} else {
		request_fn = options.callback ? runPromiseRequest : runSyncRequest;
	}
	return request_fn(api_request, options.callback);
}

/**
 * @returns {Number}
 */
function generateNonce() {
	return Math.floor(Math.random() * 9999999);
}

/**
 * @returns {Number}
 */
function generateTimestamp() {
	return (new Date()).getTime();
}

/**
 * Function returns string in ?q1=v1&q2=v2 format
 * from passed config key-value pairs
 *
 * @param {Object} get_params
 * @returns {undefined}
 */
function createQueryString(get_params) {
	if(!get_params) {
		return "";
	}

	var result = [];

	for(var key in get_params) {
		if(typeof get_params[key] === "undefined" || get_params[key] === null) {
			continue;
		}

		result.push(key + "=" + get_params[key]);
	}

	if(result.length > 0) {
		return "?" + result.join("&");
	}

	return "";
}

/**
 * @param {Object} api_request
 * @returns {String}
 */
function generateQueryUrl(api_request) {
	api_request.getParams[api_request.oAuth.consumerKeyKey]     = api_request.consumerKey;
	api_request.getParams[api_request.oAuth.nonceKey]           = api_request.nonce;
	api_request.getParams[api_request.oAuth.signatureMethodKey] = "HMAC-SHA1";
	api_request.getParams[api_request.oAuth.timestampKey]       = api_request.timestamp;
	api_request.getParams[api_request.oAuth.versionKey]         = api_request.oAuth.version;

	var queryStr = createQueryString(api_request.getParams);
	var url = api_request.API_HOST + '/' + api_request.path + '.' + api_request.format;
	url += queryStr;

	return url;
}

/**
 * @param {Object} api_request
 * @returns {String}
 */
function generateUrl(api_request) {
	var queryStr = createQueryString(api_request.getParams);
	var url = api_request.API_HOST + '/' + api_request.path + '.' + api_request.format;
	url += queryStr;

	return url;
}

/**
 * @param {Object} api_request
 * @returns {Array}
 */
function getRequestHeaders(api_request) {
	var headers = {};

	headers["Authorization"] = generateAuthHeader(api_request);

	if (api_request.method == "POST") {
		headers["Content-Type"] = "application/x-www-form-urlencoded";
	}

	headers["x-app-name"] = api_request.applicationName;
	headers["x-api-version"] = api_request.X_API_VERSION;

	return headers;

}

/**
 * @param {Object} api_request
 * @returns {String}
 */
function generateAuthHeader(api_request) {
	var hash = getQueryHash(api_request);

	var items = {};

	items["OAuth"] = "";
	items[api_request.oAuth.versionKey]         = api_request.oAuth.version;
	items[api_request.oAuth.signatureMethodKey] = "HMAC-SHA1";
	items[api_request.oAuth.nonceKey]           = "\"" + api_request.nonce + "\"";
	items[api_request.oAuth.consumerKeyKey]     = "\"" + api_request.consumerKey + "\"";
	items[api_request.oAuth.timestampKey]       = "\"" + api_request.timestamp + "\"";
	items[api_request.oAuth.signatureKey]       = "\"" + hash + "\"";

	var parameters = [];
	for (key in items) {
		if (items[key] != '') {
			parameters.push(key + "=" + items[key]);
		} else {
			parameters.push(key);
		}

	}

	return parameters.join(',');
}

/**
 * @param {Object} api_request
 * @returns {String}
 */
function getQueryHash(api_request) {
	var md5cs = crypto.createHash('md5').update(api_request.consumerSecret).digest("hex");
	var escquery = encodeURIComponent(api_request.queryUrl);
	var hash = crypto.createHmac('sha1', md5cs).update(escquery).digest('base64');
	return encodeURIComponent(hash);
}

var Utils = {
	encodeUtf8: function(s) {
		return Utils.unescapeUtf8(encodeURIComponent(s));
	},

	decodeUtf8: function(s) {
		return decodeURIComponent(Utils.escapeUtf8(s));
	},

	escapeUtf8: function(str) {
		return str.replace(/[^*+.-9A-Z_a-z-]/g, function(s) {
			var c = s.charCodeAt(0);
			return (c<16?"%0"+c.toString(16):c<128?"%"+c.toString(16):c<2048?"%"+(c>>6|192).toString(16)+"%"+(c&63|128).toString(16):"%"+(c>>12|224).toString(16)+"%"+(c>>6&63|128).toString(16)+"%"+(c&63|128).toString(16)).toUpperCase()
		});
	},
	unescapeUtf8: function(str) {
		return str.replace(/%(E(0%[AB]|[1-CEF]%[89AB]|D%[89])[0-9A-F]|C[2-9A-F]|D[0-9A-F])%[89AB][0-9A-F]|%[0-7][0-9A-F]/ig, function(s) {
			var c = parseInt(s.substring(1), 16);
			return String.fromCharCode(c<128?c:c<224?(c&31)<<6|parseInt(s.substring(4),16)&63:((c&15)<<6|parseInt(s.substring(4),16)&63)<<6|parseInt(s.substring(7),16)&63)
		});
	}
}

//function runSyncRequest(api_request) {
//	var request_options = {
//		headers: api_request.headers,
//		followRedirects: false
//	};
//
//	if (api_request.postParams) {
//		request_options.body = api_request.postParams;
//	}
//	var result = syncRequest(api_request.method, api_request.queryUrl, request_options);
//	return processResponse(api_request, {
//		status: result.statusCode,
//		data: result.body.toString('utf8')
//	});
//}
function getRequestOptions(api_request) {
	var request_options = {
		url: api_request.queryUrl,
		method: api_request.method,
		headers: api_request.headers,
		encoding: 'utf8'
	};

	if (api_request.postParams) {
		request_options.body = api_request.postParams;
	}

	return request_options;
}

function runSyncRequest(api_request) {
	var request_options = getRequestOptions(api_request);
	request_options.encoding = 'utf8';
	var result = syncRequest(request_options);
	return processResponse(api_request, {
		status: result.statusCode,
		data: result.body
	});
}

function runAsyncRequest(api_request, callback) {
	var request_options = getRequestOptions(api_request);
	request(request_options, function(error, response, body) {
		if(error) {
			throw error;
		} else {
			callback(processResponse(api_request, {
				status: response.statusCode,
				data: body
			}));
		}
	});
}

function runPromiseRequest(api_request) {
	var request_options = getRequestOptions(api_request);

	return new promise(function (resolve, reject){
		request(request_options, function(error, response, body) {
			if(error) {
				reject(error);
			} else {
				var result = processResponse(api_request, {
					status: response.statusCode,
					data: body
				});
				if (response.statusCode == 200 || response.statusCode == 202) {
					resolve(result);
				} else {
					reject(new Error(result.message, result.status));
				}
			}
		});
	});
}

function processResponse(api_request, response) {
	var method = api_request.method.toLowerCase(),
		status = response["status"],
		message = response["data"];

	api_request.onResponse.call(api_request.session, {
		status: status,
		message: message
	});

	if(method === "delete") {
		if (status === 202) {
			return status;
		}

		api_request.onError.call(api_request.session, {
			status: status,
			message: message
		});
	} else {
		if (status == 200) {
			var result =  JSON.parse(Utils.decodeUtf8(message));
			if (api_request.callAfterResponseHook) {
				api_request.onAfterResponse.call(api_request.session, result);
			}
			return result
		}

		if (status == 202) {
			return status;
		}

		api_request.onError.call(api_request.session, {
			status: status,
			message: message
		});
	}

	return response;
}

exports.runApiRequest = runApiRequest;

/**
 * @param {Session} string
 * @param {Object} config
 */
function obtainSessionKeys(session) {
    if (fs.existsSync(session.session_file)) {
        try {
            var contents = fs.readFileSync(session.session_file).toString();
            var info = JSON.parse(contents)
            if (info.id) {
                var url = session_key_url + '/' + info.id + '.json?appkey=' + session.appkey
                var response = syncRequest(url)
                if (response.statusCode == 200) {
                    var json_res = JSON.parse(response.body)
                    session.consumerKey = json_res.custom_params.key
                    session.consumerSecret = json_res.custom_params.secret
                    return true
                }
            }
        } catch(e) {}
        // Do something
    }
    var url = session_key_url + '.json?appkey=' + session.appkey;
    var data = {
        username: session.username,
        password: session.password
    }
    var response = syncRequest({
        url: url,
        method: "POST",
        body: JSON.stringify(data),
        encoding: 'utf8'
    })
    if (response.statusCode != 200) {
        return false
    }
    var json_res = JSON.parse(response.body)
    var info = {
        id: json_res.id
    }
    fs.writeFileSync(session.session_file, JSON.stringify(info))
    session.consumerKey = json_res.custom_params.key
    session.consumerSecret = json_res.custom_params.secret
    return true
}

exports.obtainSessionKeys = obtainSessionKeys;
