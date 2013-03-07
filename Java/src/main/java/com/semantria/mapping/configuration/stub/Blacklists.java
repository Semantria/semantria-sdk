package com.semantria.mapping.configuration.stub;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="blacklist")
public final class Blacklists
{
	private List<String> item = new ArrayList<String>();

	public Blacklists() { }

	public Blacklists(List<String> item)
	{
		this.item = item;
	}

	@XmlElement(name="item")
	public List<String> getBlacklist() { return  item; }
	
	public void setBlacklist(List<String> list) { item = list; }
}
