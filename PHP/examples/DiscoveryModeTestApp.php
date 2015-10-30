<?php
require_once('../semantria/session.php');

print("Semantria Collection processing mode demo.\n");

// the consumer key and secret
define('CONSUMER_KEY', "__key_george");
define('CONSUMER_SECRET', "__secret_george");

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

print("Reading collection from file...\n");
$documents = array();
$file = fopen("source.txt", "r");
if (!$file) {
    print("");
    exit(1);
}

while (($line = fgets($file)) !== FALSE) {
    $documents[] = $line;
}
if (!feof($file)) {
    echo "Error: unexpected fgets() fail\n";
}
fclose($file);

// Initializes new session with the serializer object and the keys.
$session = new \Semantria\Session(CONSUMER_KEY, CONSUMER_SECRET, NULL, NULL, TRUE);

// Initialize session callback handler
$callback = new SessionCallbackHandler();
$session->setCallbackHandler($callback);

// Queues collection for analysis using default configuration
$collection_id = uniqid('', true);
$status = $session->queueCollection(array('id' => $collection_id, 'documents' => $documents));

if ($status != 202 && $status != 200) {
    print("Error\n");
    exit(1);
}

print("{$collection_id} collection queued successfully.\n");

// Retreives analysis results for queued collection
$results = NULL;
do {
    sleep(1);
    print("Retrieving your processed results...\n");
    $results = $session->getCollection($collection_id);
} while ($results['status'] == 'QUEUED');

if ($results['status'] != 'PROCESSED') {
    print("Error");
    exit(1);
}

// Prints analysis results
print("\n");
foreach ($results['facets'] as $facet) {
    print("{$facet['label']} : {$facet['count']}\n");
    if (isset($facet['attributes'])) {
        foreach ($facet['attributes'] as $attribute) {
            print("\t{$attribute['label']} : {$attribute['count']}\n");
        }
    }
}
