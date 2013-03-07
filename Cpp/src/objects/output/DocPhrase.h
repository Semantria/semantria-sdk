#ifndef DOCPHRASE_H
#define DOCPHRASE_H

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class DocPhrase: JsonSerializable {
public:
    DocPhrase();
    virtual ~DocPhrase();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetTitle() {return title;}
    double GetSentimentScore() {return sentiment_score;}
    string GetNegatingPhrase() {return negating_phrase;}
    bool GetIsNegated() {return is_negated;}

    void SetTitle(string title) {this->title = title;}
    void SetSentimentScore(double sentiment_score) {this->sentiment_score = sentiment_score;}
    void SetNegatingPhrase(string negating_phrase) {this->negating_phrase = negating_phrase;}
    void SetIsNegated(bool is_negated) {this->is_negated = is_negated;}

private:
    string title;
    double sentiment_score;
    string negating_phrase;
    bool is_negated;
};

#endif // DOCPHRASE_H
