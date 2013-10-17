#ifndef DOCANALYTICDATA_H
#define DOCANALYTICDATA_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../common/Enums.h"
#include "../../common/EnumsHelper.h"
#include "../../objects/output/DocAutoCategories.h"
#include "../../objects/output/DocDetails.h"
#include "../../objects/output/DocEntity.h"
#include "../../objects/output/DocTopic.h"
#include "../../objects/output/DocTheme.h"
#include "../../objects/output/DocPhrase.h"
#include "../../objects/output/DocRelations.h"
#include "../../objects/output/DocOpinion.h"


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
    string GetTag() {return tag;}
    TaskStatus GetStatus() {return status;}
    string GetStatusAsString();
    string GetSourceText() {return source_text;};
    string GetLanguage() {return language;};
    double GetLanguageScore() {return language_score;};
    double GetSentimentScore() {return sentiment_score;}
    string GetSentimentPolarity() {return sentiment_polarity;};
    string GetSummary() {return summary;}

    vector<DocDetails*>* GetDetails() {return details;}
    vector<DocAutoCategories*>* GetAutoCategories() {return auto_categories;}
    vector<DocEntity*>* GetEntities() {return entities;}
    vector<DocTopic*>* GetTopics() {return topics;}
    vector<DocTheme*>* GetThemes() {return themes;}
    vector<DocPhrase*>* GetPhrases() {return phrases;}
    vector<DocOpinion*>* GetOpinions() {return opinions;}
    vector<DocRelations*>* GetRelations() {return relations;}


    void SetId(string id) {this->id = id;}
    void SetConfigId(string config_id) {this->config_id = config_id;}
    void SetTag(string tag) {this->tag = tag;}
    void SetSummary(string summary) {this->summary = summary;}
    void SetStatus(TaskStatus status) {this->status = status;}
    void SetStatusFromString(string status);
    void SetSentimentScore(double sentiment_score) {this->sentiment_score = sentiment_score;}
    void SetSentimentPolarity(string sentiment_polarity) {this->sentiment_polarity = sentiment_polarity;}
    void SetEntities(vector<DocEntity*>* entities) {this->entities = entities;}
    void SetTopics(vector<DocTopic*>* topics) {this->topics = topics;}
    void SetThemes(vector<DocTheme*>* themes) {this->themes = themes;}
    void SetPhrases(vector<DocPhrase*>* phrases) {this->phrases = phrases;}
    void AddEntity(DocEntity* entity) {this->entities->push_back(entity);}
    void AddTopic(DocTopic* topic) {this->topics->push_back(topic);}
    void AddTheme(DocTheme* theme) {this->themes->push_back(theme);}
    void AddPhrase(DocPhrase* phrase) {this->phrases->push_back(phrase);}
    void AddOpinion(DocOpinion* opinion) {this->opinions->push_back(opinion);}
    void AddRelation(DocRelations* relation) {this->relations->push_back(relation);}


private:
    string id;
    string config_id;
    string tag;
    TaskStatus status;
    string source_text;
    string language;
    double language_score;
    double sentiment_score;
    string sentiment_polarity;
    string summary;

    vector<DocAutoCategories*>* auto_categories;
    vector<DocDetails*>* details;
    vector<DocPhrase*>* phrases;
    vector<DocTheme*>* themes;
    vector<DocEntity*>* entities;
    vector<DocRelations*>* relations;
    vector<DocOpinion*>* opinions;
    vector<DocTopic*>* topics;
};

#endif // DOCANALYTICDATA_H
