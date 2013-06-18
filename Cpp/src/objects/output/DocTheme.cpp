#include "DocTheme.h"

DocTheme::DocTheme() {
    mentions = new vector<MentionWithLocations*>();
}

DocTheme::~DocTheme() {
    delete mentions;
}

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

    if (NULL == this->mentions) {
        this->mentions = new vector<MentionWithLocations*>();
    }

    Json::Value mentionsj = root["mentions"];
    for ( int i = 0; i < mentionsj.size(); ++i ) {
        MentionWithLocations* attr = new MentionWithLocations();
        attr->Deserialize(mentionsj[i]);
        this->mentions->push_back(attr);
    }
}
