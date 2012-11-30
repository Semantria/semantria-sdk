public class UserEntity {
	private String name;
	private String type;
	
	public UserEntity(String name, String type) {
		this.name = name;
		this.type = type;
	}
	
	public String getName() { return name; }
	public String getType() { return type; }
	
	public void setName(String name) { this.name = name; }
	public void setType(String type) { this.type = type; }
}