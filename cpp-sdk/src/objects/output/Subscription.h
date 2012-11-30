#ifndef SUBSCRIPTION_H
#define SUBSCRIPTION_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../serializers/xml/SubscriptionXmlHandler.h"

using namespace std;

class Subscription: public JsonSerializable, public XmlSerializable {
public:
    Subscription();
    virtual ~Subscription();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);
    void Deserialize(string source);
    void Serialize(xmlNodePtr node) {}
    xmlSAXHandler* GetXmlHandler();
    string GetRootElement() {return "";}

    string GetName() { return name; }
    string GetStatus() { return status; }
    string GetPriority() { return priority; }
    long GetExpirationDate() { return expiration_date; }
    int GetBatchLimit() { return batch_limit; }
    int GetCollectionLimit() { return collection_limit; }
    int GetAutoResponseLimit() { return auto_response_limit; }
    int GetProcessedBatchLimit() { return processed_batch_limit; }
    int GetCallbackBatchLimit() { return callback_batch_limit; }
    int GetCallsBalance() {return calls_balance;}
    int GetCallsLimit() {return calls_limit;}
    int GetCallsLimitInterval() {return calls_limit_interval;}
    int GetDocsBalance() {return docs_balance;}
    int GetDocsLimit() {return docs_limit;}
    int GetDocsLimitInterval() {return docs_limit_interval;}
    int GetConfigurationsLimit() {return configurations_limit;}
    int GetBlacklistLimit() {return blacklist_limit;}
    int GetCategoriesLimit() {return categories_limit;}
    int GetQueriesLimit() {return queries_limit;}
    int GetEntitiesLimit() {return entities_limit;}
    int GetSentimentLimit() {return sentiment_limit;}
    int GetCharactersLimit() {return characters_limit;}
    string GetLimitType() {return limit_type;}

    void SetName(string name) {this->name = name;}
    void SetStatus(string status) {this->status = status;}
    void SetPriority(string priority) {this->priority = priority;}
    void SetExpirationDate(long expiration_date) {this->expiration_date = expiration_date;}
    void SetBatchLimit(int batch_limit) {this->batch_limit = batch_limit;}
	void SetCollectionLimit(int collection_limit) {this->collection_limit = collection_limit;}
	void SetAutoResponseLimit(int auto_response_limit) {this->auto_response_limit = auto_response_limit;}
	void SetProcessedBatchLimit(int processed_batch_limit) {this->processed_batch_limit = processed_batch_limit;}
    void SetCallbackBatchLimit(int callback_batch_limit) {this->callback_batch_limit = callback_batch_limit;}
    void SetCallsBalance(int calls_balance) {this->calls_balance = calls_balance;}
    void SetCallsLimit(int calls_limit) {this->calls_limit = calls_limit;}
    void SetCallsLimitInterval(int calls_limit_interval) {this->calls_limit_interval = calls_limit_interval;}
    void SetDocsBalance(int docs_balance) {this->docs_balance = docs_balance;}
    void SetDocsLimit(int docs_limit) {this->docs_limit = docs_limit;}
    void SetDocsLimitInterval(int docs_limit_interval) {this->docs_limit_interval = docs_limit_interval;}
    void SetConfigurationsLimit(int configurations_limit) {this->configurations_limit = configurations_limit;}
    void SetBlacklistLimit(int blacklist_limit) {this->blacklist_limit = blacklist_limit;}
    void SetCategoriesLimit(int categories_limit) {this->categories_limit = categories_limit;}
    void SetQueriesLimit(int queries_limit) {this->queries_limit = queries_limit;}
    void SetEntitiesLimit(int entities_limit) {this->entities_limit = entities_limit;}
    void SetSentimentLimit(int sentiment_limit) {this->sentiment_limit = sentiment_limit;}
    void SetCharactersLimit(int characters_limit) {this->characters_limit = characters_limit;}
    void SetLimitType(string limit_type) {this->limit_type = limit_type;}

private:
    string name;
	string status;
	string priority;
	long expiration_date;
	int batch_limit;
	int collection_limit;
	int auto_response_limit;
	int processed_batch_limit;
    int callback_batch_limit;
    int calls_balance;
    int calls_limit;
    int calls_limit_interval;
    int docs_balance;
    int docs_limit;
    int docs_limit_interval;
    int configurations_limit;
    int blacklist_limit;
    int categories_limit;
    int queries_limit;
    int entities_limit;
    int sentiment_limit;
    int characters_limit;
    string limit_type;
};

#endif // SUBSCRIPTION_H
