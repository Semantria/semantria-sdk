package com.semantria.utils;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.semantria.mapping.output.statistics.StatsInterval;
import com.google.common.base.MoreObjects;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;
import java.util.zip.GZIPInputStream;


public final class AuthRequest {

	private String method = "GET";
	private String url = "";
	private HashMap<String, String> params = null;
	private String body = null;
	private Integer status = 0;
	private String key = "";
	private String secret = "";
	private String response = "";
	private String appName = "Java/4.2.101/";
	private String apiVersion = "";
	private String errorMsg = null;
	private boolean useCompression = false;
	private Map<String, String> httpHeaders = new HashMap<>();
	final private int CONNECTION_TIMEOUT = 120000;


	public static AuthRequest getInstance(String url, String method) {
		return new AuthRequest(url, method);
	}

	private AuthRequest(String url, String method) {
        if (method != null) {
            method = method.toUpperCase();
        }
		this.url = url;
		this.method = method;
		this.params = new HashMap<String, String>();
	}

	public AuthRequest key(String key) {
		this.key = key;
		return this;
	}

	public AuthRequest secret(String secret) {
		try {
			this.secret = hashMD5(secret);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Can't create hash from secret", e);
		}
		return this;
	}

	public AuthRequest body(String body) {
		if (body != null) {
			this.body = body;
		}
		return this;
	}

	public AuthRequest config_id(String config_id) {
		this.setParam("config_id", config_id);
		return this;
	}

	public AuthRequest headers(Map<String, String> headers) {
		httpHeaders.putAll(headers);
		return this;
	}

	private void setParam(String key, String value) {
		if (value == null) {
			this.params.remove(key);
		} else {
			this.params.put(key, value);
		}
	}

	public AuthRequest interval(StatsInterval interval) {
		if (interval != null) {
			this.setParam("interval", interval.name());
		}
		return this;
	}

	public AuthRequest groupBy(String groupBy) {
		this.setParam("group", groupBy);
		return this;
	}

	public AuthRequest from(String from) {
		this.setParam("from", from);
		return this;
	}

	public AuthRequest to(String to) {
		this.setParam("to", to);
		return this;
	}

	public AuthRequest job_id(String job_id) {
		this.setParam("job_id", job_id);
		return this;
	}

	public AuthRequest language(String language) {
		this.setParam("language", language);
		return this;
	}

	public AuthRequest useCompression(boolean useCompression) {
		this.useCompression = useCompression;
		return this;
	}

	public AuthRequest appName(String appName) {
		if (appName == null) {
			appName = "";
		}
		String type = this.url.contains("json") ? "JSON" : "XML";
		this.appName = appName + "/" + this.appName + type;
		return this;
	}

	public AuthRequest apiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
		return this;
	}

	public String getMethod() {
		return method;
	}

	public Integer doRequest() {
		HttpURLConnection conn = null;
		try {
			response = null;
			errorMsg = null;
			initSSLContext();
			conn = getOAuthSignedConnection();
			conn.setConnectTimeout(CONNECTION_TIMEOUT);
			conn.connect();
			sendRequestBodyIfSet(conn);
			receiveResponseFromServer(conn);
			status = conn.getResponseCode();
		} catch (Exception e) {
			System.err.println("Error performing request: " + e);
			e.printStackTrace();
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		return status;
	}

	private void initSSLContext() throws java.security.GeneralSecurityException {
		SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
		SSLContext.setDefault(ctx);
	}

	public String getFullUrl() {
		if (params.isEmpty()) {
			return url;
		} else {
			return url + "?" + buildRequest(params);
		}
	}

	private HttpURLConnection getOAuthSignedConnection() throws IOException, NoSuchAlgorithmException, InvalidKeyException {
		setOAuthParameters();

		String fullUrl = getFullUrl();
		URL urlObj = new URL(fullUrl);
		HttpURLConnection conn = null;
		if (this.url.startsWith("https")) {
			conn = (HttpsURLConnection) urlObj.openConnection();
			((HttpsURLConnection) conn).setHostnameVerifier(new HostnameVerifier() {
				@Override
				public boolean verify(String arg0, SSLSession arg1) {
					return true;
				}
			});
		} else {
			conn = (HttpURLConnection) urlObj.openConnection();
		}

		setRequestProperties(conn, fullUrl);

		return conn;
	}

	private void setOAuthParameters() {
		if (!key.isEmpty()) {
			if (params == null) {
				params = new HashMap<String, String>();
			}
			params.put("oauth_nonce", Long.toString(new Random().nextLong() & 0xffffffffL));
			params.put("oauth_consumer_key", key);
			params.put("oauth_signature_method", "HMAC-SHA1");
			params.put("oauth_timestamp", Long.toString(System.currentTimeMillis() / 1000));
			params.put("oauth_version", "1.0");
		}
	}

	private void setRequestProperties(HttpURLConnection conn, String fullUrl) throws IOException, InvalidKeyException, NoSuchAlgorithmException {
		for (Map.Entry<String, String> entry : httpHeaders.entrySet()) {
			conn.setRequestProperty(entry.getKey(), entry.getValue());
		}

		conn.setRequestProperty("Connection", "close");
		conn.setDoOutput(true);
		conn.setRequestMethod(method);

		if (method.equals("GET") && useCompression) {
			conn.setRequestProperty("Accept-Encoding", "gzip,deflate");
		}
		if (!key.isEmpty() && !secret.isEmpty()) {
			conn.setRequestProperty("Authorization", getAuthorizationHeader(fullUrl));
		}
		conn.setRequestProperty("x-app-name", appName);
		if (! Strings.isNullOrEmpty(apiVersion)) {
			conn.setRequestProperty("x-api-version", apiVersion);
		}
	}

	public String getAuthorizationHeader(String fullUrl) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		return String.format("OAuth,oauth_consumer_key=\"%s\",oauth_signature=\"%s\"",
				key, URLEncoder.encode(signRequest(fullUrl, secret), "UTF-8"));
	}

	private void sendRequestBodyIfSet(HttpURLConnection conn) throws IOException {
		if (null != body) {
			OutputStream out = conn.getOutputStream();
			out.write(body.getBytes("UTF-8"));
			out.close();
		}
	}

	private void receiveResponseFromServer(HttpURLConnection conn) throws IOException {
		byte[] data = null;

		try {
			String gzip = conn.getRequestProperty("Accept-Encoding");
			if (gzip != null && gzip.contains("gzip")) {
				InputStream stream = conn.getInputStream();
				if (stream != null && stream.available() > 0) {
					BufferedReader reader = null;
					try {
						GZIPInputStream gzipStream = new GZIPInputStream(stream);
						Reader decoder = new InputStreamReader(gzipStream, "UTF-8");
						reader = new BufferedReader(decoder);
						StringBuilder result = new StringBuilder();
						String text = null;
						while ((text = reader.readLine()) != null) {
							result.append(text);
						}
						response = result.toString();
					} finally {
						if (reader != null) {
							reader.close();
						}
					}
				}
			} else {
				data = getBytesFromInputStream(conn.getInputStream());
				if (data != null) {
					response = new String(data, "UTF-8");
				}

			}
		} catch (Exception e) {
			try {
				data = getBytesFromInputStream(conn.getErrorStream());
				if (data != null) {
					errorMsg = new String(data, "UTF-8");
				}
			} catch (Exception e2) {
				System.err.println("Error: error occurred while getting error message: " + e2.toString());
			}
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

	public String getResponse() {
		return response;
	}

	private String buildRequest(HashMap<String, String> map) {
		String request = "";
		Iterator<String> iterator = map.keySet().iterator();
		Integer i = 0;
		while (iterator.hasNext()) {
			String key = iterator.next();
			request = request.concat(key + "=" + map.get(key));
			if (i < map.size() - 1) {
				request = request.concat("&");
			}
			i++;
		}
		return request;
	}

	private String signRequest(String fullUrl, String secretkey) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
		String encodedURL = URLEncoder.encode(fullUrl, "UTF-8");
		Mac mac = Mac.getInstance("HmacSHA1");
		SecretKeySpec secret = new SecretKeySpec(secretkey.getBytes(), "HmacSHA1");
		mac.init(secret);
		byte[] digest = mac.doFinal(encodedURL.getBytes());
		String signature = java.util.Base64.getEncoder().encodeToString(digest);
		return signature;
	}

	private String hashMD5(String md5) throws NoSuchAlgorithmException {
		java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
		byte[] array = md.digest(md5.getBytes());
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < array.length; ++i) {
			sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
		}
		return sb.toString();
	}

	public String getRequestUrl() {
		return url;
	}

	public String getErrorMessage() {
		return errorMsg;
	}

	public Integer getStatus() {
		return status;
	}

	public String getMessageFromJsonErrorMessage(String key) {
		Map<String,Object> map = new Gson().fromJson(errorMsg, Map.class);
		if (map.containsKey(key)) {
			return (String) map.get(key);
		} else {
			return errorMsg;
		}
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.omitNullValues()
				.add("method", method)
				.add("url", url)
				.add("status", status)
				.add("errorMsg", errorMsg)
				.toString();
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

