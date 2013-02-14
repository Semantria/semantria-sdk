<?php

require_once 'xmlhandlers.php';
require_once 'common.php';

class Semantria_Session
{
    // the consumer key and secret
    protected $consumerKey;
    protected $consumerSecret;
    protected $applicationName;

    protected $serializer;
    protected $format = 'json';
    protected $wrapperName = 'PHP';

    public $host = "https://api21.semantria.com";

    public function __construct($consumerKey = null, $consumerSecret = null, $serializer = null, $applicationName = null)
    {
        if (empty($consumerKey)) {
            throw new Exception('Parameter is null or empty "'.$consumerKey.'"');
        }
        if (empty($consumerSecret)) {
            throw new Exception('Parameter is null or empty "'.$consumerSecret.'"');
        }

        $this->consumerKey = $consumerKey;
        $this->consumerSecret = $consumerSecret;

        if (empty($applicationName)) {
            $this->applicationName = $this->wrapperName;
        } else {
            $this->applicationName = $applicationName . '.' . $this->wrapperName;
        }

        if (empty($serializer)) {
            $serializer = new Semantria_JsonSerializer();
        }

        $this->serializer = $serializer;
        $this->format = $serializer->getType();
    }

    public function setCallbackHandler(Semantria_CallbackHandler_Default &$callback)
    {
        $this->callback = $callback;
    }

    public function registerSerializer($serializer)
    {
        if (!empty($serializer)) {
            $this->serializer = $serializer;
            $this->format = $serializer->getType();
        } else {
            throw new Exception('Parameter is null or empty "'.$serializer.'"');
        }
    }

    public function createUpdateProxy()
    {
        return array("added"=>array(), "removed"=>array(), "cloned"=>array());
    }

    public function getStatus()
    {
        $url = string_format('{0}/status.{1}', $this->host, $this->format);
        return $this->runRequest("GET", $url, "get_status");
    }

    public function verifySubscription()
    {
        $url = string_format('{0}/subscription.{1}', $this->host, $this->format);
        return $this->runRequest("GET", $url, "get_subscription");
    }

    public function getConfigurations()
    {
        $url = string_format('{0}/configurations.{1}', $this->host, $this->format);
        $result = $this->runRequest("GET", $url, "get_configurations");

        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function updateConfigurations($updates)
    {
        $url = string_format('{0}/configurations.{1}', $this->host, $this->format);
        $wrapper = $this->getTypeWrapper("update_configurations");
        $data = $this->serializer->serialize($updates, $wrapper);
        return $this->runRequest("POST", $url, null, $data);
    }

    public function getBlacklist($configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/blacklist.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/blacklist.{1}', $this->host, $this->format);
        }

        $result = $this->runRequest("GET", $url, "get_blacklist");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function updateBlacklist($updates, $configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/blacklist.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/blacklist.{1}', $this->host, $this->format);
        }

        $wrapper = $this->getTypeWrapper("update_blacklist");
        $data = $this->serializer->serialize($updates, $wrapper);
        return $this->runRequest("POST", $url, null, $data);
    }

    public function getCategories($configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/categories.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/categories.{1}', $this->host, $this->format);
        }

        $result = $this->runRequest("GET", $url, "get_categories");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function updateCategories($updates, $configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/categories.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/categories.{1}', $this->host, $this->format);
        }

        $wrapper = $this->getTypeWrapper("update_categories");
        $data = $this->serializer->serialize($updates, $wrapper);
        return $this->runRequest("POST", $url, null, $data);
    }

    public function getQueries($configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/queries.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/queries.{1}', $this->host, $this->format);
        }

        $result = $this->runRequest("GET", $url, "get_queries");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function updateQueries($updates, $configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/queries.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/queries.{1}', $this->host, $this->format);
        }

        $wrapper = $this->getTypeWrapper("update_queries");
        $data = $this->serializer->serialize($updates, $wrapper);
        return $this->runRequest("POST", $url, null, $data);
    }

    public function getSentimentPhrases($configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/sentiment.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/sentiment.{1}', $this->host, $this->format);
        }

        $result = $this->runRequest("GET", $url, "get_sentiment_phrases");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function updateSentimentPhrases($updates, $configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/sentiment.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/sentiment.{1}', $this->host, $this->format);
        }

        $wrapper = $this->getTypeWrapper("update_sentiment_phrases");
        $data = $this->serializer->serialize($updates, $wrapper);
        return $this->runRequest("POST", $url, null, $data);
    }

    public function getEntities($configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/entities.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/entities.{1}', $this->host, $this->format);
        }

        $result = $this->runRequest("GET", $url, "get_entities");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function updateEntities($updates, $configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/entities.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/entities.{1}', $this->host, $this->format);
        }

        $wrapper = $this->getTypeWrapper("update_entities");
        $data = $this->serializer->serialize($updates, $wrapper);
        return $this->runRequest("POST", $url, null, $data);
    }

    public function queueDocument($task, $configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/document.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/document.{1}', $this->host, $this->format);
        }

        $wrapper = $this->getTypeWrapper("queue_document");
        $data = $this->serializer->serialize($task, $wrapper);

        $result = $this->runRequest("POST", $url, "get_processed_documents", $data);
        if (isset($result) && !is_int($result)) {
            $this->onDocsAutoResponse($result);
            return 200;
        } else {
            return $result;
        }
    }

    public function queueBatchOfDocuments($batch, $configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/document/batch.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/document/batch.{1}', $this->host, $this->format);
        }

        $wrapper = $this->getTypeWrapper("queue_batch_documents");
        $data = $this->serializer->serialize($batch, $wrapper);
        $result = $this->runRequest("POST", $url, "get_processed_documents", $data);
        if (isset($result) && !is_int($result)) {
            $this->onDocsAutoResponse($result);
            return 200;
        } else {
            return $result;
        }
    }

    public function getDocument($id, $configId=null)
    {
        if (isset($configId)) {
            if (isset($id)) {
                $url = string_format('{0}/document/{1}.{2}?config_id={3}', $this->host, $id, $this->format, $configId);
            } else {
                throw new Exception('Parameter is null or empty "'.$id.'"');
            }

        } else {
            $url = string_format('{0}/document/{1}.{2}', $this->host, $id, $this->format);
        }

        return $this->runRequest("GET", $url, "get_document");
    }

    public function cancelDocument($id, $configId=null)
    {
        if (isset($configId)) {
            if (isset($id)) {
                $url = string_format('{0}/document/{1}.{2}?config_id={3}', $this->host, $id, $this->format, $configId);
            } else {
                throw new Exception('Parameter is null or empty "'.$id.'"');
            }

        } else {
            $url = string_format('{0}/document/{1}.{2}', $this->host, $id, $this->format);
        }

        return $this->runRequest("DELETE", $url);
    }

    public function getProcessedDocuments($configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/document/processed.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/document/processed.{1}', $this->host, $this->format);
        }

        $result = $this->runRequest("GET", $url, "get_processed_documents");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function queueCollection($task, $configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/collection.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/collection.{1}', $this->host, $this->format);
        }

        $wrapper = $this->getTypeWrapper("queue_collection");
        $data = $this->serializer->serialize($task, $wrapper);
        $result = $this->runRequest("POST", $url, "get_processed_collections", $data);
        if (isset($result) && !is_int($result)) {
            $this->onCollsAutoResponse($result);
            return 200;
        } else {
            return $result;
        }
    }

    public function getCollection($id, $configId=null)
    {
        if (isset($configId)) {
            if (isset($id)) {
                $url = string_format('{0}/collection/{1}.{2}?config_id={3}', $this->host, $id, $this->format, $configId);
            } else {
                throw new Exception('Parameter is null or empty "'.$id.'"');
            }

        } else {
            $url = string_format('{0}/collection/{1}.{2}', $this->host, $id, $this->format);
        }

        return $this->runRequest("GET", $url, "get_collection");
    }

    public function cancelCollection($id, $configId=null)
    {
        if (isset($configId)) {
            if (isset($id)) {
                $url = string_format('{0}/collection/{1}.{2}?config_id={3}', $this->host, $id, $this->format, $configId);
            } else {
                throw new Exception('Parameter is null or empty "'.$id.'"');
            }

        } else {
            $url = string_format('{0}/collection/{1}.{2}', $this->host, $id, $this->format);
        }

        return $this->runRequest("DELETE", $url);
    }

    public function getProcessedCollections($configId=null)
    {
        if (isset($configId)) {
            $url = string_format('{0}/collection/processed.{1}?config_id={2}', $this->host, $this->format, $configId);
        } else {
            $url = string_format('{0}/collection/processed.{1}', $this->host, $this->format);
        }

        $result = $this->runRequest("GET", $url, "get_processed_collections");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    private function runRequest($method, $url, $type=null, $postData = null)
    {
        $this->onRequest(array("method"=>$method, "url"=>$url, "message"=>$postData));
        $request = new Semantria_AuthRequest($this->consumerKey, $this->consumerSecret);

        $response = $request->authWebRequest($method, $url, $postData);
        $this->onResponse($response);

        $status = $response["status"];
        $message = $response["message"];

        if ($method == "DELETE") {
            if ($status == 200 or $status == 202) {
                return $status;
            } else {
                $this->resolveError($status, $message);
                return $status;
            }
        } else {
            if ($status == 200) {
                $handler = $this->getTypeHandler($type);
                $obj = $this->serializer->deserialize($message, $handler);
                return $obj;
            } elseif ($status == 202) {
                if ($method == "POST") {
                    return $status;
                } else {
                    return null;
                }
            } else {
                $this->resolveError($status, $message);
            }
        }
    }

    private function resolveError($status, $message=null)
    {
        if ($status == 400 || $status == 401 || $status == 402 || $status == 403 || $status == 406 || $status == 500)
            $this->onError(array("status"=>$status, "message"=>$message));
        else
            throw new Exception('Exception code: '.$status.' and message: '.$message);
    }

    private function getTypeHandler($type)
    {
        if ($this->serializer->gettype() == "json") {
            return null;
        }

        #only for xml serializer
        if ($type == "get_status") {
            return new Semantria_XmlHandler_Status();
        } elseif ($type == "get_subscription") {
            return new GetSubscriptionHandler();
        } elseif ($type == "get_configurations") {
            return new GetConfigurationsHandler();
        } elseif ($type == "get_blacklist") {
            return new GetBlacklistHandler();
        } elseif ($type == "get_categories") {
            return new GetCategoriesHandler();
        } elseif ($type == "get_queries") {
            return new GetQueriesHandler();
        } elseif ($type == "get_sentiment_phrases") {
            return new GetSentimentPhrasesHandler();
        } elseif ($type == "get_entities") {
            return new GetEntitiesHandler();
        } elseif ($type == "get_document") {
            return new GetDocumentHandler();
        } elseif ($type == "get_processed_documents") {
            return new GetProcessedDocumentsHandler();
        } elseif ($type == "get_collection") {
            return new GetCollectionHandler();
        } elseif ($type == "get_processed_collections") {
            return new GetProcessedCollectionsHandler();
        } else {
            return null;
        }
    }

    private function getTypeWrapper($type)
    {
        if ($this->serializer->gettype() == "json") {
            return null;
        }

        #only for xml serializer
        if ($type == "update_configurations") {
            return array("root"=>"configurations", "added"=>"configuration", "removed"=>"configuration");
        } elseif ($type == "update_blacklist") {
            return array("root"=>"blacklist", "added"=>"item", "removed"=>"item");
        } elseif ($type == "update_categories") {
            return array("root"=>"categories", "added"=>"category", "removed"=>"category", "samples"=>"sample");
        } elseif ($type == "update_queries") {
            return array("root"=>"queries", "added"=>"query", "removed"=>"query");
        } elseif ($type == "update_sentiment_phrases") {
            return array("root"=>"phrases", "added"=>"phrase", "removed"=>"phrase");
        } elseif ($type == "update_entities") {
            return array("root"=>"entities", "added"=>"entity", "removed"=>"entity");
        } elseif ($type == "queue_document") {
            return array("root"=>"document");
        } elseif ($type == "queue_batch_documents") {
            return array("root"=>"documents", "item"=>"document");
        } elseif ($type == "queue_collection") {
            return array("root"=>"collection", "documents"=>"document");
        } else {
            return null;
        }
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

