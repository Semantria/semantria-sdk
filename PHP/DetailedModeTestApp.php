<?php

require_once('semantria/session.php');

echo "Semantria service demo ...", "\r\n";

// the consumer key and secret
define('CONSUMER_KEY', "");
define('CONSUMER_SECRET', "");

// Task statuses
define('TASK_STATUS_UNDEFINED', 'UNDEFINED');
define('TASK_STATUS_FAILED', 'FAILED');
define('TASK_STATUS_QUEUED', 'QUEUED');
define('TASK_STATUS_PROCESSED', 'PROCESSED');


$initialTexts = array(
    "Lisa - there's 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh's). I couldn't find them instore and i'm not going to walmart before the 19th. Oh well sounds like i'm not missing much ...lol",
    "In Lake Louise - a guided walk for the family with Great Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The \"must-do\" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.",
    "On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time."
);

class SessionCallbackHandler extends \Semantria\CallbackHandler
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

function queuedComparator($value) {
    return $value == TASK_STATUS_QUEUED;
}

// Initializes new session with the serializer object and the keys.
$session = new \Semantria\Session(CONSUMER_KEY, CONSUMER_SECRET, NULL, NULL, TRUE);

// Initialize session callback handler
$callback = new SessionCallbackHandler();
$session->setCallbackHandler($callback);

// A dictionary that keeps IDs of sent documents and their statuses. 
// It's required to make sure that we get correct documents from the API.
$tracker = array();
$documets = array();

foreach ($initialTexts as $text) {
    // Creates a sample document which need to be processed on Semantria
    // Unique document ID
    // Source text which need to be processed
    $doc_id = uniqid('', TRUE);

    $documents[] = array('id' => $doc_id, 'text' => $text);
    $tracker[$doc_id] = TASK_STATUS_QUEUED;
}

$docsCount = count($documents);

$res = $session->queueBatch($documents);
if ($res == 200 || $res == 202) {
    print("${docsCount} documents queued successfully.\n");
}
else {
    die("Unexpected error.\n");
}

print("\n");

$results = array();

// As Semantria isn't real-time solution you need to wait some time
// before getting of the processed results.
// In real application here can be implemented two separate jobs, 
// one for queuing of source data another one for retreiving.
while (count(array_filter($tracker, 'queuedComparator'))) {
    usleep(500000);

    print("Retrieving your processed results...\n");
    $response = $session->getProcessedDocuments();

    foreach ($response as $item) {
        if (array_key_exists($item['id'], $tracker)) {
            $tracker[$item['id']] = $item['status'];
            $results[] = $item;
        }
    }
}

print("\n");

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

    print("\n");
}
