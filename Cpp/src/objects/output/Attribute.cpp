#include "Attribute.h"

Attribute::Attribute() {
    mentions = new vector<Mention*>();
}

Attribute::~Attribute() {
    delete mentions;
}

void Attribute::Serialize(Json::Value& root) {}

void Attribute::Deserialize(Json::Value& root) {
    label = root.get("label", "").asString();
    count = root.get("count", 0).asUInt();

    if (NULL == this->mentions) {
        this->mentions = new vector<Mention*>();
    }

    Json::Value mentionsj = root["mentions"];
    for ( int i = 0; i < mentionsj.size(); ++i ) {
        Mention* attr = new Mention();
        attr->Deserialize(mentionsj[i]);
        this->mentions->push_back(attr);
    }
}
