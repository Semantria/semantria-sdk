<?php

require_once('../semantria/session.php');

echo "Semantria service demo ...", "\r\n";

// The consumer key and secret
define('CONSUMER_KEY', getenv("SEMANTRIA_KEY"));
define('CONSUMER_SECRET', getenv("SEMANTRIA_SECRET"));

// Task statuses
define('TASK_STATUS_UNDEFINED', 'UNDEFINED');
define('TASK_STATUS_FAILED', 'FAILED');
define('TASK_STATUS_QUEUED', 'QUEUED');
define('TASK_STATUS_PROCESSED', 'PROCESSED');


$initialTexts = array();

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

$subscription = $session->getSubscription();

// A dictionary that keeps IDs of sent documents and their statuses. 
// It's required to make sure that we get correct documents from the API.
$tracker = array();
$documets = array();

print("Reading collection from file...\n");
$file = fopen("source.txt", "r");
if (!$file) {
    print("");
    exit(1);
}

while (($line = fgets($file)) !== FALSE) {
    $initialTexts[] = $line;
}
if (!feof($file)) {
    echo "Error: unexpected fgets() fail\n";
}
fclose($file);

foreach ($initialTexts as $text) {
    // Creates a sample document which need to be processed on Semantria
    // Unique document ID
    // Source text which need to be processed
    $doc_id = uniqid('', TRUE);

    $documents[] = array('id' => $doc_id, 'text' => $text);
    $tracker[$doc_id] = TASK_STATUS_QUEUED;
    
    if (count($documents) == $subscription['basic_settings']['incoming_batch_limit']) {
        $docsCount = count($documents);
        $res = $session->queueBatch($documents);
        if ($res == 200 || $res == 202) {
            print("${docsCount} documents queued successfully.\n");
            $documents = array();
        }
    }
}

$docsCount = count($documents);
if ($docsCount) {
    $res = $session->queueBatch($documents);
    if ($res == 200 || $res == 202) {
        print("${docsCount} documents queued successfully.\n");
    }
    else {
        die("Unexpected error.\n");
    }
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
    if (isset($data["themes"])) {
        echo "Document themes:", "\r\n";
        foreach ($data["themes"] as $theme) {
            echo "	", $theme["title"], " (sentiment: ", $theme["sentiment_score"], ")", "\r\n";
        }
    }

    // Printing of document entities
    if (isset($data["entities"])) {
        echo "Entities:", "\r\n";
        foreach ($data["entities"] as $entity) {
            echo "	", $entity["title"], " : ", $entity["entity_type"], " (sentiment: ", $entity["sentiment_score"], ")", "\r\n";
        }
    }

    print("\n");
}
