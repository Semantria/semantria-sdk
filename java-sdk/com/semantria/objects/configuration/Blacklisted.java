package com.semantria.objects.configuration;

public final class Blacklisted 
{
	private String item;
	
	public Blacklisted(String bitem)
	{
		item = bitem;
	}
	
	public String getItem() { return item; }
	public void setItem(String bitem) { item = bitem; }
}
