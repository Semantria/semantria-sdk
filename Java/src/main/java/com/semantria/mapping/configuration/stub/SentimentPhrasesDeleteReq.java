package com.semantria.mapping.configuration.stub;

import com.semantria.mapping.configuration.SentimentPhrase;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="phrases")
public final class SentimentPhrasesDeleteReq
{
    private List<String> sentimentPhrases = new ArrayList<String>();

	public SentimentPhrasesDeleteReq() {}

	public SentimentPhrasesDeleteReq(List<SentimentPhrase> sentimentPhrases)
	{
		if( sentimentPhrases != null && !sentimentPhrases.isEmpty() )
		{
			for (SentimentPhrase sentimentPhrase : sentimentPhrases)
			{
				this.sentimentPhrases.add( sentimentPhrase.getName() );
			}
		}
	}

	@XmlElement(name="phrase")
    public List<String> getSentimentPhrases() { return sentimentPhrases; }
    public void setSentimentPhrases(List<String> sentimentPhrases) { this.sentimentPhrases = sentimentPhrases; }
}
