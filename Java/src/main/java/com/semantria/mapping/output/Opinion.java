package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;

public class Opinion
{
	private String m_quotation = null;
	private String m_type = null;
	private String m_speaker = null;
	private String m_topic = null;
	private Float m_sentimentScore = null;
	private String m_sentimentPolarity = null;

	public Opinion() {};

	public Opinion(String quotation, String type, String speaker, String topic, Float sentimentScore, String sentimentPolatiry)
	{
		m_quotation = quotation;
		m_type = type;
		m_speaker = speaker;
		m_topic = topic;
		m_sentimentScore = sentimentScore;
		m_sentimentPolarity = sentimentPolatiry;
	}

	@XmlElement(name = "quotation")
	public String getQuotation() { return m_quotation; }
	@XmlElement(name = "type")
	public String getType() { return m_type; }
	@XmlElement(name = "speaker")
	public String getSpeaker() { return m_speaker; }
	@XmlElement(name = "topic")
	public String getTopic() { return m_topic; }
	@XmlElement(name = "sentiment_score")
	public Float getSentimentScore() { return m_sentimentScore; }
	@XmlElement(name = "sentiment_polarity")
	public String getSentimentPolarity() { return m_sentimentPolarity; }

	public void setQuotation(String quotation) { m_quotation = quotation; }
	public void setType(String type) { m_type = type; }
	public void setSpeaker(String speaker)  { m_speaker = speaker; }
	public void setTopic(String topic) { m_topic = topic; }
	public void setSentimentScore(Float sentimentScore) { m_sentimentScore = sentimentScore; }
	public void setSentimentPolarity(String sentimentPolarity) {this.m_sentimentPolarity = sentimentPolarity; }
}
