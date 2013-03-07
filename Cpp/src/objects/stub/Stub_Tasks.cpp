#include "Stub_Tasks.h"

Stub_Tasks::Stub_Tasks() {
    documents = new vector<Document*>();
}

Stub_Tasks::~Stub_Tasks() {
    // For removing stub's items use clear() function instead
}

void Stub_Tasks::Serialize(Json::Value& root) {
    for(int i = 0; i != documents->size(); i++) {
        Json::Value document;
        documents->at(i)->Serialize(document);
        root.append(document);
    }
}

void Stub_Tasks::Deserialize(Json::Value& root){}

void Stub_Tasks::Serialize(xmlNodePtr root) {
    for(vector<string>::size_type i = 0; i != documents->size(); i++) {
        Document* document = documents->at(i);
        xmlNodePtr documentNode = xmlNewChild(root, NULL, BAD_CAST document->GetRootElement().c_str(), NULL);
        document->Serialize(documentNode);
    }
}

void Stub_Tasks::Deserialize(std::string source) {}

string Stub_Tasks::GetRootElement() {
    return "documents";
}
