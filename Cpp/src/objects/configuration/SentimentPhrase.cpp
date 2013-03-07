#include "SentimentPhrase.h"

SentimentPhrase::SentimentPhrase() {}

SentimentPhrase::SentimentPhrase(string name, double weight) {
    this->name = name;
    this->weight = weight;
}

SentimentPhrase::~SentimentPhrase() {}

void SentimentPhrase::Serialize(Json::Value& root) {
	root["name"] = name;
	root["weight"] = weight;
}

void SentimentPhrase::Deserialize(Json::Value& root) {
    name = root.get("name", "").asString();
    weight = root.get("weight", 0).asDouble();
}

/*
void SentimentPhrase::Serialize(xmlNodePtr root) {
    addNewXmlChild(root, "name", name);
    addNewXmlChild(root, "weight", DoubleToString(weight));
}

xmlSAXHandler* SentimentPhrase::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &SentimentPhraseXmlHandler::startElement;
    result->endElement = &SentimentPhraseXmlHandler::endElement;
    result->characters = &SentimentPhraseXmlHandler::characters;

    return result;
}

string SentimentPhrase::GetRootElement() {
    return "sentiment";
}
*/

string SentimentPhrase::GetName() {
    return name;
}

double SentimentPhrase::GetWeight() {
    return weight;
}

void SentimentPhrase::SetName(string name) {
    this->name = name;
}

void SentimentPhrase::SetWeight(double weight) {
    this->weight = weight;
}
