package com.semantria.mapping.configuration;

/*
 * Class to store process configuration, mapped from DB request
 */

import javax.xml.bind.annotation.XmlElement;

public class DocumentConfiguration
{
    private Boolean detect_language = true;
    private Boolean auto_categories = true;
    private Boolean sentiment_phrases = true;
    private Boolean themes = false;
    private Boolean mentions = false;
    private Boolean named_entities = true;
    private Boolean user_entities = true;
    private Boolean opinions = false;
    private Boolean relations = false;
    private Boolean query_topics = true;
    private Boolean concept_topics = false;
    private Boolean model_sentiment = false;
    private Boolean intentions = false;
    private Integer summary_size = 3;
    private String pos_types = null;

	public DocumentConfiguration(){}

    public DocumentConfiguration(Integer summary_size, Boolean detect_language, String pos_types, Boolean auto_categories,
                                 Boolean sentiment_phrases, Boolean themes, Boolean mentions, Boolean named_entities,
                                 Boolean user_entities, Boolean opinions, Boolean relations, Boolean query_topics,
                                 Boolean concept_topics, Boolean model_sentiment, Boolean intentions) {
        this.summary_size = summary_size;
        this.detect_language = detect_language;
        this.pos_types = pos_types;
        this.auto_categories = auto_categories;
        this.sentiment_phrases = sentiment_phrases;
        this.themes = themes;
        this.mentions = mentions;
        this.named_entities = named_entities;
        this.user_entities = user_entities;
        this.opinions = opinions;
        this.relations = relations;
        this.query_topics = query_topics;
        this.concept_topics = concept_topics;
        this.model_sentiment = model_sentiment;
        this.intentions = intentions;
    }

    @XmlElement(name="model_sentiment")
    public Boolean getModelSentiment() {
        return model_sentiment;
    }

	@XmlElement(name="intentions")
	public Boolean getIntentions() {
        return intentions;
    }

	@XmlElement(name="summary_size")
	public Integer getSummarySize() {
        return summary_size;
    }

	@XmlElement(name = "detect_language")
	public Boolean getDetectLanguage() {
        return detect_language;
    }

	@XmlElement(name = "pos_types")
	public String getPosTypes() {
        return pos_types;
    }

    @XmlElement(name = "auto_categories")
    public Boolean getAutoCategories() {
        return auto_categories;
    }

    @XmlElement(name = "sentiment_phrases")
    public Boolean getSentimentPhrases() {
        return sentiment_phrases;
    }

    @XmlElement(name = "themes")
    public Boolean getThemes() {
        return themes;
    }

    @XmlElement(name = "mentions")
    public Boolean getMentions() {
        return mentions;
    }

    @XmlElement(name = "named_entities")
    public Boolean getNamedEntities() {
        return named_entities;
    }

    @XmlElement(name = "user_entities")
    public Boolean getUserEntities() {
        return user_entities;
    }

    @XmlElement(name = "opinions")
    public Boolean getOpinions() {
        return opinions;
    }

    @XmlElement(name = "relations")
    public Boolean getRelations() {
        return relations;
    }

    @XmlElement(name = "query_topics")
    public Boolean getQueryTopics() {
        return query_topics;
    }

    @XmlElement(name = "concept_topics")
    public Boolean getConceptTopics() {
        return concept_topics;
    }

    public void setSummarySize(Integer summary_size) {
        this.summary_size = summary_size;
    }

    public void setDetectLanguage(Boolean detect_language) {
        this.detect_language = detect_language;
    }

    public void setPosTypes(String pos_types) {
        this.pos_types = pos_types;
    }

    public void setAutoCategories(Boolean auto_categories) {
        this.auto_categories = auto_categories;
    }

    public void setSentimentPhrases(Boolean sentiment_phrases) {
        this.sentiment_phrases = sentiment_phrases;
    }

    public void setThemes(Boolean themes) {
        this.themes = themes;
    }

    public void setMentions(Boolean mentions) {
        this.mentions = mentions;
    }

    public void setNamedEntities(Boolean named_entities) {
        this.named_entities = named_entities;
    }

    public void setUserEntities(Boolean user_entities) {
        this.user_entities = user_entities;
    }

    public void setOpinions(Boolean opinions) {
        this.opinions = opinions;
    }

    public void setRelations(Boolean relations) {
        this.relations = relations;
    }

    public void setQueryTopics(Boolean query_topics) {
        this.query_topics = query_topics;
    }

    public void setConceptTopics(Boolean concept_topics) {
        this.concept_topics = concept_topics;
    }

    public void setModelSentiment(Boolean model_sentiment) {
        this.model_sentiment = model_sentiment;
    }

    public void setIntentions(Boolean intentions) {
        this.intentions = intentions;
    }
}