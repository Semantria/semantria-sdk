#include "Stub_SentimentPhrase.h"

Stub_SentimentPhrase::Stub_SentimentPhrase() {
    phrases = new vector<SentimentPhrase*>();
    phrase = NULL;
}

Stub_SentimentPhrase::~Stub_SentimentPhrase() {
    // For removing stub's items use clear() function instead
    phrase = NULL;
}

void Stub_SentimentPhrase::Serialize(Json::Value& root) {}

void Stub_SentimentPhrase::Deserialize(Json::Value& root) {
    if (NULL == this->phrases) {
        this->phrases = new vector<SentimentPhrase*>();
    }

    for ( int i = 0; i < root.size(); ++i ) {
        SentimentPhrase* phrase = new SentimentPhrase();
        phrase->Deserialize(root[i]);
        this->phrases->push_back(phrase);
    }
}

void Stub_SentimentPhrase::Deserialize(std::string source) {
    xmlSAXUserParseMemory(GetXmlHandler(), this, source.c_str(), int(source.length()));
    xmlCleanupParser();
}

void Stub_SentimentPhrase::Serialize(xmlNodePtr root) {}

xmlSAXHandler* Stub_SentimentPhrase::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &SentimentPhraseXmlHandler::startElement;
    result->endElement = &SentimentPhraseXmlHandler::endElement;
    result->characters = &SentimentPhraseXmlHandler::characters;

    return result;
}

string Stub_SentimentPhrase::GetRootElement() {
    return "sentiment";
}
