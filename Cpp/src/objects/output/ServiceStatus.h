#ifndef SERVICESTATUS_H_
#define SERVICESTATUS_H_

#include "../../serializers/json/JsonSerializable.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../serializers/xml/ServiceStatusXmlHandler.h"

using namespace std;

class ServiceStatus: public JsonSerializable, public XmlSerializable {
public:
    ServiceStatus();
	virtual ~ServiceStatus();

	void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);
    void Deserialize(string source);
    void Serialize(xmlNodePtr node);
    xmlSAXHandler* GetXmlHandler();
    string GetRootElement();

	string GetServiceStatus() {return service_status;}
    string GetApiVersion() {return api_version;}
    string GetServiceVersion() {return service_version;}
	string GetSupportedCompression() {return supported_compression;}
    string GetSupportedEncoding() {return supported_encoding;}
    vector<string>* GetSupportedLanguages() {return supported_languages;}

    void SetServiceStatus(string service_status) {this->service_status = service_status;}
    void SetApiVersion(string api_version) {this->api_version = api_version;}
    void SetServiceVersion(string service_version) {this->service_version = service_version;}
    void SetSupportedEncoding(string supported_encoding) {this->supported_encoding = supported_encoding;}
    void SetSupportedCompression(string supported_compression) {this->supported_compression = supported_compression;}
    void SetSupportedLanguages(vector<string>* supported_languages) {this->supported_languages = supported_languages;}
    void AddSupportedLanguage(string language) {supported_languages->push_back(language);}

private:
    string service_status;
    string api_version;
    string service_version;
    string supported_encoding;
    string supported_compression;
    vector<string>* supported_languages;
};

#endif /* SERVICESTATUS_H_ */
