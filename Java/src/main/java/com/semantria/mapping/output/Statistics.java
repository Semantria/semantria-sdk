package com.semantria.mapping.output;

import com.semantria.mapping.output.statistics.Details;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "statistics")
public class Statistics
{
	private String name = null;
	private String status = null;
	private String latest_used_app = null;
	private String used_apps = null;
	private Long overall_texts = null;
	private Long overall_calls = null;
	private Long calls_settings = null;
    private Long calls_polling = null;
	private Long calls_data = null;
	private Long overall_docs = null;
	private Long docs_processed = null;
	private Long docs_failed = null;
	private Long docs_responded = null;
	private Long overall_colls = null;
	private Long colls_processed = null;
	private Long colls_failed = null;
	private Long colls_responded = null;
	private Long overall_batches = null;
	private Long colls_documents = null;
    private Long overall_exceeded = null;
	private List<Details> configurations = null;
	public Statistics() {}

	@XmlElement(name = "name")
	public String getName() { return name; }
	@XmlElement(name = "status")
	public String getStatus() { return status != null ? status.toLowerCase() : status; }
	@XmlElement(name = "latest_used_app")
	public String getLatestUsedApp() { return latest_used_app;}
	@XmlElement(name = "used_apps")
	public String getUsedApps() { return used_apps; }
	@XmlElement(name = "overall_texts")
	public Long getOverallTexts() { return overall_texts; }
	@XmlElement(name = "overall_calls")
	public Long getOverallCalls() { return overall_calls; }
	@XmlElement(name = "calls_settings")
	public Long getCallsSettings() { return calls_settings; }
    @XmlElement(name = "calls_polling")
    public Long getCallsPolling() { return calls_polling; }
	@XmlElement(name = "calls_data")
	public Long getCallsData() { return calls_data; }
	@XmlElement(name = "overall_docs")
	public Long getOverallDocs() { return overall_docs; }
	@XmlElement(name = "docs_processed")
	public Long getDocsProcessed() { return docs_processed; }
	@XmlElement(name = "docs_failed")
	public Long getDocsFailed() { return docs_failed; }
	@XmlElement(name = "docs_responded")
	public Long getDocsResponded() { return docs_responded; }
	@XmlElement(name = "overall_colls")
	public Long getOverallColls() { return overall_colls; }
	@XmlElement(name = "colls_processed")
	public Long getCollsProcessed() { return colls_processed; }
	@XmlElement(name = "colls_failed")
	public Long getCollsFailed() { return colls_failed; }
	@XmlElement(name = "colls_responded")
 	public Long getCollsResponded() { return colls_responded; }
	@XmlElement(name = "overall_batches")
	public Long getOverallBatches() { return overall_batches; }
	@XmlElement(name = "colls_documents")
	public Long getCollsDocuments() { return colls_documents; }
	@XmlElementWrapper(name = "configurations")
	@XmlElement(name = "configuration")
	public List<Details> getConfigurations() { return configurations; }
    @XmlElement(name = "overall_exceeded")
    public Long getOverallExceeded() { return overall_exceeded; }

	public void setName(String name) { this.name = name; }
	public void setStatus(String status) { this.status = status; }
	public void setLatestUsedApp(String latest_used_app) { this.latest_used_app = latest_used_app; }
	public void setUsedApps(String used_apps) { this.used_apps = used_apps; }
	public void setOverallTexts(Long overall_texts) { this.overall_texts = overall_texts; }
	public void setOverallCalls(Long overall_calls) { this.overall_calls = overall_calls; }
	public void setCallsSettings(Long calls_settings) { this.calls_settings = calls_settings; }
    public void setCallsPolling(Long calls_polling) { this.calls_polling = calls_polling; }
	public void setCallsData(Long calls_data) { this.calls_data = calls_data; }
	public void setOverallDocs(Long overall_docs) { this.overall_docs = overall_docs; }
	public void setDocsProcessed(Long docs_processed) { this.docs_processed = docs_processed; }
	public void setDocsFailed(Long docs_failed) { this.docs_failed = docs_failed; }
	public void setDocsResponded(Long docs_responded) { this.docs_responded = docs_responded; }
	public void setOverallColls(Long overall_colls) { this.overall_colls = overall_colls; }
	public void setCollsProcessed(Long colls_processed) { this.colls_processed = colls_processed; }
	public void setCollsFailed(Long colls_failed) { this.colls_failed = colls_failed; }
	public void setCollsResponded(Long colls_responded) { this.colls_responded = colls_responded; }
	public void setOverallBatches(Long overall_batches) { this.overall_batches = overall_batches; }
	public void setCollsDocuments(Long colls_documents) { this.colls_documents = colls_documents; }
 	public void setConfigurations(List<Details> details) { this.configurations = details; }
    public void setOverallExceeded(Long overall_exceeded) { this.overall_exceeded = overall_exceeded; }
}
