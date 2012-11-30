public class DocEntity {
    private String title;
    private String type;
    private String entity_type;
    private Integer evidence;
    private Double sentiment_score;
    private List<DocTheme> themes;
    private Boolean is_about;
    private Boolean confident;
    
    public String getTitle() { return title; }
    public String getType() { return type; }
    public String getEntityType() { return entity_type; }
    public Integer getEvidence() { return evidence; }
    public Double getSentimentScore() { return sentiment_score; }
    public List<DocTheme> getThemes() { return themes; }
    public Boolean getIsAbout() { return is_about; }
    public Boolean getConfident() { return confident; }
    
    public void setTitle(String title) { this.title = title; }
    public void setType(String type) { this.type = type; }
    public void setEntityType(String entityType) { this.entity_type = entityType; }
    public void setEvidence(Integer evidence) { this.evidence = evidence; }
    public void setSentimentScore(Double sentimentScore) { this.sentiment_score = sentimentScore; }
    public void setThemes(List<DocTheme> themes) { this.themes = themes; }
    public void setIsAbout(Boolean isAbout) { this.is_about = isAbout; }
    public void setConfident(Boolean confident) { this.confident = confident; }
}