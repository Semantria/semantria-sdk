package com.semantria.mapping.configuration;

import javax.xml.bind.annotation.XmlElement;

public class TaxonomyTopic {
    private String id = "";
    private String type = "";

    @XmlElement(name="id")
    public String getId() { return id; }
    @XmlElement(name="type")
    public String getType() { return type; }

    public void setId(String id) { this.id = id; }
    public void setType(String type) { this.type = type; }
}
