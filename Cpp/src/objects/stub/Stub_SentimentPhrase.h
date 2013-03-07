#ifndef STUB_SENTIMENTPHRASE_H
#define STUB_SENTIMENTPHRASE_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../serializers/xml/SentimentPhraseXmlHandler.h"
#include "../../objects/configuration/SentimentPhrase.h"

using namespace std;

class Stub_SentimentPhrase: public JsonSerializable, public XmlSerializable {
public:
    Stub_SentimentPhrase();
    virtual ~Stub_SentimentPhrase();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    void Serialize(xmlNodePtr node);
    void Deserialize(string source);
    xmlSAXHandler* GetXmlHandler();
    string GetRootElement();

    vector<SentimentPhrase*>* GetPhrases() {return phrases;}
    SentimentPhrase* GetPhrase() {return phrase;}
    void SetPhrase(SentimentPhrase* phrase) {this->phrase = phrase;}

    void clear() {
        if (NULL != phrases) {delete phrases;}
        if (NULL != phrase) {delete phrase;}
    }
private:
    vector<SentimentPhrase*>* phrases;
    SentimentPhrase* phrase;
};

#endif // STUB_SENTIMENTPHRASE_H
