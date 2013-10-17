#include "Facet.h"

Facet::Facet() {
    attributes = new vector<Attribute*>();
    mentions = new vector<Mention*>();
}

Facet::~Facet() {
    delete attributes;
    delete mentions;
}

void Facet::Serialize(Json::Value& root) {}

void Facet::Deserialize(Json::Value& root) {
    label = root.get("label", "").asString();
    count = root.get("count", 0).asUInt();
    negative_count = root.get("negative_count", 0).asUInt();
    neutral_count = root.get("neutral_count", 0).asUInt();
    positive_count = root.get("positive_count", 0).asUInt();

    // Attributes
    if (NULL == this->attributes) {
        this->attributes = new vector<Attribute*>();
    }

    Json::Value attributesj = root["attributes"];
    for ( unsigned int i = 0; i < attributesj.size(); ++i ) {
        Attribute* attr = new Attribute();
        attr->Deserialize(attributesj[i]);
        this->attributes->push_back(attr);
    }

    // Mentions
    if (NULL == this->mentions) {
        this->mentions = new vector<Mention*>();
    }

    Json::Value mentionsj = root["mentions"];
    for ( unsigned int i = 0; i < mentionsj.size(); ++i ) {
        Mention* attr = new Mention();
        attr->Deserialize(mentionsj[i]);
        this->mentions->push_back(attr);
    }
}

