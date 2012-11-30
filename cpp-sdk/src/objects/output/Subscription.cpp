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
	priority = root.get("priority", "").asString();
	expiration_date = (long)root.get("expiration_date", 0).asDouble();
	batch_limit = root.get("batch_limit", 0).asInt();
    collection_limit = root.get("collection_limit", 0).asInt();
    auto_response_limit = root.get("auto_response_limit", 0).asInt();
    processed_batch_limit = root.get("processed_batch_limit", 0).asInt();
    callback_batch_limit = root.get("callback_batch_limit", 0).asInt();
    calls_balance = root.get("calls_balance", 0).asInt();
    calls_limit = root.get("calls_limit", 0).asInt();
    calls_limit_interval = root.get("calls_limit_interval", 0).asInt();
    docs_balance = root.get("docs_balance", 0).asInt();
    docs_limit = root.get("docs_limit", 0).asInt();
    docs_limit_interval = root.get("docs_limit_interval", 0).asInt();
    configurations_limit = root.get("configurations_limit", 0).asInt();
    blacklist_limit = root.get("blacklist_limit", 0).asInt();
    categories_limit = root.get("categories_limit", 0).asInt();
    queries_limit = root.get("queries_limit", 0).asInt();
    entities_limit = root.get("entities_limit", 0).asInt();
    sentiment_limit = root.get("sentiment_limit", 0).asInt();
    characters_limit = root.get("characters_limit", 0).asInt();
    limit_type = root.get("limit_type", "").asString();
}

void Subscription::Deserialize(string source) {
    xmlSAXHandler* handler = GetXmlHandler();
    int result = xmlSAXUserParseMemory(handler, this, source.c_str(), int(source.length()));
    xmlCleanupParser();
}

xmlSAXHandler* Subscription::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &SubscriptionXmlHandler::startElement;
    result->endElement = &SubscriptionXmlHandler::endElement;
    result->characters = &SubscriptionXmlHandler::characters;

    return result;
}
