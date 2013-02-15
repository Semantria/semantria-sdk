<?php

/**
 * (Pulled out of xmlhandlers.php)
 *
 * @package SemantriaSdk
 * @author Scott Carpenter <scarpent@nerdery.com>
 * @version $Id$
 */
class Semantria_XmlHandler_SentimentPhrase
{
    function __construct()
    {
        $this->current = "";
        $this->data = null;
        $this->phrase = null;

        $this->title = null;
        $this->weight = null;
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
        if ($tag == "phrases") $this->data = array();
        elseif ($tag == "phrase") $this->idx += 1;
    }

    # Call when an elements ends
    function endElement($parser, $tag)
    {
        if ($tag == "phrase" && $this->idx == 1)
        {
            $this->idx -= 1;
            $this->phrase = array();
            $this->phrase["title"] = $this->title;
            $this->phrase["weight"] = $this->weight;
            array_push($this->data, $this->phrase);
            $this->title = null;
            $this->weight = null;
        }
        elseif ($tag == "phrase" && $this->idx == 2) $this->idx -= 1;

        $this->current = "";
    }

    # Call when a character is read
    function characters($parser, $content)
    {
        if ($this->current == "title") {
            if (!isset($this->title)) {
                $this->title = $content;
            } else {
                $this->title .= $content;
            }
        } elseif ($this->current == "weight" && $this->weight == null) $this->weight = $content;
    }
}
