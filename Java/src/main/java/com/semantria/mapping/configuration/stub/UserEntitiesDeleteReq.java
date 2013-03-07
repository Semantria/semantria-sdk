package com.semantria.mapping.configuration.stub;

import com.semantria.mapping.configuration.UserEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="entities")
public final class UserEntitiesDeleteReq
{
	private List<String> entities = new ArrayList<String>();

	public UserEntitiesDeleteReq() {}

	public UserEntitiesDeleteReq(List<UserEntity> entities)
	{
		if(  entities != null && !entities.isEmpty() )
		{
			for (UserEntity entity : entities)
			{
				this.entities.add( entity.getName() );
			}
		}
	}

	@XmlElement(name="entity")
	public List<String> getEntities() { return  entities; }
	
	public void setEntities(List<String> entities) { this.entities = entities; }
}
