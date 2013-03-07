package com.semantria.mapping.configuration.stub;

import com.semantria.mapping.configuration.Configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="configurations")
public final class Configurations
{
	private List<Configuration> configurations = new ArrayList<Configuration>();

	public Configurations() { }

	public Configurations(List<Configuration> configurations)
	{
		this.configurations = configurations;
	}

	@XmlElement(name="configuration")
	public List<Configuration> getConfigurations() { return configurations; }
	
	public void setConfigurations(List<Configuration> configurations) { this.configurations = configurations; }
}
