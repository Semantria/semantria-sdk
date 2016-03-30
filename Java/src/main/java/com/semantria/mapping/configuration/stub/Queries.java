package com.semantria.mapping.configuration.stub;

import com.semantria.mapping.configuration.Query;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="queries")
public final class Queries
{
	private List<Query> queries = new ArrayList<Query>();

	public Queries() {}

	public Queries(List<Query> queries)
	{
		this.queries = queries;
	}

	@XmlElement(name="query")
	public List<Query> getQueries() { return queries; }
	
	public void setQueries(List<Query> queries) { this.queries = queries; }
}
