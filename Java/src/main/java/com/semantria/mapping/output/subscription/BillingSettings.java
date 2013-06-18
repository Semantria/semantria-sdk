package com.semantria.mapping.output.subscription;

import javax.xml.bind.annotation.XmlElement;

/**
 * Subscription's billing settings mapping class
 */

public class BillingSettings
{
	private String priority = null;
	private Long expiration_date = null;
	private String limit_type = null;
	private Long docs_balance = null;
	private Long docs_limit = null;
	private Integer docs_limit_interval = null;
	private Long calls_balance = null;
	private Long calls_limit = null;
	private Integer calls_limit_interval = null;
    private Long docs_suggested = null;
    private Integer docs_suggested_interval = null;

	public BillingSettings(){ }

	@XmlElement(name="priority")
	public String getPriority() { return priority; }
	@XmlElement(name="expiration_date")
	public Long getExpirationDate() { return expiration_date; }
	@XmlElement(name = "limit_type")
	public String getLimitType() { return limit_type; }
	@XmlElement(name="docs_balance")
	public Long getDocsBalance() { return docs_balance; }
	@XmlElement(name="docs_limit")
	public Long getDocsLimit() { return docs_limit; }
	@XmlElement(name="docs_limit_interval")
	public Integer getDocsLimitInterval() { return docs_limit_interval; }
	@XmlElement(name="calls_balance")
	public Long getCallsBalance() { return calls_balance; }
	@XmlElement(name="calls_limit")
	public Long getCallsLimit() { return calls_limit; }
	@XmlElement(name="calls_limit_interval")
	public Integer getCallsLimitInterval() { return calls_limit_interval; }

    @XmlElement(name="docs_suggested")
    public Long getDocsSuggested() { return docs_suggested; }
    @XmlElement(name="docs_suggested_interval")
    public Integer getDocsSuggestedInterval() { return docs_suggested_interval; }

    public void setPriority(String priority) { this.priority = priority; }
	public void setExpirationDate(Long expiration_date) { this.expiration_date = expiration_date; }
	public void setLimitType(String limit_type) { this.limit_type = limit_type; }
	public void setDocsBalance(Long docs_balance) { this.docs_balance = docs_balance; }
	public void setDocsLimit(Long docs_limit) { this.docs_limit = docs_limit; }
	public void setDocsLimitInterval(Integer docs_limit_interval) { this.docs_limit_interval = docs_limit_interval; }
	public void setCallsBalance(Long calls_balance) { this.calls_balance = calls_balance; }
	public void setCallsLimit(Long calls_limit) { this.calls_limit = calls_limit; }
	public void setCallsLimitInterval(Integer calls_limit_interval) { this.calls_limit_interval = calls_limit_interval; }
    public void setDocsSuggested(Long docs_suggested) { this.docs_suggested = docs_suggested; }
    public void setDocsSuggestedInterval(Integer docs_suggested_interval) { this.docs_suggested_interval = docs_suggested_interval; }
}
