<?php

require_once 'PHPUnit/Autoload.php';

$id = uniqid('');
$message = "Amazon Web Services has announced a new feature called VM£Ware Import, which allows IT departments to move virtual machine images from their internal data centers to the cloud.";

class SemantriaTest extends PHPUnit_Framework_TestCase
{
    public function setUp() {
        ob_start();
        $this->consumerKey = Config::$consumerKey;
        $this->consumerSecret = Config::$consumerSecret;
        $this->serializer = new Semantria_XmlSerializer();
        $this->session = new Semantria_Session($this->consumerKey, $this->consumerSecret, $this->serializer);

        $callback = new Semantria_CallbackHandler_Terminal();
        $this->session->setCallbackHandler($callback);
    }

    public function tearDown() {
        $output = ob_get_contents();
        ob_end_clean();
    }

    public function testGetStatus() {
        $status = $this->session->getStatus();
        $this->assertTrue(isset($status));
    }

    public function testVerifySubscription() {
        $status = $this->session->verifySubscription();
        $this->assertTrue(isset($status));
    }

    public function testGetConfigurations() {
        $status = $this->session->getConfigurations();
        $this->assertTrue(isset($status));
    }

    public function testUpdateConfiguration() {
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

    public function testGetBlacklist() {
        $status = $this->session->getBlacklist();
        $this->assertTrue(isset($status));
    }

    public function testAddBlacklist() {
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["added"], "php*");
        $status = $this->session->updateBlacklist($proxy);
        $this->assertTrue(isset($status) && $status == 202);
    }

    public function testRemoveBlacklist() {
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["removed"], "php*");
        $status = $this->session->updateBlacklist($proxy);
        $this->assertTrue(isset($status) && $status == 202);
    }

    public function testGetCategories() {
        $status = $this->session->getCategories();
        $this->assertTrue(isset($status));
    }

    public function testAddCategories() {
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["added"], array("name"=>"Service", "samples"=>array("Amazon", "VMWare")));
        $status = $this->session->updateCategories($proxy);
        $this->assertTrue(isset($status) && $status == 202);
    }

    public function testRemoveCategories() {
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["removed"], "Service");
        $status = $this->session->updateCategories($proxy);
        $this->assertTrue(isset($status) && $status == 202);
    }

    public function testGetQueries() {
        $status = $this->session->getQueries();
        $this->assertTrue(isset($status));
    }

    public function testAddQueries() {
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["added"], array("name"=>"Web", "query"=>"Amazon"));
        $status = $this->session->updateQueries($proxy);
        $this->assertTrue(isset($status) && $status == 202);
    }

    public function testRemoveQueries() {
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["removed"], "Web");
        $status = $this->session->updateQueries($proxy);
        $this->assertTrue(isset($status) && $status == 202);
    }

    public function testGetSentimentPhrases() {
        $status = $this->session->getSentimentPhrases();
        $this->assertTrue(isset($status));
    }

    public function testAddSentimentPhrases() {
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["added"], array("title"=>"Web", "weight"=>0.1));
        $status = $this->session->updateSentimentPhrases($proxy);
        $this->assertTrue(isset($status) && $status == 202);
    }

    public function testRemoveSentimentPhrases() {
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["removed"], "Web");
        $status = $this->session->updateSentimentPhrases($proxy);
        $this->assertTrue(isset($status) && $status == 202);
    }

    public function testGetEntities() {
        $status = $this->session->getEntities();
        $this->assertTrue(isset($status));
    }

    public function testAddEntities() {
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["added"], array("name"=>"Amazon", "type"=>"Web"));
        $status = $this->session->updateEntities($proxy);
        $this->assertTrue(isset($status) && $status == 202);
    }

    public function testRemoveEntities() {
        $proxy = $this->session->createUpdateProxy();
        array_push($proxy["removed"], "Amazon");
        $status = $this->session->updateEntities($proxy);
        $this->assertTrue(isset($status) && $status == 202);
    }

    public function testCreateDocument() {
        $status = $this->session->queueDocument(array("id"=>$GLOBALS["id"], "text"=>$GLOBALS["message"]));
        $this->assertTrue(isset($status) && ($status == 200 || $status == 202));
    }

    public function testGetDocument() {
        $status = $this->session->getDocument($GLOBALS["id"]);
        $this->assertTrue(isset($status));
    }

    public function testCancelDocument() {
        $status = $this->session->cancelDocument($GLOBALS["id"]);
        $this->assertTrue(isset($status) && ($status == 200 || $status == 202));
    }

    public function testCreateBatchOfDocuments() {
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

    public function testGetProcessedDocuments() {
        $status = $this->session->getProcessedDocuments();
        $this->assertTrue(is_array($status));
    }

    public function testCreateCollection() {
        $status = $this->session->queueCollection(array("id"=>$GLOBALS["id"], "documents"=>array($GLOBALS["message"], $GLOBALS["message"])));
        $this->assertTrue(isset($status) && ($status == 200 || $status == 202));
    }

    public function testGetCollection() {
        $status = $this->session->getCollection($GLOBALS["id"]);
        $this->assertTrue(isset($status));
    }

    public function testCancelCollection() {
        $status = $this->session->cancelCollection($GLOBALS["id"]);
        $this->assertTrue(isset($status) && ($status == 200 || $status == 202));
    }

    public function testGetProcessedCollections() {
        $status = $this->session->getProcessedCollections();
        $this->assertTrue(is_array($status));
    }

    public function testUtf8Encode() {
        $encoded = Semantria_Common::utf8Encode(
            array('a' => 'AA', 'b' => 1, 'c' => '€w', 'd' => 'áéóúZ')
            );
        $this->assertTrue(true);
    }

    public function testUtf8Decode() {
        $encoded = Semantria_Common::utf8Encode(
            array('a' => 'AA', 'b' => 1, 'c' => '€w', 'd' => 'áéóúZ')
        );
        print_r($encoded);
        $decoded = Semantria_Common::utf8Decode(array($encoded));
        $this->assertTrue(true);
    }
}
