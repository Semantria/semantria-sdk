<?php
namespace Semantria;

require_once('authrequest.php');
require_once('jsonserializer.php');
//require_once('common.php');

define('WRAPPER_VERSION', "3.1.74");

class Session
{
    // the consumer key and secret
    protected $consumerKey;
    protected $consumerSecret;
    protected $applicationName;

    protected $serializer;
    protected $format = 'json';
    protected $wrapperName = 'PHP';

    private $host = "https://api30.semantria.com";
    private $callback;
    private $use_compression = FALSE;
    private $request;

    public function __construct($consumerKey, $consumerSecret, $serializer = NULL, $applicationName = NULL, $use_compression = FALSE)
    {
        if (empty($consumerKey)) {
            throw new \Exception('Consumer KEY can\'t be empty.');
        }

        if (empty($consumerSecret)) {
            throw new \Exception('Consumer SECRET can\'t be empty.');
        }

        $this->consumerKey = $consumerKey;
        $this->consumerSecret = $consumerSecret;
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

        $this->request = new AuthRequest(
            $this->consumerKey,
            $this->consumerSecret,
            $this->applicationName,
            $this->use_compression
        );
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

    public function getStatus()
    {
        $url = "{$this->host}/status.{$this->format}";
        return $this->runRequest("GET", $url, "get_status");
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
        return $this->updateConfigurations($items);
    }

    public function updateConfigurations($items)
    {
        if (!is_array($items)) $items = array($items);

        $url = "{$this->host}/configurations.{$this->format}";
        $wrapper = $this->getTypeWrapper("update_configurations");
        $data = $this->serializer->serialize($items, $wrapper);
        return $this->runRequest("POST", $url, NULL, $data);
    }

    public function removeConfigurations($items)
    {
        if (!is_array($items)) $items = array($items);

        $url = "{$this->host}/configurations.{$this->format}";
        $wrapper = $this->getTypeWrapper("remove_configurations");
        $data = $this->serializer->serialize($items, $wrapper);
        return $this->runRequest("DELETE", $url, NULL, $data);
    }

    public function getBlacklist($configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/blacklist.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/blacklist.{$this->format}";
        }

        $result = $this->runRequest("GET", $url, "get_blacklist");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function addBlacklist($items, $configId = NULL)
    {
        return $this->updateBlacklist($items, $configId);
    }

    public function updateBlacklist($items, $configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/blacklist.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/blacklist.{$this->format}";
        }

        $wrapper = $this->getTypeWrapper("update_blacklist");
        $data = $this->serializer->serialize($items, $wrapper);
        return $this->runRequest("POST", $url, NULL, $data);
    }

    public function removeBlacklist($items, $configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/blacklist.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/blacklist.{$this->format}";
        }

        $wrapper = $this->getTypeWrapper("remove_blacklist");
        $data = $this->serializer->serialize($items, $wrapper);
        return $this->runRequest("DELETE", $url, NULL, $data);
    }

    public function getCategories($configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/categories.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/categories.{$this->format}";
        }

        $result = $this->runRequest("GET", $url, "get_categories");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function addCategories($items, $configId = NULL)
    {
        return $this->updateCategories($items, $configId);
    }

    public function updateCategories($items, $configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/categories.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/categories.{$this->format}";
        }

        $wrapper = $this->getTypeWrapper("update_categories");
        $data = $this->serializer->serialize($items, $wrapper);
        return $this->runRequest("POST", $url, NULL, $data);
    }

    public function removeCategories($items, $configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/categories.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/categories.{$this->format}";
        }

        $wrapper = $this->getTypeWrapper("remove_categories");
        $data = $this->serializer->serialize($items, $wrapper);
        return $this->runRequest("DELETE", $url, NULL, $data);
    }

    public function getQueries($configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/queries.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/queries.{$this->format}";
        }

        $result = $this->runRequest("GET", $url, "get_queries");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function addQueries($items, $configId = NULL)
    {
        return $this->updateQueries($items, $configId);
    }

    public function updateQueries($items, $configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/queries.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/queries.{$this->format}";
        }

        $wrapper = $this->getTypeWrapper("update_queries");
        $data = $this->serializer->serialize($items, $wrapper);
        return $this->runRequest("POST", $url, NULL, $data);
    }

    public function removeQueries($items, $configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/queries.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/queries.{$this->format}";
        }

        $wrapper = $this->getTypeWrapper("remove_queries");
        $data = $this->serializer->serialize($items, $wrapper);
        return $this->runRequest("DELETE", $url, NULL, $data);
    }

    public function getPhrases($configId = null)
    {
        if (isset($configId)) {
            $url = "{$this->host}/phrases.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/phrases.{$this->format}";
        }

        $result = $this->runRequest("GET", $url, "get_phrases");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function addPhrases($items, $configId = NULL)
    {
        return $this->updatePhrases($items, $configId);
    }

    public function updatePhrases($items, $configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/phrases.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/phrases.{$this->format}";
        }

        $wrapper = $this->getTypeWrapper("update_phrases");
        $data = $this->serializer->serialize($items, $wrapper);
        return $this->runRequest("POST", $url, NULL, $data);
    }

    public function removePhrases($items, $configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/phrases.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/phrases.{$this->format}";
        }

        $wrapper = $this->getTypeWrapper("remove_phrases");
        $data = $this->serializer->serialize($items, $wrapper);
        return $this->runRequest("DELETE", $url, NULL, $data);
    }

    public function getEntities($configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/entities.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/entities.{$this->format}";
        }

        $result = $this->runRequest("GET", $url, "get_entities");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function addEntities($items, $configId = NULL)
    {
        return $this->updateEntities($items, $configId);
    }

    public function updateEntities($items, $configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/entities.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/entities.{$this->format}";
        }

        $wrapper = $this->getTypeWrapper("update_entities");
        $data = $this->serializer->serialize($items, $wrapper);
        return $this->runRequest("POST", $url, NULL, $data);
    }

    public function removeEntities($items, $configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/entities.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/entities.{$this->format}";
        }

        $wrapper = $this->getTypeWrapper("remove_entities");
        $data = $this->serializer->serialize($items, $wrapper);
        return $this->runRequest("DELETE", $url, NULL, $data);
    }

    public function queueDocument($task, $configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/document.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/document.{$this->format}";
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

    public function queueBatch($batch, $configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/document/batch.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/document/batch.{$this->format}";
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

    public function getDocument($id, $configId = NULL)
    {
        if (!isset($id)) throw new \Exception('Document ID is null or empty');

        if (isset($configId)) {
            $url = "{$this->host}/document/{$id}.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/document/{$id}.{$this->format}";
        }

        return $this->runRequest("GET", $url, "get_document");
    }

    public function cancelDocument($id, $configId = NULL)
    {
        if (!isset($id)) throw new \Exception('Document ID is null or empty');

        if (isset($configId)) {
            $url = "{$this->host}/document/{$id}.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/document/{$id}.{$this->format}";
        }

        return $this->runRequest("DELETE", $url);
    }

    public function getProcessedDocuments($configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/document/processed.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/document/processed.{$this->format}";
        }

        $result = $this->runRequest("GET", $url, "get_processed_documents");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    public function queueCollection($task, $configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/collection.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/collection.{$this->format}";
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

    public function getCollection($id, $configId = NULL)
    {
        if (!isset($id)) throw new \Exception('Collection ID is null or empty');

        if (isset($configId)) {
            $url = "{$this->host}/collection/{$id}.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/collection/{$id}.{$this->format}";
        }

        return $this->runRequest("GET", $url, "get_collection");
    }

    public function cancelCollection($id, $configId = NULL)
    {
        if (!isset($id)) throw new \Exception('Collection ID is null or empty');

        if (isset($configId)) {
            $url = "{$this->host}/collection/{$id}.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/collection/{$id}.{$this->format}";
        }

        return $this->runRequest("DELETE", $url);
    }

    public function getProcessedCollections($configId = NULL)
    {
        if (isset($configId)) {
            $url = "{$this->host}/collection/processed.{$this->format}?config_id={$configId}";
        } else {
            $url = "{$this->host}/collection/processed.{$this->format}";
        }

        $result = $this->runRequest("GET", $url, "get_processed_collections");
        if (!isset($result)) {
            $result = array();
        }
        return $result;
    }

    private function runRequest($method, $url, $type = NULL, $postData = NULL)
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
                $handler = $this->getTypeHandler($type);
                $obj = $this->serializer->deserialize($message, $handler);
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
        if ($status == 400 || $status == 401 || $status == 402 || $status == 403 || $status == 406 || $status == 500)
            $this->onError(array("status" => $status, "message" => $message));
        else
            throw new \Exception('Exception code: ' . $status . ' and message: ' . $message);
    }

    private function getTypeHandler($type)
    {
        if ($this->serializer->gettype() == "json") {
            return NULL;
        }

        // Only for xml serializer
        // Need another implementations
        /*
        if ($type == "get_status") {
            return new GetStatusHandler();
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
        } elseif ($type == "get_phrases") {
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
        */
    }

    private function getTypeWrapper($type)
    {
        if ($this->serializer->gettype() == "json") {
            return null;
        }

        // Only for xml serializer
        /*
        if ($type == "update_configurations") {
            return array("root" => "configurations", "added" => "configuration", "removed" => "configuration");
        } elseif ($type == "update_blacklist") {
            return array("root" => "blacklist", "added" => "item", "removed" => "item");
        } elseif ($type == "update_categories") {
            return array("root" => "categories", "added" => "category", "removed" => "category", "samples" => "sample");
        } elseif ($type == "update_queries") {
            return array("root" => "queries", "added" => "query", "removed" => "query");
        } elseif ($type == "update_sentiment_phrases") {
            return array("root" => "phrases", "added" => "phrase", "removed" => "phrase");
        } elseif ($type == "update_entities") {
            return array("root" => "entities", "added" => "entity", "removed" => "entity");
        } elseif ($type == "queue_document") {
            return array("root" => "document");
        } elseif ($type == "queue_batch_documents") {
            return array("root" => "documents", "item" => "document");
        } elseif ($type == "queue_collection") {
            return array("root" => "collection", "documents" => "document");
        } else {
            return NULL;
        }
        */
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
