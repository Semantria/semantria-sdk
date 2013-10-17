#include "CollEntity.h"

CollEntity::CollEntity() {
    mentions = new vector<Mention*>();
}

CollEntity::~CollEntity() {
    delete mentions;
}

void CollEntity::Serialize(Json::Value& root) {}

void CollEntity::Deserialize(Json::Value& root) {
    title = root.get("title", "").asString();
    type = root.get("type", "").asString();
    label = root.get("label", "").asString();
    entity_type = root.get("entity_type", "").asString();
    count = root.get("count", 0).asUInt();
    negative_count = root.get("negative_count", 0).asUInt();
    neutral_count = root.get("neutral_count", 0).asUInt();
    positive_count = root.get("positive_count", 0).asUInt();

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
