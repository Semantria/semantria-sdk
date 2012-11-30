#include "Stub_Blacklist.h"

Stub_Blacklist::Stub_Blacklist() {
    items = new vector<string>();
}

Stub_Blacklist::~Stub_Blacklist() {
    // For removing stub's items use clear() function instead
}

void Stub_Blacklist::Serialize(Json::Value& root) {}

void Stub_Blacklist::Deserialize(Json::Value& root) {
    if (NULL == this->items) {
        this->items = new vector<string>();
    }

    for ( int i = 0; i < root.size(); ++i ) {
        this->items->push_back(root[i].asString());
    }
}

void Stub_Blacklist::Serialize(xmlNodePtr root) {}

void Stub_Blacklist::Deserialize(std::string source) {
    xmlSAXUserParseMemory(GetXmlHandler(), this, source.c_str(), int(source.length()));
    xmlCleanupParser();
}

xmlSAXHandler* Stub_Blacklist::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &BlacklistXmlHandler::startElement;
    result->endElement = &BlacklistXmlHandler::endElement;
    result->characters = &BlacklistXmlHandler::characters;

    return result;
}

string Stub_Blacklist::GetRootElement() {
    return "blacklist";
}
