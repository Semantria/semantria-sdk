package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;

public final class DocModelSentiment
{
    private String model_name = null;
    private Float mixed_score = null;
    private Float negative_score = null;
    private Float neutral_score = null;
    private Float positive_score = null;
    private String sentiment_polarity = null;

    @XmlElement(name="model_name")
    public String getModelName() { return model_name; }
    @XmlElement(name="mixed_score")
    public Float getMixedScore() { return mixed_score; }
    @XmlElement(name="negative_score")
    public Float getNegativeScore() { return negative_score; }
    @XmlElement(name="neutral_score")
    public Float getNeutralScore() { return neutral_score; }
    @XmlElement(name="positive_score")
    public Float getPositiveScore() { return positive_score; }
    @XmlElement(name="sentiment_polarity")
    public String getSentimentPolarity() { return sentiment_polarity; }

    public void setModelName(String model_name) { this.model_name = model_name; }
    public void setMixedScore(Float mixed_score) { this.mixed_score = mixed_score; }
    public void setNegativeScore(Float negative_score) { this.negative_score = negative_score; }
    public void setNeutralScore(Float neutral_score) { this.neutral_score = neutral_score; }
    public void setPositiveScore(Float positive_score) { this.positive_score = positive_score; }
    public void setSentimentPolarity(String sentiment_polarity) { this.sentiment_polarity = sentiment_polarity; }
}
