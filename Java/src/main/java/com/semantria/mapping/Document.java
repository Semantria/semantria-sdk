package com.semantria.mapping;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name="document")
public final class Document 
{
	private String id = null;
	private String text = null;
    private String tag = null;
    private String job_id = null;
	
	public Document() {}

	public Document(String id, String text)
	{
		this.id = id;
		this.text = text;
	}

    public Document(String id, String text, String tag)
    {
        this.id = id;
        this.text = text;
        this.tag = tag;
    }

	@XmlElement(name="id")
	public String getId() { return id; }
	@XmlElement(name="text")
	public String getText() { return text; }
    @XmlElement(name = "tag")
    public String getTag() { return tag; }
    @XmlElement(name = "job_id")
    public String getJobId() { return job_id; }
	
	public void setId(String id) { this.id = id; }
	public void setText(String text) { this.text = text; }
    public void setTag(String tag) { this.tag = tag; }
    public void setJobId(String jobId) { this.job_id = jobId; }
}
