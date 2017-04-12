package com.semantria.mapping.output.stub;

import com.semantria.mapping.output.DocAnalyticData;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="documents")
public final class DocsAnalyticData
{
	private List<DocAnalyticData> docAnalytics = new ArrayList<DocAnalyticData>();

	public DocsAnalyticData() {}

	public DocsAnalyticData(List<DocAnalyticData> docAnalytics)
	{
		this.docAnalytics = docAnalytics;
	}

	@XmlElement(name="document")
	public List<DocAnalyticData> getDocuments() { return docAnalytics; }
	
	public void setDocuments(List<DocAnalyticData> value) { this.docAnalytics = value; }
}
