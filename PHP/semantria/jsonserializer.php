<?php
namespace Semantria;

class JsonSerializer
{
    function getType()
    {
        return "json";
    }

    function serialize($obj)
    {
        return json_encode($obj);
    }

    function deserialize($str)
    {
        return json_decode($str, TRUE);
    }

}
