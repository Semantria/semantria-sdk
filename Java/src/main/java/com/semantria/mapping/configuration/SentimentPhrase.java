package com.semantria.mapping.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="document")
public class SentimentPhrase {
    private String name;
    private Float weight;

    public SentimentPhrase() {}

	public SentimentPhrase(String name, Float weight)
	{
		this.name = name;
		this.weight = weight;
	}

	@XmlElement(name="name")
    public String getName() { return name; }
    @XmlElement(name="weight")
    public Float getWeight() { return weight; }

    public void setName( String title ) { this.name = title; }
    public void setWeight(Float weight) { this.weight = weight; }
}
