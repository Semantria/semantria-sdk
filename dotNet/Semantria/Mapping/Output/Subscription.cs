using System;
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
        [DataMember(Name = "priority")]
        [XmlElementAttribute("priority")]
        public string Priority { get; set; }

        [DataMember(Name = "expiration_date")]
        [XmlElementAttribute("expiration_date")]
        public long ExpirationDate { get; set; }

        [DataMember(Name = "limit_type")]
        [XmlElementAttribute("limit_type")]
        public string LimitType { get; set; }

        [DataMember(Name = "calls_balance")]
        [XmlElementAttribute("calls_balance")]
        public int CallsBalance { get; set; }

        [DataMember(Name = "calls_limit")]
        [XmlElementAttribute("calls_limit")]
        public int CallsLimit { get; set; }

        [DataMember(Name = "calls_limit_interval")]
        [XmlElementAttribute("calls_limit_interval")]
        public int CallsLimitInterval { get; set; }

        [DataMember(Name = "docs_balance")]
        [XmlElementAttribute("docs_balance")]
        public int DocsBalance { get; set; }

        [DataMember(Name = "docs_limit")]
        [XmlElementAttribute("docs_limit")]
        public int DocsLimit { get; set; }

        [DataMember(Name = "docs_limit_interval")]
        [XmlElementAttribute("docs_limit_interval")]
        public int DocsLimitInterval { get; set; }

		[DataMember(Name = "docs_suggested")]
		[XmlElementAttribute("docs_suggested")]
		public int DocsSuggested{ get; set; }

		[DataMember(Name = "docs_suggested_interval")]
		[XmlElementAttribute("docs_suggested_interval")]
		public int DocsSuggestedInterval { get; set; }
    }

    [DataContract(Name = "basic_settings")]
    [XmlRootAttribute("basic_settings", Namespace = "")]
    public class BasicSettings
    {
        [DataMember(Name = "configurations_limit")]
        [XmlElementAttribute("configurations_limit")]
        public int ConfigurationsLimit { get; set; }

        [DataMember(Name = "blacklist_limit")]
        [XmlElementAttribute("blacklist_limit")]
        public int BlacklistLimit { get; set; }

        [DataMember(Name = "categories_limit")]
        [XmlElementAttribute("categories_limit")]
        public int CategoriesLimit { get; set; }

		[DataMember(Name = "category_samples_limit")]
		[XmlElementAttribute("category_samples_limit")]
		public int CategorySamplesLimit { get; set; }

        [DataMember(Name = "queries_limit")]
        [XmlElementAttribute("queries_limit")]
        public int QueriesLimit { get; set; }

        [DataMember(Name = "entities_limit")]
        [XmlElementAttribute("entities_limit")]
        public int EntitiesLimit { get; set; }

        [DataMember(Name = "sentiment_limit")]
        [XmlElementAttribute("sentiment_limit")]
        public int SentimentLimit { get; set; }

        [DataMember(Name = "characters_limit")]
        [XmlElementAttribute("characters_limit")]
        public int CharactersLimit { get; set; }

        [DataMember(Name = "batch_limit")]
        [XmlElementAttribute("batch_limit")]
        public int BatchLimit { get; set; }

        [DataMember(Name = "collection_limit")]
        [XmlElementAttribute("collection_limit")]
        public int CollectionLimit { get; set; }

        [DataMember(Name = "auto_response_limit")]
        [XmlElementAttribute("auto_response_limit")]
        public int AutoResponseLimit { get; set; }

        [DataMember(Name = "processed_batch_limit")]
        [XmlElementAttribute("processed_batch_limit")]
        public int ProcessedBatchLimit { get; set; }

        [DataMember(Name = "callback_batch_limit")]
        [XmlElementAttribute("callback_batch_limit")]
        public int CallbackBatchLimit { get; set; }

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
    }

    [DataContract(Name = "document")]
    [XmlRootAttribute("document", Namespace = "")]
    public class DocsFeatureSettings
    {
        [DataMember(Name = "summary")]
        [XmlElementAttribute("summary")]
        public bool Summary { get; set; }

        [DataMember(Name = "auto_categories")]
        [XmlElementAttribute("auto_categories")]
        public bool AutoCategories { get; set; }

        [DataMember(Name = "themes")]
        [XmlElementAttribute("themes")]
        public bool Themes { get; set; }

        [DataMember(Name = "named_entities")]
        [XmlElementAttribute("named_entities")]
        public bool NamedEntities { get; set; }

        [DataMember(Name = "user_entities")]
        [XmlElementAttribute("user_entities")]
        public bool UserEntities { get; set; }

        [DataMember(Name = "entity_themes")]
        [XmlElementAttribute("entity_themes")]
        public bool EntityThemes { get; set; }

		[DataMember(Name = "mentions")]
		[XmlElementAttribute("mentions")]
		public bool Mentions { get; set; }

		[DataMember(Name = "opinions")]
		[XmlElementAttribute("opinions")]
		public bool Opinions { get; set; }

        [DataMember(Name = "named_relations")]
        [XmlElementAttribute("named_relations")]
        public bool NamedRelations { get; set; }

        [DataMember(Name = "user_relations")]
        [XmlElementAttribute("user_relations")]
        public bool UserRelations { get; set; }

        [DataMember(Name = "query_topics")]
        [XmlElementAttribute("query_topics")]
        public bool QueryTopics { get; set; }

        [DataMember(Name = "concept_topics")]
        [XmlElementAttribute("concept_topics")]
        public bool ConceptTopics { get; set; }

        [DataMember(Name = "sentiment_phrases")]
        [XmlElementAttribute("sentiment_phrases")]
        public bool SentimentPhrases { get; set; }

        [DataMember(Name = "phrases_detection")]
        [XmlElementAttribute("phrases_detection")]
        public bool PhrasesDetection { get; set; }

        [DataMember(Name = "pos_tagging")]
        [XmlElementAttribute("pos_tagging")]
        public bool PosTagging { get; set; }

        [DataMember(Name = "language_detection")]
        [XmlElementAttribute("language_detection")]
        public bool LanguageDetection { get; set; }
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

        [DataMember(Name = "named_entities")]
        [XmlElementAttribute("named_entities")]
        public bool NamedEntities { get; set; }

        [DataMember(Name = "mentions")]
        [XmlElementAttribute("mentions")]
        public bool Mentions { get; set; }

        [DataMember(Name = "query_topics")]
        [XmlElementAttribute("query_topics")]
        public bool QueryTopics { get; set; }

        [DataMember(Name = "concept_topics")]
        [XmlElementAttribute("concept_topics")]
        public bool ConceptTopics { get; set; }
    }
}
