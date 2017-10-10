package com.semantria.mapping.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.sql.Timestamp;
import java.util.List;

public final class Category 
{
    private String id = null;
	private String name = "";
	private Float weight = 0.0f;
	private List<String> samples;
    private transient Timestamp modified = null;

    @XmlElement(name="id")
    public String getId() { return id; }
	@XmlElement(name="name")
	public String getName() { return name; }
	@XmlElement(name="weight")
	public Float getWeight() { return weight; }
	@XmlElementWrapper(name="samples")
	@XmlElement(name="sample")
	public List<String> getSamples() { return samples; }
    @XmlElement(name="modified")
    public Timestamp getModified() { return modified; }

    public void setId(String id) { this.id = id; }
	public void setName(String category) { name = category; }
	public void setSamples(List<String> samples) { this.samples = samples; }
	public void setWeight(Float weight) { this.weight = weight; }
    public void setModified(Timestamp modified) { this.modified = modified; }
}
