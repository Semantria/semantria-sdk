package com.semantria.mapping.configuration;

import javax.xml.bind.annotation.XmlElement;

public final class Query 
{
	private String name;
	private String query;
	
	public Query() { }

	public Query(String name, String query)
	{
		this.name = name;
		this.query = query;
	}

	@XmlElement(name="name")
	public String getName() { return name; }
	@XmlElement(name="query")
	public String getQuery() { return query; }
	
	public void setName(String name) { this.name = name; }
	public void setQuery(String query) { this.query = query; }
}
