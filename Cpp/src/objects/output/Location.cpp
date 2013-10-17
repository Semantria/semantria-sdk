#include "Location.h"

Location::Location() {}

Location::~Location() {}

void Location::Serialize(Json::Value& root) {}

void Location::Deserialize(Json::Value& root) {
    index = root.get("index", 0).asInt();
    offset = root.get("offset", 0).asInt();
    length = root.get("length", 0).asInt();
}
