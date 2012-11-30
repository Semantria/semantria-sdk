public class Subscription {
	private String name;
	private String status;
	private String priority;
	private Long expiration_date;
	private Integer calls_balance;
	private Integer calls_limit;
	private Integer calls_limit_interval;
	private Integer docs_balance;
	private Integer docs_limit;
	private Integer docs_limit_interval;
	private Integer configurations_limit;
	private Integer blacklist_limit;
	private Integer categories_limit;
	private Integer queries_limit;
	private Integer entities_limit;
	private Integer sentiment_limit;
	private Integer characters_limit;
	private Integer batch_limit;
	private Integer collection_limit;
	private Integer auto_response_limit;
	private Integer processed_batch_limit;
	private Integer callback_batch_limit;
	private String limit_type;
	
	public String getName() { return name; }
	public String getStatus() { return status; }
	public String getPriority() { return priority; }
	public Long getExpirationDate() { return expiration_date; }
	public Integer getCallsBalance() { return calls_balance; }
	public Integer getCallsLimit() { return calls_limit; }
	public Integer getCallsLimitInterval() { return calls_limit_interval; }
	public Integer getDocsBalance() { return docs_balance; }
	public Integer getDocsLimit() { return docs_limit; }
	public Integer getDocsLimitInterval() { return docs_limit_interval; }
	public Integer getConfigurationsLimit() { return configurations_limit; }
	public Integer getBlacklistLimit() { return blacklist_limit; }
	public Integer getCategoriesLimit() { return categories_limit; }
	public Integer getQueriesLimit() { return queries_limit; }
	public Integer getEntitiesLimit() { return entities_limit; }
	public Integer getSentimentLimit() { return sentiment_limit; }
	public Integer getCharactersLimit() { return characters_limit; }
	public Integer getBatchLimit() { return batch_limit; }
	public Integer getCollectionLimit() { return collection_limit; }
	public Integer getAutoResponseLimit() { return auto_response_limit; }
	public Integer getProcessedBatchLimit() { return processed_batch_limit; }
	public Integer getCallbackBatchLimit() { return callback_batch_limit; }
	public String getLimitType() { return limit_type; }
	
	public void setName(String name) { this.name = name; }
	public void setStatus(String status) { this.status = status; }
	public void setPriority(String priority) { this.priority = priority; }
	public void setExpirationDate(Long expirationDate) { this.expiration_date = expirationDate; }
	public void setCallsBalance(Integer callsBalance) { this.calls_balance = callsBalance; }
	public void setCallsLimit(Integer callsLimit) { this.calls_limit = callsLimit; }
	public void setCallsLimitInterval(Integer callsLimitInterval) { this.calls_limit_interval = callsLimitInterval; }
	public void setDocsBalance(Integer docsBalance) { this.docs_balance = docsBalance; }
	public void setDocsLimit(Integer docsLimit) { this.docs_limit = docsLimit; }
	public void setDocsLimitInterval(Integer docsLimitInterval) { this.docs_limit_interval = docsLimitInterval; }
	public void setConfigurationsLimit(Integer configurationsLimit) { this.configurations_limit = configurationsLimit; }
	public void setBlacklistLimit(Integer blacklistLimit) { this.blacklist_limit = blacklistLimit; }
	public void setCategoriesLimit(Integer categoriesLimit) { this.categories_limit = categoriesLimit; }
	public void setQueriesLimit(Integer queriesLimit) { this.queries_limit = queriesLimit; }
	public void setEntitiesLimit(Integer entitiesLimit) { this.entities_limit = entitiesLimit; }
	public void setSentimentLimit(Integer sentimentLimit) { this.sentiment_limit = sentimentLimit; }
	public void setCharactersLimit(Integer charactersLimit) { this.characters_limit = charactersLimit; }
	public void setBatchLimit(Integer batchLimit) { this.batch_limit = batchLimit; }
	public void setCollectionLimit(Integer collectionLimit) { this.collection_limit = collectionLimit; }
	public void setAutoResponseLimit(Integer autoResponseLimit) { this.auto_response_limit = autoResponseLimit; }
	public void setProcessedBatchLimit(Integer processedBatchLimit) { this.processed_batch_limit = processedBatchLimit; }
	public void setCallbackBatchLimit(Integer callback_batch_limit) { this.callback_batch_limit = callback_batch_limit; }
	public void setLimitType(String limit_type) { this.limit_type = limit_type; }
}