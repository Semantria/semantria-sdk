<?php
namespace Semantria;

class AuthRequest 
{
	protected $OAuthVersion = "1.0";
	protected $OAuthParameterPrefix = "oauth_";
	protected $OAuthConsumerKeyKey = "oauth_consumer_key";
	protected $OAuthVersionKey = "oauth_version";
	protected $OAuthSignatureMethodKey = "oauth_signature_method";
	protected $OAuthSignatureKey = "oauth_signature";
	protected $OAuthTimestampKey = "oauth_timestamp";
	protected $OAuthNonceKey = "oauth_nonce";
	
	// the consumer key and secret
	protected $_consumerKey;
	protected $_consumerSecret;

	protected $_applicationName;
	protected $_http_info;

    protected $use_compression;

	function __construct($consumerKey = NULL, $consumerSecret = NULL, $applicationName = NULL, $use_compression = FALSE)
	{
		if (empty($consumerKey))
			throw new \Exception('Parameter is null or empty "'.$consumerKey.'"');

        if (empty($consumerSecret))
			throw new \Exception('Parameter is null or empty "'.$consumerSecret.'"');

		$this->_consumerKey = $consumerKey;
		$this->_consumerSecret = $consumerSecret;
		$this->_applicationName = $applicationName;
        $this->use_compression = $use_compression;
	}

	function authWebRequest($method, $url, $body)
	{
		$nonce = uniqid('');
		$timestamp = time();
		$query = $this->generateQuery($method, $url, $timestamp, $nonce);
		$auth_header = $this->generateAuthHeader($query, $timestamp, $nonce);
		
		$headers = array();
		$headers[] = 'Authorization: '.$auth_header;
		if ($method == "POST") {
			$headers[] = 'Content-type: application/x-www-form-urlencoded';
		}
		$headers['x-api-version'] = '3';
		$headers[] = 'x-api-version: 3';
		$headers[] = 'x-app-name: ' . $this->_applicationName;
		
		$response = $this->httpRequest($query, $method, $headers, $body);
		return $response;
	}

	protected function generateQuery($method, $url, $timestamp, $nonce)
	{
		$ps = @parse_url($url);
		$np = $this->getNormalizedParameters('&', $timestamp, $nonce);

		if (!empty($ps['query'])) {
			$ps['query'] = $ps['query'].'&'.$np;
		}
		else{
			$ps['query'] = $np;
		}
		
		$query = $ps['scheme'].'://'.$ps['host'].(isset($ps['port']) && $ps['port'] != '' ? (':'.$ps['port']) : '').$ps['path'].'?'.$ps['query'];
		return $query;   
	}
	
	protected function generateAuthHeader($query, $timestamp, $nonce)
	{
		$md5cs = md5($this->_consumerSecret);
		$escquery = $this->urlencode($query);
		$hash = $this->getSHA1($md5cs, $escquery);
		
		$headers = array();
		$headers['OAuth realm'] = "";
		$headers[$this->OAuthVersionKey] = $this->OAuthVersion;
		$headers[$this->OAuthTimestampKey] = $timestamp;
		$headers[$this->OAuthNonceKey] = $nonce;
		$headers[$this->OAuthSignatureMethodKey] = "HMAC-SHA1";
		$headers[$this->OAuthConsumerKeyKey] = $this->_consumerKey;
		$headers[$this->OAuthSignatureKey] = $hash;
		
		ksort($headers);
		
		$h   = array();
		foreach ($headers as $name => $value)
		{
			$h[] = $name.'="'.$value.'"';
		}

		$hs = implode(',', $h); 
		return $hs;
	}
	
	protected function getNormalizedParameters($glue = "", $timestamp, $nonce)
	{
		$headers = array();
		$headers[$this->OAuthVersionKey] = $this->OAuthVersion;
		$headers[$this->OAuthTimestampKey] = $timestamp;
		$headers[$this->OAuthNonceKey] = $nonce;
		$headers[$this->OAuthSignatureMethodKey] = "HMAC-SHA1";
		$headers[$this->OAuthConsumerKeyKey] = $this->_consumerKey;
		
		ksort($headers);
		
		$h   = array();
		foreach ($headers as $name => $value)
		{
			$h[] = $name.'='.$value;
		}

		$hs = implode($glue, $h); 
		return $hs;
	}
	
	protected function urlencode($s)
	{
		if ($s === false)
		{
			return $s;
		}
		else
		{
			return str_replace('%7E', '~', rawurlencode($s));
		}
	}
	
	protected function getMD5Hash($str)
	{
		$md5 = md5($str);
		$bin = '';
		
		for ($i = 0; $i < strlen($md5); $i += 2)
		{
			$bin .= chr(hexdec($md5{$i+1}) + hexdec($md5{$i}) * 16);
		}
		
		$md5sig = $this->urlencode(base64_encode($bin)); 
		return $md5sig;
	}
	
	protected function getSHA1($key, $query)
	{
		//$signature = base64_encode(hash_hmac("sha1", $query, $key, true));
		$blocksize	= 64;
		$hashfunc	= 'sha1';
		
		if (strlen($key) > $blocksize)
		{
			$key = pack('H*', $hashfunc($key));
		}
		
		$key	= str_pad($key,$blocksize,chr(0x00));
		$ipad	= str_repeat(chr(0x36),$blocksize);
		$opad	= str_repeat(chr(0x5c),$blocksize);
		$hmac 	= pack(
			'H*',$hashfunc(
					($key^$opad).pack(
						'H*',$hashfunc(
							($key^$ipad).$query
							)
						)
					)
				);
		
		$signature = base64_encode($hmac);
		$sha1sig = $this->urlencode($signature); 
		return $sha1sig;
	}

	protected function httpRequest($url, $method, $headers, $postfields=NULL) {
		$http_info = array();
		$ci = curl_init();
		
		curl_setopt($ci, CURLOPT_HTTPHEADER, $headers);
		curl_setopt($ci, CURLOPT_VERBOSE, FALSE);
		curl_setopt($ci, CURLOPT_RETURNTRANSFER, TRUE);
		curl_setopt($ci, CURLOPT_SSL_VERIFYPEER, FALSE);
		curl_setopt($ci, CURLOPT_TIMEOUT, 30);

        if ($this->use_compression) {
            curl_setopt($ci, CURLOPT_ENCODING, "gzip,deflate");
        }

		switch ($method) {
			case 'POST':
				curl_setopt($ci, CURLOPT_POST, TRUE);
				if (!empty($postfields)) {
					curl_setopt($ci, CURLOPT_POSTFIELDS, $postfields);
				}
				break;
			case 'DELETE':
				curl_setopt($ci, CURLOPT_CUSTOMREQUEST, 'DELETE');
				if (!empty($postfields)) {
					//$url = "{$url}?{$postfields}";
                    curl_setopt($ci, CURLOPT_POSTFIELDS, $postfields);
				}
		}
		
		curl_setopt($ci, CURLOPT_URL, $url);

		$response = curl_exec($ci);
		$this->_http_info = array_merge($http_info, curl_getinfo($ci));
		$code = curl_getinfo($ci, CURLINFO_HTTP_CODE);
		$message = $response;
		
		curl_close ($ci);

		$result = array("status"=>$code, "message"=>$message);
		return $result;
	} 
}
