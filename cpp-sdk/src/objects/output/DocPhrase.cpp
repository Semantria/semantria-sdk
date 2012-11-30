#include "DocPhrase.h"

DocPhrase::DocPhrase() {}
DocPhrase::~DocPhrase() {}

void DocPhrase::Serialize(Json::Value& root) {
    root["title"] = title;
    root["sentiment_score"] = sentiment_score;
    root["negating_phrase"] = negating_phrase;
    root["is_negated"] = is_negated;
}

void DocPhrase::Deserialize(Json::Value& root) {
    title = root.get("title", "").asString();
    sentiment_score = root.get("sentiment_score", 0.0).asDouble();
    negating_phrase = root.get("negating_phrase", "").asString();
    is_negated = root.get("is_negated", false).asBool();
}
