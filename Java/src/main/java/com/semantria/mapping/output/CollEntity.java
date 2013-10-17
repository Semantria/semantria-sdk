package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class CollEntity {
    private String title;
    private String type;
    private String entity_type;
    private Integer count;
    private Integer negative_count;
    private Integer neutral_count;
    private Integer positive_count;
    private String label;
    private List<Mention> mentions;

    @XmlElement(name="title")
    public String getTitle() { return title; }
    @XmlElement(name="type")
    public String getType() { return type; }
    @XmlElement(name="entity_type")
    public String getEntityType() { return entity_type; }
    @XmlElement(name="count")
    public Integer getCount() { return count; }
    @XmlElement(name="negative_count")
    public Integer getNegativeCount() { return negative_count; }
    @XmlElement(name="neutral_count")
    public Integer getNeutralCount() { return neutral_count; }
    @XmlElement(name="positive_count")
    public Integer getPositiveCount() { return positive_count; }
    @XmlElement(name="label")
    public String getLabel() { return label; }
    @XmlElementWrapper(name = "mentions")
    @XmlElement(name = "mention")
    public List<Mention> getMentions() { return mentions; }

    public void setTitle(String title) { this.title = title; }
    public void setType(String type) { this.type = type; }
    public void setEntityType(String entity_type) { this.entity_type = entity_type; }
    public void setCount(Integer count) {this.count = count;}
    public void setNegativeCount(Integer negative_count) {this.negative_count = negative_count;}
    public void setNeutralCount(Integer neutral_count) {this.neutral_count = neutral_count;}
    public void setPositiveCount(Integer positive_count) {this.positive_count = positive_count;}
    public void setLabel(String label) { this.label = label; }
    public void setMentions(List<Mention> mentions) { this.mentions = mentions; }

}
