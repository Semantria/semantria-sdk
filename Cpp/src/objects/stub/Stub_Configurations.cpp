#include "Stub_Configurations.h"

Stub_Configurations::Stub_Configurations() {
    configurations = new vector<Configuration*>();
    hierarchy = new vector<string>();
}

Stub_Configurations::~Stub_Configurations() {
    // For removing stub's items use clear() function instead
    configuration = NULL;
	if (NULL != hierarchy) {delete hierarchy;}
}

void Stub_Configurations::Serialize(Json::Value& root) {}

void Stub_Configurations::Deserialize(Json::Value& root) {
    if (NULL == this->configurations) {
        this->configurations = new vector<Configuration*>();
    }

    for ( int i = 0; i < root.size(); ++i ) {
        Configuration* configuration = new Configuration();
        configuration->Deserialize(root[i]);
        this->configurations->push_back(configuration);
    }
}

void Stub_Configurations::Deserialize(std::string source) {
    xmlSAXUserParseMemory(GetXmlHandler(), this, source.c_str(), int(source.length()));
    xmlCleanupParser();
}

void Stub_Configurations::Serialize(xmlNodePtr root) {}

xmlSAXHandler* Stub_Configurations::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &ConfigurationXmlHandler::startElement;
    result->endElement = &ConfigurationXmlHandler::endElement;
    result->characters = &ConfigurationXmlHandler::characters;

    return result;
}

string Stub_Configurations::GetRootElement() {
    return "configuration";
}
