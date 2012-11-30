public class CollTopic {
    private String title;
    private String type;
    private Integer hitcount;
    private Double sentiment_score;

    public String getTitle() { return title; }
    public String getType() { return type; }
    public Integer getHitCount() { return hitcount; }
    public Double getSentimentScore() { return sentiment_score; }

    public void setTitle(String ttitle) { title = ttitle; }
    public void setType(String ttype) { type = ttype; }
    public void setHitCount(Integer thitCount) { hitcount = thitCount; }
    public void setSentimentScore(Double tscore) { sentiment_score = tscore; }
}