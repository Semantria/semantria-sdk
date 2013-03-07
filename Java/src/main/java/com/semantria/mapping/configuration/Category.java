package com.semantria.mapping.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public final class Category 
{
	private String name = "";
	private Float weight = new Float(0);
	private List<String> samples;

	@XmlElement(name="name")
	public String getName() { return name; }
	@XmlElement(name="weight")
	public Float getWeight() { return weight; }
	@XmlElementWrapper(name="samples")
	@XmlElement(name="sample")
	public List<String> getSamples() { return samples; }
	
	public void setName(String category) { name = category; }
	public void setSamples(List<String> samples) { this.samples = samples; }
	public void setWeight(Float weight) { this.weight = weight; }
}
