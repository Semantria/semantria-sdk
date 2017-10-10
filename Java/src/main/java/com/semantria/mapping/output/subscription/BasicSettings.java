package com.semantria.mapping.output.subscription;

import javax.xml.bind.annotation.XmlElement;

/**
 * Subscription's basic settings  mapping file
 */
public class BasicSettings
{
	private Integer configurations_limit = null;
    private Integer summary_size_limit = null;
	private Integer blacklist_limit = null;
	private Integer concept_topics_limit = null;
	private Integer concept_topic_samples_limit = null;
	private Integer query_topics_limit = null;
    private Integer near_operator_limit = null;
    private Integer near_operator_distance = null;
    private Integer queries_depth_level = null;
    private Integer taxonomy_limit = null;
    private Integer taxonomy_depth_limit = null;
    private Integer taxonomy_topics_limit = null;
    private Integer user_entities_limit = null;
    private Integer sentiment_phrases_limit = null;
    private Integer document_length = null;
	private Integer incoming_batch_limit = null;
	private Integer collection_limit = null;
	private Integer auto_response_batch_limit = null;
	private Integer polling_batch_limit = null;
	private Integer callback_batch_limit = null;
	private Boolean return_source_text = null;
	private Integer metadata_limit = null;

	public BasicSettings() { }

	@XmlElement(name="configurations_limit")
	public Integer getConfigurationsLimit() { return configurations_limit; }
	@XmlElement(name="blacklist_limit")
	public Integer getBlacklistLimit(){ return blacklist_limit; }
	@XmlElement(name="concept_topics_limit")
	public Integer getConceptTopicsLimit(){ return concept_topics_limit; }
	@XmlElement(name="concept_topic_samples_limit")
	public Integer getConceptTopicSamplesLimit(){ return concept_topic_samples_limit; }
	@XmlElement(name="query_topics_limit")
	public Integer getQueryTopicsLimit(){ return query_topics_limit; }
	@XmlElement(name="user_entities_limit")
	public Integer getUserEntitiesLimit(){ return user_entities_limit; }
	@XmlElement(name="incoming_batch_limit")
	public Integer getIncomingBatchLimit() { return incoming_batch_limit; }
	@XmlElement(name="collection_limit")
	public Integer getCollectionLimit() { return collection_limit; }
	@XmlElement(name="auto_response_batch_limit")
	public Integer getAutoResponseBatchLimit() { return auto_response_batch_limit; }
	@XmlElement(name="polling_batch_limit")
	public Integer getPollingBatchLimit() { return polling_batch_limit; }
	@XmlElement(name="callback_batch_limit")
	public Integer getCallbackBatchLimit() { return callback_batch_limit;  }
	@XmlElement(name = "return_source_text")
	public Boolean getReturnSourceText() { return return_source_text; }
    @XmlElement(name = "summary_size_limit")
    public Integer getSummarySizeLimit() { return summary_size_limit; }
    @XmlElement(name = "near_operator_limit")
    public Integer getNearOperatorLimit() { return near_operator_limit; }
    @XmlElement(name = "near_operator_distance")
    public Integer getNearOperatorDistance() { return near_operator_distance; }
    @XmlElement(name = "queries_depth_level")
    public Integer getQueriesDepthLevel() { return queries_depth_level; }
    @XmlElement(name = "taxonomy_limit")
    public Integer getTaxonomyLimit() { return taxonomy_limit; }
    @XmlElement(name = "taxonomy_depth_limit")
    public Integer getTaxonomyDepthLimit() { return taxonomy_depth_limit; }
    @XmlElement(name = "taxonomy_topics_limit")
    public Integer getTaxonomyTopicsLimit() { return taxonomy_topics_limit; }
    @XmlElement(name = "sentiment_phrases_limit")
    public Integer getSentimentPhrasesLimit() { return sentiment_phrases_limit; }
    @XmlElement(name = "document_length")
    public Integer getDocumentLength() { return document_length; }
	@XmlElement(name="metadata_limit")
	public Integer getMetadataLimit() { return metadata_limit; }

	public void setConfigurationsLimit(Integer configurations_limit) { this.configurations_limit = configurations_limit; }
	public void setBlacklistLimit(Integer blacklist_limit) { this.blacklist_limit = blacklist_limit; }
	public void setConceptTopicsLimit(Integer concept_topics_limit) { this.concept_topics_limit = concept_topics_limit; }
	public void setConceptTopicSamplesLimit(Integer concept_topic_samples_limit) { this.concept_topic_samples_limit = concept_topic_samples_limit; }
	public void setQueryTopicsLimit(Integer query_topics_limit) { this.query_topics_limit = query_topics_limit; }
	public void setUserEntitiesLimit(Integer user_entities_limit) { this.user_entities_limit = user_entities_limit; }
	public void setIncomingBatchLimit(Integer incoming_batch_limit) { this.incoming_batch_limit = incoming_batch_limit; }
	public void setCollectionLimit(Integer collection_limit) { this.collection_limit = collection_limit; }
	public void setAutoResponseBatchLimit(Integer auto_response_batch_limit) { this.auto_response_batch_limit = auto_response_batch_limit; }
 	public void setPollingBatchLimit(Integer polling_batch_limit) { this.polling_batch_limit = polling_batch_limit; }
	public void setCallbackBatchLimit(Integer callback_batch_limit) { this.callback_batch_limit = callback_batch_limit; }
 	public void setReturnSourceText(Boolean return_source_text) { this.return_source_text = return_source_text; }
    public void setSummarySizeLimit(Integer summary_size_limit) { this.summary_size_limit = summary_size_limit; }
    public void setNearOperatorLimit(Integer near_operator_limit) { this.near_operator_limit = near_operator_limit; }
    public void setNearOperatorDistance(Integer near_operator_distance) { this.near_operator_distance = near_operator_distance; }
    public void setQueriesDepthLevel(Integer queries_depth_level) { this.queries_depth_level = queries_depth_level; }
    public void setTaxonomyLimit(Integer taxonomy_limit) { this.taxonomy_limit = taxonomy_limit; }
    public void setTaxonomyDepthLimit(Integer taxonomy_depth_limit) { this.taxonomy_depth_limit = taxonomy_depth_limit; }
    public void setTaxonomyTopicsLimit(Integer taxonomy_topics_limit) { this.taxonomy_topics_limit = taxonomy_topics_limit; }
    public void setSentimentPhrasesLimit(Integer sentiment_phrases_limit) { this.sentiment_phrases_limit = sentiment_phrases_limit; }
    public void setDocumentLength(Integer document_length) { this.document_length = document_length; }
	public void setMetadataLimit(Integer metadata_limit) { this.metadata_limit = metadata_limit; }

}
