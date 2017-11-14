package com.semantria.mapping.configuration;

import javax.xml.bind.annotation.XmlElement;
import java.sql.Timestamp;

public class Query 
{
    private String id = null;
	private String name;
	private String query;
    private transient Timestamp modified = null;

	public Query() { }

	public Query(String name, String query)
	{
		this.name = name;
		this.query = query;
	}

    @XmlElement(name="id")
    public String getId() { return id; }
	@XmlElement(name="name")
	public String getName() { return name; }
	@XmlElement(name="query")
	public String getQuery() { return query; }
    @XmlElement(name="modified")
    public Timestamp getModified() { return modified; }

    public void setId(String id) { this.id = id; }
	public void setName(String name) { this.name = name; }
	public void setQuery(String query) { this.query = query; }
    public void setModified(Timestamp modified) { this.modified = modified; }
}
