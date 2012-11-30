package com.semantria.objects.output;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="subscription")
public class Subscription 
{
    private String name;
	private String status;
	private String priority;
	private Long expiration_date;
	private Integer batch_limit;
	private Integer collection_limit;
	private Integer auto_response_limit;
	private Integer processed_batch_limit;
    private Integer callback_batch_limit;
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
	private String limit_type;

    @XmlElement(name="name")
    public String getName() { return name; }
	@XmlElement(name="status")
	public String getStatus() { return status; }
	@XmlElement(name="priority")
	public String getPriority() { return priority; }
	@XmlElement(name="expiration_date")
	public Long getExpirationDate() { return expiration_date; }
	@XmlElement(name="batch_limit")
	public Integer getBatchLimit() { return batch_limit; }
	@XmlElement(name="collection_limit")
	public Integer getCollectionLimit() { return collection_limit; }
	@XmlElement(name="auto_response_limit")
	public Integer getAutoResponseLimit() { return auto_response_limit; }
	@XmlElement(name="processed_batch_limit")
	public Integer getProcessedBatchLimit() { return processed_batch_limit; }
    @XmlElement(name="callback_batch_limit")
    public Integer getCallbackBatchLimit() { return callback_batch_limit; }
    @XmlElement(name="calls_balance")
    public Integer getCallsBalance() {return calls_balance;}
    @XmlElement(name="calls_limit")
    public Integer getCallsLimit() {return calls_limit;}
    @XmlElement(name="calls_limit_interval")
    public Integer getCallsLimitInterval() {return calls_limit_interval;}
    @XmlElement(name="docs_balance")
    public Integer getDocsBalance() {return docs_balance;}
    @XmlElement(name="docs_limit")
    public Integer getDocsLimit() {return docs_limit;}
    @XmlElement(name="docs_limit_interval")
    public Integer getDocsLimitInterval() {return docs_limit_interval;}
    @XmlElement(name="configurations_limit")
    public Integer getConfigurationsLimit() {return configurations_limit;}
    @XmlElement(name="blacklist_limit")
    public Integer getBlacklistLimit() {return blacklist_limit;}
    @XmlElement(name="categories_limit")
    public Integer getCategoriesLimit() {return categories_limit;}
    @XmlElement(name="queries_limit")
    public Integer getQueriesLimit() {return queries_limit;}
    @XmlElement(name="entities_limit")
    public Integer getEntitiesLimit() {return entities_limit;}
    @XmlElement(name="sentiment_limit")
    public Integer getSentimentLimit() {return sentiment_limit;}
    @XmlElement(name="characters_limit")
    public Integer getCharactersLimit() {return characters_limit;}
	@XmlElement(name="limit_type")
	public String getLimitType() {return limit_type;}

    public void setName(String name) { this.name = name; }
    public void setStatus(String sstatus) { status = sstatus; }
	public void setPriority(String priority) { this.priority = priority; }
	public void setExpirationDate(Long exp_date) { expiration_date = exp_date; }
	public void setBatchLimit(Integer sbatch_limit) { batch_limit = sbatch_limit; }
	public void setCollectionLimit(Integer scollection_limit) { collection_limit= scollection_limit; }
	public void setAutoResponseLimit(Integer limit) { auto_response_limit = limit; }
	public void setProcessedBatchLimit(Integer limit) { processed_batch_limit = limit; }
    public void setCallbackBatchLimit(Integer limit) { callback_batch_limit = limit; }
    public void setCallsBalance(Integer calls_balance) {this.calls_balance = calls_balance;}
    public void setCallsLimit(Integer calls_limit) {this.calls_limit = calls_limit;}
    public void setCallsLimitInterval(Integer calls_limit_interval) {this.calls_limit_interval = calls_limit_interval;}
    public void setDocsBalance(Integer docs_balance) {this.docs_balance = docs_balance;}
    public void setDocsLimit(Integer docs_limit) {this.docs_limit = docs_limit;}
    public void setDocsLimitInterval(Integer docs_limit_interval) {this.docs_limit_interval = docs_limit_interval;}
    public void setConfigurationsLimit(Integer configurations_limit) {this.configurations_limit = configurations_limit;}
    public void setBlacklistLimit(Integer blacklist_limit) {this.blacklist_limit = blacklist_limit;}
    public void setCategoriesLimit(Integer categories_limit) {this.categories_limit = categories_limit;}
    public void setQueriesLimit(Integer queries_limit) {this.queries_limit = queries_limit;}
    public void setEntitiesLimit(Integer entities_limit) {this.entities_limit = entities_limit;}
    public void setSentimentLimit(Integer sentiment_limit) {this.sentiment_limit = sentiment_limit;}
    public void setCharactersLimit(Integer characters_limit) {this.characters_limit = characters_limit;}
	public void setLimitType(String limit_type) { this.limit_type = limit_type; }
}
