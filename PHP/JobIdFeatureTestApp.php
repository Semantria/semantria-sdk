<?php

require_once('semantria/session.php');

// the consumer key and secret
define('CONSUMER_KEY', "");
define('CONSUMER_SECRET', "");

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

print("Semantria JobId feature demo.\n");

# 0 - send every single document separately
# 1 - send uniqueJobIdCount batches
# 2 - send all documents in single batch
$data_sending_mode = 2;
$unique_jobid_count = 4;
$job_ids = array();
$job_documents = array();
$jobs = array();

// Generates N unique jobId values
for ($i=0; $i < $unique_jobid_count; $i++) { 
    $job_id = uniqid('', TRUE);
    $job_ids[] = $job_id;
    $jobs[$job_id] = 0;
    $job_documents[$job_id] = array();
}

print("Reading collection from file...\n");
$file = fopen("source.txt", "r");
if (!$file) {
    print("File not found\n");
    exit(1);
}

while (($line = fgets($file)) !== FALSE) {
    $job_id = $job_ids[rand(0, $unique_jobid_count-1)];

    $jobs[$job_id] += 1;
    $job_documents[$job_id][] = array(
        'id' => uniqid('', TRUE),
        'text' => $line,
        'job_id' => $job_id,
    );
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

switch ($data_sending_mode) {
    case 0:
        foreach ($job_documents as $job_id => $documents) {
            foreach ($documents as $document) {
                $session->queueDocument($document);
            }
            $length = count($document);
            print("${length} documents queued for ${job_id} job ID\n");
        }
        break;
    
    case 1:
        foreach ($job_documents as $job_id => $documents) {
            $session->queueBatch($documents);
            $length = count($documents);
            print("${length} documents queued for ${job_id} job ID\n");
        }
        break;

    default:
        $full_batch = array();

        foreach ($job_documents as $job_id => $documents) {
            $full_batch = array_merge($full_batch, $documents);
        }

        $session->queueBatch($full_batch);

        $length = count($full_batch);
        print("${length} documents queued in single batch\n");
        break;
}

print("\n");

foreach ($jobs as $job_id => $documents_counter) {
    $counter = 0;

    while ($jobs[$job_id] > 0) {
        usleep(500000);

        $res = $session->getProcessedDocumentsByJobId($job_id);
        $jobs[$job_id] -= count($res);
        $counter += count($res);
    }

    print("${counter} documents received for ${job_id} Job ID.\n");
}

print("\n");
print("Done!\n");
