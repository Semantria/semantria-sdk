package com.semantria.objects.output;

import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="collection")
public class CollAnalyticData 
{
	private String id;
	private String config_id;
	private TaskStatus status;
	private List<Facet> facets;
    private List<CollTopic> topics;
    private List<CollTheme> themes;
    private List<CollEntity> entities;
	
	@XmlElement(name="id")
	public String getId() { return id; }
	@XmlElement(name="config_id")
	public String getConfigId() { return config_id; }
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
	
	public void setId(String cid) { id = cid; }
	public void setConfigId(String config) { config_id = config; }
	public void setStatus(TaskStatus cstatus) { status = cstatus; }
	public void setFacets(List<Facet> list) { facets = list; }
    public void setEntities(List<CollEntity> sentities) { entities = sentities; }
    public void setTopics(List<CollTopic> atopics) { topics = atopics; }
    public void setThemes(List<CollTheme> athemes) { themes = athemes; }
}
