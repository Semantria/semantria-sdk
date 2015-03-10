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
	private Long settings_calls_balance = null;
	private Long settings_calls_limit = null;
	private Integer settings_calls_limit_interval = null;
    private Long polling_calls_balance = null;
    private Long polling_calls_limit = null;
    private Integer polling_calls_limit_interval = null;
    private Long data_calls_balance = null;
    private Long data_calls_limit = null;
    private Integer data_calls_limit_interval = null;
    private Long docs_suggested = null;
    private Integer docs_suggested_interval = null;
    private Integer app_seats_permitted = null;
    private Integer app_seats_allocated = null;

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
	@XmlElement(name="settings_calls_balance")
	public Long getSettingsCallsBalance() { return settings_calls_balance; }
	@XmlElement(name="settings_calls_limit")
	public Long getSettingsCallsLimit() { return settings_calls_limit; }
	@XmlElement(name="settings_calls_limit_interval")
	public Integer getSettingsCallsLimitInterval() { return settings_calls_limit_interval; }
    @XmlElement(name="polling_calls_balance")
    public Long getPollingCallsBalance() { return polling_calls_balance; }
    @XmlElement(name="polling_calls_limit")
    public Long getPollingCallsLimit() { return polling_calls_limit; }
    @XmlElement(name="polling_calls_limit_interval")
    public Integer getPollingCallsLimitInterval() { return polling_calls_limit_interval; }
    @XmlElement(name="data_calls_balance")
    public Long getDataCallsBalance() { return data_calls_balance; }
    @XmlElement(name="data_calls_limit")
    public Long getDataCallsLimit() { return data_calls_limit; }
    @XmlElement(name="data_calls_limit_interval")
    public Integer getDataCallsLimitInterval() { return data_calls_limit_interval; }
    @XmlElement(name="docs_suggested")
    public Long getDocsSuggested() { return docs_suggested; }
    @XmlElement(name="docs_suggested_interval")
    public Integer getDocsSuggestedInterval() { return docs_suggested_interval; }
    @XmlElement(name="app_seats_permitted")
    public Integer getPermittedSeats() { return app_seats_permitted; }
    @XmlElement(name="app_seats_allocated")
    public Integer getAllocatedSeats() { return app_seats_allocated; }

    public void setPriority(String priority) { this.priority = priority; }
	public void setExpirationDate(Long expirationDate) { this.expiration_date = expirationDate; }
	public void setLimitType(String limitType) { this.limit_type = limitType; }
	public void setDocsBalance(Long docsBalance) { this.docs_balance = docsBalance; }
	public void setDocsLimit(Long docsLimit) { this.docs_limit = docsLimit; }
	public void setDocsLimitInterval(Integer docsLimitInterval) { this.docs_limit_interval = docsLimitInterval; }
	public void setSettingsCallsBalance(Long settingsCallsBalance) { this.settings_calls_balance = settingsCallsBalance; }
	public void setSettingsCallsLimit(Long settingsCallsLimit) { this.settings_calls_limit = settingsCallsLimit; }
	public void setSettingsCallsLimitInterval(Integer settingsCallsLimitInterval) { this.settings_calls_limit_interval = settingsCallsLimitInterval; }
    public void setPollingCallsBalance(Long pollingCallsBalance) { this.polling_calls_balance = pollingCallsBalance; }
    public void setPollingCallsLimit(Long pollingCallsLimit) { this.polling_calls_limit = pollingCallsLimit; }
    public void setPollingCallsLimitInterval(Integer pollingCallsLimitInterval) { this.polling_calls_limit_interval = pollingCallsLimitInterval; }
    public void setDataCallsBalance(Long dataCallsBalance) { this.data_calls_balance = dataCallsBalance; }
    public void setDataCallsLimit(Long dataCallsLimit) { this.data_calls_limit = dataCallsLimit; }
    public void setDataCallsLimitInterval(Integer dataCallsLimitInterval) { this.data_calls_limit_interval = dataCallsLimitInterval; }
    public void setDocsSuggested(Long docs_suggested) { this.docs_suggested = docs_suggested; }
    public void setDocsSuggestedInterval(Integer docs_suggested_interval) { this.docs_suggested_interval = docs_suggested_interval; }
    public void setPermittedSeats(Integer seats_permitted) { this.app_seats_permitted = seats_permitted; }
    public void setAllocatedSeats(Integer seats_allocated) { this.app_seats_allocated = seats_allocated; }
}
