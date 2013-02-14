<?php

class Semantria_XmlHandler_Subscription
{
    function __construct()
    {
        $this->current = "";
        $this->data = null;

        $this->name = null;
        $this->priority = null;
        $this->status = null;
        $this->expiration_date = null;
        $this->calls_balance = 0;
        $this->calls_limit = 0;
        $this->calls_limit_interval = 0;
        $this->docs_balance = 0;
        $this->docs_limit = 0;
        $this->docs_limit_interval = 0;
        $this->configurations_limit = 0;
        $this->blacklist_limit = 0;
        $this->categories_limit = 0;
        $this->queries_limit = 0;
        $this->entities_limit = 0;
        $this->sentiment_limit = 0;
        $this->characters_limit = 0;
        $this->batch_limit = 0;
        $this->collection_limit = 0;
        $this->auto_response_limit = 0;
        $this->processed_batch_limit = 0;
        $this->callback_batch_limit = 0;
        $this->limit_type = null;
    }

    function getData()
    {
        return $this->data;
    }

    # Call when an element starts
    function startElement($parser, $tag, $attributes)
    {
        $this->current = $tag;
        if ($tag == "subscription") {
            $this->data = array();
        }
    }

    # Call when an elements ends
    function endElement($parser, $tag)
    {
        if ($tag == "subscription")
        {
            $this->data["name"] = $this->name;
            $this->data["status"] = $this->status;
            $this->data["priority"] = $this->priority;
            $this->data["expiration_date"] = $this->expiration_date;
            $this->data["calls_balance"] = $this->calls_balance;
            $this->data["calls_limit"] = $this->calls_limit;
            $this->data["calls_limit_interval"] = $this->calls_limit_interval;
            $this->data["docs_balance"] = $this->docs_balance ;
            $this->data["docs_limit"] = $this->docs_limit;
            $this->data["docs_limit_interval"] = $this->docs_limit_interval;
            $this->data["configurations_limit"] = $this->configurations_limit;
            $this->data["blacklist_limit"] = $this->blacklist_limit;
            $this->data["categories_limit"] = $this->categories_limit;
            $this->data["queries_limit"] = $this->queries_limit;
            $this->data["entities_limit"] = $this->entities_limit;
            $this->data["sentiment_limit"] = $this->sentiment_limit;
            $this->data["characters_limit"] = $this->characters_limit;
            $this->data["batch_limit"] = $this->batch_limit;
            $this->data["collection_limit"] = $this->collection_limit;
            $this->data["auto_response_limit"] = $this->auto_response_limit;
            $this->data["processed_batch_limit"] = $this->processed_batch_limit;
            $this->data["callback_batch_limit"] = $this->callback_batch_limit;
            $this->data["limit_type"] = $this->limit_type;
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
        } elseif ($this->current == "status") $this->status = $content;
        elseif ($this->current == "priority") $this->priority = $content;
        elseif ($this->current == "expiration_date") $this->expiration_date = $content;
        elseif ($this->current == "calls_balance") $this->calls_balance = (int) $content;
        elseif ($this->current == "calls_limit") $this->calls_limit = (int) $content;
        elseif ($this->current == "calls_limit_interval") $this->calls_limit_interval = (int) $content;
        elseif ($this->current == "docs_balance") $this->docs_balance = (int) $content;
        elseif ($this->current == "docs_limit") $this->docs_limit = (int) $content;
        elseif ($this->current == "docs_limit_interval") $this->docs_limit_interval = (int) $content;
        elseif ($this->current == "configurations_limit") $this->configurations_limit = (int) $content;
        elseif ($this->current == "blacklist_limit") $this->blacklist_limit = (int) $content;
        elseif ($this->current == "categories_limit") $this->categories_limit = (int) $content;
        elseif ($this->current == "queries_limit") $this->queries_limit = (int) $content;
        elseif ($this->current == "entities_limit") $this->entities_limit = (int) $content;
        elseif ($this->current == "sentiment_limit") $this->sentiment_limit = (int) $content;
        elseif ($this->current == "characters_limit") $this->characters_limit = (int) $content;
        elseif ($this->current == "batch_limit") $this->batch_limit = (int) $content;
        elseif ($this->current == "collection_limit") $this->collection_limit = (int) $content;
        elseif ($this->current == "auto_response_limit") $this->auto_response_limit = (int) $content;
        elseif ($this->current == "processed_batch_limit") $this->processed_batch_limit = (int) $content;
        elseif ($this->current == "callback_batch_limit") $this->callback_batch_limit = (int) $content;
        elseif ($this->current == "limit_type") $this->limit_type = $content;
    }
}
