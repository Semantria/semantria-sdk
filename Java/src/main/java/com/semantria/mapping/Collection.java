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
    private String tag;
    private String job_id;

	public Collection() {}

    public Collection(String id)
    {
        this.id = id;
    }

    public Collection(String id, String tag)
    {
        this.id = id;
        this.tag = tag;
    }

    public String getId() { return id; }
	@XmlElementWrapper(name="documents")
	@XmlElement(name="document")
	public List<String> getDocuments() { return documents; }
    @XmlElement(name = "gat")
    public String getTag() { return tag; }
    @XmlElement(name = "job_id")
    public String getJobId() { return job_id; }
	
	public void setId(String sid) { id = sid; }
	public void setDocuments(List<String> docs) { documents = docs; }
    public void setTag(String tag) { this.tag = tag; }
    public void setJobId(String jobId) { this.job_id = jobId; }
}
