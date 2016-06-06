package com.semantria.mapping.configuration;

/*
 * Class to store process configuration, mapped from DB request
 */

import javax.xml.bind.annotation.XmlElement;

public class DocConfiguration
{
    private Boolean model_sentiment = null;
	private Boolean intentions = null;
	private Integer entity_themes_limit = null;
	private Integer summary_limit = null;
	private Integer themes_limit = null;
	private Integer query_topics_limit = null;
	private Integer concept_topics_limit = null;
	private Integer named_entities_limit = null;
	private Integer user_entities_limit = null;
	private Integer phrases_limit = null;
	private Boolean detect_language = null;
	private Integer possible_phrases_limit = null;
	private String pos_types = null;
	private Integer named_relations_limit = null;
	private Integer user_relations_limit = null;
	private Integer named_mentions_limit = null;
    private Integer theme_mentions_limit = null;
	private Integer user_mentions_limit = null;
	private Integer named_opinions_limit = null;
	private Integer user_opinions_limit = null;
    private Integer auto_categories_limit = null;

	public DocConfiguration(){};

	public DocConfiguration(Boolean model_sentiment, Boolean intentions, Integer entity_themes_limit, Integer summary_limit, Integer themes_limit,
                            Integer query_topics_limit, Integer concept_topics_limit, Integer named_entities_limit, Integer user_entities_limit, Integer phrases_limit,
                            Boolean detect_language, Integer possible_phrases_limit, String pos_types, Integer named_relations_limit, Integer user_relations_limit,
                            Integer named_mentions_limit, Integer user_mentions_limit, Integer named_opinions_limit, Integer user_opinions_limit,
                            Integer theme_mentrions_limit, Integer auto_categories_limit)
	{
        this.model_sentiment = model_sentiment;
		this.intentions = intentions;
		this.entity_themes_limit = entity_themes_limit;
		this.summary_limit = summary_limit;
		this.themes_limit = themes_limit;
		this.query_topics_limit = query_topics_limit;
		this.concept_topics_limit = concept_topics_limit;
		this.named_entities_limit = named_entities_limit;
		this.user_entities_limit = user_entities_limit;
		this.phrases_limit = phrases_limit;
		this.detect_language = detect_language;
		this.possible_phrases_limit = possible_phrases_limit;
		this.pos_types = pos_types;
		this.named_relations_limit = named_relations_limit;
		this.user_relations_limit = user_relations_limit;
		this.named_mentions_limit = named_mentions_limit;
		this.user_mentions_limit = user_mentions_limit;
		this.named_opinions_limit = named_opinions_limit;
		this.user_opinions_limit = user_opinions_limit;
        this.theme_mentions_limit = theme_mentrions_limit;
        this.auto_categories_limit = auto_categories_limit;
	}

    @XmlElement(name="model_sentiment")
    public Boolean getModelSentiment() { return model_sentiment; }
	@XmlElement(name="intentions")
	public Boolean getIntentions() { return intentions; }
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
	public Integer getPhrasesLimit() { return phrases_limit; }
	@XmlElement(name = "detect_language")
	public Boolean getDetectLanguage() { return detect_language; }
	@XmlElement(name="possible_phrases_limit")
	public Integer getPossiblePhrasesLimit() { return possible_phrases_limit; }
	@XmlElement(name = "pos_types")
	public String getPosTypes() { return pos_types; }
	@XmlElement(name = "named_relations_limit")
	public Integer getNamedRelationsLimit() { return named_relations_limit; }
	@XmlElement(name = "user_relations_limit")
	public Integer getUserRelationsLimit() { return user_relations_limit; }
	@XmlElement(name = "named_mentions_limit")
	public Integer getNamedMentionsLimit() { return named_mentions_limit; }
	@XmlElement(name = "user_mentions_limit")
	public Integer getUserMentionsLimit() { return user_mentions_limit; }
    @XmlElement(name = "theme_mentions_limit")
    public Integer getThemeMentionsLimit() { return theme_mentions_limit; }
	@XmlElement(name = "named_opinions_limit")
	public Integer getNamedOpinionsLimit() { return named_opinions_limit; }
	@XmlElement(name = "user_opinions_limit")
	public Integer getUserOpinionsLimit() { return user_opinions_limit; }
    @XmlElement(name = "auto_categories_limit")
    public Integer getAutoCategoriesLimit() { return auto_categories_limit; }

    public void setModelSentiment(Boolean model_sentiment) { this.model_sentiment = model_sentiment; }
	public void setIntentions(Boolean intentions) { this.intentions = intentions; }
	public void setEntityThemesLimit(Integer limit) { entity_themes_limit = limit; }
	public void setSummaryLimit(Integer limit) { summary_limit = limit; }
	public void setThemesLimit(Integer limit){ themes_limit = limit; }
	public void setQueryTopicsLimit( Integer limit ) { query_topics_limit = limit; }
	public void setConceptTopicsLimit( Integer limit ) { concept_topics_limit = limit; }
	public void setNamedEntitiesLimit( Integer limit ) { named_entities_limit = limit; }
	public void setUserEntitiesLimit( Integer limit) { user_entities_limit = limit; }
	public void setPhrasesLimit( Integer limit ) { phrases_limit = limit; }
	public void setDetectLanguage(Boolean detect_language) { this.detect_language = detect_language; }
	public void setPossiblePhrasesLimit( Integer limit ) { possible_phrases_limit = limit; }
	public void setPosTypes(String pos_types) { this.pos_types = pos_types; }
	public void setNamedRelationsLimit(Integer limit) { named_relations_limit = limit; }
	public void setUserRelationsLimit(Integer limit) { user_relations_limit = limit; }
	public void setNamedMentionsLimit(Integer limit) { named_mentions_limit = limit; }
	public void setUserMentionsLimit(Integer limit) { user_mentions_limit = limit; }
    public void setThemeMentionsLimit(Integer limit) { theme_mentions_limit = limit; }
	public void setNamedOpinionsLimit(Integer limit) { named_opinions_limit = limit; }
	public void setUserOpinionsLimit(Integer limit) { user_opinions_limit = limit; }
    public void setAutoCategoriesLimit(Integer limit) { auto_categories_limit = limit; }
}