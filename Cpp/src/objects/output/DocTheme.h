#ifndef DOCTHEME_H
#define DOCTHEME_H

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class DocTheme: public JsonSerializable {
public:
    DocTheme();
    virtual ~DocTheme();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetTitle() {return title;}
    double GetSentimentScore() {return sentiment_score;}
    int GetEvidence() {return evidence;}
    bool GetIsAbout() {return is_about;}
    double GetStrengthScore() {return strength_score;}

    void SetTitle(string title) {this->title = title;}
    void SetSentimentScore(double sentiment_score) {this->sentiment_score = sentiment_score;}
    void SetEvidence(int evidence) {this->evidence = evidence;}
    void SetIsAbout(bool is_about) {this->is_about = is_about;}
    void SetStrengthScore(double strength_score) {this->strength_score = strength_score;}

private:
    string title;
    double sentiment_score;
    int evidence;
    bool is_about;
    double strength_score;
};

#endif // DOCTHEME_H
