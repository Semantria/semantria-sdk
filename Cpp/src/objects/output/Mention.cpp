#include "Mention.h"


Mention::Mention() {
    locations = new vector<Location*>();
}

Mention::~Mention() {
    delete locations;
}

void Mention::Serialize(Json::Value& root) {}

void Mention::Deserialize(Json::Value& root) {
    label = root.get("label", "").asString();

    is_negated = root.get("is_negated", true).asBool();
    negating_phrase = root.get("negating_phrase", "").asString();

    // Locations
    if (NULL == this->locations) {
        this->locations = new vector<Location*>();
    }

    Json::Value locations = root["locations"];
    for ( unsigned int i = 0; i < locations.size(); ++i ) {
        Location* location = new Location();
        location->Deserialize(locations[i]);
        this->locations->push_back(location);
    }
}
