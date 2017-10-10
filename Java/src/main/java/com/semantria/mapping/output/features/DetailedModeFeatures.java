package com.semantria.mapping.output.features;

import javax.xml.bind.annotation.XmlElement;


public class DetailedModeFeatures
{
    private Boolean language_detection;
    private Boolean sentiment;
    private Boolean sentiment_phrases;
    private Boolean model_sentiment;
    private Boolean pos_tagging;
    private Boolean summarization;
    private Boolean intentions;
    private Boolean themes;
    private Boolean mentions;
    private Boolean named_entities;
    private Boolean user_entities;
    private Boolean relations;
    private Boolean opinions;
    private Boolean auto_categories;
    private Boolean concept_topics;
    private Boolean query_topics;
    private Boolean taxonomy;

    public DetailedModeFeatures() {}

    @XmlElement(name="language_detection")
    public Boolean getLanguageDetection() { return language_detection; }
    @XmlElement(name="sentiment")
    public Boolean getSentiment() { return sentiment; }
    @XmlElement(name="sentiment_phrases")
    public Boolean getSentimentPhrases() { return sentiment_phrases; }
    @XmlElement(name="model_sentiment")
    public Boolean getModelSentiment() { return model_sentiment; }
    @XmlElement(name="pos_tagging")
    public Boolean getPOSTagging() { return pos_tagging; }
    @XmlElement(name="summarization")
    public Boolean getSumarization() { return summarization; }
    @XmlElement(name="intentions")
    public Boolean getIntentions() { return intentions; }
    @XmlElement(name="themes")
    public Boolean getThemes() { return themes; }
    @XmlElement(name="named_entities")
    public Boolean getNamedEntities() { return named_entities; }
    @XmlElement(name="user_entities")
    public Boolean getUserEntities() { return user_entities; }
    @XmlElement(name="relations")
    public Boolean getRelations() { return relations; }
    @XmlElement(name="opinions")
    public Boolean getOpinions() { return opinions; }
    @XmlElement(name="mentions")
    public Boolean getMentions() { return mentions; }
    @XmlElement(name="auto_categories")
    public Boolean getAutoCategories() { return auto_categories; }
    @XmlElement(name="concept_topics")
    public Boolean getConceptTopics() { return concept_topics; }
    @XmlElement(name = "query_topics")
    public Boolean getQueryTopics() { return query_topics; }
    @XmlElement(name = "taxonomy")
    public Boolean getTaxonomy() { return taxonomy; }

    public void setLanguageDetection(Boolean feature) { this.language_detection = feature; }
    public void setSentiment(Boolean feature) { this.sentiment = feature; }
    public void setSentimentPhrases(Boolean feature) { this.sentiment_phrases = feature; }
    public void setModelSentiment(Boolean feature) { this.model_sentiment = feature; }
    public void setPOSTagging(Boolean feature) { this.pos_tagging = feature; }
    public void setSumarization(Boolean feature) { this.summarization = feature; }
    public void setIntentions(Boolean feature) { this.intentions = feature; }
    public void setThemes(Boolean feature) { this.themes = feature; }
    public void setNamedEntities(Boolean feature) { this.named_entities = feature; }
    public void setUserEntities(Boolean feature) { this.user_entities = feature; }
    public void setRelations(Boolean feature) { this.relations = feature; }
    public void setOpinions(Boolean feature) { this.opinions = feature; }
    public void setMentions(Boolean feature) { this.mentions = feature; }
    public void setAutoCategories(Boolean feature) { this.auto_categories = feature; }
    public void setConceptTopics(Boolean feature) { this.concept_topics = feature; }
    public void setQueryTopics(Boolean feature) { this.query_topics = feature; }
    public void setTaxonomy(Boolean feature) { this.taxonomy = feature; }
}