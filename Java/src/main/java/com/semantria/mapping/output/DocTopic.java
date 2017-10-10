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
	private List<SentimentMentionPhrase> sentiment_phrases = null;

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
	@XmlElement(name = "sentiment_phrases")
	public List<SentimentMentionPhrase> getSentimentPhrases() { return sentiment_phrases; }

	@XmlElementWrapper(name = "topics")
	@XmlElement(name = "topic")
	public List<DocTopic> getTopics() { return topics; }
	@XmlElementWrapper(name = "mentions")
	@XmlElement(name = "mention")
	public List<Mention> getMentions() { return mentions; }


	public void setTitle(String value) { title = value; }
	public void setType(String value) { type = value; }
	public void setHitCount(Integer value) { hitcount = value; }
	public void setSentimentScore(Float value) { sentiment_score = value; }
	public void setStrengthScore(Float value) { strength_score = value; }
	public void setSentimentPolarity(String value) { sentiment_polarity = value; }
	public void setSentimentPhrases(List<SentimentMentionPhrase> value) { sentiment_phrases = value; }
	public void setTopics(List<DocTopic> value) { topics = value; }
	public void setMentions(List<Mention> value) { mentions = value; }
}
