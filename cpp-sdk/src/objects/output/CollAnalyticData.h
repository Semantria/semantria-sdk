#ifndef COLLANALYTICDATA_H
#define COLLANALYTICDATA_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../common/Enums.h"
#include "../../common/EnumsHelper.h"
#include "../../objects/output/CollEntity.h"
#include "../../objects/output/CollTopic.h"
#include "../../objects/output/CollTheme.h"
#include "../../objects/output/Facet.h"

using namespace std;

class CollAnalyticData: public JsonSerializable, public XmlSerializable {
public:
    CollAnalyticData();
    virtual ~CollAnalyticData();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    void Serialize(xmlNodePtr node);
    void Deserialize(string source);
    xmlSAXHandler* GetXmlHandler();
    string GetRootElement();

    string GetId() {return id;}
    string GetConfigId() {return config_id;}
    TaskStatus GetStatus() {return status;}
    string GetStatusAsString();
    vector<CollEntity*>* GetEntities() {return entities;}
    vector<CollTopic*>* GetTopics() {return topics;}
    vector<CollTheme*>* GetThemes() {return themes;}
    vector<Facet*>* GetFacets() {return facets;}

    void SetId(string id) {this->id = id;}
    void SetConfigId(string config_id) {this->config_id = config_id;}
    void SetStatus(TaskStatus status) {this->status = status;}
    void SetStatusFromString(string status);
    void SetEntities(vector<CollEntity*>* entities) {this->entities = entities;}
    void SetTopics(vector<CollTopic*>* topics) {this->topics = topics;}
    void SetThemes(vector<CollTheme*>* themes) {this->themes = themes;}
    void SetFacets(vector<Facet*>* facets) {this->facets = facets;}
    void AddEntity(CollEntity* entity) {this->entities->push_back(entity);}
    void AddTopic(CollTopic* topic) {this->topics->push_back(topic);}
    void AddTheme(CollTheme* theme) {this->themes->push_back(theme);}
    void AddFacet(Facet* facet) {this->facets->push_back(facet);}

private:
    string id;
    string config_id;
    TaskStatus status;
    vector<Facet*>* facets;
    vector<CollEntity*>* entities;
    vector<CollTopic*>* topics;
    vector<CollTheme*>* themes;
};

#endif // COLLANALYTICDATA_H
