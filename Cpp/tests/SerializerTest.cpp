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

#include "../src/common/Enums.h"
#include "../src/Session.h"


namespace {

    string eraseLastCharacter(string source) {
        return source.erase(source.length() - 1, 1);
    }

    TEST(JsonSerializingSentimentPhrases) {
        cout << "JsonSerializingSentimentPhrases" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "[{\"name\":\"Feature: Cloud service\",\"weight\":0.0},{\"name\":\"Features\",\"weight\":0.0}]";

        vector<SentimentPhrase*>* list = new vector<SentimentPhrase*>();
        SentimentPhrase* addedPhrase = new SentimentPhrase("Feature: Cloud service", 0.0);
        list->push_back(addedPhrase);

        SentimentPhrase* removedPhrase = new SentimentPhrase("Features", 0.0);
        list->push_back(removedPhrase);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize((vector<JsonSerializable*>*)list)));

        delete serializer;
        delete addedPhrase;
        delete list;
    }

    TEST(JsonSerializingConfigurations) {
        cout << "JsonSerializingConfigurations" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "[{\"auto_response\":true,\"callback\":\"https://anyapi.anydomain.com/processed/docs.json\",\"chars_threshold\":80,"
        "\"collection\":{\"attribute_mentions_limit\":2,\"concept_topics_limit\":5,\"facet_atts_limit\":5,\"facet_mentions_limit\":5,"
        "\"facets_limit\":15,\"named_entities_limit\":5,\"named_mentions_limit\":0,\"query_topics_limit\":5,\"theme_mentions_limit\":0,\"themes_limit\":0},\"document\":{\"auto_categories_limit\":5,\"concept_topics_limit\":5,"
        "\"detect_language\":true,\"entity_themes_limit\":5,\"named_entities_limit\":5,\"named_mentions_limit\":0,\"named_opinions_limit\":0,"
        "\"named_relations_limit\":0,\"phrases_limit\":10,\"pos_types\":\"\",\"possible_phrases_limit\":0,\"query_topics_limit\":5,"
        "\"summary_limit\":0,\"theme_mentions_limit\":0,\"themes_limit\":0,\"user_entities_limit\":5,\"user_mentions_limit\":0,"
        "\"user_opinions_limit\":0,\"user_relations_limit\":0},\"is_primary\":true,\"language\":\"English\",\"name\":\"A test configuration\","
        "\"one_sentence\":false,\"process_html\":false,\"template\":\"template\"}]";

        vector<Configuration*>* list = new vector<Configuration*>();

        Configuration* addedConfiguration = new Configuration();
        addedConfiguration->SetName("A test configuration");
        addedConfiguration->SetTemplate("template");
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
        addedConfiguration->SetPhrasesLimit(10);
        addedConfiguration->SetSummaryLimit(0);

        addedConfiguration->SetFacetsLimit(15);
        addedConfiguration->SetFacetMentionsLimit(5);
        addedConfiguration->SetFacetAttributesLimit(5);
        addedConfiguration->SetAttributeMentionsLimit(2);

        addedConfiguration->SetCollConceptTopicsLimit(5);
        addedConfiguration->SetCollQueryTopicsLimit(5);
        addedConfiguration->SetCollNamedEntitiesLimit(5);
        addedConfiguration->SetCollThemesLimit(0);
        addedConfiguration->SetOneSentence(false);

        list->push_back(addedConfiguration);

        string result = eraseLastCharacter(serializer->Serialize((vector<JsonSerializable*>*)list));
        CHECK_EQUAL(expectedResult, result);

        delete serializer;
        delete addedConfiguration;
        delete list;
    }

    TEST(JsonSerializingBlacklisted) {
        cout << "JsonSerializingBlacklisted" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "[\".*@.*com\",\".*@com\\\\.net\",\"http://www\\\\..*\\\\.com\"]";

        vector<Blacklisted*>* list = new vector<Blacklisted*>();

        Blacklisted* addedItem_1 = new Blacklisted(".*@.*com");
        list->push_back(addedItem_1);
        Blacklisted* addedItem_2 = new Blacklisted(".*@com\\.net");
        list->push_back(addedItem_2);
        Blacklisted* addedItem_3 = new Blacklisted("http://www\\..*\\.com");
        list->push_back(addedItem_3);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize((vector<JsonSerializable*>*)list)));

        delete serializer;
        delete addedItem_1;
        delete addedItem_2;
        delete addedItem_3;
        delete list;
    }

    TEST(JsonSerializingQuery) {
        cout << "JsonSerializingQuery" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "[{\"name\":\"Feature: Cloud service\",\"query\":\"Amazon AND EC2 AND Cloud\"}]";

        vector<Query*>* list = new vector<Query*>();
        Query* addedQuery = new Query("Feature: Cloud service", "Amazon AND EC2 AND Cloud");
        list->push_back(addedQuery);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize((vector<JsonSerializable*>*)list)));

        delete serializer;
        delete addedQuery;
        delete list;
    }

    TEST(JsonSerializingEntity) {
        cout << "JsonSerializingEntity" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "[{\"label\":\"wiki\",\"name\":\"chair\",\"normalized\":\"norm\",\"type\":\"furniture\"}]";

        vector<UserEntity*>* list = new vector<UserEntity*>();
        UserEntity* addedEntity = new UserEntity("chair", "furniture");
        addedEntity->SetLabel("wiki");
        addedEntity->SetNormalized("norm");
        list->push_back(addedEntity);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize((vector<JsonSerializable*>*)list)));

        delete serializer;
        delete addedEntity;
        delete list;
    }

    TEST(JsonSerializingCategory) {
        cout << "JsonSerializingCategory" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string expectedResult = "[{\"name\":\"chair\",\"samples\":[\"sample 1\",\"sample 2\"],\"weight\":0.40}]";

        vector<Category*>* list = new vector<Category*>();
        vector<string>* samples = new vector<string>();
        samples->push_back("sample 1");
        samples->push_back("sample 2");
        Category* addedCategory = new Category("chair", 0.4, samples);
        list->push_back(addedCategory);

        CHECK_EQUAL(expectedResult, eraseLastCharacter(serializer->Serialize((vector<JsonSerializable*>*)list)));

        delete serializer;
        delete addedCategory;
        delete list;
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
                            \"billing_settings\":{\
                                \"priority\" : \"normal\",\
                                \"expiration_date\" : 1293883200,\
                                \"limit_type\" : \"throughput\",\
                                \"calls_balance\" : 87,\
                                \"calls_limit\" : 100,\
                                \"calls_limit_interval\" : 60,\
                                \"docs_balance\" : 49897,\
                                \"docs_limit\" : 0,\
                                \"docs_limit_interval\" : 0,\
                                \"docs_suggested\" : 12,\
                                \"docs_suggested_interval\" : 30\
                            },\
                            \"basic_settings\":{\
                                \"configurations_limit\" : 10,\
                                \"output_data_limit\" : 20,\
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
                                \"callback_batch_limit\" : 100\
                            },\
                            \"feature_settings\":{\
                                \"document\": {\
                                    \"summary\": true,\
                                    \"themes\": true,\
                                    \"named_entities\": true,\
                                    \"user_entities\": true,\
                                    \"entity_themes\": false,\
                                    \"mentions\" : false,\
                                    \"opinions\" : false,\
                                    \"named_relations\": false,\
                                    \"user_relations\": false,\
                                    \"query_topics\": true,\
                                    \"concept_topics\": false,\
                                    \"sentiment_phrases\": true,\
                                    \"phrases_detection\": false,\
                                    \"pos_tagging\": false,\
                                    \"language_detection\": false,\
                                    \"auto_categories\": false\
                                },\
                                \"collection\": {\
                                    \"facets\": true,\
                                    \"mentions\": false,\
                                    \"themes\": true,\
                                    \"named_entities\": true,\
                                    \"query_topics\": true,\
                                    \"concept_topics\": false\
                                },\
                                \"supported_languages\" : \"English, French, Spanish\",\
                                \"html_processing\" : false\
                            }\
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

        CHECK_EQUAL(12, subscription->GetDocsSuggested()   );
        CHECK_EQUAL(30, subscription->GetDocsSuggestedInterval());

        CHECK_EQUAL(10, subscription->GetConfigurationsLimit());
        CHECK_EQUAL(20, subscription->GetOutputDataLimit());
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

        CHECK_EQUAL(false, subscription->GetDocMentions());
        CHECK_EQUAL(false, subscription->GetOpinions());

        CHECK_EQUAL(true, subscription->GetFacets());
        CHECK_EQUAL(false, subscription->GetMentions());

        CHECK_EQUAL(false, subscription->GetAutoCategories());

        CHECK_EQUAL("throughput", subscription->GetLimitType());
        CHECK_EQUAL("English, French, Spanish", subscription->GetSupportedLanguages());
        CHECK_EQUAL(false, subscription->GetHtmlProcessing());

        delete serializer;
        delete subscription;
    }


    TEST(JsonDeserializingSentimentPhrases) {
        cout << "JsonDeserializingSentimentPhrases" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string source = "[{\"name\":\"chair\",\"weight\":0.75}]";;

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
        CHECK_EQUAL("chair", phrases->at(0)->GetName());
        CHECK_EQUAL(0.75, phrases->at(0)->GetWeight());

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
        CHECK_EQUAL(0.75, categories->at(0)->GetWeight());
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
        string source = "[{\"name\":\"chair\",\"type\":\"furniture\",\"label\":\"wiki\"}]";

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
        CHECK_EQUAL("wiki", entities->at(0)->GetLabel());

        delete serializer;
        delete stub;
    }

    TEST(JsonDeserializingStatistics) {
        cout << "JsonDeserializingSatistics" << endl;
        JsonSerializer * serializer = new JsonSerializer();

        string source = "{\
        \"name\": \"Subscriber\",\
        \"status\": \"active\",\
        \"overall_texts\": 129863,\
        \"overall_batches\": 1300,\
        \"overall_calls\": 13769,\
        \"overall_exceeded\" : 1200,\
        \"calls_system\": 21,\
        \"calls_data\": 13748,\
        \"overall_docs\": 121863,\
        \"docs_processed\": 121860,\
        \"docs_failed\": 3,\
        \"docs_responded\": 121863,\
        \"overall_colls\": 8,\
        \"colls_processed\": 8,\
        \"colls_failed\": 0,\
        \"colls_responded\": 8,\
        \"colls_documents\": 8000,\
        \"latest_used_app\": \".Net\",\
        \"used_apps\": \".Net,Excel add-in x86,Python\",\
        \"configurations\": [\
            {\
                \"config_id\": \"cd2e7341-a3c2-4fb4-9d3a-779e8b0a5eff\",\
                \"name\": \"A test configuration\",\
                \"overall_texts\": 129863,\
                \"overall_batches\": 1300,\
                \"overall_calls\": 13769,\
                \"overall_exceeded\" : 1200,\
                \"calls_system\": 21,\
                \"calls_data\": 13748,\
                \"overall_docs\": 121863,\
                \"docs_processed\": 121860,\
                \"docs_failed\": 3,\
                \"docs_responded\": 121863,\
                \"overall_colls\": 8,\
                \"colls_processed\": 8,\
                \"colls_failed\": 0,\
                \"colls_responded\": 8,\
                \"colls_documents\": 8000,\
                \"latest_used_app\": \".Net\",\
                \"used_apps\": \".Net,Excel add-in x86,Python\"\
            }\
        ]\
        }";

        Json::Value root;
        Json::Reader reader;
        if (!reader.parse( source, root )){
            CHECK(false);
            return;
        }

        Statistics* statistic = new Statistics();
        serializer->Deserialize(source, statistic);

        CHECK_EQUAL("Subscriber", statistic->GetName());
        CHECK_EQUAL(8000, statistic->GetCollsDocuments());
        CHECK_EQUAL(1200, statistic->GetOverallExceeded());

        CHECK_EQUAL("cd2e7341-a3c2-4fb4-9d3a-779e8b0a5eff", statistic->GetConfigurations()->at(0)->GetConfigId());
        CHECK_EQUAL(1200, statistic->GetConfigurations()->at(0)->GetOverallExceeded());



        delete serializer;
        delete statistic;
    }

    TEST(JsonDeserializingDocumentAnalyticData) {
        cout << "JsonDeserializingDocumentAnalyticData" << endl;
        JsonSerializer * serializer = new JsonSerializer();
        string source = "[{\
                \"id\":\"6F9619FF8B86D011B42D00CF4FC964FF\",\
                \"config_id\":\"23498367\",\
                \"tag\":\"mytag\",\
                \"status\":\"PROCESSED\",\
                \"source_text\" : \"See Output Data Details chapter.\",\
                \"language\" : \"English\",\
                \"language_score\" : 0.6972651,\
                \"sentiment_score\":0.8295653,\
                \"sentiment_polarity\" : \"positive\",\
                \"summary\":\"Summary of the document’s text.\",\
                \"details\" : [ \
                { \
                    \"is_imperative\" : false, \
                    \"is_polar\" : false, \
                    \"words\" : \
                    [ { \
                        \"tag\" : \"NNP\", \
                        \"type\" : \"Noun\", \
                        \"title\" : \"Aaron\", \
                        \"stemmed\" : \"Aaron\", \
                        \"is_negated\" : false, \
                        \"sentiment_score\" : 0.569 \
                        } \
                        ] \
                    } \
                ],\
                \"auto_categories\" :[ \
                { \
                    \"title\" : \"Automotive\", \
                    \"type\" : \"node\", \
                    \"strength_score\" : 0.378, \
                    \"categories\" : [ { \
                        \"title\" : \"Moto\", \
                        \"type\" : \"leaf\",\
                        \"strength_score\" : 0.67 \
                        } ] } \
                ],\
                \"themes\":[\
                {\
                        \"evidence\":1,\
                        \"is_about\":true,\
                        \"strength_score\":0.0,\
                        \"sentiment_score\":0.0,\
                        \"title\":\"republican moderates\",\
                        \"mentions\" : [\
                              {\
                                 \"label\" : \"Something\",\
                                 \"is_negated\" : false,\
                                 \"negating_phrase\" : \"negator\",\
                                 \"locations\" : [\
                                     {\
                                        \"offset\" : 987,\
                                        \"length\" : 9\
                                     }\
                                 ]\
                              }\
                        ]\
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
                    \"label\" : \"The capital of the United States of America.\",\
                    \"sentiment_score\":1.0542796,\
                    \"sentiment_polarity\" : \"positive\",\
                    \"mentions\" : [\
                              {\
                                 \"label\" : \"Something\",\
                                 \"is_negated\" : false,\
                                 \"negating_phrase\" : \"negator\",\
                                 \"locations\" : [\
                                     {\
                                        \"offset\" : 123,\
                                        \"length\" : 9\
                                     }\
                                 ]\
                              }\
                        ],\
                    \"themes\":[\
                        {\
                            \"evidence\":1,\
                            \"is_about\":true,\
                            \"strength_score\":0.0,\
                            \"sentiment_score\":0.0,\
                            \"title\":\"republican moderates\",\
                            \"mentions\" : [\
                              {\
                                 \"label\" : \"Something\",\
                                 \"is_negated\" : false,\
                                 \"negating_phrase\" : \"negator\",\
                                 \"locations\" : [\
                                     {\
                                        \"offset\" : 321,\
                                        \"length\" : 9\
                                     }\
                                 ]\
                              }\
                        ]\
                        }\
                    ]\
                    }\
                ],\
                \"relations\" : [ \
                    {\
                        \"type\" : \"named\",\
                        \"relation_type\" : \"Occupation\",\
                        \"confidence_score\" : 1.0,\
                        \"extra\" : \"\", \
                        \"entities\" : [\
                            {\
                                 \"title\" : \"head judge\",\
                                \"entity_type\" : \"Job Title\" \
                            },\
                            { \
                                \"title\" : \"John Snow\",\
                                \"entity_type\" : \"Person\"\
                            }\
                        ]\
                    }\
                ],\
                \"opinions\" : [ \
                    { \
                         \"quotation\" : \"Some expressed opinion of John Kerry about United States.\",\
                         \"type\" : \"named\", \
                         \"speaker\" : \"John Kerry\", \
                         \"topic\" : \"United States\", \
                         \"sentiment_score\" : 0.49 ,\
                         \"sentiment_polarity\" : \"positive\"\
                     } \
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
        CHECK_EQUAL("mytag", analyticData->GetTag());
        CHECK_EQUAL(PROCESSED, analyticData->GetStatus());
        CHECK_EQUAL(0.8295653, analyticData->GetSentimentScore());
        CHECK_EQUAL("Summary of the document’s text.", analyticData->GetSummary());

        CHECK_EQUAL("See Output Data Details chapter.", analyticData->GetSourceText());
        CHECK_EQUAL("English", analyticData->GetLanguage());
        CHECK_EQUAL(0.6972651, analyticData->GetLanguageScore());
        CHECK_EQUAL("positive", analyticData->GetSentimentPolarity());

        CHECK_EQUAL(1, analyticData->GetThemes()->size());
        CHECK_EQUAL(1, analyticData->GetEntities()->size());
        CHECK_EQUAL(1, analyticData->GetTopics()->size());

        //Check Details
        DocDetails* detailsObj = analyticData->GetDetails()->at(0);
        CHECK_EQUAL(false, detailsObj->GetIsPolar());
        CHECK_EQUAL(false, detailsObj->GetIsImperative());
        DocWords* docWObj = detailsObj->GetWords()->at(0);
        CHECK_EQUAL("NNP", docWObj->GetTag());
        CHECK_EQUAL("Aaron", docWObj->GetTitle());
        CHECK_EQUAL("Noun", docWObj->GetType());
        CHECK_EQUAL("Aaron", docWObj->GetStemmed());
        CHECK_EQUAL(false, docWObj->GetIsNegated());
        CHECK_EQUAL(0.569, docWObj->GetSentimentScore());

        //Check Auto Categories
        DocAutoCategories* autoCategoriesObj = analyticData->GetAutoCategories()->at(0);
        CHECK_EQUAL("Automotive", autoCategoriesObj->GetTitle());
        CHECK_EQUAL("node", autoCategoriesObj->GetType());
        CHECK_EQUAL(0.378, autoCategoriesObj->GetStrengthScore());
        DocCategories* categoryObj = autoCategoriesObj->GetCategories()->at(0);
        CHECK_EQUAL("Moto", categoryObj->GetTitle());
        CHECK_EQUAL("leaf", categoryObj->GetType());
        CHECK_EQUAL(0.67, categoryObj->GetStrengthScore());

        //Check Document Themes
        DocTheme* themeObj = analyticData->GetThemes()->at(0);
        CHECK_EQUAL(1, themeObj->GetEvidence());
        CHECK_EQUAL(0.0, themeObj->GetSentimentScore());
        CHECK_EQUAL(0.0, themeObj->GetStrengthScore());
        CHECK_EQUAL("republican moderates", themeObj->GetTitle());
        CHECK(themeObj->GetIsAbout());
        // mentions
        MentionWithLocations* mention = themeObj->GetMentions()->at(0);
        CHECK_EQUAL("Something", mention->GetLabel());
        CHECK_EQUAL(false, mention->GetIsNedated());
        CHECK_EQUAL("negator", mention->GetNegationPhrase());
        Location* location = mention->GetLocations()->at(0);
        CHECK_EQUAL(987, location->GetOffset());
        CHECK_EQUAL(9, location->GetLength());

        //Check Document Entities
        DocEntity* entityObj = analyticData->GetEntities()->at(0);
        CHECK_EQUAL(0, entityObj->GetEvidence());
        CHECK(entityObj->GetIsAbout());
        CHECK(entityObj->GetConfident());
        CHECK_EQUAL("WASHINGTON", entityObj->GetTitle());
        CHECK_EQUAL("The capital of the United States of America.", entityObj->GetLabel());
        CHECK_EQUAL(1.0542796, entityObj->GetSentimentScore());
        CHECK_EQUAL("positive", entityObj->GetSentimentPolarity());
        CHECK_EQUAL("Place", entityObj->GetEntityType());
        CHECK_EQUAL("named", entityObj->GetType());
        // mentions
        MentionWithLocations* mention1 = entityObj->GetMentions()->at(0);
        CHECK_EQUAL("Something", mention1->GetLabel());
        CHECK_EQUAL(false, mention1->GetIsNedated());
        CHECK_EQUAL("negator", mention1->GetNegationPhrase());
        Location* location1 = mention1->GetLocations()->at(0);
        CHECK_EQUAL(123, location1->GetOffset());
        CHECK_EQUAL(9, location1->GetLength());

        //Check Entity Themes
        CHECK_EQUAL(1, entityObj->GetThemes()->size());
        DocTheme* entityThemeObj = entityObj->GetThemes()->at(0);
        CHECK_EQUAL(1, entityThemeObj->GetEvidence());
        CHECK_EQUAL(0.0, entityThemeObj->GetSentimentScore());
        CHECK_EQUAL(0.0, entityThemeObj->GetStrengthScore());
        CHECK_EQUAL("republican moderates", entityThemeObj->GetTitle());
        CHECK(entityThemeObj->GetIsAbout());
        // mentions
        MentionWithLocations* mention2 = entityThemeObj->GetMentions()->at(0);
        CHECK_EQUAL("Something", mention2->GetLabel());
        CHECK_EQUAL(false, mention2->GetIsNedated());
        CHECK_EQUAL("negator", mention2->GetNegationPhrase());
        Location* location2 = mention2->GetLocations()->at(0);
        CHECK_EQUAL(321, location2->GetOffset());
        CHECK_EQUAL(9, location2->GetLength());

        //Check Document Relations
        CHECK_EQUAL(1, analyticData->GetRelations()->size());
        DocRelations* relations = analyticData->GetRelations()->at(0);
        CHECK_EQUAL("Occupation", relations->GetRelationType());
        CHECK_EQUAL("named", relations->GetType());
        CHECK_EQUAL(2, relations->GetEntitles()->size());
        DocRelEntities* ent1 = relations->GetEntitles()->at(0);
        CHECK_EQUAL("Job Title", ent1->GetEntityType());
        CHECK_EQUAL("head judge", ent1->GetTitle());

        //Check Document Opinions
        CHECK_EQUAL(1, analyticData->GetOpinions()->size());
        DocOpinion* opinion = analyticData->GetOpinions()->at(0);
        CHECK_EQUAL("Some expressed opinion of John Kerry about United States.", opinion->GetQuotation());
        CHECK_EQUAL("named", opinion->GetType());
        CHECK_EQUAL("John Kerry", opinion->GetSpeaker());
        CHECK_EQUAL("United States", opinion->GetTopic());
        CHECK_EQUAL(0.49, opinion->GetSentimentScore());
        CHECK_EQUAL("positive", opinion->GetSentimentPolarity());


        //Check Document Topics
        CHECK_EQUAL(1, analyticData->GetTopics()->size());
        DocTopic* topicObj = analyticData->GetTopics()->at(0);
        CHECK_EQUAL(0, topicObj->GetHitCount());
        CHECK_EQUAL(0.6133076, topicObj->GetSentimentScore());
        CHECK_EQUAL(0.0, topicObj->GetStrengthScore());
        CHECK_EQUAL("Something", topicObj->GetTitle());
        CHECK_EQUAL("concept", topicObj->GetType());

        //Check Document Phrases
        DocPhrase* phraseObj = analyticData->GetPhrases()->at(0);
        CHECK(phraseObj != NULL);
        CHECK(phraseObj->GetIsNegated());
        CHECK_EQUAL(0.6133076, phraseObj->GetSentimentScore());
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
                \"tag\":\"mytag\",\
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
                                \"count\":5,\
                                \"mentions\" : [\
                                      {\
                                         \"label\" : \"something\",\
                                         \"is_negated\" : true,\
                                         \"negating_phrase\" : \"negator\",\
                                         \"locations\" : [ {\
                                             \"index\" : 17,\
                                             \"offset\" : 987,\
                                             \"length\" : 9\
                                          }]\
                                      }\
                                ]\
                            }\
                        ],\
                        \"mentions\" : [\
                            {\
                                \"label\" : \"Mention_009\",\
                                \"is_negated\" : true,\
                                \"negating_phrase\" : \"negator\",\
                                \"locations\" : [ {\
                                     \"index\" : 117,\
                                     \"offset\" : 1987,\
                                     \"length\" : 19\
                                  }]\
                            }\
                        ]\
                    }\
                ],\
                \"themes\":[\
                    {\
                        \"phrases_count\":1,\
                        \"themes_count\":1,\
                        \"sentiment_score\":0.0,\
                        \"title\":\"republican moderates\",\
                        \"mentions\" : [\
                            {\
                                \"label\" : \"Mention_007\",\
                                \"is_negated\" : true,\
                                \"negating_phrase\" : \"negator\",\
                                \"locations\" : [ {\
                                     \"index\" : 62,\
                                     \"offset\" : 1987,\
                                     \"length\" : 19\
                                  }]\
                            }\
                        ]\
                    }\
                ],\
                \"entities\":[\
                    {\
                        \"type\":\"named\",\
                        \"label\":\"label1\",\
                        \"entity_type\":\"Place\",\
                        \"title\":\"WASHINGTON\",\
                        \"count\":1,\
                        \"negative_count\":1,\
                        \"neutral_count\":1,\
                        \"positive_count\":1,\
                        \"mentions\" : [\
                            {\
                                \"label\" : \"Mention_003\",\
                                \"is_negated\" : false,\
                                \"negating_phrase\" : \"negator\",\
                                \"locations\" : [ {\
                                     \"index\" : 3,\
                                     \"offset\" : 5,\
                                     \"length\" : 6\
                                  }]\
                            }\
                        ]\
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
        CHECK_EQUAL("mytag", analyticData->GetTag());
        CHECK_EQUAL(1, analyticData->GetFacets()->size());
        CHECK_EQUAL(PROCESSED, analyticData->GetStatus());

        // Check Fasets
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

        Mention* attMention = attributeObj->GetMentions()->at(0);
        CHECK_EQUAL("something", attMention->GetLabel());

        Location* location = attMention->GetLocations()->at(0);
        CHECK_EQUAL(17, location->GetIndex());

        Mention* mention = facetObj->GetMentions()->at(0);
        CHECK_EQUAL("Mention_009", mention->GetLabel());
        {
            Location* location = mention->GetLocations()->at(0);
            CHECK_EQUAL(117, location->GetIndex());
        }


        //Check Collection Themes
        CollTheme* themeObj = analyticData->GetThemes()->at(0);
        CHECK_EQUAL(1, themeObj->GetPhrasesCount());
        CHECK_EQUAL(1, themeObj->GetThemesCount());
        CHECK_EQUAL(0.0, themeObj->GetSentimentScore());
        CHECK_EQUAL("republican moderates", themeObj->GetTitle());
        {
            Mention* mention = themeObj->GetMentions()->at(0);
            CHECK_EQUAL("Mention_007", mention->GetLabel());

            Location* location = mention->GetLocations()->at(0);
            CHECK_EQUAL(62, location->GetIndex());
        }

        //Check Collection Entities
        CollEntity* entityObj = analyticData->GetEntities()->at(0);
        CHECK_EQUAL("WASHINGTON", entityObj->GetTitle());
        CHECK_EQUAL("named", entityObj->GetType());
        CHECK_EQUAL("label1", entityObj->GetLabel());
        CHECK_EQUAL("Place", entityObj->GetEntityType());
        CHECK_EQUAL(1, entityObj->GetCount());
        CHECK_EQUAL(1, entityObj->GetNegativeCount());
        CHECK_EQUAL(1, entityObj->GetNeutralCount());
        CHECK_EQUAL(1, entityObj->GetPositiveCount());
        {
            Mention* mention = entityObj->GetMentions()->at(0);
            CHECK_EQUAL("Mention_003", mention->GetLabel());
            CHECK_EQUAL(false, mention->GetIsNedated());
            CHECK_EQUAL("negator", mention->GetNegatingPhrase());

            Location* location = mention->GetLocations()->at(0);
            CHECK_EQUAL(3, location->GetIndex());
            CHECK_EQUAL(5, location->GetOffset());
            CHECK_EQUAL(6, location->GetLength());
        }

        //Check Collection Topics
        CHECK_EQUAL(1, analyticData->GetTopics()->size());
        CollTopic* topicObj = analyticData->GetTopics()->at(0);
        CHECK_EQUAL(0, topicObj->GetHitCount());
        CHECK_EQUAL(0.6133076, topicObj->GetSentimentScore());
        CHECK_EQUAL("Something", topicObj->GetTitle());
        CHECK_EQUAL("concept", topicObj->GetType());

        delete serializer;
        delete analyticData;
    }

}

