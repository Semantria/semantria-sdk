package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;

public class Opinion
{
	private String quotation = null;
	private String type = null;
	private String speaker = null;
	private String topic = null;
	private Float sentimentScore = null;
	private String sentimentPolarity = null;

	public Opinion() {};

	public Opinion(String quotation, String type, String speaker, String topic, Float sentimentScore, String sentimentPolatiry)
	{
		this.quotation = quotation;
		this.type = type;
		this.speaker = speaker;
		this.topic = topic;
		this.sentimentScore = sentimentScore;
		this.sentimentPolarity = sentimentPolatiry;
	}

	@XmlElement(name = "quotation")
	public String getQuotation() { return quotation; }
	@XmlElement(name = "type")
	public String getType() { return type; }
	@XmlElement(name = "speaker")
	public String getSpeaker() { return speaker; }
	@XmlElement(name = "topic")
	public String getTopic() { return topic; }
	@XmlElement(name = "sentiment_score")
	public Float getSentimentScore() { return sentimentScore; }
	@XmlElement(name = "sentiment_polarity")
	public String getSentimentPolarity() { return sentimentPolarity; }

	public void setQuotation(String quotation) { this.quotation = quotation; }
	public void setType(String type) { this.type = type; }
	public void setSpeaker(String speaker)  { this.speaker = speaker; }
	public void setTopic(String topic) { this.topic = topic; }
	public void setSentimentScore(Float sentimentScore) { this.sentimentScore = sentimentScore; }
	public void setSentimentPolarity(String sentimentPolarity) { this.sentimentPolarity = sentimentPolarity; }
}
