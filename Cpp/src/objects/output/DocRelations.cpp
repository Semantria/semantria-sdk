#include "DocRelations.h"

DocRelations::DocRelations() {
    this->entitles = new vector<DocRelEntities*>();
}

DocRelations::~DocRelations() {
    delete this->entitles;
}

void DocRelations::Serialize(Json::Value& root) {
    //TODO:
}

void DocRelations::Deserialize(Json::Value& root) {
    type = root.get("type", "").asString();
    relation_type = root.get("relation_type", "").asString();
    confidence_score = root.get("confidence_score", "").asDouble();
    extra = root.get("extra", "").asString();
    // Words
    if (NULL == this->entitles) {
        this->entitles = new vector<DocRelEntities*>();
    }

    Json::Value entitles = root["entities"];
    for ( int i = 0; i < entitles.size(); ++i ) {
        DocRelEntities* w = new DocRelEntities();
        w->Deserialize(entitles[i]);
        this->entitles->push_back(w);
    }
}
