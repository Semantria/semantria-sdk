package com.semantria.mapping.configuration.stub;

import com.semantria.mapping.configuration.SentimentPhrase;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="phrases")
public final class SentimentPhrases
{
    private List<SentimentPhrase> sentimentPhrases = new ArrayList<SentimentPhrase>();

	public SentimentPhrases() {}

	public SentimentPhrases(List<SentimentPhrase> sentimentPhrases)
	{
		this.sentimentPhrases = sentimentPhrases;
	}

	@XmlElement(name="phrase")
    public List<SentimentPhrase> getSentimentPhrases() { return sentimentPhrases; }
    public void setSentimentPhrases(List<SentimentPhrase> sentimentPhrases) { this.sentimentPhrases = sentimentPhrases; }
}
