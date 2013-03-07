package com.semantria.mapping.configuration.stub;

import com.semantria.mapping.configuration.Configuration;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name="configurations")
public final class ConfigurationsDeleteReq
{
	private List<String> configurations = new ArrayList<String>();

	public ConfigurationsDeleteReq() { }


	public ConfigurationsDeleteReq(List<Configuration> configurations)
	{
		if( configurations != null && !configurations.isEmpty())
		{
			for (Configuration configuration : configurations)
			{
				this.configurations.add( configuration.getId() );
			}
		};
	}

	@XmlElement(name="configuration")
	public List<String> getConfigurations() { return configurations; }
	
	public void setConfigurations(List<String> configurations) { this.configurations = configurations; }
}
