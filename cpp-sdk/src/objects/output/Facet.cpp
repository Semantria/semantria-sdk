#include "Facet.h"

Facet::Facet() {
    attributes = new vector<Attribute*>();
}

Facet::~Facet() {
    delete attributes;
}

void Facet::Serialize(Json::Value& root) {}

void Facet::Deserialize(Json::Value& root) {
    label = root.get("label", "").asString();
    count = root.get("count", 0).asUInt();
    negative_count = root.get("negative_count", 0).asUInt();
    neutral_count = root.get("neutral_count", 0).asUInt();
    positive_count = root.get("positive_count", 0).asUInt();

    //

    if (NULL == this->attributes) {
        this->attributes = new vector<Attribute*>();
    }

    Json::Value attributes = root["attributes"];
    for ( int i = 0; i < attributes.size(); ++i ) {
        Attribute* attr = new Attribute();
        attr->Deserialize(attributes[i]);
        this->attributes->push_back(attr);
    }
}

