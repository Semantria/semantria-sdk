package com.semantria.mapping.output;

import com.semantria.mapping.output.subscription.BasicSettings;
import com.semantria.mapping.output.subscription.BillingSettings;
import com.semantria.mapping.output.subscription.FeatureSettings;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="subscription")
public class Subscription 
{
	private BillingSettings billing_settings = null;
	private BasicSettings basic_settings = null;
	private FeatureSettings feature_settings = null;
	private String status = null;
	private String name = null;

	public Subscription() {};

	@XmlElement(name="status")
	public String getStatus() { return status; }
	@XmlElement(name="name")
	public String getName() { return name; }
	@XmlElement(name = "billing_settings")
	public BillingSettings getBillingSettings() { return billing_settings; }
	@XmlElement(name = "basic_settings")
	public BasicSettings getBasicSettings() { return basic_settings; }
	@XmlElement(name = "feature_settings")
	public FeatureSettings getFeatureSettings() { return feature_settings; }

	public void setBillingSettings(BillingSettings billing_settings) { this.billing_settings = billing_settings; }
	public void setBasicSettings(BasicSettings basic_settings) { this.basic_settings = basic_settings; }
	public void setFeatureSettings(FeatureSettings feature_settings) { this.feature_settings = feature_settings; }
	public void setStatus(String status) { this.status = status; }
	public void setName(String name) { this.name = name; }
}
