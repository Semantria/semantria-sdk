package com.semantria.mapping.configuration.stub;

import com.semantria.mapping.configuration.Category;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="categories")
public final class CategoriesDeleteReq
{
	private List<String> categories = new ArrayList<String>();

	public CategoriesDeleteReq() {}

	public CategoriesDeleteReq(List<Category> categories)
	{
		if( categories != null && !categories.isEmpty())
		{
			for (Category category : categories)
			{
				this.categories.add( category.getName() );
			}
		}
	}

	@XmlElement(name="category")
	public List<String> getCategories() { return categories; }
	
	public void setCategories(List<String> categories) { this.categories = categories; }
}
