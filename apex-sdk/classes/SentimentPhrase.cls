public class SentimentPhrase {
    private String title;
    private Double weight;
    
    public SentimentPhrase(String title, Double weight) {
    	this.title = title;
    	this.weight = weight;
    }
    
    public String getTitle() {
    	return title;
    }
    
    public Double getWeight() {
    	return weight;
    }
    
    public void setTitle(String title) {
    	this.title = title;
    }
    
    public void setWeight(Double weight) {
    	this.weight = weight;
    }
}