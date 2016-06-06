using System;
using System.Runtime.Serialization;
using System.Collections.Generic;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Configuration
{
    [DataContract(Name = "configuration")]
    [XmlRootAttribute("configuration", Namespace = "")]
    public class Configuration
    {
        [DataMember(Name = "id", EmitDefaultValue = false)]
        [XmlElementAttribute("id")]
        public string Id { get; set; }

        [DataMember(Name = "modified", EmitDefaultValue = false)]
        [XmlElementAttribute("modified")]
        public UInt64 Modified { get; set; }

        [DataMember(Name = "name")]
        [XmlElementAttribute("name")]
        public string Name { get; set; }

        [DataMember(Name = "language", EmitDefaultValue = false)]
        [XmlElementAttribute("language")]
        public string Language { get; set; }

        [DataMember(Name = "template", EmitDefaultValue = false)]
        [XmlElementAttribute("template")]
        public string Template { get; set; }

        [DataMember(Name = "from_template_config_id", EmitDefaultValue = false)]
        [XmlElementAttribute("from_template_config_id")]
        public string FromTemplateId { get; set; }

        [DataMember(Name = "version", EmitDefaultValue = false)]
        [XmlElementAttribute("version")]
        public string Version { get; set; }

        [DataMember(Name = "is_primary")]
        [XmlElementAttribute("is_primary")]
        public bool IsPrimary { get; set; }

        [DataMember(Name = "auto_response")]
        [XmlElementAttribute("auto_response")]
        public bool AutoResponse { get; set; }

        [DataMember(Name = "alphanumeric_threshold")]
        [XmlElementAttribute("alphanumeric_threshold")]
        public int AlphanumericThreshold { get; set; }

        [DataMember(Name = "concept_topics_threshold")]
        [XmlElementAttribute("concept_topics_threshold")]
        public float ConceptTopicsThreshold { get; set; }

        [DataMember(Name = "entities_threshold")]
        [XmlElementAttribute("entities_threshold")]
        public int EntitiesThreshold { get; set; }

        [DataMember(Name = "one_sentence_mode")]
        [XmlElementAttribute("one_sentence_mode")]
        public bool OneSentenceMode { get; set; }

        [DataMember(Name = "process_html")]
        [XmlElementAttribute("process_html")]
        public bool ProcessHtml { get; set; }

        [DataMember(Name = "callback", EmitDefaultValue = false)]
        [XmlElementAttribute("callback")]
        public string Callback { get; set; }

        [DataMember(Name = "document", EmitDefaultValue = false)]
        [XmlElementAttribute("document", typeof(DocumentConfiguration))]
        public DocumentConfiguration Document { get; set; }

        [DataMember(Name = "collection", EmitDefaultValue = false)]
        [XmlElementAttribute("collection", typeof(CollectionConfiguration))]
        public CollectionConfiguration Collection { get; set; }

        public override string ToString()
        {
            return this.Name;
        }
    }

    [DataContract(Name = "document")]
    [XmlRootAttribute("document", Namespace = "")]
    public class DocumentConfiguration
    {
        [DataMember(Name = "summary_size")]
        [XmlElementAttribute("summary_size")]
        public int SummarySize { get; set; }

        [DataMember(Name = "detect_language")]
        [XmlElementAttribute("detect_language")]
        public bool DetectLanguage { get; set; }

        [DataMember(Name = "pos_types")]
        [XmlElementAttribute("pos_types")]
        public string PosTypes { get; set; }

        [DataMember(Name = "auto_categories")]
        [XmlElementAttribute("auto_categories")]
        public bool AutoCategories { get; set; }

        [DataMember(Name = "sentiment_phrases")]
        [XmlElementAttribute("sentiment_phrases")]
        public bool SentimentPhrases { get; set; }

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

        [DataMember(Name = "opinions")]
        [XmlElementAttribute("opinions")]
        public bool Opinions { get; set; }

        [DataMember(Name = "relations")]
        [XmlElementAttribute("relations")]
        public bool Relations { get; set; }

        [DataMember(Name = "query_topics")]
        [XmlElementAttribute("query_topics")]
        public bool QueryTopics { get; set; }

        [DataMember(Name = "concept_topics")]
        [XmlElementAttribute("concept_topics")]
        public bool ConceptTopics { get; set; }

        [DataMember(Name = "model_sentiment")]
        [XmlElementAttribute("model_sentiment")]
        public bool ModelSentiment { get; set; }

        [DataMember(Name = "intentions")]
        [XmlElementAttribute("intentions")]
        public bool Intentions { get; set; }
    }

    [DataContract(Name = "collection")]
    [XmlRootAttribute("collection", Namespace = "")]
    public class CollectionConfiguration
    {
        [DataMember(Name = "themes")]
        [XmlElementAttribute("themes")]
        public bool Themes { get; set; }

        [DataMember(Name = "mentions")]
        [XmlElementAttribute("mentions")]
        public bool Mentions { get; set; }

        [DataMember(Name = "facets")]
        [XmlElementAttribute("facets")]
        public bool Facets { get; set; }

        [DataMember(Name = "attributes")]
        [XmlElementAttribute("attributes")]
        public bool Attributes { get; set; }

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

namespace Semantria.Com.Mapping.Configuration.Stub
{
    [DataContract(Name = "configurations")]
    [XmlRootAttribute("configurations", Namespace = "")]
    public class Configurations : IStub<Configuration>
    {
        public Configurations()
        {
        }

        public Configurations(List<Configuration> items)
        {
            this.Data = items;
        }
        
        [XmlElementAttribute("configuration")]
        public List<Configuration> Data { get; set; }

        private List<string> _keys = null;
        [DataMember(Name = "fbe947eeb47")]
        [XmlElementAttribute("fbe947eeb47")]
        public List<string> Keys 
        {
            get 
            {
                return _keys;
            }
        }

        public void ToKeys()
        {
            _keys = this.Data.ConvertAll<string>(new Converter<Configuration, string>(ItemKey));
            this.Data = null;
        }

        public string ConvertTag(string data)
        {
            return data.Replace("<fbe947eeb47>", "<id>").Replace("</fbe947eeb47>", "</id>");
        }

        private string ItemKey(Configuration item)
        {
            return item.Id;
        }
    }
}
