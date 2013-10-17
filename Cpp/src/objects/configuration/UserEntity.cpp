#include "UserEntity.h"

UserEntity::UserEntity() {}
UserEntity::~UserEntity() {}

UserEntity::UserEntity(string name, string type) {
    this->name = name;
    this->type = type;
    this->label = "";
    this->normalized = "";
}

void UserEntity::Serialize(Json::Value& root) {
    root["name"] = name;
    root["type"] = type;
    root["label"] = label;
    root["normalized"] = normalized;
}

void UserEntity::Deserialize(Json::Value& root) {
    name = root.get("name", "").asString();
    type = root.get("type", "").asString();
    label = root.get("label", "").asString();
    normalized = root.get("normalized", "").asString();
}
