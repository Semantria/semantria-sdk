#include "DocOpinion.h"

DocOpinion::DocOpinion() {}
DocOpinion::~DocOpinion() {}

void DocOpinion::Serialize(Json::Value& root) {
    root["quotation"] = quotation;
    root["type"] = type;
    root["speaker"] = speaker;
    root["topic"] = topic;
    root["sentiment_score"] = sentiment_score;
    root["sentiment_polarity"] = sentiment_polarity;
}

void DocOpinion::Deserialize(Json::Value& root) {
    quotation = root.get("quotation", "").asString();
    type = root.get("type", "").asString();
    speaker = root.get("speaker", "").asString();
    topic = root.get("topic", "").asString();
    sentiment_score = root.get("sentiment_score", "").asDouble();
    sentiment_polarity = root.get("sentiment_polarity", "").asString();
}

