package com.semantria.mapping;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
@XmlRootElement(name="collection")
public class Collection 
{
	private String id;
	private List<String> documents;

	public Collection() {}

	public String getId() { return id; }
	@XmlElementWrapper(name="documents")
	@XmlElement(name="document")
	public List<String> getDocuments() { return documents; }
	
	public void setId(String sid) { id = sid; }
	public void setDocuments(List<String> docs) { documents = docs; }
	
}
