#include "Document.h"

Document::Document() {}

Document::Document(string id, string text) {
    this->id = id;
    this->text = text;
}

Document::~Document() {}

void Document::Serialize(Json::Value& root) {
    root["id"] = id;
    root["text"] = text;
}

void Document::Deserialize(Json::Value& root) {
    id = root.get("id", "").asString();
    text = root.get("text", "").asString();
}

void Document::Serialize(xmlNodePtr root) {
    addNewXmlChild(root, "id", id);
    addNewXmlChild(root, "text", text);
}

string Document::GetRootElement() {
    return "document";
}
