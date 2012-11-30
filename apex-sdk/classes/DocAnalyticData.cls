public class DocAnalyticData {
	private String id;
	private String config_id;
	private String status;
	private Double sentiment_score;
	private List<DocEntity> entities;
	private List<DocTopic> topics;
	private List<DocTheme> themes;
	private List<DocPhrase> phrases;
	private String summary;
	
	public String getId() { return id; }
	public String getConfigId() { return config_id; }
	public String getStatus() { return status; }
	public Double getSentimentScore() { return sentiment_score; }
	public List<DocEntity> getEntities() { return entities; }
	public List<DocTopic> getTopics() { return topics; }
	public List<DocTheme> getThemes() { return themes; }
	public List<DocPhrase> getPhrases() { return phrases; }
	public String getSummary() { return summary; } 
	
	public void setId(String id) { this.id = id;}
	public void setConfigId(String configId) { this.config_id = configId; }
	public void setStatus(String status) { this.status = status; }
	public void setSentimentScore(Double sentimentScore) { this.sentiment_score = sentimentScore; }
	public void setEntities(List<DocEntity> entities) { this.entities = entities; }
	public void setTopics(List<DocTopic> topics) { this.topics = topics; }
	public void setThemes(List<DocTheme> themes) { this.themes = themes; }
	public void setPhrases(List<DocPhrase> phrases) { this.phrases = phrases; }
	public void setSummary(String summary) { this.summary = summary; }
}