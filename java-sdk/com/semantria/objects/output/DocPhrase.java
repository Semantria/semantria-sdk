package com.semantria.objects.output;

import javax.xml.bind.annotation.XmlElement;

public class DocPhrase {
    private String title;
    private Float sentiment_score;
    private String negating_phrase;
    private Boolean is_negated;

    @XmlElement(name="title")
    public String getTitle() { return title; }
    @XmlElement(name="sentiment_score")
    public Float getSentimentScore() { return sentiment_score; }
    @XmlElement(name="negating_phrase")
    public String getNegatingPhrase() { return negating_phrase; }
    @XmlElement(name="is_negated")
    public Boolean getIsNegated() { return is_negated; }

    public void setTitle(String title) {this.title = title;}
    public void setSentimentScore(Float sentiment_score) {this.sentiment_score = sentiment_score;}
    public void setNegatingPhrase(String negating_phrase) {this.negating_phrase = negating_phrase;}
    public void setIsNegated(Boolean is_negated) {this.is_negated = is_negated;}
}
