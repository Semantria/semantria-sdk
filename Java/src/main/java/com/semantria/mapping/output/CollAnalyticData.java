package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name="collection")
public class CollAnalyticData 
{
	private String id = null;
	private String config_id = null;
    private String job_id = null;
	private TaskStatus status = null;
	private List<Facet> facets = null;
    private List<CollTopic> topics = null;
    private List<CollTheme> themes = null;
    private List<CollEntity> entities = null;
	private String summary = null;
    private String tag = null;
	
	@XmlElement(name="id")
	public String getId() { return id; }
	@XmlElement(name="config_id")
	public String getConfigId() { return config_id; }
    @XmlElement(name="job_id")
    public String getJobId() { return job_id; }
	@XmlElement(name="status")
	public TaskStatus getStatus() { return status; }
	@XmlElementWrapper(name="facets")
	@XmlElement(name="facet")
	public List<Facet> getFacets() { return facets; }
    @XmlElementWrapper(name="entities")
    @XmlElement(name="entity")
    public List<CollEntity> getEntities() { return entities; }
    @XmlElementWrapper(name="topics")
    @XmlElement(name="topic")
    public List<CollTopic> getTopics() { return topics; }
    @XmlElementWrapper(name="themes")
    @XmlElement(name="theme")
    public List<CollTheme> getThemes() { return themes; }
	@XmlElement(name="summary")
	public String getSummary() { return summary; };
    @XmlElement(name = "tag")
    public String getTag() { return tag; }
	
	public void setId(String cid) { id = cid; }
	public void setConfigId(String configId) { config_id = configId; }
    public void setJobId(String jobId) { config_id = jobId; }
	public void setStatus(TaskStatus cstatus) { status = cstatus; }
	public void setFacets(List<Facet> list) { facets = list; }
    public void setEntities(List<CollEntity> sentities) { entities = sentities; }
    public void setTopics(List<CollTopic> atopics) { topics = atopics; }
    public void setThemes(List<CollTheme> athemes) { themes = athemes; }
	public void setSummary(String summary) { this.summary = summary; }
    public void setTag(String tag) { this.tag = tag; }
}
