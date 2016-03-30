package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;

public class DocPhrase
{
    private String title = null;
    private Float sentiment_score = null;
    private String negating_phrase = null;
    private Boolean is_negated = null;
	private String sentiment_polarity = null;
	private String type = null;

    @XmlElement(name="title")
    public String getTitle() { return title; }
    @XmlElement(name="sentiment_score")
    public Float getSentimentScore() { return sentiment_score; }
    @XmlElement(name="negating_phrase")
    public String getNegatingPhrase() { return negating_phrase; }
    @XmlElement(name="is_negated")
    public Boolean getIsNegated() { return is_negated; }
	@XmlElement(name = "sentiment_polarity")
	public String getSentimentPolarity() { return sentiment_polarity; }
	@XmlElement(name = "type")
	public String getType() { return type; }

    public void setTitle(String title) {this.title = title;}
    public void setSentimentScore(Float sentiment_score) {this.sentiment_score = sentiment_score;}
    public void setNegatingPhrase(String negating_phrase) {this.negating_phrase = negating_phrase;}
    public void setIsNegated(Boolean is_negated) {this.is_negated = is_negated;}
	public void setSentimentPolarity(String sentiment_polarity) { this.sentiment_polarity = sentiment_polarity; }
	public void setType(String type) { this.type = type; }
}
