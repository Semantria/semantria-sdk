package com.semantria.proxies;

import com.semantria.interfaces.IUpdateProxy;
import com.semantria.objects.configuration.Category;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="categories")
public final class CategoryUpdateProxy implements IUpdateProxy<Category> 
{
	private List<Category> added;
	private List<String> removed;
	
	public CategoryUpdateProxy()
	{
		added = new ArrayList<Category>();
		removed = new ArrayList<String>();
	}
	
	public void add(Category cat)
	{
		added.add(cat);
	}
	
	public void remove(Category id)
	{
		removed.add(id.getName());
	}
	
	public void update(Category cat)
	{
		added.add(cat);
	}

	public void clone(Category item) {}

    public void removeAll() {
        added.clear();
        removed.clear();
        removed.add(REMOVE_ALL_ITEMS_MARK);
    }
	
	@XmlElementWrapper(name="added")
	@XmlElement(name="category")
	public List<Category> getAdded() { return added; }
	@XmlElementWrapper(name="removed")
	@XmlElement(name="category")
	public List<String> getRemoved() { return removed; }
	
}
