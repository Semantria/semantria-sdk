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

	public FeatureSettings() { }

	@XmlElement(name = "collection")
	public Collection getCollection() { return collection; }
	@XmlElement(name = "document")
	public Document getDocument() { return document; }
	@XmlElement(name = "supported_languages")
	public String getSupportedLanguages() { return supported_languages; }

	public void setCollection(Collection collection) { this.collection = collection; }
	public void setDocument(Document document) { this.document = document; }
	public void setSupportedLanguages(String supported_languages) { this.supported_languages = supported_languages; }
}
