<?php

class Semantria_XmlHandler_Blacklist
{
    function __construct()
    {
        $this->current = "";
        $this->data = null;
        $this->item = "";
    }

    function getData()
    {
        return $this->data;
    }

    # Call when an element starts
    function startElement($parser, $tag, $attributes)
    {
        $this->current = $tag;
        if ($tag == "blacklist")
        {
            $this->data = array();
        }
    }

    # Call when an elements ends
    function endElement($parser, $tag)
    {
        if ($tag == "item") {
            array_push($this->data, $this->item);
        }

        $this->current = "";
    }

    # Call when a character is read
    function characters($parser, $content)
    {
        if ($this->current == "item") {
            $this->item = $content;
        }
    }
}
