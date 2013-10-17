#include "CollTheme.h"

CollTheme::CollTheme() {
    mentions = new vector<Mention*>();
}

CollTheme::~CollTheme() {
    delete mentions;
}

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

    // Mentions
    if (NULL == this->mentions) {
        this->mentions = new vector<Mention*>();
    }

    Json::Value mentionsj = root["mentions"];
    for ( unsigned int i = 0; i < mentionsj.size(); ++i ) {
        Mention* attr = new Mention();
        attr->Deserialize(mentionsj[i]);
        this->mentions->push_back(attr);
    }
}
