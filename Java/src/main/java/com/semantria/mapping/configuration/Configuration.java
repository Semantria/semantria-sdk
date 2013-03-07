package com.semantria.mapping.configuration;

import javax.xml.bind.annotation.XmlElement;

public final class Configuration
{
	private String config_id = null;
	private String template = null;
	private String name = null;
	private Boolean auto_response = null;
	private Boolean is_primary = null;
	private Boolean one_sentence = null;
	private String language = null;
	private Integer chars_threshold = null;
	private DocConfiguration document = null;
	private CollConfiguration collection = null;
	private String callback = null;

	public Configuration() {}

	public Configuration(String id, String template, String name, Boolean auto_response, Boolean is_primary, Boolean one_sentence, String language, Integer chars_threshold, DocConfiguration document, CollConfiguration collection, String callback)
	{
		this.config_id = id;
		this.template = template;
		this.name = name;
		this.auto_response = auto_response;
		this.is_primary = is_primary;
		this.one_sentence = one_sentence;
		this.language = language;
		this.chars_threshold = chars_threshold;
		this.document = document;
		this.collection = collection;
		this.callback = callback;
	}

	@XmlElement(name="template")
	public String getTemplate() { return template; }
	@XmlElement(name="config_id")
	public String getId() { return config_id; }
	@XmlElement(name="auto_response")
	public Boolean getAutoResponse() { return auto_response; }
	@XmlElement(name="is_primary")
	public Boolean getIsPrimary() { return is_primary; }
	@XmlElement(name="language")
	public String getLanguage() { return language; }
	@XmlElement(name="name")
	public String getName() { return name; }
	@XmlElement(name="chars_threshold")
	public Integer getCharsThreshold() { return chars_threshold; }
	@XmlElement(name="document")
	public DocConfiguration getDocument() { return document; }
	@XmlElement(name="collection")
	public CollConfiguration getCollection() { return collection; }
	@XmlElement(name="callback")
	public String getCallback() { return callback; }
	@XmlElement(name="one_sentence")
	public Boolean getOneSentence() { return one_sentence; }

	public void setId(String id) { this.config_id = id; }
	public void setAutoResponse(Boolean auto_response) { this.auto_response = auto_response; }
	public void setIsPrimary(Boolean is_primary) { this.is_primary = is_primary; }
	public void setName(String name) { this.name = name; }
	public void setCollection(CollConfiguration collection) { this.collection = collection; }
	public void setDocument(DocConfiguration document) { this.document = document;	}
	public void setLanguage(String lang) { language = lang; }
	public void setCharsThreshold(Integer chars_threshold) { this.chars_threshold = chars_threshold; }
	public void setCallback(String url) { callback = url; }
	public void setOneSentence(Boolean value) { one_sentence = value; }
	public void setTemplate(String template) { this.template = template; }
}
