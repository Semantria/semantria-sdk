package com.semantria.mapping.output;

import com.semantria.mapping.output.features.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by George on 2/11/2015.
 */
public class FeaturesSet
{
    private String id;
    private String language;
    private Boolean html_processing;
    private Boolean one_sentence_mode;
    private APISettings settings;
    private DetailedModeFeatures detailed_mode;
    private DiscoveryModeFeatures discovery_mode;

    public FeaturesSet() {};

    @XmlElement(name="id")
    public String getId() { return id; }
    @XmlElement(name="language")
    public String getLanguage() { return language; }
    @XmlElement(name="html_processing")
    public Boolean getHTMLProcessing() { return html_processing; }
    @XmlElement(name="one_sentence_mode")
    public Boolean getOneSentenceMode() { return one_sentence_mode; }
    @XmlElement(name = "settings")
    public APISettings getAPISettings() { return settings; }
    @XmlElement(name = "detailed_mode")
    public DetailedModeFeatures getDetailedModeFeatures() { return detailed_mode; }
    @XmlElement(name = "discovery_mode")
    public DiscoveryModeFeatures getDiscoveryModeFeatures() { return discovery_mode; }

    public void setId(String id) { this.id = id; }
    public void setLanguage(String language) { this.language = language; }
    public void setHTMLProcessing(Boolean htmlProcessing) { this.html_processing = htmlProcessing; }
    public void setAPISettings(APISettings settings) { this.settings = settings; }
    public void setDetailedModeFeatures(DetailedModeFeatures features) { this.detailed_mode = features; }
    public void setDiscoveryModeFeatures(DiscoveryModeFeatures features) { this.discovery_mode = features; }
}
