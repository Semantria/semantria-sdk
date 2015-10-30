<?php

require_once '../vendor/autoload.php';
require_once '../semantria/session.php';

// the consumer key and secret
define('CONSUMER_KEY', "__key_george");
define('CONSUMER_SECRET', "__secret_george");

$id = uniqid('');
$message = "Amazon Web Services has announced a new feature called VM£Ware Import, which "
    . "allows IT departments to move virtual machine images from their internal data centers to the cloud.";

class SessionCallbackHandler extends \Semantria\CallbackHandler
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

/**
 * Class SemantriaTest
 * @property \Semantria\Session $session
 */
class SemantriaTest extends PHPUnit_Framework_TestCase
{
    private $consumerKey;
    private $consumerSecret;
    private $session;

    function setUp()
    {
        $this->consumerKey = CONSUMER_KEY;
        $this->consumerSecret = CONSUMER_SECRET;
        $this->session = new \Semantria\Session($this->consumerKey, $this->consumerSecret, NULL, NULL, TRUE);

        $callback = new SessionCallbackHandler();
        $this->session->setCallbackHandler($callback);
    }

    function testGetStatus()
    {
        $response = $this->session->getStatus();
        $this->assertInternalType('array', $response);
    }

    function testGetSupportedFeatures()
    {
        $response = $this->session->getSupportedFeatures();
        $this->assertInternalType('array', $response);
    }

    function testGetSubscription()
    {
        $response = $this->session->getSubscription();
        $this->assertInternalType('array', $response);
    }

    function testGetConfigurations()
    {
        $response = $this->session->getConfigurations();
        $this->assertInternalType('array', $response);
    }

    function testUpdateConfiguration()
    {
        $response = $this->session->getConfigurations();
        $this->assertInternalType('array', $response);

        $default = null;
        foreach ($response as $item) {
            if ($item["name"] == "default") {
                $item["document"]["summary_limit"] = 5;
                $item["document"]["themes_limit"] = 5;
                $item["auto_response"] = false;
                $default = $item;
            }
        }
        $this->assertInternalType('array', $default);

        $status = $this->session->updateConfigurations(array($default));
        $this->assertEquals(202, $status);
    }

    function testGetBlacklist()
    {
        $response = $this->session->getBlacklist();
        $this->assertInternalType('array', $response);
    }

    function testAddBlacklist()
    {
        $status = $this->session->addBlacklist(array("php*",));
        $this->assertEquals(202, $status);
    }

    function testRemoveBlacklist()
    {
        $status = $this->session->removeBlacklist(array("php*"));
        $this->assertEquals(202, $status);
    }

    function testGetCategories()
    {
        $response = $this->session->getCategories();
        $this->assertInternalType('array', $response);
    }

    function testAddCategories()
    {
        $status = $this->session->addCategories(array(
            array("name" => "Service", "samples" => array("Amazon", "VMWare")),
        ));
        $this->assertEquals(202, $status);
    }

    function testRemoveCategories()
    {
        $status = $this->session->removeCategories(array("Service", "SaaS services"));
        $this->assertEquals(202, $status);
    }

    function testGetQueries()
    {
        $response = $this->session->getQueries();
        $this->assertInternalType('array', $response);
    }

    function testAddQueries()
    {
        $status = $this->session->addQueries(array(
            array("name" => "Web", "query" => "Amazon")
        ));
        $this->assertEquals(202, $status);
    }

    function testRemoveQueries()
    {
        $status = $this->session->removeQueries(array("Web"));
        $this->assertEquals(202, $status);
    }

    function testGetSentimentPhrases()
    {
        $response = $this->session->getPhrases();
        $this->assertInternalType('array', $response);
    }

    function testAddSentimentPhrases()
    {
        $status = $this->session->addPhrases(array(
            array("name" => "Web", "weight" => 0.1)
        ));
        $this->assertEquals(202, $status);
    }

    function testRemoveSentimentPhrases()
    {
        $status = $this->session->removePhrases(array("Web"));
        $this->assertEquals(202, $status);
    }

    function testGetEntities()
    {
        $response = $this->session->getEntities();
        $this->assertInternalType('array', $response);
    }

    function testAddEntities()
    {
        $status = $this->session->addEntities(array(
            array("name" => "Amazon", "type" => "Web")
        ));
        $this->assertEquals(202, $status);
    }

    function testRemoveEntities()
    {
        $status = $this->session->removeEntities(array("Amazon"));
        $this->assertEquals(202, $status);
    }

    function testCreateDocument()
    {
        $status = $this->session->queueDocument(array("id" => $GLOBALS["id"], "text" => $GLOBALS["message"]));
        $this->assertTrue($status == 200 || $status == 202);
    }

    function testGetDocument()
    {
        $response = $this->session->getDocument($GLOBALS["id"]);
        $this->assertInternalType('array', $response);
    }

    /*
	function testCancelDocument() {
		$status = $this->session->cancelDocument($GLOBALS["id"]);
		$this->assertTrue(isset($status) && ($status == 200 || $status == 202));
	}
    */

    function testCreateBatchOfDocuments()
    {
        $batch = array();
        $var = 10;
        while ($var > 0) {
            array_push($batch, array("id" => uniqid(''), "text" => $GLOBALS["message"]));
            $var = $var - 1;
        }

        $status = $this->session->queueBatch($batch);
        $this->assertTrue($status == 200 || $status == 202);
    }

    function testGetProcessedDocuments()
    {
        $response = $this->session->getProcessedDocuments();
        $this->assertInternalType('array', $response);
    }

    function testCreateCollection()
    {
        $status = $this->session->queueCollection(
            array("id" => $GLOBALS["id"], "documents" => array($GLOBALS["message"], $GLOBALS["message"]))
        );
        $this->assertTrue($status == 200 || $status == 202);
    }

    function testGetCollection()
    {
        $response = $this->session->getCollection($GLOBALS["id"]);
        $this->assertInternalType('array', $response);
    }

    /*
	function testCancelCollection() {
		$status = $this->session->cancelCollection($GLOBALS["id"]);
		$this->assertTrue(isset($status) && ($status == 200 || $status == 202));
	}
    */

    function testGetProcessedCollections()
    {
        $response = $this->session->getProcessedCollections();
        $this->assertInternalType('array', $response);
    }
}
