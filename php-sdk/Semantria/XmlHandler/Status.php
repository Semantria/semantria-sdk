<?php

class Semantria_XmlHandler_Status
{
    function __construct()
    {
        $this->current = "";
        $this->data = null;
        
        $this->service_status = null;
        $this->api_version = null;
        $this->service_version = null;
        $this->supported_encoding = null;
        $this->supported_compression = null;
        $this->supported_languages = null;
        $this->language = null;
    }
 
    function getData()
    {
        return $this->data;
    }
   
    # Call when an element starts
    function startElement($parser, $tag, $attributes)
    {
        $this->current = $tag;
        if ($tag == "status") {
            $this->data = array();
        }
        elseif ($tag == "supported_languages") {
            $this->supported_languages = array();
        }
    }

    # Call when an elements ends
    function endElement($parser, $tag)
    {
        if ($tag == "status") {
            $this->data["service_status"] = $this->service_status;
            $this->data["api_version"] = $this->api_version;
            $this->data["service_version"] = $this->service_version;
            $this->data["supported_encoding"] = $this->supported_encoding;
            $this->data["supported_compression"] = $this->supported_compression;
        }
        elseif ($tag == "supported_languages") {
            $this->data["supported_languages"] = $this->supported_languages;
        }
        elseif ($tag == "language") {
            array_push($this->supported_languages, $this->language);
        }

        $this->current = "";
    }
    
    # Call when a character is read
    function characters($parser, $content)
    {
        if ($this->current == "service_status") $this->service_status = $content;
        elseif ($this->current == "api_version") $this->api_version = $content;
        elseif ($this->current == "service_version") $this->service_version = $content;
        elseif ($this->current == "supported_encoding") $this->supported_encoding = $content;
        elseif ($this->current == "supported_compression") $this->supported_compression = $content;
        elseif ($this->current == "language") $this->language = $content;
    }
}

