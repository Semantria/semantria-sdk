package com.semantria.mapping.output.statistics;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "statistics")
public class StatisticsOverall extends StatisticsBase {
    private String config_id = null;
    private String consumer_name = null;
    private String latest_used_app = null;

    @XmlElement(name = "config_id")
    public String getConfig_id() { return config_id; }
    @XmlElement(name = "consumer_name")
    public String getConsumer_name() { return consumer_name; }
    @XmlElement(name = "latest_used_app")
    public String getLatestUsedApp() { return latest_used_app;}

    public void setConfig_id(String config_id) { this.config_id = config_id; }
    public void setConsumer_name(String consumer_name) { this.consumer_name = consumer_name; }
    public void setLatestUsedApp(String latest_used_app) { this.latest_used_app = latest_used_app; }
}
