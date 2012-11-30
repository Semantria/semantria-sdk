package com.semantria.proxies;

import com.semantria.interfaces.IUpdateProxy;
import com.semantria.objects.configuration.Query;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="queries")
public final class QueryUpdateProxy implements IUpdateProxy<Query>
{
	private List<Query> added;
	private List<String> removed;
	
	public QueryUpdateProxy()
	{
		added = new ArrayList<Query>();
		removed = new ArrayList<String>();
	}
	
	public void add(Query query) 
	{
		added.add(query);
	}
	
	public void remove(Query id)
	{
		removed.add(id.getName());
	}
	
	public void update(Query query)
	{
		added.add(query);
	}

	public void clone(Query item) {}

    public void removeAll() {
        added.clear();
        removed.clear();
        removed.add(REMOVE_ALL_ITEMS_MARK);
    }

	@XmlElementWrapper(name="added")
	@XmlElement(name="query")
	public List<Query> getAdded() { return added; }
	@XmlElementWrapper(name="removed")
	@XmlElement(name="query")
	public List<String> getRemoved() { return removed; }
	
}
