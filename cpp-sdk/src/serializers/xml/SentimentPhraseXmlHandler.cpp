#include "SentimentPhraseXmlHandler.h"

SentimentPhraseXmlHandler::SentimentPhraseXmlHandler() {}
SentimentPhraseXmlHandler::~SentimentPhraseXmlHandler() {}

void SentimentPhraseXmlHandler::startElement(void* user_data, const xmlChar* name, const xmlChar** attrs) {
    Stub_SentimentPhrase* stub = static_cast<Stub_SentimentPhrase*>(user_data);
    string elementName = (const char *)(name);
    if ("phrase" == elementName) {
        stub->SetPhrase(new SentimentPhrase());
    }
    stub->SetCurrent(elementName);
}

void SentimentPhraseXmlHandler::endElement(void* user_data, const xmlChar* name) {
    Stub_SentimentPhrase* stub = static_cast<Stub_SentimentPhrase*>(user_data);
    string elementName = (const char *)(name);
    if ("phrase" == elementName) {
        stub->GetPhrases()->push_back(stub->GetPhrase());
        stub->SetPhrase(new SentimentPhrase());
    }

    stub->SetCurrent("");
}

void SentimentPhraseXmlHandler::characters(void* user_data, const xmlChar* ch, int len) {
    Stub_SentimentPhrase* stub = static_cast<Stub_SentimentPhrase*>(user_data);
    string value = (const char *)(xmlStrncatNew(BAD_CAST "", xmlStrsub(ch, 0, len), len));
    SentimentPhrase* phrase = stub->GetPhrase();
    if (stub->GetCurrent() == "title") {
        phrase->SetTitle(phrase->GetTitle() + value);
    } else if (stub->GetCurrent() == "weight") {
        phrase->SetWeight(atof(value.c_str()));
    }
}
