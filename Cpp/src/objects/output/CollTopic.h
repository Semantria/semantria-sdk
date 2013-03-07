#ifndef COLLTOPIC_H
#define COLLTOPIC_H

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class CollTopic: JsonSerializable {
public:
    CollTopic();
    virtual ~CollTopic();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetTitle() {return title;}
    string GetType() {return type;}
    int GetHitCount() {return hitcount;}
    double GetSentimentScore() {return sentiment_score;}

    void SetTitle(string title) {this->title = title;}
    void SetType(string type) {this->type = type;}
    void SetHitCount(int hitcount) {this->hitcount = hitcount;}
    void SetSentimentScore(double sentiment_score) {this->sentiment_score = sentiment_score;}

private:
    string title;
    string type;
    int hitcount;
    double sentiment_score;
};


#endif // COLLTOPIC_H
