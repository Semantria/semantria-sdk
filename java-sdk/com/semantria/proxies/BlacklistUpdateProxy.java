package com.semantria.proxies;

import com.semantria.interfaces.IUpdateProxy;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="blacklist")
public final class BlacklistUpdateProxy implements IUpdateProxy<String> 
{
	private List<String> added;
	private List<String> removed;
	
	public BlacklistUpdateProxy()
	{
		added = new ArrayList<String>();
		removed = new ArrayList<String>();
	}
	
	public void add(String item) 
	{
		added.add(item);
	}
	
	public void remove(String id)
	{
		removed.add(id);
	}
	
	public void update(String item)
	{
		added.add(item);
	}

	public void clone(String item) {}

    public void removeAll() {
        added.clear();
        removed.clear();
        removed.add(REMOVE_ALL_ITEMS_MARK);
    }

	@XmlElementWrapper(name="added")
	@XmlElement(name="item")
	public List<String> getAdded() { return added; }
	@XmlElementWrapper(name="removed")
	@XmlElement(name="item")
	public List<String> getRemoved() { return removed; }
	
}
