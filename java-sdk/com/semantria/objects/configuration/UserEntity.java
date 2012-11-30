package com.semantria.objects.configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class UserEntity 
{
	private String name = null;
	private String type = null;

    public UserEntity() {}

    public UserEntity(String name, String type) {
        this.name = name;
        this.type = type;
    }

	@XmlElement(name="name")
	public String getName() { return name; }
	@XmlElement(name="type")	
	public String getType() { return type; }
	
	public void setName( String name ) { this.name = name; }
	public void setType(String type) { this.type = type; }
}
