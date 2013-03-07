package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;

public class CollTopic
{
    private String title;
    private String type;
    private Integer hitcount;
    private Float sentiment_score;
	private String sentiment_polarity;

	@XmlElement(name="title")
    public String getTitle() { return title; }
    @XmlElement(name="type")
    public String getType() { return type; }
    @XmlElement(name="hitcount")
    public Integer getHitCount() { return hitcount; }
    @XmlElement(name="sentiment_score")
    public Float getSentimentScore() { return sentiment_score; }
	@XmlElement(name = "sentiment_polarity")
	public String getSentimentPolarity() { return sentiment_polarity; }

    public void setTitle(String title) { this.title = title; }
    public void setType(String type) { this.type = type; }
    public void setHitCount(Integer hitCount) { this.hitcount = hitCount; }
    public void setSentimentScore(Float sentiment_score) { this.sentiment_score = sentiment_score; }
	public void setSentimentPolarity(String sentiment_polarity){ this.sentiment_polarity = sentiment_polarity; }
}
