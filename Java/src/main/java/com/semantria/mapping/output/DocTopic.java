package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public final class DocTopic
{
	private String title = null;
	private String type = null;
	private Integer hitcount = null;
	private Float sentiment_score = null;
	private Float strength_score = null;
	private String sentiment_polarity = null;

	private List<DocTopic> topics = null;
	private List<Mention> mentions = null;


	@XmlElement(name="title")
	public String getTitle() { return title; }
	@XmlElement(name="type")
	public String getType() { return type; }
	@XmlElement(name="hitcount")
	public Integer getHitCount() { return hitcount; }
	@XmlElement(name="sentiment_score")
	public Float getSentimentScore() { return sentiment_score; }
	@XmlElement(name="strength_score")
	public Float getStrengthScore() { return strength_score; }
	@XmlElement(name = "sentiment_polarity")
	public String getSentimentPolarity() { return sentiment_polarity; }

	@XmlElementWrapper(name = "topics")
	@XmlElement(name = "topic")
	public List<DocTopic> getTopics() { return topics; }
	@XmlElementWrapper(name = "mentions")
	@XmlElement(name = "mention")
	public List<Mention> getMentions() { return mentions; }


	public void setTitle(String ttitle) { title = ttitle; }
	public void setType(String ttype) { type = ttype; }
	public void setHitCount(Integer thitCount) { hitcount = thitCount; }
	public void setSentimentScore(Float tscore) { sentiment_score = tscore; }
	public void setStrengthScore(Float tscore) { strength_score = tscore; }
	public void setSentimentPolarity(String sentiment_polarity) { this.sentiment_polarity = sentiment_polarity; }
	public void setTopics(List<DocTopic> topics) { this.topics = topics; }
	public void setMentions(List<Mention> mentions) { this.mentions = mentions; }
}
