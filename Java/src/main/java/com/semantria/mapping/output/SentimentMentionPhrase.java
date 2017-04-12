package com.semantria.mapping.output;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.SerializedName;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class SentimentMentionPhrase {

    @SerializedName("score")
    private float sentimentScore;
    private int modified;
    private int type;
    private MentionPhrase phrase;
    @SerializedName("supporting_phrases")
    private List<MentionPhrase> supportingPhrases;

	public SentimentMentionPhrase() {
    }

    public String getTitle() {
        return (phrase != null) ? phrase.getTitle() : null;
    }

    @XmlElement(name = "phrase")
    public MentionPhrase getPhrase() {
        return phrase;
    }

    @XmlElement(name = "score")
    public float getSentimentScore() {
        return sentimentScore;
    }

    @XmlElement(name = "modified")
    public int getModified() {
        return modified;
    }

    @XmlElement(name = "type")
    public int getType() {
        return type;
    }

    @XmlElement(name = "supporting_phrases")
    public List<MentionPhrase> getSupportingPhrases() {
        return supportingPhrases;
    }

    public void setSentimentScore(float value) {
        sentimentScore = value;
    }

    public void setModified(int value) {
        modified = value;
    }

    public void setSupportingPhrases(List<MentionPhrase> value) {
        supportingPhrases = value;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("title", getTitle())
                .add("score", getSentimentScore())
                .toString();
    }

}
