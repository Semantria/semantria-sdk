<?php

/**
 * (Pulled out of xmlhandlers.php)
 *
 * @package SemantriaSdk
 * @author Scott Carpenter <scarpent@nerdery.com>
 * @version $Id$
 */
class Semantria_XmlHandler_Category
{
    function __construct()
    {
        $this->current = "";
        $this->data = null;
        $this->category = null;
        $this->samples = null;

        $this->name = null;
        $this->weight = null;
        $this->sample = null;
    }

    function getData()
    {
        return $this->data;
    }

    # Call when an element starts
    function startElement($parser, $tag, $attributes)
    {
        $this->current = $tag;

        if ($tag == "categories") $this->data = array();
        elseif ($tag == "category") $this->category = array();
        elseif ($tag == "samples") $this->samples = array();
    }

    # Call when an elements ends
    function endElement($parser, $tag)
    {
        if ($tag == "category")
        {
            $this->category["name"] = $this->name;
            $this->category["weight"] = $this->weight;
            array_push($this->data, $this->category);
            $this->name = null;
            $this->weight = null;
        }
        elseif ($tag == "samples")
        {
            $this->category["samples"] = $this->samples;
            $this->samples = null;
        }
        elseif ($tag == "sample")
        {
            array_push($this->samples, $this->sample);
            $this->sample = null;
        }

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
        } elseif ($this->current == "weight") $this->weight = (float) $content;
        elseif ($this->current == "sample") {
            if (!isset($this->sample)) {
                $this->sample = $content;
            } else {
                $this->sample .= $content;
            }
        }
    }
}
