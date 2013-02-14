<?php

require_once 'init.php';

$initialTexts = array(
    "Lisa - there's 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh's). I couldn't find them instore and i'm not going to walmart before the 19th. Oh well sounds like i'm not missing much ...lol",
    "In Lake Louise - a guided walk for the family with Great Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The \"must-do\" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.",
    "On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time."
);

echo "Semantria service demo .", "\r\n";

// Creates JSON serializer instance
$serializer = new Semantria_JsonSerializer();
// Initializes new session with the serializer object and the keys.
$session = new Semantria_Session(Config::$consumerKey, Config::$consumerSecret, $serializer);
// Initialize session callback handler
$callback = new Semantria_CallbackHandler_Default();
$session->setCallbackHandler($callback);

foreach ($initialTexts as $text) {
    // Creates a sample document which need to be processed on Semantria
    // Unique document ID
    // Source text which need to be processed
    $doc = array("id"=>uniqid(''), "text"=>$text);
    
    // Queues document for processing on Semantria service
    $status = $session->queueDocument($doc);
    // Check status from Semantria service
    if ($status == 202) {
        echo "\"", $doc["id"], "\" document queued successfully.", "\r\n";
    }
}

// Count of the sample documents which need to be processed on Semantria
$length = count($initialTexts);	
$results = array();

while (count($results) < $length) {
    echo "Retrieving your processed results...", "\r\n";
    // As Semantria isn't real-time solution you need to wait some time before getting of the processed results
    // In real application here can be implemented two separate jobs, one for queuing of source data another one for retreiving
    // Wait some seconds while Semantria process queued document
    sleep(2);
    
    // Requests processed results from Semantria service
    $status = $session->getProcessedDocuments();
    // Check status from Semantria service
    if (is_array($status)) {
        $results = array_merge($results, $status); 
    }
}

foreach ($results as $data) {
    // Printing of document sentiment score
    echo "Document ", $data["id"], " Sentiment score: ", $data["sentiment_score"], "\r\n";
    
    // Printing of document themes
    echo "Document themes:", "\r\n";
    if (isset($data["themes"]) && is_array($data['themes'])) {
        foreach ($data["themes"] as $theme) {
            echo "	", $theme["title"], " (sentiment: ", $theme["sentiment_score"], ")", "\r\n";
        }
    }
    
    // Printing of document entities
    echo "Entities:", "\r\n";
    if (isset($data["entities"]) && is_array($data['entities'])) {
        foreach ($data["entities"] as $entity) {
            echo "	", $entity["title"], " : ", $entity["entity_type"], " (sentiment: ", $entity["sentiment_score"], ")", "\r\n";
        }
    }

    echo "\r\n";
}
