package com.semantria.objects.mapping;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.*;
import com.semantria.objects.configuration.Configuration;


@XmlRootElement(name="configurations")
public final class Stub_Configurations
{
	private List<Configuration> configurations = new ArrayList<Configuration>();
	@XmlElement(name="configuration")
	public List<Configuration> getConfigurations() { return configurations; }
	
	public void setConfigurations(List<Configuration> cconfigurations) { configurations = cconfigurations; }
}
