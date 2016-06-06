package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;

public class Template {
    private String id = null;
    private String config_id = null;
    private Boolean is_free = null;
    private String language = null;
    private String name = null;
    private String description = null;
    private String type = null;
    private String version = null;

    public Template() { }

    @XmlElement(name = "id")
    public String getId() { return id; }
    @XmlElement(name = "config_id")
    public String getConfig_id() { return config_id; }
    @XmlElement(name = "is_free")
    public Boolean getIs_free() { return is_free; }
    @XmlElement(name = "language")
    public String getLanguage() { return language; }
    @XmlElement(name = "name")
    public String getName() { return name; }
    @XmlElement(name = "description")
    public String getDescription() { return description; }
    @XmlElement(name = "type")
    public String getType() { return type; }
    @XmlElement(name = "version")
    public String getVersion() { return version; }

    public void setId(String id) {this.id = id;}
    public void setConfig_id(String config_id) {this.config_id = config_id;}
    public void setIs_free(Boolean is_free) {this.is_free = is_free;}
    public void setLanguage(String language) {this.language = language;}
    public void setName(String name) {this.name = name;}
    public void setDescription(String description) {this.description = description;}
    public void setType(String type) {this.type = type;}
    public void setVersion(String version) {this.version = version;}
}
