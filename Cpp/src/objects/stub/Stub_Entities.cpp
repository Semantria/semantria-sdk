#include "Stub_Entities.h"

Stub_Entities::Stub_Entities() {
    entities = new vector<UserEntity*>();
}

Stub_Entities::~Stub_Entities() {
    // For removing stub's items use clear() function instead
    entity = NULL;
}

void Stub_Entities::Serialize(Json::Value& root) {}

void Stub_Entities::Deserialize(Json::Value& root) {
    if (NULL == this->entities) {
        this->entities = new vector<UserEntity*>();
    }

    for ( int i = 0; i < root.size(); ++i ) {
        UserEntity* entity = new UserEntity();
        entity->Deserialize(root[i]);
        this->entities->push_back(entity);
    }
}


void Stub_Entities::Deserialize(std::string source) {
    xmlSAXUserParseMemory(GetXmlHandler(), this, source.c_str(), int(source.length()));
    xmlCleanupParser();
}

void Stub_Entities::Serialize(xmlNodePtr root) {}

xmlSAXHandler* Stub_Entities::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &UserEntityXmlHandler::startElement;
    result->endElement = &UserEntityXmlHandler::endElement;
    result->characters = &UserEntityXmlHandler::characters;

    return result;
}

string Stub_Entities::GetRootElement() {
    return "entity";
}
