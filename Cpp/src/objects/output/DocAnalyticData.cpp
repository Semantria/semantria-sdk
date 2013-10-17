#include "DocAnalyticData.h"
#include "../../serializers/xml/DocumentXmlHandler.h"

#define uint unsigned int

DocAnalyticData::DocAnalyticData() {
    details = new vector<DocDetails*>();
    auto_categories = new vector<DocAutoCategories*>();
    entities = new vector<DocEntity*>();
    topics = new vector<DocTopic*>();
    themes = new vector<DocTheme*>();
    phrases = new vector<DocPhrase*>();
    opinions = new vector<DocOpinion*>();
    relations = new vector<DocRelations*>();
}

DocAnalyticData::~DocAnalyticData() {
    delete details;
    delete auto_categories;
    delete entities;
    delete topics;
    delete themes;
    delete phrases;
    delete opinions;
    delete relations;
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
    tag = root.get("tag", "").asString();
    this->SetStatusFromString(root.get("status", "").asString());
    source_text = root.get("source_text", "").asString();
    language = root.get("language", "").asString();
    language_score = root.get("language_score", "").asDouble();
    sentiment_score = root.get("sentiment_score", 0.0).asDouble();
    sentiment_polarity = root.get("sentiment_polarity", "").asString();
    summary = root.get("summary", "").asString();

    // Details
    if (NULL == this->details) {
        this->details = new vector<DocDetails*>();
    }

    Json::Value details = root["details"];
    for ( uint i = 0; i < details.size(); ++i ) {
        DocDetails* detail = new DocDetails();
        detail->Deserialize(details[i]);
        this->details->push_back(detail);
    }

    // Auto Categories
    if (NULL == this->auto_categories) {
        this->auto_categories = new vector<DocAutoCategories*>();
    }

    Json::Value auto_categories = root["auto_categories"];
    for ( uint i = 0; i < auto_categories.size(); ++i ) {
        DocAutoCategories* auto_category = new DocAutoCategories();
        auto_category->Deserialize(auto_categories[i]);
        this->auto_categories->push_back(auto_category);
    }

    // Entities
    if (NULL == this->entities) {
        this->entities = new vector<DocEntity*>();
    }

    Json::Value entities = root["entities"];
    for ( uint i = 0; i < entities.size(); ++i ) {
        DocEntity* entity = new DocEntity();
        entity->Deserialize(entities[i]);
        this->entities->push_back(entity);
    }

    // Topics
    if (NULL == this->topics) {
        this->topics = new vector<DocTopic*>();
    }

    Json::Value topics = root["topics"];
    for ( uint i = 0; i < topics.size(); ++i ) {
        DocTopic* topic = new DocTopic();
        topic->Deserialize(topics[i]);
        this->topics->push_back(topic);
    }

    // Themes
    if (NULL == this->themes) {
        this->themes = new vector<DocTheme*>();
    }

    Json::Value themes = root["themes"];
    for ( uint i = 0; i < themes.size(); ++i ) {
        DocTheme* theme = new DocTheme();
        theme->Deserialize(themes[i]);
        this->themes->push_back(theme);
    }

    // Phrases
    if (NULL == this->phrases) {
        this->phrases = new vector<DocPhrase*>();
    }

    Json::Value phrases = root["phrases"];
    for ( uint i = 0; i < phrases.size(); ++i ) {
        DocPhrase* phrase = new DocPhrase();
        phrase->Deserialize(phrases[i]);
        this->phrases->push_back(phrase);
    }

    // Opinions
    if (NULL == this->opinions) {
        this->opinions = new vector<DocOpinion*>();
    }

    Json::Value opinions = root["opinions"];
    for ( uint i = 0; i < opinions.size(); ++i ) {
        DocOpinion* opinion = new DocOpinion();
        opinion->Deserialize(opinions[i]);
        this->opinions->push_back(opinion);
    }

    // Relations
    if (NULL == this->relations) {
        this->relations = new vector<DocRelations*>();
    }

    Json::Value relations = root["relations"];
    for ( uint i = 0; i < relations.size(); ++i ) {
        DocRelations* relation = new DocRelations();
        relation->Deserialize(relations[i]);
        this->relations->push_back(relation);
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
