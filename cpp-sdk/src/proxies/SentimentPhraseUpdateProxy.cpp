#include "SentimentPhraseUpdateProxy.h"

SentimentPhraseUpdateProxy::SentimentPhraseUpdateProxy() {}
SentimentPhraseUpdateProxy::~SentimentPhraseUpdateProxy() {}

void SentimentPhraseUpdateProxy::Remove(SentimentPhrase* phrase) {
    removed->push_back(phrase->GetTitle());
}

void SentimentPhraseUpdateProxy::Serialize(xmlNodePtr root) {
    if (added->size() > 0) {
        xmlNodePtr addedNode = xmlNewChild(root, NULL, BAD_CAST "added", NULL);
        for(vector<string>::size_type i = 0; i != added->size(); i++) {
            xmlNodePtr phrase = xmlNewChild(addedNode, NULL, BAD_CAST "phrase", NULL);
            SentimentPhrase* sentimentPhrase = added->at(i);
            addNewXmlChild(phrase, "title", sentimentPhrase->GetTitle());
            addNewXmlChild(phrase, "weight", DoubleToString(sentimentPhrase->GetWeight()));
        }
    }

    if (removed->size() > 0) {
        xmlNodePtr removedNode = xmlNewChild(root, NULL, BAD_CAST "removed", NULL);
        for(vector<string>::size_type i = 0; i != removed->size(); i++) {
            addNewXmlChild(removedNode, "phrase", removed->at(i));
        }
    }
}

string SentimentPhraseUpdateProxy::GetRootElement() {
    return "phrases";
}
