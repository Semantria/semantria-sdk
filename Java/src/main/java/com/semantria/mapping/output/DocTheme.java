package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public final class DocTheme
{
	private String title = null;
	private Float sentiment_score = null;
	private Integer evidence = null;
	private Boolean is_about = null;
	private Float strength_score = null;
	private String sentiment_polarity = null;
	private List<Mention> mentions = null;

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
	@XmlElement(name = "sentiment_polarity")
	public String getSentimentPolarity() { return sentiment_polarity; }
	@XmlElementWrapper(name = "mentions")
	@XmlElement(name = "mention")
	public List<Mention> getMentions(){ return mentions; }

	public void setTitle(String title) { this.title = title; }
	public void setSentimentScore(Float sentiment_score) { this.sentiment_score = sentiment_score; }
	public void setEvidence(Integer evidence) { this.evidence = evidence; }
	public void setIsAbout(Boolean is_about) { this.is_about = is_about; }
	public void setStrengthScore(Float strength_score) { this.strength_score = strength_score; }
	public void setSentimentPolarity(String sentiment_polarity) { this.sentiment_polarity = sentiment_polarity; }
	public void setMentions(List<Mention> mentions) { this.mentions = mentions; }
}
