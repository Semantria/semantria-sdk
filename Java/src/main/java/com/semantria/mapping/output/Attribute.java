package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class Attribute 
{
	private String label = null;
	private Integer count = null;
	List<Mention> mentions = null;
	
	@XmlElement(name="label")
	public String getLabel(){ return label; }
	@XmlElement(name="count")
	public Integer getCount(){ return count; }
	@XmlElementWrapper(name="mentions")
	@XmlElement(name="mention")
	public List<Mention> getMentions() { return mentions; }
	
	public void setLabel(String label){ this.label = label; }
	public void setCount(Integer count){ this.count = count; }
}
