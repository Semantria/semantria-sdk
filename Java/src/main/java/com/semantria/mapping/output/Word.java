package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;

public class Word
{
	private String type = null;
	private String tag = null;
	private String title = null;
	private String stemmed = null;
	private Float sentiment_score = null;

	@XmlElement(name = "type")
	public String getType() { return type; }
	@XmlElement(name = "title")
	public String getTitle() { return title; }
	@XmlElement(name = "stemmed")
	public String getStemmed() { return stemmed; }
	@XmlElement(name = "tag")
	public String getTag() { return tag; }
	@XmlElement(name = "sentiment_score")
	public Float getSentimentScore() { return sentiment_score; }

	public void setType(String type) { this.type = type; }
	public void setTitle(String title) { this.title = title; }
	public void setStemmed(String stemmed) { this.stemmed = stemmed; }
	public void setTag(String tag) { this.tag = tag; }
	public void setSentimentScore(Float sentiment_score) { this.sentiment_score = sentiment_score; }
}