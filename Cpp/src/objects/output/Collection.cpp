#include "Collection.h"

Collection::Collection() {
    documents = new vector<string>();
}

Collection::~Collection() {
    delete documents;
}

void Collection::Serialize(Json::Value& root) {
    root["id"] = id;
    for(int i = 0; i != documents->size(); i++) {
        root["documents"].append(documents->at(i));
    }
}

void Collection::Deserialize(Json::Value& root) {}

void Collection::Serialize(xmlNodePtr root) {
    addNewXmlChild(root, "id", id);
    if (documents->size() > 0) {
        xmlNodePtr documents = xmlNewChild(root, NULL, BAD_CAST "documents", NULL);
        for (int i = 0; i != this->documents->size(); i++) {
            addNewXmlChild(documents, "document", this->documents->at(i));
        }
    }
}

string Collection::GetRootElement() {
    return "collection";
}
