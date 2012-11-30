package com.semantria.objects.mapping;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import com.semantria.objects.output.DocAnalyticData;

@XmlRootElement(name="documents")
public final class Stub_DocAnalyticDatas 
{
	private List<DocAnalyticData> t_tasks = new ArrayList<DocAnalyticData>();
	
	@XmlElement(name="document")
	public List<DocAnalyticData> getDocuments() { return t_tasks; }
	
	public void setDocuments(List<DocAnalyticData> tasks) { t_tasks = tasks; }
}
