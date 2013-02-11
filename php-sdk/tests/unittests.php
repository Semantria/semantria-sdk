<?php

require_once 'PHPUnit/Autoload.php';

require_once('semantria/session.php');
require_once('semantria/jsonserializer.php');
require_once('semantria/xmlserializer.php');

$id = uniqid('');
$message = "Amazon Web Services has announced a new feature called VM£Ware Import, which allows IT departments to move virtual machine images from their internal data centers to the cloud.";

class SessionCallbackHandler extends CallbackHandler
{
	function onRequest($sender, $args)
	{
		$s = json_encode($args);
		echo "\nREQUEST: " . $s;
	}
	
	function onResponse($sender, $args)
	{
		$s = json_encode($args);
		echo "\nRESPONSE: " . $s;
	}
	
	function onError($sender, $args)
	{
		$s = json_encode($args);
		echo "\nERROR: " . $s;
	}
	
	function onDocsAutoResponse($sender, $args)
	{
		$s = json_encode($args);
		echo "\nDOCS AUTORESPONSE: " . $s;
	}
	
	function onCollsAutoResponse($sender, $args)
	{
		$s = json_encode($args);
		echo "\nCOLLS AUTORESPONSE: " . $s;
	}
}

class SemantriaTest extends PHPUnit_Framework_TestCase
{
	function setUp() {
        ob_start();
		$this->consumerKey = Config::$consumerKey;
		$this->consumerSecret = Config::$consumerSecret;
		$this->serializer = new XmlSerializer();
		$this->session = new Session($this->consumerKey, $this->consumerSecret, $this->serializer);
		
		$callback = new SessionCallbackHandler();
		$this->session->setCallbackHandler($callback);
	}

    function tearDown() {
        $output = ob_get_contents();
        ob_end_clean();
    }

	function testGetStatus() {
		$status = $this->session->getStatus();
		$this->assertTrue(isset($status));
	}
	
	function testVerifySubscription() {
		$status = $this->session->verifySubscription();
		$this->assertTrue(isset($status));
	}
		
	function testGetConfigurations() {
		$status = $this->session->getConfigurations();
		$this->assertTrue(isset($status));
	}
	
	function testUpdateConfiguration() {
		$status = $this->session->getConfigurations();
		$this->assertTrue(isset($status));
		
		$default = null;
		foreach ($status as $item)
		{
			if ($item["name"] == "default") {
				$item["document"]["summary_limit"] = 5;
				$item["document"]["themes_limit"] = 5;
				$item["auto_responding"] = false;
				$default = $item;
			}
		}
		
		$this->assertTrue(isset($default));
		$proxy =  $this->session->createUpdateProxy();
		array_push($proxy["added"], $default);
		$status = $this->session->updateConfigurations($proxy);
		$this->assertTrue(isset($status) && $status == 202);
	}
	
	function testGetBlacklist() {
		$status = $this->session->getBlacklist();
		$this->assertTrue(isset($status));
	}
	
	function testAddBlacklist() {
		$proxy = $this->session->createUpdateProxy();
		array_push($proxy["added"], "php*");
		$status = $this->session->updateBlacklist($proxy);
		$this->assertTrue(isset($status) && $status == 202);
	}
	
	function testRemoveBlacklist() {
		$proxy = $this->session->createUpdateProxy();
		array_push($proxy["removed"], "php*");
		$status = $this->session->updateBlacklist($proxy);
		$this->assertTrue(isset($status) && $status == 202);
	}
	
	function testGetCategories() {
		$status = $this->session->getCategories();
		$this->assertTrue(isset($status));
	}
	
	function testAddCategories() {
		$proxy = $this->session->createUpdateProxy();
		array_push($proxy["added"], array("name"=>"Service", "samples"=>array("Amazon", "VMWare")));
		$status = $this->session->updateCategories($proxy);
		$this->assertTrue(isset($status) && $status == 202);
	}
	
	function testRemoveCategories() {
		$proxy = $this->session->createUpdateProxy();
		array_push($proxy["removed"], "Service");
		$status = $this->session->updateCategories($proxy);
		$this->assertTrue(isset($status) && $status == 202);
	}
	
	function testGetQueries() {
		$status = $this->session->getQueries();
		$this->assertTrue(isset($status));
	}
	
	function testAddQueries() {
		$proxy = $this->session->createUpdateProxy();
		array_push($proxy["added"], array("name"=>"Web", "query"=>"Amazon"));
		$status = $this->session->updateQueries($proxy);
		$this->assertTrue(isset($status) && $status == 202);
	}
	
	function testRemoveQueries() {
		$proxy = $this->session->createUpdateProxy();
		array_push($proxy["removed"], "Web");
		$status = $this->session->updateQueries($proxy);
		$this->assertTrue(isset($status) && $status == 202);
	}
	
	function testGetSentimentPhrases() {
		$status = $this->session->getSentimentPhrases();
		$this->assertTrue(isset($status));
	}
	
	function testAddSentimentPhrases() {
		$proxy = $this->session->createUpdateProxy();
		array_push($proxy["added"], array("title"=>"Web", "weight"=>0.1));
		$status = $this->session->updateSentimentPhrases($proxy);
		$this->assertTrue(isset($status) && $status == 202);
	}

	function testRemoveSentimentPhrases() {
		$proxy = $this->session->createUpdateProxy();
		array_push($proxy["removed"], "Web");
		$status = $this->session->updateSentimentPhrases($proxy);
		$this->assertTrue(isset($status) && $status == 202);
	}

	function testGetEntities() {
		$status = $this->session->getEntities();
		$this->assertTrue(isset($status));
	}
	
	function testAddEntities() {
		$proxy = $this->session->createUpdateProxy();
		array_push($proxy["added"], array("name"=>"Amazon", "type"=>"Web"));
		$status = $this->session->updateEntities($proxy);
		$this->assertTrue(isset($status) && $status == 202);
	}
	
	function testRemoveEntities() {
		$proxy = $this->session->createUpdateProxy();
		array_push($proxy["removed"], "Amazon");
		$status = $this->session->updateEntities($proxy);
		$this->assertTrue(isset($status) && $status == 202);
	}

	function testCreateDocument() {
		$status = $this->session->queueDocument(array("id"=>$GLOBALS["id"], "text"=>$GLOBALS["message"]));
		$this->assertTrue(isset($status) && ($status == 200 || $status == 202));
	}

	function testGetDocument() {
		$status = $this->session->getDocument($GLOBALS["id"]);
		$this->assertTrue(isset($status));
	}

	function testCancelDocument() {
		$status = $this->session->cancelDocument($GLOBALS["id"]);
		$this->assertTrue(isset($status) && ($status == 200 || $status == 202));
	}

	function testCreateBatchOfDocuments() {
		$batch = array();
		$var = 10;
		while ($var > 0)
		{
			array_push($batch, array("id"=>uniqid(''), "text"=>$GLOBALS["message"]));             
			$var = $var - 1;
		}
		
		$status = $this->session->queueBatchOfDocuments($batch);
		$this->assertTrue(isset($status) && ($status == 200 || $status == 202));
	}
	
	function testGetProcessedDocuments() {
		$status = $this->session->getProcessedDocuments();
		$this->assertTrue(is_array($status));
	}

	function testCreateCollection() {
		$status = $this->session->queueCollection(array("id"=>$GLOBALS["id"], "documents"=>array($GLOBALS["message"], $GLOBALS["message"])));
		$this->assertTrue(isset($status) && ($status == 200 || $status == 202));
	}

	function testGetCollection() {
		$status = $this->session->getCollection($GLOBALS["id"]);
		$this->assertTrue(isset($status));
	}

	function testCancelCollection() {
		$status = $this->session->cancelCollection($GLOBALS["id"]);
		$this->assertTrue(isset($status) && ($status == 200 || $status == 202));
	}
	
	function testGetProcessedCollections() {
		$status = $this->session->getProcessedCollections();
		$this->assertTrue(is_array($status));
	}	
	
}
?> 
