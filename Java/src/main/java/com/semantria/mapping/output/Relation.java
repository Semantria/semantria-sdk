package com.semantria.mapping.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import java.util.List;

public class Relation
{
	private String relation_type = null;
	private String extra = null;
	private Float confidence_score = null;
	private List<RelationEntity> entities = null;
	private String type = null;

	public Relation() {}

	@XmlElement(name = "relation_type")
	public String getRelationType() { return relation_type; }
	@XmlElement(name = "extra")
	public String getExtra() { return extra; }
	@XmlElement(name = "confidence_score")
	public Float getConfidenceScore() { return confidence_score; }
	@XmlElementWrapper(name = "entities")
	@XmlElement(name = "entity")
	public List<RelationEntity> getEntities() { return entities; }
	@XmlElement(name = "type")
	public String getType() { return type; }

	public void setRelationType(String relation_type) { this.relation_type = relation_type; }
	public void setExtra(String extra) { this.extra = extra; }
	public void setConfidenceScore(Float confidence_score) { this.confidence_score = confidence_score; }
	public void setEntities(List<RelationEntity> entities) { this.entities = entities; }
	public void setType(String type) { this.type = type; }
}
