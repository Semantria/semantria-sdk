#include "DocTheme.h"

DocTheme::DocTheme() {}
DocTheme::~DocTheme() {}

void DocTheme::Serialize(Json::Value& root) {
    root["title"] = title;
    root["sentiment_score"] = sentiment_score;
    root["evidence"] = evidence;
    root["is_about"] = is_about;
    root["strength_score"] = strength_score;
}

void DocTheme::Deserialize(Json::Value& root) {
    title = root.get("title", "").asString();
    sentiment_score = root.get("sentiment_score", 0.0).asDouble();
    evidence = root.get("evidence", 0).asUInt();
    is_about = root.get("is_about", false).asBool();
    strength_score = root.get("strength_score", 0.0).asDouble();
}
