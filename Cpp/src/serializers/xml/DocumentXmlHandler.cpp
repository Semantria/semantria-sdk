#include "DocumentXmlHandler.h"

DocumentXmlHandler::DocumentXmlHandler() {}
DocumentXmlHandler::~DocumentXmlHandler() {}

void DocumentXmlHandler::startElement(void* user_data, const xmlChar* name, const xmlChar** attrs) {
    Stub_DocAnalyticDatas* stub = static_cast<Stub_DocAnalyticDatas*>(user_data);
    string elementName = (const char *)(name);
    stub->SetCurrent(elementName);

    if ("document" == elementName) {
        stub->SetDocAnalytic(new DocAnalyticData());
        stub->SetDocEntity(new DocEntity());
        stub->SetDocTheme(new DocTheme());
        stub->SetDocTopic(new DocTopic());
        stub->SetDocPhrase(new DocPhrase());
    }

    if (elementName == "document" || elementName == "entity" || elementName == "theme"
            || elementName == "topic" || elementName == "phrase") {
        stub->SetParent(elementName);
        stub->GetHierarchy()->push_back(elementName);
    }
}

void DocumentXmlHandler::endElement(void* user_data, const xmlChar* name) {
    Stub_DocAnalyticDatas* stub = static_cast<Stub_DocAnalyticDatas*>(user_data);
    string elementName = (const char *)(name);

    if (elementName == "document" || elementName == "entity" || elementName == "theme"
            || elementName == "topic" || elementName == "phrase") {
        stub->SetParent(stub->GetHierarchy()->back());
        stub->GetHierarchy()->pop_back();
        if (stub->GetHierarchy()->size() > 0) {
            stub->SetParent(stub->GetHierarchy()->at(stub->GetHierarchy()->size() - 1));
        }
    }

    if ("document" == elementName) {
        stub->GetAnalyticData()->push_back(stub->GetDocAnalytic());
        stub->SetDocAnalytic(new DocAnalyticData());
    } else if ("entity" == elementName) {
        stub->GetDocAnalytic()->GetEntities()->push_back(stub->GetDocEntity());
        stub->SetDocEntity(new DocEntity());
    } else if ("phrase" == elementName) {
        stub->GetDocAnalytic()->GetPhrases()->push_back(stub->GetDocPhrase());
        stub->SetDocPhrase(new DocPhrase());
    } else if ("topic" == elementName) {
        stub->GetDocAnalytic()->GetTopics()->push_back(stub->GetDocTopic());
        stub->SetDocTopic(new DocTopic());
    } else if ("theme" == elementName) {
        if ("document" == stub->GetParent()) {
            stub->GetDocAnalytic()->GetThemes()->push_back(stub->GetDocTheme());
        } else if ("entity" == stub->GetParent()) {
            stub->GetDocEntity()->GetThemes()->push_back(stub->GetDocTheme());
        }
        stub->SetDocTheme(new DocTheme());
    }

    stub->SetCurrent("");
}

void DocumentXmlHandler::characters(void* user_data, const xmlChar* ch, int len) {
    Stub_DocAnalyticDatas* stub = static_cast<Stub_DocAnalyticDatas*>(user_data);
    string value = (const char *)(xmlStrncatNew(BAD_CAST "", xmlStrsub(ch, 0, len), len));

    if (stub->GetParent() == "document") {
        DocAnalyticData* doc = stub->GetDocAnalytic();
        if (stub->GetCurrent() == "id") {
            doc->SetId(value);
        } else if (stub->GetCurrent() == "config_id") {
            doc->SetConfigId(value);
        } else if (stub->GetCurrent() == "status") {
            doc->SetStatusFromString(value);
        } else if (stub->GetCurrent() == "sentiment_score") {
            doc->SetSentimentScore(atof(value.c_str()));
        } else if (stub->GetCurrent() == "summary") {
            doc->SetSummary(doc->GetSummary() + value);
        }
    } else if (stub->GetParent() == "entity") {
        DocEntity* entity = stub->GetDocEntity();
        if (stub->GetCurrent() == "evidence") {
            entity->SetEvidence(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "sentiment_score") {
            entity->SetSentimentScore(atof(value.c_str()));
        } else if (stub->GetCurrent() == "title") {
            entity->SetTitle(entity->GetTitle() + value);
        } else if (stub->GetCurrent() == "type") {
            entity->SetType(value);
        } else if (stub->GetCurrent() == "is_about") {
            entity->SetIsAbout(stub->StringToBool(value));
        } else if (stub->GetCurrent() == "confident") {
            entity->SetConfident(stub->StringToBool(value));
        } else if (stub->GetCurrent() == "entity_type") {
            entity->SetEntityType(value);
        }
    } else if (stub->GetParent() == "theme") {
        DocTheme* theme = stub->GetDocTheme();
        if (stub->GetCurrent() == "evidence") {
            theme->SetEvidence(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "is_about") {
            theme->SetIsAbout(stub->StringToBool(value));
        } else if (stub->GetCurrent() == "sentiment_score") {
            theme->SetSentimentScore(atof(value.c_str()));
        } else if (stub->GetCurrent() == "strength_score") {
            theme->SetStrengthScore(atof(value.c_str()));
        } else if (stub->GetCurrent() == "title") {
            theme->SetTitle(theme->GetTitle() + value);
        }
    } else if (stub->GetParent() == "topic") {
        DocTopic* topic = stub->GetDocTopic();
        if (stub->GetCurrent() == "sentiment_score") {
            topic->SetSentimentScore(atof(value.c_str()));
        } else if (stub->GetCurrent() == "strength_score") {
            topic->SetStrengthScore(atof(value.c_str()));
        } else if (stub->GetCurrent() == "type") {
            topic->SetType(value);
        } else if (stub->GetCurrent() == "hitcount") {
            topic->SetHitCount(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "title") {
            topic->SetTitle(topic->GetTitle() + value);
        }
    } else if (stub->GetParent() == "phrase") {
        DocPhrase* phrase = stub->GetDocPhrase();
        if (stub->GetCurrent() == "sentiment_score") {
            phrase->SetSentimentScore(atof(value.c_str()));
        } else if (stub->GetCurrent() == "is_negated") {
            phrase->SetIsNegated(stub->StringToBool(value));
        } else if (stub->GetCurrent() == "negating_phrase") {
            phrase->SetNegatingPhrase(phrase->GetNegatingPhrase() + value);
        } else if (stub->GetCurrent() == "title") {
            phrase->SetTitle(phrase->GetTitle() + value);
        }
    }
}
