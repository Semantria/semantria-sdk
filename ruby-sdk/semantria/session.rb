# encoding: utf-8
$LOAD_PATH << File.dirname(__FILE__) unless $LOAD_PATH.include?(File.dirname(__FILE__))

require 'authrequest'
require 'jsonserializer'
require 'xmlserializer'
require 'xmlhandlers'

class Session
   # Create a new instance
  def initialize(consumerKey, consumerSecret, serializer = nil, applicationName = nil)
    @host = 'https://api21.semantria.com'
	@wrapperName = 'Ruby'
    @consumerKey = consumerKey
    @consumerSecret = consumerSecret
	
	unless applicationName.nil?
		@applicationName = '%s.%s' % [applicationName, @wrapperName]
	else
		@applicationName = @wrapperName
	end
	
    unless serializer.nil?
      @serializer = serializer
      @format = @serializer.gettype()
    else
      #set default json serializer
      @serializer = JsonSerializer.new()
      @format = @serializer.gettype()
    end
  end
  
  def host= host
    @host = host
  end
  
  def host
    @host
  end
 
  def registerSerializer(serializer)
    unless serializer.nil?
      @serializer = serializer
      @format = serializer.gettype()
    else
      raise 'Parameter not found: %s' % serializer
    end
  end
  
  def setCallbackHandler(callback)
    if (callback.class < CallbackHandler)
      @callback = callback
    else
      raise 'Parameter is not subclass of CallbackHandler %s' % callback
    end
  end
  
  def createUpdateProxy()
    return {"added"=>[], "removed"=>[], "cloned"=>[]}
  end

  def getStatus()
    url = '%s/status.%s' % [@host, @format]
    return runRequest("GET", url, "get_status")
  end
 
  def verifySubscription()
    url = '%s/subscription.%s' % [@host, @format]
    return runRequest("GET", url, "get_subscription")
  end
 
  def getConfigurations()
    url = '%s/configurations.%s' % [@host, @format]
    result = runRequest("GET", url, "get_configurations")
    
    if result.nil?
      result = []
    end
    return result
  end
  
  def updateConfigurations(updates)
    url = '%s/configurations.%s' % [@host, @format]
    wrapper = getTypeWrapper("update_configurations")
    data = @serializer.serialize(updates, wrapper)
    return runRequest("POST", url, nil, data)
  end
  
  def getBlacklist(configId = nil)
    unless configId.nil?
      url = '%s/blacklist.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/blacklist.%s' % [@host, @format]
    end
    
    result = runRequest("GET", url, "get_blacklist")
    if result.nil?
      result = []
    end
    return result
  end
  
  def updateBlacklist(updates, configId = nil)
    unless configId.nil?
      url = '%s/blacklist.%s?config_id=%s' % [@host, @format, configId]
    else
       url = '%s/blacklist.%s' % [@host, @format]
    end

    wrapper = getTypeWrapper("update_blacklist")
    data = @serializer.serialize(updates, wrapper)
    return runRequest("POST", url, nil, data)
  end
  
  def getCategories(configId = nil)
    unless configId.nil?
      url = '%s/categories.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/categories.%s' % [@host, @format]
    end
    
    result = runRequest("GET", url, "get_categories")
    if result.nil?
      result = []
    end
    return result
  end
  
  def updateCategories(updates, configId = nil)
    unless configId.nil?
      url = '%s/categories.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/categories.%s' % [@host, @format]
    end

    wrapper = getTypeWrapper("update_categories")
    data = @serializer.serialize(updates, wrapper)
    return runRequest("POST", url, nil, data)
  end
  
  def getQueries(configId = nil)
    unless configId.nil?
      url = '%s/queries.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/queries.%s' % [@host, @format]
    end
    
    result = runRequest("GET", url, "get_queries")
    if result.nil?
      result = []
    end
    return result
  end
  
  def updateQueries(updates, configId = nil)
    unless configId.nil?
      url = '%s/queries.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/queries.%s' % [@host, @format]
    end

    wrapper = getTypeWrapper("update_queries")
    data = @serializer.serialize(updates, wrapper)
    return runRequest("POST", url, nil, data)
  end
  
  def getSentimentPhrases(configId = nil)
    unless configId.nil?
      url = '%s/sentiment.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/sentiment.%s' % [@host, @format]
    end
    
    result = runRequest("GET", url, "get_sentiment_phrases")
    if result.nil?
      result = []
    end
    return result
  end
  
  def updateSentimentPhrases(updates, configId = nil)
    unless configId.nil?
      url = '%s/sentiment.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/sentiment.%s' % [@host, @format]
    end

    wrapper = getTypeWrapper("update_sentiment_phrases")
    data = @serializer.serialize(updates, wrapper)
    return runRequest("POST", url, nil, data)
  end  
    
  def getEntities(configId = nil)
    unless configId.nil?
      url = '%s/entities.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/entities.%s' % [@host, @format]
    end
    
    result = runRequest("GET", url, "get_entities")
    if result.nil?
      result = []
    end
    return result
  end
  
  def updateEntities(updates, configId = nil)
    unless configId.nil?
      url = '%s/entities.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/entities.%s' % [@host, @format]
    end

    wrapper = getTypeWrapper("update_entities")
    data = @serializer.serialize(updates, wrapper)
    return runRequest("POST", url, nil, data)
  end
  
  def queueDocument(task, configId = nil)
    unless configId.nil?
      url = '%s/document.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/document.%s' % [@host, @format]
    end

    wrapper = getTypeWrapper("queue_document")
    data = @serializer.serialize(task, wrapper)
    result = runRequest("POST", url, "get_processed_documents", data)
    if (result != nil && result.is_a?(Array))
      onDocsAutoResponse(result)
      return 200
    else 
      return result
    end
  end
  
  def queueBatchOfDocuments(batch, configId = nil)
    unless configId.nil?
      url = '%s/document/batch.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/document/batch.%s' % [@host, @format]
    end

    wrapper = getTypeWrapper("queue_batch_documents")
    data = @serializer.serialize(batch, wrapper)
    result = runRequest("POST", url, "get_processed_documents", data)
    if (result != nil && result.is_a?(Array))
      onDocsAutoResponse(result)
      return 200
    else 
      return result
    end
  end

  def getDocument(id, configId = nil)
    unless configId.nil?
      unless id.nil?
        url = '%s/document/%s.%s?config_id=%s' % [@host, id, @format, configId]
      else
        raise 'Parameter is nil %s' % id
      end
    else
      url = '%s/document/%s.%s' % [@host, id, @format]
    end
    
    return runRequest("GET", url, "get_document")
  end

  def cancelDocument(id, configId = nil)
    unless configId.nil?
      unless id.nil?
        url = '%s/document/%s.%s?config_id=%s' % [@host, id, @format, configId]
      else
        raise 'Parameter is nil %s' % id
      end
    else
      url = '%s/document/%s.%s' % [@host, id, @format]
    end

    return runRequest("DELETE", url)
  end

  def getProcessedDocuments(configId = nil)
    unless configId.nil?
      url = '%s/document/processed.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/document/processed.%s' % [@host, @format]
    end
    
    result = runRequest("GET", url, "get_processed_documents")
    if result.nil?
      result = []
    end
    return result
  end
     
  def queueCollection(task, configId = nil)
    unless configId.nil?
      url = '%s/collection.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/collection.%s' % [@host, @format]
    end

    wrapper = getTypeWrapper("queue_collection")
    data = @serializer.serialize(task, wrapper)
    result = runRequest("POST", url, "get_processed_collections", data)
    if (result != nil && result.is_a?(Array))
      onCollsAutoResponse(result)
      return 200
    else 
      return result
    end
  end

  def getCollection(id, configId = nil)
    unless configId.nil?
      unless id.nil?
        url = '%s/collection/%s.%s?config_id=%s' % [@host, id, @format, configId]
      else
        raise 'Parameter is nil %s' % id
      end
    else
      url = '%s/collection/%s.%s' % [@host, id, @format]
    end
    
    return runRequest("GET", url, "get_collection")
  end

  def cancelCollection(id, configId = nil)
    unless configId.nil?
      unless id.nil?
        url = '%s/collection/%s.%s?config_id=%s' % [@host, id, @format, configId]
      else
        raise 'Parameter is nil %s' % id
      end
    else
      url = '%s/collection/%s.%s' % [@host, id, @format]
    end

    return runRequest("DELETE", url)
  end

  def getProcessedCollections(configId = nil)
    unless configId.nil?
      url = '%s/collection/processed.%s?config_id=%s' % [@host, @format, configId]
    else
      url = '%s/collection/processed.%s' % [@host, @format]
    end
    
    result = runRequest("GET", url, "get_processed_collections")
    if result.nil?
      result = []
    end
    return result
  end
   
  private
  def runRequest(method, url, type = nil, postData = nil)
    request = AuthRequest.new(@consumerKey, @consumerSecret, @applicationName)
    onRequest({"method"=>method, "url"=>url, "message"=>postData}) 
    response = request.authWebRequest(method, url, postData)
    onResponse({"status"=>response["status"], "reason"=>response["reason"], "message"=>response["data"]}) 
    
    status = response["status"]
    message = response["reason"]
    
    if (response["data"] != "")
      message = response["data"]
    end
  
    if (method == "DELETE")
      if (status == 200 or status == 202)
        return status
      else
        resolveError(status, message)
        return status
      end
    else
      if (status == 200)
        handler = getTypeHandler(type) 
        message = @serializer.deserialize(response["data"], handler)
        return message
      elsif (status == 202)
        if (method == "POST")
          return status
        else
          return nil
        end
      else
        resolveError(status, message)
      end
    end
  end
        
  def getTypeHandler(type)
    if @serializer.gettype() == "json"
        return nil
    end

    #only for xml serializer
    if type == "get_status"
        return GetStatusHandler.new()
    elsif type == "get_subscription"
        return GetSubscriptionHandler.new()
    elsif type == "get_configurations"
        return GetConfigurationsHandler.new()
    elsif type == "get_blacklist"
        return GetBlacklistHandler.new()
    elsif type == "get_categories"
        return GetCategoriesHandler.new()
    elsif type == "get_queries"
        return GetQueriesHandler.new()
    elsif type == "get_sentiment_phrases"
        return GetSentimentPhrasesHandler.new()	
    elsif type == "get_entities"
        return GetEntitiesHandler.new()
    elsif type == "get_document"
        return GetDocumentHandler.new()
    elsif type == "get_processed_documents"
        return GetProcessedDocumentsHandler.new()
    elsif type == "get_collection"
        return GetCollectionHandler.new()
    elsif type == "get_processed_collections"
        return GetProcessedCollectionsHandler.new()
    else
        return nil
    end
  end
 
  def getTypeWrapper(type)
    if @serializer.gettype() == "json"
      return nil
    end

    #only for xml serializer
    if type == "update_configurations"
      return {"root"=>"configurations", "added"=>"configuration", "removed"=>"configuration"}
    elsif type == "update_blacklist"
      return {"root"=>"blacklist", "added"=>"item", "removed"=>"item"}
    elsif type == "update_categories"
      return {"root"=>"categories", "added"=>"category", "removed"=>"category", "samples"=>"sample"}
    elsif type == "update_queries"
      return {"root"=>"queries", "added"=>"query", "removed"=>"query"}
    elsif type == "update_sentiment_phrases"
      return {"root"=>"phrases", "added"=>"phrase", "removed"=>"phrase"}      
    elsif type == "update_entities"
      return {"root"=>"entities", "added"=>"entity", "removed"=>"entity"}
    elsif type == "queue_document"
      return {"root"=>"document"}
    elsif type == "queue_batch_documents"
      return {"root"=>"documents", "item"=>"document"}
    elsif type == "queue_collection"
      return {"root"=>"collection", "documents"=>"document"}
    else
      return nil
    end
  end
  
  def resolveError(status, message = nil)
    if (status == 400 || status == 401 || status == 402 || status == 403 || status == 406 || status == 500)
      onError({"status"=>status, "message"=>message})
    else
      raise 'HTTP error was found with status: %s and message: %s' % [status, message]
    end
  end
  
  def onRequest(request) 
    unless @callback.nil?
      @callback.onRequest(self, request) 
    end
  end 
  
  def onResponse(response) 
    unless @callback.nil? 
      @callback.onResponse(self, response) 
    end
  end
  
  def onError(response) 
    unless @callback.nil?
      @callback.onError(self, response) 
    end 
  end
  
  def onDocsAutoResponse(response)
    unless @callback.nil? 
      @callback.onDocsAutoResponse(self, response) 
    end 
  end
    
  def onCollsAutoResponse(response)
    unless @callback.nil? 
      @callback.onCollsAutoResponse(self, response) 
    end 
  end
end 

class CallbackHandler
  def initialize
    raise "Don't instantiate me!" if abstract_class?
    puts "Instantiated!"
  end
  
  private
  def abstract_class?
    self.class == CallbackHandler
  end

  def onRequest(sender, args)
    raise NoMethodError.new("Abstract method onRequest")
  end
  
  def onResponse(sender, args)
    raise NoMethodError.new("Abstract method onResponse")
   end
  
  def onError(sender, args)
    raise NoMethodError.new("Abstract method onError")
  end
  
  def onDocsAutoResponse(sender, args)
    raise NoMethodError.new("Abstract method onDocsAutoResponse")
  end
    
  def onCollsAutoResponse(sender, args)
    raise NoMethodError.new("Abstract method onCollsAutoResponse")
  end
end