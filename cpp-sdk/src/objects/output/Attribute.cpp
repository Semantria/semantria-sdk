#include "Attribute.h"

Attribute::Attribute() {}
Attribute::~Attribute() {}

void Attribute::Serialize(Json::Value& root) {}

void Attribute::Deserialize(Json::Value& root) {
    label = root.get("label", "").asString();
    count = root.get("count", 0).asUInt();
}
