#ifndef SESSION_H_
#define SESSION_H_

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
#include "objects/configuration/Blacklisted.h"
#include "objects/output/Statistics.h"

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

    // CallbackHandler & Serializer
    void SetCallbackHandler(CallbackHandler* callbackHandler);

	// End-point calls
	ServiceStatus* GetStatus();
	Subscription* VerifySubscription();
    
    
    // Blacklisted
	vector<string>* GetBlacklist(string configId = "");
    int AddBlacklist(vector<Blacklisted*>* list, string configId = "");
    int UpdateBlacklist(vector<Blacklisted*>* list, string configId = "");
    int RemoveBlacklist(vector<string>* list, string configId = "");
    
    // Categories
	vector<Category*>* GetCategories(string configId = "");
    int AddCategories(vector<Category*>* list, string configId = "" );
    int UpdateCategories(vector<Category*>* list, string configId = "" );
    int RemoveCategories(vector<string>* removeIds, string configId = "" );

    // Configuration
	vector<Configuration*>* GetConfigurations();
    int AddConfigurations(vector<Configuration*>*);
    int UpdateConfigurations(vector<Configuration*>*);
    int RemoveConfigurations(vector<string>*);
    
    // User Entities
	vector<UserEntity*>* GetEntities(string configId = "");
    int AddEntities(vector<UserEntity*>* list, string configId = "" );
    int UpdateEntities(vector<UserEntity*>* list, string configId = "" );
    int RemoveEntities(vector<string>* removeIds, string configId = "" );
    
    // Queries
	vector<Query*>* GetQueries(string configId = "");
    int AddQueries(vector<Query*>* list, string configId = "");
    int UpdateQueries(vector<Query*>* list, string configId = "");
    int RemoveQueries(vector<string>* list, string configId = "");

    // SP
	vector<SentimentPhrase*>* GetSentimentPhrases(string configId = "");
    int AddSentimentPhrases(vector<SentimentPhrase*>* list, string configId = "");
    int UpdateSentimentPhrases(vector<SentimentPhrase*>* list, string configId = "");
    int RemoveSentimentPhrases(vector<string>* list, string configId = "");
    
    Statistics*                 GetStatistics( void );
    
	DocAnalyticData*            GetDocument(string documentId, string configId = "");
	CollAnalyticData*           GetCollection(string collectionId, string configId = "");
	vector<DocAnalyticData*>*   GetProcessedDocuments(string configId = "");
	vector<CollAnalyticData*>*  GetProcessedCollections(string configId = "");
    
	int QueueDocument(Document* document, string configId = "");
	int QueueBatch(vector<Document*>* documents, string configId = "");
	int QueueCollection(Collection* collection, string configId = "");

    void SetUseCompression( bool useCompression ){ this->useCompression = useCompression; };
    bool GetUseCompression( void ){ return this->useCompression; };
    
private:
    // Constructors
	Session();
	Session(string consumerKey, string consumerSecret, void* serializer, string applicationName);

	void init(string consumerKey, string consumerSecret, void* serializer, string applicationName);

	void RegisterSerializer(void* serializer);

    string GetFullApplicationName();

	void HandleConfigurationResponse(int status, AuthRequest* authRequest);
	void HandleDocAutoResponse(int status, AuthRequest* authRequest);
	void HandleCollAutoResponse(int status, AuthRequest* authRequest);
	void HandleRequest(QueryMethod method, string url, string message);

	template <class T>
    int UpdateItems(string path, vector<T>* list, string configId);
    int RemoveItems(string path, vector<string>* removeIds, string configId = "" );
    string SerializeVectorOfStringsToJson(vector<string>* list);

	string consumerKey;
	string consumerSecret;
    
	string format;
    bool useCompression;
    
	string applicationName;
	void* serializer;
	CallbackHandler* callbackHandler;

	static const string host;
	static const string wrapperName;
    static const string wrapperVersion;
};

#endif /* SESSION_H_ */
