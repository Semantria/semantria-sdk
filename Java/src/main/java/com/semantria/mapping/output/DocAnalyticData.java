
package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="document")
public final class DocAnalyticData 
{
	private String id = null;
	private TaskStatus status = null;
	private String config_id = null;
	private List<DocEntity> entities = null;
	private List<DocTopic> topics = null;
	private List<DocTheme> themes = null;
	private List<DocPhrase> phrases = null;
	private Float sentiment_score = null;
	private String summary = null;
	private String language = null;
	private List<Sentence> details = null;
	private List<Relation> relations = null;
	private String source_text = null;
	private String sentiment_polarity = null;

	public DocAnalyticData() { };

	@XmlElement
	public String getId() { return id; }
	@XmlElement(name="config_id")
	public String getConfigId() { return config_id; }
	@XmlElement
	public TaskStatus getStatus() { return status; }
	@XmlElementWrapper(name="entities")
	@XmlElement(name="entity")
	public List<DocEntity> getEntities() { return entities; }
	@XmlElementWrapper(name="topics")
	@XmlElement(name="topic")
	public List<DocTopic> getTopics() { return topics; }
	@XmlElementWrapper(name="themes")
	@XmlElement(name="theme")
	public List<DocTheme> getThemes() { return themes; }
	@XmlElementWrapper(name="phrases")
	@XmlElement(name="phrase")
	public List<DocPhrase> getPhrases() { return phrases; }
	@XmlElement(name="sentiment_score")
	public Float getSentimentScore() { return sentiment_score; }
	@XmlElement(name="summary")
	public String getSummary() { return summary; }
	@XmlElement(name = "language")
	public String getLanguage() { return language; }
	@XmlElement(name = "sentiment_polarity")
	public String getSentimentPolarity() { return sentiment_polarity; }
	@XmlElementWrapper(name = "details")
	@XmlElement(name = "sentence")
	public List<Sentence> getDetails() { return details; }
	@XmlElementWrapper(name = "relations")
	@XmlElement(name = "relation")
	public List<Relation> getRelations() { return relations; }
	@XmlElement(name = "source_text")
	public String getSourceText() { return source_text; }

	public void setId(String id) { this.id = id; }
	public void setConfigId(String id) { this.config_id = id; }
	public void setStatus(TaskStatus status) { this.status = status; }
	public void setEntities(List<DocEntity> entities) { this.entities = entities; }
	public void setTopics(List<DocTopic> topic) { topics = topic; }
	public void setThemes(List<DocTheme> theme) { themes = theme; }
	public void setSentimentScore(Float score) { sentiment_score = score; }
	public void setPhrases(List<DocPhrase> phrase) { phrases = phrase; }
	public void setSummary(String summary) { this.summary = summary; }
	public void setLanguage(String language) { this.language = language; }
	public void setDetails(List<Sentence> details) { this.details = details; }
	public void setRelations(List<Relation> relations) { this.relations = relations; }
	public void setSourceText(String source_text) { this.source_text = source_text; }
	public void setSentimentPolarity(String sentiment_polarity) { this.sentiment_polarity = sentiment_polarity; }
}

