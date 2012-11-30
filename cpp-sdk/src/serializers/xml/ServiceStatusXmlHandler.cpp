#include "ServiceStatusXmlHandler.h"


ServiceStatusXmlHandler::ServiceStatusXmlHandler() {}
ServiceStatusXmlHandler::~ServiceStatusXmlHandler() {}

void ServiceStatusXmlHandler::startElement(void* user_data, const xmlChar* name, const xmlChar** attrs) {
    ServiceStatus* status = static_cast<ServiceStatus*>(user_data);
    status->SetCurrent((const char *)(name));
}

void ServiceStatusXmlHandler::endElement(void* user_data, const xmlChar* name) {
    ServiceStatus* status = static_cast<ServiceStatus*>(user_data);
    status->SetCurrent("");
}

void ServiceStatusXmlHandler::characters(void* user_data, const xmlChar* ch, int len) {
    ServiceStatus* status = static_cast<ServiceStatus*>(user_data);
    string value = (const char *)(xmlStrncatNew(BAD_CAST "", xmlStrsub(ch, 0, len), len));

    if (status->GetCurrent() == "service_status") {
        status->SetServiceStatus(value);
    } else if (status->GetCurrent() == "api_version") {
        status->SetApiVersion(value);
    } else if (status->GetCurrent() == "service_version") {
        status->SetServiceVersion(value);
    } else if (status->GetCurrent() == "supported_encoding") {
        status->SetSupportedEncoding(value);
    } else if (status->GetCurrent() == "supported_compression") {
        status->SetSupportedCompression(value);
    } else if (status->GetCurrent() == "language") {
        status->AddSupportedLanguage(value);
    }
}
