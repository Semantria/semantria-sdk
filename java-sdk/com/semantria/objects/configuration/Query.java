package com.semantria.objects.configuration;

import javax.xml.bind.annotation.*;

public final class Query 
{
	private String name;
	private String query;
	
	public Query()
	{
		
	}
	public Query(String qname, String qquery)
	{
		name = qname;
		query = qquery;
	}
	@XmlElement(name="name")
	public String getName() { return name; }
	@XmlElement(name="query")
	public String getQuery() { return query; }
	
	public void setName(String topic) { name = topic; }
	public void setQuery(String qquery) { query = qquery; }
}
