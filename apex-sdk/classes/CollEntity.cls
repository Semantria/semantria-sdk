public class CollEntity {
    private String title;
    private String type;
    private String entity_type;
    private Integer count;
    private Integer negative_count;
    private Integer neutral_count;
    private Integer positive_count;

    public String getTitle() { return title; }
    public String getType() { return type; }
    public String getEntityType() { return entity_type; }
    public Integer getCount() { return count; }
    public Integer getNegativeCount() { return negative_count; }
    public Integer getNeutralCount() { return neutral_count; }
    public Integer getPositiveCount() { return positive_count; }

    public void setTitle(String title) { this.title = title; }
    public void setType(String type) { this.type = type; }
    public void setEntityType(String entity_type) { this.entity_type = entity_type; }
    public void setCount(Integer count) {this.count = count;}
    public void setNegativeCount(Integer negative_count) {this.negative_count = negative_count;}
    public void setNeutralCount(Integer neutral_count) {this.neutral_count = neutral_count;}
    public void setPositiveCount(Integer positive_count) {this.positive_count = positive_count;}
}