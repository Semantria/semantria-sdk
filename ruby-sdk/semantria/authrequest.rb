# encoding: utf-8
require 'uri'
require 'base64'
require 'cgi' 
require 'digest/md5'
require 'openssl'
require 'digest/sha1'
require 'net/http'
require 'net/https'

OAuthVersion = "1.0"
OAuthParameterPrefix = "oauth_"
OAuthConsumerKeyKey = "oauth_consumer_key"
OAuthVersionKey = "oauth_version"
OAuthSignatureMethodKey = "oauth_signature_method"
OAuthSignatureKey = "oauth_signature"
OAuthTimestampKey = "oauth_timestamp"
OAuthNonceKey = "oauth_nonce"

class AuthRequest  
  # Create a new instance
  def initialize(consumerKey, consumerSecret, applicationName)
    @consumerKey = consumerKey
    @consumerSecret = consumerSecret
	@applicationName = applicationName
  end

  def authWebRequest(method, url, postData = nil)
    nonce = generateNonce()
    timestamp = generateTimestamp()
    query = generateQuery(method, url, timestamp, nonce)
    authheader = generateAuthHeader(query, timestamp, nonce)
    headers = {"Authorization" => authheader}
    
    if (method == "POST")
      headers["Content-type"] = "application/x-www-form-urlencoded"
    end
	headers["x-api-version"] = "2"
	headers["x-app-name"] = @applicationName
    
    uri = URI.parse(query)
    conn = Net::HTTP.new(uri.host, 443)
    conn.use_ssl = true
    conn.verify_mode = OpenSSL::SSL::VERIFY_NONE

    path = uri.request_uri #'%s?%s' % [qpath, qquery]
    request = getRequest(method, path, headers, postData)
    response = conn.request(request)
 
    result = { "status"=>response.code.to_i, "reason"=>response.message, "data"=>response.body }
    return result
  end
  
  private
  # create the http request object for a given http_method and path
  def getRequest(method, path, headers, postData = nil)
    case method
    when "POST"
      request = Net::HTTP::Post.new(path, headers)
    when "PUT"
      request = Net::HTTP::Put.new(path, headers)
    when "GET"
      request = Net::HTTP::Get.new(path, headers)
    when "DELETE"
      request =  Net::HTTP::Delete.new(path, headers)
    else
      raise ArgumentError, "Don't know how to handle method: :#{method}"
    end
    
    unless postData.nil?
      request.body = postData
      request["Content-Length"] = request.body.length.to_s
    end
    
    request
  end 
  
  def generateQuery(method, url, timestamp, nonce)
    uri = URI.parse(url)
    np = getNormalizedParameters(timestamp, nonce)

    if uri.query
      uri.query = '%s&%s' % [uri.query, np]
    else
      uri.query = '%s' % np
    end
  
    return uri.to_s        
  end

  def generateAuthHeader(query, timestamp, nonce)
    md5cs = getMD5Hash(@consumerSecret)
    escquery = escape(query)
    hash = getSHA1(md5cs, escquery)
    hash = escape(hash)
   
    items = Hash.new()
    items["OAuth realm"] = ""
    items[OAuthVersionKey] = "%s" % OAuthVersion
    items[OAuthTimestampKey] = "%s" % timestamp
    items[OAuthNonceKey] = "%s" % nonce
    items[OAuthSignatureMethodKey] = "HMAC-SHA1"
    items[OAuthConsumerKeyKey] = "%s" % @consumerKey
    items[OAuthSignatureKey] = "%s" % hash
 
    params = []
    for key in items.keys.sort 
       params.push('%s="%s"' % [key, items[key]])
    end
    
    return params.join(',')
  end

  def getNormalizedParameters(timestamp, nonce)
    items = Hash.new()
    items[OAuthVersionKey] = OAuthVersion
    items[OAuthTimestampKey] = timestamp
    items[OAuthNonceKey] = nonce
    items[OAuthSignatureMethodKey] = "HMAC-SHA1"
    items[OAuthConsumerKeyKey] = @consumerKey
    
    params = []
    for key in items.keys.sort 
       params.push('%s=%s' % [key, items[key]])
    end 
        
    np = params.join('&')
    # Encode signature parameters per Oauth Core 1.0 protocol
    # Spaces must be encoded with "%20" instead of "+"
    return np.gsub('+', '%20').gsub('%7E', '~')
  end

  def getMD5Hash(str)
    md5hash = Digest::MD5.hexdigest(str) 
  end
 
  def getSHA1(md5cs, query)
    digest = OpenSSL::Digest::Digest.new('sha1') 
    # our composite signing key now has the token secret after the ampersand
    sha1res = OpenSSL::HMAC.digest(digest, md5cs, query)
    sha1sig = Base64.encode64(sha1res).chomp.gsub(/\n/, "")
  end

  def generateTimestamp()
    Time.now.to_i.to_s
  end

  def generateNonce(length = 20)
    rand(10 ** length).to_s.rjust(length,'0')
  end
  
  def escape(s)
      CGI::escape(s)
  end   
end  