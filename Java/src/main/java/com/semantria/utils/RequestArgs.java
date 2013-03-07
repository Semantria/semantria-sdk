package com.semantria.utils;

public class RequestArgs 
{
	private String r_method;
	private String r_message;
	private String r_url;
	
	public RequestArgs(String method, String url, String message)
	{
		r_method = method;
		r_url = url;
		r_message = message;
	}
	
	public String getMethod() { return r_method; }
	public String getUrl() { return r_url; }
	public String getMessage() { return r_message; }
	
	public void setMethod(String method) { r_method = method; }
	public void setUrl(String url) { r_url = url; }
	public void setMessage(String message) { r_message = message; }
}
