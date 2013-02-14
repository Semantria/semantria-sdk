<?php

class Semantria_XmlHandler_Configuration
{
    function __construct()
    {
        $this->current = "";
        $this->hierarchy = array();
        $this->parent = "";
        $this->data = null;
        $this->item = null;
        $this->document = null;
        $this->collection = null;

        $this->config_id = "";
        $this->name = "";
        $this->is_primary = false;
        $this->one_sentence = false;
        $this->auto_responding = false;
        $this->language = "";
        $this->chars_threshold = 0;
        $this->callback = "";

        $this->entity_themes_limit = 0;
        $this->phrases_limit = 0;
        $this->summary_limit = 0;
        $this->user_entities_limit = 0;

        $this->facets_limit = 0;
        $this->facet_atts_limit = 0;

        $this->themes_limit = 0;
        $this->named_entities_limit = 0;
        $this->concept_topics_limit = 0;
        $this->query_topics_limit = 0;
    }

    function getData()
    {
        return $this->data;
    }

    # Call when an element starts
    function startElement($parser, $tag, $attributes)
    {
        $this->current = $tag;

        if ($tag == "document" or $tag == "collection")
        {
            $this->parent = $tag;
            array_push($this->hierarchy, $tag);
        }

        if ($tag == "configurations") {
            $this->data = array();
        }
        elseif ($tag == "document")    $this->document = array();
        elseif ($tag == "collection")    $this->collection = array();
    }

    # Call when an elements ends
    function endElement($parser, $tag)
    {
        if ($tag == "document" or $tag == "collection")
        {
            $this->parent = array_pop($this->hierarchy);
            if (sizeof($this->hierarchy) > 0)
            {
                $this->parent = $this->hierarchy[sizeof($this->hierarchy) - 1];
            }
        }

        if ($tag == "configuration")
        {
            $this->item = array();
            $this->item["config_id"] = $this->config_id;
            $this->item["name"] = $this->name;
            $this->item["one_sentence"] = $this->one_sentence;
            $this->item["is_primary"] = $this->is_primary;
            $this->item["auto_responding"] = $this->auto_responding;
            $this->item["language"] = $this->language;
            $this->item["chars_threshold"] = $this->chars_threshold;
            $this->item["callback"] = $this->callback;
            $this->item["document"] = $this->document;
            $this->item["collection"] = $this->collection;
            array_push($this->data, $this->item);
            $this->config_id = "";
            $this->name = "";
            $this->one_sentence = false;
            $this->is_primary = false;
            $this->auto_responding = false;
            $this->language = "";
            $this->chars_threshold = 0;
            $this->callback = "";
            $this->document = null;
            $this->collection = null;
        }
        elseif ($tag == "document")
        {
            $this->document["concept_topics_limit"] = $this->concept_topics_limit;
            $this->document["query_topics_limit"] = $this->query_topics_limit;
            $this->document["named_entities_limit"] = $this->named_entities_limit;
            $this->document["user_entities_limit"] = $this->user_entities_limit;
            $this->document["themes_limit"] = $this->themes_limit;
            $this->document["entity_themes_limit"] = $this->entity_themes_limit;
            $this->document["phrases_limit"] = $this->phrases_limit;
            $this->document["summary_limit"] = $this->summary_limit;
            $this->concept_topics_limit = 0;
            $this->query_topics_limit = 0;
            $this->named_entities_limit = 0;
            $this->user_entities_limit = 0;
            $this->themes_limit = 0;
            $this->entity_themes_limit = 0;
            $this->phrases_limit = 0;
            $this->summary_limit = 0;
        }
        elseif ($tag == "collection")
        {
            $this->collection["facets_limit"] = $this->facets_limit;
            $this->collection["facet_atts_limit"] = $this->facet_atts_limit;
            $this->collection["themes_limit"] = $this->themes_limit;
            $this->collection["concept_topics_limit"] = $this->concept_topics_limit;
            $this->collection["query_topics_limit"] = $this->query_topics_limit;
            $this->collection["named_entities_limit"] = $this->named_entities_limit;
            $this->facets_limit = 0;
            $this->facet_atts_limit = 0;
            $this->concept_topics_limit = 0;
            $this->query_topics_limit = 0;
            $this->named_entities_limit = 0;
            $this->themes_limit = 0;
        }

        $this->current = "";
    }

    # Call when a character is read
    function characters($parser, $content)
    {
        if ($this->current == "config_id") $this->config_id = $content;
        elseif ($this->current == "name") {
            if (!isset($this->name)) {
                $this->name = $content;
            } else {
                $this->name .= $content;
            }
            $this->name = $content;
        } elseif ($this->current == "is_primary") $this->is_primary = str2bool($content);
        elseif ($this->current == "one_sentence") $this->one_sentence = str2bool($content);
        elseif ($this->current == "auto_responding") $this->auto_responding = str2bool($content);
        elseif ($this->current == "language") $this->language = $content;
        elseif ($this->current == "chars_threshold") $this->chars_threshold = (int) $content;
        elseif ($this->current == "callback") $this->callback = $content;
        elseif ($this->current == "phrases_limit") $this->phrases_limit = (int) $content;
        elseif ($this->current == "themes_limit") $this->themes_limit = (int) $content;
        elseif ($this->current == "named_entities_limit") $this->named_entities_limit = (int) $content;
        elseif ($this->current == "user_entities_limit") $this->user_entities_limit = (int) $content;
        elseif ($this->current == "entity_themes_limit") $this->entity_themes_limit = (int) $content;
        elseif ($this->current == "concept_topics_limit") $this->concept_topics_limit = (int) $content;
        elseif ($this->current == "query_topics_limit") $this->query_topics_limit = (int) $content;
        elseif ($this->current == "summary_limit") $this->summary_limit = (int) $content;
        elseif ($this->current == "facet_atts_limit") $this->facet_atts_limit = (int) $content;
        elseif ($this->current == "facets_limit") $this->facets_limit = (int) $content;
    }
}
