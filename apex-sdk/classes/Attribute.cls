public class Attribute {
	private String label;
	private Integer count;
	
	public Attribute(String label, Integer count) {
		this.label = label;
		this.count = count;
	}
	
	public String getLabel(){ return label; }
	public Integer getCount(){ return count; }
	
	public void setLabel(String label){ this.label = label; }
	public void setCount(Integer count){ this.count = count; }
}