
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
    private String job_id = null;
    private DocModelSentiment model_sentiment = null;
	private List<DocIntention> intentions = null;
	private List<DocEntity> entities = null;
	private List<DocTopic> topics = null;
	private List<DocTheme> themes = null;
	private List<DocPhrase> phrases = null;
	private Float sentiment_score = null;
	private String summary = null;
	private String language = null;
	private Float language_score = null;
	private List<Sentence> details = null;
	private List<Relation> relations = null;
	private String source_text = null;
	private String sentiment_polarity = null;
	private List<Opinion> opinions = null;
    private String tag = null;
    private List<DocCategory> auto_categories = null;

	public DocAnalyticData() { };

	@XmlElement
	public String getId() { return id; }
	@XmlElement(name="config_id")
	public String getConfigId() { return config_id; }
    @XmlElement(name="job_id")
    public String getJobId() { return job_id; }
	@XmlElement
	public TaskStatus getStatus() { return status; }
    @XmlElement(name="model_sentiment")
    public DocModelSentiment getModelSentiment() { return model_sentiment; }
	@XmlElementWrapper(name="intentions")
	@XmlElement(name="intention")
	public List<DocIntention> getIntentions() { return intentions; }
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
	@XmlElement(name = "language_score")
	public Float getLanguageScore() { return language_score; }
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
	@XmlElementWrapper(name = "opinions")
	@XmlElement(name = "opinion")
	public List<Opinion> getOpinions() { return opinions; }
    @XmlElement(name = "tag")
    public String getTag() { return tag; }
    @XmlElementWrapper(name = "auto_categories")
    @XmlElement(name = "category")
    public List<DocCategory> getAutoCategories() { return auto_categories; }

	public void setId(String id) { this.id = id; }
	public void setConfigId(String configId) { this.config_id = configId; }
    public void setJobId(String jobId) { this.config_id = jobId; }
	public void setStatus(TaskStatus status) { this.status = status; }
    public void setModelSentiment(DocModelSentiment model_sentiment) { this.model_sentiment = model_sentiment; }
    public void setIntentions(List<DocIntention> intentions) { this.intentions = intentions; }
	public void setEntities(List<DocEntity> entities) { this.entities = entities; }
	public void setTopics(List<DocTopic> topic) { topics = topic; }
	public void setThemes(List<DocTheme> theme) { themes = theme; }
	public void setSentimentScore(Float score) { sentiment_score = score; }
	public void setPhrases(List<DocPhrase> phrase) { phrases = phrase; }
	public void setSummary(String summary) { this.summary = summary; }
	public void setLanguage(String language) { this.language = language; }
	public void setLanguageScore(Float language_score) { this.language_score = language_score; }
	public void setDetails(List<Sentence> details) { this.details = details; }
	public void setRelations(List<Relation> relations) { this.relations = relations; }
	public void setSourceText(String source_text) { this.source_text = source_text; }
	public void setSentimentPolarity(String sentiment_polarity) { this.sentiment_polarity = sentiment_polarity; }
	public void setOpinions(List<Opinion> opinions) { this.opinions = opinions; }
    public void setTag(String tag) { this.tag = tag; }
    public void setAutoCategories(List<DocCategory> auto_categories) { this.auto_categories = auto_categories; }
}

