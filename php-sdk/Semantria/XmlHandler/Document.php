<?php

/**
 * (Pulled out of xmlhandlers.php)
 *
 * @package SemantriaSdk
 * @author Scott Carpenter <scarpent@nerdery.com>
 * @version $Id$
 */
class Semantria_XmlHandler_Document
{
    function __construct()
    {
        $this->current = "";
        $this->hierarchy = array();
        $this->parent = "";
        $this->document = null;
        $this->entity = null;
        $this->theme = null;
        $this->topic = null;
        $this->entities = null;
        $this->entity_themes = null;
        $this->doc_themes = null;
        $this->topics = null;
        $this->phrases = null;
        $this->phrase = null;

        $this->id = null;
        $this->config_id = null;
        $this->status = null;
        $this->sentiment_score = null;
        $this->summary = null;

        $this->evidence = null;
        $this->is_about = null;
        $this->is_negated = null;
        $this->confident = null;
        $this->strength_score = null;
        $this->type = null;
        $this->title = null;
        $this->hitcount = null;
        $this->entity_type = null;
        $this->negating_phrase = null;
    }

    function getData()
    {
        return $this->document;
    }

    # Call when an element starts
    function startElement($parser, $tag, $attributes)
    {
        $this->current = $tag;

        if ($tag == "document" or $tag == "entity" or $tag == "theme" or $tag == "topic"
            or $tag == "phrase")
        {
            $this->parent = $tag;
            array_push($this->hierarchy, $tag);
        }

        if ($tag == "document") $this->document = array();
        elseif ($tag == "entities") $this->entities = array();
        elseif ($tag == "entity") $this->entity = array();
        elseif ($tag == "themes" && $this->parent == "entity") $this->entity_themes = array();
        elseif ($tag == "themes" && $this->parent == "document") $this->doc_themes = array();
        elseif ($tag == "theme") $this->theme = array();
        elseif ($tag == "topics") $this->topics = array();
        elseif ($tag == "topic") $this->topic = array();
        elseif ($tag == "phrases") $this->phrases = array();
        elseif ($tag == "phrase") $this->phrase = array();
    }

    # Call when an elements ends
    function endElement($parser, $tag)
    {
        if ($tag == "document" or $tag == "entity" or $tag == "theme" or $tag == "topic"
            or $tag == "phrase")
        {
            $this->parent = array_pop($this->hierarchy);
            if (sizeof($this->hierarchy) > 0)
            {
                $this->parent = $this->hierarchy[sizeof($this->hierarchy) - 1];
            }
        }

        if ($tag == "entities")
        {
            $this->document["entities"] = $this->entities;
        }
        elseif ($tag == "topics")
        {
            $this->document["topics"] = $this->topics;
        }
        elseif ($tag == "themes" && $this->parent == "entity")
        {
            $this->entity["themes"] = $this->entity_themes;
        }
        elseif ($tag == "themes"  && $this->parent == "document")
        {
            $this->document["themes"] = $this->doc_themes;
        }
        elseif ($tag == "phrases")
        {
            $this->document["phrases"] = $this->phrases;
        }
        elseif ($tag == "document")
        {
            $this->document["entities"] = $this->entities;
            $this->document["topics"] = $this->topics;
            $this->document["phrases"] = $this->phrases;
            $this->document["themes"] = $this->doc_themes;
            $this->entities = null;
            $this->topics = null;
            $this->phrases = null;
            $this->doc_themes = null;
        }
        elseif ($tag == "entity")
        {
            $this->entity["themes"] = $this->entity_themes;
            array_push($this->entities, $this->entity);
            $this->entity = null;
            $this->entity_themes  = null;
        }
        elseif ($tag == "theme")
        {
            if ($this->parent == "entity")
            {
                array_push($this->entity_themes, $this->theme);
            }
            elseif ($this->parent == "document")
            {
                array_push($this->doc_themes, $this->theme);
            }
            $this->theme = null;
        }
        elseif ($tag == "topic")
        {
            array_push($this->topics, $this->topic);
            $this->topic = null;
        }
        elseif ($tag == "phrase")
        {
            array_push($this->phrases, $this->phrase);
            $this->phrase = null;
        }

        $this->current = "";
    }

    # Call when a character is read
    function characters($parser, $content)
    {
        if ($this->current == "id") $this->id = $content;
        elseif ($this->current == "config_id") $this->config_id = $content;
        elseif ($this->current == "status") $this->status = $content;
        elseif ($this->current == "sentiment_score") $this->sentiment_score = (float) $content;
        elseif ($this->current == "evidence") $this->evidence = (float) $content;
        elseif ($this->current == "strength_score") $this->strength_score = (float) $content;
        elseif ($this->current == "type") $this->type = $content;
        elseif ($this->current == "title") $this->title = $content;
        elseif ($this->current == "hitcount") $this->hitcount = (int) $content;
        elseif ($this->current == "summary") $this->summary = $content;
        elseif ($this->current == "is_about") $this->is_about = Semantria_Common::str2bool($content);
        elseif ($this->current == "confident") $this->confident = Semantria_Common::str2bool($content);
        elseif ($this->current == "entity_type") $this->entity_type = $content;
        elseif ($this->current == "negating_phrase") $this->negating_phrase = $content;
        elseif ($this->current == "is_negated") $this->is_negated = Semantria_Common::str2bool($content);

        if ($this->parent == "document")
        {
            if (isset($this->id))
            {
                $this->document["id"] = $this->id;
                $this->id = null;
            }
            if (isset($this->config_id))
            {
                $this->document["config_id"] = $this->config_id;
                $this->config_id = null;
            }
            if (isset($this->status))
            {
                $this->document["status"] = $this->status;
                $this->status = null;
            }
            if (isset($this->sentiment_score))
            {
                $this->document["sentiment_score"] = $this->sentiment_score;
                $this->sentiment_score = null;
            }
            if (isset($this->summary))
            {
                if (!isset($this->document["summary"])) {
                    $this->document["summary"] = $this->summary;
                } else {
                    $this->document["summary"] .= $this->summary;
                }
                $this->summary = null;
            }
        }
        elseif ($this->parent == "entity")
        {
            if (isset($this->evidence))
            {
                $this->entity["evidence"] = $this->evidence;
                $this->evidence = null;
            }
            if (isset($this->sentiment_score))
            {
                $this->entity["sentiment_score"] = $this->sentiment_score;
                $this->sentiment_score = null;
            }
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
            if (isset($this->is_about))
            {
                $this->entity["is_about"] = $this->is_about;
                $this->is_about = null;
            }
            if (isset($this->confident))
            {
                $this->entity["confident"] = $this->confident;
                $this->confident = null;
            }
            if (isset($this->entity_type))
            {
                $this->entity["entity_type"] = $this->entity_type;
                $this->entity_type = null;
            }
        }
        elseif ($this->parent == "theme")
        {
            if (isset($this->evidence))
            {
                $this->theme["evidence"] = $this->evidence;
                $this->evidence = null;
            }
            if (isset($this->is_about))
            {
                $this->theme["is_about"] = $this->is_about;
                $this->is_about = null;
            }
            if (isset($this->sentiment_score))
            {
                $this->theme["sentiment_score"] = $this->sentiment_score;
                $this->sentiment_score = null;
            }
            if (isset($this->strength_score))
            {
                $this->theme["strength_score"] = $this->strength_score;
                $this->strength_score = null;
            }
            if (isset($this->title))
            {
                if (!isset($this->theme["title"])) {
                    $this->theme["title"] = $this->title;
                } else {
                    $this->theme["title"] .= $this->title;
                }
                $this->title = null;
            }
        }
        elseif ($this->parent == "topic")
        {
            if (isset($this->sentiment_score))
            {
                $this->topic["sentiment_score"] = $this->sentiment_score;
                $this->sentiment_score = null;
            }
            if (isset($this->strength_score))
            {
                $this->topic["strength_score"] = $this->strength_score;
                $this->strength_score = null;
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
            if (isset($this->title))
            {
                if (!isset($this->topic["title"])) {
                    $this->topic["title"] = $this->title;
                } else {
                    $this->topic["title"] .= $this->title;
                }
                $this->title = null;
            }
        }
        elseif ($this->parent == "phrase")
        {
            if (isset($this->sentiment_score))
            {
                $this->phrase["sentiment_score"] = $this->sentiment_score;
                $this->sentiment_score = null;
            }
            if (isset($this->is_negated))
            {
                $this->phrase["is_negated"] = $this->is_negated;
                $this->is_negated = null;
            }
            if (isset($this->negating_phrase))
            {
                if (!isset($this->phrase["negating_phrase"])) {
                    $this->phrase["negating_phrase"] = $this->negating_phrase;
                } else {
                    $this->phrase["negating_phrase"] .= $this->negating_phrase;
                }
                $this->negating_phrase = null;
            }
            if (isset($this->title))
            {
                if (!isset($this->phrase["title"])) {
                    $this->phrase["title"] = $this->title;
                } else {
                    $this->phrase["title"] .= $this->title;
                }
                $this->title = null;
            }
        }
    }
}
