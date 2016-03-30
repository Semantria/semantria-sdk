package com.semantria.mapping.output.features;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by George on 2/11/2015.
 */

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
    private Boolean theme_mentions;
    private Boolean named_entities;
    private Boolean user_entities;
    private Boolean entity_relations;
    private Boolean entity_opinions;
    private Boolean entity_mentions;
    private Boolean entity_themes;
    private Boolean auto_categories;
    private Boolean user_categories;
    private Boolean queries;

    public DetailedModeFeatures() {};

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
    @XmlElement(name="theme_mentions")
    public Boolean getThemeMentions() { return theme_mentions; }
    @XmlElement(name="named_entities")
    public Boolean getNamedEntities() { return named_entities; }
    @XmlElement(name="user_entities")
    public Boolean getUserEntities() { return user_entities; }
    @XmlElement(name="entity_relations")
    public Boolean getNamedRelations() { return entity_relations; }
    @XmlElement(name="entity_opinions")
    public Boolean getEntityOpinions() { return entity_opinions; }
    @XmlElement(name="entity_mentions")
    public Boolean getEntityMentions() { return entity_mentions; }
    @XmlElement(name="entity_themes")
    public Boolean getEntityThemes() { return entity_themes; }
    @XmlElement(name="auto_categories")
    public Boolean getAutoCategories() { return auto_categories; }
    @XmlElement(name="user_categories")
    public Boolean getUserCategories() { return user_categories; }
    @XmlElement(name = "queries")
    public Boolean getQueries() { return queries; }

    public void setLanguageDetection(Boolean feature) { this.language_detection = feature; }
    public void setSentiment(Boolean feature) { this.sentiment = feature; }
    public void setSentimentPhrases(Boolean feature) { this.sentiment_phrases = feature; }
    public void setModelSentiment(Boolean feature) { this.model_sentiment = feature; }
    public void setPOSTagging(Boolean feature) { this.pos_tagging = feature; }
    public void setSumarization(Boolean feature) { this.summarization = feature; }
    public void setIntentions(Boolean feature) { this.intentions = feature; }
    public void setThemes(Boolean feature) { this.themes = feature; }
    public void setThemeMentions(Boolean feature) { this.theme_mentions = feature; }
    public void setNamedEntities(Boolean feature) { this.named_entities = feature; }
    public void setUserEntities(Boolean feature) { this.user_entities = feature; }
    public void setEntityRelations(Boolean feature) { this.entity_relations = feature; }
    public void setEntityOpinions(Boolean feature) { this.entity_opinions = feature; }
    public void setEntityMentions(Boolean feature) { this.entity_mentions = feature; }
    public void setEntityThemes(Boolean feature) { this.entity_themes = feature; }
    public void setAutoCategories(Boolean feature) { this.auto_categories = feature; }
    public void setUserCategories(Boolean feature) { this.user_categories = feature; }
    public void setQueries(Boolean feature) { this.queries = feature; }
}