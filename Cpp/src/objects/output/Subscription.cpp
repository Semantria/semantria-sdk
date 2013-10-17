#include "Subscription.h"

Subscription::Subscription() {}

Subscription::~Subscription() {}

void Subscription::Serialize(Json::Value& root) {
	// We don't need to serialize Subscription object, cause we
	// can only receive it from Analytic Service, and can not send
}

void Subscription::Deserialize(Json::Value& root) {
	name = root.get("name", "").asString();
	status= root.get("status", "").asString();

    Json::Value bs  = root.get("billing_settings", Json::Value(Json::objectValue));
    priority = bs.get("priority", "").asString();
    expiration_date = (long)bs.get("expiration_date", 0).asDouble();
    limit_type = bs.get("limit_type", "").asString();
    calls_balance = bs.get("calls_balance", 0).asInt();
    calls_limit = bs.get("calls_limit", 0).asInt();
    calls_limit_interval = bs.get("calls_limit_interval", 0).asInt();
    docs_balance = bs.get("docs_balance", 0).asInt();
    docs_limit = bs.get("docs_limit", 0).asInt();
    docs_limit_interval = bs.get("docs_limit_interval", 0).asInt();
    docs_suggested = bs.get("docs_suggested", 0).asInt();
    docs_suggested_interval = bs.get("docs_suggested_interval", 0).asInt();

    bs = root.get("basic_settings", Json::Value(Json::objectValue));
    configurations_limit = bs.get("configurations_limit", 0).asInt();
    output_data_limit = bs.get("output_data_limit", 0).asInt();
    blacklist_limit = bs.get("blacklist_limit", 0).asInt();
    categories_limit = bs.get("categories_limit", 0).asInt();
    category_samples_limit = bs.get("category_samples_limit", 0).asInt();
    return_source_text = bs.get("return_source_text", false).asBool();
    queries_limit = bs.get("queries_limit", 0).asInt();
    entities_limit = bs.get("entities_limit", 0).asInt();
    sentiment_limit = bs.get("sentiment_limit", 0).asInt();
    characters_limit = bs.get("characters_limit", 0).asInt();
	batch_limit = bs.get("batch_limit", 0).asInt();
    collection_limit = bs.get("collection_limit", 0).asInt();
    auto_response_limit = bs.get("auto_response_limit", 0).asInt();
    processed_batch_limit = bs.get("processed_batch_limit", 0).asInt();
    callback_batch_limit = bs.get("callback_batch_limit", 0).asInt();

    Json::Value fs = root.get("feature_settings", Json::Value(Json::objectValue));

    Json::Value doc = fs.get("document", Json::Value(Json::objectValue));
    summary = doc.get("summary", 0).asBool();
    auto_categories = doc.get("auto_categories", 0).asBool();
    doc_themes = doc.get("themes", 0).asBool();
    doc_named_entities = doc.get("named_entities", 0).asBool();
    doc_user_entities = doc.get("user_entities", 0).asBool();
    entity_themes = doc.get("entity_themes", 0).asBool();
    doc_mentions = doc.get("mentions", 0).asBool();
    opinions = doc.get("opinions", 0).asBool();
    named_relations = doc.get("named_relations", 0).asBool();
    user_relations = doc.get("user_relations", 0).asBool();
    doc_query_topics = doc.get("query_topics", 0).asBool();
    doc_concept_topics = doc.get("concept_topics", 0).asBool();
    sentiment_phrases = doc.get("sentiment_phrases", 0).asBool();
    phrases_detection = doc.get("phrases_detection", 0).asBool();
    pos_tagging = doc.get("pos_tagging", 0).asBool();
    language_detection = doc.get("language_detection", 0).asBool();

    Json::Value collection = fs.get("collection", Json::Value(Json::objectValue));
    facets = collection.get("facets", 0).asBool();
    mentions = collection.get("mentions", 0).asBool();
    coll_themes = collection.get("themes", 0).asBool();
    coll_named_entities = collection.get("named_entities", 0).asBool();
    //coll_user_entities = collection.get("user_entities", 0).asBool();
    coll_query_topics = collection.get("query_topics", 0).asBool();
    coll_concept_topics = collection.get("concept_topics", 0).asBool();

    supported_languages = fs.get("supported_languages", "").asString();
    html_processing = fs.get("html_processing", "").asBool();
}

void Subscription::Deserialize(string source) {
    xmlSAXHandler* handler = GetXmlHandler();
    xmlSAXUserParseMemory(handler, this, source.c_str(), int(source.length()));
    xmlCleanupParser();
}

xmlSAXHandler* Subscription::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &SubscriptionXmlHandler::startElement;
    result->endElement = &SubscriptionXmlHandler::endElement;
    result->characters = &SubscriptionXmlHandler::characters;

    return result;
}
