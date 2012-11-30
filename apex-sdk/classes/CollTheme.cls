public class CollTheme {
    private String title;
    private Double sentiment_score;
    private Integer phrases_count;
    private Integer themes_count;

    public String getTitle() { return title; }
    public Double getSentimentScore() { return sentiment_score; }
    public Integer getPhrasesCount() { return phrases_count; }
    public Integer getThemesCount() { return themes_count; }

    public void setTitle(String ttitle) { title = ttitle; }
    public void setSentimentScore(Double tsentiment) { sentiment_score = tsentiment; }
    public void setPhrasesCount(Integer phrases_count) {this.phrases_count = phrases_count;}
    public void setThemesCount(Integer themes_count) {this.themes_count = themes_count;}
}