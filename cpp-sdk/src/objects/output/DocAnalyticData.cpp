#include "DocAnalyticData.h"
#include "../../serializers/xml/DocumentXmlHandler.h"

DocAnalyticData::DocAnalyticData() {
    entities = new vector<DocEntity*>();
    topics = new vector<DocTopic*>();
    themes = new vector<DocTheme*>();
    phrases = new vector<DocPhrase*>();
}

DocAnalyticData::~DocAnalyticData() {
    delete entities;
    delete topics;
    delete themes;
    delete phrases;
}

string DocAnalyticData::GetStatusAsString() {
    return EnumsHelper::GetStatusAsString(status);
}

void DocAnalyticData::SetStatusFromString(string status) {
    this->status = EnumsHelper::GetStatusFromString(status);
}

void DocAnalyticData::Serialize(Json::Value& root) {}

void DocAnalyticData::Deserialize(Json::Value& root) {
    id = root.get("id", "").asString();
    config_id = root.get("config_id", "").asString();
    summary = root.get("summary", "").asString();
    sentiment_score = root.get("sentiment_score", 0.0).asDouble();
    this->SetStatusFromString(root.get("status", "").asString());

    // Entities
    if (NULL == this->entities) {
        this->entities = new vector<DocEntity*>();
    }

    Json::Value entities = root["entities"];
    for ( int i = 0; i < entities.size(); ++i ) {
        DocEntity* entity = new DocEntity();
        entity->Deserialize(entities[i]);
        this->entities->push_back(entity);
    }

    // Topics
    if (NULL == this->topics) {
        this->topics = new vector<DocTopic*>();
    }

    Json::Value topics = root["topics"];
    for ( int i = 0; i < topics.size(); ++i ) {
        DocTopic* topic = new DocTopic();
        topic->Deserialize(topics[i]);
        this->topics->push_back(topic);
    }

    // Themes
    if (NULL == this->themes) {
        this->themes = new vector<DocTheme*>();
    }

    Json::Value themes = root["themes"];
    for ( int i = 0; i < themes.size(); ++i ) {
        DocTheme* theme = new DocTheme();
        theme->Deserialize(themes[i]);
        this->themes->push_back(theme);
    }

    // Phrases
    if (NULL == this->phrases) {
        this->phrases = new vector<DocPhrase*>();
    }

    Json::Value phrases = root["phrases"];
    for ( int i = 0; i < phrases.size(); ++i ) {
        DocPhrase* phrase = new DocPhrase();
        phrase->Deserialize(phrases[i]);
        this->phrases->push_back(phrase);
    }
}

void DocAnalyticData::Deserialize(std::string source) {
    xmlSAXUserParseMemory(GetXmlHandler(), this, source.c_str(), int(source.length()));
    xmlCleanupParser();
}

void DocAnalyticData::Serialize(xmlNodePtr root) {}

xmlSAXHandler* DocAnalyticData::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &DocumentXmlHandler::startElement;
    result->endElement = &DocumentXmlHandler::endElement;
    result->characters = &DocumentXmlHandler::characters;

    return result;
}

string DocAnalyticData::GetRootElement() {
    return "document";
}
