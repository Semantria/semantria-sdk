public class Topic {
	private String title;
	private String type;
	private Integer hitcount;
	private Double sentiment_score;
	private Double strength_score;
	
	public String getTitle() { return title; }
	public String getType() { return type; }
	public Integer getHitCount() { return hitcount; }
	public Double getSentimentScore() { return sentiment_score; }
	public Double getStrengthScore() { return strength_score; }
	
	public void setTitle(String title) { this.title = title; }
	public void setType(String type) { this.type = type; }
	public void setHitCount(Integer hitCount) { this.hitcount = hitCount; }
	public void setSentimentScore(Double sentimentScore) { this.sentiment_score = sentimentScore; }
	public void setStrengthScore(Double strengthScore) { this.strength_score = strengthScore; }
}