<?php

// require_once 'PHPUnit/Autoload.php';
require_once 'semantria/session.php';

// the consumer key and secret
define('CONSUMER_KEY', "");
define('CONSUMER_SECRET', "");

$id = uniqid('');
$message = "Amazon Web Services has announced a new feature called VM£Ware Import, which "
    . "allows IT departments to move virtual machine images from their internal data centers to the cloud.";

class SessionCallbackHandler extends \Semantria\CallbackHandler
{
    function onRequest($sender, $args)
    {
        // $s = json_encode($args);
        // echo "\nREQUEST: " . $s;
    }

    function onResponse($sender, $args)
    {
        // $s = json_encode($args);
        // echo "\nRESPONSE: " . $s;
    }

    function onError($sender, $args)
    {
        $s = json_encode($args);
        echo "\nERROR: " . $s;
    }

    function onDocsAutoResponse($sender, $args)
    {
        // $s = json_encode($args);
        // echo "\nDOCS AUTORESPONSE: " . $s;
    }

    function onCollsAutoResponse($sender, $args)
    {
        // $s = json_encode($args);
        // echo "\nCOLLS AUTORESPONSE: " . $s;
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

    function testCRUDConfirurations()
    {
        $test_configuration = array(
            'auto_response' => false,
            'is_primary' => false,
            'name' => 'TEST_CONFIG',
            'language' => 'English',
            'document' => array(
                'query_topics_limit' => 5,
                'concept_topics_limit' => 5,
                'named_entities_limit' => 5,
                'user_entities_limit' => 5,
            ),
        );

        $response = $this->session->addConfigurations(array($test_configuration));
        $this->assertNotEquals(0, count($response));

        $test_configuration = null;
        foreach ($response as $configuration) {
            if ($configuration['name'] == 'TEST_CONFIG') {
                $test_configuration = $configuration;
                break;
            }
        }

        $this->assertNotNull($test_configuration);
        $this->assertNotEmpty($test_configuration['id']);
        $this->assertEquals('English', $test_configuration['language']);
        $this->assertEquals(80, $test_configuration['chars_threshold']);
        $configuration_id = $test_configuration['id'];

        $test_configuration['chars_threshold'] = 20;
        $response = $this->session->updateConfigurations(array($test_configuration));
        $this->assertNotEquals(0, count($response));

        $test_configuration = null;
        foreach ($response as $configuration) {
            if ($configuration['id'] == $configuration_id) {
                $test_configuration = $configuration;
                break;
            }
        }

        $this->assertNotNull($test_configuration);
        $this->assertEquals(20, $test_configuration['chars_threshold']);
    }

    function testCRUDBlacklist()
    {
        $response = $this->session->getBlacklist();
        $this->assertInternalType('array', $response);

        $response = $this->session->addBlacklist(array(array("name" => "php*")));
        $this->assertNotEquals(0, count($response));

        $item = null;
        foreach ($response as $blacklist) {
            if ($blacklist['name'] == 'php*') {
                $item = $blacklist;
                break;
            }
        }

        $this->assertNotNull($item);

        $status = $this->session->removeBlacklist(array($item['id']));
        $this->assertEquals(202, $status);
    }

    function testCRUDCategories()
    {
        $response = $this->session->getCategories();
        $this->assertInternalType('array', $response);

        $response = $this->session->addCategories(array(
            array("name" => "TEST_CATEGORY_PHP", "samples" => array("Amazon", "VMWare")),
        ));
        $this->assertNotEquals(0, count($response));

        $category = null;
        foreach ($response as $item) {
            if ($item['name'] == 'TEST_CATEGORY_PHP') {
                $category = $item;
                break;
            }
        }

        $this->assertNotNull($category);

        $status = $this->session->removeCategories(array($category['id']));
        $this->assertEquals(202, $status);
    }

    function testCRUDQueries()
    {
        $response = $this->session->getQueries();
        $this->assertInternalType('array', $response);

        $response = $this->session->addQueries(array(
            array("name" => "TEST_QUERY", "query" => "Amazon")
        ));
        $this->assertNotEquals(0, count($response));

        $query = null;
        foreach ($response as $item) {
            if ($item['name'] == 'TEST_QUERY') {
                $query = $item;
                break;
            }
        }
        $this->assertNotNull($query);

        $status = $this->session->removeQueries(array($query['id']));
        $this->assertEquals(202, $status);
    }

    function testCRUDPhrases()
    {
        $response = $this->session->getPhrases();
        $this->assertInternalType('array', $response);

        $response = $this->session->addPhrases(array(
           array("name" => "TEST_PHRASE", "weight" => 0.1)
        ));
        $this->assertNotEquals(0, count($response));

        $phrase = null;
        foreach ($response as $item) {
            if ($item['name'] == 'TEST_PHRASE') {
                $phrase = $item;
                break;
            }
        }
        $this->assertNotNull($phrase);

        $status = $this->session->removePhrases(array($phrase['id']));
        $this->assertEquals(202, $status);
    }

    function testCRUDEntities()
    {
        $response = $this->session->getEntities();
        $this->assertInternalType('array', $response);

        $response = $this->session->addEntities(array(
            array("name" => "TEST_ENTITY", "type" => "TEST_TYPE")
        ));
        $this->assertNotEquals(0, count($response));

        $entity = null;
        foreach ($response as $item) {
            if ($item['name'] == 'TEST_ENTITY') {
                $entity = $item;
                break;
            }
        }
        $this->assertNotNull($entity);

        $status = $this->session->removeEntities(array($entity['id']));
        $this->assertEquals(202, $status);
    }

    function testCRUDTaxonomy()
    {
        $response = $this->session->getTaxonomy();
        $this->assertInternalType('array', $response);

        $response = $this->session->addTaxonomy(array(
            array('name' => 'TEST_TAXONOMY'),
        ));
        $this->assertNotEquals(0, count($response));

        $taxonomy = null;
        foreach ($response as $item) {
            if ($item['name'] == 'TEST_TAXONOMY') {
                $taxonomy = $item;
                break;
            }
        }
        $this->assertNotNull($taxonomy);

        $status = $this->session->removeTaxonomy(array($taxonomy['id']));
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
