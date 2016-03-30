package com.semantria.mapping.output.subscription;

import javax.xml.bind.annotation.XmlElement;

/**
 * Subscription's basic settings  mapping file
 */
public class BasicSettings
{

	private Integer configurations_limit = null;
	private Integer blacklist_limit = null;
	private Integer categories_limit = null;
	private Integer category_samples_limit = null;
	private Integer queries_limit = null;
	private Integer entities_limit = null;
	private Integer sentiment_limit = null;
	private Integer batch_limit = null;
	private Integer collection_limit = null;
	private Integer auto_response_limit = null;
	private Integer processed_batch_limit = null;
	private Integer callback_batch_limit = null;
	private Boolean return_source_text = null;
	private Integer characters_limit = null;
    private Integer output_data_limit = null;

	public BasicSettings() { }

	@XmlElement(name="configurations_limit")
	public Integer getConfigurationsLimit() { return configurations_limit; }
	@XmlElement(name="blacklist_limit")
	public Integer getBlacklistLimit(){ return blacklist_limit; }
	@XmlElement(name="categories_limit")
	public Integer getCategoriesLimit(){ return categories_limit; }
	@XmlElement(name="category_samples_limit")
	public Integer getCategorySamplesLimit(){ return category_samples_limit; }
	@XmlElement(name="queries_limit")
	public Integer getQueriesLimit(){ return queries_limit; }
	@XmlElement(name="entities_limit")
	public Integer getEntitiesLimit(){ return entities_limit; }
	@XmlElement(name="sentiment_limit")
	public Integer getSentimentLimit(){ return sentiment_limit; }
	@XmlElement(name="batch_limit")
	public Integer getBatchLimit() { return batch_limit; }
	@XmlElement(name="collection_limit")
	public Integer getCollectionLimit() { return collection_limit; }
	@XmlElement(name="auto_response_limit")
	public Integer getAutoResponseLimit() { return auto_response_limit; }
	@XmlElement(name="processed_batch_limit")
	public Integer getProcessedBatchLimit() { return processed_batch_limit; }
	@XmlElement(name="callback_batch_limit")
	public Integer getCallbackBatchLimit() { return callback_batch_limit;  }
	@XmlElement(name = "return_source_text")
	public Boolean getReturnSourceText() { return return_source_text; }
	@XmlElement(name="characters_limit")
	public Integer getCharactersLimit() { return characters_limit; }
    @XmlElement(name = "output_data_limit")
    public Integer getOutputDataLimit() { return output_data_limit; }

	public void setConfigurationsLimit(Integer configurations_limit) { this.configurations_limit = configurations_limit; }
	public void setBlacklistLimit(Integer blacklist_limit) { this.blacklist_limit = blacklist_limit; }
	public void setCategoriesLimit(Integer categories_limit) { this.categories_limit = categories_limit; }
	public void setCategorySamplesLimit(Integer category_samples_limit) { this.category_samples_limit = category_samples_limit; }
	public void setQueriesLimit(Integer queries_limit) { this.queries_limit = queries_limit; }
	public void setEntitiesLimit(Integer entities_limit) { this.entities_limit = entities_limit; }
	public void setSentimentLimit(Integer sentiment_limit) { this.sentiment_limit = sentiment_limit; }
	public void setBatchLimit(Integer batch_limit) { this.batch_limit = batch_limit; }
	public void setCollectionLimit(Integer collection_limit) { this.collection_limit = collection_limit; }
	public void setAutoResponseLimit(Integer auto_response_limit) { this.auto_response_limit = auto_response_limit; }
 	public void setProcessedBatchLimit(Integer processed_batch_limit) { this.processed_batch_limit = processed_batch_limit; }
	public void setCallbackBatchLimit(Integer callback_batch_limit) { this.callback_batch_limit = callback_batch_limit; }
 	public void setReturnSourceText(Boolean return_source_text) { this.return_source_text = return_source_text; }
	public void setCharactersLimit(Integer characters_limit) { this.characters_limit = characters_limit; }
    public void setOutputDataLimit(Integer output_data_limit) { this.output_data_limit = output_data_limit; }
}
