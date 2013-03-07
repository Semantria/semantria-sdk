#include "SubscriptionXmlHandler.h"

SubscriptionXmlHandler::SubscriptionXmlHandler() {}
SubscriptionXmlHandler::~SubscriptionXmlHandler() {}

void SubscriptionXmlHandler::startElement(void* user_data, const xmlChar* name, const xmlChar** attrs) {
    Subscription* subscription = static_cast<Subscription*>(user_data);
    string elementName = (const char *)(name);
    subscription->SetCurrent(elementName);
}

void SubscriptionXmlHandler::endElement(void* user_data, const xmlChar* name) {
    Subscription* subscription = static_cast<Subscription*>(user_data);
    subscription->SetCurrent("");
}

void SubscriptionXmlHandler::characters(void* user_data, const xmlChar* ch, int len) {
    Subscription* subscription = static_cast<Subscription*>(user_data);
    string value = (const char *)(xmlStrncatNew(BAD_CAST "", xmlStrsub(ch, 0, len), len));
    if (subscription->GetCurrent() == "name") {
        subscription->SetName(subscription->GetName() + value);
    } else if (subscription->GetCurrent() == "status") {
        subscription->SetStatus(value);
    } else if (subscription->GetCurrent() == "priority") {
        subscription->SetPriority(value);
    } else if (subscription->GetCurrent() == "expiration_date") {
        subscription->SetExpirationDate(atol(value.c_str()));
    } else if (subscription->GetCurrent() == "batch_limit") {
        subscription->SetBatchLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "collection_limit") {
        subscription->SetCollectionLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "auto_response_limit") {
        subscription->SetAutoResponseLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "processed_batch_limit") {
        subscription->SetProcessedBatchLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "callback_batch_limit") {
        subscription->SetCallbackBatchLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "calls_balance") {
        subscription->SetCallsBalance(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "calls_limit") {
        subscription->SetCallsLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "calls_limit_interval") {
        subscription->SetCallsLimitInterval(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "docs_balance") {
        subscription->SetDocsBalance(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "docs_limit") {
        subscription->SetDocsLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "docs_limit_interval") {
        subscription->SetDocsLimitInterval(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "configurations_limit") {
        subscription->SetConfigurationsLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "blacklist_limit") {
        subscription->SetBlacklistLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "categories_limit") {
        subscription->SetCategoriesLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "queries_limit") {
        subscription->SetQueriesLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "entities_limit") {
        subscription->SetEntitiesLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "sentiment_limit") {
        subscription->SetSentimentLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "characters_limit") {
        subscription->SetCharactersLimit(atoi(value.c_str()));
    } else if (subscription->GetCurrent() == "limit_type") {
        subscription->SetLimitType(value);
    }
}
