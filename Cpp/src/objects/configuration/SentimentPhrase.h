#ifndef SENTIMENTPHRASE_H
#define SENTIMENTPHRASE_H

#include "../../serializers/json/JsonSerializable.h"
//#include "../../serializers/xml/XmlSerializable.h"
//#include "../../serializers/xml/SentimentPhraseXmlHandler.h"

using namespace std;

class SentimentPhrase: public JsonSerializable {
public:
    SentimentPhrase();
    SentimentPhrase(string name, double weight);
    virtual ~SentimentPhrase();

    virtual void Serialize(Json::Value& root);
    virtual void Deserialize(Json::Value& root);

    string GetName();
    double GetWeight();
    void SetName(string name);
    void SetWeight(double weight);

private:
    string name;
    double weight;
};

#endif // SENTIMENTPHRASE_H
