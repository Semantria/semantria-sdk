package com.semantria.mapping.output.features;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by George on 2/11/2015.
 */

public class APISettings
{
    private Boolean sentiment_phrases;
    private Boolean user_entities;
    private Boolean user_categories;
    private Boolean queries;
    private Boolean blacklist;

    public APISettings() {};

    @XmlElement(name="sentiment_phrases")
    public Boolean getSentimentPhrases() { return sentiment_phrases; }
    @XmlElement(name="user_entities")
    public Boolean getUserEntities() { return user_entities; }
    @XmlElement(name="user_categories")
    public Boolean getUserCategories() { return user_categories; }
    @XmlElement(name = "queries")
    public Boolean getQueries() { return queries; }
    @XmlElement(name = "blacklist")
    public Boolean getBlacklist() { return blacklist; }

    public void setSentimentPhrases(Boolean phrases) { this.sentiment_phrases = phrases; }
    public void setUserEntities(Boolean entities) { this.user_entities = entities; }
    public void setUserCategories(Boolean categories) { this.user_categories = categories; }
    public void setQueries(Boolean queries) { this.queries = queries; }
    public void setBlacklist(Boolean blacklist) { this.blacklist = blacklist; }
}