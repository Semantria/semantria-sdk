<?php

require_once 'common.php';

class Semantria_JsonSerializer
{
    public function getType()
    {
        return "json";
    }

    public function serialize($obj, $wrapper=null)
    {
        $obj = processUpdateProxyClonedElement($obj);
        $str = json_encode(utf8Encode($obj));
        return $str;
    }

    public function deserialize($str, $handler=null)
    {
        $obj = json_decode($str, true);
        return $obj;
    }
}
