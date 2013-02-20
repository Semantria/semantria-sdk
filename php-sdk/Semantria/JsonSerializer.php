<?php

class Semantria_JsonSerializer
{
    public function getType()
    {
        return "json";
    }

    public function serialize($obj, $wrapper=null)
    {
        $obj = Semantria_Common::processUpdateProxyClonedElement($obj);
        $str = json_encode(Semantria_Common::utf8Encode($obj));
        return $str;
    }

    public function deserialize($str, $handler=null)
    {
        $obj = json_decode($str, true);
        return $obj;
    }
}
