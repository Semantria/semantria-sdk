package com.semantria.objects.configuration;

import com.semantria.objects.mapping.Stub_Collection;
import com.semantria.objects.mapping.Stub_Document;

import javax.xml.bind.annotation.XmlElement;

public final class Configuration 
{
	private String config_id = null;
	private String name = null;
	private String template = null;
	private Boolean one_sentence = null;
	private Boolean auto_responding = null;
	private Boolean is_primary = null;
	private String language = null;
	private Integer chars_threshold = null;
	private Stub_Document document = null;
	private Stub_Collection collection = null;
	private String callback = null;
	
	@XmlElement(name="config_id")
	public String getId() { return config_id; }
	@XmlElement(name="name")
	public String getName() { return name; }
	@XmlElement(name="template")
	public String getTemplate() { return template; }
	@XmlElement(name="one_sentence")
	public Boolean getOneSentence() { return one_sentence; }
	@XmlElement(name="auto_responding")
	public Boolean getAutoResponding() { return auto_responding; }
	@XmlElement(name="is_primary")
	public Boolean getIsPrimary() { return is_primary; }
	@XmlElement(name="language")
	public String getLanguage() { return language; }
	@XmlElement(name="chars_threshold")
	public Integer getCharsThreshold() { return chars_threshold; }
	@XmlElement(name="document")
	public Stub_Document getDocument() { return document; }
	@XmlElement(name="collection")
	public Stub_Collection getCollection() { return collection; }
	@XmlElement(name="callback")
	public String getCallback() { return callback; }
	
	public void setId(String id) { config_id = id; }
	public void setName(String cname) { name = cname; }
	public void setTemplate(String ctemplate) { template = ctemplate; }
	public void setOneSentence(Boolean one_sentence) { this.one_sentence = one_sentence; }
	public void setAutoResponding(Boolean autoresponse) { auto_responding = autoresponse; }
	public void setIsPrimary(Boolean cis_primary) { is_primary = cis_primary; }
	public void setCharsThreshold(Integer limit) { chars_threshold = limit;}
	public void setCollection(Stub_Collection scollection) { collection = scollection; }
	public void setDocument(Stub_Document sdocument) { document = sdocument; }
	public void setCallback(String url) { callback = url; }
    public void setLanguage(String lang) { language = lang; }
	
}
