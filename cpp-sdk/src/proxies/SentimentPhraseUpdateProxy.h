#ifndef SENTIMENTPHRASEUPDATEPROXY_H
#define SENTIMENTPHRASEUPDATEPROXY_H

#include "UpdateProxy.h"
#include "../objects/configuration/SentimentPhrase.h"

class SentimentPhraseUpdateProxy: public UpdateProxy<SentimentPhrase> {
public:
    SentimentPhraseUpdateProxy();
    virtual ~SentimentPhraseUpdateProxy();

    void Serialize(xmlNodePtr node);
    string GetRootElement();
    void Remove(SentimentPhrase* val);
};

#endif // SENTIMENTPHRASEUPDATEPROXY_H
