#include "DocTopic.h"

DocTopic::DocTopic() {}
DocTopic::~DocTopic() {}

void DocTopic::Serialize(Json::Value& root) {
    root["title"] = title;
    root["type"] = type;
    root["hitcount"] = hitcount;
    root["sentiment_score"] = sentiment_score;
    root["strength_score"] = strength_score;
}

void DocTopic::Deserialize(Json::Value& root) {
    title = root.get("title", "").asString();
    type = root.get("type", "").asString();
    hitcount = root.get("hitcount", 0).asUInt();
    sentiment_score = root.get("sentiment_score", 0.0).asDouble();
    strength_score = root.get("strength_score", 0.0).asDouble();
}
