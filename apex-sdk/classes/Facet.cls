public class Facet {
	private String label = null;
	private Integer count = null;
	private Integer negative_count = null;
	private Integer positive_count = null;
	private Integer neutral_count = null;
	List<Attribute> attributes = null;
	
	public String getLabel(){ return label; }
	public Integer getCount(){ return count; }
	public Integer getNegativeCount(){ return negative_count; }
	public Integer getPositiveCount(){ return positive_count; }
	public Integer getNeutralCount(){ return neutral_count; }
	public List<Attribute> getAttributes() { return attributes; }
	
	public void setLabel(String label){ this.label = label; }
	public void setCount(Integer count){ this.count = count; }
	public void setNegativeCount(Integer negativeCount){ this.negative_count = negativeCount; }
	public void setPositiveCount(Integer positiveCount){ this.positive_count = positiveCount; }
	public void setNeutralCount(Integer neutralCount){ this.neutral_count = neutralCount; }
	public void setAttributes(List<Attribute> attributes) { this.attributes = attributes; }
}