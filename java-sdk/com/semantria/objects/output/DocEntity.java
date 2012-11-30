package com.semantria.objects.output;

import java.util.List;
import javax.xml.bind.annotation.*;

public final class DocEntity
{
	private String title;
	private String type;
	private String entity_type;
	private Integer evidence;
	private Float sentiment_score;
	private List<DocTheme> themes;
	private Boolean is_about;
    private Boolean confident;
	
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
	
	public void setTitle(String title) { this.title = title; }
	public void setType(String type) { this.type = type; }
	public void setEntityType(String entity_type) { this.entity_type = entity_type; }
	public void setEvidence(Integer evidence) { this.evidence = evidence; }
	public void setSentimentScore(Float sentiment_score) { this.sentiment_score = sentiment_score; }
	public void setThemes(List<DocTheme> themes) { this.themes = themes; }
	public void setIsAbout(Boolean is_about) { this.is_about = is_about; }
    public void setConfident(Boolean confident) { this.confident = confident; }
}
