package com.semantria.objects.mapping;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import com.semantria.objects.configuration.Query;

@XmlRootElement(name="queries")
public final class Stub_Queries 
{
	private List<Query> queries = new ArrayList<Query>();
	@XmlElement(name="query")
	public List<Query> getQueries() { return queries; }
	
	public void setQueries(List<Query> qqueries) { queries = qqueries; }
}
