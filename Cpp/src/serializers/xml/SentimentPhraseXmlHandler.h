#ifndef SENTIMENTPHRASEXMLHANDLER_H
#define SENTIMENTPHRASEXMLHANDLER_H

#include "../../objects/stub/Stub_SentimentPhrase.h"

class SentimentPhraseXmlHandler {
public:
    SentimentPhraseXmlHandler();
    virtual ~SentimentPhraseXmlHandler();

    static void startElement(void* user_data, const xmlChar* name, const xmlChar** attrs);
    static void endElement(void* user_data, const xmlChar* name);
    static void characters(void* user_data, const xmlChar* ch, int len);
};

#endif // SENTIMENTPHRASEXMLHANDLER_H
