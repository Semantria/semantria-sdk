package com.semantria.mapping.output.subscription;

import javax.xml.bind.annotation.XmlElement;

/**
 * Subscription's billing settings mapping class
 */

public class BillingSettings
{
	private Long expiration_date = null;
	private Long docs_balance = null;
	private Long settings_calls_balance = null;
	private Long settings_calls_limit = null;
	private Long settings_calls_limit_interval = null;
    private Long polling_calls_balance = null;
    private Long polling_calls_limit = null;
    private Long polling_calls_limit_interval = null;
    private Long data_calls_balance = null;
    private Long data_calls_limit = null;
    private Long data_calls_limit_interval = null;
    private Integer app_seats_permitted = null;
    private Integer app_seats_allocated = null;
    private Integer job_ids_permitted = null;
    private Integer job_ids_allocated = null;
	private Long storage_docs_limit = null;
	private Boolean is_ai_assembler_enabled = null;

	public BillingSettings(){ }

	@XmlElement(name="expiration_date")
	public Long getExpirationDate() { return expiration_date; }
	@XmlElement(name="docs_balance")
	public Long getDocsBalance() { return docs_balance; }
	@XmlElement(name="settings_calls_balance")
	public Long getSettingsCallsBalance() { return settings_calls_balance; }
	@XmlElement(name="settings_calls_limit")
	public Long getSettingsCallsLimit() { return settings_calls_limit; }
	@XmlElement(name="settings_calls_limit_interval")
	public Long getSettingsCallsLimitInterval() { return settings_calls_limit_interval; }
    @XmlElement(name="polling_calls_balance")
    public Long getPollingCallsBalance() { return polling_calls_balance; }
    @XmlElement(name="polling_calls_limit")
    public Long getPollingCallsLimit() { return polling_calls_limit; }
    @XmlElement(name="polling_calls_limit_interval")
    public Long getPollingCallsLimitInterval() { return polling_calls_limit_interval; }
    @XmlElement(name="data_calls_balance")
    public Long getDataCallsBalance() { return data_calls_balance; }
    @XmlElement(name="data_calls_limit")
    public Long getDataCallsLimit() { return data_calls_limit; }
    @XmlElement(name="data_calls_limit_interval")
    public Long getDataCallsLimitInterval() { return data_calls_limit_interval; }
    @XmlElement(name="app_seats_permitted")
    public Integer getPermittedSeats() { return app_seats_permitted; }
    @XmlElement(name="app_seats_allocated")
    public Integer getAllocatedSeats() { return app_seats_allocated; }
    @XmlElement(name="job_ids_permitted")
    public Integer getPermittedJobIds() { return job_ids_permitted; }
    @XmlElement(name="job_ids_allocated")
    public Integer getAllocatedJobIds() { return job_ids_allocated; }
	@XmlElement(name="storage_docs_limit")
	public Long getStorageDocsLimit() { return (storage_docs_limit == null) ? 0L : storage_docs_limit; }
	@XmlElement(name="is_ai_assembler_enabled")
	public Boolean getIsAiAssemblerEnabled() { return (is_ai_assembler_enabled == null) ? false : is_ai_assembler_enabled; }

	public void setExpirationDate(Long expirationDate) { this.expiration_date = expirationDate; }
	public void setDocsBalance(Long docsBalance) { this.docs_balance = docsBalance; }
	public void setSettingsCallsBalance(Long settingsCallsBalance) { this.settings_calls_balance = settingsCallsBalance; }
	public void setSettingsCallsLimit(Long settingsCallsLimit) { this.settings_calls_limit = settingsCallsLimit; }
	public void setSettingsCallsLimitInterval(Long settingsCallsLimitInterval) { this.settings_calls_limit_interval = settingsCallsLimitInterval; }
    public void setPollingCallsBalance(Long pollingCallsBalance) { this.polling_calls_balance = pollingCallsBalance; }
    public void setPollingCallsLimit(Long pollingCallsLimit) { this.polling_calls_limit = pollingCallsLimit; }
    public void setPollingCallsLimitInterval(Long pollingCallsLimitInterval) { this.polling_calls_limit_interval = pollingCallsLimitInterval; }
    public void setDataCallsBalance(Long dataCallsBalance) { this.data_calls_balance = dataCallsBalance; }
    public void setDataCallsLimit(Long dataCallsLimit) { this.data_calls_limit = dataCallsLimit; }
    public void setDataCallsLimitInterval(Long dataCallsLimitInterval) { this.data_calls_limit_interval = dataCallsLimitInterval; }
    public void setPermittedSeats(Integer seats_permitted) { this.app_seats_permitted = seats_permitted; }
    public void setAllocatedSeats(Integer seats_allocated) { this.app_seats_allocated = seats_allocated; }
    public void setPermittedJobIds(Integer job_ids_permitted) { this.job_ids_permitted = job_ids_permitted; }
    public void setAllocatedJobIds(Integer job_ids_allocated) { this.job_ids_allocated = job_ids_allocated; }
	public void setStorageDocsLimit(Long storage_docs_limit) { this.storage_docs_limit = storage_docs_limit; }
	public void setIsAiAssemblerEnabled(Boolean is_ai_assembler_enabled) { this.is_ai_assembler_enabled = is_ai_assembler_enabled; }

}
