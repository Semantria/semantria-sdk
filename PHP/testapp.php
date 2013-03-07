<?php

require_once('semantria/session.php');

echo "Semantria service demo ...", "\r\n";

// the consumer key and secret
define('CONSUMER_KEY', "");
define('CONSUMER_SECRET', "");

$initialTexts = array(
    "Lisa - there's 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh's). I couldn't find them instore and i'm not going to walmart before the 19th. Oh well sounds like i'm not missing much ...lol",
    "In Lake Louise - a guided walk for the family with Great Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The \"must-do\" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.",
    "On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time."
);

class SessionCallbackHandler extends CallbackHandler
{
    function onRequest($sender, $args)
    {
        //$s = json_encode($args);
        //echo "REQUEST: ", htmlspecialchars($s), "\r\n";
    }

    function onResponse($sender, $args)
    {
        //$s = json_encode($args);
        //echo "RESPONSE: ", htmlspecialchars($s), "\r\n";
    }

    function onError($sender, $args)
    {
        $s = json_encode($args);
        echo "ERROR: ", htmlspecialchars($s), "\r\n";
    }

    function onDocsAutoResponse($sender, $args)
    {
        //$s = json_encode($args);
        //echo "DOCS AUTORESPONSE: ", htmlspecialchars($s), "\r\n";
    }

    function onCollsAutoResponse($sender, $args)
    {
        //$s = json_encode($args);
        //echo "COLLS AUTORESPONSE: ", htmlspecialchars($s), "\r\n";
    }
}

// Initializes new session with the serializer object and the keys.
$session = new Session(CONSUMER_KEY, CONSUMER_SECRET, NULL, NULL, TRUE);

// Initialize session callback handler
$callback = new SessionCallbackHandler();
$session->setCallbackHandler($callback);

foreach ($initialTexts as $text) {
    // Creates a sample document which need to be processed on Semantria
    // Unique document ID
    // Source text which need to be processed
    $doc = array("id" => uniqid(''), "text" => $text);

    // Queues document for processing on Semantria service
    $status = $session->queueDocument($doc);
    // Check status from Semantria service
    if ($status == 202) {
        echo "Document ", $doc["id"], " queued successfully.", "\r\n";
    }
}

// Count of the sample documents which need to be processed on Semantria
$length = count($initialTexts);
$results = array();

while (count($results) < $length) {
    echo "Please wait 10 sec for documents ...", "\r\n";
    // As Semantria isn't real-time solution you need to wait some time before getting of the processed results
    // In real application here can be implemented two separate jobs, one for queuing of source data another one for retreiving
    // Wait ten seconds while Semantria process queued document
    sleep(10);

    // Requests processed results from Semantria service
    $status = $session->getProcessedDocuments();
    // Check status from Semantria service
    if (is_array($status)) {
        $results = array_merge($results, $status);
    }
    echo count($status), " documents received successfully.", "\r\n";
}

foreach ($results as $data) {
    // Printing of document sentiment score
    echo "Document ", $data["id"], " Sentiment score: ", $data["sentiment_score"], "\r\n";

    // Printing of document themes
    echo "Document themes:", "\r\n";
    if ($data["themes"]) {
        foreach ($data["themes"] as $theme) {
            echo "	", $theme["title"], " (sentiment: ", $theme["sentiment_score"], ")", "\r\n";
        }
    }

    // Printing of document entities
    echo "Entities:", "\r\n";
    if (isset($data["entities"])) {
        foreach ($data["entities"] as $entity) {
            echo "	", $entity["title"], " : ", $entity["entity_type"], " (sentiment: ", $entity["sentiment_score"], ")", "\r\n";
        }
    }

    echo "\r\n";
}
