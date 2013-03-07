package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;

public class CollTheme
{
    private String title = null;
    private Float sentiment_score = null;
    private Integer phrases_count = null;
    private Integer themes_count = null;
	private String sentiment_polarity = null;

	@XmlElement(name="title")
    public String getTitle() { return title; }
    @XmlElement(name="sentiment_score")
    public Float getSentimentScore() { return sentiment_score; }
    @XmlElement(name="phrases_count")
    public Integer getPhrasesCount() { return phrases_count; }
    @XmlElement(name="themes_count")
    public Integer getThemesCount() { return themes_count; }
	@XmlElement(name = "sentiment_polarity")
	public String getSentimentPolarity() { return sentiment_polarity; }

    public void setTitle(String title) { this.title = title; }
    public void setSentimentScore(Float sentiment_score) { this.sentiment_score = sentiment_score; }
    public void setPhrasesCount(Integer phrases_count) {this.phrases_count = phrases_count;}
    public void setThemesCount(Integer themes_count) {this.themes_count = themes_count;}
	public void setSentimentPolarity(String sentiment_polarity) { this.sentiment_polarity = sentiment_polarity; }
}
