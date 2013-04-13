#include "Mention.h"


Mention::Mention() {
    indexes = new vector<int>();

}
Mention::~Mention() {}

void Mention::Serialize(Json::Value& root) {}

void Mention::Deserialize(Json::Value& root) {
    label = root.get("label", "").asString();
    
    is_negated = root.get("is_negated", true).asBool();
    negation_phrase = root.get("negation_phrase", "").asString();
    
    if (NULL == this->indexes) {
        this->indexes = new vector<int>();
    }
    
    Json::Value indexesj = root["indexes"];
    for ( int i = 0; i < indexesj.size(); ++i ) {
        int index = indexesj[i].asInt();
        this->indexes->push_back(index);
    }
}
