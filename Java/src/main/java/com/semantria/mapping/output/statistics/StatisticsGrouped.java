package com.semantria.mapping.output.statistics;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class StatisticsGrouped {
    private String config_id = null;
    private String config_name = null;
    private String consumer_name = null;
    private String user_email = null;
    private String edition_name = null;
    private String language = null;
    private String app_group = null;

    private List<StatisticsGroup> values = null;

    // Grouping related getters
    @XmlElement(name = "config_id")
    public String getConfig_id() { return config_id; }
    @XmlElement(name = "config_name")
    public String getConfig_name() { return config_name; }
    @XmlElement(name = "consumer_name")
    public String getConsumer_name() { return consumer_name; }
    @XmlElement(name = "user_email")
    public String getUser_email() { return user_email; }
    @XmlElement(name = "edition_name")
    public String getEdition_name() { return edition_name; }
    @XmlElement(name = "language")
    public String getLanguage() { return language; }
    @XmlElement(name = "app_group")
    public String getApp_group() { return app_group; }
    @XmlElementWrapper(name = "values")
    @XmlElement(name = "value")
    public List<StatisticsGroup> getValues() { return values; }

    // Grouping related setters
    public void setConfig_id(String config_id) { this.config_id = config_id; }
    public void setConfig_name(String config_name) { this.config_name = config_name; }
    public void setConsumer_name(String consumer_name) { this.consumer_name = consumer_name; }
    public void setUser_email(String user_email) { this.user_email = user_email; }
    public void setEdition_name(String edition_name) { this.edition_name = edition_name; }
    public void setLanguage(String language) { this.language = language; }
    public void setApp_group(String app_group) { this.app_group = app_group; }
    public void setValues(List<StatisticsGroup> values) { this.values = values; }
}
