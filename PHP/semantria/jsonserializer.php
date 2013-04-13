<?php
namespace Semantria;

class JsonSerializer
{
    function getType()
    {
        return "json";
    }

    function serialize($obj, $wrapper = NULL)
    {
        //$obj = processUpdateProxyClonedElement($obj);
        $str = json_encode($obj);
        return $str;
    }

    function deserialize($str, $handler = NULL)
    {
        $obj = json_decode($str, TRUE);
        return $obj;
    }
}
