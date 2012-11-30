#include "SentimentPhrase.h"

SentimentPhrase::SentimentPhrase() {}

SentimentPhrase::SentimentPhrase(string title, double weight) {
    this->title = title;
    this->weight = weight;
}

SentimentPhrase::~SentimentPhrase() {}

void SentimentPhrase::Serialize(Json::Value& root) {
	root["title"] = title;
	root["weight"] = weight;
}

void SentimentPhrase::Deserialize(Json::Value& root) {
    title = root.get("title", "").asString();
    weight = root.get("weight", 0).asDouble();
}

/*
void SentimentPhrase::Serialize(xmlNodePtr root) {
    addNewXmlChild(root, "title", title);
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

string SentimentPhrase::GetTitle() {
    return title;
}

double SentimentPhrase::GetWeight() {
    return weight;
}

void SentimentPhrase::SetTitle(string title) {
    this->title = title;
}

void SentimentPhrase::SetWeight(double weight) {
    this->weight = weight;
}
