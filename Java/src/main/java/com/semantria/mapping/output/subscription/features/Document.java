package com.semantria.mapping.output.subscription.features;

import javax.xml.bind.annotation.XmlElement;

/**
 * Subscription' s document features mapping object
 */
public class Document
{
    private Boolean model_sentiment = null;
	private Boolean intentions = null;
	private Boolean summary = null;
	private Boolean themes = null;
	private Boolean named_entities = null;
	private Boolean user_entities = null;
	private Boolean entity_themes = null;
	private Boolean named_relations = null;
	private Boolean user_relations = null;

	private Boolean query_topics = null;
	private Boolean concept_topics = null;
	private Boolean sentiment_phrases = null;
	private Boolean pos_tagging = null;
	private Boolean language_detection = null;
	private Boolean phrases_detection = null;
	private Boolean mentions = null;
	private Boolean opinions = null;

    private Boolean auto_categories = null;

	public Document() {}

    @XmlElement(name = "model_sentiment")
    public Boolean getModelSentiment() { return model_sentiment == null ? false : model_sentiment; }
	@XmlElement(name = "intentions")
	public Boolean getIntentions() { return intentions == null ? false : intentions; }
	@XmlElement(name = "summary")
	public Boolean getSummary() { return summary == null ? false : summary; }
	@XmlElement(name = "themes")
	public Boolean getThemes() { return themes == null ? false : themes; }
	@XmlElement(name = "named_entities")
	public Boolean getNamedEntities() { return named_entities == null ? false : named_entities; }
	@XmlElement(name = "user_entities")
	public Boolean getUserEntities() { return user_entities == null ? false : user_entities; }
	@XmlElement(name = "entity_themes")
	public Boolean getEntityThemes() { return entity_themes == null ? false : entity_themes; }
	@XmlElement(name = "named_relations")
	public Boolean getNamedRelations() { return named_relations == null ? false : named_relations; }
	@XmlElement(name = "user_relations")
	public Boolean getUserRelations() { return user_relations == null ? false : user_relations; }
	@XmlElement(name = "query_topics")
	public Boolean getQueryTopics() { return query_topics == null ? false : query_topics; }
	@XmlElement(name = "concept_topics")
	public Boolean getConceptTopics() { return concept_topics == null ? false : concept_topics; }
	@XmlElement(name = "sentiment_phrases")
	public Boolean getSentimentPhrases() { return sentiment_phrases == null ? false : sentiment_phrases; }
	@XmlElement(name = "pos_tagging")
	public Boolean getPosTagging() { return pos_tagging == null ? false : pos_tagging; }
	@XmlElement(name = "language_detection")
	public Boolean getLanguageDetection() { return language_detection == null ? false : language_detection; }
	@XmlElement(name = "phrases_detection")
	public Boolean getPhrasesDetection() { return phrases_detection; }
	@XmlElement(name = "mentions")
	public Boolean getMentions() { return mentions; }
	@XmlElement(name = "opinions")
	public Boolean getOpinions() { return opinions; }
    @XmlElement(name = "auto_categories")
    public Boolean getAutoCategories() { return auto_categories; }

    public void setModelSentiment(Boolean model_sentiment) { this.model_sentiment = model_sentiment; }
	public void setIntentions(Boolean intentions) { this.intentions = intentions; }
	public void setSummary(Boolean summary) { this.summary = summary; }
	public void setThemes(Boolean themes) { this.themes = themes; }
	public void setNamedEntities(Boolean named_entities) { this.named_entities = named_entities; }
	public void setUserEntities(Boolean user_entities) { this.user_entities = user_entities; }
	public void setEntityThemes(Boolean entity_themes) { this.entity_themes = entity_themes; }
	public void setNamedRelations(Boolean named_relations) { this.named_relations = named_relations; }
	public void setUserRelations(Boolean user_relations) { this.user_relations = user_relations; }
	public void setQueryTopics(Boolean query_topics) { this.query_topics = query_topics; }
	public void setConceptTopics(Boolean concept_topics) { this.concept_topics = concept_topics; }
	public void setSentimentPhrases(Boolean sentiment_phrases) { this.sentiment_phrases = sentiment_phrases; }
	public void setPosTagging(Boolean pos_tagging) { this.pos_tagging = pos_tagging; }
	public void setLanguageDetection(Boolean language_detection) { this.language_detection = language_detection; }
	public void setPhrasesDetection(Boolean phrases_detection) { this.phrases_detection = phrases_detection; }
    public void setMentions(Boolean mentions) { this.mentions = mentions; }
    public void setOpinions(Boolean opinions) { this.opinions = opinions; }
    public void setAutoCategories(Boolean auto_categories) { this.auto_categories = auto_categories; }
}
