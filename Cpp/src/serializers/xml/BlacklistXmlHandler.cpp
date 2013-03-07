#include "BlacklistXmlHandler.h"
#include <cstdlib>

BlacklistXmlHandler::BlacklistXmlHandler() {}
BlacklistXmlHandler::~BlacklistXmlHandler() {}

void BlacklistXmlHandler::startElement(void* user_data, const xmlChar* name, const xmlChar** attrs) {
    Stub_Blacklist* stub = static_cast<Stub_Blacklist*>(user_data);
    string elementName = (const char *)(name);
    stub->SetCurrent(elementName);
}

void BlacklistXmlHandler::endElement(void* user_data, const xmlChar* name) {
    Stub_Blacklist* stub = static_cast<Stub_Blacklist*>(user_data);
    stub->SetCurrent("");
}

void BlacklistXmlHandler::characters(void* user_data, const xmlChar* ch, int len) {
    Stub_Blacklist* stub = static_cast<Stub_Blacklist*>(user_data);
    if (stub->GetCurrent() == "item") {
        string value = (const char *)(xmlStrncatNew(BAD_CAST "", xmlStrsub(ch, 0, len), len));
        stub->GetBlacklistItems()->push_back(value);
    }
}
