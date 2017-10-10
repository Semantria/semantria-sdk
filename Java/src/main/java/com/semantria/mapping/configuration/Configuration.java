package com.semantria.mapping.configuration;

import javax.xml.bind.annotation.XmlElement;
import java.sql.Timestamp;

public final class Configuration
{
	private String id = null;
    private String name = null;
    private String language = null;
	private String template = null;
    private Boolean is_primary = false;
    private transient Timestamp modified = null;
    private Boolean auto_response = false;
    private Integer alphanumeric_threshold = 80;
    private Float concept_topics_threshold = 0.45f;
    private Integer entities_threshold = 55;
    private Boolean one_sentence_mode = false;
    private Boolean process_html = false;
    private String callback = null;

    private String from_template_config_id = null;
    private String version = null;

	private DocumentConfiguration document = null;
	private CollectionConfiguration collection = null;

	public Configuration() {}

	public Configuration(String id, String name)
	{
		this.id = id;
		this.name = name;
	}

    public Configuration(String id, String name, String template)
    {
        this.id = id;
        this.name = name;
        this.template = template;
    }

	@XmlElement(name="template")
	public String getTemplate() { return template; }
	@XmlElement(name="id")
	public String getId() { return id; }
	@XmlElement(name="is_primary")
	public Boolean getIsPrimary() { return is_primary; }
    @XmlElement(name="auto_response")
    public Boolean getAutoResponse() { return auto_response; }
	@XmlElement(name="language")
	public String getLanguage() { return language; }
	@XmlElement(name="name")
	public String getName() { return name; }
    @XmlElement(name="from_template_config_id")
    public String getFromTemplateConfigId() { return from_template_config_id; }
    @XmlElement(name="version")
    public String getVersion() { return version; }
    @XmlElement(name = "modified")
    public Timestamp getModified() { return modified; }
    @XmlElement(name = "alphanumeric_threshold")
    public Integer getAlphanumericThreshold() { return alphanumeric_threshold; }
    @XmlElement(name = "concept_topics_threshold")
    public Float getConceptTopicsThreshold() { return concept_topics_threshold; }
    @XmlElement(name = "entities_threshold")
    public Integer getEntitiesThreshold() { return entities_threshold; }
    @XmlElement(name="one_sentence_mode")
    public Boolean getOneSentenceMode() { return one_sentence_mode; }
    @XmlElement(name="process_html")
    public Boolean getProcessHtml() { return process_html; }
    @XmlElement(name="callback")
    public String getCallback() { return callback; }
	@XmlElement(name="document")
	public DocumentConfiguration getDocument() { return document; }
	@XmlElement(name="collection")
	public CollectionConfiguration getCollection() { return collection; }

	public void setId(String id) { this.id = id; }
	public void setIsPrimary(Boolean is_primary) { this.is_primary = is_primary; }
    public void setAutoResponse(Boolean auto_response) { this.auto_response = auto_response; }
	public void setName(String name) { this.name = name; }
    public void setVersion(String version) { this.version = version; }
    public void setFromTemplateConfigId(String from_template_config_id) { this.from_template_config_id = from_template_config_id; }
	public void setLanguage(String lang) { language = lang; }
	public void setTemplate(String template) { this.template = template; }
    public void setModified(Timestamp timestamp) { this.modified = timestamp; }
    public void setAlphanumericThreshold(Integer alphanumeric_threshold) { this.alphanumeric_threshold = alphanumeric_threshold; }
    public void setConceptTopicsThreshold(Float concept_topics_threshold) { this.concept_topics_threshold = concept_topics_threshold; }
    public void setEntitiesThreshold(Integer entities_threshold) { this.entities_threshold = entities_threshold; }
    public void setOneSentenceMode(Boolean one_sentence_mode) { this.one_sentence_mode = one_sentence_mode; }
    public void setProcessHtml(Boolean process_html) { this.process_html = process_html; }
    public void setCallback(String callback) { this.callback = callback; }
    public void setCollection(CollectionConfiguration collection) { this.collection = collection; }
    public void setDocument(DocumentConfiguration document) { this.document = document;	}
}
