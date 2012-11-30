package com.semantria.objects.output;

import javax.xml.bind.annotation.XmlElement;

public class CollTheme {
    private String title;
    private Float sentiment_score;
    private Integer phrases_count;
    private Integer themes_count;

    @XmlElement(name="title")
    public String getTitle() { return title; }
    @XmlElement(name="sentiment_score")
    public Float getSentimentScore() { return sentiment_score; }
    @XmlElement(name="phrases_count")
    public Integer getPhrasesCount() { return phrases_count; }
    @XmlElement(name="themes_count")
    public Integer getThemesCount() { return themes_count; }

    public void setTitle(String ttitle) { title = ttitle; }
    public void setSentimentScore(Float tsentiment) { sentiment_score = tsentiment; }
    public void setPhrasesCount(Integer phrases_count) {this.phrases_count = phrases_count;}
    public void setThemesCount(Integer themes_count) {this.themes_count = themes_count;}
}
