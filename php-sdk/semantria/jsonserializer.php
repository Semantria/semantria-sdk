<?php

require_once('common.php');

class JsonSerializer  
{
    function getType()
    { 
        return "json";
    }

    function serialize($obj, $wrapper=null)
    {
		$obj = processUpdateProxyClonedElement($obj);
        $str = json_encode(utf8Encode($obj));
        return $str;
    }

    function deserialize($str, $handler=null)
    {
        $obj = json_decode($str, true);
        return $obj;
    }
}

?>