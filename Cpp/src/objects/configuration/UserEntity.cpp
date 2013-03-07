#include "UserEntity.h"

UserEntity::UserEntity() {}
UserEntity::~UserEntity() {}

UserEntity::UserEntity(string name, string type) {
    this->name = name;
    this->type = type;
}

void UserEntity::Serialize(Json::Value& root) {
    root["name"] = name;
    root["type"] = type;
}

void UserEntity::Deserialize(Json::Value& root) {
    name = root.get("name", "").asString();
    type = root.get("type", "").asString();
}
