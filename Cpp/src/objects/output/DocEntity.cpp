#include "DocEntity.h"

DocEntity::DocEntity() {
    mentions = new vector<MentionWithLocations*>();
    themes = new vector<DocTheme*>();
}

DocEntity::~DocEntity() {
    delete themes;
    delete mentions;
}

void DocEntity::Serialize(Json::Value& root) {}

void DocEntity::Deserialize(Json::Value& root) {
    title = root.get("title", "").asString();
    label = root.get("label", "").asString();
    type = root.get("type", "").asString();
    entity_type = root.get("entity_type", "").asString();
    evidence = root.get("evidence", 0).asUInt();
    sentiment_score = root.get("sentiment_score", 0.0).asDouble();
    sentiment_polarity = root.get("sentiment_polarity", "").asString();

    is_about = root.get("is_about", false).asBool();
    confident = root.get("confident", false).asBool();

    if (NULL == this->mentions) {
        this->mentions = new vector<MentionWithLocations*>();
    }

    Json::Value mentionsj = root["mentions"];
    for ( int i = 0; i < mentionsj.size(); ++i ) {
        MentionWithLocations* attr = new MentionWithLocations();
        attr->Deserialize(mentionsj[i]);
        this->mentions->push_back(attr);
    }
    //
    if (NULL == this->themes) {
        this->themes = new vector<DocTheme*>();
    }

    Json::Value themes = root["themes"];
    for ( int i = 0; i < themes.size(); ++i ) {
        DocTheme* theme = new DocTheme();
        theme->Deserialize(themes[i]);
        this->themes->push_back(theme);
    }
}
