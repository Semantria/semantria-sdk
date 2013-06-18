package com.semantria.mapping.configuration;

import javax.xml.bind.annotation.XmlElement;

public class UserEntity 
{
	private String name = null;
	private String type = null;
    private String label = null;

    public UserEntity() {}

	public UserEntity(String name, String type, String label)
	{
		this.name = name;
		this.type = type;
        this.label = label;
	}

	@XmlElement(name="name")
	public String getName() { return name; }
	@XmlElement(name="type")	
	public String getType() { return type; }
    @XmlElement(name="label")
    public String getLabel() { return label; }
	
	public void setName( String name ) { this.name = name; }
	public void setType(String type) { this.type = type; }
    public void setLabel(String label) { this.label = label; }
}
