package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;

public class RelationEntity
{
	private String title = null;
	private String entity_type = null;

	public RelationEntity() {}

	@XmlElement(name = "title")
	public String getTitle() { return title; }
	@XmlElement(name = "entity_type")
	public String getEntityType() { return entity_type; }

	public void setTitle(String title) { this.title = title; }
	public void setEntityType(String entity_type) { this.entity_type = entity_type; }

}
