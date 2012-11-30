#ifndef DOCTOPIC_H
#define DOCTOPIC_H

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class DocTopic: JsonSerializable {
public:
    DocTopic();
    virtual ~DocTopic();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetTitle() {return title;}
    string GetType() {return type;}
    int GetHitCount() {return hitcount;}
    double GetSentimentScore() {return sentiment_score;}
    double GetStrengthScore() {return strength_score;}

    void SetTitle(string title) {this->title = title;}
    void SetType(string type) {this->type = type;}
    void SetHitCount(int hitcount) {this->hitcount = hitcount;}
    void SetSentimentScore(double sentiment_score) {this->sentiment_score = sentiment_score;}
    void SetStrengthScore(double strength_score) {this->strength_score = strength_score;}

private:
    string title;
    string type;
    int hitcount;
    double sentiment_score;
    double strength_score;
};

#endif // DOCTOPIC_H
