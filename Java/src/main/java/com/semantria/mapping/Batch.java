package com.semantria.mapping;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="documents")
public final class Batch
{
	private List<Document> documents = new ArrayList<Document>();

	public Batch() {}

	public Batch(List<Document> documents)
	{
		this.documents = documents;
	}

	@XmlElement(name="document")
	public List<Document> getDocuments() { return documents; }
	
	public void setDocuments(List<Document> documents) { this.documents = documents; }
}
