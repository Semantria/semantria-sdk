public class DocTheme {
    private String title;
    private Double sentiment_score;
    private Integer evidence;
    private Boolean is_about;
    private Double strength_score;
    
    public String getTitle() { return title; }
    public Double getSentimentScore() { return sentiment_score; }
    public Integer getEvidence() { return evidence; }
    public Boolean getIsAbout() { return is_about; }
    public Double getStrengthScore() { return strength_score; }
    
    public void setTitle(String title) { this.title = title; }
    public void setSentimentScore(Double sentimentScore) { this.sentiment_score = sentimentScore; }
    public void setEvidence(Integer evidence) { this.evidence = evidence; }
    public void setIsAbout(Boolean isAbout) { this.is_about = isAbout; }
    public void setStrengthScore(Double strengthScore) { this.strength_score = strengthScore; }
}