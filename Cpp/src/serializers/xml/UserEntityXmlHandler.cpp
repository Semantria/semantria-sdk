#include "UserEntityXmlHandler.h"

UserEntityXmlHandler::UserEntityXmlHandler() {}
UserEntityXmlHandler::~UserEntityXmlHandler() {}

void UserEntityXmlHandler::startElement(void* user_data, const xmlChar* name, const xmlChar** attrs) {
    Stub_Entities* stub = static_cast<Stub_Entities*>(user_data);
    string elementName = (const char *)(name);

    if ("entity" == elementName) {
        stub->SetEntity(new UserEntity());
    }
    stub->SetCurrent(elementName);
}

void UserEntityXmlHandler::endElement(void* user_data, const xmlChar* name) {
    Stub_Entities* stub = static_cast<Stub_Entities*>(user_data);
    string elementName = (const char *)(name);

    if ("entity" == elementName) {
        stub->GetEntities()->push_back(stub->GetEntity());
        stub->SetEntity(new UserEntity());
    }

    stub->SetCurrent("");
}

void UserEntityXmlHandler::characters(void* user_data, const xmlChar* ch, int len) {
    Stub_Entities* stub = static_cast<Stub_Entities*>(user_data);
    string value = (const char *)(xmlStrncatNew(BAD_CAST "", xmlStrsub(ch, 0, len), len));
    UserEntity* entity = stub->GetEntity();

    if (stub->GetCurrent() == "name") {
        entity->SetName(entity->GetName() + value);
    } else if (stub->GetCurrent() == "type") {
        entity->SetType(entity->GetType() + value);
    }
}
