<?php

// require_once 'PHPUnit/Autoload.php';
require_once 'semantria/session.php';


// API Key/Secret
// Set the environment vars before calling this program
// or edit this file and put your key and secret here.
define('CONSUMER_KEY', getenv("SEMANTRIA_KEY"));
define('CONSUMER_SECRET', getenv("SEMANTRIA_SECRET"));

// If set will run username/password authentication test
define('SEMANTRIA_USERNAME', getenv('SEMANTRIA_USERNAME'));
define('SEMANTRIA_PASSWORD', getenv('SEMANTRIA_PASSWORD'));


if (empty(CONSUMER_KEY) || empty(CONSUMER_SECRET)) {
  error_log("Error: missing key/secret.\n"
            . "  Define SEMANTRIA_KEY and SEMANTRIA_SECRET before running tests.");
  exit(1);
}


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
class SemantriaTest extends PHPUnit\Framework\TestCase
{
    private $session;
    private $testConfigId;

    private $testConfigName = 'TEST_CONFIG';

    function setUp()
    {
        $this->session = new \Semantria\Session(CONSUMER_KEY, CONSUMER_SECRET, NULL, NULL, TRUE);
        $callback = new SessionCallbackHandler();
        $this->session->setCallbackHandler($callback);
    }

    function getOrCreateTestConfig()
    {
        $test_config = null;
        $response = $this->session->getQueries();
        foreach ($response as $configuration) {
            if ($configuration['name'] == $this->testConfigName) {
                $test_config = $configuration;
                break;
            }
        }
        if ($test_config) {
            $this->testConfigId = $tedtConfig['id'];
        } else {
            $new_config = array(
                'name' => $this->testConfigName,
                'language' => 'English',
                'document' => array(
                    'query_topics' => true,
                    'concept_topics' => true,
                    'named_entities' => true,
                    'user_entities' => true,
                ),
            );  
            $response = $this->session->addConfigurations(array($new_config));
            $this->assertEquals(1, count($response));
            $this->testConfigId = $response[0]['id'];
        }
        return $this->testConfigId;
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

    function testCRUDConfigurations()
    {
        $test_config = array(
            'name' => $this->testConfigName,
            'language' => 'English',
            'alphanumeric_threshold' => 85,
            'document' => array(
                'query_topics' => true,
                'concept_topics' => true,
                'named_entities' => true,
                'user_entities' => true,
            ),
        );

        $response = $this->session->addConfigurations(array($test_config));
        $this->assertNotEquals(0, count($response));

        $test_config = null;
        foreach ($response as $configuration) {
            if ($configuration['name'] == $this->testConfigName) {
                $test_config = $configuration;
                break;
            }
        }

        $this->assertNotNull($test_config);
        $this->assertNotEmpty($test_config['id']);
        $this->assertEquals('English', $test_config['language']);
        $this->assertEquals(85, $test_config['alphanumeric_threshold']);
        $configuration_id = $test_config['id'];

        $new_config = array(
            'id' => $configuration_id,
            'alphanumeric_threshold' => 20,
        );
        $response = $this->session->updateConfigurations(array($new_config));
        $this->assertNotEquals(0, count($response));

        $test_config = null;
        foreach ($response as $configuration) {
            if ($configuration['id'] == $configuration_id) {
                $test_config = $configuration;
                break;
            }
        }

        $this->assertNotNull($test_config);
        $this->assertEquals(20, $test_config['alphanumeric_threshold']);
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

    function testGetProcessedCollections()
    {
        $response = $this->session->getProcessedCollections();
        $this->assertInternalType('array', $response);
    }

    function testGetUserDirectory()
    {
        $configId = $this->getOrCreateTestConfig();
        $query_name = "q1";
        $query_query = "dog AND cat";
        $response = $this->session->addQueries(
                array(array("name" => $query_name, "query" => $query_query)),
                $configId);
        $this->assertEquals(1, count($response));

        $query = null;
        foreach ($response as $item) {
            if ($item['name'] == $query_name) {
                $query = $item;
                break;
            }
        }
        $this->assertNotNull($query);

        $bytes = $this->session->getUserDirectory("tar", $configId);
        // Check that bytes contains query.
        // (This works because tar is an uncompressed format and query contains only single byte chars.)
        $this->assertTrue(strpos($bytes, $query_query) >= 0);
        $this->writeBytesToFile($bytes, "test.tar");

        $this->session->removeQueries(array($query['id']), $configId);
    }

    function writeBytesToFile($bytes, $filename)
    {
        $file = fopen($filename, "w");
        $this->assertNotNull($file);
        fwrite($file, $bytes);
        fclose($file);
    }

    function testUsernamePasswordAuthentication()
    {
        if (empty(SEMANTRIA_USERNAME) || empty(SEMANTRIA_PASSWORD)) {
            $this->assertTrue(true);   // keep unit test happy
            return;
        }

        $user_session = new \Semantria\Session(null, null, null, null, true,
                                               SEMANTRIA_USERNAME, SEMANTRIA_PASSWORD);
        $callback = new SessionCallbackHandler();
        $user_session->setCallbackHandler($callback);
      
        $response = $user_session->getStatus();
        $this->assertInternalType('array', $response);
    }

}
