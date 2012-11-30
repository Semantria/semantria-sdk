public class ServiceStatus {
    private String service_status;
    private String api_version;
    private String service_version;
    private String supported_encoding;
    private String supported_compression;
    private Integer processed_response_limit;
    private List<String> supported_languages;
    
    public String getServiceStatus() { return service_status; }
    public String getApiVersion() { return api_version; }
    public String getServiceVersion() { return service_version; }
    public String getSupportedEncoding() { return supported_encoding; }
    public String getSupportedCompression() { return supported_compression; }
    public Integer getProcessedResponseLimit() { return processed_response_limit; }
    public List<String> getSupportedLanguages() { return supported_languages; }
    
    public void setServiceStatus(String serviceStatus) { this.service_status = serviceStatus; }
    public void setApiVersion(String apiVersion) { this.api_version = apiVersion; }
    public void setServiceVersion(String serviceVersion) { this.service_version = serviceVersion; }
    public void setSupportedEncoding(String supportedEncoding) { this.supported_encoding = supportedEncoding; }
    public void setSupportedCompression(String supportedCompression) { this.supported_compression = supportedCompression; }
    public void setProcessedResponseLimit(Integer processedResponseLimit) { this.processed_response_limit = processedResponseLimit; }
    public void setSupportedLanguages(List<String> supportedLanguages) { this.supported_languages = supportedLanguages; }
}