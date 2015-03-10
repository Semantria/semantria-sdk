using Semantria.Com.Mapping.Output;
using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "features")]
    [XmlRootAttribute("features", Namespace = "")]
    public class FeaturesSet
    {
        [DataMember(Name = "id")]
        [XmlElementAttribute("id")]
        public string Id { get; set; }

        [DataMember(Name = "language")]
        [XmlElementAttribute("language")]
        public string Language { get; set; }

        [DataMember(Name = "html_processing")]
        [XmlElementAttribute("html_processing")]
        public bool HTMLProcessing { get; set; }

        [DataMember(Name = "settings")]
        [XmlElementAttribute("settings")]
        public APISettings APISettings { get; set; }

        [DataMember(Name = "detailed_mode")]
        [XmlElementAttribute("detailed_mode")]
        public DetailedModeFeatures DetailedMode { get; set; }

        [DataMember(Name = "discovery_mode")]
        [XmlElementAttribute("discovery_mode")]
        public DiscoveryModeFeatures DiscoveryMode { get; set; }
    }

    [DataContract(Name = "settings")]
    [XmlRootAttribute("settings", Namespace = "")]
    public class APISettings
    {
        [DataMember(Name = "sentiment_phrases")]
        [XmlElementAttribute("sentiment_phrases")]
        public bool SentimentPhrases { get; set; }

        [DataMember(Name = "user_entities")]
        [XmlElementAttribute("user_entities")]
        public bool UserEntities { get; set; }

        [DataMember(Name = "user_categories")]
        [XmlElementAttribute("user_categories")]
        public bool UserCategories { get; set; }

        [DataMember(Name = "queries")]
        [XmlElementAttribute("queries")]
        public bool Queries { get; set; }

        [DataMember(Name = "blacklist")]
        [XmlElementAttribute("blacklist")]
        public bool Blacklist { get; set; }
    }

    [DataContract(Name = "detailed_mode")]
    [XmlRootAttribute("detailed_mode", Namespace = "")]
    public class DetailedModeFeatures
    {
        [DataMember(Name = "sentiment")]
        [XmlElementAttribute("sentiment")]
        public bool SentimentDetection { get; set; }

        [DataMember(Name = "sentiment_phrases")]
        [XmlElementAttribute("sentiment_phrases")]
        public bool SentimentPhrases { get; set; }

        [DataMember(Name = "pos_tagging")]
        [XmlElementAttribute("pos_tagging")]
        public bool POSTagging { get; set; }

        [DataMember(Name = "summarization")]
        [XmlElementAttribute("summarization")]
        public bool Summarization { get; set; }

        [DataMember(Name = "themes")]
        [XmlElementAttribute("themes")]
        public bool Themes { get; set; }

        [DataMember(Name = "theme_mentions")]
        [XmlElementAttribute("theme_mentions")]
        public bool ThemeMentions { get; set; }

        [DataMember(Name = "named_entities")]
        [XmlElementAttribute("named_entities")]
        public bool NamedEntities { get; set; }

        [DataMember(Name = "user_entities")]
        [XmlElementAttribute("user_entities")]
        public bool UserEntities { get; set; }

        [DataMember(Name = "entity_relations")]
        [XmlElementAttribute("entity_relations")]
        public bool EntityRelations { get; set; }

        [DataMember(Name = "entity_opinions")]
        [XmlElementAttribute("entity_opinions")]
        public bool EntityOpinions { get; set; }

        [DataMember(Name = "entity_mentions")]
        [XmlElementAttribute("entity_mentions")]
        public bool EntityMentions { get; set; }

        [DataMember(Name = "entity_themes")]
        [XmlElementAttribute("entity_themes")]
        public bool EntityThemes { get; set; }

        [DataMember(Name = "auto_categories")]
        [XmlElementAttribute("auto_categories")]
        public bool AutoCategories { get; set; }

        [DataMember(Name = "user_categories")]
        [XmlElementAttribute("user_categories")]
        public bool UserCategories { get; set; }

        [DataMember(Name = "queries")]
        [XmlElementAttribute("queries")]
        public bool Queries { get; set; }

        [DataMember(Name = "intentions")]
        [XmlElementAttribute("intentions")]
        public bool Intentions { get; set; }
    }

    [DataContract(Name = "discovery_mode")]
    [XmlRootAttribute("discovery_mode", Namespace = "")]
    public class DiscoveryModeFeatures
    {
        [DataMember(Name = "facets")]
        [XmlElementAttribute("facets")]
        public bool Facets { get; set; }

        [DataMember(Name = "facet_attributes")]
        [XmlElementAttribute("facet_attributes")]
        public bool FacetAttributes { get; set; }

        [DataMember(Name = "facet_mentioins")]
        [XmlElementAttribute("facet_mentioins")]
        public bool FacetMentions { get; set; }

        [DataMember(Name = "themes")]
        [XmlElementAttribute("themes")]
        public bool Themes { get; set; }

        [DataMember(Name = "theme_mentions")]
        [XmlElementAttribute("theme_mentions")]
        public bool ThemeMentions { get; set; }

        [DataMember(Name = "named_entities")]
        [XmlElementAttribute("named_entities")]
        public bool NamedEntities { get; set; }

        [DataMember(Name = "user_entities")]
        [XmlElementAttribute("user_entities")]
        public bool UserEntities { get; set; }

        [DataMember(Name = "entity_mentions")]
        [XmlElementAttribute("entity_mentions")]
        public bool EntityMentions { get; set; }

        [DataMember(Name = "user_categories")]
        [XmlElementAttribute("user_categories")]
        public bool UserCategories { get; set; }

        [DataMember(Name = "queries")]
        [XmlElementAttribute("queries")]
        public bool Queries { get; set; }
    }
}

namespace Semantria.Com.Mapping.Configuration.Stub
{
    [DataContract(Name = "supported_features")]
    [XmlRootAttribute("supported_features", Namespace = "")]
    public class FeaturesSetList : IStub<FeaturesSet>
    {
        public FeaturesSetList()
        {
        }

        public FeaturesSetList(List<FeaturesSet> items)
        {
            this.Data = items;
        }

        [XmlElementAttribute("features")]
        public List<FeaturesSet> Data { get; set; }

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
            _keys = this.Data.ConvertAll<string>(new Converter<FeaturesSet, string>(ItemKey));
            this.Data = null;
        }

        public string ConvertTag(string data)
        {
            return data.Replace("<fbe947eeb47>", "<features>").Replace("</fbe947eeb47>", "</features>");
        }

        private string ItemKey(FeaturesSet item)
        {
            return item.Id;
        }
    }
}