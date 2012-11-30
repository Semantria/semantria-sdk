package com.semantria.proxies;

import com.semantria.interfaces.IUpdateProxy;
import com.semantria.objects.configuration.Configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="configurations")
public final class ConfigurationUpdateProxy implements IUpdateProxy<Configuration> 
{
	private List<Configuration> added;
	private List<String> removed;
	
	public ConfigurationUpdateProxy()
	{
		added = new ArrayList<Configuration>();
		removed = new ArrayList<String>();
	}
	
	public void add(Configuration conf) 
	{
		added.add(conf);
	}
	
	public void remove(Configuration id)
	{
		removed.add(id.getId());
	}
	
	public void update(Configuration conf)
	{
		added.add(conf);
	}

	public void clone(Configuration conf)
	{
		conf.setTemplate(conf.getId());
		conf.setId(null);
		added.add(conf);
	}

    public void removeAll() {
        added.clear();
        removed.clear();
        removed.add(REMOVE_ALL_ITEMS_MARK);
    }

	@XmlElementWrapper(name="added")
	@XmlElement(name="configuration")
	public List<Configuration> getAdded() { return added; }
	@XmlElementWrapper(name="removed")
	@XmlElement(name="configuration")
	public List<String> getRemoved() { return removed; }
	
}
