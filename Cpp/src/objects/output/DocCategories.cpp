#include "DocCategories.h"

DocCategories::DocCategories() {}
DocCategories::~DocCategories() {}

void DocCategories::Serialize(Json::Value& root) {
    root["title"] = title;
    root["type"] = type;
    root["strength_score"] = strength_score;
}

void DocCategories::Deserialize(Json::Value& root) {
    title = root.get("title", "").asString();
    type = root.get("type", "").asString();
    strength_score = root.get("strength_score", 0.0).asDouble();
}
