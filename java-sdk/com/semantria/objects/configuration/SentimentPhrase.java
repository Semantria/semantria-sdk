package com.semantria.objects.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="document")
public class SentimentPhrase {
    private String title;
    private Float weight;

    public SentimentPhrase() {}

    public SentimentPhrase(String title, Float weight) {
        this.title = title;
        this.weight = weight;
    }

    @XmlElement(name="title")
    public String getTitle() { return title; }
    @XmlElement(name="weight")
    public Float getWeight() { return weight; }

    public void setTitle( String title ) { this.title = title; }
    public void setWeight(Float weight) { this.weight = weight; }
}
