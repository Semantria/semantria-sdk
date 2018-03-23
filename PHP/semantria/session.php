<?php
namespace Semantria;

require_once('authrequest.php');
require_once('jsonserializer.php');

//error_reporting(E_ALL);

define('WRAPPER_VERSION', "4.2.87");

class Session
{
    // the consumer key and secret
    protected $consumerKey;
    protected $consumerSecret;
    protected $applicationName;

    protected $serializer;
    protected $format = 'json';
    protected $wrapperName = 'PHP';

    // urls start with http and do not include a trailing slash
    private $host = "https://api.semantria.com";
    private $key_url = "https://semantria.com/auth/session";
    private $app_key = "cd954253-acaf-4dfa-a417-0a8cfb701f12";
    private $callback;
    private $use_compression = FALSE;
    private $request;

    public function __construct($consumerKey = NULL, $consumerSecret = NULL,
                                $serializer = NULL, $applicationName = NULL, $use_compression = FALSE,
                                $username = NULL, $password = NULL,
                                $session_file = '/tmp/semantria-session.dat')
    {
    	$this->use_compression = $use_compression;
    	
    	if (empty($applicationName)) {
    		$this->applicationName = $this->wrapperName . '/' . WRAPPER_VERSION;
    	} else {
    		$this->applicationName = $applicationName . '/' . $this->wrapperName . '/' . WRAPPER_VERSION;
    	}
    	
    	if (empty($serializer)) {
    		$serializer = new JsonSerializer();
    	}
    	
    	$this->serializer = $serializer;
    	$this->format = $serializer->getType();
    	$this->applicationName = $this->applicationName . '/' . strtoupper($this->format);
    	
    	if ((empty($consumerKey) || empty($consumerSecret)) && ($username && $password)) {
    		list($consumerKey, $consumerSecret) = $this->obtainKeys($username, $password, $session_file);
    		if (empty($consumerKey) || empty($consumerSecret)) {
    			throw new \Exception('Cannot obtain Semantria keys. Wrong username or password.');
    		}
    	}
        if (empty($consumerKey)) {
            throw new \Exception('Consumer KEY can\'t be empty.');
        }
        if (empty($consumerSecret)) {
            throw new \Exception('Consumer SECRET can\'t be empty.');
        }

        $this->consumerKey = $consumerKey;
        $this->consumerSecret = $consumerSecret;

        $this->request = new AuthRequest(
            $this->consumerKey,
            $this->consumerSecret,
            $this->applicationName,
            $this->use_compression
        );
    }

    

	protected function obtainKeys($username, $password, $session_file = '/tmp/semantria-session.dat') {
      $session_id = $this->getSavedSessionId($session_file, $username);
		if ($session_id) {
			$url = "{$this->key_url}/{$session_id}.json?appkey={$this->app_key}";
			$result = $this->request($url, "GET");
			if ($result['status'] == 200) {
				$message = $this->serializer->deserialize($result['message']);
				if (isset($message['custom_params']['key']) && isset($message['custom_params']['secret'])) {
					return array($message['custom_params']['key'], $message['custom_params']['secret']);
				}
			}
		}

		$url = "{$this->key_url}.json?appkey={$this->app_key}";
		$items = array('username' => $username, 'password' => $password);
	    $data = $this->serializer->serialize($items);
	    $result = $this->request($url, "POST", NULL, $data);

	    if ($result['status'] != 200) {
	    	return array(false, false);
	    }
	    
	    $message = $this->serializer->deserialize($result['message']);
	    $session_id = $message['id'];
        $this->saveSessionId($session_file, $username, $session_id);
	    
	    return array($message['custom_params']['key'], $message['custom_params']['secret']);
    }
    

    # Cache session id in a simple two line format:
    #   username
    #   session id
    protected function saveSessionId($session_file, $username, $session_id) {
      if ($session_file && $session_id) {
        file_put_contents($session_file,
                          $username . "\n" . $session_id . "\n");
      }
    }

    # Returns session id if a reasonable one is found
    protected function getSavedSessionId($session_file, $username) {
      if (is_readable($session_file)) {
        $lines = file($session_file);
        if ((count($lines) == 2) || (trim($lines[0]) == $username)) {
          return trim($lines[1]);
        }
      }
      return null;
    }

    protected function request($url, $method, $headers = NULL, $postfields = NULL) {
    	$ci = curl_init();
    	
    	if ($headers) {
    		curl_setopt($ci, CURLOPT_HTTPHEADER, $headers);
    	}
    	curl_setopt($ci, CURLOPT_VERBOSE, FALSE);
    	curl_setopt($ci, CURLOPT_RETURNTRANSFER, TRUE);
    	curl_setopt($ci, CURLOPT_SSL_VERIFYPEER, FALSE);
    	curl_setopt($ci, CURLOPT_TIMEOUT, 30);
    	
    	if ($this->use_compression) {
    		curl_setopt($ci, CURLOPT_ENCODING, "gzip,deflate");
    	}
    	
    	switch ($method) {
    		case 'POST':
    			curl_setopt($ci, CURLOPT_POST, TRUE);
    			if (!empty($postfields)) {
    				curl_setopt($ci, CURLOPT_POSTFIELDS, $postfields);
    			}
    			break;
    		case 'PUT':
    			curl_setopt($ci, CURLOPT_CUSTOMREQUEST, 'PUT');
    			if (!empty($postfields)) {
    				curl_setopt($ci, CURLOPT_POSTFIELDS, $postfields);
    			}
    			break;
    		case 'DELETE':
    			curl_setopt($ci, CURLOPT_CUSTOMREQUEST, 'DELETE');
    			if (!empty($postfields)) {
    				curl_setopt($ci, CURLOPT_POSTFIELDS, $postfields);
    			}
    	}
    	
    	curl_setopt($ci, CURLOPT_URL, $url);
    	
    	$response = curl_exec($ci);
    	$code = curl_getinfo($ci, CURLINFO_HTTP_CODE);
    	$message = $response;
    	
    	if ($code == 0) {
    		$message = curl_error($ci);
    	}
    	
    	curl_close($ci);
    	
    	$result = array("status" => $code, "message" => $message);
    	return $result;
    }

    public function setHost($host) {
        // starts with "http"?
        if (substr(strtolower($host), 0, 4) !== "http") {
            $host = "https://$host";
        }
        // ends with "/"?
        if (substr($host, -1) === "/") {
            $host = substr($host, 0, -1);
        }
        $this->host = $host;
    }

    public function setCallbackHandler(&$callback)
    {
        if (is_subclass_of($callback, '\Semantria\CallbackHandler')) {
            $this->callback = $callback;
        } else {
            throw new \Exception('Parameter is not subclass of CallbackHandler "' . $callback . '"');
        }
    }

    /**
     * @param $serializer
     * @throws \Exception
     */
    public function registerSerializer($serializer)
    {
        if (isset($serializer)) {
            $this->serializer = $serializer;
            $this->format = $serializer->getType();
        } else {
            throw new \Exception('Parameter is null or empty "' . $serializer . '"');
        }
    }
    
    public function getApiVersion() {
    	return $this->request->getApiVersion();
    }
    
    /**
     * @param string $apiVersion
     */
    public function setApiVersion($apiVersion) {
    	$this->request->setApiVersion($apiVersion);
    }

    public function getStatus()
    {
        $url = $this->makeUrl("/status");
        return $this->runRequest("GET", $url, "get_status");
    }

    public function getSupportedFeatures($language = null)
    {
        if ($language) {
            $url = "{$this->host}/features.{$this->format}?language={$language}";
        } else {
            $url = "{$this->host}/features.{$this->format}";
        }
        return $this->runRequest("GET", $url, "get_features");
    }

    public function getSubscription()
    {
        $url = "{$this->host}/subscription.{$this->format}";
        return $this->runRequest("GET", $url, "get_subscription");
    }

    public function getConfigurations()
    {
        $url = "{$this->host}/configurations.{$this->format}";
        $result = $this->runRequest("GET", $url, "get_configurations");

        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function addConfigurations($items)
    {
        return $this->updateConfigurations($items, TRUE);
    }

    public function cloneConfiguration($name, $template) {
        $items = array(array(
            "name" => $name,
            "template" => $template,
        ));

        return $this->updateConfigurations($items, TRUE);
    }

    public function updateConfigurations($items, $create = false)
    {
        if (!is_array($items)) $items = array($items);

        $url = "{$this->host}/configurations.{$this->format}";
        $data = $this->serializer->serialize($items);
        return $this->runRequest(($create ? "POST" : "PUT"), $url, NULL, $data);
    }

    public function removeConfigurations($items)
    {
        if (!is_array($items)) $items = array($items);

        $url = "{$this->host}/configurations.{$this->format}";
        $data = $this->serializer->serialize($items);
        return $this->runRequest("DELETE", $url, NULL, $data);
    }

    public function getBlacklist($configId = NULL)
    {
        $url = $this->makeUrl("/blacklist", $configId);
        $result = $this->runRequest("GET", $url, "get_blacklist");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function addBlacklist($items, $configId = NULL)
    {
        return $this->updateBlacklist($items, $configId, TRUE);
    }

    public function updateBlacklist($items, $configId = NULL, $create = TRUE)
    {
        $url = $this->makeUrl("/blacklist", $configId);
        $data = $this->serializer->serialize($items);
        return $this->runRequest(($create ? "POST" : "PUT"), $url, NULL, $data);
    }

    public function removeBlacklist($items, $configId = NULL)
    {
        $url = $this->makeUrl("/blacklist", $configId);
        $data = $this->serializer->serialize($items);
        return $this->runRequest("DELETE", $url, NULL, $data);
    }

    public function getCategories($configId = NULL)
    {
        $url = $this->makeUrl("/categories", $configId);
        $result = $this->runRequest("GET", $url, "get_categories");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function addCategories($items, $configId = NULL)
    {
        return $this->updateCategories($items, $configId, TRUE);
    }

    public function updateCategories($items, $configId = NULL, $create = FALSE)
    {
        $url = $this->makeUrl("/categories", $configId);
        $data = $this->serializer->serialize($items);
        return $this->runRequest(($create ? "POST" : "PUT"), $url, NULL, $data);
    }

    public function removeCategories($items, $configId = NULL)
    {
        $url = $this->makeUrl("/categories", $configId);
        $data = $this->serializer->serialize($items);
        return $this->runRequest("DELETE", $url, NULL, $data);
    }

    public function getQueries($configId = NULL)
    {
        $url = $this->makeUrl("/queries", $configId);
        $result = $this->runRequest("GET", $url, "get_queries");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function addQueries($items, $configId = NULL)
    {
        return $this->updateQueries($items, $configId, TRUE);
    }

    public function updateQueries($items, $configId = NULL, $create = FALSE)
    {
        $url = $this->makeUrl("/queries", $configId);
        $data = $this->serializer->serialize($items);
        return $this->runRequest(($create ? "POST" : "PUT"), $url, NULL, $data);
    }

    public function removeQueries($items, $configId = NULL)
    {
        $url = $this->makeUrl("/queries", $configId);
        $data = $this->serializer->serialize($items);
        return $this->runRequest("DELETE", $url, NULL, $data);
    }

    public function getPhrases($configId = null)
    {
        $url = $this->makeUrl("/phrases", $configId);
        $result = $this->runRequest("GET", $url, "get_phrases");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function addPhrases($items, $configId = NULL)
    {
        return $this->updatePhrases($items, $configId, TRUE);
    }

    public function updatePhrases($items, $configId = NULL, $create = FALSE)
    {
        $url = $this->makeUrl("/phrases", $configId);
        $data = $this->serializer->serialize($items);
        return $this->runRequest(($create ? "POST" : "PUT"), $url, NULL, $data);
    }

    public function removePhrases($items, $configId = NULL)
    {
        $url = $this->makeUrl("/phrases", $configId);
        $data = $this->serializer->serialize($items);
        return $this->runRequest("DELETE", $url, NULL, $data);
    }

    public function getTaxonomy($configId = null)
    {
        $url = $this->makeUrl("/taxonomy", $configId);
        $result = $this->runRequest("GET", $url, "get_taxonomy");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function addTaxonomy($items, $configId = NULL)
    {
        return $this->updateTaxonomy($items, $configId, TRUE);
    }

    public function updateTaxonomy($items, $configId = NULL, $create = FALSE)
    {
        $url = $this->makeUrl("/taxonomy", $configId);
        $data = $this->serializer->serialize($items);
        return $this->runRequest(($create ? "POST" : "PUT"), $url, NULL, $data);
    }

    public function removeTaxonomy($items, $configId = NULL)
    {
        $url = $this->makeUrl("/taxonomy", $configId);
        $data = $this->serializer->serialize($items);
        return $this->runRequest("DELETE", $url, NULL, $data);
    }

    public function getEntities($configId = NULL)
    {
        $url = $this->makeUrl("/entities", $configId);
        $result = $this->runRequest("GET", $url, "get_entities");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function addEntities($items, $configId = NULL)
    {
        return $this->updateEntities($items, $configId, TRUE);
    }

    public function updateEntities($items, $configId = NULL, $create = FALSE)
    {
        $url = $this->makeUrl("/entities", $configId);
        $data = $this->serializer->serialize($items);
        return $this->runRequest(($create ? "POST" : "PUT"), $url, NULL, $data);
    }

    public function removeEntities($items, $configId = NULL)
    {
        $url = $this->makeUrl("/entities", $configId);
        $data = $this->serializer->serialize($items);
        return $this->runRequest("DELETE", $url, NULL, $data);
    }

    public function queueDocument($task, $configId = NULL)
    {
        $url = $this->makeUrl("/document", $configId);
        $data = $this->serializer->serialize($task);

        $result = $this->runRequest("POST", $url, "get_processed_documents", $data);
        if (isset($result) && !is_int($result)) {
            $this->onDocsAutoResponse($result);
            return 200;
        } else {
            return $result;
        }
    }

    public function queueBatch($batch, $configId = NULL)
    {
        $url = $this->makeUrl("/document/batch", $configId);
        $data = $this->serializer->serialize($batch);

        $result = $this->runRequest("POST", $url, "get_processed_documents", $data);
        if (isset($result) && !is_int($result)) {
            $this->onDocsAutoResponse($result);
            return 200;
        } else {
            return $result;
        }
    }

    public function getDocument($id, $configId = NULL)
    {
        if (!isset($id)) throw new \Exception('Document ID is null or empty');

        $id = rawurlencode($id);

        $url = $this->makeUrl("/document/{$id}", $configId);
        return $this->runRequest("GET", $url, "get_document");
    }

    public function cancelDocument($id, $configId = NULL)
    {
        if (!isset($id)) throw new \Exception('Document ID is null or empty');

        $id = rawurlencode($id);

        $url = $this->makeUrl("/document/{$id}", $configId);
        return $this->runRequest("DELETE", $url);
    }

    public function getProcessedDocuments($configId = NULL)
    {
        $url = $this->makeUrl("/document/processed", $configId);
        $result = $this->runRequest("GET", $url, "get_processed_documents");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function getProcessedDocumentsByJobId($jobId)
    {
        $url = "{$this->host}/document/processed.{$this->format}?job_id={$jobId}";
        $result = $this->runRequest("GET", $url, "get_processed_documents_by_job_id");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function queueCollection($task, $configId = NULL)
    {
        $url = $this->makeUrl("/collection", $configId);
        $data = $this->serializer->serialize($task);

        $result = $this->runRequest("POST", $url, "get_processed_collections", $data);
        if (isset($result) && !is_int($result)) {
            $this->onCollsAutoResponse($result);
            return 200;
        } else {
            return $result;
        }
    }

    public function getCollection($id, $configId = NULL)
    {
        if (!isset($id)) throw new \Exception('Collection ID is null or empty');

        $id = rawurlencode($id);

        $url = $this->makeUrl("/collection/{$id}", $configId);
        return $this->runRequest("GET", $url, "get_collection");
    }

    public function cancelCollection($id, $configId = NULL)
    {
        if (!isset($id)) throw new \Exception('Collection ID is null or empty');

        $id = rawurlencode($id);

        $url = $this->makeUrl("/collection/{$id}", $configId);
        return $this->runRequest("DELETE", $url);
    }

    public function getProcessedCollections($configId = NULL)
    {
        $url = $this->makeUrl("/collection/processed", $configId);
        $result = $this->runRequest("GET", $url, "get_processed_collections");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function getProcessedCollectionsByJobId($jobId)
    {
        $url = "{$this->host}/collection/processed.{$this->format}?job_id={$jobId}";
        $result = $this->runRequest("GET", $url, "get_processed_collections_by_job_id");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    /** Returns archive of Salience user directory for $configId.
     *
     * @param $format  - one of "zip", "tar", "tar.gz"
     * @param $configId
     */
    public function getUserDirectory($format = "zip", $configId = NULL)
    {
        $url = $this->makeUrl("/salience/user-directory.{$format}", $configId, true);
        $result = $this->runRequest("GET", $url, "get_user_directory", NULL, true);
        return $result;
    }


    private function makeUrl($path, $configId = NULL, $isBinary = false)
    {
        if (substr($path, 0, 1) !== "/") {
            $path = "/{$path}";
        }
        $url = "{$this->host}{$path}";
        if (! $isBinary) {
            $url .= ".{$this->format}";
        }
        if (isset($configId)) {
            $url .= "?config_id={$configId}";
        }
        return $url;
    }

    private function runRequest($method, $url, $type = NULL, $postData = NULL, $isBinary = false)
    {
        $this->onRequest(array("method" => $method, "url" => $url, "message" => $postData));
        $response = $this->request->authWebRequest($method, $url, $postData);
        $this->onResponse($response);

        $status = $response["status"];
        $message = $response["message"];

        if ($method == "DELETE") {
            if ($status == 200 || $status == 202) {
                return $status;
            } else {
                $this->resolveError($status, $message);
                return $status;
            }
        } else {
            if ($status == 200) {
                if ($isBinary) {
                    return $message;
                }
                $obj = $this->serializer->deserialize($message);
                return $obj;
            } else if ($status == 202) {
                if ($method == "POST") {
                    return $status;
                } else {
                    return NULL;
                }
            } else {
                $this->resolveError($status, $message);
            }
        }

        return NULL;
    }

    private function resolveError($status, $message = NULL)
    {
        if ($status == 400 || $status == 401 || $status == 402 || $status == 403 || $status == 406 || $status == 413 || $status == 500)
            $this->onError(array("status" => $status, "message" => $message));
        else
            throw new \Exception('Exception code: ' . $status . ' and message: ' . $message);
    }

    private function onRequest($request)
    {
        if (isset($this->callback)) {
            call_user_func_array(array($this->callback, "onRequest"), array(&$this, $request));
        }
    }

    private function onResponse($response)
    {
        if (isset($this->callback)) {
            call_user_func_array(array($this->callback, "onResponse"), array(&$this, $response));
        }
    }

    private function onError($response)
    {
        if (isset($this->callback)) {
            call_user_func_array(array($this->callback, "OnError"), array(&$this, $response));
        }
    }

    private function onDocsAutoResponse($response)
    {
        if (isset($this->callback)) {
            call_user_func_array(array($this->callback, "OnDocsAutoResponse"), array(&$this, $response));
        }
    }

    private function onCollsAutoResponse($response)
    {
        if (isset($this->callback)) {
            call_user_func_array(array($this->callback, "OnCollsAutoResponse"), array(&$this, $response));
        }
    }
}

abstract class CallbackHandler
{
    abstract function onRequest($sender, $args);

    abstract function onResponse($sender, $args);

    abstract function onError($sender, $args);

    abstract function onDocsAutoResponse($sender, $args);

    abstract function onCollsAutoResponse($sender, $args);
}
