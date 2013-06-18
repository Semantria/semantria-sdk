#ifndef DOCENTITY_H
#define DOCENTITY_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/output/DocTheme.h"
#include "../../objects/output/MentionWithLocations.h"

using namespace std;

class DocEntity: public JsonSerializable {
public:
    DocEntity();
    virtual ~DocEntity();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetTitle() {return title;}
    string GetLabel() {return label;}
    string GetType() {return type;}
    string GetEntityType() {return entity_type;}
    int GetEvidence() {return evidence;}
    double GetSentimentScore() {return sentiment_score;}
    string GetSentimentPolarity() {return sentiment_polarity;};
    bool GetIsAbout() {return is_about;}
    bool GetConfident() {return confident;}

    vector<MentionWithLocations*>* GetMentions() {return mentions;}
    vector<DocTheme*>* GetThemes() {return themes;}

    void SetTitle(string title) {this->title = title;}
    void SetLabel(string label) {this->label = label;}
    void SetType(string type) {this->type = type;}
    void SetEntityType(string entity_type) {this->entity_type = entity_type;}
    void SetEvidence(int evidence) {this->evidence = evidence;}
    void SetSentimentScore(double sentiment_score) {this->sentiment_score = sentiment_score;}
    void SetSentimentPolarity(string sentiment_polarity) {this->sentiment_polarity = sentiment_polarity;}
    void SetIsAbout(bool is_about) {this->is_about = is_about;}
    void SetConfident(bool confident) {this->confident = confident;}
    void SetThemes(vector<DocTheme*>* themes) {this->themes = themes;}
    void AddTheme(DocTheme* theme) {this->themes->push_back(theme);}
    void SetMentions(vector<MentionWithLocations*>* mentions) {this->mentions = mentions;}

private:
    string title;
    string label;
    string type;
    string entity_type;
    int evidence;
    double sentiment_score;
    string sentiment_polarity;
    bool is_about;
    bool confident;

    vector<MentionWithLocations*>* mentions;
    vector<DocTheme*>* themes;
};

#endif // DOCENTITY_H
