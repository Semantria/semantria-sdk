package com.semantria.objects.mapping;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;
import com.semantria.objects.output.Document;

@XmlRootElement(name="documents")
public final class Stub_Tasks
{
	private List<Document> tasks = new ArrayList<Document>();
	
	@XmlElement(name="document")
	public List<Document> getTasks() { return tasks; }
	
	public void setTasks(List<Document> ttasks) { tasks = ttasks; }
}
