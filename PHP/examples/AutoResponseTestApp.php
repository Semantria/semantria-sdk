<?php

require_once('semantria/session.php');

echo "Semantria Auto-response feature demo.", "\r\n";

// The consumer key and secret
define('CONSUMER_KEY', getenv("SEMANTRIA_KEY"));
define('CONSUMER_SECRET', getenv("SEMANTRIA_SECRET"));

$results = array();


/**
 * Simple Semantria callback's handler
 */
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

    function onDocsAutoResponse($sender, $response)
    {
        global $results;

        foreach ($response as $item) {
            $results[] = $item;
        }
    }

    function onCollsAutoResponse($sender, $args)
    {
        //$s = json_encode($args);
        //echo "COLLS AUTORESPONSE: ", htmlspecialchars($s), "\r\n";
    }
}

print("Reading documents from file...\n");
$documents = array();
$file = fopen("source.txt", "r");
if (!$file) {
    print("File not found.");
    exit(1);
}

while (($line = fgets($file)) !== FALSE) {
    $documents[] = $line;
}
if (!feof($file)) {
    echo "Error: unexpected fgets() fail\n";
}
fclose($file);

$total_docs = count($documents);

// Initializes new session with the serializer object and the keys.
$session = new \Semantria\Session(CONSUMER_KEY, CONSUMER_SECRET, NULL, NULL, TRUE);

// Initialize session callback handler
$callback = new SessionCallbackHandler();
$session->setCallbackHandler($callback);

$configurations = $session->getConfigurations();
$autoresponse_config_id = NULL;

foreach ($configurations as $configuration) {
    if ($configuration['name'] == 'AutoResponseTestx') {
        $autoresponse_config_id = $configuration['id'];
    }
}

if ($autoresponse_config_id == NULL) {
    $configuration = $session->addConfigurations(array(array(
        'name' => 'AutoResponseTestx',
        'language' => 'English',
        'auto_response' => TRUE,
    )));
    print_r($configuration);
    $autoresponse_config_id = $configuration[0]['id'];
}

// Queues documents for analysis one by one. (Note: in a real application you
// would queue a batch of docs! One-by-one is inefficient.)
$doc_counter = 0;
foreach ($documents as $doc) {
    $session->queueDocument(array('id' => uniqid('', TRUE), 'text' => $doc), $autoresponse_config_id);
    $doc_counter += 1;
    // Note, this sleep is only needed for the demo. This allows time for some docs to
    // be processed before all docs are sent. Thus, some results will be returned as
    // autoresponse data.
    usleep(100000);
    $results_len = count($results);
    print("Documents queued: ${doc_counter}, received: ${results_len}\n");
}

// This final call is to get any remaining results from the server.
// Be sure to do this in your application before it shuts down.
sleep(1);
while (count($results) < count($documents)) {
    $result = $session->getProcessedDocuments();

    foreach ($result as $item) {
        $results[] = $item;
    }

    usleep(500000);
}

$results_len = count($results);
print("Totals:  queued: ${total_docs}, received: ${results_len}\n");

