#ifndef DOCANALYTICDATA_H
#define DOCANALYTICDATA_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../common/Enums.h"
#include "../../common/EnumsHelper.h"
#include "../../objects/output/DocEntity.h"
#include "../../objects/output/DocTopic.h"
#include "../../objects/output/DocTheme.h"
#include "../../objects/output/DocPhrase.h"

using namespace std;

class DocAnalyticData: public JsonSerializable, public XmlSerializable {
public:
    DocAnalyticData();
    virtual ~DocAnalyticData();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    void Serialize(xmlNodePtr node);
    void Deserialize(string source);
    xmlSAXHandler* GetXmlHandler();
    string GetRootElement();

    string GetId() {return id;}
    string GetConfigId() {return config_id;}
    string GetSummary() {return summary;}
    TaskStatus GetStatus() {return status;}
    string GetStatusAsString();
    double GetSentimentScore() {return sentiment_score;}
    vector<DocEntity*>* GetEntities() {return entities;}
    vector<DocTopic*>* GetTopics() {return topics;}
    vector<DocTheme*>* GetThemes() {return themes;}
    vector<DocPhrase*>* GetPhrases() {return phrases;}

    void SetId(string id) {this->id = id;}
    void SetConfigId(string config_id) {this->config_id = config_id;}
    void SetSummary(string summary) {this->summary = summary;}
    void SetStatus(TaskStatus status) {this->status = status;}
    void SetStatusFromString(string status);
    void SetSentimentScore(double sentiment_score) {this->sentiment_score = sentiment_score;}
    void SetEntities(vector<DocEntity*>* entities) {this->entities = entities;}
    void SetTopics(vector<DocTopic*>* topics) {this->topics = topics;}
    void SetThemes(vector<DocTheme*>* themes) {this->themes = themes;}
    void SetPhrases(vector<DocPhrase*>* phrases) {this->phrases = phrases;}
    void AddEntity(DocEntity* entity) {this->entities->push_back(entity);}
    void AddTopic(DocTopic* topic) {this->topics->push_back(topic);}
    void AddTheme(DocTheme* theme) {this->themes->push_back(theme);}
    void AddPhrase(DocPhrase* phrase) {this->phrases->push_back(phrase);}


private:
    string id;
    string config_id;
    string summary;
    TaskStatus status;
    double sentiment_score;
    vector<DocEntity*>* entities;
    vector<DocTopic*>* topics;
    vector<DocTheme*>* themes;
    vector<DocPhrase*>* phrases;

};

#endif // DOCANALYTICDATA_H
