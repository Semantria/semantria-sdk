package com.semantria.proxies;

import com.semantria.interfaces.IUpdateProxy;
import com.semantria.objects.configuration.SentimentPhrase;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="phrases")
public class SentimentPhraseUpdateProxy implements IUpdateProxy<SentimentPhrase> {
    private List<SentimentPhrase> added;
    private List<String> removed;

    public SentimentPhraseUpdateProxy()
    {
        added = new ArrayList<SentimentPhrase>();
        removed = new ArrayList<String>();
    }

    public void add(SentimentPhrase sentimentPhrase)
    {
        added.add(sentimentPhrase);
    }

    public void remove(SentimentPhrase sentimentPhrase)
    {
        removed.add(sentimentPhrase.getTitle());
    }

    public void update(SentimentPhrase sentimentPhrase)
    {
        added.add(sentimentPhrase);
    }

	public void clone(SentimentPhrase item) {}

    public void removeAll() {
        added.clear();
        removed.clear();
        removed.add(REMOVE_ALL_ITEMS_MARK);
    }

    @XmlElementWrapper(name="added")
    @XmlElement(name="phrase")
    public List<SentimentPhrase> getAdded() { return added; }

    @XmlElementWrapper(name="removed")
    @XmlElement(name="phrase")
    public List<String> getRemoved() { return removed; }
}
