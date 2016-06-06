package com.semantria.mapping.configuration;

/*
 * Class to store process configuration, mapped from DB request
 */

import javax.xml.bind.annotation.XmlElement;

public class CollConfiguration
{
	private Integer facets_limit = null;
	private Integer facet_atts_limit = null;
	private Integer themes_limit = null;
	private Integer named_entities_limit = null;
    private Integer user_entities_limit = null;
	private Integer query_topics_limit = null;
	private Integer concept_topics_limit = null;
	private Integer facet_mentions_limit = null;
	private Integer attribute_mentions_limit = null;
    private Integer theme_mentions_limit = null;
    private Integer named_mentions_limit = null;
    private Integer user_mentions_limit = null;

	public CollConfiguration(){}

	public CollConfiguration(Integer facets_limit, Integer facet_atts_limit, Integer themes_limit, Integer named_entities_limit, Integer user_entities_limit, Integer query_topics_limit, Integer concept_topics_limit, Integer facet_mentions_limit, Integer attribute_mentions_limit, Integer theme_mentions_limit, Integer named_mentions_limit, Integer user_mentions_limit)
	{
		this.facets_limit = facets_limit;
		this.facet_atts_limit = facet_atts_limit;
		this.themes_limit = themes_limit;
		this.named_entities_limit = named_entities_limit;
        this.user_entities_limit = user_entities_limit;
		this.query_topics_limit = query_topics_limit;
		this.concept_topics_limit = concept_topics_limit;
		this.facet_mentions_limit = facet_mentions_limit;
		this.attribute_mentions_limit = attribute_mentions_limit;
        this.theme_mentions_limit = theme_mentions_limit;
        this.named_mentions_limit = named_mentions_limit;
        this.user_mentions_limit = user_mentions_limit;
	}

	@XmlElement(name="facets_limit")
	public Integer getFacetsLimit() { return facets_limit; }
	@XmlElement(name="facet_atts_limit")
	public Integer getFacetAttributesLimit() { return facet_atts_limit; }
	@XmlElement(name="themes_limit")
	public Integer getThemesLimit() { return themes_limit; }
	@XmlElement(name="named_entities_limit")
	public Integer getNamedEntitiesLimit(){ return named_entities_limit; }
    @XmlElement(name="user_entities_limit")
    public Integer getUserEntitiesLimit(){ return user_entities_limit; }
	@XmlElement(name="concept_topics_limit")
	public Integer getConceptTopicsLimit() { return concept_topics_limit; }
	@XmlElement(name="query_topics_limit")
	public Integer getQueryTopicsLimit() { return query_topics_limit; }
	@XmlElement(name="facet_mentions_limit")
	public Integer getFacetMentionsLimit() { return facet_mentions_limit; }
	@XmlElement(name = "attribute_mentions_limit")
	public Integer getAttributeMentionsLimit() { return attribute_mentions_limit; }
    @XmlElement(name = "theme_mentions_limit")
    public Integer getThemeMentionsLimit() { return theme_mentions_limit; }
    @XmlElement(name = "named_mentions_limit")
    public Integer getNamedMentionsLimit() { return named_mentions_limit; }
    @XmlElement(name = "user_mentions_limit")
    public Integer getUserMentionsLimit() { return user_mentions_limit; }

    public void setFacetsLimit(Integer limit) { facets_limit = limit; }
	public void setFacetAttributesLimit(Integer limit) { facet_atts_limit = limit; }
	public void setThemesLimit(Integer limit) { themes_limit = limit; }
	public void setNamedEntitiesLimit(Integer limit) { named_entities_limit = limit; }
    public void setUserEntitiesLimit(Integer limit) { user_entities_limit = limit; }
	public void setConceptTopicsLimit(Integer limit) { concept_topics_limit = limit; }
	public void setQueryTopicsLimit(Integer limit) { query_topics_limit = limit; }
	public void setFacetMentionsLimit(Integer limit) { facet_mentions_limit = limit; }
	public void setAttributeMentionsLimit(Integer limit) { attribute_mentions_limit = limit; }
    public void setThemeMentionsLimit(Integer limit) { theme_mentions_limit = limit; }
    public void setNamedMentionsLimit(Integer limit) { named_mentions_limit = limit; }
    public void setUserdMentionsLimit(Integer limit) { user_mentions_limit = limit; }
}