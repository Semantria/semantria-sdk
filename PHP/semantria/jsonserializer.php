<?php
require_once('common.php');

class JsonSerializer
{
    function getType()
    {
        return "json";
    }

    function serialize($obj, $wrapper = NULL)
    {
        $obj = processUpdateProxyClonedElement($obj);
        $str = json_encode(utf8Encode($obj));
        return $str;
    }

    function deserialize($str, $handler = NULL)
    {
        $obj = json_decode($str, TRUE);
        return $obj;
    }
}
