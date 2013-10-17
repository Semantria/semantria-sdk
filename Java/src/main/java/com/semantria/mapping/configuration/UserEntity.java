package com.semantria.mapping.configuration;

import javax.xml.bind.annotation.XmlElement;

public class UserEntity 
{
	private String name = null;
	private String type = null;
    private String label = null;
    private String normalized = null;

    public UserEntity() {}

	public UserEntity(String name, String type, String label, String normalized)
	{
		this.name = name;
		this.type = type;
        this.label = label;
        this.normalized = normalized;
	}

	@XmlElement(name="name")
	public String getName() { return name; }
	@XmlElement(name="type")	
	public String getType() { return type; }
    @XmlElement(name="label")
    public String getLabel() { return label; }
    @XmlElement(name = "normalized")
    public String getNormalized() { return normalized; }
	
	public void setName( String name ) { this.name = name; }
	public void setType(String type) { this.type = type; }
    public void setLabel(String label) { this.label = label; }
    public void setNormalized(String normalized) { this.normalized = normalized; }
}
