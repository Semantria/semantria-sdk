package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Mention
{
	private String label = null;
	private Boolean is_negated = null;
	private String negating_phrase = null;
	private List<Integer> indexes = null;
	private List<Location> locations = null;

	public Mention() { }

	public Mention(String label, boolean is_negated, String negating_phrase, int index, Location location)
	{
		this.label = label;
		this.is_negated = is_negated;
		this.negating_phrase = negating_phrase;
		this.indexes = new ArrayList<Integer>( Arrays.asList(index) );
		this.locations = new ArrayList<Location>( Arrays.asList(location) );
	}

	@XmlElement(name = "label")
	public String getLabel() { return label; }
	@XmlElement(name = "is_negated")
	public Boolean getIs_negated() { return is_negated; }
	@XmlElement(name = "negating_phrase")
	public String getNegating_phrase() { return negating_phrase; }
	@XmlElementWrapper(name = "indexes")
	@XmlElement(name = "index")
	public List<Integer> getIndexes() { return indexes; }
	@XmlElementWrapper(name = "locations")
	@XmlElement(name = "location")
	public List<Location> getLocations() { return locations; }

	public void setLabel(String label) { this.label = label; }
	public void setIs_negated(Boolean is_negated) { this.is_negated = is_negated; }
	public void setNegating_phrase(String negating_phrase) { this.negating_phrase = negating_phrase; }
	public void setIndexes(List<Integer> indexes) { this.indexes = indexes; }
	public void setLocations(List<Location> locations) { this.locations = locations; }
}
