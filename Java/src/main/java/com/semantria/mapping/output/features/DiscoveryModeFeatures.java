package com.semantria.mapping.output.features;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by George on 2/11/2015.
 */

public class DiscoveryModeFeatures
{
    private Boolean facets;
    private Boolean facet_attributes;
    private Boolean facet_mentioins;
    private Boolean themes;
    private Boolean theme_mentions;
    private Boolean named_entities;
    private Boolean user_entities;
    private Boolean entity_mentions;
    private Boolean user_categories;
    private Boolean queries;

    public DiscoveryModeFeatures() {};

    @XmlElement(name="facets")
    public Boolean getFacets() { return facets; }
    @XmlElement(name="facet_attributes")
    public Boolean getFacetAttributes() { return facet_attributes; }
    @XmlElement(name="facet_mentioins")
    public Boolean getFacetMentions() { return facet_mentioins; }
    @XmlElement(name="themes")
    public Boolean getThemes() { return themes; }
    @XmlElement(name="theme_mentions")
    public Boolean getThemeMentions() { return theme_mentions; }
    @XmlElement(name="named_entities")
    public Boolean getNamedEntities() { return named_entities; }
    @XmlElement(name="user_entities")
    public Boolean getUserEntities() { return user_entities; }
    @XmlElement(name="entity_mentions")
    public Boolean getEntityMentions() { return entity_mentions; }
    @XmlElement(name="user_categories")
    public Boolean getUserCategories() { return user_categories; }
    @XmlElement(name = "queries")
    public Boolean getQueries() { return queries; }

    public void setFacets(Boolean feature) { this.facets = feature; }
    public void setFacetAttributes(Boolean feature) { this.facet_attributes = feature; }
    public void setFacetMentions(Boolean feature) { this.facet_mentioins = feature; }
    public void setThemes(Boolean feature) { this.themes = feature; }
    public void setThemeMentions(Boolean feature) { this.theme_mentions = feature; }
    public void setNamedEntities(Boolean feature) { this.named_entities = feature; }
    public void setUserEntities(Boolean feature) { this.user_entities = feature; }
    public void setEntityMentions(Boolean feature) { this.entity_mentions = feature; }
    public void setUserCategories(Boolean feature) { this.user_categories = feature; }
    public void setQueries(Boolean feature) { this.queries = queries; }
}