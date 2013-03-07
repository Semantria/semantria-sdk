package com.semantria.mapping.configuration.stub;

import com.semantria.mapping.configuration.Query;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="queries")
public final class QueriesDeleteReq
{
	private List<String> queries = new ArrayList<String>();

	public QueriesDeleteReq() {}

	public QueriesDeleteReq(List<Query> queries)
	{
		if( queries != null && !queries.isEmpty() )
		{
			for (Query query : queries)
			{
				this.queries.add( query.getName() );
			}
		}
	}

	@XmlElement(name="query")
	public List<String> getQueries() { return queries; }
	
	public void setQueries(List<String> queries) { this.queries = queries; }
}
