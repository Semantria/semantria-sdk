package com.semantria.objects.mapping;

/*
 * Class to store process configuration, mapped from DB request
 */

import javax.xml.bind.annotation.XmlElement;

public class Stub_Collection 
{
	private Integer facets_limit = null;
	private Integer facet_atts_limit = null;
    private Integer themes_limit = null;
    private Integer query_topics_limit = null;
    private Integer concept_topics_limit = null;
    private Integer named_entities_limit = null;

	public Stub_Collection(){}
	
	public Stub_Collection(Integer facets_limit, Integer facet_atts_limit, Integer themes_limit, Integer query_topics_limit,
                           Integer concept_topics_limit, Integer named_entities_limit)
	{
        this.facets_limit = facets_limit;
		this.facet_atts_limit = facet_atts_limit;
        this.themes_limit = themes_limit;
        this.query_topics_limit = query_topics_limit;
        this.concept_topics_limit = concept_topics_limit;
        this.named_entities_limit = named_entities_limit;
	}

	@XmlElement(name="facets_limit")
	public Integer getFacetsLimit() { return facets_limit; }
	@XmlElement(name="facet_atts_limit")
	public Integer getFacetAttributesLimit() { return facet_atts_limit; }
    @XmlElement(name="themes_limit")
    public Integer getThemesLimit() { return themes_limit; }
    @XmlElement(name="query_topics_limit")
    public Integer getQueryTopicsLimit() { return query_topics_limit; }
    @XmlElement(name="concept_topics_limit")
    public Integer getConceptTopicsLimit() { return concept_topics_limit; }
    @XmlElement(name="named_entities_limit")
    public Integer getNamedEntitiesLimit() { return named_entities_limit; }

	public void setFacetsLimit(Integer limit){  facets_limit = limit; }
	public void setFacetAttributesLimit(Integer limit) { facet_atts_limit = limit; }
    public void setThemesLimit(Integer limit) { themes_limit = limit; }
    public void setQueryTopicsLimit( Integer limit ) { query_topics_limit = limit; }
    public void setConceptTopicsLimit( Integer limit ) { concept_topics_limit = limit; }
    public void setNamedEntitiesLimit( Integer limit ) { named_entities_limit = limit; }
}