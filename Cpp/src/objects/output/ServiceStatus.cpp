#include "ServiceStatus.h"

ServiceStatus::ServiceStatus() {
    supported_languages = new vector<string>();
}
ServiceStatus::~ServiceStatus() {
    delete supported_languages;
}

void ServiceStatus::Serialize(Json::Value& root) {
	// We don't need to serialize ServiceStatus object, cause we
	// can only receive it from Analytic Service, and can not send
}

void ServiceStatus::Deserialize(Json::Value& root) {
	service_status = root.get("service_status", "").asString();
	api_version = root.get("api_version", "").asString();
	service_version = root.get("service_version", "").asString();
	supported_compression = root.get("supported_compression", "").asString();
	supported_encoding = root.get("supported_encoding", "").asString();

	const Json::Value languages = root["supported_languages"];
	for (int i = 0, languagesAmount = languages.size(); i < languagesAmount; ++i )  {
	   supported_languages->push_back(languages[i].asString());
	}
}

void ServiceStatus::Deserialize(std::string source) {
    xmlSAXHandler* handler = GetXmlHandler();
    xmlSAXUserParseMemory(handler, this, source.c_str(), int(source.length()));
    xmlCleanupParser();
}

void ServiceStatus::Serialize(xmlNodePtr root) {}

xmlSAXHandler* ServiceStatus::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &ServiceStatusXmlHandler::startElement;
    result->endElement = &ServiceStatusXmlHandler::endElement;
    result->characters = &ServiceStatusXmlHandler::characters;

    return result;
}

string ServiceStatus::GetRootElement() {
    return "status";
}
