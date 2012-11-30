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

        [DataMember(Name = "expiration_date")]
        [XmlElementAttribute("expiration_date")]
        public long ExpirationDate { get; set; }

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

        [DataMember(Name = "configurations_limit")]
        [XmlElementAttribute("configurations_limit")]
        public int ConfigurationsLimit { get; set; }

        [DataMember(Name = "blacklist_limit")]
        [XmlElementAttribute("blacklist_limit")]
        public int BlacklistLimit { get; set; }

        [DataMember(Name = "categories_limit")]
        [XmlElementAttribute("categories_limit")]
        public int CategoriesLimit { get; set; }

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

        [DataMember(Name = "priority")]
        [XmlElementAttribute("priority")]
        public string Priority { get; set; }

        [DataMember(Name = "limit_type")]
        [XmlElementAttribute("limit_type")]
        public string LimitType { get; set; }
    }
}
