package com.semantria.mapping.output.stub;

import com.semantria.mapping.output.DocAnalyticData;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="documents")
public final class DocsAnalyticData
{
	private List<DocAnalyticData> docAnalyics = new ArrayList<DocAnalyticData>();

	public DocsAnalyticData() {}

	public DocsAnalyticData(List<DocAnalyticData> docAnalyics)
	{
		this.docAnalyics = docAnalyics;
	}

	@XmlElement(name="document")
	public List<DocAnalyticData> getDocuments() { return docAnalyics; }
	
	public void setDocuments(List<DocAnalyticData> docAnalyics) { this.docAnalyics = docAnalyics; }
}
