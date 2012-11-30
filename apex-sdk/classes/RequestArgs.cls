public class RequestArgs {
	private String method;
	private String message;
	private String url;
	
	public RequestArgs(String method, String url, String message) {
		this.method = method;
		this.url = url;
		this.message = message;
	}
	
	public String getMethod() { return this.method; }
	public String getUrl() { return this.url; }
	public String getMessage() { return this.message; }
	
	public void setMethod(String method) { this.method = method; }
	public void setUrl(String url) { this.url = url; }
	public void setMessage(String message) { this.message = message; }
}