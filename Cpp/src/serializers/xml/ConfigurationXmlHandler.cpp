#include "ConfigurationXmlHandler.h"
#include <cstdlib>

ConfigurationXmlHandler::ConfigurationXmlHandler() {}
ConfigurationXmlHandler::~ConfigurationXmlHandler() {}

void ConfigurationXmlHandler::startElement(void* user_data, const xmlChar* name, const xmlChar** attrs) {
    Stub_Configurations* stub = static_cast<Stub_Configurations*>(user_data);
    string elementName = (const char *)(name);

    if ("configuration" == elementName) {
        stub->SetConfiguration(new Configuration());
    }

    if ("document" == elementName || "collection" == elementName) {
        stub->SetParent(elementName);
        stub->GetHierarchy()->push_back(elementName);
    }

    stub->SetCurrent(elementName);
}

void ConfigurationXmlHandler::endElement(void* user_data, const xmlChar* name) {
    Stub_Configurations* stub = static_cast<Stub_Configurations*>(user_data);
    string elementName = (const char *)(name);

    if ("document" == elementName || "collection" == elementName) {
        stub->SetParent(stub->GetHierarchy()->back());
        stub->GetHierarchy()->pop_back();
        if (stub->GetHierarchy()->size() > 0) {
            stub->SetParent(stub->GetHierarchy()->at(stub->GetHierarchy()->size() - 1));
        }
    }

    if ("configuration" == elementName) {
        stub->GetConfigurations()->push_back(stub->GetConfiguration());
        stub->SetConfiguration(new Configuration());
    }
    stub->SetCurrent("");
}

void ConfigurationXmlHandler::characters(void* user_data, const xmlChar* ch, int len) {
    Stub_Configurations* stub = static_cast<Stub_Configurations*>(user_data);
    string value = (const char *)(xmlStrncatNew(BAD_CAST "", xmlStrsub(ch, 0, len), len));
    Configuration* config = stub->GetConfiguration();

    if (stub->GetCurrent() == "config_id") {
        config->SetId(value);
    } else if (stub->GetCurrent() == "name") {
        config->SetName(config->GetName() + value);
    } else if (stub->GetCurrent() == "is_primary") {
        config->SetIsPrimary(stub->StringToBool(value));
    } else if (stub->GetCurrent() == "one_sentence") {
        config->SetOneSentence(stub->StringToBool(value));
    } else if (stub->GetCurrent() == "auto_response") {
        config->SetAutoResponding(stub->StringToBool(value));
    } else if (stub->GetCurrent() == "language") {
        config->SetLanguage(value);
    } else if (stub->GetCurrent() == "chars_threshold") {
        config->SetCharsThreshold(atoi((value).c_str()));
    } else if (stub->GetCurrent() == "callback") {
        config->SetCallback(value);
    } else if (stub->GetCurrent() == "phrases_limit") {
        config->SetPhrasesLimit(atoi(value.c_str()));
    } else if (stub->GetCurrent() == "themes_limit") {
        if (stub->GetParent() == "document") {
            config->SetDocThemesLimit(atoi(value.c_str()));
        } else if (stub->GetParent() == "collection") {
            config->SetCollThemesLimit(atoi(value.c_str()));
        }
    } else if (stub->GetCurrent() == "named_entities_limit") {
        if (stub->GetParent() == "document") {
            config->SetDocNamedEntitiesLimit(atoi(value.c_str()));
        } else if (stub->GetParent() == "collection") {
            config->SetCollNamedEntitiesLimit(atoi(value.c_str()));
        }
    } else if (stub->GetCurrent() == "user_entities_limit") {
        config->SetUserEntitiesLimit(atoi(value.c_str()));
    } else if (stub->GetCurrent() == "entity_themes_limit") {
        config->SetEntityThemesLimit(atoi(value.c_str()));
    } else if (stub->GetCurrent() == "concept_topics_limit") {
        if (stub->GetParent() == "document") {
            config->SetDocConceptTopicsLimit(atoi(value.c_str()));
        } else if (stub->GetParent() == "collection") {
            config->SetCollConceptTopicsLimit(atoi(value.c_str()));
        }
    } else if (stub->GetCurrent() == "query_topics_limit") {
        if (stub->GetParent() == "document") {
            config->SetDocQueryTopicsLimit(atoi(value.c_str()));
        } else if (stub->GetParent() == "collection") {
            config->SetCollQueryTopicsLimit(atoi(value.c_str()));
        }
    } else if (stub->GetCurrent() == "summary_limit") {
        config->SetSummaryLimit(atoi(value.c_str()));
    } else if (stub->GetCurrent() == "facet_atts_limit") {
        config->SetFacetAttributesLimit(atoi(value.c_str()));
    } else if (stub->GetCurrent() == "facets_limit") {
        config->SetFacetsLimit(atoi(value.c_str()));
    }
}
