package com.semantria.mapping.output.features;

import javax.xml.bind.annotation.XmlElement;


public class APISettings
{
    private Boolean sentiment_phrases;
    private Boolean user_entities;
    private Boolean concept_topics;
    private Boolean query_topics;
    private Boolean blacklist;
    private Boolean taxonomy;

    public APISettings() {}

    @XmlElement(name="sentiment_phrases")
    public Boolean getSentimentPhrases() { return sentiment_phrases; }
    @XmlElement(name="user_entities")
    public Boolean getUserEntities() { return user_entities; }
    @XmlElement(name="concept_topics")
    public Boolean getConceptTopics() { return concept_topics; }
    @XmlElement(name = "query_topics")
    public Boolean getQueryTopics() { return query_topics; }
    @XmlElement(name = "blacklist")
    public Boolean getBlacklist() { return blacklist; }
    @XmlElement(name = "taxonomy")
    public Boolean getTaxonomy() { return taxonomy; }

    public void setSentimentPhrases(Boolean phrases) { this.sentiment_phrases = phrases; }
    public void setUserEntities(Boolean entities) { this.user_entities = entities; }
    public void setConceptTopics(Boolean categories) { this.concept_topics = categories; }
    public void setQueryTopics(Boolean queries) { this.query_topics = queries; }
    public void setBlacklist(Boolean blacklist) { this.blacklist = blacklist; }
    public void setTaxonomy(Boolean taxonomy) { this.taxonomy = taxonomy; }
}