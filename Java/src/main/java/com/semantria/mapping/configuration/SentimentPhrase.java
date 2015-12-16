package com.semantria.mapping.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.sql.Timestamp;

@XmlRootElement(name="document")
public class SentimentPhrase {
    private String id = null;
    private String name;
    private Float weight;
    private transient Timestamp modified = null;

    public SentimentPhrase() {}

	public SentimentPhrase(String name, Float weight)
	{
		this.name = name;
		this.weight = weight;
	}

    @XmlElement(name="id")
    public String getId() { return id; }
	@XmlElement(name="name")
    public String getName() { return name; }
    @XmlElement(name="weight")
    public Float getWeight() { return weight; }
    @XmlElement(name="modified")
    public Timestamp getModified() { return modified; }

    public void setId(String id) { this.id = id; }
    public void setName( String title ) { this.name = title; }
    public void setWeight(Float weight) { this.weight = weight; }
    public void setModified(Timestamp modified) { this.modified = modified; }
}
