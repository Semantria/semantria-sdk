#ifndef COLLTHEME_H
#define COLLTHEME_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/output/Attribute.h"

using namespace std;

class CollTheme: public JsonSerializable {
public:
    CollTheme();
    virtual ~CollTheme();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetTitle() {return title;}
    double GetSentimentScore() {return sentiment_score;}
    int GetPhrasesCount() {return phrases_count;}
    int GetThemesCount() {return themes_count;}
    vector<Mention*>* GetMentions() {return mentions;}

    void SetTitle(string title) {this->title = title;}
    void SetSentimentScore(double sentiment_score) {this->sentiment_score = sentiment_score;}
    void SetPhrasesCount(int phrases_count) {this->phrases_count = phrases_count;}
    void SetThemesCount(int themes_count) {this->themes_count = themes_count;}
    void SetMentions(vector<Mention*>* mentions) {this->mentions = mentions;}
    void AddMentions(Mention* mention) {this->mentions->push_back(mention);}

private:
    string title;
    double sentiment_score;
    int phrases_count;
    int themes_count;

    vector<Mention*>* mentions;
};

#endif // COLLTHEME_H
