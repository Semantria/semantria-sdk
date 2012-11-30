package com.semantria.objects.mapping;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.semantria.objects.configuration.UserEntity;

@XmlRootElement(name="entities")
public final class Stub_Entities 
{
	private List<UserEntity> entities = new ArrayList<UserEntity>();
	@XmlElement(name="entity")
	public List<UserEntity> getEntities() { return  entities; }
	
	public void setEntities(List<UserEntity> list) { entities = list; }
}
