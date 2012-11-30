#ifndef SENTIMENTPHRASE_H
#define SENTIMENTPHRASE_H

#include "../../serializers/json/JsonSerializable.h"
//#include "../../serializers/xml/XmlSerializable.h"
//#include "../../serializers/xml/SentimentPhraseXmlHandler.h"

using namespace std;

class SentimentPhrase: public JsonSerializable {
public:
    SentimentPhrase();
    SentimentPhrase(string title, double weight);
    virtual ~SentimentPhrase();

    virtual void Serialize(Json::Value& root);
    virtual void Deserialize(Json::Value& root);

    string GetTitle();
    double GetWeight();
    void SetTitle(string title);
    void SetWeight(double weight);

private:
    string title;
    double weight;
};

#endif // SENTIMENTPHRASE_H
