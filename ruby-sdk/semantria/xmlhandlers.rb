# encoding: utf-8
require 'rexml/document'                                             
require 'rexml/parsers/sax2parser'
require 'rexml/sax2listener'
require 'common'

class GetStatusHandler 
  include REXML::SAX2Listener
  
  def initialize()
    @current = ""
    @data = nil
    
    @service_status = ""
    @api_version = ""
    @service_version = ""
    @supported_encoding = ""
    @supported_compression = ""	
    @supported_languages = nil
    @language = ""
  end
 
  def getData()
    return @data
  end
   
  def start_element(uri, localname, tag, attributes)
    @current = tag
    if (tag == "status")
      @data = Hash.new()
    elsif (tag == "supported_languages")
      @supported_languages = []
    end
  end

  def end_element(uri, localname, tag)
    if (tag == "status")
      @data["service_status"] = @service_status
      @data["api_version"] = @api_version
      @data["service_version"] = @service_version
      @data["supported_encoding"] = @supported_encoding
      @data["supported_compression"] = @supported_compression	  
    elsif (tag == "supported_languages")
      @data["supported_languages"] = @supported_languages
    elsif (tag == "language")
      @supported_languages.push(@language)
    end

    @current = ""
  end
  
  def characters(content)
    if (@current == "service_status") 
      @service_status = content
    elsif (@current == "api_version") 
      @api_version = content
    elsif (@current == "service_version") 
      @service_version = content
    elsif (@current == "supported_encoding") 
      @supported_encoding = content     
    elsif (@current == "supported_compression") 
      @supported_compression = content 	  
    elsif (@current == "language") 
      @language = content
    end
  end
end

class GetSubscriptionHandler 
  include REXML::SAX2Listener
  
  def initialize()
    @current = ""
    @data = nil
    
    @name = ""
    @status = ""
    @priority = ""
    @expiration_date = 0
    @calls_balance = 0
    @calls_limit = 0
    @calls_limit_interval = 0
    @docs_balance = 0
    @docs_limit = 0
    @docs_limit_interval = 0    
    @configurations_limit = 0    
    @blacklist_limit = 0
    @categories_limit = 0    
    @queries_limit = 0    
    @entities_limit = 0        
    @sentiment_limit = 0
    @characters_limit = 0    
    @batch_limit = 0
    @collection_limit = 0
    @auto_response_limit = 0
    @processed_batch_limit = 0
    @callback_batch_limit = 0
	@limit_type = ""
  end
  
  def getData()
    return @data
  end
     
  def start_element(uri, localname, tag, attributes)
    @current = tag
    if (tag == "subscription")
      @data = Hash.new()
    end
  end

  def end_element(uri, localname, tag)
    if (tag == "subscription") 
      @data["name"] = @name
      @data["status"] = @status
      @data["priority"] = @priority
      @data["expiration_date"] = @expiration_date
      @data["calls_balance"] = @calls_balance
      @data["calls_limit"] = @calls_limit
      @data["calls_limit_interval"] = @calls_limit_interval
      @data["docs_balance"] = @docs_balance
      @data["docs_limit"] = @docs_limit
      @data["docs_limit_interval"] = @docs_limit_interval
      @data["configurations_limit"] = @configurations_limit
      @data["blacklist_limit"] = @blacklist_limit
      @data["categories_limit"] = @categories_limit
      @data["queries_limit"] = @queries_limit
      @data["entities_limit"] = @entities_limit
      @data["sentiment_limit"] = @sentiment_limit
      @data["characters_limit"] = @characters_limit
      @data["batch_limit"] = @batch_limit
      @data["collection_limit"] = @collection_limit
      @data["auto_response_limit"] = @auto_response_limit
      @data["processed_batch_limit"] = @processed_batch_limit
      @data["callback_batch_limit"] = @callback_batch_limit
	  @data["limit_type"] = @limit_type
    end
    
    @current = ""
  end

  def characters(content)
    if (@current == "name") 
      @name = content
    elsif (@current == "status") 
      @status = content
    elsif (@current == "priority")
      @priority = content
    elsif (@current == "expiration_date") 
      @expiration_date = content.to_i
    elsif (@current == "calls_balance") 
      @calls_balance = content.to_i
    elsif (@current == "calls_limit") 
      @calls_limit = content.to_i
    elsif (@current == "calls_limit_interval") 
      @calls_limit_interval = content.to_i
    elsif (@current == "docs_balance") 
      @docs_balance = content.to_i      
    elsif (@current == "docs_limit") 
      @docs_limit = content.to_i
    elsif (@current == "docs_limit_interval") 
      @docs_limit_interval = content.to_i   
    elsif (@current == "configurations_limit") 
      @configurations_limit = content.to_i
    elsif (@current == "blacklist_limit") 
      @blacklist_limit = content.to_i
    elsif (@current == "categories_limit") 
      @categories_limit = content.to_i
    elsif (@current == "queries_limit") 
      @queries_limit = content.to_i      
    elsif (@current == "entities_limit") 
      @entities_limit = content.to_i
    elsif (@current == "sentiment_limit") 
      @sentiment_limit = content.to_i         
    elsif (@current == "characters_limit") 
      @characters_limit = content.to_i               
    elsif (@current == "batch_limit") 
      @batch_limit = content.to_i
    elsif (@current == "collection_limit") 
      @collection_limit = content.to_i
    elsif (@current == "auto_response_limit") 
      @auto_response_limit = content.to_i
    elsif (@current == "processed_batch_limit") 
      @processed_batch_limit = content.to_i
    elsif (@current == "callback_batch_limit") 
      @callback_batch_limit = content.to_i
    elsif (@current == "limit_type") 
      @limit_type = content	  
    end
  end
end

class GetConfigurationsHandler 
  include REXML::SAX2Listener
  
  def initialize()
    @current = ""
    @hierarchy = []
    @parent = ""
    @data = nil
    @item = nil
    @document = nil
    @collection = nil
    
    @config_id = ""
    @name = ""
	@one_sentence = false
    @is_primary = false
    @auto_responding = false
    @language = ""
    @chars_threshold = 0
    @callback = ""
	
    #document's fields
    @entity_themes_limit = 0
    @phrases_limit = 0	
    @summary_limit = 0
	@user_entities_limit = 0
    
    #colleciton's fields
    @facets_limit = 0
    @facet_atts_limit = 0
	
    #common fields for document and collection
    @themes_limit = 0
    @concept_topics_limit = 0
    @query_topics_limit = 0
    @named_entities_limit = 0
  end

  def getData()
    return @data
  end
  
  def start_element(uri, localname, tag, attributes)
    @current = tag
	
    if (tag == "document" or tag == "collection") 
      @parent = tag
      @hierarchy.push(tag)
    end
	
    if (tag == "configurations")
      @data = []
    elsif (tag == "document") 
      @document = Hash.new()
    elsif (tag == "collection") 
      @collection = Hash.new()
    end
  end

  def end_element(uri, localname, tag)
    if (tag == "configuration")
      @item = Hash.new()
      @item["config_id"] = @config_id
      @item["name"] = @name
      @item["is_primary"] = @is_primary
	  @item["one_sentence"] = @one_sentence
      @item["auto_responding"] = @auto_responding
      @item["language"] = @language
      @item["chars_threshold"] = @chars_threshold
      @item["callback"] = @callback
      @item["document"] = @document
      @item["collection"] = @collection
      @data.push(@item)
      @config_id = ""
      @name = ""
      @is_primary = false
	  @one_sentence = false
      @auto_responding = false
      @language = ""
      @chars_threshold = 0
      @callback = ""
      @document = nil
      @collection = nil
    elsif (tag == "document") 
      @document["concept_topics_limit"] = @concept_topics_limit
      @document["query_topics_limit"] = @query_topics_limit
      @document["named_entities_limit"] = @named_entities_limit
      @document["user_entities_limit"] = @user_entities_limit
      @document["entity_themes_limit"] = @entity_themes_limit
      @document["themes_limit"] = @themes_limit
      @document["phrases_limit"] = @phrases_limit	  
      @document["summary_limit"] = @summary_limit
      @concept_topics_limit = 0
      @query_topics_limit = 0
      @named_entities_limit = 0
      @user_entities_limit = 0
      @entity_themes_limit = 0
      @themes_limit = 0
      @phrases_limit = 0	  
      @summary_limit = 0
    elsif (tag == "collection") 
      @collection["facets_limit"] = @facets_limit
      @collection["facet_atts_limit"] = @facet_atts_limit
      @collection["concept_topics_limit"] = @concept_topics_limit
      @collection["query_topics_limit"] = @query_topics_limit
      @collection["named_entities_limit"] = @named_entities_limit
      @collection["themes_limit"] = @themes_limit
      @facets_limit = 0
      @facet_atts_limit = 0
      @concept_topics_limit = 0
      @query_topics_limit = 0
      @named_entities_limit = 0
      @themes_limit = 0
    end
      
    @current = ""
  end

  def characters(content)
    if (@current == "config_id") 
      @config_id = content
    elsif (@current == "name") 
      @name += content
    elsif (@current == "is_primary") 
      @is_primary = content.to_boolean()
    elsif (@current == "one_sentence") 
      @one_sentence = content.to_boolean()	  
    elsif (@current == "auto_responding") 
      @auto_responding = content.to_boolean()
    elsif (@current == "language") 
      @language = content
    elsif (@current == "callback")
      @callback = content
    elsif (@current == "chars_threshold") 
      @chars_threshold = content.to_i
    elsif (@current == "concept_topics_limit") 
      @concept_topics_limit = content.to_i
    elsif (@current == "query_topics_limit") 
      @query_topics_limit = content.to_i
    elsif (@current == "named_entities_limit") 
      @named_entities_limit = content.to_i
    elsif (@current == "user_entities_limit") 
      @user_entities_limit = content.to_i
    elsif (@current == "entity_themes_limit") 
      @entity_themes_limit = content.to_i
    elsif (@current == "themes_limit") 
      @themes_limit = content.to_i
    elsif (@current == "phrases_limit") 
      @phrases_limit = content.to_i	  
    elsif (@current == "summary_limit") 
      @summary_limit = content.to_i
    elsif (@current == "facets_limit") 
      @facets_limit = content.to_i
    elsif (@current == "facet_atts_limit") 
      @facet_atts_limit = content.to_i
    end
    
  end
end

class GetBlacklistHandler 
  include REXML::SAX2Listener
  
  def initialize()
    @current = ""
    @data = nil
    @item = ""
  end

  def getData() 
    return @data
  end

  def start_element(uri, localname, tag, attributes)
    @current = tag
    if (tag == "blacklist") 
      @data = []
    end
  end

  def end_element(uri, localname, tag)
    if (tag == "item")
      @data.push(@item)
      @item = ""
    end
 
    @current = ""
  end

  def characters(content)
    if (@current == "item")
      @item += content
    end
  end
end

class GetCategoriesHandler 
  include REXML::SAX2Listener
  
  def initialize()
    @current = ""
    @data = nil
    @category = nil
    
    @name = ""
    @weight = nil
    @sample = ""
  end
    
  def getData()
    return @data
  end

  def start_element(uri, localname, tag, attributes)
    @current = tag
    
    if (tag == "categories") 
      @data = []
    elsif (tag == "category") 
      @category = Hash.new()
    elsif (tag == "samples") 
      @samples = []
    end
  end

  def end_element(uri, localname, tag)
    if (tag == "category")
      @category["name"] = @name
      @category["weight"] = @weight
      @data.push(@category)
      @name = ""
      @weight = nil
    elsif (tag == "samples") 
      @category["samples"] = @samples
    elsif (tag == "sample") 
      @samples.push(@sample)
      @sample = ""
    end

    @current = ""
  end

  def characters(content)
    if (@current == "name") 
      @name += content
    elsif (@current == "weight") 
      @weight = content.to_f
    elsif (@current == "sample") 
      @sample += content
    end
  end
end

class GetQueriesHandler 
  include REXML::SAX2Listener
  
  def initialize()
    @current = ""
    @data = nil
    @query = nil
    
    @name = ""
    @content = ""
    @idx = 0
  end
    
  def getData() 
    return @data
  end

  def start_element(uri, localname, tag, attributes)
    @current = tag
    if (tag == "queries") 
      @data = []
    elsif (tag == "query") 
      @idx += 1
    end
  end

  def end_element(uri, localname, tag)
    if (tag == "query" && @idx == 1)
      @idx -= 1
      @query = Hash.new()
      @query["name"] = @name
      @query["query"] = @content
      @data.push(@query)
      @name = ""
      @content = ""
    elsif (tag == "query" && @idx == 2) 
      @idx -= 1
    end

    @current = ""
  end

  def characters(content)
    if (@current == "name") 
      @name += content
    elsif (@current == "query") 
      @content += content
    end
  end
end

class GetSentimentPhrasesHandler 
  include REXML::SAX2Listener
  
  def initialize()
    @current = ""
    @data = nil
    @phrase = nil
    
    @title = ""
    @weight = 0
    @idx = 0
  end
    
  def getData() 
    return @data
  end

  def start_element(uri, localname, tag, attributes)
    @current = tag
    if (tag == "phrases") 
      @data = []
    elsif (tag == "phrase") 
      @idx += 1
    end
  end

  def end_element(uri, localname, tag)
    if (tag == "phrase" && @idx == 1)
      @idx -= 1
      @phrase = Hash.new()
      @phrase["title"] = @title
      @phrase["weight"] = @weight
      @data.push(@phrase)
      @title = ""
      @weight = 0
    elsif (tag == "phrase" && @idx == 2) 
      @idx -= 1
    end

    @current = ""
  end

  def characters(content)
    if (@current == "title") 
      @title += content
    elsif (@current == "weight" && @weight == 0) 
      @weight = content.to_f
    end
  end
end

class GetEntitiesHandler 
  include REXML::SAX2Listener
  
  def initialize()
    @current = ""
    @data = nil
    @entity = nil
    
    @name = ""
    @type = ""
  end
    
  def getData() 
    return @data
  end

  def start_element(uri, localname, tag, attributes)
    @current = tag
    if (tag == "entities") 
      @data = []
    end
  end

  def end_element(uri, localname, tag)
    if (tag == "entity")
      @entity = Hash.new()
      @entity["name"] = @name
      @entity["type"] = @type
      @data.push(@entity)
      @name = ""
      @type = ""
    end

    @current = ""
  end

  def characters(content)
    if (@current == "name") 
      @name += content
    elsif (@current == "type") 
      @type += content
    end
  end
end

class GetDocumentHandler 
  include REXML::SAX2Listener
  
  def initialize()
    @current = ""
    @hierarchy = []
    @parent = ""
    @document = nil
    @entities = nil
    @entity = nil
    @entity_themes = nil
    @doc_themes = nil
    @theme = nil
    @topics = nil
    @topic = nil
    @phrases = nil	
    @phrase = nil
    
    @id = nil
    @config_id = nil
    @status = nil
    @sentiment_score = nil
    @summary = ""
    
    @evidence = nil
    @is_about = nil
    @confident = nil
    @strength_score = nil
    @type = nil
    @title = ""
    @hitcount = nil
    @entity_type = nil
    @is_negated = nil
    @negating_phrase = ""
  end
       
  def getData()
    return @document
  end

  def start_element(uri, localname, tag, attributes)
    @current = tag
    
    if (tag == "document" or tag == "entity" or tag == "theme" or tag == "topic" or tag == "phrase") 
      @parent = tag
      @hierarchy.push(tag)
    end
  
    if (tag == "document") 
      @document = Hash.new()
    elsif (tag == "entities") 
      @entities = []
    elsif (tag == "themes") 
      if (@parent == "entity")
        @entity_themes = []
      elsif (@parent == "document")
        @doc_themes = []
      end
    elsif (tag == "topics") 
      @topics = []
    elsif (tag == "phrases") 
      @phrases = []	  
    elsif (tag == "entity") 
      @entity = Hash.new()
    elsif (tag == "theme") 
      @theme = Hash.new()
    elsif (tag == "topic") 
      @topic = Hash.new()
	elsif (tag == "phrase") 
      @phrase = Hash.new()
    end
  end

  def end_element(uri, localname, tag)
    if (tag == "document" or tag == "entity" or tag == "theme" or tag == "topic" or tag == "phrase") 
      @parent = @hierarchy.pop()
      if (@hierarchy.length > 0) 
         @parent = @hierarchy[@hierarchy.length - 1]
      end
    end

    if (tag == "entities") 
      @document["entities"] = @entities
    elsif (tag == "topics") 
      @document["topics"] = @topics
    elsif (tag == "phrases") 
      @document["phrases"] = @phrases	  
    elsif (tag == "themes") 
        if (@parent == "entity") 
            @entity["themes"] = @entity_themes
        elsif (@parent == "document")
            @document["themes"] = @doc_themes
        end
    elsif (tag == "document")
      @document["entities"] = @entities
      @document["topics"] = @topics
      @document["phrases"] = @phrases	  
      @entities = nil
      @topics = nil
      @phrases = nil	  
    elsif (tag == "entity")
      @entity["themes"] = @entity_themes
      @entities.push(@entity)
      @entity = nil
      @entity_themes  = nil
    elsif (tag == "theme")
      if (@parent == "entity")
        @entity_themes.push(@theme)
      elsif (@parent == "document")
        @doc_themes.push(@theme)
      end
      @theme = nil
    elsif (tag == "topic")
      @topics.push(@topic)
      @topic = nil
    elsif (tag == "phrase")
      @phrases.push(@phrase)
      @phrase = nil	  
    end

    @current = ""
  end

  def characters(content)
    if (@current == "id") 
      @id = content
    elsif (@current == "config_id") 
      @config_id = content
    elsif (@current == "status") 
      @status = content
    elsif (@current == "sentiment_score") 
      @sentiment_score = content.to_f
    elsif (@current == "evidence") 
      @evidence = content.to_f
    elsif (@current == "strength_score") 
      @strength_score = content.to_f
    elsif (@current == "type") 
      @type = content
    elsif (@current == "title") 
      @title = content
    elsif (@current == "hitcount") 
      @hitcount = content.to_f
    elsif (@current == "summary") 
      @summary = content
    elsif (@current == "is_about") 
      @is_about = content.to_boolean
    elsif (@current == "confident")
      @confident = content.to_boolean
    elsif (@current == "is_negated")
      @is_negated = content.to_boolean
    elsif (@current == "entity_type") 
      @entity_type = content
    elsif (@current == "negating_phrase")
      @negating_phrase = content
    end
    
    if (@parent == "document") 
      if (@id != nil)
        @document["id"] = @id
        @id = nil
      end
      if (@config_id != nil)
        @document["config_id"] = @config_id
        @config_id = nil
      end
      if (@status != nil)
        @document["status"] = @status
        @status = nil
      end
      if (@sentiment_score != nil)
        @document["sentiment_score"] = @sentiment_score
        @sentiment_score = nil
      end
      if (@summary != nil)
        if (@document.has_key?("summary"))
          @document["summary"] += @summary
        else
          @document["summary"] = @summary
        end
        @summary = nil
      end
    elsif (@parent == "entity") 
      if (@evidence != nil)
        @entity["evidence"] = @evidence 
        @evidence = nil
      end
      if (@sentiment_score != nil)
        @entity["sentiment_score"] = @sentiment_score
        @sentiment_score = nil
      end
      if (@title != nil)
        if (@entity.has_key?("title"))
          @entity["title"] += @title
        else
          @entity["title"] = @title
        end
        @title = nil
      end
      if (@type != nil)
        @entity["type"] = @type
        @type = nil
      end
      if (@is_about != nil)
        @entity["is_about"] = @is_about
        @is_about = nil
      end
      if (@confident != nil)
        @entity["confident"] = @confident
        @confident = nil
      end
      if (@entity_type != nil)
        @entity["entity_type"] = @entity_type
        @entity_type = nil
      end
    elsif (@parent == "theme") 
      if (@evidence != nil)
        @theme["evidence"] = @evidence
        @evidence = nil
      end
      if (@is_about != nil)
        @theme["is_about"] = @is_about
        @is_about = nil
      end
      if (@sentiment_score != nil)
        @theme["sentiment_score"] = @sentiment_score
        @sentiment_score = nil
      end
      if (@strength_score != nil)
        @theme["strength_score"] = @strength_score
        @strength_score = nil
      end
      if (@title != nil)
        if (@theme.has_key?("title"))
          @theme["title"] += @title
        else
          @theme["title"] = @title
        end
        @title = nil
      end
   elsif (@parent == "topic") 
      if (@sentiment_score != nil)
        @topic["sentiment_score"] = @sentiment_score
        @sentiment_score = nil
      end
      if (@strength_score != nil)
        @topic["strength_score"] = @strength_score
        @strength_score = nil
      end
      if (@type != nil)
        @topic["type"] = @type
        @type = nil
      end
      if (@hitcount != nil)
        @topic["hitcount"] = @hitcount
        @hitcount = nil
      end
      if (@title != nil)
        if (@topic.has_key?("title"))
          @topic["title"] += @title
        else
          @topic["title"] = @title
        end
        @title = nil
      end
   elsif (@parent == "phrase") 
      if (@sentiment_score != nil)
        @phrase["sentiment_score"] = @sentiment_score
        @sentiment_score = nil
      end
      if (@is_negated != nil)
        @phrase["is_negated"] = @is_negated
        @is_negated = nil
      end
      if (@negating_phrase != nil)
        if (@phrase.has_key?("negating_phrase"))
          @phrase["negating_phrase"] += @negating_phrase
        else
          @phrase["negating_phrase"] = @negating_phrase
        end
        @negating_phrase = nil
      end
      if (@title != nil)
        if (@phrase.has_key?("title"))
          @phrase["title"] += @title
        else
          @phrase["title"] = @title
        end
        @title = nil
      end
    end	
  end
end

class GetProcessedDocumentsHandler < GetDocumentHandler
  def initialize()
    @data = nil
    super()
  end
  
  def getData()
    return @data
  end

  def start_element(uri, localname, tag, attributes)
    if (tag == "documents") 
      @data = []
    else
      super(uri, localname, tag, attributes)
    end
  end
  
  def end_element(uri, localname, tag)
    super(uri, localname, tag)
    if (tag == "document")
      @data.push(@document)
      @document = nil
    end
  end 
end

class GetCollectionHandler 
  include REXML::SAX2Listener
  
  def initialize()
    @current = ""
    @hierarchy = []
    @parent = ""
    @collection = nil
    @facets = nil
    @facet = nil
    @attributes = nil
    @attribute = nil
    @themes = nil
    @theme = nil
    @topics = nil
    @topic = nil
    @entities = nil
    @entity = nil
    
    @id = nil
    @config_id = nil
    @status = nil
    @label = nil
    @count = nil
    @negative_count = nil
    @positive_count = nil
    @neutral_count = nil
	
    @title = nil
    @phrases_count = nil    
    @themes_count = nil    
    @sentiment_score = nil
    @type = nil
    @entity_type = nil        
    @hitcount = nil
    @evidence = nil
  end
       
  def getData()
    return @collection
  end

  def start_element(uri, localname, tag, attributes)
    @current = tag
    
    if (tag == "collection" or tag == "facet" or tag == "attribute" or tag == "entity" or tag == "theme" or tag == "topic") 
      @parent = tag
      @hierarchy.push(tag)
    end
  
    if (tag == "collection") 
      @collection = Hash.new()
    elsif (tag == "facets") 
      @facets = []
    elsif (tag == "facet") 
      @facet = Hash.new()
    elsif (tag == "attributes") 
      @attributes = []
    elsif (tag == "attribute") 
      @attribute = Hash.new()
    elsif (tag == "entities") 
      @entities = []
    elsif (tag == "entity") 
      @entity = Hash.new()
    elsif (tag == "themes") 
      @themes = []
    elsif (tag == "theme") 
      @theme = Hash.new()
    elsif (tag == "topics") 
      @topics = []
    elsif (tag == "topic") 
      @topic = Hash.new()
    end
  end

  def end_element(uri, localname, tag)
    if (tag == "collection" or tag == "facet" or tag == "attribute" or tag == "entity" or tag == "theme" or tag == "topic") 
      @parent = @hierarchy.pop()
      if (@hierarchy.length > 0) 
         @parent = @hierarchy[@hierarchy.length - 1]
      end
    end

    if (tag == "facets") 
      @collection["facets"] = @facets
    elsif (tag == "attributes") 
      @facet["attributes"] = @attributes
    elsif (tag == "collection")
      @collection["facets"] = @facets
      @facets = nil
    elsif (tag == "facet")
      @facet["attributes"] = @attributes
      @facets.push(@facet)
      @facet = nil
      @attributes  = nil
    elsif (tag == "attribute")
      if (@parent == "facet")
        @attributes.push(@attribute)
      end
      @attribute = nil
    elsif (tag == "entities") 
      @collection["entities"] = @entities
    elsif (tag == "entity") 
      @entities.push(@entity)
      @entity = nil
    elsif (tag == "themes") 
      @collection["themes"] = @themes
    elsif (tag == "theme") 
      @themes.push(@theme)
      @theme = nil
    elsif (tag == "topics") 
      @collection["topics"] = @topics
    elsif (tag == "topic") 
      @topics.push(@topic)
      @topic = nil
    end

    @current = ""
  end

  def characters(content)
    if (@current == "id") 
      @id = content
    elsif (@current == "config_id") 
      @config_id = content
    elsif (@current == "status") 
      @status = content
    elsif (@current == "label") 
      @label = content
    elsif (@current == "count") 
      @count = content.to_i
    elsif (@current == "negative_count") 
      @negative_count = content.to_i
    elsif (@current == "positive_count") 
      @positive_count = content.to_i
    elsif (@current == "neutral_count") 
      @neutral_count = content.to_i
    elsif (@current == "title")
      @title = content
    elsif (@current == "phrases_count")
      @phrases_count = content.to_i
    elsif (@current == "themes_count")
      @themes_count = content.to_i
    elsif (@current == "sentiment_score")
      @sentiment_score = content.to_f
    elsif (@current == "type")
      @type = content
    elsif (@current == "entity_type")
      @entity_type = content
    elsif (@current == "hitcount")
      @hitcount = content.to_i
    end
    
    if (@parent == "collection") 
      if (@id != nil)
        @collection["id"] = @id
        @id = nil
      end
      if (@config_id != nil)
        @collection["config_id"] = @config_id
        @config_id = nil
      end
      if (@status != nil)
        @collection["status"] = @status
        @status = nil
      end
    elsif (@parent == "facet") 
      if (@label != nil)
        if (@facet.has_key?("@label"))
          @facet["label"] += @label
        else
          @facet["label"] = @label
        end
        @label = nil
      end
      if (@count != nil)
        @facet["count"] = @count
        @count = nil
      end
      if (@negative_count != nil)
        @facet["negative_count"] = @negative_count
        @negative_count = nil
      end
      if (@positive_count != nil)
        @facet["positive_count"] = @positive_count
        @positive_count = nil
      end
      if (@neutral_count != nil)
        @facet["neutral_count"] = @neutral_count
        @neutral_count = nil
      end
    elsif (@parent == "attribute") 
      if (@label != nil)
        if (@attribute.has_key?("@label"))
          @attribute["label"] += @label
        else
          @attribute["label"] = @label
        end
        @label = nil
      end
      if (@count != nil)
        @attribute["count"] = @count
        @count = nil
      end
    elsif (@parent == "entity") 
      if (@title != nil)
        if (@entity.has_key?("@title"))
          @entity["title"] += @title
        else
          @entity["title"] = @title
        end
        @title = nil
      end
      if (@type != nil)
        @entity["type"] = @type
        @type = nil
      end
      if (@entity_type != nil)
        @entity["entity_type"] = @entity_type
        @entity_type = nil
      end
      if (@count != nil)
        @entity["count"] = @count
        @count = nil
      end
      if (@negative_count != nil)
        @entity["negative_count"] = @negative_count
        @negative_count = nil
      end
      if (@neutral_count != nil)
        @entity["neutral_count"] = @neutral_count
        @neutral_count = nil
      end
      if (@positive_count != nil)
        @entity["positive_count"] = @positive_count
        @positive_count = nil
      end    
    elsif (@parent == "topic") 
      if (@title != nil)
        if (@topic.has_key?("@title"))
          @topic["title"] += @title
        else
          @topic["title"] = @title
        end
        @title = nil
      end
      if (@type != nil)
        @topic["type"] = @type
        @type = nil
      end
      if (@hitcount != nil)
        @topic["hitcount"] = @hitcount
        @hitcount = nil
      end
      if (@sentiment_score != nil)
        @topic["sentiment_score"] = @sentiment_score
        @sentiment_score = nil
      end
    elsif (@parent == "theme") 
      if (@title != nil)
        if (@theme.has_key?("@title"))
          @theme["title"] += @title
        else
          @theme["title"] = @title
        end
        @title = nil
      end
      if (@phrases_count != nil)
        @theme["phrases_count"] = @phrases_count
        @phrases_count = nil
      end
      if (@themes_count != nil)
        @theme["themes_count"] = @themes_count
        @themes_count = nil
      end
      if (@sentiment_score != nil)
        @theme["sentiment_score"] = @sentiment_score
        @sentiment_score = nil
      end    
    end
  end
end

class GetProcessedCollectionsHandler < GetCollectionHandler
  def initialize()
    @data = nil
    super()
  end
  
  def getData()
    return @data
  end

  def start_element(uri, localname, tag, attributes)
    if (tag == "collections") 
      @data = []
    else
      super(uri, localname, tag, attributes)
    end
  end
  
  def end_element(uri, localname, tag)
    super(uri, localname, tag)
    if (tag == "collection")
      @data.push(@collection)
      @collection = nil
    end
  end 
end