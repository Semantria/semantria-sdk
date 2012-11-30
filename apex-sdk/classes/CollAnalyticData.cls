public class CollAnalyticData {
	private String id;
	private String config_id;
	private String status;
	private List<Facet> facets;
	private List<CollEntity> entities;
	private List<CollTopic> topics;
	private List<CollTheme> themes;
	
	public String getId() { return id; }
	public String getConfigId() { return config_id; }
	public String getStatus() { return status; }
	public List<Facet> getFacets() { return facets; }
	public List<CollEntity> getEntities() { return entities; }
	public List<CollTopic> getTopics() { return topics; }
	public List<CollTheme> getThemes() { return themes; }
	
	public void setId(String id) { this.id = id; } 
	public void setConfigId(String configId) { this.config_id = configId; }
	public void setStatus(String status) { this.status = status; }
	public void setFacets(List<Facet> facets) { this.facets = facets; }
	public void setEntities(List<CollEntity> entities) { this.entities = entities; }
	public void setTopics(List<CollTopic> topics) { this.topics = topics; }
	public void setThemes(List<CollTheme> themes) { this.themes = themes; }
}