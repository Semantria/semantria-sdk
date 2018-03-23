# encoding: utf-8

module Semantria

  class Session
    attr_accessor :host
    attr_accessor :api_version

    # Create a new instance
    def initialize(consumer_key=nil, consumer_secret=nil,
                   username:nil, password:nil,
                   application_name:nil, use_compression:false, serializer:nil,
                   session_file:'/tmp/semantria-session.dat')
      @host = 'https://api.semantria.com'
      @key_url = 'https://semantria.com/auth/session'
      @app_key = 'cd954253-acaf-4dfa-a417-0a8cfb701f12'
      @wrapper_name = "Ruby/#{Semantria::VERSION}"
      @consumer_key = consumer_key
      @consumer_secret = consumer_secret
      @use_compression = use_compression
      @api_version = '4.2'

      if serializer.nil?
        #set default json serializer
        @serializer = JsonSerializer.new()
        @format = @serializer.gettype()
      else
        @serializer = serializer
        @format = @serializer.gettype()
      end

      if not ((consumer_key && consumer_secret) || (username && password))
        fail "Missing key/secret or username/password"
      end

      if consumer_key.nil? || consumer_secret.nil?
        @consumer_key, @consumer_secret = obtainKeys(username, password, session_file)
        if @consumer_key.nil? || @consumer_secret.nil?
          fail "Can't login. Bad username or password?"
        end
      end

      if application_name.nil?
        @application_name = @wrapper_name
      else
        @application_name = '%s/%s' % [application_name, @wrapper_name]
      end

    end


    def setCallbackHandler(callback)
      if callback.class < CallbackHandler
        @callback = callback
      else
        fail "Parameter is not subclass of CallbackHandler #{callback}"
      end
    end

    def getStatus
      url = makeUrl("/status")
      runRequest('GET', url)
    end

    def getSupportedFeatures(language)
      url = makeUrl("/features")
      if language
        url << "?language=#{language}"
      end
      runRequest('GET', url)
    end

    def getSubscription
      url = makeUrl("/subscription")
      runRequest('GET', url)
    end

    def getStatistics
      url = makeUrl("/statistics")
      runRequest('GET', url)
    end

    def getConfigurations
      url = makeUrl("/configurations")
      runRequest('GET', url) || []
    end

    def addConfigurations(items)
      updateConfigurations(items, true)
    end

    def updateConfigurations(items, create = false)
      url = makeUrl("/configurations")
      data = @serializer.serialize(items)
      runRequest((create ? 'POST' : 'PUT'), url, data)
    end

    def cloneConfigurations(name, template)
      items = {
        'name' => name,
        'template' => template
      }
      updateConfigurations([items])
    end

    def removeConfigurations(items)
      url = makeUrl("/configurations")
      data = @serializer.serialize(items)
      runRequest('DELETE', url, data)
    end

    def getBlacklist(config_id = nil)
      url = makeUrl("/blacklist", config_id)
      runRequest('GET', url) || []
    end

    def addBlacklist(items, config_id = nil)
      updateBlacklist(items, config_id, true)
    end

    def updateBlacklist(items, config_id = nil, create = false)
      url = makeUrl("/blacklist", config_id)
      data = @serializer.serialize(items)
      runRequest((create ? 'POST' : 'PUT'), url, data)
    end

    def removeBlacklist(items, config_id = nil)
      url = makeUrl("/blacklist", config_id)
      data = @serializer.serialize(items)
      runRequest('DELETE', url, data)
    end

    def getCategories(config_id = nil)
      url = makeUrl("/categories", config_id)
      runRequest('GET', url) || []
    end

    def addCategories(items, config_id = nil)
      updateCategories(items, config_id, true)
    end

    def updateCategories(items, config_id = nil, create = false)
      url = makeUrl("/categories", config_id)
      data = @serializer.serialize(items)
      runRequest((create ? 'POST' : 'PUT'), url, data)
    end

    def removeCategories(items, config_id = nil)
      url = makeUrl("/categories", config_id)
      data = @serializer.serialize(items)
      runRequest('DELETE', url, data)
    end

    def getQueries(config_id = nil)
      url = makeUrl("/queries", config_id)
      runRequest('GET', url) || []
    end

    def addQueries(items, config_id = nil)
      updateQueries(items, config_id, true)
    end

    def updateQueries(items, config_id = nil, create = false)
      url = makeUrl("/queries", config_id)
      data = @serializer.serialize(items)
      runRequest((create ? 'POST' : 'PUT'), url, data)
    end

    def removeQueries(items, config_id = nil)
      url = makeUrl("/queries", config_id)
      data = @serializer.serialize(items)
      runRequest('DELETE', url, data)
    end

    def getPhrases(config_id = nil)
      url = makeUrl("/phrases", config_id)
      runRequest('GET', url) || []
    end

    def addPhrases(items, config_id = nil)
      updatePhrases(items, config_id, true)
    end

    def updatePhrases(items, config_id = nil, create = false)
      url = makeUrl("/phrases", config_id)
      data = @serializer.serialize(items)
      runRequest((create ? 'POST' : 'PUT'), url, data)
    end

    def removePhrases(items, config_id = nil)
      url = makeUrl("/phrases", config_id)
      data = @serializer.serialize(items)
      runRequest('DELETE', url, data)
    end

    def getEntities(config_id = nil)
      url = makeUrl("/entities", config_id)
      runRequest('GET', url) || []
    end

    def addEntities(items, config_id = nil)
      updateEntities(items, config_id, true)
    end

    def updateEntities(items, config_id = nil, create = false)
      url = makeUrl("/entities", config_id)
      data = @serializer.serialize(items)
      runRequest((create ? 'POST' : 'PUT'), url, data)
    end

    def removeEntities(items, config_id = nil)
      url = makeUrl("/entities", config_id)
      data = @serializer.serialize(items)
      runRequest('DELETE', url, data)
    end

    def getTaxonomy(config_id = nil)
      url = makeUrl("/taxonomy", config_id)
      runRequest('GET', url) || []
    end

    def addTaxonomy(items, config_id = nil)
      updateTaxonomy(items, config_id, true)
    end

    def updateTaxonomy(items, config_id = nil, create = false)
      url = makeUrl("/taxonomy", config_id)
      data = @serializer.serialize(items)
      runRequest((create ? 'POST' : 'PUT'), url, data)
    end

    def removeTaxonomy(items, config_id = nil)
      url = makeUrl("/taxonomy", config_id)
      data = @serializer.serialize(items)
      runRequest('DELETE', url, data)
    end

    def queueDocument(task, config_id = nil)
      url = makeUrl("/document", config_id)
      data = @serializer.serialize(task)
      result = runRequest('POST', url, data)
      if result != nil && result.is_a?(Array)
        onDocsAutoResponse(result)
        200
      else
        result
      end
    end

    def queueBatch(batch, config_id = nil)
      url = makeUrl("/document/batch", config_id)
      data = @serializer.serialize(batch)
      result = runRequest('POST', url, data)
      if result != nil && result.is_a?(Array)
        onDocsAutoResponse(result)
        200
      else
        result
      end
    end

    def getDocument(doc_id, config_id = nil)
      fail 'Document ID is nil or empty' if doc_id.nil?
      doc_id = URI.encode doc_id
      url = makeUrl("/document/#{doc_id}", config_id)
      runRequest('GET', url)
    end

    def cancelDocument(doc_id, config_id = nil)
      fail 'Document ID is nil or empty' if doc_id.nil?
      doc_id = URI.encode doc_id
      url = makeUrl("/document/#{doc_id}", config_id)
      runRequest('DELETE', url)
    end

    def getProcessedDocuments(config_id = nil)
      url = makeUrl("/document/processed", config_id)
      result = runRequest('GET', url)
      result ||= []
    end

    def getProcessedDocumentsByJobId(job_id)
      url = makeUrl("/document/processed")
      url << "?job_id=#{job_id}"
      result = runRequest('GET', url)
      result ||= []
    end

    def queueCollection(task, config_id = nil)
      url = makeUrl("/collection", config_id)
      data = @serializer.serialize(task)
      result = runRequest('POST', url, data)
      if result != nil && result.is_a?(Array)
        onCollsAutoResponse(result)
        200
      else
        result
      end
    end

    def getCollection(id, config_id = nil)
      fail 'Collection ID is nil or empty' if id.nil?
      id = URI.encode id
      url = "#{@host}/collection/#{id}.#{@format}"
      if config_id
        url << "?config_id=#{config_id}"
      end
      runRequest('GET', url)
    end

    def cancelCollection(id, config_id = nil)
      fail 'Collection ID is nil or empty' if id.nil?
      id = URI.encode id
      url = makeUrl("/collection/#{id}", config_id)
      runRequest('DELETE', url)
    end

    def getProcessedCollections(config_id = nil)
      url = makeUrl("/collection/processed", config_id)
      result = runRequest('GET', url)
      result ||= []
    end

    def getProcessedCollectionsByJobId(job_id)
      url = makeUrl("/collection/processed", config_id)
      url << "?job_id=#{job_id}"
      result = runRequest('GET', url)
      result ||= []
    end

    # Gets the salience user data directory files for a config as a tar
    # or zip archive. Writes the archive as a file if path is given
    # otherwise returns a byte array. If there is an error getting the
    # archive data, then any error handlers are called and this function
    # returns the HTTP status. If there is an error writing the archive
    # file then an exception is raised.
    #
    # NOTE: these are keyword arguments rather than positional arguments.
    def getUserDirectory(config_id:nil, path:nil, format:'zip')
      format = getArchiveFormat(path, format)
      url = makeUrl('/salience/user-directory', config_id, fmt=format)
      response = runRequest('GET', url, nil, true)
      if !success?(response[:status])
        _resolveError(response)
        return response[:status]
      end
      if path
        IO.binwrite(path, response[:data])
        return response[:status]
      else
        return response[:data]
      end
    end

    # Determines the archive format. If an unknown format is given or no
    # format is given then use zip format. Format is determined first
    # from the path (if given) then from the the format parameter.
    private
    def getArchiveFormat(path, format)
      if path
        path_lower = path.downcase
        if path_lower.end_with?('.tar.gz')
          return 'tar.gz'
        end
        if path_lower.end_with?('.tar')
          return 'tar'
        end
        if path_lower.end_with?('.zip')
          return 'zip'
        end
      end
      if format
        if ['tar.gz', 'tar', 'zip'].include?(format)
          return format
        end
        if ['.tar.gz', '.tar', '.zip'].include?(format)
          return format[1..-1]
        end
      end
      # zip format is the default
      'zip'
    end

    private

    def makeUrl(path, config_id=nil, fmt=@format)
      if ! path.start_with?("/")
        path = "/#{path}"
      end
      url = "#{@host}#{path}.#{fmt}"
      if config_id
        url << "?config_id=#{config_id}"
      end
      url
    end

    def success?(x)
      x.between?(200, 299)
    end

    def runRequest(method, url, post_data=nil, is_binary=false)
      request = AuthRequest.new(@consumer_key, @consumer_secret, @application_name, @use_compression)
      request.api_version=(@api_version)
      
      onRequest({'method' => method,
                 'url' => url,
                 'message' => post_data})
      # Note, authWebRequest returns a hash with symbols top level keys
      # (:status, :reason and :data). However, json objects read from
      # the API in :data will have string keys.
      response = request.authWebRequest(method, url, post_data)
      onResponse({'status' => response[:status],
                  'reason' => response[:reason],
                  'message' => response[:data]})

      status = response[:status]

      if is_binary && success?(status)
        return response
      end

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
          message = @serializer.deserialize(response[:data])
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


    def obtainKeys(username, password, session_file = '/tmp/sematria-session.dat')

      session_id = getSavedSessionId(session_file, username)
      if !session_id.to_s.empty?
        url = "#{@key_url}/#{session_id}.json?appkey=#{@app_key}"
        result = httpRequest(url)
        if result
          custom_params = result['custom_params']
          if custom_params && custom_params.key?('key') && custom_params.key?('secret')
            return [custom_params['key'], custom_params['secret']]
          end
        end
      end

      url = "#{@key_url}.json?appkey=#{@app_key}";
      data = {'username' => username, 'password' => password}
      result = httpRequest(url, data)

      if !result
        return [nil, nil]
      end

      session_id = result['id'].to_s
      saveSessionId(session_file, username, session_id)

      custom_params = result['custom_params']
      if custom_params && custom_params.key?('key') && custom_params.key?('secret')
        return [custom_params['key'], custom_params['secret']]
      else
        return [nil, nil]
      end
    end

    def httpRequest(url, data=nil)
      uri = URI.parse url
      http = Net::HTTP.new(uri.host, uri.port)
      http.use_ssl = true
      http.verify_mode = OpenSSL::SSL::VERIFY_NONE
      if data
        request = Net::HTTP::Post.new(uri.request_uri)
        request.body = data.to_json
      else
        request = Net::HTTP::Get.new(uri.request_uri)
      end
      response = http.request(request)
      if response.code.to_i == 200
        return @serializer.deserialize(response.body)
      else
        return nil
      end
    end
        

    # Cache session id in a simple two line format:
    #   username
    #   session id
    def saveSessionId(session_file, username, session_id)
      if session_file.to_s.empty? || session_id.to_s.empty?
        return
      end
      File.open(session_file, "w") {
        |f|
        f.write("#{username}\n")
        f.write("#{session_id}\n")
      }
    end

    # Returns session id if a reasonable one is found
    def getSavedSessionId(session_file, username)
      if ! File.readable?(session_file)
        return nil
      end
      begin
        File.open(session_file, "r") {
          |f|
          u = f.readline("\n").strip
          if u == username
            return f.readline().strip
          else
            return nil
          end
        }
      rescue Exception => e
        pp 'Error reading session file', e, e.message
        return nil
      end
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

end
