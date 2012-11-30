package com.semantria.objects.output;

import javax.xml.bind.annotation.XmlElement;

public class CollTopic {
    private String title;
    private String type;
    private Integer hitcount;
    private Float sentiment_score;

    @XmlElement(name="title")
    public String getTitle() { return title; }
    @XmlElement(name="type")
    public String getType() { return type; }
    @XmlElement(name="hitcount")
    public Integer getHitCount() { return hitcount; }
    @XmlElement(name="sentiment_score")
    public Float getSentimentScore() { return sentiment_score; }

    public void setTitle(String ttitle) { title = ttitle; }
    public void setType(String ttype) { type = ttype; }
    public void setHitCount(Integer thitCount) { hitcount = thitCount; }
    public void setSentimentScore(Float tscore) { sentiment_score = tscore; }
}
