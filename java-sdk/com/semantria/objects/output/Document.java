package com.semantria.objects.output;

import javax.xml.bind.annotation.*;
@XmlRootElement(name="document")
public final class Document 
{

	private String id;
	private String text;
	
	public Document()
	{}
	
	public Document(String tid, String ttext)
	{
		id = tid;
		text = ttext;
	}
	
	@XmlElement(name="id")
	public String getId() { return id; }
	@XmlElement(name="text")
	public String getText() { return text; }
	
	public void setId(String tid) { id = tid; }
	public void setText(String ttext) { text = ttext; }
}
