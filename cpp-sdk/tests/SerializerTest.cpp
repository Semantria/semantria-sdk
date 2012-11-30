#include "UnitTest++.h"

#include <libxml/xmlmemory.h>
#include <libxml/parser.h>

#include "../src/serializers/json/JsonSerializer.h"
#include "../src/serializers/xml/XmlSerializer.h"
#include "../src/objects/output/ServiceStatus.h"
#include "../src/objects/output/Subscription.h"
#include "../src/objects/stub/Stub_SentimentPhrase.h"
#include "../src/objects/stub/Stub_Blacklist.h"
#include "../src/objects/stub/Stub_Queries.h"
#include "../src/objects/stub/Stub_Categories.h"
#include "../src/objects/stub/Stub_CollAnalyticDatas.h"
#include "../src/objects/stub/Stub_Configurations.h"
#include "../src/objects/stub/Stub_DocAnalyticDatas.h"
#include "../src/objects/stub/Stub_Entities.h"
#include "../src/objects/stub/Stub_Tasks.h"

#include "../src/proxies/SentimentPhraseUpdateProxy.h"
#include "../src/proxies/BlacklistUpdateProxy.h"
#include "../src/proxies/CategoryUpdateProxy.h"
#include "../src/proxies/ConfigurationUpdateProxy.h"
#include "../src/proxies/EntityUpdateProxy.h"
#include "../src/proxies/QueryUpdateProxy.h"
#include "../src/common/Enums.h"
#include "../src/Session.h"


namespace {

    string eraseLastCharacter(string source) {
        return source.erase(source.length() - 1, 1);
    }

    TEST(JsonSerializingSentimentPhrases) {
        cout << "JsonSerializingSentimentPhrases" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "{\"added\":[{\"title\":\"Feature: Cloud service\",\"weight\":0.0}],\"removed\":[\"Features\"]}";

        SentimentPhraseUpdateProxy* proxy = Session::CreateSession("", "")->CreateSentimentPhraseUpdateProxy();
        SentimentPhrase* addedPhrase = new SentimentPhrase("Feature: Cloud service", 0.0d);
        proxy->Add(addedPhrase);

        SentimentPhrase* removedPhrase = new SentimentPhrase("Features", 0.0d);
        proxy->Remove(removedPhrase);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(proxy)));

        delete serializer;
        delete addedPhrase;
        delete removedPhrase;
    }

    TEST(JsonSerializingConfigurations) {
        cout << "JsonSerializingConfigurations" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "{\"added\":[{\"auto_responding\":true,\"callback\":\"https://anyapi.anydomain.com/processed/docs.json\",\"chars_threshold\":80,\"collection\":{\"concept_topics_limit\":5,\"facet_atts_limit\":5,\"facets_limit\":15,\"named_entities_limit\":5,\"query_topics_limit\":5,\"themes_limit\":0},\"document\":{\"concept_topics_limit\":5,\"entity_themes_limit\":5,\"named_entities_limit\":5,\"phrases_limit\":0,\"query_topics_limit\":5,\"summary_limit\":0,\"themes_limit\":0,\"user_entities_limit\":5},\"is_primary\":true,\"language\":\"English\",\"name\":\"A test configuration\",\"one_sentence\":true}],\"removed\":[\"45699836\"]}";

        ConfigurationUpdateProxy* proxy = Session::CreateSession("", "")->CreateConfigurationUpdateProxy();

        Configuration* addedConfiguration = new Configuration();
        addedConfiguration->SetName("A test configuration");
        addedConfiguration->SetIsPrimary(true);
        addedConfiguration->SetAutoResponding(true);
        addedConfiguration->SetLanguage("English");
        addedConfiguration->SetCharsThreshold(80);
        addedConfiguration->SetCallback("https://anyapi.anydomain.com/processed/docs.json");

        addedConfiguration->SetDocConceptTopicsLimit(5);
        addedConfiguration->SetDocQueryTopicsLimit(5);
        addedConfiguration->SetDocNamedEntitiesLimit(5);
        addedConfiguration->SetUserEntitiesLimit(5);
        addedConfiguration->SetDocThemesLimit(0);
        addedConfiguration->SetEntityThemesLimit(5);
        addedConfiguration->SetPhrasesLimit(0);
        addedConfiguration->SetSummaryLimit(0);

        addedConfiguration->SetFacetsLimit(15);
        addedConfiguration->SetFacetAttributesLimit(5);
        addedConfiguration->SetCollConceptTopicsLimit(5);
        addedConfiguration->SetCollQueryTopicsLimit(5);
        addedConfiguration->SetCollNamedEntitiesLimit(5);
        addedConfiguration->SetCollThemesLimit(0);

        proxy->Add(addedConfiguration);

        Configuration* removedConfiguration = new Configuration();
        removedConfiguration->SetId("45699836");
        proxy->Remove(removedConfiguration);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(proxy)));

        delete serializer;
        delete addedConfiguration;
        delete removedConfiguration;
    }

    TEST(JsonSerializingBlacklisted) {
        cout << "JsonSerializingBlacklisted" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "{\"added\":[\".*@.*com\",\".*@com\\\\.net\"],\"removed\":[\"http://www\\\\..*\\\\.com\"]}";

        BlacklistUpdateProxy* proxy = Session::CreateSession("", "")->CreateBlacklistUpdateProxy();
        Blacklisted* addedItem_1 = new Blacklisted(".*@.*com");
        proxy->Add(addedItem_1);
        Blacklisted* addedItem_2 = new Blacklisted(".*@com\\.net");
        proxy->Add(addedItem_2);

        Blacklisted* removedItem = new Blacklisted("http://www\\..*\\.com");
        proxy->Remove(removedItem);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(proxy)));

        delete serializer;
        delete addedItem_1;
        delete addedItem_2;
        delete removedItem;
    }

    TEST(JsonSerializingQuery) {
        cout << "JsonSerializingQuery" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "{\"added\":[{\"name\":\"Feature: Cloud service\",\"query\":\"Amazon AND EC2 AND Cloud\"}],\"removed\":[\"Features\"]}";

        QueryUpdateProxy* proxy = Session::CreateSession("", "")->CreateQueryUpdateProxy();
        Query* addedQuery = new Query("Feature: Cloud service", "Amazon AND EC2 AND Cloud");
        proxy->Add(addedQuery);

        Query* removedQuery = new Query("Features", "");
        proxy->Remove(removedQuery);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(proxy)));

        delete serializer;
        delete addedQuery;
        delete removedQuery;
    }

    TEST(JsonSerializingEntity) {
        cout << "JsonSerializingEntity" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "{\"added\":[{\"name\":\"chair\",\"type\":\"furniture\"}],\"removed\":[\"table\"]}";

        EntityUpdateProxy* proxy = Session::CreateSession("", "")->CreateEntityUpdateProxy();
        UserEntity* addedEntity = new UserEntity("chair", "furniture");
        proxy->Add(addedEntity);

        UserEntity* removedEntity = new UserEntity("table", "");
        proxy->Remove(removedEntity);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(proxy)));

        delete serializer;
        delete addedEntity;
        delete removedEntity;
    }

    TEST(JsonSerializingCategory) {
        cout << "JsonSerializingCategory" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "{\"added\":[{\"name\":\"chair\",\"samples\":[\"sample 1\",\"sample 2\"],\"weight\":0.40}],\"removed\":[\"table\"]}";

        CategoryUpdateProxy* proxy = Session::CreateSession("", "")->CreateCategoryUpdateProxy();
        vector<string>* samples = new vector<string>();
        samples->push_back("sample 1");
        samples->push_back("sample 2");
        Category* addedCategory = new Category("chair", 0.4d, samples);
        proxy->Add(addedCategory);

        Category* removedCategory = new Category("table", 0.0);
        proxy->Remove(removedCategory);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(proxy)));

        delete serializer;
        delete addedCategory;
        delete removedCategory;
    }

    TEST(JsonSerializingDocument) {
        cout << "JsonSerializingDocument" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "{\"id\":\"TASK_ID\",\"text\":\"some text\"}";

        Document* document = new Document();
        document->SetId("TASK_ID");
        document->SetText("some text");

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(document)));

        delete serializer;
        delete document;
    }

    TEST(JsonSerializingBatch) {
        cout << "JsonSerializingBatch" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "[{\"id\":\"TASK_ID\",\"text\":\"some text\"},{\"id\":\"No_ID\",\"text\":\"other text\"}]";

        Document* document1 = new Document();
        document1->SetId("TASK_ID");
        document1->SetText("some text");
        Document* document2 = new Document();
        document2->SetId("No_ID");
        document2->SetText("other text");
        vector<Document*>* documents = new vector<Document*>();
        documents->push_back(document1);
        documents->push_back(document2);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(new Stub_Tasks(documents))));

        delete serializer;
        delete documents;
    }

    TEST(JsonSerializingCollection) {
        cout << "JsonSerializingCollection" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "{\"documents\":[\"other text\",\"some text\"],\"id\":\"COLL_ID\"}";

        Collection* collection = new Collection();
        vector<string>* documents = new vector<string>();
        documents->push_back("other text");
        documents->push_back("some text");
        collection->SetId("COLL_ID");
        collection->SetDocuments(documents);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(collection)));

        delete serializer;
        delete collection;
    }

    TEST(JsonDeserializingServiceStatus) {
        cout << "JsonDeserializingServiceStatus" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string source = "{\
                            \"service_status\":\"online\",\
                            \"api_version\":\"2.0\",\
                            \"service_version\":\"1.0.2.63\",\
                            \"supported_encoding\":\"UTF-8\",\
                            \"supported_compression\":\"gzip\",\
                            \"supported_languages\":[\
                                    \"English\",\
                                    \"French\"\
                            ]\
                        }";

        CHECK_EQUAL(serializer->GetType(), "json");
        ServiceStatus* status = new ServiceStatus();
        serializer->Deserialize(source, status);

        CHECK(status != NULL);
        CHECK_EQUAL("online", status->GetServiceStatus());
        CHECK_EQUAL("2.0", status->GetApiVersion());
        CHECK_EQUAL("1.0.2.63", status->GetServiceVersion());
        CHECK_EQUAL("UTF-8", status->GetSupportedEncoding());
        CHECK_EQUAL("gzip", status->GetSupportedCompression());
        CHECK_EQUAL(2, status->GetSupportedLanguages()->size());
        CHECK(status->GetSupportedLanguages()->at(0) != status->GetSupportedLanguages()->at(1));
        CHECK("English" == status->GetSupportedLanguages()->at(0) || "English" == status->GetSupportedLanguages()->at(1));
        CHECK("French" == status->GetSupportedLanguages()->at(0) || "French" == status->GetSupportedLanguages()->at(1));

        delete serializer;
        delete status;
    }

    TEST(JsonDeserializingSubscription) {
        cout << "JsonDeserializingSubscription" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string source = "{\
                            \"name\" : \"Subscriber\",\
                            \"status\" : \"active\",\
                            \"priority\" : \"normal\",\
                            \"expiration_date\" : 1293883200,\
                            \"calls_balance\" : 87,\
                            \"calls_limit\" : 100,\
                            \"calls_limit_interval\" : 60,\
                            \"docs_balance\" : 49897,\
                            \"docs_limit\" : 0,\
                            \"docs_limit_interval\" : 0,\
                            \"configurations_limit\" : 10,\
                            \"blacklist_limit\" : 100,\
                            \"categories_limit\" : 100,\
                            \"queries_limit\" : 100,\
                            \"entities_limit\" : 1000,\
                            \"sentiment_limit\" : 1000,\
                            \"characters_limit\" : 8192,\
                            \"batch_limit\" : 10,\
                            \"collection_limit\" : 10,\
                            \"auto_response_limit\" : 2,\
                            \"processed_batch_limit\" : 100,\
                            \"callback_batch_limit\" : 100,\
                            \"limit_type\" : \"throughput\"\
                        }";
        Subscription* subscription = new Subscription();
        serializer->Deserialize(source, subscription);

        CHECK(subscription != NULL);
        CHECK_EQUAL("Subscriber", subscription->GetName());
        CHECK_EQUAL("active", subscription->GetStatus());
        CHECK_EQUAL("normal", subscription->GetPriority());
        CHECK_EQUAL(1293883200l, subscription->GetExpirationDate());
        CHECK_EQUAL(87, subscription->GetCallsBalance());
        CHECK_EQUAL(100, subscription->GetCallsLimit());
        CHECK_EQUAL(60, subscription->GetCallsLimitInterval());
        CHECK_EQUAL(49897, subscription->GetDocsBalance());
        CHECK_EQUAL(0, subscription->GetDocsLimit());
        CHECK_EQUAL(0, subscription->GetDocsLimitInterval());
        CHECK_EQUAL(10, subscription->GetConfigurationsLimit());
        CHECK_EQUAL(100, subscription->GetBlacklistLimit());
        CHECK_EQUAL(100, subscription->GetCategoriesLimit());
        CHECK_EQUAL(100, subscription->GetQueriesLimit());
        CHECK_EQUAL(1000, subscription->GetEntitiesLimit());
        CHECK_EQUAL(1000, subscription->GetSentimentLimit());
        CHECK_EQUAL(8192, subscription->GetCharactersLimit());
        CHECK_EQUAL(10, subscription->GetBatchLimit());
        CHECK_EQUAL(10, subscription->GetCollectionLimit());
        CHECK_EQUAL(2, subscription->GetAutoResponseLimit());
        CHECK_EQUAL(100, subscription->GetProcessedBatchLimit());
        CHECK_EQUAL(100, subscription->GetCallbackBatchLimit());
        CHECK_EQUAL("throughput", subscription->GetLimitType());

        delete serializer;
        delete subscription;
    }

    TEST(JsonDeserializingSentimentPhrases) {
        cout << "JsonDeserializingSentimentPhrases" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string source = "[{\"title\":\"chair\",\"weight\":0.75}]";;

        Json::Value root;
        Json::Reader reader;
        if (!reader.parse( source, root )){
            CHECK(false);
            return;
        }

        Stub_SentimentPhrase* stub = new Stub_SentimentPhrase();
        serializer->Deserialize(source, stub);
        vector<SentimentPhrase*>* phrases = stub->GetPhrases();

        CHECK_EQUAL(1, phrases->size());
        CHECK_EQUAL("chair", phrases->at(0)->GetTitle());
        CHECK_EQUAL(0.75d, phrases->at(0)->GetWeight());

        delete serializer;
        delete stub;
    }

    TEST(JsonDeserializingCategories) {
        cout << "JsonDeserializingCategories" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string source = "[{\"name\":\"Feature: Cloud service\",\"weight\":0.75,\"samples\":[\"Amazon\",\"EC2\"]}]";

        Json::Value root;
        Json::Reader reader;
        if (!reader.parse( source, root )){
            CHECK(false);
            return;
        }

        Stub_Categories* stub = new Stub_Categories();
        serializer->Deserialize(source, stub);
        vector<Category*>* categories = stub->GetCategories();

        CHECK_EQUAL(1, categories->size());
        CHECK_EQUAL("Feature: Cloud service", categories->at(0)->GetName());
        CHECK_EQUAL(0.75d, categories->at(0)->GetWeight());
        CHECK_EQUAL(2, categories->at(0)->GetSamples()->size());
        CHECK_EQUAL("Amazon", categories->at(0)->GetSamples()->at(0));

        delete serializer;
        delete stub;
    }

    TEST(JsonDeserializingBlacklist) {
        cout << "JsonDeserializingBlacklist" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string source = "[\".*@.*com\",\".*@com\\\\.net\"]";

        Json::Value root;
        Json::Reader reader;
        if (!reader.parse( source, root )){
            CHECK(false);
            return;
        }

        Stub_Blacklist* stub = new Stub_Blacklist();
        serializer->Deserialize(source, stub);

        vector<string>* items = stub->GetBlacklistItems();

        CHECK_EQUAL(2, items->size());
        CHECK_EQUAL(".*@.*com", items->at(0));
        CHECK_EQUAL(".*@com\\.net", items->at(1));

        delete serializer;
        delete stub;
    }

    TEST(JsonDeserializingQueries) {
        cout << "JsonDeserializingQueries" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string source = "[{\"name\":\"Feature: Cloud service\",\"query\":\"Amazon AND EC2 AND Cloud\"}]";

        Json::Value root;
        Json::Reader reader;
        if (!reader.parse( source, root )){
            CHECK(false);
            return;
        }

        Stub_Queries* stub = new Stub_Queries();
        serializer->Deserialize(source, stub);

        vector<Query*>* queries = stub->GetQueries();

        CHECK_EQUAL(1, queries->size());
        CHECK_EQUAL("Feature: Cloud service", queries->at(0)->GetName());
        CHECK_EQUAL("Amazon AND EC2 AND Cloud", queries->at(0)->GetQuery());

        delete serializer;
        delete stub;
    }

    TEST(JsonDeserializingEntities) {
        cout << "JsonDeserializingEntities" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string source = "[{\"name\":\"chair\",\"type\":\"furniture\"}]";

        Json::Value root;
        Json::Reader reader;
        if (!reader.parse( source, root )){
            CHECK(false);
            return;
        }

        Stub_Entities* stub = new Stub_Entities();
        serializer->Deserialize(source, stub);

        vector<UserEntity*>* entities = stub->GetEntities();

        CHECK_EQUAL(1, entities->size());
        CHECK_EQUAL("chair", entities->at(0)->GetName());
        CHECK_EQUAL("furniture", entities->at(0)->GetType());

        delete serializer;
        delete stub;
    }

    TEST(JsonDeserializingDocumentAnalyticData) {
        cout << "JsonDeserializingDocumentAnalyticData" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string source = "[{\
                \"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\",\
                \"config_id\":\"23498367\",\
                \"status\":\"PROCESSED\",\
                \"sentiment_score\":0.8295653,\
                \"summary\":\"Summary of the document’s text.\",\
                \"themes\":[\
                {\
                        \"evidence\":1,\
                        \"is_about\":true,\
                        \"strength_score\":0.0,\
                        \"sentiment_score\":0.0,\
                        \"title\":\"republican moderates\"\
                    }\
                ],\
                \"entities\":[\
                    {\
                    \"type\":\"named\",\
                    \"evidence\":0,\
                    \"is_about\":true,\
                    \"confident\":true,\
                    \"entity_type\":\"Place\",\
                    \"title\":\"WASHINGTON\",\
                    \"sentiment_score\":1.0542796,\
                    \"themes\":[\
                        {\
                            \"evidence\":1,\
                            \"is_about\":true,\
                            \"strength_score\":0.0,\
                            \"sentiment_score\":0.0,\
                            \"title\":\"republican moderates\"\
                        }\
                    ]\
                    }\
                ],\
                \"topics\":[\
                    {\
                        \"title\":\"Something\",\
                        \"type\":\"concept\",\
                        \"hitcount\":0,\
                        \"strength_score\":0.0,\
                        \"sentiment_score\":0.6133076\
                    }\
                ],\
                \"phrases\":[\
                    {\
                        \"title\":\"Something\",\
                        \"is_negated\":true,\
                        \"negating_phrase\":\"not\",\
                        \"sentiment_score\":0.6133076\
                    }\
                ]\
                }]";

        Json::Value root;
        Json::Reader reader;
        if (!reader.parse( source, root )){
            CHECK(false);
            return;
        }

        Stub_DocAnalyticDatas* stub = new Stub_DocAnalyticDatas();
        serializer->Deserialize(source, stub);

        vector<DocAnalyticData*>* analyticDataList = stub->GetAnalyticData();

        CHECK_EQUAL(1, analyticDataList->size());
        DocAnalyticData* analyticData = analyticDataList->at(0);

        CHECK_EQUAL("23498367", analyticData->GetConfigId());
        CHECK_EQUAL("6F9619FF8B86D011B42D00CF4FC964FF", analyticData->GetId());
        CHECK_EQUAL(PROCESSED, analyticData->GetStatus());
        CHECK_EQUAL(0.8295653d, analyticData->GetSentimentScore());
        CHECK_EQUAL("Summary of the document’s text.", analyticData->GetSummary());
        CHECK_EQUAL(1, analyticData->GetThemes()->size());
        CHECK_EQUAL(1, analyticData->GetEntities()->size());
        CHECK_EQUAL(1, analyticData->GetTopics()->size());

        //Check Document Themes
        DocTheme* themeObj = analyticData->GetThemes()->at(0);
        CHECK_EQUAL(1, themeObj->GetEvidence());
        CHECK_EQUAL(0.0d, themeObj->GetSentimentScore());
        CHECK_EQUAL(0.0d, themeObj->GetStrengthScore());
        CHECK_EQUAL("republican moderates", themeObj->GetTitle());
        CHECK(themeObj->GetIsAbout());

        //Check Document Entities
        DocEntity* entityObj = analyticData->GetEntities()->at(0);
        CHECK_EQUAL(0, entityObj->GetEvidence());
        CHECK(entityObj->GetIsAbout());
        CHECK(entityObj->GetConfident());
        CHECK_EQUAL("WASHINGTON", entityObj->GetTitle());
        CHECK_EQUAL(1.0542796d, entityObj->GetSentimentScore());
        CHECK_EQUAL("Place", entityObj->GetEntityType());
        CHECK_EQUAL("named", entityObj->GetType());

        //Check Entity Themes
        CHECK_EQUAL(1, entityObj->GetThemes()->size());
        DocTheme* entityThemeObj = entityObj->GetThemes()->at(0);
        CHECK_EQUAL(1, entityThemeObj->GetEvidence());
        CHECK_EQUAL(0.0d, entityThemeObj->GetSentimentScore());
        CHECK_EQUAL(0.0d, entityThemeObj->GetStrengthScore());
        CHECK_EQUAL("republican moderates", entityThemeObj->GetTitle());
        CHECK(entityThemeObj->GetIsAbout());

        //Check Document Topics
        CHECK_EQUAL(1, analyticData->GetTopics()->size());
        DocTopic* topicObj = analyticData->GetTopics()->at(0);
        CHECK_EQUAL(0, topicObj->GetHitCount());
        CHECK_EQUAL(0.6133076d, topicObj->GetSentimentScore());
        CHECK_EQUAL(0.0d, topicObj->GetStrengthScore());
        CHECK_EQUAL("Something", topicObj->GetTitle());
        CHECK_EQUAL("concept", topicObj->GetType());

        //Check Document Phrases
        DocPhrase* phraseObj = analyticData->GetPhrases()->at(0);
        CHECK(phraseObj != NULL);
        CHECK(phraseObj->GetIsNegated());
        CHECK_EQUAL(0.6133076d, phraseObj->GetSentimentScore());
        CHECK_EQUAL("Something", phraseObj->GetTitle());
        CHECK_EQUAL("not", phraseObj->GetNegatingPhrase());

        delete serializer;
        delete stub;
    }

    TEST(JsonDeserializingCollectionAnalyticData) {
        cout << "JsonDeserializingCollectionAnalyticData" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string source = "{\
                \"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\",\
                \"config_id\":\"23498367\",\
                \"status\":\"PROCESSED\",\
                \"facets\":[\
                    {\
                        \"label\":\"Something\",\
                        \"count\":10,\
                        \"negative_count\":2,\
                        \"positive_count\":1,\
                        \"neutral_count\":7,\
                        \"attributes\":[\
                            {\
                                \"label\":\"Attribute\",\
                                \"count\":5\
                            }\
                        ]\
                    }\
                ],\
                \"themes\":[\
                    {\
                        \"phrases_count\":1,\
                        \"themes_count\":1,\
                        \"sentiment_score\":0.0,\
                        \"title\":\"republican moderates\"\
                    }\
                ],\
                \"entities\":[\
                    {\
                        \"type\":\"named\",\
                        \"entity_type\":\"Place\",\
                        \"title\":\"WASHINGTON\",\
                        \"count\":1,\
                        \"negative_count\":1,\
                        \"neutral_count\":1,\
                        \"positive_count\":1\
                    }\
                ],\
                \"topics\":[\
                    {\
                        \"title\":\"Something\",\
                        \"type\":\"concept\",\
                        \"hitcount\":0,\
                        \"sentiment_score\":0.6133076\
                    }\
                ]\
            }";

        Json::Value root;
        Json::Reader reader;
        if (!reader.parse( source, root )){
            CHECK(false);
            return;
        }

        CollAnalyticData* analyticData = new CollAnalyticData();
        serializer->Deserialize(source, analyticData);

        CHECK_EQUAL("23498367", analyticData->GetConfigId());
        CHECK_EQUAL("6F9619FF8B86D011B42D00CF4FC964FF", analyticData->GetId());
        CHECK_EQUAL(1, analyticData->GetFacets()->size());
        CHECK_EQUAL(PROCESSED, analyticData->GetStatus());

        Facet* facetObj = analyticData->GetFacets()->at(0);
        CHECK_EQUAL("Something", facetObj->GetLabel());
        CHECK_EQUAL(10, facetObj->GetCount());
        CHECK_EQUAL(2, facetObj->GetNegativeCount());
        CHECK_EQUAL(1, facetObj->GetPositiveCount());
        CHECK_EQUAL(7, facetObj->GetNeutralCount());
        CHECK_EQUAL(1, facetObj->GetAttributes()->size());

        Attribute* attributeObj = facetObj->GetAttributes()->at(0);
        CHECK_EQUAL("Attribute", attributeObj->GetLabel());
        CHECK_EQUAL(5, attributeObj->GetCount());

        //Check Collection Themes
        CollTheme* themeObj = analyticData->GetThemes()->at(0);
        CHECK_EQUAL(1, themeObj->GetPhrasesCount());
        CHECK_EQUAL(1, themeObj->GetThemesCount());
        CHECK_EQUAL(0.0d, themeObj->GetSentimentScore());
        CHECK_EQUAL("republican moderates", themeObj->GetTitle());

        //Check Collection Entities
        CollEntity* entityObj = analyticData->GetEntities()->at(0);
        CHECK_EQUAL("WASHINGTON", entityObj->GetTitle());
        CHECK_EQUAL("named", entityObj->GetType());
        CHECK_EQUAL("Place", entityObj->GetEntityType());
        CHECK_EQUAL(1, entityObj->GetCount());
        CHECK_EQUAL(1, entityObj->GetNegativeCount());
        CHECK_EQUAL(1, entityObj->GetNeutralCount());
        CHECK_EQUAL(1, entityObj->GetPositiveCount());

        //Check Collection Topics
        CHECK_EQUAL(1, analyticData->GetTopics()->size());
        CollTopic* topicObj = analyticData->GetTopics()->at(0);
        CHECK_EQUAL(0, topicObj->GetHitCount());
        CHECK_EQUAL(0.6133076d, topicObj->GetSentimentScore());
        CHECK_EQUAL("Something", topicObj->GetTitle());
        CHECK_EQUAL("concept", topicObj->GetType());

        delete serializer;
        delete analyticData;
    }

        TEST(XmlSerializingSentimentPhrases) {
        cout << "XmlSerializingSentimentPhrases" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string expectedResult = "<?xml version=\"1.0\"?>\n<phrases><added><phrase><title>Feature: Cloud service</title><weight>0</weight></phrase></added><removed><phrase>Features</phrase></removed></phrases>";

        SentimentPhraseUpdateProxy* proxy = Session::CreateSession("", "")->CreateSentimentPhraseUpdateProxy();
        SentimentPhrase* addedPhrase = new SentimentPhrase("Feature: Cloud service", 0.0d);
        proxy->Add(addedPhrase);

        SentimentPhrase* removedPhrase = new SentimentPhrase("Features", 0.0d);
        proxy->Remove(removedPhrase);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(proxy)));

        delete serializer;
        delete addedPhrase;
        delete removedPhrase;
    }

    TEST(XmlSerializingConfigurations) {
        cout << "XmlSerializingConfigurations" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string expectedResult = "<?xml version=\"1.0\"?>\n<configurations><added><configuration><name>A test configuration</name><is_primary>true</is_primary><one_sentence>false</one_sentence><auto_responding>true</auto_responding><language>English</language><chars_threshold>80</chars_threshold><callback>https://anyapi.anydomain.com/processed/docs.json</callback><document><entity_themes_limit>5</entity_themes_limit><summary_limit>0</summary_limit><phrases_limit>0</phrases_limit><themes_limit>0</themes_limit><query_topics_limit>5</query_topics_limit><concept_topics_limit>5</concept_topics_limit><named_entities_limit>5</named_entities_limit><user_entities_limit>5</user_entities_limit></document><collection><facets_limit>15</facets_limit><facet_atts_limit>5</facet_atts_limit><themes_limit>0</themes_limit><query_topics_limit>5</query_topics_limit><concept_topics_limit>5</concept_topics_limit><named_entities_limit>5</named_entities_limit></collection></configuration></added><removed><configuration>45699836</configuration></removed></configurations>";

        ConfigurationUpdateProxy* proxy = Session::CreateSession("", "")->CreateConfigurationUpdateProxy();

        Configuration* addedConfiguration = new Configuration();
        addedConfiguration->SetName("A test configuration");
        addedConfiguration->SetIsPrimary(true);
        addedConfiguration->SetAutoResponding(true);
        addedConfiguration->SetOneSentence(false);
        addedConfiguration->SetLanguage("English");
        addedConfiguration->SetCharsThreshold(80);
        addedConfiguration->SetCallback("https://anyapi.anydomain.com/processed/docs.json");

        addedConfiguration->SetDocConceptTopicsLimit(5);
        addedConfiguration->SetDocQueryTopicsLimit(5);
        addedConfiguration->SetDocNamedEntitiesLimit(5);
        addedConfiguration->SetUserEntitiesLimit(5);
        addedConfiguration->SetDocThemesLimit(0);
        addedConfiguration->SetEntityThemesLimit(5);
        addedConfiguration->SetPhrasesLimit(0);
        addedConfiguration->SetSummaryLimit(0);

        addedConfiguration->SetFacetsLimit(15);
        addedConfiguration->SetFacetAttributesLimit(5);
        addedConfiguration->SetCollConceptTopicsLimit(5);
        addedConfiguration->SetCollQueryTopicsLimit(5);
        addedConfiguration->SetCollNamedEntitiesLimit(5);
        addedConfiguration->SetCollThemesLimit(0);

        proxy->Add(addedConfiguration);

        Configuration* removedConfiguration = new Configuration();
        removedConfiguration->SetId("45699836");
        proxy->Remove(removedConfiguration);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(proxy)));

        delete serializer;
        delete addedConfiguration;
        delete removedConfiguration;
    }

    TEST(XmlSerializingBlacklisted) {
        cout << "XmlSerializingBlacklisted" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string expectedResult = "<?xml version=\"1.0\"?>\n<blacklist><added><item>.*@.*com</item><item>.*@com\\.net</item></added><removed><item>http://www\\..*\\.com</item></removed></blacklist>";

        BlacklistUpdateProxy* proxy = Session::CreateSession("", "")->CreateBlacklistUpdateProxy();
        Blacklisted* addedItem_1 = new Blacklisted(".*@.*com");
        proxy->Add(addedItem_1);
        Blacklisted* addedItem_2 = new Blacklisted(".*@com\\.net");
        proxy->Add(addedItem_2);

        Blacklisted* removedItem = new Blacklisted("http://www\\..*\\.com");
        proxy->Remove(removedItem);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(proxy)));

        delete serializer;
        delete addedItem_1;
        delete addedItem_2;
        delete removedItem;
    }

    TEST(XmlSerializingQuery) {
        cout << "XmlSerializingQuery" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string expectedResult = "<?xml version=\"1.0\"?>\n<queries><added><query><name>Feature: Cloud service</name><query>Amazon AND EC2 AND Cloud</query></query></added><removed><query>Features</query></removed></queries>";

        QueryUpdateProxy* proxy = Session::CreateSession("", "")->CreateQueryUpdateProxy();
        Query* addedQuery = new Query("Feature: Cloud service", "Amazon AND EC2 AND Cloud");
        proxy->Add(addedQuery);

        Query* removedQuery = new Query("Features", "");
        proxy->Remove(removedQuery);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(proxy)));

        delete serializer;
        delete addedQuery;
        delete removedQuery;
    }

    TEST(XmlSerializingEntity) {
        cout << "XmlSerializingEntity" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string expectedResult = "<?xml version=\"1.0\"?>\n<entities><added><entity><name>chair</name><type>furniture</type></entity></added><removed><entity>table</entity></removed></entities>";

        EntityUpdateProxy* proxy = Session::CreateSession("", "")->CreateEntityUpdateProxy();
        UserEntity* addedEntity = new UserEntity("chair", "furniture");
        proxy->Add(addedEntity);

        UserEntity* removedEntity = new UserEntity("table", "");
        proxy->Remove(removedEntity);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(proxy)));

        delete serializer;
        delete addedEntity;
        delete removedEntity;
    }

    TEST(XmlSerializingCategory) {
        cout << "XmlSerializingCategory" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string expectedResult = "<?xml version=\"1.0\"?>\n<categories><added><category><name>chair</name><weight>0.4</weight><samples><sample>sample 1</sample><sample>sample 2</sample></samples></category></added><removed><category>table</category></removed></categories>";

        CategoryUpdateProxy* proxy = Session::CreateSession("", "")->CreateCategoryUpdateProxy();
        vector<string>* samples = new vector<string>();
        samples->push_back("sample 1");
        samples->push_back("sample 2");
        Category* addedCategory = new Category("chair", 0.4d, samples);
        proxy->Add(addedCategory);

        Category* removedCategory = new Category("table", 0.0);
        proxy->Remove(removedCategory);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(proxy)));

        delete serializer;
        delete addedCategory;
        delete removedCategory;
    }

    TEST(XmlSerializingDocument) {
        cout << "XmlSerializingDocument" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string expectedResult = "<?xml version=\"1.0\"?>\n<document><id>TASK_ID</id><text>some text</text></document>";

        Document* document = new Document();
        document->SetId("TASK_ID");
        document->SetText("some text");

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(document)));

        delete serializer;
        delete document;
    }

    TEST(XmlSerializingBatch) {
        cout << "XmlSerializingBatch" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string expectedResult = "<?xml version=\"1.0\"?>\n<documents><document><id>TASK_ID</id><text>some text</text></document><document><id>No_ID</id><text>other text</text></document></documents>";

        Document* document1 = new Document();
        document1->SetId("TASK_ID");
        document1->SetText("some text");
        Document* document2 = new Document();
        document2->SetId("No_ID");
        document2->SetText("other text");
        vector<Document*>* documents = new vector<Document*>();
        documents->push_back(document1);
        documents->push_back(document2);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(new Stub_Tasks(documents))));

        delete serializer;
        delete documents;
    }

    TEST(XmlSerializingCollection) {
        cout << "XmlSerializingCollection" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string expectedResult = "<?xml version=\"1.0\"?>\n<collection><id>COLL_ID</id><documents><document>other text</document><document>some text</document></documents></collection>";

        Collection* collection = new Collection();
        vector<string>* documents = new vector<string>();
        documents->push_back("other text");
        documents->push_back("some text");
        collection->SetId("COLL_ID");
        collection->SetDocuments(documents);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize(collection)));

        delete serializer;
        delete collection;
    }

    TEST(XmlDeserializingServiceStatus) {
        cout << "XmlDeserializingServiceStatus" << endl;
        XmlSerializer* serializer = new XmlSerializer();
        ServiceStatus* status = new ServiceStatus();

        string source = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\
                        <status>\
                            <service_status>online</service_status>\
                            <api_version>2.0</api_version>\
                            <service_version>1.0.2.63</service_version>\
                            <supported_encoding>UTF-8</supported_encoding>\
                            <supported_compression>gzip</supported_compression>\
                            <supported_languages>\
                                <language>English</language>\
                                <language>French</language>\
                            </supported_languages>\
                        </status>";

        serializer->Deserialize(source, status);

        CHECK_EQUAL("online", status->GetServiceStatus());
        CHECK_EQUAL("2.0", status->GetApiVersion());
        CHECK_EQUAL("1.0.2.63", status->GetServiceVersion());
        CHECK_EQUAL("UTF-8", status->GetSupportedEncoding());
        CHECK_EQUAL("gzip", status->GetSupportedCompression());
        CHECK_EQUAL(2, status->GetSupportedLanguages()->size());
        CHECK(status->GetSupportedLanguages()->at(0) != status->GetSupportedLanguages()->at(1));
        CHECK("English" == status->GetSupportedLanguages()->at(0) || "English" == status->GetSupportedLanguages()->at(1));
        CHECK("French" == status->GetSupportedLanguages()->at(0) || "French" == status->GetSupportedLanguages()->at(1));

        delete serializer;
        delete status;
    }

    TEST(XmlDeserializingSubscription) {
        cout << "XmlDeserializingSubscription" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        Subscription* subscription = new Subscription();
        string source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\
                        <subscription>\
                            <name>Subscriber</name>\
                            <status>active</status>\
                            <priority>normal</priority>\
                            <expiration_date>1293883200</expiration_date>\
                            <calls_balance>87</calls_balance>\
                            <calls_limit>100</calls_limit>\
                            <calls_limit_interval>60</calls_limit_interval>\
                            <docs_balance>49897</docs_balance>\
                            <docs_limit>0</docs_limit>\
                            <docs_limit_interval>0</docs_limit_interval>\
                            <configurations_limit>10</configurations_limit>\
                            <blacklist_limit>100</blacklist_limit>\
                            <categories_limit>100</categories_limit>\
                            <queries_limit>100</queries_limit>\
                            <entities_limit>1000</entities_limit>\
                            <sentiment_limit>1000</sentiment_limit>\
                            <characters_limit>8192</characters_limit>\
                            <batch_limit>1</batch_limit>\
                            <collection_limit>10</collection_limit>\
                            <auto_response_limit>2</auto_response_limit>\
                            <processed_batch_limit>100</processed_batch_limit>\
                            <callback_batch_limit>100</callback_batch_limit>\
                            <limit_type>throughput</limit_type>\
                        </subscription>";

        serializer->Deserialize(source, subscription);

        CHECK_EQUAL("Subscriber", subscription->GetName());
        CHECK_EQUAL("active", subscription->GetStatus());
        CHECK_EQUAL("normal", subscription->GetPriority());
        CHECK_EQUAL(1293883200L, subscription->GetExpirationDate());
        CHECK_EQUAL(87, subscription->GetCallsBalance());
        CHECK_EQUAL(100, subscription->GetCallsLimit());
        CHECK_EQUAL(60, subscription->GetCallsLimitInterval());
        CHECK_EQUAL(49897, subscription->GetDocsBalance());
        CHECK_EQUAL(0, subscription->GetDocsLimit());
        CHECK_EQUAL(0, subscription->GetDocsLimitInterval());
        CHECK_EQUAL(10, subscription->GetConfigurationsLimit());
        CHECK_EQUAL(100, subscription->GetBlacklistLimit());
        CHECK_EQUAL(100, subscription->GetCategoriesLimit());
        CHECK_EQUAL(100, subscription->GetQueriesLimit());
        CHECK_EQUAL(1000, subscription->GetEntitiesLimit());
        CHECK_EQUAL(1000, subscription->GetSentimentLimit());
        CHECK_EQUAL(8192, subscription->GetCharactersLimit());
        CHECK_EQUAL(1, subscription->GetBatchLimit());
        CHECK_EQUAL(10, subscription->GetCollectionLimit());
        CHECK_EQUAL(2, subscription->GetAutoResponseLimit());
        CHECK_EQUAL(100, subscription->GetProcessedBatchLimit());
        CHECK_EQUAL(100, subscription->GetCallbackBatchLimit());
        CHECK_EQUAL("throughput", subscription->GetLimitType());

        delete serializer;
        delete subscription;
    }

    TEST(XmlDeserializingSentimentPhrases) {
        cout << "XmlDeserializingSentimentPhrases" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string source = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><phrases><phrase><title>chair</title><weight>0.3</weight></phrase><phrase><title>asd</title><weight>0.76</weight></phrase></phrases>";

        Stub_SentimentPhrase* stub = new Stub_SentimentPhrase();
        serializer->Deserialize(source, stub);
        vector<SentimentPhrase*>* phrases = stub->GetPhrases();

        CHECK_EQUAL(2, phrases->size());
        CHECK_EQUAL("chair", phrases->at(0)->GetTitle());
        CHECK_EQUAL(0.3d, phrases->at(0)->GetWeight());
        CHECK_EQUAL("asd", phrases->at(1)->GetTitle());
        CHECK_EQUAL(0.76d, phrases->at(1)->GetWeight());

        delete serializer;
        delete stub;
    }

    TEST(XmlDeserializingBlacklist) {
        cout << "XmlDeserializingBlacklist" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\
                        <blacklist>\
                            <item>Filter 1</item>\
                            <item>Filter 2</item>\
                        </blacklist>";

        Stub_Blacklist* stub = new Stub_Blacklist();
        serializer->Deserialize(source, stub);

        vector<string>* items = stub->GetBlacklistItems();

        CHECK_EQUAL(2, items->size());
        CHECK_EQUAL("Filter 1", items->at(0));
        CHECK_EQUAL("Filter 2", items->at(1));

        delete serializer;
        delete stub;
    }

    TEST(XmlDeserializingCategories) {
        cout << "XmlDeserializingCategories" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\
                            <categories>\
                                <category>\
                                    <name>Feature: Cloud service</name>\
                                    <weight>0.75</weight>\
                                    <samples>\
                                        <sample>Amazon</sample>\
                                        <sample>EC2</sample>\
                                    </samples>\
                                </category>\
                            </categories>";

        Stub_Categories* stub = new Stub_Categories();
        serializer->Deserialize(source, stub);
        vector<Category*>* categories = stub->GetCategories();

        CHECK_EQUAL(1, categories->size());
        CHECK_EQUAL("Feature: Cloud service", categories->at(0)->GetName());
        CHECK_EQUAL(0.75d, categories->at(0)->GetWeight());
        CHECK_EQUAL(2, categories->at(0)->GetSamples()->size());
        CHECK_EQUAL("Amazon", categories->at(0)->GetSamples()->at(0));
        CHECK_EQUAL("EC2", categories->at(0)->GetSamples()->at(1));

        delete serializer;
        delete stub;
    }

    TEST(XmlDeserializingQueries) {
        cout << "XmlDeserializingQueries" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\
                        <queries>\
                            <query><name>Query 1</name><query>Something AND something</query></query>\
                            <query><name>Query 2</name><query>Something AND something</query></query>\
                        </queries>";

        Stub_Queries* stub = new Stub_Queries();
        serializer->Deserialize(source, stub);

        vector<Query*>* queries = stub->GetQueries();

        CHECK_EQUAL(2, queries->size());
        CHECK_EQUAL("Query 1", queries->at(0)->GetName());
        CHECK_EQUAL("Something AND something", queries->at(0)->GetQuery());
        CHECK_EQUAL("Query 2", queries->at(1)->GetName());
        CHECK_EQUAL("Something AND something", queries->at(1)->GetQuery());

        delete serializer;
        delete stub;
    }

    TEST(XmlDeserializingEntities) {
        cout << "XmlDeserializingEntities" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\
                            <entities>\
                                <entity>\
                                    <name>chair</name>\
                                    <type>furniture</type> \
                                </entity>\
                            </entities>";

        Stub_Entities* stub = new Stub_Entities();
        serializer->Deserialize(source, stub);

        vector<UserEntity*>* entities = stub->GetEntities();

        CHECK_EQUAL(1, entities->size());
        CHECK_EQUAL("chair", entities->at(0)->GetName());
        CHECK_EQUAL("furniture", entities->at(0)->GetType());

        delete serializer;
        delete stub;
    }

    TEST(XmlDeserializingConfigurations) {
        cout << "XmlDeserializingConfigurations" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string source = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\
                        <configurations>\
                            <configuration>\
                                <auto_responding>true</auto_responding>\
                                <callback>https://anyapi.anydomain.com/processed/docs.json</callback>\
                                <chars_threshold>80</chars_threshold>\
                                <collection>\
                                    <concept_topics_limit>5</concept_topics_limit>\
                                    <facet_atts_limit>20</facet_atts_limit>\
                                    <facets_limit>15</facets_limit>\
                                    <named_entities_limit>5</named_entities_limit>\
                                    <query_topics_limit>5</query_topics_limit>\
                                    <themes_limit>0</themes_limit>\
                                </collection>\
                                <document>\
                                    <concept_topics_limit>5</concept_topics_limit>\
                                    <entity_themes_limit>5</entity_themes_limit>\
                                    <named_entities_limit>5</named_entities_limit>\
                                    <phrases_limit>0</phrases_limit>\
                                    <query_topics_limit>5</query_topics_limit>\
                                    <summary_limit>0</summary_limit>\
                                    <themes_limit>0</themes_limit>\
                                    <user_entities_limit>5</user_entities_limit>\
                                </document>\
                                <is_primary>true</is_primary>\
                                <one_sentence>true</one_sentence>\
                                <language>English</language>\
                                <name>A test configuration</name>\
                            </configuration>\
                        </configurations>";

        Stub_Configurations* stub = new Stub_Configurations();
        serializer->Deserialize(source, stub);

        vector<Configuration*>* configurations = stub->GetConfigurations();

        CHECK_EQUAL(1, configurations->size());
        Configuration* configObj = configurations->at(0);
        CHECK_EQUAL("A test configuration", configObj->GetName());
        CHECK_EQUAL("English", configObj->GetLanguage());
        CHECK_EQUAL("https://anyapi.anydomain.com/processed/docs.json", configObj->GetCallback());
        CHECK_EQUAL(80, configObj->GetCharsThreshold());
        CHECK(configObj->GetAutoResponding());
        CHECK(configObj->GetIsPrimary());
        CHECK(configObj->GetOneSentence());
        CHECK_EQUAL(5, configObj->GetCollConceptTopicsLimit());
        CHECK_EQUAL(20, configObj->GetFacetAttributesLimit());
        CHECK_EQUAL(15, configObj->GetFacetsLimit());
        CHECK_EQUAL(5, configObj->GetCollNamedEntitiesLimit());
        CHECK_EQUAL(5, configObj->GetCollQueryTopicsLimit());
        CHECK_EQUAL(0, configObj->GetCollThemesLimit());
        CHECK_EQUAL(5, configObj->GetDocConceptTopicsLimit());
        CHECK_EQUAL(5, configObj->GetEntityThemesLimit());
        CHECK_EQUAL(5, configObj->GetDocNamedEntitiesLimit());
        CHECK_EQUAL(0, configObj->GetPhrasesLimit());
        CHECK_EQUAL(5, configObj->GetDocQueryTopicsLimit());
        CHECK_EQUAL(0, configObj->GetSummaryLimit());
        CHECK_EQUAL(0, configObj->GetDocThemesLimit());
        CHECK_EQUAL(5, configObj->GetUserEntitiesLimit());

        delete serializer;
        delete stub;
    }

    TEST(XmlDeserializingDocumentAnalyticData) {
        cout << "XmlDeserializingDocumentAnalyticData" << endl;
        XmlSerializer* serializer = new XmlSerializer();
        string source = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\
                        <document>\
                            <config_id>23498367</config_id>\
                            <id>6F9619FF8B86D011B42D00CF4FC964FF</id>\
                            <status>PROCESSED</status>\
                            <sentiment_score>0.2398756</sentiment_score>\
                            <summary>Summary of the document’s text.</summary>\
                            <themes>\
                                <theme>\
                                    <evidence>1</evidence>\
                                    <is_about>true</is_about>\
                                    <strength_score>0.0</strength_score>\
                                    <sentiment_score>0.0</sentiment_score>\
                                    <title>republican moderates</title>\
                                </theme>\
                            </themes>\
                            <entities>\
                                <entity>\
                                    <evidence>0</evidence>\
                                    <is_about>true</is_about>\
                                    <confident>true</confident>\
                                    <title>WASHINGTON</title>\
                                    <sentiment_score>1.0542796</sentiment_score>\
                                    <type>named</type>\
                                    <entity_type>Place</entity_type>\
                                    <themes>\
                                        <theme>\
                                            <evidence>1</evidence>\
                                            <is_about>true</is_about>\
                                            <strength_score>0.0</strength_score>\
                                            <sentiment_score>0.0</sentiment_score>\
                                            <title>republican moderates</title>\
                                        </theme>\
                                    </themes>\
                                </entity>\
                            </entities>\
                            <topics>\
                                <topic>\
                                    <title>Something</title>\
                                    <hitcount>0</hitcount>\
                                    <sentiment_score>0.6133076</sentiment_score>\
                                    <strength_score>0.6133076</strength_score>\
                                    <type>concept</type>\
                                </topic>\
                            </topics>\
                            <phrases>\
                                <phrase>\
                                    <title>Something</title>\
                                    <sentiment_score>0.6133076</sentiment_score>\
                                    <is_negated>true</is_negated>\
                                    <negating_phrase>not</negating_phrase>\
                                </phrase>\
                            </phrases>\
                        </document>";

        Stub_DocAnalyticDatas* stub = new Stub_DocAnalyticDatas();
        serializer->Deserialize(source, stub);

        vector<DocAnalyticData*>* analyticDataList = stub->GetAnalyticData();

        CHECK_EQUAL(1, analyticDataList->size());
        DocAnalyticData* analyticData = analyticDataList->at(0);

        CHECK_EQUAL("23498367", analyticData->GetConfigId());
        CHECK_EQUAL("6F9619FF8B86D011B42D00CF4FC964FF", analyticData->GetId());
        CHECK_EQUAL(PROCESSED, analyticData->GetStatus());
        CHECK_EQUAL(0.2398756d, analyticData->GetSentimentScore());
        CHECK_EQUAL("Summary of the document’s text.", analyticData->GetSummary());
        CHECK_EQUAL(1, analyticData->GetThemes()->size());
        CHECK_EQUAL(1, analyticData->GetEntities()->size());
        CHECK_EQUAL(1, analyticData->GetTopics()->size());

        //Check Document Themes
        DocTheme* themeObj = analyticData->GetThemes()->at(0);
        CHECK_EQUAL(1, themeObj->GetEvidence());
        CHECK_EQUAL(0.0d, themeObj->GetSentimentScore());
        CHECK_EQUAL(0.0d, themeObj->GetStrengthScore());
        CHECK_EQUAL("republican moderates", themeObj->GetTitle());
        CHECK(themeObj->GetIsAbout());

        //Check Document Entities
        DocEntity* entityObj = analyticData->GetEntities()->at(0);
        CHECK_EQUAL(0, entityObj->GetEvidence());
        CHECK(entityObj->GetIsAbout());
        CHECK(entityObj->GetConfident());
        CHECK_EQUAL("WASHINGTON", entityObj->GetTitle());
        CHECK_EQUAL(1.0542796d, entityObj->GetSentimentScore());
        CHECK_EQUAL("Place", entityObj->GetEntityType());
        CHECK_EQUAL("named", entityObj->GetType());

        //Check Entity Themes
        CHECK_EQUAL(1, entityObj->GetThemes()->size());
        DocTheme* entityThemeObj = entityObj->GetThemes()->at(0);
        CHECK_EQUAL(1, entityThemeObj->GetEvidence());
        CHECK_EQUAL(0.0d, entityThemeObj->GetSentimentScore());
        CHECK_EQUAL(0.0d, entityThemeObj->GetStrengthScore());
        CHECK_EQUAL("republican moderates", entityThemeObj->GetTitle());
        CHECK(entityThemeObj->GetIsAbout());

        //Check Document Topics
        CHECK_EQUAL(1, analyticData->GetTopics()->size());
        DocTopic* topicObj = analyticData->GetTopics()->at(0);
        CHECK_EQUAL(0, topicObj->GetHitCount());
        CHECK_EQUAL(0.6133076d, topicObj->GetSentimentScore());
        CHECK_EQUAL(0.6133076d, topicObj->GetStrengthScore());
        CHECK_EQUAL("Something", topicObj->GetTitle());
        CHECK_EQUAL("concept", topicObj->GetType());

        //Check Document Phrases
        DocPhrase* phraseObj = analyticData->GetPhrases()->at(0);
        CHECK(phraseObj != NULL);
        CHECK(phraseObj->GetIsNegated());
        CHECK_EQUAL(0.6133076d, phraseObj->GetSentimentScore());
        CHECK_EQUAL("Something", phraseObj->GetTitle());
        CHECK_EQUAL("not", phraseObj->GetNegatingPhrase());

        delete serializer;
        delete stub;
    }

    TEST(XmlDeserializingCollectionAnalyticData) {
        cout << "XmlDeserializingCollectionAnalyticData" << endl;
        XmlSerializer * serializer = new XmlSerializer();
        string source = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\
                        <collection>\
                            <config_id>23498367</config_id>\
                            <id>6F9619FF8B86D011B42D00CF4FC964FF</id>\
                            <status>PROCESSED</status>\
                            <facets>\
                                <facet>\
                                <label>Something</label>\
                                <count>10</count>\
                                <negative_count>2</negative_count>\
                                <positive_count>1</positive_count>\
                                <neutral_count>7</neutral_count>\
                                <attributes>\
                                    <attribute>\
                                    <label>Attribute</label>\
                                    <count>5</count>\
                                    </attribute>\
                                </attributes>\
                                </facet>\
                            </facets>\
                            <themes>\
                                <theme>\
                                    <phrases_count>1</phrases_count>\
                                    <themes_count>1</themes_count>\
                                    <sentiment_score>0.0</sentiment_score>\
                                    <title>republican moderates</title>\
                                </theme>\
                            </themes>\
                            <entities>\
                                <entity>\
                                    <title>WASHINGTON</title>\
                                    <type>named</type>\
                                    <entity_type>Place</entity_type>\
                                    <count>1</count>\
                                    <negative_count>1</negative_count>\
                                    <neutral_count>1</neutral_count>\
                                    <positive_count>1</positive_count>\
                                </entity>\
                            </entities>\
                            <topics>\
                                <topic>\
                                    <title>Something</title>\
                                    <hitcount>0</hitcount>\
                                    <sentiment_score>0.6133076</sentiment_score>\
                                    <type>concept</type>\
                                </topic>\
                            </topics>\
                        </collection>";

        Stub_CollAnalyticDatas* stub = new Stub_CollAnalyticDatas();
        serializer->Deserialize(source, stub);

        CHECK_EQUAL(1, stub->GetAnalyticData()->size());
        CollAnalyticData* analyticData = stub->GetAnalyticData()->at(0);
        CHECK_EQUAL("23498367", analyticData->GetConfigId());
        CHECK_EQUAL("6F9619FF8B86D011B42D00CF4FC964FF", analyticData->GetId());
        CHECK_EQUAL(1, analyticData->GetFacets()->size());
        CHECK_EQUAL(PROCESSED, analyticData->GetStatus());

        Facet* facetObj = analyticData->GetFacets()->at(0);
        CHECK_EQUAL("Something", facetObj->GetLabel());
        CHECK_EQUAL(10, facetObj->GetCount());
        CHECK_EQUAL(2, facetObj->GetNegativeCount());
        CHECK_EQUAL(1, facetObj->GetPositiveCount());
        CHECK_EQUAL(7, facetObj->GetNeutralCount());
        CHECK_EQUAL(1, facetObj->GetAttributes()->size());

        Attribute* attributeObj = facetObj->GetAttributes()->at(0);
        CHECK_EQUAL("Attribute", attributeObj->GetLabel());
        CHECK_EQUAL(5, attributeObj->GetCount());

        //Check Collection Themes
        CollTheme* themeObj = analyticData->GetThemes()->at(0);
        CHECK_EQUAL(1, themeObj->GetPhrasesCount());
        CHECK_EQUAL(1, themeObj->GetThemesCount());
        CHECK_EQUAL(0.0d, themeObj->GetSentimentScore());
        CHECK_EQUAL("republican moderates", themeObj->GetTitle());

        //Check Collection Entities
        CollEntity* entityObj = analyticData->GetEntities()->at(0);
        CHECK_EQUAL("WASHINGTON", entityObj->GetTitle());
        CHECK_EQUAL("named", entityObj->GetType());
        CHECK_EQUAL("Place", entityObj->GetEntityType());
        CHECK_EQUAL(1, entityObj->GetCount());
        CHECK_EQUAL(1, entityObj->GetNegativeCount());
        CHECK_EQUAL(1, entityObj->GetNeutralCount());
        CHECK_EQUAL(1, entityObj->GetPositiveCount());

        //Check Collection Topics
        CHECK_EQUAL(1, analyticData->GetTopics()->size());
        CollTopic* topicObj = analyticData->GetTopics()->at(0);
        CHECK_EQUAL(0, topicObj->GetHitCount());
        CHECK_EQUAL(0.6133076d, topicObj->GetSentimentScore());
        CHECK_EQUAL("Something", topicObj->GetTitle());
        CHECK_EQUAL("concept", topicObj->GetType());

        delete serializer;
        delete stub;
    }

}

