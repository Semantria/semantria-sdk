public class Category {
	private String name;
	private Double weight;
	private List<String> samples;

	public Category(String name, Double weight) {
		this.name = name;
		this.weight = weight;
		this.samples = new List<String>();
	}

	public String getName() { return name; }
	public Double getWeight() { return weight; }
	public List<String> getSamples() { return samples; }
	
	public void setName(String name) { this.name = name; }
	public void setWeight(Double weight) { this.weight = weight; }
	public void setSamples(List<String> samples) { this.samples = samples; }
}