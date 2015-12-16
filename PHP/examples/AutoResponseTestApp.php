<?php

require_once('../semantria/session.php');

echo "Semantria Auto-response feature demo.", "\r\n";

// the consumer key and secret
define('CONSUMER_KEY', "");
define('CONSUMER_SECRET', "");

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

print("Reading collection from file...\n");
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
$primary_config = NULL;
$autoresponse_config = NULL;

foreach ($configurations as $configuration) {
    if ($configuration['is_primary']) {
        $primary_config = $configuration;
    } 
    else if ($configuration['name'] == 'AutoResponseTest') {
        $autoresponse_config = $configuration;
    }
}

if ($autoresponse_config == NULL) {
    $session->addConfigurations(array(array(
        'name' => 'AutoResponseTest',
        'language' => 'English',
        'is_primary' => TRUE,
        'auto_response' => TRUE,
    )));
}
else {
    $autoresponse_config['is_primary'] = TRUE;
    $session->updateConfigurations(array($autoresponse_config));
}

// Queues documents for analysis one by one
$doc_counter = 0;
foreach ($documents as $doc) {
    $session->queueDocument(array('id' => uniqid('', TRUE), 'text' => $doc));
    $doc_counter += 1;
    usleep(100000);

    $results_len = count($results);
    print("Documents queued/received rate: ${doc_counter}/${results_len}\n");
}

// The final call to get remained data from server, Just for demo purposes.
sleep(1);
while (count($results) < count($documents)) {
    $result = $session->getProcessedDocuments();

    foreach ($result as $item) {
        $results[] = $item;
    }

    usleep(500000);
}

$results_len = count($results);
print("Documents queued/received rate: ${total_docs}/${results_len}\n");

// Sets original primary configuration back after the test.
$session->updateConfigurations(array($primary_config));
