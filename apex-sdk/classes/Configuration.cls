public class Configuration {
	private String config_id;
	private String template; 
	private String name;
	private Boolean is_primary;
	private Boolean one_sentence;
	private Boolean auto_responding;
	private String language;
	private Integer chars_threshold;
	private String callback;
	private DocumentFields document; 
	private CollectionFields collection; 
	
	public String getConfigId() { return config_id; }
	public String getTemplate() { return template; } 
	public String getName() { return name; }
	public Boolean isAutoResponding() { return auto_responding; }
	public Boolean isOneSentence() {return one_sentence;}
	public Boolean isPrimary() { return is_primary; }
	public Integer getCharsThreshold() { return chars_threshold; }
	public String getLanguage() { return language; }
	public String getCallbackUrl() { return callback; }
	//document
	public Boolean isDocumentParametersSetted() { return null != document;}
	public Integer getDocConceptTopicsLimit() { return (null != document ? document.concept_topics_limit : null); }
	public Integer getDocQueryTopicsLimit() { return (null != document ? document.query_topics_limit : null); }
	public Integer getDocNamedEntitiesLimit() { return (null != document ? document.named_entities_limit : null); }
	public Integer getDocUserEntitiesLimit() { return (null != document ? document.user_entities_limit : null); }
	public Integer getDocThemesLimit() { return (null != document ? document.themes_limit : null); }
	public Integer getEntityThemesLimit() { return (null != document ? document.entity_themes_limit : null); }
	public Integer getPhrasesLimit() { return (null != document ? document.phrases_limit : null); }
	public Integer getSummarylimit() { return (null != document ? document.summary_limit : null); }
	//collection
	public Boolean isCollectionParametersSetted() { return null != collection;}
	public Integer getFacetsLimit() { return (null != collection ? collection.facets_limit : null); }
	public Integer getFacetAttsLimit() { return (null != collection ? collection.facet_atts_limit : null); }
	public Integer getCollConceptTopicsLimit() { return (null != collection ? collection.concept_topics_limit : null); }
	public Integer getCollQueryTopicsLimit() { return (null != collection ? collection.query_topics_limit : null); }
	public Integer getCollNamedEntitiesLimit() { return (null != collection ? collection.named_entities_limit : null); }
	public Integer getCollThemesLimit() { return (null != collection ? collection.themes_limit : null); }
	
	public void setConfigId(String config_id) { this.config_id = config_id; }
	public void setTemplate(String template) { this.template = template; }
	public void setName(String name) { this.name = name; }
	public void setAutoResponding(Boolean autoResponding) { this.auto_responding = autoResponding; }
	public void setOneSentence(Boolean oneSentence) { this.one_sentence = oneSentence; }
	public void setIsPrimary(Boolean isPrimary) { this.is_primary = isPrimary; }
	public void setCharsThreshold(Integer charsThreshold) { this.chars_threshold = charsThreshold; }
	public void setCallbackUrl(String callbackUrl) { this.callback = callbackUrl; }
	//document	
	public void setDocConceptTopicsLimit(Integer conceptTopicsLimit) { if(null == document){document = new DocumentFields();} document.concept_topics_limit = conceptTopicsLimit; }
	public void setDocQueryTopicsLimit(Integer queryTopicsLimit) { if(null == document){document = new DocumentFields();} document.query_topics_limit = queryTopicsLimit; }
	public void setDocNamedEntitiesLimit(Integer namedEntitiesLimit) { if(null == document){document = new DocumentFields();} document.named_entities_limit = namedEntitiesLimit; }
	public void setDocUserEntitiesLimit(Integer userEntitiesLimit) { if(null == document){document = new DocumentFields();} document.user_entities_limit = userEntitiesLimit; }
	public void setDocThemesLimit(Integer themesLimit) { if(null == document){document = new DocumentFields();} document.themes_limit = themesLimit; }
	public void setEntityThemesLimit(Integer entityThemesLimit) { if(null == document){document = new DocumentFields();} document.entity_themes_limit = entityThemesLimit; }
	public void setPhrasesLimit(Integer phrasesLimit) { if(null == document){document = new DocumentFields();} document.phrases_limit = phrasesLimit; }
	public void setSummaryLimit(Integer summaryLimit) { if(null == document){document = new DocumentFields();} document.summary_limit = summaryLimit; }
	//collection
	public void setFacetsLimit(Integer facetsLimit) { if(null == collection){collection = new CollectionFields();} collection.facets_limit = facetsLimit; }
	public void setFacetAttsLimit(Integer facetAttsLimit) { if(null == collection){collection = new CollectionFields();} collection.facet_atts_limit = facetAttsLimit; }
	public void setCollConceptTopicsLimit(Integer conceptTopicsLimit) { if(null == collection){collection = new CollectionFields();} collection.concept_topics_limit = conceptTopicsLimit; }
	public void setCollQueryTopicsLimit(Integer queryTopicsLimit) { if(null == collection){collection = new CollectionFields();} collection.query_topics_limit = queryTopicsLimit; }
	public void setCollNamedEntitiesLimit(Integer namedEntitiesLimit) { if(null == collection){collection = new CollectionFields();} collection.named_entities_limit = namedEntitiesLimit; }
	public void setCollThemesLimit(Integer themesLimit) { if(null == collection){collection = new CollectionFields();} collection.themes_limit = themesLimit; }
	
	public void setLanguage(String language) { 
		this.language = language;
	} 
	
	class DocumentFields {
		public Integer concept_topics_limit;
		public Integer query_topics_limit;
		public Integer named_entities_limit;
		public Integer user_entities_limit;
		public Integer entity_themes_limit;
		public Integer themes_limit;
		public Integer phrases_limit;
		public Integer summary_limit;
	}
	
	class CollectionFields {
		public Integer facets_limit;
		public Integer facet_atts_limit;
		public Integer concept_topics_limit;
		public Integer query_topics_limit;
		public Integer named_entities_limit;
		public Integer themes_limit;
	}
}