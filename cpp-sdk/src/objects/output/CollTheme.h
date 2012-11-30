#ifndef COLLTHEME_H
#define COLLTHEME_H

#include "../../serializers/json/JsonSerializable.h"

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

    void SetTitle(string title) {this->title = title;}
    void SetSentimentScore(double sentiment_score) {this->sentiment_score = sentiment_score;}
    void SetPhrasesCount(int phrases_count) {this->phrases_count = phrases_count;}
    void SetThemesCount(int themes_count) {this->themes_count = themes_count;}

private:
    string title;
    double sentiment_score;
    int phrases_count;
    int themes_count;
};

#endif // COLLTHEME_H
