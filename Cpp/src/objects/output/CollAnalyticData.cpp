#include "CollAnalyticData.h"
#include "../../serializers/xml/CollectionXmlHandler.h"

CollAnalyticData::CollAnalyticData() {
    facets = new vector<Facet*>();
    entities = new vector<CollEntity*>();
    topics = new vector<CollTopic*>();
    themes = new vector<CollTheme*>();
}

CollAnalyticData::~CollAnalyticData() {
    delete facets;
    delete entities;
    delete topics;
    delete themes;
}

string CollAnalyticData::GetStatusAsString() {
    return EnumsHelper::GetStatusAsString(status);
}

void CollAnalyticData::SetStatusFromString(string status) {
    this->status = EnumsHelper::GetStatusFromString(status);
}

void CollAnalyticData::Serialize(Json::Value& root) {}

void CollAnalyticData::Deserialize(Json::Value& root) {
    id = root.get("id", "").asString();
    config_id = root.get("config_id", "").asString();
    this->SetStatusFromString(root.get("status", "").asString());

    // Facets
    if (NULL == this->facets) {
        this->facets = new vector<Facet*>();
    }

    Json::Value facets = root["facets"];
    for ( int i = 0; i < facets.size(); ++i ) {
        Facet* facet = new Facet();
        facet->Deserialize(facets[i]);
        this->facets->push_back(facet);
    }

    // Entities
    if (NULL == this->entities) {
        this->entities = new vector<CollEntity*>();
    }

    Json::Value entities = root["entities"];
    for ( int i = 0; i < entities.size(); ++i ) {
        CollEntity* entity = new CollEntity();
        entity->Deserialize(entities[i]);
        this->entities->push_back(entity);
    }

    // Topics
    if (NULL == this->topics) {
        this->topics = new vector<CollTopic*>();
    }

    Json::Value topics = root["topics"];
    for ( int i = 0; i < topics.size(); ++i ) {
        CollTopic* topic = new CollTopic();
        topic->Deserialize(topics[i]);
        this->topics->push_back(topic);
    }

    // Themes
    if (NULL == this->themes) {
        this->themes = new vector<CollTheme*>();
    }

    Json::Value themes = root["themes"];
    for ( int i = 0; i < themes.size(); ++i ) {
        CollTheme* theme = new CollTheme();
        theme->Deserialize(themes[i]);
        this->themes->push_back(theme);
    }
}

void CollAnalyticData::Deserialize(std::string source) {
    xmlSAXUserParseMemory(GetXmlHandler(), this, source.c_str(), int(source.length()));
    xmlCleanupParser();
}

void CollAnalyticData::Serialize(xmlNodePtr root) {}

xmlSAXHandler* CollAnalyticData::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &CollectionXmlHandler::startElement;
    result->endElement = &CollectionXmlHandler::endElement;
    result->characters = &CollectionXmlHandler::characters;

    return result;
}

string CollAnalyticData::GetRootElement() {
    return "collection";
}
