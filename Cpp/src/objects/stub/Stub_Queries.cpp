#include "Stub_Queries.h"

Stub_Queries::Stub_Queries() {
    queries = new vector<Query*>();
    idx = 0;
}

Stub_Queries::~Stub_Queries() {
    // For removing stub's items use clear() function instead
    query = NULL;
}

void Stub_Queries::Serialize(Json::Value& root) {}

void Stub_Queries::Deserialize(Json::Value& root) {
    if (NULL == this->queries) {
        this->queries = new vector<Query*>();
    }

    for ( int i = 0; i < root.size(); ++i ) {
        Query* query = new Query();
        query->Deserialize(root[i]);
        this->queries->push_back(query);
    }
}

void Stub_Queries::Deserialize(std::string source) {
    xmlSAXUserParseMemory(GetXmlHandler(), this, source.c_str(), int(source.length()));
    xmlCleanupParser();
}

void Stub_Queries::Serialize(xmlNodePtr root) {}

xmlSAXHandler* Stub_Queries::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &QueryXmlHandler::startElement;
    result->endElement = &QueryXmlHandler::endElement;
    result->characters = &QueryXmlHandler::characters;

    return result;
}

string Stub_Queries::GetRootElement() {
    return "query";
}
