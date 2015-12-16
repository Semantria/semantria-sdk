package com.semantria.mapping.configuration;

import javax.xml.bind.annotation.XmlElement;
import java.sql.Timestamp;

public class BlacklistItem {
    private String id = null;
    private String name = "";
    private transient Timestamp modified = null;

    @XmlElement(name="id")
    public String getId() { return id; }
    @XmlElement(name="name")
    public String getName() { return name; }
    @XmlElement(name="modified")
    public Timestamp getModified() { return modified; }

    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setModified(Timestamp modified) { this.modified = modified; }
}
