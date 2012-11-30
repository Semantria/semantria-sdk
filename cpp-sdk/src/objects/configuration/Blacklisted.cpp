#include "Blacklisted.h"

Blacklisted::Blacklisted() {}
Blacklisted::~Blacklisted() {}

Blacklisted::Blacklisted(string item) {
    this->item = item;
}

void Blacklisted::Serialize(Json::Value& root) {
    root[""] = item;
}

void Blacklisted::Deserialize(Json::Value& root) {
    // see Stub_Blacklist
}
