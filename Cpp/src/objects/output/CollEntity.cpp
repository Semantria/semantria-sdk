#include "CollEntity.h"

CollEntity::CollEntity() {}
CollEntity::~CollEntity() {}

void CollEntity::Serialize(Json::Value& root) {}

void CollEntity::Deserialize(Json::Value& root) {
    title = root.get("title", "").asString();
    type = root.get("type", "").asString();
    entity_type = root.get("entity_type", "").asString();
    count = root.get("count", 0).asUInt();
    negative_count = root.get("negative_count", 0).asUInt();
    neutral_count = root.get("neutral_count", 0).asUInt();
    positive_count = root.get("positive_count", 0).asUInt();
}
