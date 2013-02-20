<?php

class Semantria_CallbackHandler_Terminal
    extends Semantria_CallbackHandler_Abstract
    implements Semantria_CallbackHandler_Interface
{
    public function onRequest($sender, $args)
    {
        $s = json_encode($args);
        //echo "\nREQUEST: " . $s;
    }

    public function onResponse($sender, $args)
    {
        $s = json_encode($args);
        //echo "\nRESPONSE: " . $s;
    }

    public function onError($sender, $args)
    {
        $s = json_encode($args);
        //echo "\nERROR: " . $s;
    }

    public function onDocsAutoResponse($sender, $args)
    {
        $s = json_encode($args);
        //echo "\nDOCS AUTORESPONSE: " . $s;
    }

    public function onCollsAutoResponse($sender, $args)
    {
        $s = json_encode($args);
        //echo "\nCOLLS AUTORESPONSE: " . $s;
    }
}
