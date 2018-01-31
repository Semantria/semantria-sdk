var runApiRequest = require('./api-request').runApiRequest;
var obtainSessionKeys = require('./api-request').obtainSessionKeys;

/**
 * @var {Function} emptyFn
 *
 */
function emptyFn() {}

/**
 * @param {String} string
 * @param {Object} config
 * @returns {String}
 */
function tpl(string, config) {
	return string.replace(/{([^{]*)}/g, function(fm, im) {
		return config[im];
	});
}

/**
 * @callback SemantriaApiCallback
 * @param {*} apiResponse
 */


/**
 * initializes the API
 *
 * @param {Object} config
 * @returns {undefined}
 */
function Session(config) {

	if(typeof config != "object") {
		config = {
			"consumerKey": arguments[0],
			"consumerSecret": arguments[1],
			"applicationName": arguments[2],
			"format": arguments[3],
		}
	}

	this.eventHandlers = {};
	this.consumerKey = config.consumerKey;
	this.consumerSecret = config.consumerSecret;
	this.username = config.username;
	this.password = config.password;
	this.appkey = config.appkey || 'cd954253-acaf-4dfa-a417-0a8cfb701f12';
	this.session_file = config.session_file || '/tmp/session.dat' ;
	this.format = config.format || "json";
	this.applicationName = config.applicationName ? (config.applicationName + "/") : "";

	this.applicationName += tpl("NodeJs/{SDK_VERSION}/{format}", {
		SDK_VERSION: this.SDK_VERSION,
		format: this.format
	});
};

Session.prototype = {

	/**
	 * @var {String} SDK_VERSION
	 * @constant
	 */
	SDK_VERSION: require('./package').version,

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
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 *    false - synchronous call; retutn Object
	 * @returns {Object|Promise|null}
	 */
	getStatus: function(callback) {
		return runApiRequest(this, {
			path: 'status',
			callback: callback
		});
	},

	/**
	 * @param {string}language
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 *    false - synchronous call; retutn Array of Objects
	 * @returns {Object[]|Promise|null}
	 */
	getSupportedFeatures: function(language, callback) {
		return runApiRequest(this, {
			path: 'features',
			getParams: {
				language: language
			},
			callback: callback
		});
	},

	/**
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 *    false - synchronous call; retutn Object
	 * @returns {Object|Promise|null}
	 */
	getSubscription: function(callback) {
		return runApiRequest(this, {
			path: 'subscription',
			callback: callback
		});
	},

	/**
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 *    false - synchronous call; retutn Array of Objects
	 * @returns {Object[]|Promise|null}
	 */
	getStatistics: function(params, callback) {
		return runApiRequest(this, {
			path: 'statistics',
			getParams: params,
			callback: callback
		});
	},

	/**
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 *    false - synchronous call; retutn Array of Objects
	 * @returns {Object[]|Promise|null}
	 */
	getConfigurations: function(callback) {
		return runApiRequest(this, {
			path: 'configurations',
			callback: callback
		});
	},

	/**
	 * @param {Object[]} params
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	addConfigurations: function(params, callback) {
		return runApiRequest(this, {
			method: 'POST',
			path: 'configurations',
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string[]} name - new configuration name
	 * @param {string[]} template - template configuration id
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
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
	 * @param {Object[]} params
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	updateConfigurations: function(params, callback) {
		return runApiRequest(this, {
			method: 'PUT',
			path: 'configurations',
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string[]} params
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	removeConfigurations: function(params, callback) {
		return runApiRequest(this, {
			method: 'DELETE',
			path: 'configurations',
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	getBlacklist: function(configId, callback) {
		return runApiRequest(this, {
			path: 'blacklist',
			getParams: { config_id: configId },
			callback: callback
		});
	},

	/**
	 * @param {Object[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	addBlacklist: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'POST',
			path: 'blacklist',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {Object[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	updateBlacklist: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'PUT',
			path: 'blacklist',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	removeBlacklist: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'DELETE',
			path: 'blacklist',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	getCategories: function(configId, callback) {
		return runApiRequest(this, {
			path: 'categories',
			getParams: { config_id: configId },
			callback: callback
		});
	},

	/**
	 * @param {Object[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	addCategories: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'POST',
			path: 'categories',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {Object[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	updateCategories: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'PUT',
			path: 'categories',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	removeCategories: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'DELETE',
			path: 'categories',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	getQueries: function(configId, callback) {
		return runApiRequest(this, {
			path: 'queries',
			getParams: { config_id: configId },
			callback: callback
		});
	},

	/**
	 * @param {Object[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	addQueries: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'POST',
			path: 'queries',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {Object[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	updateQueries: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'PUT',
			path: 'queries',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	removeQueries: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'DELETE',
			path: 'queries',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	getEntities: function(configId, callback) {
		return runApiRequest(this, {
			path: 'entities',
			getParams: { config_id: configId },
			callback: callback
		});
	},

	/**
	 * @param {Object[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	addEntities: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'POST',
			path: 'entities',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {Object[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	updateEntities: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'PUT',
			path: 'entities',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	removeEntities: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'DELETE',
			path: 'entities',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	getPhrases: function(configId, callback) {
		return runApiRequest(this, {
			path: 'phrases',
			getParams: { config_id: configId },
			callback: callback
		});
	},

	/**
	 * @param {Object[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	addPhrases: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'POST',
			path: 'phrases',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {Object[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	updatePhrases: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'PUT',
			path: 'phrases',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	removePhrases: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'DELETE',
			path: 'phrases',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	getTaxonomy: function(configId, callback) {
		return runApiRequest(this, {
			path: 'taxonomy',
			getParams: { config_id: configId },
			callback: callback
		});
	},

	/**
	 * @param {Object[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	addTaxonomy: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'POST',
			path: 'taxonomy',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {Object[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	updateTaxonomy: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'PUT',
			path: 'taxonomy',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {string[]} params
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	removeTaxonomy: function(params, configId, callback) {
		return runApiRequest(this, {
			method: 'DELETE',
			path: 'taxonomy',
			getParams: { config_id: configId },
			postParams: params,
			callback: callback
		});
	},

	/**
	 * @param {Object} doc
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	queueDocument: function(doc, configId, callback) {
		return runApiRequest(this, {
			method: 'POST',
			path: 'document',
			getParams: { config_id: configId },
			postParams: doc,
			callAfterResponseHook: true,
			callback: callback
		});
	},

	/**
	 * @param {Object[]} batch
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	queueBatchOfDocuments: function(batch, configId, callback) {
		return runApiRequest(this, {
			method: 'POST',
			path: 'document/batch',
			getParams: { config_id: configId },
			postParams: batch,
			callAfterResponseHook: true,
			callback: callback
		});
	},

	/**
	 * @param {Object} collection
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	queueCollection: function(collection, configId, callback) {
		return runApiRequest(this, {
			method: 'POST',
			path: 'collection',
			getParams: { config_id: configId },
			postParams: collection,
			callAfterResponseHook: true,
			callback: callback
		});
	},

	/**
	 * @param {string} id
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	getDocument: function(id, configId, callback) {
		if(!id) {
			throw "Specified document's ID is empty";
		}

		return runApiRequest(this, {
			path: 'document/' + encodeURIComponent(id),
			getParams: { config_id: configId },
			callAfterResponseHook: true,
			callback: callback
		});
	},

	/**
	 * @param {string} id
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	getCollection: function(id, configId, callback) {
		if(!id) {
			throw "Specified document's ID is empty";
		}

		return runApiRequest(this, {
			path: 'collection/' + encodeURIComponent(id),
			getParams: { config_id: configId },
			callback: callback
		});
	},

	/**
	 * @param {string} id
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	cancelDocument: function(id, configId, callback) {
		if (!id) {
			throw "Specified document's ID is empty";
		}

		return runApiRequest(this, {
			method: 'DELETE',
			path: 'document/' + encodeURIComponent(id),
			getParams: { config_id: configId },
			callback: callback
		});
	},

	/**
	 * @param {string} id
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	cancelCollection: function(id, configId, callback) {
		if (!id) {
			throw "Specified document's ID is empty";
		}

		return runApiRequest(this, {
			method: 'DELETE',
			path: 'collection/' + encodeURIComponent(id),
			getParams: { config_id: configId },
			callback: callback
		});
	},

	/**
	 * @param {string|null} [null] configId  - null default configuration
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	getProcessedDocuments: function(configId, callback) {
		return runApiRequest(this, {
			path: 'document/processed',
			getParams: { config_id: configId },
			callback: callback
		});
	},

	/**
	 * @param jobId
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	getProcessedDocumentsByJobId: function(jobId, callback) {
		return runApiRequest(this, {
			path: 'document/processed',
			getParams: { job_id: jobId },
			callback: callback
		});
	},

	/**
	 * @param configId
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	getProcessedCollections: function(configId, callback) {
		return runApiRequest(this, {
			path: 'collection/processed',
			getParams: { config_id: configId },
			callback: callback
		});
	},

	/**
	 * @param jobId
	 * @param {(SemantriaApiCallback)}  If specified, callback with results. Else a promise will be returned
	 * @returns {*}
	 */
	getProcessedCollectionsByJobId: function(jobId, callback) {
		return runApiRequest(this, {
			path: 'collection/processed',
			getParams: { job_id: jobId },
			callback: callback
		});
	}
}

exports.Session = Session;
exports.obtainSessionKeys = obtainSessionKeys;
