#include "UnitTest++.h"

#include <iostream>
#include <sstream>

#include "../src/Session.h"
#include "../src/handlers/SimpleCallBackhandler.h"
#include "../src/serializers/json/JsonSerializer.h"
#include "../src/serializers/xml/XmlSerializer.h"
#include "../src/serializers/Serializer.h"
#include "../src/connection/HMAC_SHA1.h"
#include "../src/connection/base64.h"

namespace {
    string KEY = "";
    string SECRET = "";


    TEST(AuthWebRequestFunctionality) {
        cout << "AuthWebRequestFunctionality" << endl;
        AuthRequest* request = new AuthRequest(KEY, SECRET);
        request->authWebRequest("https://api21.semantria.com/status.xml", GET);
        CHECK("" != request->GetReceivedData());

        delete request;
    }

    TEST(HMACSHAFunctionality) {
        cout << "HMACSHAFunctionality" << endl;
        string key = "35082e00355b70980dd0fab0556e582c";
        vector<char> keyBytes(key.begin(), key.end());
        keyBytes.push_back('\0');
        const char *keySecretPointer = &keyBytes[0];

        BYTE digest[20];

        string myString = "https%3a%2f%2fapi21%2edev%2esemantria%2ecom%2fstatus%2exml%3foauth%5fconsumer%5fkey%3d88570356%2dafd2%2d49ac%2coauth%5fnonce%3d27044%2coauth%5fsignature%5fmethod%3dHMAC%2dSHA1%2coauth%5ftimestamp%3d1341338347%2coauth%5fversion%3d1%2e0";
        vector<char> bytes(myString.begin(), myString.end());
        bytes.push_back('\0');
        const char *c = &bytes[0];

        CHMAC_SHA1* sha = new CHMAC_SHA1();
        sha->HMAC_SHA1((BYTE*)c, strlen(c), (BYTE*)keySecretPointer, strlen(keySecretPointer), digest);

        CHECK_EQUAL("rENGqY/UO3FhB0zC0HWfqOM2Nvw=", base64_encode(digest, sizeof(digest)));
        delete sha;
    }

    TEST(GetStatus) {
        cout << "GetStatus" << endl;
        Session* session = Session::CreateSession(KEY, SECRET, new JsonSerializer());
        ServiceStatus* status = session->GetStatus();
        CHECK(NULL != status);

        CHECK_EQUAL("available", status->GetServiceStatus());
        CHECK_EQUAL("2.1", status->GetApiVersion());

        delete session;
        delete status;
    }

    TEST(VerifySubscription) {
        cout << "VerifySubscription" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        Subscription* subscription= session->VerifySubscription();
        CHECK(NULL != subscription);

        CHECK_EQUAL("active", subscription->GetStatus());

        delete session;
        if (NULL != subscription) {
            delete subscription;
        }
    }

    TEST(ReceivingBlacklist) {
        cout << "ReceivingBlacklist" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        vector<string>* blacklist = session->GetBlacklist();
        CHECK(NULL != blacklist);

        delete session;
        if (NULL != blacklist) {
            delete blacklist;
        }
    }

    TEST(ReceivingCategories) {
        cout << "ReceivingCategories" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        vector<Category*>* categories = session->GetCategories();
        CHECK(NULL != categories);

        delete session;
        if (NULL != categories) {
            delete categories;
        }
    }

    TEST(ReceivingConfigurations) {
        cout << "ReceivingConfigurations" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        vector<Configuration*>* configurations = session->GetConfigurations();
        CHECK(NULL != configurations);
        CHECK(0 != configurations->size());

        delete session;
        if (NULL != configurations) {
            delete configurations;
        }
    }

    TEST(ReceivingQueries) {
        cout << "ReceivingQueries" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        vector<Query*>* queries = session->GetQueries();
        CHECK(NULL != queries);

        delete session;
        if (NULL != queries) {
            delete queries;
        }
    }

    TEST(ReceivingEntities) {
        cout << "ReceivingEntities" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        vector<UserEntity*>* entities = session->GetEntities();
        CHECK(NULL != entities);

        delete session;
        if (NULL != entities) {
            delete entities;
        }
    }

    TEST(ReceivingSentimenPhrases) {
        cout << "ReceivingSentimenPhrases" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        vector<SentimentPhrase*>* phrases = session->GetSentimentPhrases();
        CHECK(NULL != phrases);

        delete session;
        if (NULL != phrases) {
            delete phrases;
        }
    }

    TEST(ReceivingDocument) {
        cout << "ReceivingDocument" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        DocAnalyticData* analyticData = session->GetDocument("DOC_ID");
        CHECK(NULL != analyticData);

        delete session;
        if (NULL != analyticData) {
            delete analyticData;
        }
    }

    TEST(ReceivingCollection) {
        cout << "ReceivingCollection" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        CollAnalyticData* analyticData = session->GetCollection("COLL_ID");
        CHECK(NULL != analyticData);

        delete session;
        if (NULL != analyticData) {
            delete analyticData;
        }
    }

    TEST(ReceivingProcessedDocuments) {
        cout << "ReceivingProcessedDocuments" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        vector<DocAnalyticData*>* analyticData = session->GetProcessedDocuments();
        CHECK(NULL != analyticData);

        delete session;
        if (NULL != analyticData) {
            delete analyticData;
        }
    }

    TEST(ReceivingProcessedCollections) {
        cout << "ReceivingProcessedCollections" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        vector<CollAnalyticData*>* analyticData = session->GetProcessedCollections();
        CHECK(NULL != analyticData);

        delete session;
        if (NULL != analyticData) {
            delete analyticData;
        }
    }

    TEST(UpdateBlacklist) {
        cout << "UpdateBlacklist" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);

        // Checking
        vector<string>* items = session->GetBlacklist();
        CHECK(NULL != items);
        int initialBlacklistAmount = items->size();
        delete items;

        BlacklistUpdateProxy* proxy = session->CreateBlacklistUpdateProxy();
        Blacklisted* item1 = new Blacklisted("new item #1");
        Blacklisted* item2 = new Blacklisted("aand new item #2");
        proxy->Add(item1);
        proxy->Add(item2);

        CHECK_EQUAL(202, session->UpdateBlacklist(proxy));
        delete proxy;

        // Checking
        items = session->GetBlacklist();
        CHECK(NULL != items);
        CHECK_EQUAL(initialBlacklistAmount + 2, items->size());
        delete items;

        //
        proxy = session->CreateBlacklistUpdateProxy();
        Blacklisted* item3 = new Blacklisted("new item #1");
        Blacklisted* item4 = new Blacklisted("aand new item #2");
        proxy->Remove(item3);
        proxy->Remove(item4);
        CHECK_EQUAL(202, session->UpdateBlacklist(proxy));
        delete proxy;

        // Checking
        items = session->GetBlacklist();
        CHECK(NULL != items);
        CHECK_EQUAL(initialBlacklistAmount, items->size());
        delete items;
        delete session;
    }

    TEST(UpdateCategories) {
        cout << "UpdateCategories" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);

        // Checking
        vector<Category*>* items = session->GetCategories();
        CHECK(NULL != items);
        int initialCategoriesAmount = items->size();
        delete items;

        CategoryUpdateProxy* proxy = session->CreateCategoryUpdateProxy();
        vector<string>* samples = new vector<string>();
        samples->push_back("sample 1");
        samples->push_back("sample 2");
        Category* addedItem = new Category("category 1", 1.45, samples);
        proxy->Add(addedItem);

        CHECK_EQUAL(202, session->UpdateCategories(proxy));
        delete proxy;


        // Checking
        items = session->GetCategories();
        CHECK(NULL != items);
        CHECK_EQUAL(initialCategoriesAmount + 1, items->size());
        delete items;

        //
        proxy = session->CreateCategoryUpdateProxy();
        Category* removedItem = new Category("category 1", 0.0);
        proxy->Remove(removedItem);
        CHECK_EQUAL(202, session->UpdateCategories(proxy));
        delete proxy;

        // Checking
        items = session->GetCategories();
        CHECK(NULL != items);
        CHECK_EQUAL(initialCategoriesAmount, items->size());
        delete items;

        delete session;
    }

    TEST(UpdateConfigurations) {
        cout << "UpdateConfigurations" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);

        // Checking
        vector<Configuration*>* items = session->GetConfigurations();
        CHECK(NULL != items);
        int initialConfigAmount = items->size();
        delete items;

        ConfigurationUpdateProxy* proxy = session->CreateConfigurationUpdateProxy();
        Configuration* addedItem = new Configuration();
        addedItem->SetName("config new");
        addedItem->SetAutoResponding(false);
        addedItem->SetOneSentence(true);
        addedItem->SetLanguage("English");
        addedItem->SetIsPrimary(false);
        proxy->Add(addedItem);

        CHECK_EQUAL(202, session->UpdateConfigurations(proxy));
        delete proxy;


        // Checking
        items = session->GetConfigurations();
        CHECK(NULL != items);
        CHECK_EQUAL(initialConfigAmount + 1, items->size());
        string createdConfigId = "";
        for(vector<Configuration*>::size_type i = 0; i != items->size(); i++) {
            if (items->at(i)->GetName() == "config new") {
                createdConfigId = items->at(i)->GetId();
            }
        }
        delete items;

        //
        proxy = session->CreateConfigurationUpdateProxy();
        Configuration* removedItem = new Configuration();
        removedItem->SetId(createdConfigId);
        proxy->Remove(removedItem);
        CHECK_EQUAL(202, session->UpdateConfigurations(proxy));
        delete proxy;

        // Checking
        items = session->GetConfigurations();
        CHECK(NULL != items);
        CHECK_EQUAL(initialConfigAmount, items->size());
        delete items;

        delete session;
    }

    TEST(UpdateQueries) {
        cout << "UpdateQueries" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        session->SetCallbackHandler(new SimpleCallBackHandler());

        // Checking

        vector<Query*>* items = session->GetQueries();
        CHECK(NULL != items);
        int initialQueriesCount = items->size();
        delete items;

        QueryUpdateProxy* proxy = session->CreateQueryUpdateProxy();
        Query* addedItem = new Query("new query #1", "query");
        proxy->Add(addedItem);

        session->UpdateQueries(proxy);
        //CHECK_EQUAL(202, session->UpdateQueries(proxy));
        delete proxy;


        // Checking
        items = session->GetQueries();
        CHECK(NULL != items);
        CHECK_EQUAL(initialQueriesCount + 1, items->size());
        delete items;

        //
        proxy = session->CreateQueryUpdateProxy();
        Query* removedItem = new Query("new query #1", "query body");
        proxy->Remove(removedItem);
        CHECK_EQUAL(202, session->UpdateQueries(proxy));
        delete proxy;

        // Checking
        items = session->GetQueries();
        CHECK(NULL != items);
        CHECK_EQUAL(initialQueriesCount, items->size());
        delete items;


        delete session;
    }

    TEST(UpdateEntities) {
        cout << "UpdateEntities" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);

        // Checking
        vector<UserEntity*>* items = session->GetEntities();
        CHECK(NULL != items);
        int initialEntitiesCount = items->size();
        delete items;

        EntityUpdateProxy* proxy = session->CreateEntityUpdateProxy();
        UserEntity* addedItem = new UserEntity("new entity #1", "entity type");
        proxy->Add(addedItem);

        CHECK_EQUAL(202, session->UpdateEntities(proxy));
        delete proxy;

        // Checking
        items = session->GetEntities();
        CHECK(NULL != items);
        CHECK_EQUAL(initialEntitiesCount + 1, items->size());
        delete items;

        //
        proxy = session->CreateEntityUpdateProxy();
        UserEntity* removedItem = new UserEntity("new entity #1", "entity type");
        proxy->Remove(removedItem);
        CHECK_EQUAL(202, session->UpdateEntities(proxy));
        delete proxy;

        // Checking
        items = session->GetEntities();
        CHECK(NULL != items);
        CHECK_EQUAL(initialEntitiesCount, items->size());
        delete items;

        delete session;
    }

    TEST(UpdateSentimentPhrases) {
        cout << "UpdateSentimentPhrases" << "\n\n";
        Session* session = Session::CreateSession(KEY, SECRET);

        // Checking
        vector<SentimentPhrase*>* items = session->GetSentimentPhrases();
        CHECK(NULL != items);
        int initialPhrasesCount = items->size();
        delete items;

        SentimentPhraseUpdateProxy* proxy = session->CreateSentimentPhraseUpdateProxy();
        SentimentPhrase* addedItem = new SentimentPhrase("new phrase #1", 0.5);
        proxy->Add(addedItem);

        CHECK_EQUAL(202, session->UpdateSentimentPhrases(proxy));
        delete proxy;

        // Checking
        items = session->GetSentimentPhrases();
        CHECK(NULL != items);
        CHECK_EQUAL(initialPhrasesCount + 1, items->size());
        delete items;

        //
        proxy = session->CreateSentimentPhraseUpdateProxy();
        SentimentPhrase* removedItem = new SentimentPhrase("new phrase #1", 0.5);
        proxy->Remove(removedItem);
        CHECK_EQUAL(202, session->UpdateSentimentPhrases(proxy));
        delete proxy;

        // Checking
        items = session->GetSentimentPhrases();
        CHECK(NULL != items);
        CHECK_EQUAL(initialPhrasesCount, items->size());
        delete items;

        delete session;
    }

}
