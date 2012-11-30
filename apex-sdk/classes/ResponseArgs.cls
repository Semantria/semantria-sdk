public class ResponseArgs {
	private Integer status;
	private String message;
	
	public ResponseArgs(Integer status, String message) {
		this.status = status;
		this.message = message;
	}
	
	public String getMessage() { 
		return message; 
	}
	
	public Integer getStatus() { 
		return status; 
	}
	
	public void setMessage(String message) { 
		this.message = message; 
	}
	
	public void setStatus(Integer status) {
		this.status = status; 
	}
}