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
	private Float categories_threshold = null;
    private Integer entities_threshold = null;
	private DocConfiguration document = null;
	private CollConfiguration collection = null;
	private String callback = null;
    private Boolean process_html = null;

	public Configuration() {}

	public Configuration(String id, String name)
	{
		this.config_id = id;
		this.name = name;
	}

    public Configuration(String id, String name, String template)
    {
        this.config_id = id;
        this.name = name;
        this.template = template;
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
	@XmlElement(name="categories_threshold")
	public Float getCategoriesThreshold() { return categories_threshold; }
    @XmlElement(name="entities_threshold")
    public Integer getEntitiesThreshold() { return entities_threshold; }
	@XmlElement(name="document")
	public DocConfiguration getDocument() { return document; }
	@XmlElement(name="collection")
	public CollConfiguration getCollection() { return collection; }
	@XmlElement(name="callback")
	public String getCallback() { return callback; }
	@XmlElement(name="one_sentence")
	public Boolean getOneSentence() { return one_sentence; }
    @XmlElement(name = "process_html")
    public Boolean getProcessHtml() { return  process_html; }

	public void setId(String id) { this.config_id = id; }
	public void setAutoResponse(Boolean auto_response) { this.auto_response = auto_response; }
	public void setIsPrimary(Boolean is_primary) { this.is_primary = is_primary; }
	public void setName(String name) { this.name = name; }
	public void setCollection(CollConfiguration collection) { this.collection = collection; }
	public void setDocument(DocConfiguration document) { this.document = document;	}
	public void setLanguage(String lang) { language = lang; }
	public void setCharsThreshold(Integer chars_threshold) { this.chars_threshold = chars_threshold; }
	public void setCategoriesThreshold(Float categories_threshold) { this.categories_threshold = categories_threshold; }
    public void setEntitiesThreshold(Integer entities_threshold) { this.entities_threshold = entities_threshold; }
	public void setCallback(String url) { callback = url; }
	public void setOneSentence(Boolean value) { one_sentence = value; }
	public void setTemplate(String template) { this.template = template; }
    public void setProcessHtml(Boolean process_html) { this.process_html = process_html; }
}
