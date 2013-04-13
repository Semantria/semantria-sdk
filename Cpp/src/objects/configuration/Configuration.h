#ifndef CONFIGURATION_H
#define CONFIGURATION_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../serializers/xml/XmlSerializable.h"

using namespace std;

class Configuration: public JsonSerializable {
public:
    Configuration();
    virtual ~Configuration();

    virtual void Serialize(Json::Value& root);
    virtual void Deserialize(Json::Value& root);

    string GetId() {return config_id;}
    string GetName() {return name;}
    string GetTemplate() {return templateId;}
    bool GetOneSentence() {return one_sentence;}
    bool GetAutoResponding() {return auto_response;}
    bool GetIsPrimary() {return is_primary;}
    string GetLanguage() {return language;}
    int GetCharsThreshold() {return chars_threshold;}
    string GetCallback() {return callback;}
    int GetEntityThemesLimit() {return entity_themes_limit;}
    int GetSummaryLimit() {return summary_limit;}
    int GetPhrasesLimit() {return phrases_limit;}
    int GetDocThemesLimit() {return doc_themes_limit;}
    int GetDocQueryTopicsLimit() {return doc_query_topics_limit;}
    int GetDocConceptTopicsLimit() {return doc_concept_topics_limit;}
    int GetDocNamedEntitiesLimit() {return doc_named_entities_limit;}
    int GetUserEntitiesLimit() {return user_entities_limit;}
    int GetFacetsLimit() {return facets_limit;}
    int GetFacetAttributesLimit() {return facet_atts_limit;}
    int GetAttributeMentionsLimit() {return attribute_mentions_limit;}
    int GetCollThemesLimit() {return coll_themes_limit;}
    int GetCollQueryTopicsLimit() {return coll_query_topics_limit;}
    int GetCollConceptTopicsLimit() {return coll_concept_topics_limit;}
    int GetCollNamedEntitiesLimit() {return coll_named_entities_limit;}

    void SetId(string id) {config_id = id;}
    void SetName(string name) {this->name = name;}
    void SetTemplate(string templateId) {this->templateId = templateId;}
    void SetAutoResponding(bool auto_response) {this->auto_response = auto_response;}
    void SetOneSentence(bool one_sentence) {this->one_sentence = one_sentence;}
    void SetIsPrimary(bool is_primary) {this->is_primary = is_primary;}
    void SetLanguage(string language) {this->language = language;}
    void SetCharsThreshold(int chars_threshold) {this->chars_threshold = chars_threshold;}
    void SetCallback(string callback) {this->callback = callback;}
    void SetEntityThemesLimit(int entity_themes_limit) {this->entity_themes_limit = entity_themes_limit;}
    void SetSummaryLimit(int summary_limit) {this->summary_limit = summary_limit;}
    void SetPhrasesLimit(int phrases_limit) {this->phrases_limit = phrases_limit;}
    void SetDocThemesLimit(int doc_themes_limit) {this->doc_themes_limit = doc_themes_limit;}
    void SetDocQueryTopicsLimit(int doc_query_topics_limit) {this->doc_query_topics_limit = doc_query_topics_limit;}
    void SetDocConceptTopicsLimit(int doc_concept_topics_limit) {this->doc_concept_topics_limit = doc_concept_topics_limit;}
    void SetDocNamedEntitiesLimit(int doc_named_entities_limit) {this->doc_named_entities_limit = doc_named_entities_limit;}
    void SetUserEntitiesLimit(int user_entities_limit) {this->user_entities_limit = user_entities_limit;}
    void SetFacetsLimit(int facets_limit) {this->facets_limit = facets_limit;}
    void SetFacetAttributesLimit(int facet_atts_limit) {this->facet_atts_limit = facet_atts_limit;}
    void SetFacetMentionsLimit(int facet_mentions_limit) {this->facet_mentions_limit = facet_mentions_limit;}
    void SetAttributeMentionsLimit(int attribute_mentions_limit) {this->attribute_mentions_limit = attribute_mentions_limit;}

    void SetCollThemesLimit(int coll_themes_limit) {this->coll_themes_limit = coll_themes_limit;}
    void SetCollQueryTopicsLimit(int coll_query_topics_limit) {this->coll_query_topics_limit = coll_query_topics_limit;}
    void SetCollConceptTopicsLimit(int coll_concept_topics_limit) {this->coll_concept_topics_limit = coll_concept_topics_limit;}
    void SetCollNamedEntitiesLimit(int coll_named_entities_limit) {this->coll_named_entities_limit = coll_named_entities_limit;}


private:
    string config_id;
    string name;
    string templateId;
    bool auto_response;
    bool is_primary;
    bool one_sentence;
    string language;
    int chars_threshold;
    string callback;

    //document
    int doc_concept_topics_limit;
    int doc_query_topics_limit;
    int doc_named_entities_limit;
    int user_entities_limit;
    int entity_themes_limit;
    int doc_themes_limit;
    int named_relations_limit;
    int user_relations_limit;
    int phrases_limit;
    int possible_phrases_limit;
    string pos_types;
    int summary_limit;
    bool detect_language;



    //collection
    int facets_limit;
    int facet_atts_limit;
    int facet_mentions_limit;
    int attribute_mentions_limit;
    int coll_concept_topics_limit;
    int coll_query_topics_limit;
    int coll_named_entities_limit;
    int coll_themes_limit;

    //
};

#endif // CONFIGURATION_H
