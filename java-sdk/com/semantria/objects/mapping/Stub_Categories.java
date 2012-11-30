package com.semantria.objects.mapping;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;
import com.semantria.objects.configuration.Category;

@XmlRootElement(name="categories")
public final class Stub_Categories 
{
	private List<Category> c_categories = new ArrayList<Category>();
	@XmlElement(name="category")
	public List<Category> getCategories() { return c_categories; }
	
	public void setCategories(List<Category> categories) { c_categories = categories; }
}
