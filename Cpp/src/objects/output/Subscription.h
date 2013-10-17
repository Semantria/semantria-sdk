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

    string GetName()                                {return name; }
    string GetStatus()                              {return status; }
    string GetPriority()                            {return priority; }
    long GetExpirationDate()                        {return expiration_date; }
    string GetLimitType()                           {return limit_type; }
    int GetCallsBalance()                           {return calls_balance;}
    int GetCallsLimit()                             {return calls_limit;}
    int GetCallsLimitInterval()                     {return calls_limit_interval;}
    int GetDocsBalance()                            {return docs_balance;}
    int GetDocsLimit()                              {return docs_limit;}
    int GetDocsLimitInterval()                      {return docs_limit_interval;}
    int GetDocsSuggested()                          {return docs_suggested;}
    int GetDocsSuggestedInterval()                  {return docs_suggested_interval;}


    int GetConfigurationsLimit()                    {return configurations_limit;}
    int GetOutputDataLimit()                        {return output_data_limit;}
    int GetBlacklistLimit()                         {return blacklist_limit;}
    int GetCategoriesLimit()                        {return categories_limit;}
    int GetCategorySamplesLimit()                   {return category_samples_limit;}
    bool GetReturnSourceText()                      {return return_source_text;}
    int GetQueriesLimit()                           {return queries_limit;}
    int GetEntitiesLimit()                          {return entities_limit;}
    int GetSentimentLimit()                         {return sentiment_limit;}
    int GetCharactersLimit()                        {return characters_limit;}
    int GetBatchLimit()                             {return batch_limit; }
    int GetCollectionLimit()                        {return collection_limit; }
    int GetAutoResponseLimit()                      {return auto_response_limit; }
    int GetProcessedBatchLimit()                    {return processed_batch_limit; }
    int GetCallbackBatchLimit()                     {return callback_batch_limit; }

    bool GetSummary()                               {return summary;}
    bool GetAutoCategories()                        {return auto_categories;}
    bool GetDocThemes()                             {return doc_themes;}
    bool GetDocNamedEntities()                      {return doc_named_entities;}
    bool GetDocUserEntities()                       {return doc_user_entities;}
    bool GetEntityThemes()                          {return entity_themes;}
    bool GetDocMentions()                           {return doc_mentions;}
    bool GetOpinions()                              {return opinions;}
    bool GetNamedRelations()                        {return named_relations;}
    bool GetUserRelations()                         {return user_relations;}
    bool GetDocQueryTopics()                        {return doc_query_topics;}
    bool GetDocConceptTopics()                      {return doc_concept_topics;}
    bool GetSentimentPhrases()                      {return sentiment_phrases;}
    bool GetPhrasesDetection()                      {return phrases_detection;}
    bool GetPosTagging()                            {return pos_tagging;}
    bool GetLanguageDetection()                     {return language_detection;}

    bool GetFacets()                                {return facets;}
    bool GetMentions()                              {return mentions;}
    bool GetCollThemes()                            {return coll_themes;}
    bool GetCollNamedEntities()                     {return coll_named_entities;}
    //bool GetCollUserEntities()                      {return coll_user_entities;}
    bool GetCollQueryTopics()                       {return coll_query_topics;}
    bool GetCollConceptTopics()                     {return coll_concept_topics;}

    string GetSupportedLanguages()                  {return supported_languages;}
    bool GetHtmlProcessing()                       {return html_processing;}




    void SetName(string name)                                   {this->name = name;}
    void SetStatus(string status)                               {this->status = status;}

    void SetPriority(string priority)                           {this->priority = priority;}
    void SetExpirationDate(long expiration_date)                {this->expiration_date = expiration_date;}
    void SetLimitType(string limit_type)                        {this->limit_type = limit_type;}

    void SetCallsBalance(int calls_balance)                     {this->calls_balance = calls_balance;}
    void SetCallsLimit(int calls_limit)                         {this->calls_limit = calls_limit;}
    void SetCallsLimitInterval(int calls_limit_interval)        {this->calls_limit_interval = calls_limit_interval;}
    void SetDocsBalance(int docs_balance)                       {this->docs_balance = docs_balance;}
    void SetDocsLimit(int docs_limit)                           {this->docs_limit = docs_limit;}
    void SetDocsLimitInterval(int docs_limit_interval)          {this->docs_limit_interval = docs_limit_interval;}
    void SetDocsSuggested(int docs_suggested)                   {this->docs_suggested = docs_suggested;}
    void SetDocsSuggestedInterval(int docs_suggested_interval)  {this->docs_suggested_interval = docs_suggested_interval;}


    void SetConfigurationsLimit(int configurations_limit)       {this->configurations_limit = configurations_limit;}
    void SetOutputDataLimit(int output_data_limit)              {this->output_data_limit = output_data_limit;}
    void SetBlacklistLimit(int blacklist_limit)                 {this->blacklist_limit = blacklist_limit;}
    void SetCategoriesLimit(int categories_limit)               {this->categories_limit = categories_limit;}
    void SetCategorySamplesLimit(int category_samples_limit)    {this->category_samples_limit = category_samples_limit;};
    void SetReturnSourceText(int return_source_text)            {this->return_source_text = return_source_text;};
    void SetQueriesLimit(int queries_limit)                     {this->queries_limit = queries_limit;}
    void SetEntitiesLimit(int entities_limit)                   {this->entities_limit = entities_limit;}
    void SetSentimentLimit(int sentiment_limit)                 {this->sentiment_limit = sentiment_limit;}
    void SetCharactersLimit(int characters_limit)               {this->characters_limit = characters_limit;}
    void SetBatchLimit(int batch_limit)                         {this->batch_limit = batch_limit;}
	void SetCollectionLimit(int collection_limit)               {this->collection_limit = collection_limit;}
	void SetAutoResponseLimit(int auto_response_limit)          {this->auto_response_limit = auto_response_limit;}
	void SetProcessedBatchLimit(int processed_batch_limit)      {this->processed_batch_limit = processed_batch_limit;}
    void SetCallbackBatchLimit(int callback_batch_limit)        {this->callback_batch_limit = callback_batch_limit;}

    void SetSummary(bool summary)                               {this->summary = summary;}
    void SetAutoCategories(bool auto_categories)                {this->auto_categories = auto_categories;}
    void SetDocThemes(bool doc_themes)                          {this->doc_themes = doc_themes;}
    void SetDocNamedEntities(bool doc_named_entities)           {this->doc_named_entities = doc_named_entities;}
    void SetDocUserEntities(bool doc_user_entities)             {this->doc_user_entities = doc_user_entities;}
    void SetEntityThemes(bool entity_themes)                    {this->entity_themes = entity_themes;}
    void SetDocMentions(bool doc_mentions)                      {this->doc_mentions = doc_mentions;}
    void SetOpinions(bool opinions)                             {this->opinions = opinions;}
    void SetNamedRelations(bool named_relations)                {this->named_relations = named_relations;}
    void SetUserRelations(bool user_relations)                  {this->user_relations = user_relations;}
    void SetDocQueryTopics(bool doc_query_topics)               {this->doc_query_topics = doc_query_topics;}
    void SetDocConceptTopics(bool doc_concept_topics)           {this->doc_concept_topics = doc_concept_topics;}
    void SetSentimentPhrases(bool sentiment_phrases)            {this->sentiment_phrases = sentiment_phrases;}
    void SetPhrasesDetection(bool phrases_detection)            {this->phrases_detection = phrases_detection;}
    void SetPosTagging(bool pos_tagging)                        {this->pos_tagging = pos_tagging;}
    void SetLanguageDetection(bool language_detection)          {this->language_detection = language_detection;}

    void SetFacets(bool facets)                                 {this->facets = facets;}
    void SetMentions(bool mentions)                             {this->mentions = mentions;}

    void SetCollThemes(bool coll_themes)                        {this->coll_themes = coll_themes;}
    void SetCollNamedEntities(bool coll_named_entities)         {this->coll_named_entities = coll_named_entities;}
    //void SetCollUserEntities(bool coll_user_entities)           {this->coll_user_entities = coll_user_entities;}
    void SetCollQueryTopics(bool coll_query_topics)             {this->coll_query_topics = coll_query_topics;}
    void SetCollConceptTopics(bool coll_concept_topics)         {this->coll_concept_topics = coll_concept_topics;}

    void SetSupportedLanguages(string supported_languages)      {this->supported_languages = supported_languages;}
    void SetHtmlProcessing(bool html_processing)                {this->html_processing = html_processing;}


private:
    string name;
	string status;

    // billing_settings
    string priority;
    long expiration_date;
    string limit_type;
    int calls_balance;
    int calls_limit;
    int calls_limit_interval;
    int docs_balance;
    int docs_limit;
    int docs_limit_interval;
    int docs_suggested;
    int docs_suggested_interval;

    // basic_settings
    int configurations_limit;
    int output_data_limit;
    int blacklist_limit;
    int categories_limit;
    int category_samples_limit;
    bool return_source_text;
    int queries_limit;
    int entities_limit;
    int sentiment_limit;
    int characters_limit;
	int batch_limit;
	int collection_limit;
	int auto_response_limit;
	int processed_batch_limit;
    int callback_batch_limit;

    // feature setting
    //-- document
    bool summary;
    bool auto_categories;
    bool doc_themes;
    bool doc_named_entities;
    bool doc_user_entities;
    bool entity_themes;
    bool doc_mentions;
    bool opinions;
    bool named_relations;
    bool user_relations;
    bool doc_query_topics;
    bool doc_concept_topics;
    bool sentiment_phrases;
    bool phrases_detection;
    bool pos_tagging;
    bool language_detection;

    //-- collection
    bool facets;
    bool mentions;
    bool coll_themes;
    bool coll_named_entities;
    //bool coll_user_entities;
    bool coll_query_topics;
    bool coll_concept_topics;

    string supported_languages;
    bool html_processing;
};

#endif // SUBSCRIPTION_H
