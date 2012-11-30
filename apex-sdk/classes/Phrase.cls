public class Phrase {
	private String title;
	private Double sentiment_score;
	private Boolean is_negated;
	private String negating_phrase;
	
	public String getTitle() { return title; }
	public Double getSentimentScore() { return sentiment_score; }
	public Boolean getIsNegated() { return is_negated; }
	public String getNegatingPhrase() { return negating_phrase; }
	
	public void setTitle(String title) { this.title = title; }
	public void setSentimentScore(Double sentimentScore) { this.sentiment_score = sentimentScore; }
	public void setIsNegated(Boolean isNegated) { this.is_negated = isNegated; }
	public void setNegatingPhrase(String negatingPhrase) { this.negating_phrase = negatingPhrase; }
}