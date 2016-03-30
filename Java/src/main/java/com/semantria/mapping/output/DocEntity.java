package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public final class DocEntity
{
	private String title = null;
	private String type = null;
	private String entity_type = null;
	private Integer evidence = null;
	private Float sentiment_score = null;
	private List<DocTheme> themes = null;
	private Boolean is_about = null;
    private Boolean confident = null;
	private String sentiment_polarity = null;
	private List<Mention> mentions = null;
	private String label = null;

	@XmlElement(name="title")
	public String getTitle() { return title; }
	@XmlElement(name="type")
	public String getType() { return type; }
	@XmlElement(name="entity_type")
	public String getEntityType() { return entity_type; }
	@XmlElement(name="evidence")
	public Integer getEvidence() { return evidence; }
	@XmlElement(name="sentiment_score")
	public Float getSentimentScore() { return sentiment_score; }
	@XmlElementWrapper(name="themes")
	@XmlElement(name="theme")
	public List<DocTheme> getThemes() { return themes; }
	@XmlElement(name="is_about")
	public Boolean getIsAbout() { return is_about; }
    @XmlElement(name="confident")
    public Boolean getConfident() { return confident; }
	@XmlElement(name = "sentiment_polarity")
	public String getSentimentPolarity() { return sentiment_polarity; }
	@XmlElementWrapper(name = "mentions")
	@XmlElement(name = "mention")
	public List<Mention> getMentions() { return mentions; }
	@XmlElement(name = "label")
	public String getLabel() { return label; }

	public void setTitle(String title) { this.title = title; }
	public void setType(String type) { this.type = type; }
	public void setEntityType(String entity_type) { this.entity_type = entity_type; }
	public void setEvidence(Integer evidence) { this.evidence = evidence; }
	public void setSentimentScore(Float sentiment_score) { this.sentiment_score = sentiment_score; }
	public void setThemes(List<DocTheme> themes) { this.themes = themes; }
	public void setIsAbout(Boolean is_about) { this.is_about = is_about; }
    public void setConfident(Boolean confident) { this.confident = confident; }
	public void setSentimentPolarity(String sentiment_polarity) { this.sentiment_polarity = sentiment_polarity; }
	public void setMentions(List<Mention> mentions) { this.mentions = mentions; }
	public void setLabel(String label) { this.label = label; }
}
