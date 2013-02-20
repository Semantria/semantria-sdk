<?php

/**
 * (Pulled out of xmlhandlers.php)
 *
 * @package SemantriaSdk
 * @author Scott Carpenter <scarpent@nerdery.com>
 * @version $Id$
 */
class Semantria_XmlHandler_Query
{
    function __construct()
    {
        $this->current = "";
        $this->data = null;
        $this->query = null;

        $this->name = null;
        $this->content = null;
        $this->idx = 0;
    }

    function getData()
    {
        return $this->data;
    }

    # Call when an element starts
    function startElement($parser, $tag, $attributes)
    {
        $this->current = $tag;
        if ($tag == "queries") $this->data = array();
        elseif ($tag == "query") $this->idx += 1;
    }

    # Call when an elements ends
    function endElement($parser, $tag)
    {
        if ($tag == "query" && $this->idx == 1)
        {
            $this->idx -= 1;
            $this->query = array();
            $this->query["name"] = $this->name;
            $this->query["query"] = $this->content;
            array_push($this->data, $this->query);
            $this->name = null;
            $this->content = null;
        }
        elseif ($tag == "query" && $this->idx == 2) $this->idx -= 1;

        $this->current = "";
    }

    # Call when a character is read
    function characters($parser, $content)
    {
        if ($this->current == "name") {
            if (!isset($this->name)) {
                $this->name = $content;
            } else {
                $this->name .= $content;
            }
        } elseif ($this->current == "query") {
            if (!isset($this->content)) {
                $this->content = $content;
            } else {
                $this->content .= $content;
            }
        }
    }
}
