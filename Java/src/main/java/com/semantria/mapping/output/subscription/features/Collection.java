package com.semantria.mapping.output.subscription.features;

import javax.xml.bind.annotation.XmlElement;

/**
 * Subscription' s collection features mapping object
 */
public class Collection
{
	private Boolean facets = null;
	private Boolean themes = null;
	private Boolean named_entities = null;
    private Boolean user_entities = null;
	private Boolean query_topics = null;
	private Boolean concept_topics = null;
    private Boolean mentions = null;

	public Collection() {}

	@XmlElement(name="facets")
	public Boolean getFacets() { return facets == null ? false : facets; }
	@XmlElement(name = "themes")
	public Boolean getThemes() { return themes == null ? false : themes; }
	@XmlElement(name = "named_entities")
	public Boolean getNamedEntities() { return named_entities == null ? false : named_entities; }
    @XmlElement(name = "user_entities")
    public Boolean getUserEntities() { return user_entities == null ? false : user_entities; }
	@XmlElement(name = "query_topics")
	public Boolean getQueryTopics() { return query_topics == null ? false : query_topics; }
	@XmlElement(name = "concept_topics")
	public Boolean getConceptTopics() { return concept_topics == null ? false : concept_topics; }
    @XmlElement(name = "mentions")
    public Boolean getMentions() { return mentions == null ? false : mentions; }

    public void setFacets(Boolean facets_and_attributes) { this.facets = facets_and_attributes; }
	public void setThemes(Boolean themes) { this.themes = themes; }
	public void setNamedEntities(Boolean named_entities) { this.named_entities = named_entities; }
    public void setUserEntities(Boolean user_entities) { this.user_entities = user_entities; }
	public void setQueryTopics(Boolean query_topics) { this.query_topics = query_topics; }
	public void setConceptTopics(Boolean concept_topics) { this.concept_topics = concept_topics; }
    public void setMentions(Boolean mentions) { this.mentions = mentions; }
}
