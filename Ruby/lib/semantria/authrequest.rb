# encoding: utf-8
require 'uri'
require 'base64'
require 'cgi'
require 'digest/md5'
require 'openssl'
require 'digest/sha1'
require 'net/http'
require 'net/https'

module Semantria
  OAUTH_VERSION = '1.0'
  OAUTH_KEY_PREFIX = "oauth_"
  OAUTH_CONSUMER_KEY = "oauth_consumer_key"
  OAUTH_VERSION_KEY = "oauth_version"
  OAUTH_SIGNATURE_METHOD_KEY = "oauth_signature_method"
  OAUTH_SIGNATURE_KEY = "oauth_signature"
  OAUTH_TIMESTAMP_KEY = "oauth_timestamp"
  OAUTH_NONCE_KEY = "oauth_nonce"

  class AuthRequest
    # Create a new instance
    def initialize(consumer_key, consumer_secret, application_name, use_compression = false)
      @consumer_key = consumer_key
      @consumer_secret = consumer_secret
      @application_name = application_name
      @use_compression = use_compression
    end

    def authWebRequest(method, url, post_data = nil)
      nonce = generateNonce()
      timestamp = generateTimestamp()
      query = generate_query(method, url, timestamp, nonce)
      auth_header = generate_auth_header(query, timestamp, nonce)
      headers = {'Authorization' => auth_header}

      headers['Content-type'] = 'application/x-www-form-urlencoded' if method == 'POST'
      headers['x-api-version'] = '3.8'
      headers['x-app-name'] = @application_name

      headers['Accept-Encoding'] = 'gzip' if @use_compression

      uri = URI.parse(query)
      conn = Net::HTTP.new(uri.host, 443)
      conn.use_ssl = true
      conn.verify_mode = OpenSSL::SSL::VERIFY_NONE

      path = uri.request_uri #'%s?%s' % [qpath, qquery]
      request = get_request(method, path, headers, post_data)
      response = conn.request(request)

      data = nil
      if response.header['Content-Encoding'].eql? 'gzip'
        sio = StringIO.new( response.body )
        gz = Zlib::GzipReader.new( sio )
        data = gz.read()
      else
        data = response.body
      end

      {status: response.code.to_i, reason: response.message, data: data}
    end

    private
    # create the http request object for a given http_method and path
    def get_request(method, path, headers, post_data = nil)
      request = nil
      case method
        when 'POST'
          request = Net::HTTP::Post.new(path, headers)
        when 'PUT'
          request = Net::HTTP::Put.new(path, headers)
        when 'GET'
          request = Net::HTTP::Get.new(path, headers)
        when 'DELETE'
          request = Net::HTTP::Delete.new(path, headers)
        else
          fail ArgumentError, "Don't know how to handle method: :#{method}"
      end

      unless post_data.nil?
        request.body = post_data
        request['Content-Length'] = request.body.length.to_s
      end

      request
    end

    def generate_query(method, url, timestamp, nonce)
      uri = URI.parse(url)
      np = get_normalized_parameters(timestamp, nonce)

      if uri.query
        uri.query = '%s&%s' % [uri.query, np]
      else
        uri.query = '%s' % np
      end

      uri.to_s
    end

    def generate_auth_header(query, timestamp, nonce)
      md5cs = get_md5_hash(@consumer_secret)
      esc_query = escape(query)
      hash = get_sha1(md5cs, esc_query)
      hash = escape(hash)

      items = Hash.new()
      items['OAuth realm'] = ''
      items[OAUTH_VERSION_KEY] = "%s" % OAUTH_VERSION
      items[OAUTH_TIMESTAMP_KEY] = "%s" % timestamp
      items[OAUTH_NONCE_KEY] = "%s" % nonce
      items[OAUTH_SIGNATURE_METHOD_KEY] = "HMAC-SHA1"
      items[OAUTH_CONSUMER_KEY] = "%s" % @consumer_key
      items[OAUTH_SIGNATURE_KEY] = "%s" % hash

      params = []
      items.keys.sort.each do |key|
        params.push('%s="%s"' % [key, items[key]])
      end

      params.join(',')
    end

    def get_normalized_parameters(timestamp, nonce)
      items = Hash.new()
      items[OAUTH_VERSION_KEY] = OAUTH_VERSION
      items[OAUTH_TIMESTAMP_KEY] = timestamp
      items[OAUTH_NONCE_KEY] = nonce
      items[OAUTH_SIGNATURE_METHOD_KEY] = "HMAC-SHA1"
      items[OAUTH_CONSUMER_KEY] = @consumer_key

      params = []
      for key in items.keys.sort
        params.push('%s=%s' % [key, items[key]])
      end

      np = params.join('&')
      # Encode signature parameters per Oauth Core 1.0 protocol
      # Spaces must be encoded with "%20" instead of "+"
      return np.gsub('+', '%20').gsub('%7E', '~')
    end

    def get_md5_hash(str)
      Digest::MD5.hexdigest(str)
    end

    def get_sha1(md5cs, query)
      digest = OpenSSL::Digest.new('sha1')
      # our composite signing key now has the token secret after the ampersand
      sha1res = OpenSSL::HMAC.digest(digest, md5cs, query)
      Base64.encode64(sha1res).chomp.gsub(/\n/, '')
    end

    def generateTimestamp()
      Time.now.to_i.to_s
    end

    def generateNonce(length = 20)
      rand(10 ** length).to_s.rjust(length, '0')
    end

    def escape(s)
      CGI::escape(s)
    end
  end
end
