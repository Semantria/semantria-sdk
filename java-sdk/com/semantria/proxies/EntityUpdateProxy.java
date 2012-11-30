package com.semantria.proxies;

import com.semantria.interfaces.IUpdateProxy;
import com.semantria.objects.configuration.UserEntity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="entities")
public class EntityUpdateProxy implements IUpdateProxy<UserEntity>
{
	private List<UserEntity> added;
	private List<String> removed;
	
	public EntityUpdateProxy()
	{
		added = new ArrayList<UserEntity>();
		removed = new ArrayList<String>();
	}
	
	public void add(UserEntity item) 
	{
		added.add(item);
	}
	
	public void remove(UserEntity id)
	{
		removed.add(id.getName());
	}
	
	public void update(UserEntity item)
	{
		added.add(item);
	}

	public void clone(UserEntity item) {}

    public void removeAll() {
        added.clear();
        removed.clear();
        removed.add(REMOVE_ALL_ITEMS_MARK);
    }

	@XmlElementWrapper(name="added")
	@XmlElement(name="entity")
	public List<UserEntity> getAdded() { return added; }
	@XmlElementWrapper(name="removed")
	@XmlElement(name="entity")
	public List<String> getRemoved() { return removed; }
}
