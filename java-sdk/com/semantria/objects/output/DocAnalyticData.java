package com.semantria.objects.output;

import java.util.List;
import javax.xml.bind.annotation.*;
@XmlRootElement(name="document")
public final class DocAnalyticData 
{
	private String id;
	private String config_id;
	private TaskStatus status;
	private Float sentiment_score;
	private List<DocEntity> entities;
	private List<DocTopic> topics;
	private List<DocTheme> themes;
    private List<DocPhrase> phrases;
	private String summary;
	
	@XmlElement(name="id")
	public String getId() { return id; }
	@XmlElement(name="config_id")
	public String getConfigId() { return config_id; }
	@XmlElement(name="status")
	public TaskStatus getStatus() { return status; }
	@XmlElement(name="sentiment_score")
	public Float getSentimentScore() { return sentiment_score; }
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
	@XmlElement(name="summary")
	public String getSummary() { return summary; }
	
	public void setId(String aid) { id = aid;}
	public void setConfigId(String confId) { config_id = confId; }
	public void setStatus(TaskStatus astatus) { status = astatus; }
	public void setSentimentScore(Float ascore) { sentiment_score = ascore; }
	public void setEntities(List<DocEntity> sentities) { entities = sentities; }
	public void setTopics(List<DocTopic> atopics) { topics = atopics; }
	public void setThemes(List<DocTheme> athemes) { themes = athemes; }
    public void setPhrases(List<DocPhrase> phrases) { this.phrases = phrases; }
	public void setSummary(String asummary) { summary = asummary; }
	
}
