package com.semantria.objects.output;
import javax.xml.bind.annotation.*;

public final class DocTheme
{
	private String title;
	private Float sentiment_score;
	private Integer evidence;
	private Boolean is_about;
	private Float strength_score;
	
	@XmlElement(name="title")
	public String getTitle() { return title; }
	@XmlElement(name="sentiment_score")
	public Float getSentimentScore() { return sentiment_score; }
	@XmlElement(name="evidence")
	public Integer getEvidence() { return evidence; }
	@XmlElement(name="is_about")
	public Boolean getIsAbout() { return is_about; }
	@XmlElement(name="strength_score")
	public Float getStrengthScore() { return strength_score; }
	
	public void setTitle(String ttitle) { title = ttitle; }
	public void setSentimentScore(Float tsentiment) { sentiment_score = tsentiment; }
	public void setEvidence(Integer tevidence) { evidence = tevidence; }
	public void setIsAbout(Boolean about) { is_about = about; }
	public void setStrengthScore(Float tscore) { strength_score = tscore; }
}
