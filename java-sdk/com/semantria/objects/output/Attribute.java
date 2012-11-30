package com.semantria.objects.output;

import javax.xml.bind.annotation.XmlElement;

public class Attribute 
{
	private String label = null;
	private Integer count = null;
	
	@XmlElement(name="label")
	public String getLabel(){ return label; }
	@XmlElement(name="count")
	public Integer getCount(){ return count; }
	
	public void setLabel(String label){ this.label = label; }
	public void setCount(Integer count){ this.count = count; }
}
