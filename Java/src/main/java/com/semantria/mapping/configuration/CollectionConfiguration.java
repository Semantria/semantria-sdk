package com.semantria.mapping.configuration;

/*
 * Class to store process configuration, mapped from DB request
 */

import javax.xml.bind.annotation.XmlElement;

public class CollectionConfiguration
{
    private Boolean themes = true;
    private Boolean mentions = false;
    private Boolean facets = true;
    private Boolean attributes = true;
    private Boolean named_entities = true;
    private Boolean user_entities = true;
    private Boolean query_topics = true;
    private Boolean concept_topics = false;

    public CollectionConfiguration(){}

    public CollectionConfiguration(Boolean themes, Boolean mentions, Boolean facets, Boolean attributes, Boolean named_entities, Boolean user_entities, Boolean query_topics, Boolean concept_topics) {
        this.themes = themes;
        this.mentions = mentions;
        this.facets = facets;
        this.attributes = attributes;
        this.named_entities = named_entities;
        this.user_entities = user_entities;
        this.query_topics = query_topics;
        this.concept_topics = concept_topics;
    }

    @XmlElement(name="themes")
    public Boolean getThemes() {
        return themes;
    }

    @XmlElement(name="mentions")
    public Boolean getMentions() {
        return mentions;
    }

    @XmlElement(name="facets")
    public Boolean getFacets() {
        return facets;
    }

    @XmlElement(name="model_sentiment")
    public Boolean getAttributes() {
        return attributes;
    }

    @XmlElement(name="named_entities")
    public Boolean getNamedEntities() {
        return named_entities;
    }

    @XmlElement(name="user_entities")
    public Boolean getUserEntities() {
        return user_entities;
    }

    @XmlElement(name="query_topics")
    public Boolean getQueryTopics() {
        return query_topics;
    }

    @XmlElement(name="concept_topics")
    public Boolean getConceptTopics() {
        return concept_topics;
    }

    public void setThemes(Boolean themes) {
        this.themes = themes;
    }

    public void setMentions(Boolean mentions) {
        this.mentions = mentions;
    }

    public void setFacets(Boolean facets) {
        this.facets = facets;
    }

    public void setAttributes(Boolean attributes) {
        this.attributes = attributes;
    }

    public void setNamedEntities(Boolean named_entities) {
        this.named_entities = named_entities;
    }

    public void setUserEntities(Boolean user_entities) {
        this.user_entities = user_entities;
    }

    public void setQueryTopics(Boolean query_topics) {
        this.query_topics = query_topics;
    }

    public void setConceptTopics(Boolean concept_topics) {
        this.concept_topics = concept_topics;
    }
}