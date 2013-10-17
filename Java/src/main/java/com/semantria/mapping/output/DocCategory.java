package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public final class DocCategory
{
	private String title = null;
	private String type = null;
	private Float strength_score = null;
    private List<DocCategory> categories = null;

	@XmlElement(name="title")
	public String getTitle() { return title; }
	@XmlElement(name="type")
	public String getType() { return type; }
	@XmlElement(name="strength_score")
	public Float getStrengthScore() { return strength_score; }
    @XmlElementWrapper(name = "categories")
    @XmlElement(name = "category")
    public List<DocCategory> getCategories() { return categories; }

	public void setTitle(String title) { this.title = title; }
	public void setType(String type) { this.type = type; }
	public void setStrengthScore(Float strength_score) { this.strength_score = strength_score; }
    public void setCategories(List<DocCategory> categories) { this.categories = categories; }
}
