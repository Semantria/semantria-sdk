# encoding: utf-8
#$LOAD_PATH << File.dirname(__FILE__) unless $LOAD_PATH.include?(File.dirname(__FILE__))
require_relative 'version'
require_relative 'authrequest'
require_relative 'jsonserializer'

class Session
  attr_accessor :host

  # Create a new instance
  def initialize(consumer_key, consumer_secret, application_name = nil, use_compression = false, serializer = nil)
    @host = 'https://api35.semantria.com'
    @wrapper_name = "Ruby/#{Semantria::VERSION}"
    @consumer_key = consumer_key
    @consumer_secret = consumer_secret
    @use_compression = use_compression

    if application_name.nil?
      @application_name = @wrapper_name
    else
      @application_name = '%s/%s' % [application_name, @wrapper_name]
    end

    if serializer.nil?
      #set default json serializer
      @serializer = JsonSerializer.new()
      @format = @serializer.gettype()
    else
      @serializer = serializer
      @format = @serializer.gettype()
    end
  end

  def registerSerializer(serializer)
    fail 'Serializer can\'t be null' if serializer.nil?

    @serializer = serializer
    @format = serializer.gettype()
  end

  def setCallbackHandler(callback)
    if callback.class < CallbackHandler
      @callback = callback
    else
      fail "Parameter is not subclass of CallbackHandler #{callback}"
    end
  end

  def getStatus
    url = "#@host/status.#@format"
    runRequest('GET', url, 'get_status')
  end

  def getSubscription
    url = "#@host/subscription.#@format"
    runRequest('GET', url, 'get_subscription')
  end

  def getStatistics
    url = "#@host/statistics.#@format"
    runRequest('GET', url, 'get_statistics')
  end

  def getConfigurations
    url = "#@host/configurations.#@format"
    result = runRequest('GET', url, 'get_configurations')
    result ||= []
  end

  def addConfigurations(items)
    updateConfigurations(items)
  end

  def updateConfigurations(items)
    url = "#@host/configurations.#@format"
    wrapper = get_type_wrapper('update_configurations')
    data = @serializer.serialize(items, wrapper)
    runRequest('POST', url, nil, data)
  end

  def deleteConfigurations(items)
    url = "#@host/configurations.#@format"

    wrapper = get_type_wrapper('delete_configurations')
    data = @serializer.serialize(items, wrapper)
    runRequest('DELETE', url, nil, data)
  end

  def getBlacklist(config_id = nil)
    if config_id.nil?
      url = "#@host/blacklist.#@format"
    else
      url = "#@host/blacklist.#@format?config_id=#{config_id}"
    end

    result = runRequest('GET', url, 'get_blacklist')
    result ||= []
  end

  def addBlacklist(items, config_id = nil)
    updateBlacklist(items, config_id)
  end

  def updateBlacklist(items, config_id = nil)
    if config_id.nil?
      url = "#@host/blacklist.#@format"
    else
      url = "#@host/blacklist.#@format?config_id=#{config_id}"
    end

    wrapper = get_type_wrapper('update_blacklist')
    data = @serializer.serialize(items, wrapper)
    runRequest('POST', url, nil, data)
  end

  def removeBlacklist(items, config_id = nil)
    if config_id.nil?
      url = "#@host/blacklist.#@format"
    else
      url = "#@host/blacklist.#@format?config_id=#{config_id}"
    end

    wrapper = get_type_wrapper('remove_blacklist')
    data = @serializer.serialize(items, wrapper)
    runRequest('DELETE', url, nil, data)
  end

  def getCategories(config_id = nil)
    if config_id.nil?
      url = "#@host/categories.#@format"
    else
      url = "#@host/categories.#@format?config_id=#{config_id}"
    end

    result = runRequest('GET', url, 'get_categories')
    result ||= []
  end

  def addCategories(items, config_id = nil)
    updateCategories(items, config_id)
  end

  def updateCategories(items, config_id = nil)
    if config_id.nil?
      url = "#@host/categories.#@format"
    else
      url = "#@host/categories.#@format?config_id=#{config_id}"
    end

    wrapper = get_type_wrapper('update_categories')
    data = @serializer.serialize(items, wrapper)
    runRequest('POST', url, nil, data)
  end

  def removeCategories(items, config_id = nil)
    if config_id.nil?
      url = "#@host/categories.#@format"
    else
      url = "#@host/categories.#@format?config_id=#{config_id}"
    end

    wrapper = get_type_wrapper('remove_categories')
    data = @serializer.serialize(items, wrapper)
    runRequest('DELETE', url, nil, data)
  end

  def getQueries(config_id = nil)
    if config_id.nil?
      url = "#@host/queries.#@format"
    else
      url = "#@host/queries.#@format?config_id=#{config_id}"
    end

    result = runRequest('GET', url, 'get_queries')
    result ||= []
  end

  def addQueries(items, config_id = nil)
    updateQueries(items, config_id)
  end

  def updateQueries(items, config_id = nil)
    if config_id.nil?
      url = "#@host/queries.#@format"
    else
      url = "#@host/queries.#@format?config_id=#{config_id}"
    end

    wrapper = get_type_wrapper('update_queries')
    data = @serializer.serialize(items, wrapper)
    runRequest('POST', url, nil, data)
  end

  def removeQueries(items, config_id = nil)
    if config_id.nil?
      url = "#@host/queries.#@format"
    else
      url = "#@host/queries.#@format?config_id=#{config_id}"
    end

    wrapper = get_type_wrapper('remove_queries')
    data = @serializer.serialize(items, wrapper)
    runRequest('DELETE', url, nil, data)
  end

  def getPhrases(config_id = nil)
    if config_id.nil?
      url = "#@host/phrases.#@format"
    else
      url = "#@host/phrases.#@format?config_id=#{config_id}"
    end

    result = runRequest('GET', url, 'get_sentiment_phrases')
    result ||= []
  end

  def addPhrases(items, config_id = nil)
    updatePhrases(items, config_id)
  end

  def updatePhrases(items, config_id = nil)
    if config_id.nil?
      url = "#@host/phrases.#@format"
    else
      url = "#@host/phrases.#@format?config_id=#{config_id}"
    end

    wrapper = get_type_wrapper('update_sentiment_phrases')
    data = @serializer.serialize(items, wrapper)
    runRequest('POST', url, nil, data)
  end

  def removePhrases(items, config_id = nil)
    if config_id.nil?
      url = "#@host/phrases.#@format"
    else
      url = "#@host/phrases.#@format?config_id=#{config_id}"
    end

    wrapper = get_type_wrapper('remove_phrases')
    data = @serializer.serialize(items, wrapper)
    runRequest('DELETE', url, nil, data)
  end

  def getEntities(config_id = nil)
    if config_id.nil?
      url = "#@host/entities.#@format"
    else
      url = "#@host/entities.#@format?config_id=#{config_id}"
    end

    result = runRequest('GET', url, 'get_entities')
    result ||= []
  end

  def addEntities(items, config_id = nil)
    updateEntities(items, config_id)
  end

  def updateEntities(items, config_id = nil)
    if config_id.nil?
      url = "#@host/entities.#@format"
    else
      url = "#@host/entities.#@format?config_id=#{config_id}"
    end

    wrapper = get_type_wrapper('update_entities')
    data = @serializer.serialize(items, wrapper)
    runRequest('POST', url, nil, data)
  end

  def removeEntities(items, config_id = nil)
    if config_id.nil?
      url = "#@host/entities.#@format"
    else
      url = "#@host/entities.#@format?config_id=#{config_id}"
    end

    wrapper = get_type_wrapper('remove_entities')
    data = @serializer.serialize(items, wrapper)
    runRequest('DELETE', url, nil, data)
  end

  def queueDocument(task, config_id = nil)
    if config_id.nil?
      url = "#@host/document.#@format"
    else
      url = "#@host/document.#@format?config_id=#{config_id}"
    end

    wrapper = get_type_wrapper('queue_document')
    data = @serializer.serialize(task, wrapper)
    result = runRequest('POST', url, 'get_processed_documents', data)
    if result != nil && result.is_a?(Array)
      onDocsAutoResponse(result)
      200
    else
      result
    end
  end

  def queueBatch(batch, config_id = nil)
    if config_id.nil?
      url = "#@host/document/batch.#@format"
    else
      url = "#@host/document/batch.#@format?config_id=#{config_id}"
    end

    wrapper = get_type_wrapper('queue_batch_documents')
    data = @serializer.serialize(batch, wrapper)
    result = runRequest('POST', url, 'get_processed_documents', data)
    if result != nil && result.is_a?(Array)
      onDocsAutoResponse(result)
      200
    else
      result
    end
  end

  def getDocument(doc_id, config_id = nil)
    fail 'Document ID is nil or empty' if doc_id.nil?

    if config_id.nil?
      url = "#@host/document/#{doc_id}.#@format"
    else
      url = "#@host/document/#{doc_id}.#@format?config_id=#{config_id}"
    end

    runRequest('GET', url, 'get_document')
  end

  def cancelDocument(doc_id, config_id = nil)
    fail 'Document ID is nil or empty' if doc_id.nil?

    if config_id.nil?
      url = "#@host/document/#{doc_id}.#@format"
    else
      url = "#@host/document/#{doc_id}.#@format?config_id=#{config_id}"
    end

    runRequest('DELETE', url)
  end

  def getProcessedDocuments(config_id = nil)
    if config_id.nil?
      url = "#@host/document/processed.#@format"
    else
      url = "#@host/document/processed.#@format?config_id=#{config_id}"
    end

    result = runRequest('GET', url, 'get_processed_documents')
    result ||= []
  end

  def queueCollection(task, config_id = nil)
    if config_id.nil?
      url = "#@host/collection.#@format"
    else
      url = "#@host/collection.#@format?config_id=#{config_id}"
    end

    wrapper = get_type_wrapper('queue_collection')
    data = @serializer.serialize(task, wrapper)
    result = runRequest('POST', url, 'get_processed_collections', data)
    if result != nil && result.is_a?(Array)
      onCollsAutoResponse(result)
      200
    else
      result
    end
  end

  def getCollection(id, config_id = nil)
    fail 'Collection ID is nil or empty' if id.nil?

    if config_id.nil?
      url = "#@host/collection/#{id}.#@format"
    else
      url = "#@host/collection/#{id}.#@format?config_id=#{config_id}"
    end

    runRequest('GET', url, 'get_collection')
  end

  def cancelCollection(id, config_id = nil)
    fail 'Collection ID is nil or empty' if id.nil?

    if config_id.nil?
      url = "#@host/collection/#{id}.#@format"
    else
      url = "#@host/collection/#{id}.#@format?config_id=#{config_id}"
    end

    runRequest('DELETE', url)
  end

  def getProcessedCollections(config_id = nil)
    if config_id.nil?
      url = "#@host/collection/processed.#@format"
    else
      url = "#@host/collection/processed.#@format?config_id=#{config_id}"
    end

    result = runRequest('GET', url, 'get_processed_collections')
    result ||= []
  end

  private
  def runRequest(method, url, type = nil, post_data = nil)
    request = AuthRequest.new(@consumer_key, @consumer_secret, @application_name, @use_compression)
    onRequest({'method' => method, 'url' => url, 'message' => post_data})
    response = request.authWebRequest(method, url, post_data)
    onResponse({'status' => response[:status], 'reason' => response[:reason], 'message' => response[:data]})

    status = response[:status]
    message = response[:reason]

    message = response[:data] unless response[:data].to_s.empty?

    if method == 'DELETE'
      if status == 200 || status == 202
        return status
      else
        resolve_error(status, message)
        return status
      end
    else
      if status == 200
        handler = get_type_handler(type)
        message = @serializer.deserialize(response[:data], handler)
        return message
      elsif status == 202
        if method == 'POST'
          return status
        else
          return nil
        end
      else
        resolve_error(status, message)
      end
    end
  end

  def get_type_handler(type)
    if @serializer.gettype() == 'json'
      return nil
    end

    #only for xml serializer
    case
      when type == 'get_status' then return GetStatusHandler.new()
      when type == 'get_subscription' then return GetSubscriptionHandler.new()
      when type == 'get_configurations' then return GetConfigurationsHandler.new()
      when type == 'get_blacklist' then return GetBlacklistHandler.new()
      when type == 'get_categories' then return GetCategoriesHandler.new()
      when type == 'get_queries' then return GetQueriesHandler.new()
      when type == 'get_sentiment_phrases' then return GetSentimentPhrasesHandler.new()
      when type == 'get_entities' then return GetEntitiesHandler.new()
      when type == 'get_document' then return GetDocumentHandler.new()
      when type == 'get_processed_documents' then return GetProcessedDocumentsHandler.new()
      when type == 'get_collection' then return GetCollectionHandler.new()
      when type == 'get_processed_collections' then return GetProcessedCollectionsHandler.new()
      else return nil
    end
  end

  def get_type_wrapper(type)
    if @serializer.gettype() == 'json'
      nil
    end

    #only for xml serializer
    #if type == "update_configurations"
    #  return {"root" => "configurations", "added" => "configuration", "removed" => "configuration"}
    #elsif type == "update_blacklist"
    #  return {"root" => "blacklist", "added" => "item", "removed" => "item"}
    #elsif type == "update_categories"
    #  return {"root" => "categories", "added" => "category", "removed" => "category", "samples" => "sample"}
    #elsif type == "update_queries"
    #  return {"root" => "queries", "added" => "query", "removed" => "query"}
    #elsif type == "update_sentiment_phrases"
    #  return {"root" => "phrases", "added" => "phrase", "removed" => "phrase"}
    #elsif type == "update_entities"
    #  return {"root" => "entities", "added" => "entity", "removed" => "entity"}
    #elsif type == "queue_document"
    #  return {"root" => "document"}
    #elsif type == "queue_batch_documents"
    #  return {"root" => "documents", "item" => "document"}
    #elsif type == "queue_collection"
    #  return {"root" => "collection", "documents" => "document"}
    #else
    #  return nil
    #end
  end

  def resolve_error(status, message = nil)
    if status == 400 || status == 401 || status == 402 || status == 403 || status == 406 || status == 500
      onError({'status' => status, 'message' => message})
    else
      fail "HTTP error was found with status: #{status} and message: #{message}"
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
    fail 'Don\'t instantiate me!' if abstract_class?
  end

  private
  def abstract_class?
    self.class == CallbackHandler
  end

  def onRequest(sender, args)
    fail 'Abstract method onRequest'
  end

  def onResponse(sender, args)
    fail 'Abstract method onResponse'
  end

  def onError(sender, args)
    fail 'Abstract method onError'
  end

  def onDocsAutoResponse(sender, args)
    fail 'Abstract method onDocsAutoResponse'
  end

  def onCollsAutoResponse(sender, args)
    fail 'Abstract method onCollsAutoResponse'
  end
end