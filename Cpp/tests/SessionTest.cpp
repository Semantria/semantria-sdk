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
        request->authWebRequest("https://api35.semantria.com/status.xml", GET);
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
        session->SetCallbackHandler(new SimpleCallBackHandler());

        ServiceStatus* status = session->GetStatus();
        CHECK(NULL != status);

        CHECK_EQUAL("available", status->GetServiceStatus());
        CHECK_EQUAL("3.5", status->GetApiVersion());

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
        //session->SetCallbackHandler(new SimpleCallBackHandler);

        // Initial texts for processing
        vector<string>* initialText = new vector<string>();
        initialText->push_back("Lisa - there's 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh's). I couldn't find them instore and i'm not going to walmart before the 19th. Oh well sounds like i'm not missing much ...lol");
        initialText->push_back("In Lake Louise - a ₤ guided walk for the family with Great Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The \"must-do\" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.");
        initialText->push_back("On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time.");

        Collection* collection = new Collection();
        collection->SetDocuments(initialText);
        collection->SetId("qpeoiwqoiepqowiepq");
        session->QueueCollection(collection);


        vector<CollAnalyticData*>* analyticData = session->GetProcessedCollections();
        if( analyticData->size() ){
            CollAnalyticData* coll = analyticData->at(0);
            coll = 0;
        }

        CHECK(NULL != analyticData);

        delete session;
        if (NULL != analyticData) {
            delete analyticData;
        }
    }


    TEST(ReceivingStatistics) {
        cout << "ReceivingStatistics" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        session->SetUseCompression( true );
        //session->SetCallbackHandler(new SimpleCallBackHandler());

        Statistics* statistics = session->GetStatistics();
        CHECK(NULL != statistics);

        delete session;
        if (NULL != statistics) {
            delete statistics;
        }
    }

    TEST(UpdateBlacklist) {
        cout << "UpdateBlacklist" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);
        //session->SetCallbackHandler(new SimpleCallBackHandler());

        // Checking
        vector<string>* items = session->GetBlacklist();
        CHECK(NULL != items);
        size_t initialBlacklistAmount = items->size();
        delete items;

        vector<Blacklisted*>* list = new vector<Blacklisted*>;
        Blacklisted* item1 = new Blacklisted("new item #1");
        Blacklisted* item2 = new Blacklisted("aand new item #2");
        list->push_back(item1);
        list->push_back(item2);

        CHECK_EQUAL(202, session->AddBlacklist(list));
        delete list;

        // Checking
        items = session->GetBlacklist();
        CHECK(NULL != items);
        CHECK_EQUAL(initialBlacklistAmount + 2, items->size());
        delete items;

        //
        vector<string>* removeList = new vector<string>;
        removeList->push_back("new item #1");
        removeList->push_back("aand new item #2");
        CHECK_EQUAL(202, session->RemoveBlacklist(removeList));
        delete removeList;

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
        //session->SetCallbackHandler(new SimpleCallBackHandler());

        // Checking
        vector<Category*>* items = session->GetCategories();
        CHECK(NULL != items);
        size_t initialCategoriesAmount = items->size();
        delete items;

        vector<Category*>* list = new vector<Category*>;
        vector<string>* samples = new vector<string>();
        samples->push_back("sample0");
        samples->push_back("sample1");
        Category* addedItem = new Category("Category011", 1.45, samples);
        list->push_back(addedItem);

        CHECK_EQUAL(202, session->AddCategories(list));
        delete list;

        // Checking
        items = session->GetCategories();
        CHECK(NULL != items);
        CHECK_EQUAL(initialCategoriesAmount + 1, items->size());
        delete items;

        //
        vector<string>* removeList = new vector<string>;
        removeList->push_back("Category011");
        CHECK_EQUAL(202, session->RemoveCategories(removeList));
        delete removeList;

        // Checking
        items = session->GetCategories();
        CHECK(NULL != items);
        CHECK_EQUAL(initialCategoriesAmount, items->size());
        delete items;

        delete session;
    }

    TEST(UpdateConfigurations) {
        cout << "GetConfigurations" << endl;
        Session* session = Session::CreateSession(KEY, SECRET);

        // Checking
        vector<Configuration*>* items = session->GetConfigurations();
        CHECK(NULL != items);
        size_t initialConfigAmount = items->size();
        for(vector<Configuration*>::size_type i = 0; i != items->size(); i++) {
            cout << items->at(i)->GetName() << endl;
        }
        delete items;

        cout << "AddConfigurations" << endl;

        Configuration* addedItem1 = new Configuration();
        addedItem1->SetName("config1");
        addedItem1->SetAutoResponding(false);
        addedItem1->SetOneSentence(true);
        addedItem1->SetLanguage("English");
        addedItem1->SetIsPrimary(false);
        vector<Configuration *> * added = new vector<Configuration*>();
        added->push_back(addedItem1);
        CHECK_EQUAL(202, session->AddConfigurations( added ));
        delete added;
        delete addedItem1;

        // Checking
        items = session->GetConfigurations();
        CHECK(NULL != items);
        CHECK_EQUAL(initialConfigAmount + 1, items->size());
        string createdConfigId = "";
        for(vector<Configuration*>::size_type i = 0; i != items->size(); i++) {
            if (items->at(i)->GetName() == "config1") {
                createdConfigId = items->at(i)->GetId();
            }
        }
        delete items;

        cout << "UpdateConfigurations" << endl;

        Configuration* updatedItem = new Configuration();
        vector<Configuration *> * updated = new vector<Configuration*>();
        updatedItem->SetId(createdConfigId);
        updatedItem->SetName("qqq");
        updatedItem->SetAutoResponding(false);
        updatedItem->SetOneSentence(true);
        updatedItem->SetLanguage("English");
        updatedItem->SetIsPrimary(false);
        updated->push_back(updatedItem);

        CHECK_EQUAL(202, session->UpdateConfigurations(updated));
        delete updated;
        delete updatedItem;

        // Checking
        items = session->GetConfigurations();
        CHECK(NULL != items);
        CHECK_EQUAL(initialConfigAmount + 1, items->size());
        for(vector<Configuration*>::size_type i = 0; i != items->size(); i++) {
            cout << items->at(i)->GetName() << endl;
        }
        delete items;

        cout << "DeleteConfigurations" << endl;

        vector<string> * remove = new vector<string>();
        remove->push_back(createdConfigId);
        CHECK_EQUAL(202, session->RemoveConfigurations(remove));

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
        //session->SetCallbackHandler(new SimpleCallBackHandler());

        // Checking

        vector<Query*>* items = session->GetQueries();
        CHECK(NULL != items);
        size_t initialQueriesCount = items->size();
        for(vector<Query*>::size_type i = 0; i != items->size(); i++) {
            cout << items->at(i)->GetName() << endl;
        }
        delete items;


        vector<Query*>* list = new vector<Query*>;
        Query* addedItem = new Query("new query #1", "query123");
        list->push_back(addedItem);

        //session->UpdateQueries(list);
        CHECK_EQUAL(202, session->UpdateQueries(list));
        delete list;


        // Checking
        items = session->GetQueries();
        CHECK(NULL != items);
        CHECK_EQUAL(initialQueriesCount + 1, items->size());
        delete items;

        //
        cout << "DeleteQueries" << endl;

        vector<string> * remove = new vector<string>();
        remove->push_back("new query #1");
        CHECK_EQUAL(202, session->RemoveQueries(remove));

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
        size_t initialEntitiesCount = items->size();
        delete items;

        vector<UserEntity*>* list = new vector<UserEntity*>;
        UserEntity* addedItem = new UserEntity("new entity #1", "entity type");
        list->push_back(addedItem);

        CHECK_EQUAL(202, session->UpdateEntities(list));
        delete list;

        // Checking
        items = session->GetEntities();
        CHECK(NULL != items);
        CHECK_EQUAL(initialEntitiesCount + 1, items->size());
        delete items;

        //
        vector<string>* removeList = new vector<string>;
        removeList->push_back("new entity #1");
        CHECK_EQUAL(202, session->RemoveEntities(removeList));
        delete removeList;

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
        size_t initialPhrasesCount = items->size();
        delete items;

        vector<SentimentPhrase*>* list = new vector<SentimentPhrase*>;
        SentimentPhrase* addedItem = new SentimentPhrase("new phrase #1", 0.5);
        list->push_back(addedItem);
        CHECK_EQUAL(202, session->AddSentimentPhrases(list));
        delete list;

        // Checking
        items = session->GetSentimentPhrases();
        CHECK(NULL != items);
        CHECK_EQUAL(initialPhrasesCount + 1, items->size());
        delete items;

        //
        vector<string>* RemoveList = new vector<string>;
        RemoveList->push_back("new phrase #1");
        CHECK_EQUAL(202, session->RemoveSentimentPhrases(RemoveList));
        delete RemoveList;

        // Checking
        items = session->GetSentimentPhrases();
        CHECK(NULL != items);
        CHECK_EQUAL(initialPhrasesCount, items->size());
        delete items;

        delete session;
    }

}
