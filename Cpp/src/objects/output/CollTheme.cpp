#include "CollTheme.h"

CollTheme::CollTheme() {}
CollTheme::~CollTheme() {}

void CollTheme::Serialize(Json::Value& root) {
    root["title"] = title;
    root["sentiment_score"] = sentiment_score;
    root["phrases_count"] = phrases_count;
    root["themes_count"] = themes_count;
}

void CollTheme::Deserialize(Json::Value& root) {
    title = root.get("title", "").asString();
    sentiment_score = root.get("sentiment_score", 0.0).asDouble();
    phrases_count = root.get("phrases_count", 0).asUInt();
    themes_count = root.get("themes_count", 0).asUInt();
}
