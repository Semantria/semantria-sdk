<?php

class Semantria_CallbackHandler_Default
    extends Semantria_CallbackHandler_Abstract
    implements Semantria_CallbackHandler_Interface
{
    public function onRequest($sender, $args)
    {
        $s = json_encode($args);
        //echo "REQUEST: ", htmlspecialchars($s), "\r\n";
    }
    
    public function onResponse($sender, $args)
    {
        $s = json_encode($args);
        //echo "RESPONSE: ", htmlspecialchars($s), "\r\n";
    }
    
    public function onError($sender, $args)
    {
        $s = json_encode($args);
        //echo "ERROR: ", htmlspecialchars($s), "\r\n";
    }
    
    public function onDocsAutoResponse($sender, $args)
    {
        $s = json_encode($args);
        //echo "DOCS AUTORESPONSE: ", htmlspecialchars($s), "\r\n";
    }
    
    public function onCollsAutoResponse($sender, $args)
    {
        $s = json_encode($args);
        //echo "COLLS AUTORESPONSE: ", htmlspecialchars($s), "\r\n";
    }
}
