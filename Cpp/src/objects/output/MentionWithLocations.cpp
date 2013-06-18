#include "MentionWithLocations.h"


MentionWithLocations::MentionWithLocations() {
    locations = new vector<Location*>();
}

MentionWithLocations::~MentionWithLocations() {}

void MentionWithLocations::Serialize(Json::Value& root) {}

void MentionWithLocations::Deserialize(Json::Value& root) {
    label = root.get("label", "").asString();

    is_negated = root.get("is_negated", true).asBool();
    negation_phrase = root.get("negating_phrase", "").asString();

    if (NULL == this->locations) {
        this->locations = new vector<Location*>();
    }

    Json::Value locations = root["locations"];
    for ( int i = 0; i < locations.size(); ++i ) {
        Location* location = new Location();
        location->Deserialize(locations[i]);
        this->locations->push_back(location);
    }
}
