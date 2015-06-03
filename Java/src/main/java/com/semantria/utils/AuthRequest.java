package com.semantria.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.zip.GZIPInputStream;


public final class AuthRequest
{
	private String method = "GET";
	private String url = "";
	private HashMap<String, String> params = null;
	private String body = null;
	private Integer status = 0;
	private String key = "";
	private String secret = "";
	private String response = "";
	private String rurl = "";
	private String appName = "Java/3.8.82/";
	private boolean useCompression = false;
	private String errorMsg = null;
    private int CONNECTION_TIMEOUT = 120000;

	public AuthRequest(String curl, String cmethod, String ckey, String csecret, boolean useCompression)
	{	 
		this(curl, cmethod, ckey, csecret, null, null, useCompression);
	}
		
	public AuthRequest(String curl, String cmethod, String ckey, String csecret, String cbody, String config, boolean useCompression)
	{	 
		this(curl, cmethod, ckey, csecret, cbody, config, null, useCompression);
	}

	public AuthRequest(String curl, String cmethod, String ckey, String csecret, String cbody, final String config, String app_name, boolean useCompression)
	{
		this(curl, cmethod, ckey, csecret, cbody, app_name, useCompression, new HashMap<String, String>() {{ put("config_id", config); }});
	}

    public AuthRequest(String curl, String cmethod, String ckey, String csecret, String cbody, final String config, String app_name, final String interval, boolean useCompression)
    {
        this(curl, cmethod, ckey, csecret, cbody, app_name, useCompression, new HashMap<String, String>() {{ put("interval", interval); put("config_id", config); }});
    }

	public AuthRequest(String curl, String cmethod, String ckey, String csecret, String cbody, String app_name, boolean useCompression, HashMap<String, String> parameters)
	{
		try {
            this.useCompression = useCompression;
            url = curl;
            method = cmethod;
            key = ckey;
            secret = hashMD5(csecret);

            if (!parameters.isEmpty()) {
                if (parameters.containsKey("config_id")) {
                    String config = parameters.get("config_id");
                    if (config == null) {
                        parameters.remove("config_id");
                    }
                }

                if (parameters.containsKey("interval")) {
                    String config = parameters.get("interval");
                    if (config == null) {
                        parameters.remove("interval");
                    }
                }

                if (!parameters.isEmpty()) {
                    params = parameters;
                }
            }

			if(app_name != null)
			{
				appName = app_name + "/" + appName + ( curl.contains("json") ? "JSON" : "XML" );
			}
			else
			{
				appName = appName +  ( curl.contains("json") ? "JSON" : "XML" );
			}

			if(cbody != null)
			{
				body = cbody;
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
			//end of all being if there's no md5 in java.security.MessageDigest
		}
	}
	
	public Integer doRequest()
	{
		try
		{
			initSSLContext();
			HttpURLConnection conn = getOAuthSignedConnection();
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
        	conn.connect();
        	sendRequestBodyIfSetted(conn);
        	receiveResponseFromServer(conn);
	        status = conn.getResponseCode();

			if( status < 202 )
			{
				errorMsg = null;
			}
			conn.disconnect();
		}
		catch(Exception e)
		{
			System.out.println(e);
			e.printStackTrace();
		}
		return status;
	}
	
	private void initSSLContext() throws java.security.GeneralSecurityException {
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
		SSLContext.setDefault(ctx);
	}
	
	private HttpURLConnection getOAuthSignedConnection() throws IOException {
		setOAuthParameters();
		
		rurl = this.url + "?" + buildRequest(params);
		URL url = new URL(rurl);
        HttpURLConnection conn = null;
        if (this.url.startsWith("https")) {
            conn = (HttpsURLConnection) url.openConnection();
            ((HttpsURLConnection)conn).setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
        } else {
            conn = (HttpURLConnection) url.openConnection();
        }

        setRequestProperties(conn);
    	
    	return conn;
	}
	
	private void setOAuthParameters() {
		if(params == null) { params = new HashMap<String, String>(); }
		params.put("oauth_nonce", Long.toString(new Random().nextLong()&0xffffffffL));
		params.put("oauth_consumer_key", key);
		params.put("oauth_signature_method", "HMAC-SHA1");
		params.put("oauth_timestamp", Long.toString(System.currentTimeMillis()/1000));
		params.put("oauth_version", "1.0");	
	}
	
	private void setRequestProperties(HttpURLConnection conn) throws IOException {
		conn.setRequestProperty("Connection", "close");
		conn.setDoOutput(true);
    	conn.setRequestMethod(method);

		if( method.equals("GET") && useCompression == true )
		{
			conn.setRequestProperty("Accept-Encoding","gzip,deflate");
		};

		conn.setRequestProperty("Authorization", "OAuth,oauth_consumer_key=\"" + key + "\",oauth_signature=\""
    					+ URLEncoder.encode(signupRequest(rurl, secret), "UTF-8") + "\"");
		conn.setRequestProperty("x-app-name", appName);
		conn.setRequestProperty("x-api-version", "3.8");
	}
	
	private void sendRequestBodyIfSetted(HttpURLConnection conn) throws IOException {
		if (null != body) {
            OutputStream out = conn.getOutputStream();
            out.write(body.getBytes("UTF-8"));
            out.close();
		}
	}
	
	private void receiveResponseFromServer(HttpURLConnection conn) throws IOException {
		byte[] data = null;

		try
		{
			String gzip = conn.getRequestProperty("Accept-Encoding");
			if( gzip != null && gzip.contains("gzip") )
			{
				InputStream stream = conn.getInputStream();
				if( stream != null && stream.available() > 0 )
				{
					BufferedReader reader = null;
					try
					{
						GZIPInputStream gzipStream =  new GZIPInputStream(stream);
						Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
						reader = new BufferedReader(decoder);
						String text = null;
						String res = "";
						while ((text = reader.readLine()) != null)
						{
							res += text;
						}

						response = res;
					}
					catch (Exception e)
					{
						throw e;
					}
					finally
					{
						if( reader != null )
						{
							reader.close();
						}
					}
				}
			}
			else
			{
				data = getBytesFromInputStream(conn.getInputStream());
				if( data != null )
				{
					response = new String(data, "UTF-8");
				}

			}
        }
		catch(Exception e)
		{
        	try {
        		data = getBytesFromInputStream(conn.getErrorStream());
		        if( data != null )
		        {
			        errorMsg = new String(data, "UTF-8");
			        errorMsg = new String(data, "UTF-8");
		        }
        	} catch(Exception ex) {}
        }
	}
	
	private byte[] getBytesFromInputStream(InputStream is) throws IOException {
		int len;
	    int size = 1024;
	    byte[] result;

	    if (is instanceof ByteArrayInputStream) {
	    	size = is.available();
	    	result = new byte[size];
	    	len = is.read(result, 0, size);
	    } else {
	    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    	result = new byte[size];
	    	while ((len = is.read(result, 0, size)) != -1) {
	    		bos.write(result, 0, len);
	    	}
	    	result = bos.toByteArray();
	    }
	    
	    is.close();
	    
	    return result;
	}

	public String getResponse() { return response; }
	
	private String buildRequest(HashMap<String, String> map)
	{
		String request = "";
		Iterator<String> iterator = map.keySet().iterator();
	    Integer i = 0;
		while (iterator.hasNext())
	    {
	    	String key = iterator.next();
	    	request = request.concat(key + "=" + map.get(key));
	    	if( i < map.size() - 1){ request = request.concat("&"); }
	    	i++;
	    }
		return request;
	}
	
	private String signupRequest(String rurl, String secretkey)
	{
		String signature = "";
		try
		{
			String encodedURL = URLEncoder.encode(rurl, "UTF-8");
			Mac mac = Mac.getInstance("HmacSHA1");
			SecretKeySpec secret = new SecretKeySpec(secretkey.getBytes(),"HmacSHA1");
			mac.init(secret);
			byte[] digest = mac.doFinal(encodedURL.getBytes());
			signature = new sun.misc.BASE64Encoder().encode(digest);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
	    
		return signature;
	}
	
	private String hashMD5(String md5)
	{
		try {
	        java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
	        byte[] array = md.digest(md5.getBytes());
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < array.length; ++i) {
	          sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
	       }
	        return sb.toString();
	    } catch (java.security.NoSuchAlgorithmException e) {
	    }
	    return null;
	}
	
	public String getRequestUrl()
	{
		return rurl;
	}

	public String getErrorMessage()
	{
		return errorMsg;
	}
}

class DefaultTrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

    @Override
    public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
}

