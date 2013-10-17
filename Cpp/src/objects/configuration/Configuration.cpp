#include "Configuration.h"

Configuration::Configuration() {
    config_id = "";
    name = "";
    is_primary = false;
    auto_response = false;
    process_html = 0;

    //document
    entity_themes_limit = 0;
    summary_limit = 0;
    phrases_limit = 0;
    possible_phrases_limit = 0;
    auto_categories_limit = 5;
    doc_themes_limit = 0;
    doc_query_topics_limit = 0;
    doc_concept_topics_limit = 0;
    doc_named_entities_limit = 0;
    user_entities_limit = 0;
    named_relations_limit = 0;
    user_relations_limit = 0;
    pos_types = "";
    detect_language = true;
    doc_named_mentions_limit = 0;
    user_mentions_limit = 0;
    doc_theme_mentions_limit = 0;
    named_opinions_limit = 0;
    user_opinions_limit = 0;

    //collection
    facets_limit = 0;
    facet_atts_limit = 0;
    facet_mentions_limit = 0;
    attribute_mentions_limit = 0;
    coll_themes_limit = 0;
    coll_query_topics_limit = 0;
    coll_concept_topics_limit = 0;
    coll_named_entities_limit = 0;

    coll_named_mentions_limit = 0;
    coll_theme_mentions_limit = 0;
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
    root["process_html"] = process_html;
    root["auto_response"] = auto_response;
    root["is_primary"] = is_primary;
    root["language"] = language;
    root["chars_threshold"] = chars_threshold;
    if (callback != "") {
        root["callback"] = callback;
    }

    //document
    root["document"]["concept_topics_limit"] = doc_concept_topics_limit;
    root["document"]["query_topics_limit"] = doc_query_topics_limit;
    root["document"]["named_entities_limit"] = doc_named_entities_limit;
    root["document"]["user_entities_limit"] = user_entities_limit;
    root["document"]["entity_themes_limit"] = entity_themes_limit;
    root["document"]["themes_limit"] = doc_themes_limit;
    root["document"]["named_relations_limit"] = named_relations_limit;
    root["document"]["user_relations_limit"] = user_relations_limit;
    root["document"]["phrases_limit"] = phrases_limit;
    root["document"]["possible_phrases_limit"] = possible_phrases_limit;
    root["document"]["auto_categories_limit"] = auto_categories_limit;
    root["document"]["pos_types"] = pos_types;
    root["document"]["summary_limit"] = summary_limit;
    root["document"]["detect_language"] = detect_language;
    root["document"]["named_mentions_limit"] = doc_named_mentions_limit;
    root["document"]["user_mentions_limit"] = user_mentions_limit;
    root["document"]["theme_mentions_limit"] = doc_theme_mentions_limit;
    root["document"]["named_opinions_limit"] = named_opinions_limit;
    root["document"]["user_opinions_limit"] = user_opinions_limit;


    //collection
    root["collection"]["facets_limit"] = facets_limit;
    root["collection"]["facet_atts_limit"] = facet_atts_limit;
    root["collection"]["facet_mentions_limit"] = facet_mentions_limit;
    root["collection"]["attribute_mentions_limit"] = attribute_mentions_limit;
    root["collection"]["themes_limit"] = coll_themes_limit;
    root["collection"]["query_topics_limit"] = coll_query_topics_limit;
    root["collection"]["concept_topics_limit"] = coll_concept_topics_limit;
    root["collection"]["named_entities_limit"] = coll_named_entities_limit;

    root["collection"]["named_mentions_limit"] = coll_named_mentions_limit;
    root["collection"]["theme_mentions_limit"] = coll_theme_mentions_limit;
}

void Configuration::Deserialize(Json::Value& root) {
    config_id = root.get("config_id", "").asString();
    name = root.get("name", "").asString();
    one_sentence = root.get("one_sentence", false).asBool();
    process_html = root.get("process_html", false).asBool();
    auto_response = root.get("auto_response", false).asBool();
    is_primary = root.get("is_primary", false).asBool();
    language = root.get("language", false).asString();
    chars_threshold = root.get("chars_threshold", 0).asUInt();
    callback = root.get("callback", "").asString();

    //document
    Json::Value doc = root.get("document", "");
    doc_concept_topics_limit = doc.get("concept_topics_limit", 0).asUInt();
    doc_query_topics_limit = doc.get("query_topics_limit", 0).asUInt();
    doc_named_entities_limit = doc.get("named_entities_limit", 0).asUInt();
    user_entities_limit = doc.get("user_entities_limit", 0).asUInt();
    entity_themes_limit = doc.get("entity_themes_limit", 0).asUInt();
    doc_themes_limit = doc.get("themes_limit", 0).asUInt();
    named_relations_limit = doc.get("named_relations_limit", 0).asUInt();
    user_relations_limit = doc.get("user_relations_limit", 0).asUInt();
    phrases_limit = doc.get("phrases_limit", 0).asUInt();
    possible_phrases_limit = doc.get("possible_phrases_limit", 0).asUInt();
    auto_categories_limit = doc.get("auto_categories_limit", 0).asUInt();
    pos_types = doc.get("pos_types", "").asString();
    summary_limit = doc.get("summary_limit", 0).asUInt();
    detect_language = doc.get("detect_language", true).asBool();

    doc_named_mentions_limit = doc.get("named_mentions_limit", 0).asUInt();
    user_mentions_limit = doc.get("user_mentions_limit", 0).asUInt();
    doc_theme_mentions_limit = doc.get("theme_mentions_limit", 0).asUInt();
    named_opinions_limit = doc.get("named_opinions_limit", 0).asUInt();
    user_opinions_limit = doc.get("user_opinions_limit", 0).asUInt();

    //collection
    Json::Value coll = root.get("document", "");
    facets_limit = coll.get("facets_limit", 0).asUInt();
    facet_atts_limit = coll.get("facet_atts_limit", 0).asUInt();
    facet_mentions_limit = coll.get("facet_mentions_limit", 0).asUInt();
    attribute_mentions_limit = coll.get("attribute_mentions_limit", 0).asUInt();
    coll_themes_limit = coll.get("themes_limit", 0).asUInt();
    coll_query_topics_limit = coll.get("query_topics_limit", 0).asUInt();
    coll_concept_topics_limit = coll.get("concept_topics_limit", 0).asUInt();
    coll_named_entities_limit = coll.get("named_entities_limit", 0).asUInt();
    coll_named_mentions_limit = coll.get("named_mentions_limit", 0).asUInt();
    coll_theme_mentions_limit = coll.get("theme_mentions_limit", 0).asUInt();
}
