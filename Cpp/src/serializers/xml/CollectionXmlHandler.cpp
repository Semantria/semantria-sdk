#include "CollectionXmlHandler.h"

CollectionXmlHandler::CollectionXmlHandler() {}
CollectionXmlHandler::~CollectionXmlHandler() {}

void CollectionXmlHandler::startElement(void* user_data, const xmlChar* name, const xmlChar** attrs) {
    Stub_CollAnalyticDatas* stub = static_cast<Stub_CollAnalyticDatas*>(user_data);
    string elementName = (const char *)(name);
    stub->SetCurrent(elementName);

    if ("collection" == elementName) {
        stub->SetCollAnalytic(new CollAnalyticData());
        stub->SetFacet(new Facet());
        stub->SetAttribute(new Attribute());
        stub->SetCollEntity(new CollEntity());
        stub->SetCollTheme(new CollTheme());
        stub->SetCollTopic(new CollTopic());
    }

    if (elementName == "collection" || elementName == "facet" || elementName == "attribute"
            || elementName == "entity" || elementName == "theme" || elementName == "topic") {
        stub->SetParent(elementName);
        stub->GetHierarchy()->push_back(elementName);
    }
}

void CollectionXmlHandler::endElement(void* user_data, const xmlChar* name) {
    Stub_CollAnalyticDatas* stub = static_cast<Stub_CollAnalyticDatas*>(user_data);
    string elementName = (const char *)(name);

    if (elementName == "collection" || elementName == "facet" || elementName == "attribute"
            || elementName == "entity" || elementName == "theme" || elementName == "topic") {
        stub->SetParent(stub->GetHierarchy()->back());
        stub->GetHierarchy()->pop_back();
        if (stub->GetHierarchy()->size() > 0) {
            stub->SetParent(stub->GetHierarchy()->at(stub->GetHierarchy()->size() - 1));
        }
    }

    if ("collection" == elementName) {
        stub->GetAnalyticData()->push_back(stub->GetCollAnalytic());
        stub->SetCollAnalytic(new CollAnalyticData());
    } else if ("entity" == elementName) {
        stub->GetCollAnalytic()->GetEntities()->push_back(stub->GetCollEntity());
        stub->SetCollEntity(new CollEntity());
    } else if ("attribute" == elementName) {
        stub->GetFacet()->GetAttributes()->push_back(stub->GetAttribute());
        stub->SetAttribute(new Attribute());
    } else if ("topic" == elementName) {
        stub->GetCollAnalytic()->GetTopics()->push_back(stub->GetCollTopic());
        stub->SetCollTopic(new CollTopic());
    } else if ("theme" == elementName) {
        stub->GetCollAnalytic()->GetThemes()->push_back(stub->GetCollTheme());
        stub->SetCollTheme(new CollTheme());
    } else if ("facet" == elementName) {
        stub->GetCollAnalytic()->GetFacets()->push_back(stub->GetFacet());
        stub->SetFacet(new Facet());
    }

    stub->SetCurrent("");
}

void CollectionXmlHandler::characters(void* user_data, const xmlChar* ch, int len) {
    Stub_CollAnalyticDatas* stub = static_cast<Stub_CollAnalyticDatas*>(user_data);
    string value = (const char *)(xmlStrncatNew(BAD_CAST "", xmlStrsub(ch, 0, len), len));

    if (stub->GetParent() == "collection") {
        CollAnalyticData* coll = stub->GetCollAnalytic();
        if (stub->GetCurrent() == "id") {
            coll->SetId(value);
        } else if (stub->GetCurrent() == "config_id") {
            coll->SetConfigId(value);
        } else if (stub->GetCurrent() == "status") {
            coll->SetStatusFromString(value);
        }
    } else if (stub->GetParent() == "entity") {
        CollEntity* entity = stub->GetCollEntity();
        if (stub->GetCurrent() == "count") {
            entity->SetCount(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "negative_count") {
            entity->SetNegativeCount(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "positive_count") {
            entity->SetPositiveCount(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "neutral_count") {
            entity->SetNeutralCount(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "title") {
            entity->SetTitle(entity->GetTitle() + value);
        } else if (stub->GetCurrent() == "type") {
            entity->SetType(value);
        } else if (stub->GetCurrent() == "entity_type") {
            entity->SetEntityType(value);
        }
    } else if (stub->GetParent() == "theme") {
        CollTheme* theme = stub->GetCollTheme();
        if (stub->GetCurrent() == "title") {
            theme->SetTitle(theme->GetTitle() + value);
        } else if (stub->GetCurrent() == "phrases_count") {
            theme->SetPhrasesCount(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "themes_count") {
            theme->SetThemesCount(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "sentiment_score") {
            theme->SetSentimentScore(atof(value.c_str()));
        }
    } else if (stub->GetParent() == "topic") {
        CollTopic* topic = stub->GetCollTopic();
        if (stub->GetCurrent() == "sentiment_score") {
            topic->SetSentimentScore(atof(value.c_str()));
        } else if (stub->GetCurrent() == "type") {
            topic->SetType(value);
        } else if (stub->GetCurrent() == "hitcount") {
            topic->SetHitCount(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "title") {
            topic->SetTitle(topic->GetTitle() + value);
        }
    } else if (stub->GetParent() == "facet") {
        Facet* facet = stub->GetFacet();
        if (stub->GetCurrent() == "count") {
            facet->SetCount(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "negative_count") {
            facet->SetNegativeCount(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "positive_count") {
            facet->SetPositiveCount(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "neutral_count") {
            facet->SetNeutralCount(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "label") {
            facet->SetLabel(facet->GetLabel() + value);
        }
    } else if (stub->GetParent() == "attribute") {
        Attribute* attribute = stub->GetAttribute();
        if (stub->GetCurrent() == "count") {
            attribute->SetCount(atoi(value.c_str()));
        } else if (stub->GetCurrent() == "label") {
            attribute->SetLabel(attribute->GetLabel() + value);
        }
    }
}
