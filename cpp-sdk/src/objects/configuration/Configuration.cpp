#include "Configuration.h"

Configuration::Configuration() {
    is_primary = false;

    //document
    entity_themes_limit = 0;
    summary_limit = 0;
    phrases_limit = 0;
    doc_themes_limit = 0;
    doc_query_topics_limit = 0;
    doc_concept_topics_limit = 0;
    doc_named_entities_limit = 0;
    user_entities_limit = 0;

    //collection
    facets_limit = 0;
    facet_atts_limit = 0;
    coll_themes_limit = 0;
    coll_query_topics_limit = 0;
    coll_concept_topics_limit = 0;
    coll_named_entities_limit = 0;
}

Configuration::~Configuration() {}

void Configuration::Serialize(Json::Value& root) {
    if (config_id != "") {
        root["config_id"] = config_id;
    }

    if (name != "") {
        root["name"] = name;
    }

    if (templateId != "") {
        root["template"] = templateId;
    }

    root["one_sentence"] = one_sentence;
    root["auto_responding"] = auto_responding;
    root["is_primary"] = is_primary;
    root["language"] = language;
    root["chars_threshold"] = chars_threshold;
    if (callback != "") {
        root["callback"] = callback;
    }

    //document
    root["document"]["entity_themes_limit"] = entity_themes_limit;
    root["document"]["summary_limit"] = summary_limit;
    root["document"]["phrases_limit"] = phrases_limit;
    root["document"]["themes_limit"] = doc_themes_limit;
    root["document"]["query_topics_limit"] = doc_query_topics_limit;
    root["document"]["concept_topics_limit"] = doc_concept_topics_limit;
    root["document"]["named_entities_limit"] = doc_named_entities_limit;
    root["document"]["user_entities_limit"] = user_entities_limit;

    //collection
    root["collection"]["facets_limit"] = facets_limit;
    root["collection"]["facet_atts_limit"] = facet_atts_limit;
    root["collection"]["themes_limit"] = coll_themes_limit;
    root["collection"]["query_topics_limit"] = coll_query_topics_limit;
    root["collection"]["concept_topics_limit"] = coll_concept_topics_limit;
    root["collection"]["named_entities_limit"] = coll_named_entities_limit;
}

void Configuration::Deserialize(Json::Value& root) {
    config_id = root.get("config_id", "").asString();
    name = root.get("name", "").asString();
    one_sentence = root.get("one_sentence", false).asBool();
    auto_responding = root.get("auto_responding", false).asBool();
    is_primary = root.get("is_primary", false).asBool();
    language = root.get("language", false).asString();
    chars_threshold = root.get("chars_threshold", 0).asUInt();
    callback = root.get("callback", "").asString();

    //document
    entity_themes_limit = root.get("entity_themes_limit", 0).asUInt();
    summary_limit = root.get("summary_limit", 0).asUInt();
    phrases_limit = root.get("phrases_limit", 0).asUInt();
    doc_themes_limit = root.get("themes_limit", 0).asUInt();
    doc_query_topics_limit = root.get("query_topics_limit", 0).asUInt();
    doc_concept_topics_limit = root.get("concept_topics_limit", 0).asUInt();
    doc_named_entities_limit = root.get("named_entities_limit", 0).asUInt();
    user_entities_limit = root.get("user_entities_limit", 0).asUInt();

    //collection
    facets_limit = root.get("facets_limit", 0).asUInt();
    facet_atts_limit = root.get("facet_atts_limit", 0).asUInt();
    coll_themes_limit = root.get("themes_limit", 0).asUInt();
    coll_query_topics_limit = root.get("query_topics_limit", 0).asUInt();
    coll_concept_topics_limit = root.get("concept_topics_limit", 0).asUInt();
    coll_named_entities_limit = root.get("named_entities_limit", 0).asUInt();
}
