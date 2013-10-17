package com.semantria.mapping.output.subscription;

import com.semantria.mapping.output.subscription.features.Collection;
import com.semantria.mapping.output.subscription.features.Document;

import javax.xml.bind.annotation.XmlElement;

/**
 * Subscription's feature settings mapping class
 */
public class FeatureSettings
{
	private Collection collection = null;
	private Document document = null;
	private String supported_languages = null;
    private Boolean html_processing = null;

	public FeatureSettings() { }

	@XmlElement(name = "collection")
	public Collection getCollection() { return collection; }
	@XmlElement(name = "document")
	public Document getDocument() { return document; }
	@XmlElement(name = "supported_languages")
	public String getSupportedLanguages() { return supported_languages; }
    @XmlElement(name = "html_processing")
    public Boolean getHtmlProcessing() { return html_processing; }

	public void setCollection(Collection collection) { this.collection = collection; }
	public void setDocument(Document document) { this.document = document; }
	public void setSupportedLanguages(String supported_languages) { this.supported_languages = supported_languages; }
    public void setHtmlProcessing(Boolean html_processing) { this.html_processing = html_processing; }
}
