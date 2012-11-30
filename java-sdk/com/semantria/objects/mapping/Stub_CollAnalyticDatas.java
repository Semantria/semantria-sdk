package com.semantria.objects.mapping;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

import com.semantria.objects.output.CollAnalyticData;

@XmlRootElement(name="collections")
public final class Stub_CollAnalyticDatas 
{
	private List<CollAnalyticData> t_tasks = new ArrayList<CollAnalyticData>();
	
	@XmlElement(name="collection")
	public List<CollAnalyticData> getDocuments() { return t_tasks; }
	
	public void setDocuments(List<CollAnalyticData> tasks) { t_tasks = tasks; }
}
