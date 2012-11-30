package com.semantria.objects.output;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="status")
public class ServiceStatus 
{
	private String service_status;
	private String api_version;
	private String service_version;
    private String supported_encoding;
    private String supported_compression;
	private List<String> supported_languages;
	
	@XmlElement(name="service_status")
	public String getServiceStatus() { return service_status; }
	@XmlElement(name="api_version")
	public String getApiVersion() { return api_version; }
	@XmlElement(name="service_version")
	public String getServiceVersion() { return service_version; }
    @XmlElement(name="supported_encoding")
    public String getSupportedEncoding() { return supported_encoding; }
    @XmlElement(name="supported_compression")
    public String getSupportedCompression() { return supported_compression; }
	@XmlElementWrapper(name="supported_languages")
	@XmlElement(name="language")
	public List<String> getSupportedLanguages() { return supported_languages; }
	
	public void setServiceStatus(String status) { service_status = status; }
	public void setApiVersion(String version) { api_version = version; }
	public void setServiceVersion(String version) { service_version = version; }
    public void setSupportedEncoding(String supported_encoding) {this.supported_encoding = supported_encoding;}
    public void setSupportedCompression(String supported_compression) {this.supported_compression = supported_compression;}
    public void setSupportedLanguages(List<String> list) { supported_languages = list; }
}
