<?php

/**
 * (Pulled out of xmlhandlers.php)
 *
 * @package SemantriaSdk
 * @author Scott Carpenter <scarpent@nerdery.com>
 * @version $Id$
 */
class Semantria_XmlHandler_Collection
{
    function __construct()
    {
        $this->current = "";
        $this->hierarchy = array();
        $this->parent = "";
        $this->collection = null;
        $this->facets = null;
        $this->facet = null;
        $this->attributes = null;
        $this->attribute = null;
        $this->entities = null;
        $this->entity = null;
        $this->themes = null;
        $this->theme = null;
        $this->topics = null;
        $this->topic = null;

        $this->id = null;
        $this->config_id = null;
        $this->status = null;
        $this->label = null;
        $this->count = null;
        $this->negative_count = null;
        $this->positive_count = null;
        $this->neutral_count = null;
        $this->title = null;
        $this->phrases_count = null;
        $this->themes_count = null;
        $this->sentiment_score = null;
        $this->type = null;
        $this->entity_type = null;
        $this->hitcount = null;
    }

    function getData()
    {
        return $this->collection;
    }

    # Call when an element starts
    function startElement($parser, $tag, $attributes)
    {
        $this->current = $tag;

        if ($tag == "collection" or $tag == "facet" or $tag == "attribute"
            or $tag == "entity" or $tag == "theme" or $tag == "topic")
        {
            $this->parent = $tag;
            array_push($this->hierarchy, $tag);
        }

        if ($tag == "collection") $this->collection = array();
        elseif ($tag == "facets") $this->facets = array();
        elseif ($tag == "facet") $this->facet = array();
        elseif ($tag == "attributes")    $this->attributes = array();
        elseif ($tag == "attribute") $this->attribute = array();
        elseif ($tag == "entities")    $this->entities = array();
        elseif ($tag == "entity") $this->entity = array();
        elseif ($tag == "themes")    $this->themes = array();
        elseif ($tag == "theme") $this->theme = array();
        elseif ($tag == "topics")    $this->topics = array();
        elseif ($tag == "topic") $this->topic = array();
    }

    # Call when an elements ends
    function endElement($parser, $tag)
    {
        if ($tag == "collection" or $tag == "facet" or $tag == "attribute"
            or $tag == "entity" or $tag == "theme" or $tag == "topic")
        {
            $this->parent = array_pop($this->hierarchy);
            if (sizeof($this->hierarchy) > 0)
            {
                $this->parent = $this->hierarchy[sizeof($this->hierarchy) - 1];
            }
        }

        if ($tag == "facets")
        {
            $this->collection["facets"] = $this->facets;
        }
        elseif ($tag == "attributes")
        {
            $this->collection["attributes"] = $this->attributes;
        }
        elseif ($tag == "entities")
        {
            $this->collection["entities"] = $this->entities;
        }
        elseif ($tag == "themes")
        {
            $this->collection["themes"] = $this->themes;
        }
        elseif ($tag == "topics")
        {
            $this->collection["topics"] = $this->topics;
        }
        elseif ($tag == "facet")
        {
            $this->facet["attributes"] = $this->attributes;
            array_push($this->facets, $this->facet);
            $this->facet = null;
            $this->attributes  = null;
        }
        elseif ($tag == "attribute")
        {
            if ($this->parent == "facet")
            {
                array_push($this->attributes, $this->attribute);
            }
            $this->attribute = null;
        }
        elseif ($tag == "entity")
        {
            $this->collection["entities"] = $this->entities;
            array_push($this->entities, $this->entity);
            $this->entity = null;
        }
        elseif ($tag == "theme")
        {
            $this->collection["themes"] = $this->themes;
            array_push($this->themes, $this->theme);
            $this->theme = null;
        }
        elseif ($tag == "topic")
        {
            $this->collection["topics"] = $this->topics;
            array_push($this->topics, $this->topic);
            $this->topic = null;
        }
        elseif ($tag == "collection")
        {
            $this->collection["facets"] = $this->facets;
            $this->collection["entities"] = $this->entities;
            $this->collection["themes"] = $this->themes;
            $this->collection["topics"] = $this->topics;
            $this->facets = null;
            $this->entities = null;
            $this->themes = null;
            $this->topics = null;
        }

        $this->current = "";
    }

    # Call when a character is read
    function characters($parser, $content)
    {
        if ($this->current == "id") $this->id = $content;
        elseif ($this->current == "config_id") $this->config_id = $content;
        elseif ($this->current == "status") $this->status = $content;
        elseif ($this->current == "label") $this->label = $content;
        elseif ($this->current == "count") $this->count = (int) $content;
        elseif ($this->current == "negative_count") $this->negative_count = (int) $content;
        elseif ($this->current == "positive_count") $this->positive_count = (int) $content;
        elseif ($this->current == "neutral_count") $this->neutral_count = (int) $content;
        elseif ($this->current == "title") $this->title = $content;
        elseif ($this->current == "phrases_count") $this->phrases_count = (int)$content;
        elseif ($this->current == "themes_count") $this->themes_count = (int)$content;
        elseif ($this->current == "sentiment_score") $this->sentiment_score = (float) $content;
        elseif ($this->current == "type") $this->type = $content;
        elseif ($this->current == "entity_type") $this->entity_type = $content;
        elseif ($this->current == "hitcount") $this->hitcount = (int) $content;

        if ($this->parent == "collection")
        {
            if (isset($this->id))
            {
                $this->collection["id"] = $this->id;
                $this->id = null;
            }
            if (isset($this->config_id))
            {
                $this->collection["config_id"] = $this->config_id;
                $this->config_id = null;
            }
            if (isset($this->status))
            {
                $this->collection["status"] = $this->status;
                $this->status = null;
            }
        }
        elseif ($this->parent == "facet")
        {
            if (isset($this->label))
            {
                if (!isset($this->facet["label"])) {
                    $this->facet["label"] = $this->label;
                } else {
                    $this->facet["label"] .= $this->label;
                }
                $this->label = null;
            }
            if (isset($this->count))
            {
                $this->facet["count"] = $this->count;
                $this->count = null;
            }
            if (isset($this->negative_count))
            {
                $this->facet["negative_count"] = $this->negative_count;
                $this->negative_count = null;
            }
            if (isset($this->positive_count))
            {
                $this->facet["positive_count"] = $this->positive_count;
                $this->positive_count = null;
            }
            if (isset($this->neutral_count))
            {
                $this->facet["neutral_count"] = $this->neutral_count;
                $this->neutral_count = null;
            }
        }
        elseif ($this->parent == "attribute")
        {
            if (isset($this->label))
            {
                if (!isset($this->attribute["label"])) {
                    $this->attribute["label"] = $this->label;
                } else {
                    $this->attribute["label"] .= $this->label;
                }
                $this->label = null;
            }
            if (isset($this->count))
            {
                $this->attribute["count"] = $this->count;
                $this->count = null;
            }
        }
        elseif ($this->parent == "entity")
        {
            if (isset($this->title))
            {
                if (!isset($this->entity["title"])) {
                    $this->entity["title"] = $this->title;
                } else {
                    $this->entity["title"] .= $this->title;
                }
                $this->title = null;
            }
            if (isset($this->type))
            {
                $this->entity["type"] = $this->type;
                $this->type = null;
            }
            if (isset($this->entity_type))
            {
                $this->entity["entity_type"] = $this->entity_type;
                $this->entity_type = null;
            }
            if (isset($this->count))
            {
                $this->entity["count"] = $this->count;
                $this->count = null;
            }
            if (isset($this->negative_count))
            {
                $this->entity["negative_count"] = $this->negative_count;
                $this->negative_count = null;
            }
            if (isset($this->neutral_count))
            {
                $this->entity["neutral_count"] = $this->neutral_count;
                $this->neutral_count = null;
            }
            if (isset($this->positive_count))
            {
                $this->entity["positive_count"] = $this->positive_count;
                $this->positive_count = null;
            }
        }
        elseif ($this->parent == "topic")
        {
            if (isset($this->title))
            {
                if (!isset($this->topic["title"])) {
                    $this->topic["title"] = $this->title;
                } else {
                    $this->topic["title"] .= $this->title;
                }
                $this->title = null;
            }
            if (isset($this->type))
            {
                $this->topic["type"] = $this->type;
                $this->type = null;
            }
            if (isset($this->hitcount))
            {
                $this->topic["hitcount"] = $this->hitcount;
                $this->hitcount = null;
            }
            if (isset($this->sentiment_score))
            {
                $this->topic["sentiment_score"] = $this->sentiment_score;
                $this->sentiment_score = null;
            }
        }
        elseif ($this->parent == "theme")
        {
            if (isset($this->title))
            {
                if (!isset($this->theme["title"])) {
                    $this->theme["title"] = $this->title;
                } else {
                    $this->theme["title"] .= $this->title;
                }
                $this->title = null;
            }
            if (isset($this->phrases_count))
            {
                $this->theme["phrases_count"] = $this->phrases_count;
                $this->phrases_count = null;
            }
            if (isset($this->themes_count))
            {
                $this->theme["themes_count"] = $this->themes_count;
                $this->themes_count = null;
            }
            if (isset($this->sentiment_score))
            {
                $this->theme["sentiment_score"] = $this->sentiment_score;
                $this->sentiment_score = null;
            }
        }
    }
}
