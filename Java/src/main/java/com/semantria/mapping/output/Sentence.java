package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class Sentence
{
	private Boolean is_imperative = null;
	private List<Word> words = null;
	private Boolean is_polar = null;

	public Sentence() { }

	@XmlElement(name = "is_imperative")
	public Boolean getIsImperative() { return is_imperative; }
	@XmlElementWrapper(name = "words")
	@XmlElement(name = "word")
	public List<Word> getWords() { return words; }
	@XmlElement(name = "is_polar")
	public Boolean getIsPolar() { return is_polar; }

	public void setIsImperative(Boolean is_imperative) { this.is_imperative = is_imperative; }
	public void setWords(List<Word> words) { this.words = words; }
	public void setIsPolar(Boolean is_polar) { this.is_polar = is_polar; }
}
