#ifndef SESSION_H_
#define SESSION_H_

// Proxies
#include "proxies/SentimentPhraseUpdateProxy.h"
#include "proxies/ConfigurationUpdateProxy.h"
#include "proxies/BlacklistUpdateProxy.h"
#include "proxies/CategoryUpdateProxy.h"
#include "proxies/EntityUpdateProxy.h"
#include "proxies/QueryUpdateProxy.h"

// Stubs & Objects
#include "objects/stub/Stub_Blacklist.h"
#include "objects/stub/Stub_Categories.h"
#include "objects/stub/Stub_Configurations.h"
#include "objects/stub/Stub_Entities.h"
#include "objects/stub/Stub_Queries.h"
#include "objects/stub/Stub_SentimentPhrase.h"
#include "objects/stub/Stub_DocAnalyticDatas.h"
#include "objects/stub/Stub_CollAnalyticDatas.h"
#include "objects/stub/Stub_Tasks.h"
#include "objects/output/Document.h"
#include "objects/output/Collection.h"

// CallbackHandler & Serializer
#include "serializers/Serializer.h"
#include "serializers/xml/XmlSerializer.h"
#include "serializers/json/JsonSerializer.h"
#include "handlers/CallbackHandler.h"

//
#include "common/EnumsHelper.h"
#include "objects/output/ServiceStatus.h"
#include "objects/output/Subscription.h"
#include "connection/AuthRequest.h"


using namespace std;

class Session {
public:
    // Static method for getting instantiations
	static Session* CreateSession(string consumerKey, string consumerSecret);
	static Session* CreateSession(string consumerKey, string consumerSecret, string applicationName);
	static Session* CreateSession(string consumerKey, string consumerSecret, void* serializer);
	static Session* CreateSession(string consumerKey, string consumerSecret, void* serializer, string applicationName);

	virtual ~Session();

    // Proxies
	SentimentPhraseUpdateProxy* CreateSentimentPhraseUpdateProxy() {return new SentimentPhraseUpdateProxy();}
	ConfigurationUpdateProxy* CreateConfigurationUpdateProxy() {return new ConfigurationUpdateProxy();}
	BlacklistUpdateProxy* CreateBlacklistUpdateProxy() {return new BlacklistUpdateProxy();}
	CategoryUpdateProxy* CreateCategoryUpdateProxy() {return new CategoryUpdateProxy();}
	EntityUpdateProxy* CreateEntityUpdateProxy() {return new EntityUpdateProxy();}
	QueryUpdateProxy* CreateQueryUpdateProxy() {return new QueryUpdateProxy();}

    // CallbackHandler & Serializer
    void SetCallbackHandler(CallbackHandler* callbackHandler);
	void RegisterSerializer(void* serializer);

	// End-point calls
	ServiceStatus* GetStatus();
	Subscription* VerifySubscription();
	vector<string>* GetBlacklist();
	vector<string>* GetBlacklist(string configId);
	int UpdateBlacklist(UpdateProxy<Blacklisted>* proxy);
	int UpdateBlacklist(UpdateProxy<Blacklisted>* proxy, string configId);
	vector<Category*>* GetCategories();
	vector<Category*>* GetCategories(string configId);
	int UpdateCategories(UpdateProxy<Category>* proxy);
	int UpdateCategories(UpdateProxy<Category>* proxy, string configId);
	vector<Configuration*>* GetConfigurations();
	int UpdateConfigurations(UpdateProxy<Configuration>* proxy);
	int UpdateConfigurations(UpdateProxy<Configuration>* proxy, string configId);
	vector<UserEntity*>* GetEntities();
	vector<UserEntity*>* GetEntities(string configId);
	int UpdateEntities(UpdateProxy<UserEntity>* proxy);
	int UpdateEntities(UpdateProxy<UserEntity>* proxy, string configId);
	vector<Query*>* GetQueries();
	vector<Query*>* GetQueries(string configId);
	int UpdateQueries(UpdateProxy<Query>* proxy);
	int UpdateQueries(UpdateProxy<Query>* proxy, string configId);
	vector<SentimentPhrase*>* GetSentimentPhrases();
	vector<SentimentPhrase*>* GetSentimentPhrases(string configId);
	int UpdateSentimentPhrases(UpdateProxy<SentimentPhrase>* proxy);
	int UpdateSentimentPhrases(UpdateProxy<SentimentPhrase>* proxy, string configId);
	DocAnalyticData* GetDocument(string documentId);
	DocAnalyticData* GetDocument(string documentId, string configId);
	CollAnalyticData* GetCollection(string collectionId);
	CollAnalyticData* GetCollection(string collectionId, string configId);
	vector<DocAnalyticData*>* GetProcessedDocuments();
	vector<DocAnalyticData*>* GetProcessedDocuments(string configId);
	vector<CollAnalyticData*>* GetProcessedCollections();
	vector<CollAnalyticData*>* GetProcessedCollections(string configId);
	int QueueDocument(Document* document);
	int QueueDocument(Document* document, string configId);
	int QueueBatch(vector<Document*>* documents);
	int QueueBatch(vector<Document*>* documents, string configId);
	int QueueCollection(Collection* collection);
	int QueueCollection(Collection* collection, string configId);

private:
    // Constructors
	Session();
	Session(string consumerKey, string consumerSecret, void* serializer, string applicationName);

	void init(string consumerKey, string consumerSecret, void* serializer, string applicationName);

	void SetApplicationName(string applicationName);

	void HandleConfigurationResponse(int status, AuthRequest* authRequest);
	void HandleDocAutoResponse(int status, AuthRequest* authRequest);
	void HandleCollAutoResponse(int status, AuthRequest* authRequest);
	void HandleRequest(QueryMethod method, string url, string message);

	template <class T>
    int UpdateItems(string path, UpdateProxy<T>* proxy, string configId);

	string consumerKey;
	string consumerSecret;
	string format;
	string applicationName;
	void* serializer;
	CallbackHandler* callbackHandler;

	static const string host;
	static const string wrapperName;
};

#endif /* SESSION_H_ */
