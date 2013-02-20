<?php

/**
 * (Pulled out of xmlhandlers.php)
 *
 * @package SemantriaSdk
 * @author Scott Carpenter <scarpent@nerdery.com>
 * @version $Id$
 */
class Semantria_XmlHandler_ProcessedCollection
    extends Semantria_XmlHandler_Collection
{
    function __construct()
    {
        $this->data = null;
        parent::__construct();
    }

    function getData()
    {
        return $this->data;
    }

    # Call when an element starts
    function startElement($parser, $tag, $attributes)
    {
        if ($tag == "collections")
            $this->data = array();
        else
            parent::startElement($parser, $tag, $attributes);
    }

    # Call when an elements ends
    function endElement($parser, $tag)
    {
        parent::endElement($parser, $tag);
        if ($tag == "collection")
        {
            array_push($this->data, $this->collection);
            $this->collection = null;
        }
    }
}
