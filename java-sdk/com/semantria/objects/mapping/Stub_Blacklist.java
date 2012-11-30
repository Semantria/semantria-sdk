package com.semantria.objects.mapping;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;

@XmlRootElement(name="blacklist")
public final class Stub_Blacklist 
{
	private List<String> b_blacklist = new ArrayList<String>();
	@XmlElement(name="item")
	public List<String> getBlacklist() { return  b_blacklist; }
	
	public void setBlacklist(List<String> list) { b_blacklist = list; }
}
