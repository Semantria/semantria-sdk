package com.semantria.objects.mapping;

/*
 * Class to store process configuration, mapped from DB request
 */

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

public class Stub_Document 
{
	
	private String config = null;
	private Integer entity_themes_limit = null;
	private Integer summary_limit = null;
    private Integer phrases_limit = null;

    private Integer themes_limit = null;
	private Integer query_topics_limit = null;
	private Integer concept_topics_limit = null;
	private Integer named_entities_limit = null;
	private Integer user_entities_limit = null;
	
	public Stub_Document() {}
	
	public Stub_Document(Integer entity_themes_limit, Integer summary_limit, Integer themes_limit,
                         Integer query_topics_limit, Integer concept_topics_limit, Integer named_entities_limit,
                         Integer user_entities_limit, Integer phrases_limit)
	{
		this.entity_themes_limit = entity_themes_limit;
		this.summary_limit = summary_limit;
		this.themes_limit = themes_limit;
		this.query_topics_limit = query_topics_limit;
		this.concept_topics_limit = concept_topics_limit;
		this.named_entities_limit = named_entities_limit;
		this.user_entities_limit = user_entities_limit;
        this.phrases_limit = phrases_limit;
	}

	@XmlTransient
	public String getConfig() { return config; }
	@XmlElement(name="entity_themes_limit")
	public Integer getEntityThemesLimit() { return entity_themes_limit; }
	@XmlElement(name="themes_limit")
	public Integer getThemesLimit() { return themes_limit; }
	@XmlElement(name="summary_limit")
	public Integer getSummaryLimit() { return summary_limit; }
	@XmlElement(name="query_topics_limit")
	public Integer getQueryTopicsLimit() { return query_topics_limit; }
	@XmlElement(name="concept_topics_limit")
	public Integer getConceptTopicsLimit() { return concept_topics_limit; }
	@XmlElement(name="named_entities_limit")
	public Integer getNamedEntitiesLimit() { return named_entities_limit; }
	@XmlElement(name="user_entities_limit")
	public Integer getUserEntitiesLimit() { return user_entities_limit; }
    @XmlElement(name="phrases_limit")
    public Integer getPhrasesLimit() {return phrases_limit;}
	
	public void setConfig(String id) { config = id; }
	public void setEntityThemesLimit(Integer limit) { entity_themes_limit = limit; }
	public void setSummaryLimit(Integer limit) { summary_limit = limit; }
	public void setThemesLimit(Integer limit) { themes_limit = limit; }
	public void setQueryTopicsLimit( Integer limit ) { query_topics_limit = limit; }
	public void setConceptTopicsLimit( Integer limit ) { concept_topics_limit = limit; }
	public void setNamedEntitiesLimit( Integer limit ) { named_entities_limit = limit; }
	public void setUserEntitiesLimit( Integer limit) {  user_entities_limit = limit; }
    public void setPhrasesLimit(Integer phrases_limit) {this.phrases_limit = phrases_limit;}
}