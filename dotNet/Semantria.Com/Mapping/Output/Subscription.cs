using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "subscription")]
    [XmlRootAttribute("subscription", Namespace = "")]
    public class Subscription
    {
        [DataMember(Name = "name")]
        [XmlElementAttribute("name")]
        public string Name { get; set; }

        [DataMember(Name = "status")]
        [XmlElementAttribute("status")]
        public string Status { get; set; }

        [DataMember(Name = "billing_settings")]
        [XmlElementAttribute("billing_settings")]
        public BillingSettings BillingSettings { get; set; }

        [DataMember(Name = "basic_settings")]
        [XmlElementAttribute("basic_settings")]
        public BasicSettings BasicSettings { get; set; }

        [DataMember(Name = "feature_settings")]
        [XmlElementAttribute("feature_settings")]
        public FeatureSettings FeatureSettings { get; set; }
    }

    [DataContract(Name = "billing_settings")]
    [XmlRootAttribute("billing_settings", Namespace = "")]
    public class BillingSettings
    {
        [DataMember(Name = "expiration_date")]
        [XmlElementAttribute("expiration_date")]
        public long ExpirationDate { get; set; }

        [DataMember(Name = "settings_calls_balance")]
        [XmlElementAttribute("settings_calls_balance")]
        public int SettingsCallsBalance { get; set; }

        [DataMember(Name = "settings_calls_limit")]
        [XmlElementAttribute("settings_calls_limit")]
        public int SettingsCallsLimit { get; set; }

        [DataMember(Name = "settings_calls_limit_interval")]
        [XmlElementAttribute("settings_calls_limit_interval")]
        public int SettingsCallsLimitInterval { get; set; }

        [DataMember(Name = "polling_calls_balance")]
        [XmlElementAttribute("polling_calls_balance")]
        public int PollingCallsLimitBalance { get; set; }

        [DataMember(Name = "polling_calls_limit")]
        [XmlElementAttribute("polling_calls_limit")]
        public int PollingCallsLimit { get; set; }

        [DataMember(Name = "polling_calls_limit_interval")]
        [XmlElementAttribute("polling_calls_limit_interval")]
        public int PollingCallsLimitInterval { get; set; }

        [DataMember(Name = "data_calls_balance")]
        [XmlElementAttribute("data_calls_balance")]
        public int DataCallsLimitBalance { get; set; }

        [DataMember(Name = "data_calls_limit")]
        [XmlElementAttribute("data_calls_limit")]
        public int DataCallsLimit { get; set; }

        [DataMember(Name = "data_calls_limit_interval")]
        [XmlElementAttribute("data_calls_limit_interval")]
        public int DataCallsLimitInterval { get; set; }

        [DataMember(Name = "docs_balance")]
        [XmlElementAttribute("docs_balance")]
        public int DocsBalance { get; set; }

        [DataMember(Name = "job_ids_permitted")]
        [XmlElementAttribute("job_ids_permitted")]
        public int PermittedJobIds { get; set; }

        [DataMember(Name = "job_ids_allocated")]
        [XmlElementAttribute("job_ids_permitted")]
        public int AllocatedJobIds { get; set; }

        [DataMember(Name = "app_seats_permitted")]
        [XmlElementAttribute("app_seats_permitted")]
        public int PermittedAppSeats { get; set; }

        [DataMember(Name = "app_seats_allocated")]
        [XmlElementAttribute("app_seats_allocated")]
        public int AllocatedAppSeats { get; set; }
    }

    [DataContract(Name = "basic_settings")]
    [XmlRootAttribute("basic_settings", Namespace = "")]
    public class BasicSettings
    {
        [DataMember(Name = "configurations_limit")]
        [XmlElementAttribute("configurations_limit")]
        public int ConfigurationsLimit { get; set; }

        [DataMember(Name = "summary_size_limit")]
        [XmlElementAttribute("summary_size_limit")]
        public int SummarySizeLimit { get; set; }

        [DataMember(Name = "blacklist_limit")]
        [XmlElementAttribute("blacklist_limit")]
        public int BlacklistLimit { get; set; }

        [DataMember(Name = "concept_topics_limit")]
        [XmlElementAttribute("concept_topics_limit")]
        public int ConceptTopicsLimit { get; set; }

		[DataMember(Name = "concept_topic_samples_limit")]
		[XmlElementAttribute("concept_topic_samples_limit")]
		public int ConceptTopicSamplesLimit { get; set; }

        [DataMember(Name = "query_topics_limit")]
        [XmlElementAttribute("query_topics_limit")]
        public int QueryTopicsLimit { get; set; }

        [DataMember(Name = "near_operator_limit")]
        [XmlElementAttribute("near_operator_limit")]
        public int NearOperatorLimit { get; set; }

        [DataMember(Name = "near_operator_distance")]
        [XmlElementAttribute("near_operator_distance")]
        public int NearOperatorDistance { get; set; }

        [DataMember(Name = "queries_depth_limit")]
        [XmlElementAttribute("queries_depth_limit")]
        public int QueriesDepthLimit { get; set; }

        [DataMember(Name = "taxonomy_limit")]
        [XmlElementAttribute("taxonomy_limit")]
        public int TaxonomyLimit { get; set; }

        [DataMember(Name = "taxonomy_depth_limit")]
        [XmlElementAttribute("taxonomy_depth_limit")]
        public int TaxonomyDepthLimit { get; set; }

        [DataMember(Name = "taxonomy_topics_limit")]
        [XmlElementAttribute("taxonomy_topics_limit")]
        public int TaxonomyTopicsLimit { get; set; }

        [DataMember(Name = "user_entities_limit")]
        [XmlElementAttribute("user_entities_limit")]
        public int UserEntitiesLimit { get; set; }

        [DataMember(Name = "sentiment_phrases_limit")]
        [XmlElementAttribute("sentiment_phrases_limit")]
        public int SentimentPhrasesLimit { get; set; }

        [DataMember(Name = "document_length")]
        [XmlElementAttribute("document_length")]
        public int DocumentLength { get; set; }

        [DataMember(Name = "incoming_batch_limit")]
        [XmlElementAttribute("incoming_batch_limit")]
        public int IncomingBatchLimit { get; set; }

        [DataMember(Name = "collection_limit")]
        [XmlElementAttribute("collection_limit")]
        public int CollectionLimit { get; set; }

        [DataMember(Name = "auto_response_batch_limit")]
        [XmlElementAttribute("auto_response_batch_limit")]
        public int AutoResponseBatchLimit { get; set; }

        [DataMember(Name = "polling_batch_limit")]
        [XmlElementAttribute("polling_batch_limit")]
        public int PollingBatchLimit { get; set; }

        [DataMember(Name = "callback_batch_limit")]
        [XmlElementAttribute("callback_batch_limit")]
        public int CallbackBatchLimit { get; set; }

        [DataMember(Name = "metadata_limit")]
        [XmlElementAttribute("metadata_limit")]
        public int MetadataLimit { get; set; }

        [DataMember(Name = "return_source_text")]
		[XmlElementAttribute("return_source_text")]
		public bool ReturnSourceText { get; set; }
    }

    [DataContract(Name = "feature_settings")]
    [XmlRootAttribute("feature_settings", Namespace = "")]
    public class FeatureSettings
    {
        [DataMember(Name = "supported_languages")]
        [XmlElementAttribute("supported_languages")]
        public string SupportedLanguages { get; set; }

        [DataMember(Name = "html_processing")]
        [XmlElementAttribute("html_processing")]
        public bool HtmlProcessing { get; set; }

        [DataMember(Name = "document")]
        [XmlElementAttribute("document")]
        public DocsFeatureSettings Document { get; set; }

        [DataMember(Name = "collection")]
        [XmlElementAttribute("collection")]
        public CollsFeatureSettings Collection { get; set; }

        [DataMember(Name = "templates", EmitDefaultValue = false)]
        [XmlArrayAttribute("templates")]
        [XmlArrayItemAttribute("template", typeof(Template))]
        public List<Template> Templates { get; set; }
    }

    [DataContract(Name = "document")]
    [XmlRootAttribute("document", Namespace = "")]
    public class DocsFeatureSettings
    {
        [DataMember(Name = "summary")]
        [XmlElementAttribute("summary")]
        public bool Summary { get; set; }

        [DataMember(Name = "language_detection")]
        [XmlElementAttribute("language_detection")]
        public bool LanguageDetection { get; set; }

        [DataMember(Name = "sentiment_phrases")]
        [XmlElementAttribute("sentiment_phrases")]
        public bool SentimentPhrases { get; set; }

        [DataMember(Name = "model_sentiment")]
        [XmlElementAttribute("model_sentiment")]
        public bool ModelSentiment { get; set; }

        [DataMember(Name = "intentions")]
        [XmlElementAttribute("intentions")]
        public bool Intentions { get; set; }

        [DataMember(Name = "pos_tagging")]
        [XmlElementAttribute("pos_tagging")]
        public bool PosTagging { get; set; }

        [DataMember(Name = "auto_categories")]
        [XmlElementAttribute("auto_categories")]
        public bool AutoCategories { get; set; }

        [DataMember(Name = "concept_topics")]
        [XmlElementAttribute("concept_topics")]
        public bool ConceptTopics { get; set; }

        [DataMember(Name = "query_topics")]
        [XmlElementAttribute("query_topics")]
        public bool QueryTopics { get; set; }

        [DataMember(Name = "themes")]
        [XmlElementAttribute("themes")]
        public bool Themes { get; set; }

        [DataMember(Name = "named_entities")]
        [XmlElementAttribute("named_entities")]
        public bool NamedEntities { get; set; }

        [DataMember(Name = "user_entities")]
        [XmlElementAttribute("user_entities")]
        public bool UserEntities { get; set; }

		[DataMember(Name = "mentions")]
		[XmlElementAttribute("mentions")]
		public bool Mentions { get; set; }

		[DataMember(Name = "opinions")]
		[XmlElementAttribute("opinions")]
		public bool Opinions { get; set; }

        [DataMember(Name = "relations")]
        [XmlElementAttribute("relations")]
        public bool Relations { get; set; }
    }

    [DataContract(Name = "collection")]
    [XmlRootAttribute("collection", Namespace = "")]
    public class CollsFeatureSettings
    {
        [DataMember(Name = "facets")]
        [XmlElementAttribute("facets")]
        public bool Facets { get; set; }

        [DataMember(Name = "themes")]
        [XmlElementAttribute("themes")]
        public bool Themes { get; set; }

        [DataMember(Name = "mentions")]
        [XmlElementAttribute("mentions")]
        public bool Mentions { get; set; }

        [DataMember(Name = "named_entities")]
        [XmlElementAttribute("named_entities")]
        public bool NamedEntities { get; set; }

        [DataMember(Name = "user_entities")]
        [XmlElementAttribute("user_entities")]
        public bool UserEntities { get; set; }

        [DataMember(Name = "query_topics")]
        [XmlElementAttribute("query_topics")]
        public bool QueryTopics { get; set; }

        [DataMember(Name = "concept_topics")]
        [XmlElementAttribute("concept_topics")]
        public bool ConceptTopics { get; set; }
    }
}
