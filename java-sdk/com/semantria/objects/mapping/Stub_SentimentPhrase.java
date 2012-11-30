package com.semantria.objects.mapping;

import com.semantria.objects.configuration.Query;
import com.semantria.objects.configuration.SentimentPhrase;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="phrases")
public final class Stub_SentimentPhrase {
    private List<SentimentPhrase> sentimentPhrases = new ArrayList<SentimentPhrase>();

    @XmlElement(name="phrase")
    public List<SentimentPhrase> getSentimentPhrases() { return sentimentPhrases; }
    public void setSentimentPhrases(List<SentimentPhrase> sentimentPhrases) { this.sentimentPhrases = sentimentPhrases; }
}
