<?php

/**
 * (Pulled out of xmlhandlers.php)
 *
 * @package SemantriaSdk
 * @author Scott Carpenter <scarpent@nerdery.com>
 * @version $Id$
 */
class Semantria_XmlHandler_Entity
{
    function __construct()
    {
        $this->current = "";
        $this->data = null;
        $this->entity = null;

        $this->name = null;
        $this->type = null;
    }

    function getData()
    {
        return $this->data;
    }

    # Call when an element starts
    function startElement($parser, $tag, $attributes)
    {
        $this->current = $tag;
        if ($tag == "entities") $this->data = array();
    }

    # Call when an elements ends
    function endElement($parser, $tag)
    {
        if ($tag == "entity")
        {
            $this->entity = array();
            $this->entity["name"] = $this->name;
            $this->entity["type"] = $this->type;
            array_push($this->data, $this->entity);
            $this->name = null;
            $this->type = null;
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
        } elseif ($this->current == "type") $this->type = $content;
    }
}
