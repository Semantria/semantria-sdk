#include "Session.h"


Session::Session() {
    init((string)"", (string)"", (void*)NULL, "");
}

Session::Session(string consumerKey, string consumerSecret, void* serializer, string applicationName) {
    init(consumerKey, consumerSecret, serializer, applicationName);
}

void Session::init(string consumerKey, string consumerSecret, void* serializer, string applicationName) {
    this->consumerKey = consumerKey;
    this->consumerSecret = consumerSecret;
    this->callbackHandler = NULL;
    this->serializer = NULL;
    SetApplicationName(applicationName);

    if (NULL != serializer) {
        RegisterSerializer(serializer);
    }

    curl_global_init(CURL_GLOBAL_ALL);
}

void Session::SetApplicationName(string applicationName) {
    if ("" != applicationName) {
        this->applicationName = applicationName + "." + wrapperName;
    } else {
        this->applicationName = wrapperName;
    }
}

Session::~Session() {
	if (NULL != serializer) {
	    delete serializer;
	}

	if (NULL != callbackHandler) {
        delete callbackHandler;
	}

	curl_global_cleanup();
}

Session* Session::CreateSession(string consumerKey, string consumerSecret) {
    return Session::CreateSession(consumerKey, consumerSecret, new XmlSerializer());
}

Session* Session::CreateSession(string consumerKey, string consumerSecret, string applicationName) {
    return Session::CreateSession(consumerKey, consumerSecret, new XmlSerializer(), applicationName);
}

Session* Session::CreateSession(string consumerKey, string consumerSecret, void* serializer) {
    return new Session(consumerKey, consumerSecret, serializer, "");
}

Session* Session::CreateSession(string consumerKey, string consumerSecret, void* serializer, string applicationName) {
    return new Session(consumerKey, consumerSecret, serializer, applicationName);
}

void Session::SetCallbackHandler(CallbackHandler* callbackHandler) {
    this->callbackHandler = callbackHandler;
}

void Session::RegisterSerializer(void* serializer) {
    this->serializer = serializer;
    // We can use casting to any Serializer derived class, "type" should be constant
    format = (static_cast<XmlSerializer*>(serializer))->GetType();
}

// End-point calls
ServiceStatus* Session::GetStatus() {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, "", applicationName);
    QueryMethod method = GET;
    string url = host + "/status." + format;
    cout << endl << "url: " << url << endl;
    int status = authRequest->authWebRequest(url, method);

    ServiceStatus* result = new ServiceStatus();
    if (status <= 202 && "" != authRequest->GetReceivedData()) {
        if (format == "xml") {
            (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), result);
        } else {
            (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), result);
        }
    }
    HandleRequest(method, url, "");
    HandleConfigurationResponse(status, authRequest);

    delete authRequest;

    return result;
}

Subscription* Session::VerifySubscription() {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, "", applicationName);
    QueryMethod method = GET;
    string url = host + "/subscription." + format;
    cout << endl << "url: " << url << endl;
    int status = authRequest->authWebRequest(url, method);

    Subscription* result = new Subscription();
    if (status <= 202 && "" != authRequest->GetReceivedData()) {
        if (format == "xml") {
            (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), result);
        } else {
            (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), result);
        }
    }
    HandleRequest(method, url, "");
    HandleConfigurationResponse(status, authRequest);

    delete authRequest;

    return result;
}

vector<string>* Session::GetBlacklist() {
    return GetBlacklist("");
}

vector<string>* Session::GetBlacklist(string configId) {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, configId, applicationName);
    QueryMethod method = GET;
    string url = host + "/blacklist." + format;
    cout << endl << "url: " << url << endl;
    int status = authRequest->authWebRequest(url, method);

    vector<string>* result = new vector<string>();
    if (status <= 202 && "" != authRequest->GetReceivedData()) {
        Stub_Blacklist* stub = new Stub_Blacklist();
        if (format == "xml") {
            (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        } else {
            (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        }
        result = stub->GetBlacklistItems();

        // @TODO don't forget that items are stored in stub, so if
        // we delete items in stub's destructor - all items from result
        // will be removed too.
        // maybe we should use smart pointer?
        delete stub;
    }
    HandleRequest(method, url, "");
    HandleConfigurationResponse(status, authRequest);

    delete authRequest;

    return result;
}

int Session::UpdateBlacklist(UpdateProxy<Blacklisted>* proxy) {
    return UpdateBlacklist(proxy, "");
}

int Session::UpdateBlacklist(UpdateProxy<Blacklisted>* proxy, string configId) {
    return UpdateItems("blacklist", proxy, configId);
}

template <class T>
int Session::UpdateItems(string path, UpdateProxy<T>* proxy, string configId) {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, configId, applicationName);
    QueryMethod method = POST;
    string url = host + "/" + path + "." + format;
    cout << endl << "url: " << url << endl;
    string body = "";
    if (format == "xml") {
        body = (static_cast<XmlSerializer*>(serializer))->Serialize(proxy);
    } else {
        body = (static_cast<JsonSerializer*>(serializer))->Serialize(proxy);
    }

    int result = authRequest->authWebRequest(url, method, body);

    HandleRequest(POST, url, body);
    HandleConfigurationResponse(result, authRequest);

    delete authRequest;
    return result;
}

vector<Category*>* Session::GetCategories() {
    return GetCategories("");
}

vector<Category*>* Session::GetCategories(string configId) {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, configId, applicationName);
    QueryMethod method = GET;
    string url = host + "/categories." + format;
    cout << endl << "url: " << url << endl;
    int status = authRequest->authWebRequest(url, method);

    vector<Category*>* result = new vector<Category*>();
    if (status <= 202 && "" != authRequest->GetReceivedData()) {
        Stub_Categories* stub = new Stub_Categories();
        if (format == "xml") {
            (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        } else {
            (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        }
        result = stub->GetCategories();
        delete stub;
    }
    HandleRequest(method, url, "");
    HandleConfigurationResponse(status, authRequest);

    delete authRequest;

    return result;
}

int Session::UpdateCategories(UpdateProxy<Category>* proxy) {
    return UpdateCategories(proxy, "");
}

int Session::UpdateCategories(UpdateProxy<Category>* proxy, string configId) {
    return UpdateItems("categories", proxy, configId);
}

vector<Configuration*>* Session::GetConfigurations() {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, "", applicationName);
    QueryMethod method = GET;
    string url = host + "/configurations." + format;
    cout << endl << "url: " << url << endl;
    int status = authRequest->authWebRequest(url, method);
    vector<Configuration*>* result = new vector<Configuration*>();
    if (status <= 202 && "" != authRequest->GetReceivedData()) {
        Stub_Configurations* stub = new Stub_Configurations();
        if (format == "xml") {
            (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        } else {
            (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        }
        result = stub->GetConfigurations();
        delete stub;
    }
    HandleRequest(method, url, "");
    HandleConfigurationResponse(status, authRequest);

    delete authRequest;

    return result;
}

int Session::UpdateConfigurations(UpdateProxy<Configuration>* proxy) {
    return UpdateConfigurations(proxy, "");
}

int Session::UpdateConfigurations(UpdateProxy<Configuration>* proxy, string configId) {
    return UpdateItems("configurations", proxy, configId);
}

vector<UserEntity*>* Session::GetEntities() {
    return GetEntities("");
}

vector<UserEntity*>* Session::GetEntities(string configId) {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, configId, applicationName);
    QueryMethod method = GET;
    string url = host + "/entities." + format;
    cout << endl << "url: " << url << endl;
    int status = authRequest->authWebRequest(url, method);
    vector<UserEntity*>* result = new vector<UserEntity*>();
    if (status <= 202 && "" != authRequest->GetReceivedData()) {
        Stub_Entities* stub = new Stub_Entities();
        if (format == "xml") {
            (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        } else {
            (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        }
        result = stub->GetEntities();
        delete stub;
    }
    HandleRequest(method, url, "");
    HandleConfigurationResponse(status, authRequest);

    delete authRequest;

    return result;
}

int Session::UpdateEntities(UpdateProxy<UserEntity>* proxy) {
    return UpdateEntities(proxy, "");
}

int Session::UpdateEntities(UpdateProxy<UserEntity>* proxy, string configId) {
    return UpdateItems("entities", proxy, configId);
}

vector<Query*>* Session::GetQueries() {
    return GetQueries("");
}

vector<Query*>* Session::GetQueries(string configId) {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, configId, applicationName);
    QueryMethod method = GET;
    string url = host + "/queries." + format;
    cout << endl << "url: " << url << endl;
    int status = authRequest->authWebRequest(url, method);
    vector<Query*>* result = new vector<Query*>();
    if (status <= 202 && "" != authRequest->GetReceivedData()) {
        Stub_Queries* stub = new Stub_Queries();
        if (format == "xml") {
            (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        } else {
            (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        }
        result = stub->GetQueries();
        delete stub;
    }
    HandleRequest(method, url, "");
    HandleConfigurationResponse(status, authRequest);

    delete authRequest;

    return result;
}

int Session::UpdateQueries(UpdateProxy<Query>* proxy) {
    return UpdateQueries(proxy, "");
}

int Session::UpdateQueries(UpdateProxy<Query>* proxy, string configId) {
    return UpdateItems("queries", proxy, configId);
}

vector<SentimentPhrase*>* Session::GetSentimentPhrases() {
    return GetSentimentPhrases("");
}

vector<SentimentPhrase*>* Session::GetSentimentPhrases(string configId) {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, configId, applicationName);
    QueryMethod method = GET;
    string url = host + "/sentiment." + format;
    cout << endl << "url: " << url << endl;
    int status = authRequest->authWebRequest(url, method);
    vector<SentimentPhrase*>* result = new vector<SentimentPhrase*>();
    if (status <= 202 && "" != authRequest->GetReceivedData()) {
        Stub_SentimentPhrase* stub = new Stub_SentimentPhrase();
        if (format == "xml") {
            (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        } else {
            (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        }
        result = stub->GetPhrases();
        delete stub;
    }
    HandleRequest(method, url, "");
    HandleConfigurationResponse(status, authRequest);

    delete authRequest;

    return result;
}

int Session::UpdateSentimentPhrases(UpdateProxy<SentimentPhrase>* proxy) {
    return UpdateSentimentPhrases(proxy, "");
}

int Session::UpdateSentimentPhrases(UpdateProxy<SentimentPhrase>* proxy, string configId) {
    return UpdateItems("sentiment", proxy, configId);
}

DocAnalyticData* Session::GetDocument(string documentId) {
    return GetDocument(documentId, "");
}

DocAnalyticData* Session::GetDocument(string documentId, string configId) {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, configId, applicationName);
    QueryMethod method = GET;
    string url = host + "/document/" + documentId + "." + format;
    cout << endl << "url: " << url << endl;
    int status = authRequest->authWebRequest(url, method);
    DocAnalyticData* result = new DocAnalyticData();
    if (status <= 202 && "" != authRequest->GetReceivedData()) {
        if (format == "xml") {
            (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), result);
        } else {
            (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), result);
        }
    }
    HandleRequest(method, url, "");
    HandleConfigurationResponse(status, authRequest);

    delete authRequest;

    return result;
}

CollAnalyticData* Session::GetCollection(string collectionId) {
    return GetCollection(collectionId, "");
}

CollAnalyticData* Session::GetCollection(string collectionId, string configId) {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, configId, applicationName);
    QueryMethod method = GET;
    string url = host + "/collection/" + collectionId + "." + format;
    cout << endl << "url: " << url << endl;
    int status = authRequest->authWebRequest(url, method);
    CollAnalyticData* result = new CollAnalyticData();
    if (status <= 202 && "" != authRequest->GetReceivedData()) {
        if (format == "xml") {
            (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), result);
        } else {
            (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), result);
        }
    }
    HandleRequest(method, url, "");
    HandleConfigurationResponse(status, authRequest);

    delete authRequest;

    return result;
}

vector<DocAnalyticData*>* Session::GetProcessedDocuments() {
    return GetProcessedDocuments("");
}

vector<DocAnalyticData*>* Session::GetProcessedDocuments(string configId) {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, configId, applicationName);
    QueryMethod method = GET;
    string url = host + "/document/processed." + format;
    cout << endl << "url: " << url << endl;
    int status = authRequest->authWebRequest(url, method);
    vector<DocAnalyticData*>* result = new vector<DocAnalyticData*>();
    if (status <= 202 && "" != authRequest->GetReceivedData()) {
        Stub_DocAnalyticDatas* stub = new Stub_DocAnalyticDatas();
        if (format == "xml") {
            (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        } else {
            (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        }
        result = stub->GetAnalyticData();
        delete stub;
    }
    HandleRequest(method, url, "");
    HandleConfigurationResponse(status, authRequest);

    delete authRequest;

    return result;
}

vector<CollAnalyticData*>* Session::GetProcessedCollections() {
    return GetProcessedCollections("");
}

vector<CollAnalyticData*>* Session::GetProcessedCollections(string configId) {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, configId, applicationName);
    QueryMethod method = GET;
    string url = host + "/collection/processed." + format;
    cout << endl << "url: " << url << endl;
    int status = authRequest->authWebRequest(url, method);
    vector<CollAnalyticData*>* result = new vector<CollAnalyticData*>();
    if (status <= 202 && "" != authRequest->GetReceivedData()) {
        Stub_CollAnalyticDatas* stub = new Stub_CollAnalyticDatas();
        if (format == "xml") {
            (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        } else {
            (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
        }
        result = stub->GetAnalyticData();
        delete stub;
    }
    HandleRequest(method, url, "");
    HandleConfigurationResponse(status, authRequest);

    delete authRequest;

    return result;
}

int Session::QueueDocument(Document* document) {
    return QueueDocument(document, "");
}

int Session::QueueDocument(Document* document, string configId) {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, configId, applicationName);
    QueryMethod method = POST;
    string url = host + "/document." + format;
    cout << endl << "url: " << url << endl;

    string body = "";
    if (format == "xml") {
        body = (static_cast<XmlSerializer*>(serializer))->Serialize(document);
    } else {
        body = (static_cast<JsonSerializer*>(serializer))->Serialize(document);
    }
    int result = authRequest->authWebRequest(url, method, body);

    HandleDocAutoResponse(result, authRequest);
    HandleRequest(method, url, body);

    delete authRequest;
    return result;
}

int Session::QueueBatch(vector<Document*>* documents) {
    return QueueBatch(documents, "");
}

int Session::QueueBatch(vector<Document*>* documents, string configId) {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, configId, applicationName);
    QueryMethod method = POST;
    string url = host + "/document/batch." + format;
    cout << endl << "url: " << url << endl;
    Stub_Tasks* documentsStub = new Stub_Tasks(documents);
    string body = "";
    if (format == "xml") {
        body = (static_cast<XmlSerializer*>(serializer))->Serialize(documentsStub);
    } else {
        body = (static_cast<JsonSerializer*>(serializer))->Serialize(documentsStub);
    }
    delete documentsStub;

    int result = authRequest->authWebRequest(url, method, body);

    HandleDocAutoResponse(result, authRequest);
    HandleRequest(method, url, body);

    delete authRequest;
    return result;
}

int Session::QueueCollection(Collection* collection) {
    return QueueCollection(collection, "");
}

int Session::QueueCollection(Collection* collection, string configId) {
    AuthRequest* authRequest = new AuthRequest(consumerKey, consumerSecret, configId, applicationName);
    QueryMethod method = POST;
    string url = host + "/collection." + format;
    cout << endl << "url: " << url << endl;
    string body = "";
    if (format == "xml") {
        body = (static_cast<XmlSerializer*>(serializer))->Serialize(collection);
    } else {
        body = (static_cast<JsonSerializer*>(serializer))->Serialize(collection);
    }
    int result = authRequest->authWebRequest(url, method, body);

    HandleCollAutoResponse(result, authRequest);
    HandleRequest(method, url, body);

    delete authRequest;

    return result;
}

void Session::HandleConfigurationResponse(int status, AuthRequest* authRequest) {
    if (NULL != callbackHandler) {
        if (status <= 202) {
            callbackHandler->OnResponse(this, new ResponseArgs(status, authRequest->GetReceivedData()));
        } else {
            callbackHandler->OnError(this, new ResponseArgs(status, authRequest->GetReceivedData()));
        }
    }
}

void Session::HandleDocAutoResponse(int status, AuthRequest* authRequest) {
    if (NULL != callbackHandler) {
        HandleConfigurationResponse(status, authRequest);

        if (status <= 202 && "" != authRequest->GetReceivedData()) {
            Stub_DocAnalyticDatas* stub = new Stub_DocAnalyticDatas();
            if (format == "xml") {
                (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
            } else {
                (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
            }
            callbackHandler->OnDocsAutoResponse(this, stub->GetAnalyticData());
            delete stub;
        }
    }
}

void Session::HandleCollAutoResponse(int status, AuthRequest* authRequest) {
    if (NULL != callbackHandler) {
        HandleConfigurationResponse(status, authRequest);

        if (status <= 202 && "" != authRequest->GetReceivedData()) {
            Stub_CollAnalyticDatas* stub = new Stub_CollAnalyticDatas();
            if (format == "xml") {
                (static_cast<XmlSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
            } else {
                (static_cast<JsonSerializer*>(serializer))->Deserialize(authRequest->GetReceivedData(), stub);
            }
            callbackHandler->OnCollsAutoResponse(this, stub->GetAnalyticData());
            delete stub;
        }
    }
}

void Session::HandleRequest(QueryMethod method, string url, string message) {
    if (NULL != callbackHandler) {
        callbackHandler->OnRequest(this, new RequestArgs(EnumsHelper::GetQueryMethodAsString(method), url, message));
    }
}

const string Session::host = "https://api21.semantria.com";
const string Session::wrapperName = "Cpp";
