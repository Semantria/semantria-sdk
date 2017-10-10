package com.semantria.mapping.output.features;

import javax.xml.bind.annotation.XmlElement;


public class DiscoveryModeFeatures
{
    private Boolean facets;
    private Boolean attributes;
    private Boolean mentions;
    private Boolean themes;
    private Boolean named_entities;
    private Boolean user_entities;
    private Boolean concept_topics;
    private Boolean query_topics;
    private Boolean taxonomy;

    public DiscoveryModeFeatures() {}

    @XmlElement(name="facets")
    public Boolean getFacets() { return facets; }
    @XmlElement(name="attributes")
    public Boolean getAttributes() { return attributes; }
    @XmlElement(name="themes")
    public Boolean getThemes() { return themes; }
    @XmlElement(name="mentions")
    public Boolean getMentions() { return mentions; }
    @XmlElement(name="named_entities")
    public Boolean getNamedEntities() { return named_entities; }
    @XmlElement(name="user_entities")
    public Boolean getUserEntities() { return user_entities; }
    @XmlElement(name="concept_topics")
    public Boolean getConceptTopics() { return concept_topics; }
    @XmlElement(name = "query_topics")
    public Boolean getQueryTopics() { return query_topics; }
    @XmlElement(name = "taxonomy")
    public Boolean getTaxonomy() { return taxonomy; }

    public void setFacets(Boolean feature) { this.facets = feature; }
    public void setAttributes(Boolean feature) { this.attributes = feature; }
    public void setThemes(Boolean feature) { this.themes = feature; }
    public void setMentions(Boolean feature) { this.mentions = feature; }
    public void setNamedEntities(Boolean feature) { this.named_entities = feature; }
    public void setUserEntities(Boolean feature) { this.user_entities = feature; }
    public void setConceptTopics(Boolean feature) { this.concept_topics = feature; }
    public void setQueryTopics(Boolean feature) { this.query_topics = feature; }
    public void setTaxonomy(Boolean feature) { this.taxonomy = feature; }
}