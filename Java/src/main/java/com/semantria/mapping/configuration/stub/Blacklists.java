package com.semantria.mapping.configuration.stub;

import com.semantria.mapping.configuration.BlacklistItem;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="blacklist")
public class Blacklists
{
	private List<BlacklistItem> item = new ArrayList<BlacklistItem>();

	public Blacklists() { }

	public Blacklists(List<BlacklistItem> item)
	{
		this.item = item;
	}

	@XmlElement(name="item")
	public List<BlacklistItem> getBlacklist() { return  item; }
	
	public void setBlacklist(List<BlacklistItem> list) { item = list; }
}
