package com.semantria.utils;

public class ResponseArgs 
{
	private Integer r_status;
	private String r_message;
	
	public ResponseArgs(Integer status, String message)
	{
		r_status = status;
		r_message = message;
	}
	
	public String getMessage() { return r_message; }
	public Integer getStatus() { return r_status; }
	
	public void setMessage(String message) { r_message = message; }
	public void setStatus(Integer status) { r_status = status; }
}
