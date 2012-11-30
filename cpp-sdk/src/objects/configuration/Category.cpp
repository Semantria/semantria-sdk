#include "Category.h"

Category::Category() {
    this->samples = new vector<string>();
}

Category::Category(string name, double weight) {
    this->name = name;
    this->weight = weight;
    this->samples = new vector<string>();
}

Category::Category(string name, double weight, vector<string>* samples) {
    this->name = name;
    this->weight = weight;
    this->samples = samples;
}

Category::~Category() {
    delete samples;
}

void Category::Serialize(Json::Value& root) {
    root["name"] = name;
    root["weight"] = weight;
    //
    for(int i = 0; i != samples->size(); i++) {
        root["samples"].append(samples->at(i));
    }
}

void Category::Deserialize(Json::Value& root) {
    name = root.get("name", "").asString();
    weight = root.get("weight", "").asDouble();

    if (NULL == this->samples) {
        this->samples = new vector<string>();
    }

    const Json::Value samples = root["samples"];
    for ( int i = 0; i < samples.size(); ++i ) {
        this->samples->push_back(samples[i].asString());
    }
}
