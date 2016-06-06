;(function() {
	/**
	 * @var {Function} emptyFn
	 *
	 */
	var emptyFn = function() {};
	
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

	var MD5 = (function() {
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

		return {
			getHash: function(source) {
				return calcMD5(source);
			}
		}
	})();

	var Utils = {
		HEX: "0123456789ABCDEF",

		encodeUtf8: function(s) {
  			return Utils.unescapeUtf8(encodeURIComponent(s));
  		},

		decodeUtf8: function(s) {
  			return decodeURIComponent(Utils.escapeUtf8(s));
		},

		nibbleToHex: function(nibble) {
			return Utils.HEX.charAt(nibble);
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
		},

		base64: function(wordArray) {
			// Shortcuts
			var words = wordArray.words;
			var sigBytes = wordArray.sigBytes;
			var map = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';

			// Clamp excess bits
			wordArray.clamp();

			// Convert
			var base64Chars = [];
			for (var i = 0; i < sigBytes; i += 3) {
				var byte1 = (words[i >>> 2]	   >>> (24 - (i % 4) * 8))	   & 0xff;
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
		},

		getHmacSha1: function(key, message) {
			return Utils.base64(CryptoJS.HmacSHA1(message, key));
		},


		/**
		 * Function returns string in ?q1=v1&q2=v2 format
		 * from passed config key-value pairs
		 *
		 * @param {Object} config
		 * @returns {undefined}
		 */
		createQueryStringFromConfig: function(config) {
			if(!config) {
				return "";
			}

			var result = [];

			for(var key in config) {
				if(typeof config[key] == "undefined") {
					continue;
				}

				result.push(key + "=" + config[key]);
			}

			if(result.length > 0) {
				return "?" + result.join("&");
			}

			return "";
		}
	};

	var Request = function(consumerKey, consumerSecret, applicationName, acceptEncoding, apiVersion) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
		this.applicationName = applicationName;
		this.acceptEncoding = acceptEncoding;
		this.apiVersion = apiVersion;
	};


	Request.prototype = {

		/**
		 * @property {Object}
		 */
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

		/**
		 * @returns {Number}
		 */
		generateNonce: function () {
			return Math.floor(Math.random() * 9999999);
		},

		/**
		 * @returns {Number}
		 */
		generateTimestamp: function() {
			return (new Date()).getTime();
		},

		/**
		 * @param {Number} timestamp
		 * @param {Number} nonce
		 * @returns {String}
		 */
		getNormalizedParameters: function(timestamp, nonce) {
			var items = {};

			items[this.oAuth.consumerKeyKey] = this.consumerKey;
			items[this.oAuth.nonceKey] = nonce;
			items[this.oAuth.signatureMethodKey] = "HMAC-SHA1";
			items[this.oAuth.timestampKey] = timestamp;
			items[this.oAuth.versionKey] = this.oAuth["version"];

			return Utils.createQueryStringFromConfig(items);
		},

		/**
		 * @param {String} method
		 * @param {String} url
		 * @param {Number} timestamp
		 * @param {Number} nonce
		 * @returns {String}
		 */
		generateQuery: function(method, url, timestamp, nonce) {
			var queryStr = this.getNormalizedParameters(timestamp, nonce);

			if (url.indexOf("?") != -1) {
				return url + '&' + queryStr.substring(1);
			}

			return url += queryStr;
		},

		generateAuthHeader: function(query, timestamp, nonce) {
			var md5cs = MD5.getHash(this.consumerSecret);
			var escquery = encodeURIComponent(query);
			var hash = encodeURIComponent(Utils.getHmacSha1(md5cs, escquery));

			var items = {};

			items["OAuth"] = "";
			items[this.oAuth.versionKey] = this.oAuth["version"];
			items[this.oAuth.signatureMethodKey] = "HMAC-SHA1";
			items[this.oAuth.nonceKey] = "\"" + nonce + "\"";
			items[this.oAuth.consumerKeyKey] = "\"" + this.consumerKey + "\"";
			items[this.oAuth.timestampKey] = "\"" + timestamp + "\"";
			items[this.oAuth.signatureKey] = "\"" + hash + "\"";

			var parameters = [];
			for (key in items) {
				if (items[key] != '') {
					parameters.push(key + "=" + items[key]);
				} else {
					parameters.push(key);
				}

			}

			return parameters.join(',');
		},

		getRequestHeaders: function(method, nonce, timestamp, query) {
			var headers = {};

			headers["Authorization"] = this.generateAuthHeader(query, timestamp, nonce);

			if (method == "POST") {
				headers["Content-Type"] = "application/x-www-form-urlencoded";
			}

			headers["x-app-name"] = this.applicationName;
			headers["x-api-version"] = this.apiVersion;
			if (typeof navigator != "undefined" && !/WebKit/.test(navigator.userAgent))
				headers["Accept-Encoding"] = this.acceptEncoding;

			return headers;
		},

		runRequest: function(method, url, headers, postData) {
			var xmlHttp;

			xmlHttp = new XMLHttpRequest();

			if(!("withCredentials" in xmlHttp)) {
				xmlHttp = new XDomainRequest();
			}

			xmlHttp.open(method, url, false);
			
			for (key in headers) {
				xmlHttp.setRequestHeader(key, headers[key]);
			}
			
			xmlHttp.send(postData);

			return {
				status: xmlHttp.status, 
				reason: xmlHttp.statusText, 
				data: xmlHttp.responseText
			}
		},

		authWebRequest: function(method, url, postData) {
			var nonce = this.generateNonce(),
				timestamp = this.generateTimestamp(),
				query = this.generateQuery(method, url, timestamp, nonce),
				headers = this.getRequestHeaders(method, nonce, timestamp, query);

			return this.runRequest(method, query, headers, postData);
		}
	};

    var authRequest = function(appkey, id, username, password) {
        var url = 'https://semantria.com/auth/session';
        var method = "GET"
        var postData = ""
        if (id !== false) {
            url += '/' + id;
        } else {
            method = "POST"
            postData = JSON.stringify({
                username: username,
                password: password
            })
        }
        url += '.json?appkey=' + appkey;
        response = Request.prototype.runRequest(method, url, [], postData)
        if (response.status != 200) {
            return false
        }
        var json_res = JSON.parse(response.data)
        if (!json_res) {
            return false
        }
        return {
            id: json_res.id,
            key: json_res.custom_params.key,
            secret: json_res.custom_params.secret
        }
    }

    var getCookie = function (name) {
        var value = "; " + document.cookie;
        var parts = value.split("; " + name + "=");
        if (parts.length == 2) return parts.pop().split(";").shift();
    }
    var createCookie = function (name,value,days) {
        if (days) {
            var date = new Date();
            date.setTime(date.getTime()+(days*24*60*60*1000));
            var expires = "; expires="+date.toGMTString();
        }
        else var expires = "";
        document.cookie = name+"="+value+expires+"; path=/";
    }
    var obtainSessionKeys = function(session) {
        var id = getCookie(session.session_cookie)
        if (id) {
            var data = authRequest(session.appkey, id)
            if (data) {
                session.consumerKey = data.key
                session.consumerSecret = data.secret
                return true
            }
        }
        var data = authRequest(session.appkey, false, session.username, session.password)
        if (!data) {
            return false
        }
        createCookie(session.session_cookie, data.id, 30)
        session.consumerKey = data.key
        session.consumerSecret = data.secret
        return true
    }
	/**
	 * initializes the API
	 *
	 * @param {Object} config
	 * @returns {undefined}
	 */
	var Session = function(config) {
		if(typeof config != "object") {
			config = {
				"consumerKey": arguments[0],
				"consumerSecret": arguments[1],
				"applicationName": arguments[2],
				"format": arguments[3],
				"useCompression": arguments[4],
			}
		}
		this.eventHandlers = {};
		this.consumerKey = config.consumerKey;
		this.consumerSecret = config.consumerSecret;
		this.username = config.username;
		this.password = config.password;
		this.appkey = config.appkey || 'cd954253-acaf-4dfa-a417-0a8cfb701f12';
		this.session_cookie = config.session_cookie || 'sematriasession' ;
		this.useCompression = config.useCompression || true;
		this.format = config.format || "json";
		this.acceptEncoding = this.useCompression ? 'gzip, deflate' : 'identity';
		this.applicationName = config.applicationName ? (config.applicationName + "/") : "";

		if(!this.consumerKey && !this.consumerSecret) {
			obtainSessionKeys(this)
		}
		if(!this.consumerKey || !this.consumerSecret) {
			throw "ConsumerKey and ConsumerSecret should be specified in order to use SDK";
		}

		this.applicationName += this.tpl("JavaScript/{SDK_VERSION}/{format}", {
			SDK_VERSION: this.SDK_VERSION,
			format: this.format
		});
	};

	Session.prototype = {

		/**
		 * @var {String} SDK_VERSION
		 * @constant
		 */
		SDK_VERSION: "4.1.83",

		/**
		 * @var {String} X_API_VERSION
		 * @constant
		 */
		X_API_VERSION: '4.2',

		/**
		 * @var {String} HOST
		 * @constant
		 */
		API_HOST: "https://api.semantria.com",

		/**
		 * @var {Object} eventHandlers
		 */
		eventHandlers: null,

		/**
		 * @param {String} string
		 * @param {Object} config
		 * @returns {String}
		 */
		tpl: function(string, config) {
			return string.replace(/{([^{]*)}/g, function(fm, im) {
				return config[im];
			});
		},

		/**
		 * @param {String} key
		 * @param {String} value
		 */
		override: function(key, value) {
			if(arguments.length == 1) {
				for(var k in key) {
					this.override(k, key[k]);
				}
			} else {
				this[key] = value;
			}

			return this;
		},

		/**
		 * @param {Object} params
		 * @returns {String}
		 */
		serialize: function(params) {
			return Utils.encodeUtf8(JSON.stringify(params));
		},

		/**
		 * @param {String} jsonSource
		 * @returns {Object}
		 */
		deserialize: function(jsonSource) {
			return JSON.parse(Utils.decodeUtf8(jsonSource));
		},
		
		/**
		 * Function will be called before each request
		 * @returns {undefiend}
		 */
		onRequest: emptyFn,

		/**
		 * Function will be called after each response
		 * @returns {undefiend}
		 */
		onResponse: emptyFn,

		/**
		 * Function will be called if some errors occured
		 * @returns {undefiend}
		 */
		onError: emptyFn,

		/**
		 * @returns {undefiend}
		 */
		onAfterResponse: emptyFn,
		/**
		 * @returns {string}
		 */

		/**
		 * @returns {undefiend}
		 */
		formatUrl: function(url, params) {
			return this.tpl("{host}{url}.{format}{getParams}", {
				host: this.API_HOST,
				url: url,
				format: this.format,
				getParams: Utils.createQueryStringFromConfig(params)
			});
		},

		getAPIversion: function() {
			return this.X_API_VERSION;
		},

		/**
		 * @param {string} version
		 */
		setAPIversion: function(version) {
			this.X_API_VERSION = version;
		},

		/**
		 * 
		 */
		getStatus: function() {
			return this.runRequest("GET", "/status");
		},
		
		getSupportedFeatures: function(language) {
			return this.runRequest("GET", "/features", {
				getParams: {
					language: language
				}
			});
		},

		/**
		 * 
		 */
		getSubscription: function() {
			return this.runRequest("GET", "/subscription");
		},

		/**
		 * 
		 */
		getStatistics: function() {
			return this.runRequest("GET", "/statistics");
		},

		/**
		 * 
		 */
		getConfigurations: function() {
			return this.runRequest("GET", "/configurations");
		},

		/**
		 * @param {Object} params
		 */
		addConfigurations: function(params) {
            return this.runRequest("POST", "/configurations", {
                postParams: params
            });
		},

		/**
		 * @param {string[]} name - new configuration name
		 * @param {string[]} template - template configuration id
		 * @param {(boolean|SemantriaApiCallback)} [false] callback
		 *    false - synchronous call; retutn api response
		 *    true  - asynchronous call; retutn Promise
		 *    SemantriaApiCallback - asynchronous call
		 * @returns {*}
		 */
		cloneConfiguration: function(name, template, callback) {
			var params = {
				name: name,
				template: template
			};
			return this.addConfigurations([params], callback);
		},

		/**
		 * @param {Object} params
		 */
		updateConfigurations: function(params) {
			return this.runRequest("PUT", "/configurations", {
				postParams: params
			});
		},

		/**
		 * @param {Object} params
		 */
		removeConfigurations: function(params) {
			return this.runRequest("DELETE", "/configurations", {
				postParams: params
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		getBlacklist: function(configId) {
			return this.runRequest("GET", "/blacklist", { 
				getParams: { 
					config_id: configId 
				}
			});
		},
		
		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		addBlacklist: function(params, configId) {
			return this.runRequest("POST", "/blacklist", { 
				getParams:  {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		updateBlacklist: function(params, configId) {
			return this.runRequest("PUT", "/blacklist", {
				getParams:  {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		removeBlacklist: function(params, configId) {
			return this.runRequest("DELETE", '/blacklist', {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		getCategories: function(configId) {
			return this.runRequest("GET", "/categories", {
				getParams: { 
					config_id: configId 
				}
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		addCategories: function(params, configId) {
			return this.runRequest("POST", "/categories", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		updateCategories: function(params, configId) {
			return this.runRequest("PUT", "/categories", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		removeCategories: function(params, configId) {
			return this.runRequest("DELETE", "/categories", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Number} configId
		 */
		getQueries: function(configId) {
			return this.runRequest("GET", "/queries", {
				getParams: {
					config_id: configId
				}
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		addQueries: function(params, configId) {
			return this.runRequest("POST", "/queries", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		updateQueries: function(params, configId) {
			return this.runRequest("PUT", "/queries", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		removeQueries: function(params, configId) {
			return this.runRequest("DELETE", "/queries", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Number} configId
		 */
		getEntities: function(configId) {
			return this.runRequest("GET", "/entities", {
				getParams: {
					config_id: configId
				}
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		addEntities: function(params, configId) {
			return this.runRequest("POST", "/entities", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		updateEntities: function(params, configId) {
			return this.runRequest("PUT", "/entities", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		removeEntities: function(params, configId) {
			return this.runRequest("DELETE", "/entities", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},

		
		/**
		 * @param {Number} configId
		 */
		getPhrases: function(configId) {
			return this.runRequest("GET", "/phrases", {
				getParams: {
					config_id: configId
				}
			});
		},


		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		addPhrases: function(params, configId) {
			return this.runRequest("POST", "/phrases", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},


		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		updatePhrases: function(params, configId) {
			return this.runRequest("PUT", "/phrases", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		removePhrases: function(params, configId) {
			return this.runRequest("DELETE", "/phrases", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},


		/**
		 * @param {Number} configId
		 */
		getTaxonomy: function(configId) {
			return this.runRequest("GET", "/taxonomy", {
				getParams: {
					config_id: configId
				}
			});
		},


		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		addTaxonomy: function(params, configId) {
			return this.runRequest("POST", "/taxonomy", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},


		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		updateTaxonomy: function(params, configId) {
			return this.runRequest("PUT", "/taxonomy", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Object} params
		 * @param {Number} configId
		 */
		removeTaxonomy: function(params, configId) {
			return this.runRequest("DELETE", "/taxonomy", {
				getParams: {
					config_id: configId
				},
				postParams: params
			});
		},

		/**
		 * @param {Object} doc
		 * @param {Number} configId
		 */
		queueDocument: function(doc, configId) {
			var result = this.runRequest("POST", "/document", {
				getParams: {
					config_id: configId
				},
				postParams: doc
			});

			if (result) {
				this.onAfterResponse(result);
				return result;
			}
			
			return result;
		},

		/**
		 * @param {Object} batch
		 * @param {Number} configId
		 */
		queueBatchOfDocuments: function(batch, configId) {
			var result = this.runRequest("POST", "/document/batch", {
				getParams: {
					config_id: configId
				},
				postParams: batch
			});

			if (result) {
				this.onAfterResponse(result);
				return result;
			}
			
			return result;
		},

		/**
		 * @param {Object} collection
		 * @param {Number} configId
		 */
		queueCollection: function(collection, configId) {
			var result = this.runRequest("POST", "/collection", {
				getParams: {
					config_id: configId
				},
				postParams: collection
			});

			if (result) {
				this.onAfterResponse(result);
				return result;
			}
			
			return result;
		},

		/**
		 * @param {Number} id
		 * @param {Number} configId
		 */
		getDocument: function(id, configId) {
			if(!id) {
				throw "Specified document's ID is empty";
			}

			var url = "/document/" + encodeURIComponent(id);
			var result = this.runRequest("GET", url, {
				getParams: {
					config_id: configId
				}
			});

			if (result) {
				this.onAfterResponse(result);
				return result;
			}
			
			return result;
		},

		/**
		 * @param {Number} id
		 * @param {Number} configId
		 */
		getCollection: function(id, configId) {
			if(!id) {
				throw "Specified document's ID is empty";
			}

			return this.runRequest("GET", "/collection/" + encodeURIComponent(id), {
				getParams: {
					config_id: configId
				}
			});
		},

		/**
		 * @param {Number} id
		 * @param {Number} configId
		 */
		cancelDocument: function(id, configId) {
			if (!id) {
				throw "Specified document's ID is empty";
			}

			return this.runRequest("DELETE", "/document/" + encodeURIComponent(id), {
				getParams: {
					config_id: configId
				}
			});
		},

		/**
		 * @param {Number} id
		 * @param {Number} configId
		 */
		cancelCollection: function(id, configId) {
			if (!id) {
				throw "Specified document's ID is empty";
			}

			return this.runRequest("DELETE", "/collection/" + encodeURIComponent(id), {
				getParams: {
					config_id: configId
				}
			});
		},

		getProcessedDocuments: function(configId) {
			return this.runRequest("GET", "/document/processed", {
				getParams: {
					config_id: configId
				}
			});
		},

		getProcessedDocumentsByJobId: function(jobId) {
			return this.runRequest("GET", "/document/processed", {
				getParams: {
					job_id: jobId
				}
			});
		},

		getProcessedCollections: function(configId) {
			return this.runRequest("GET", "/collection/processed", {
				getParams: {
					config_id: configId
				}
			});
		},

		getProcessedCollectionsByJobId: function(jobId) {
			return this.runRequest("GET", "/collection/processed", {
				getParams: {
					job_id: jobId
				}
			});
		},

		/**
		 * @param {String} method
		 * @param {String} url
		 * @param {Object} config
		 * @returns {undefined}
		 */
		runRequest: function(method, url, config) {
			config = config || {}
			config.postParams = config.postParams || null
			url = this.formatUrl(url, config.getParams);

			var request = new Request(
				this.consumerKey, 
				this.consumerSecret, 
				this.applicationName, 
				this.acceptEncoding,
				this.X_API_VERSION
			);

			if(config.postParams) {
				config.postParams = this.serialize(config.postParams);
			}

			this.onRequest({
				method: method,
				url: url, 
				message: config.postParams
			});

			var response = request.authWebRequest(method, url, config.postParams);
			var status = response["status"],
				reason = response["reason"],
				message = response["data"];

			this.onResponse({
				status: status,
				reason: reason,
				message: message
			});

			return this.processResponse(method, status, reason, message, response);
		},

		/**
		 * @param {String} method
		 * @param {Number} status
		 * @param {String} reason
		 * @param {String} message
		 * @param {Object} response
		 * @returns {Number|Object}
		 */
		processResponse: function(method, status, reason, message, response) {
			method = method.toLowerCase();

			if(method == "delete") {
				if (status === 202) {
					return status;
				}

				this.onError({
					status: status, 
					message: message
				});
			} else {
				if (status == 200) {
					return this.deserialize(message);
				}

				if (status == 202) {
					return status;
				}

				this.onError({
					status: status, 
					message: message
				});
			}

			return response;
		}
	}

	Semantria = {
		Session: Session
	}
})();