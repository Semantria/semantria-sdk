#include "ConfigurationUpdateProxy.h"

ConfigurationUpdateProxy::ConfigurationUpdateProxy() {}
ConfigurationUpdateProxy::~ConfigurationUpdateProxy() {}

void ConfigurationUpdateProxy::Remove(Configuration* obj) {
    removed->push_back(obj->GetId());
}

void ConfigurationUpdateProxy::Clone(Configuration* obj) {
    obj->SetTemplate(obj->GetId());
    obj->SetId("");
    added->push_back(obj);
}

void ConfigurationUpdateProxy::Serialize(xmlNodePtr root) {
    if (added->size() > 0) {
        xmlNodePtr addedNode = xmlNewChild(root, NULL, BAD_CAST "added", NULL);
        for(vector<string>::size_type i = 0; i != added->size(); i++) {
            xmlNodePtr configurationNode = xmlNewChild(addedNode, NULL, BAD_CAST "configuration", NULL);
            Configuration* configuration = added->at(i);

            addNewXmlChild(configurationNode, "config_id", configuration->GetId());
            addNewXmlChild(configurationNode, "name", configuration->GetName());
            addNewXmlChild(configurationNode, "template", configuration->GetTemplate());
            addNewXmlChild(configurationNode, "is_primary", BoolToString(configuration->GetIsPrimary()));
            addNewXmlChild(configurationNode, "one_sentence", BoolToString(configuration->GetOneSentence()));
            addNewXmlChild(configurationNode, "auto_responding", BoolToString(configuration->GetAutoResponding()));
            addNewXmlChild(configurationNode, "language", configuration->GetLanguage());
            addNewXmlChild(configurationNode, "chars_threshold", IntToString(configuration->GetCharsThreshold()));
            addNewXmlChild(configurationNode, "callback", configuration->GetCallback());

            xmlNodePtr document = xmlNewChild(configurationNode, NULL, BAD_CAST "document", NULL);
            addNewXmlChild(document, "entity_themes_limit", IntToString(configuration->GetEntityThemesLimit()));
            addNewXmlChild(document, "summary_limit", IntToString(configuration->GetSummaryLimit()));
            addNewXmlChild(document, "phrases_limit", IntToString(configuration->GetPhrasesLimit()));
            addNewXmlChild(document, "themes_limit", IntToString(configuration->GetDocThemesLimit()));
            addNewXmlChild(document, "query_topics_limit", IntToString(configuration->GetDocQueryTopicsLimit()));
            addNewXmlChild(document, "concept_topics_limit", IntToString(configuration->GetDocConceptTopicsLimit()));
            addNewXmlChild(document, "named_entities_limit", IntToString(configuration->GetDocNamedEntitiesLimit()));
            addNewXmlChild(document, "user_entities_limit", IntToString(configuration->GetUserEntitiesLimit()));

            xmlNodePtr collection = xmlNewChild(configurationNode, NULL, BAD_CAST "collection", NULL);
            addNewXmlChild(collection, "facets_limit", IntToString(configuration->GetFacetsLimit()));
            addNewXmlChild(collection, "facet_atts_limit", IntToString(configuration->GetFacetAttributesLimit()));
            addNewXmlChild(collection, "themes_limit", IntToString(configuration->GetCollThemesLimit()));
            addNewXmlChild(collection, "query_topics_limit", IntToString(configuration->GetCollQueryTopicsLimit()));
            addNewXmlChild(collection, "concept_topics_limit", IntToString(configuration->GetCollConceptTopicsLimit()));
            addNewXmlChild(collection, "named_entities_limit", IntToString(configuration->GetCollNamedEntitiesLimit()));
        }
    }

    if (removed->size() > 0) {
        xmlNodePtr removedNode = xmlNewChild(root, NULL, BAD_CAST "removed", NULL);
        for(vector<string>::size_type i = 0; i != removed->size(); i++) {
            addNewXmlChild(removedNode, "configuration", removed->at(i));
        }
    }
}

string ConfigurationUpdateProxy::GetRootElement() {
    return "configurations";
}
