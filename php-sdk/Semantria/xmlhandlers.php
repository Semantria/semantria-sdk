<?php

require_once('common.php');

class GetStatusHandler
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

class GetSubscriptionHandler
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

class GetConfigurationsHandler
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

class GetBlacklistHandler
{
    function __construct()
    {
        $this->current = "";
        $this->data = null;
        $this->item = "";
    }

    function getData() 
    {
        return $this->data;
    }

    # Call when an element starts
    function startElement($parser, $tag, $attributes)
    {
        $this->current = $tag;
        if ($tag == "blacklist") 
        {
            $this->data = array();
        }
    }

    # Call when an elements ends
    function endElement($parser, $tag)
    {
        if ($tag == "item") {
            array_push($this->data, $this->item);
        }
 
        $this->current = "";
    }

    # Call when a character is read
    function characters($parser, $content)
    {
        if ($this->current == "item") {
            $this->item = $content;
        }
    }
}

class GetCategoriesHandler
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

class GetQueriesHandler
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

class GetSentimentPhrasesHandler
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

class GetEntitiesHandler
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

class GetDocumentHandler
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
        elseif ($this->current == "is_about") $this->is_about = str2bool($content);
        elseif ($this->current == "confident") $this->confident = str2bool($content);
        elseif ($this->current == "entity_type") $this->entity_type = $content;
        elseif ($this->current == "negating_phrase") $this->negating_phrase = $content;
        elseif ($this->current == "is_negated") $this->is_negated = str2bool($content);
        
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

class GetProcessedDocumentsHandler extends GetDocumentHandler
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
        if ($tag == "documents") 
            $this->data = array();
        else
            parent::startElement($parser, $tag, $attributes);
    }
    
    # Call when an elements ends
    function endElement($parser, $tag)
    {
        parent::endElement($parser, $tag);
        if ($tag == "document")
        { 
            array_push($this->data, $this->document);
            $this->document = null;
        }
    }    
}

class GetCollectionHandler
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

class GetProcessedCollectionsHandler extends GetCollectionHandler
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

?>
