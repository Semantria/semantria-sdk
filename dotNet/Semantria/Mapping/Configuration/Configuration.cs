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
        [DataMember(Name = "config_id", EmitDefaultValue = false)]
        [XmlElementAttribute("config_id")]
        public string ConfigId { get; set; }

        [DataMember(Name = "template", EmitDefaultValue = false)]
        [XmlElementAttribute("template")]
        public string Template { get; set; }

        [DataMember(Name = "name")]
        [XmlElementAttribute("name")]
        public string Name { get; set; }

        [DataMember(Name = "one_sentence")]
        [XmlElementAttribute("one_sentence")]
        public bool OneSentence { get; set; }

        [DataMember(Name = "process_html")]
        [XmlElementAttribute("process_html")]
        public bool ProcessHtml { get; set; }
        
        [DataMember(Name = "is_primary")]
        [XmlElementAttribute("is_primary")]
        public bool IsPrimary { get; set; }

        [DataMember(Name = "auto_response")]
        [XmlElementAttribute("auto_response")]
        public bool AutoResponse { get; set; }

        [DataMember(Name = "language")]
        [XmlElementAttribute("language")]
        public string Language { get; set; }

        [DataMember(Name = "chars_threshold", EmitDefaultValue = false)]
        [XmlElementAttribute("chars_threshold")]
        public int CharsThreshold { get; set; }

        [DataMember(Name = "callback")]
        [XmlElementAttribute("callback")]
        public string Callback { get; set; }

        [DataMember(Name = "document")]
        [XmlElementAttribute("document", typeof(DocConfiguration))]
        public DocConfiguration Document { get; set; }

        [DataMember(Name = "collection")]
        [XmlElementAttribute("collection", typeof(CollConfiguration))]
        public CollConfiguration Collection { get; set; }

        public override string ToString()
        {
            return this.Name;
        }
    }

    [DataContract(Name = "document")]
    [XmlRootAttribute("document", Namespace = "")]
    public class DocConfiguration
    {
        [DataMember(Name = "concept_topics_limit")]
        [XmlElementAttribute("concept_topics_limit")]
        public int ConceptTopicsLimit { get; set; }

        [DataMember(Name = "query_topics_limit")]
        [XmlElementAttribute("query_topics_limit")]
        public int QueryTopicsLimit { get; set; }

        [DataMember(Name = "named_entities_limit")]
        [XmlElementAttribute("named_entities_limit")]
        public int NamedEntitiesLimit { get; set; }

		[DataMember(Name = "named_mentions_limit")]
		[XmlElementAttribute("named_mentions_limit")]
		public int NamedMentionsLimit { get; set; }

        [DataMember(Name = "user_entities_limit")]
        [XmlElementAttribute("user_entities_limit")]
        public int UserEntitiesLimit { get; set; }

        [DataMember(Name = "entity_themes_limit")]
        [XmlElementAttribute("entity_themes_limit")]
        public int EntityThemesLimit { get; set; }

		[DataMember(Name = "user_mentions_limit")]
		[XmlElementAttribute("user_mentions_limit")]
		public int UserMentionsLimit { get; set; }

        [DataMember(Name = "themes_limit")]
        [XmlElementAttribute("themes_limit")]
        public int ThemesLimit { get; set; }

		[DataMember(Name = "theme_mentions_limit")]
		[XmlElementAttribute("theme_mentions_limit")]
		public int ThemeMentionsLimit { get; set; }

        [DataMember(Name = "phrases_limit")]
        [XmlElementAttribute("phrases_limit")]
        public int PhrasesLimit { get; set; }

        [DataMember(Name = "possible_phrases_limit")]
        [XmlElementAttribute("possible_phrases_limit")]
        public int PossiblePhrasesLimit { get; set; }

        [DataMember(Name = "summary_limit")]
        [XmlElementAttribute("summary_limit")]
        public int SummaryLimit { get; set; }

        [DataMember(Name = "named_relations_limit")]
        [XmlElementAttribute("named_relations_limit")]
        public int NamedRelationsLimit { get; set; }

        [DataMember(Name = "user_relations_limit")]
        [XmlElementAttribute("user_relations_limit")]
        public int UserRelationsLimit { get; set; }

		[DataMember(Name = "named_opinions_limit")]
		[XmlElementAttribute("named_opinions_limit")]
		public int NamedOpinionsLimit { get; set; }

		[DataMember(Name = "user_opinions_limit")]
		[XmlElementAttribute("user_opinions_limit")]
		public int UserOpinionsLimit { get; set; }

        [DataMember(Name = "pos_types")]
        [XmlElementAttribute("pos_types")]
        public string PosTypes { get; set; }

        [DataMember(Name = "detect_language")]
        [XmlElementAttribute("detect_language")]
        public bool DetectLanguage { get; set; }

        [DataMember(Name = "auto_categories_limit")]
        [XmlElementAttribute("auto_categories_limit")]
        public int AutoCategoriesLimit { get; set; }
    }

    [DataContract(Name = "collection")]
    [XmlRootAttribute("collection", Namespace = "")]
    public class CollConfiguration
    {
        [DataMember(Name = "facets_limit")]
        [XmlElementAttribute("facets_limit")]
        public int FacetsLimit { get; set; }

        [DataMember(Name = "facet_atts_limit")]
        [XmlElementAttribute("facet_atts_limit")]
        public int FacetAttsLimit { get; set; }

        [DataMember(Name = "facet_mentions_limit")]
        [XmlElementAttribute("facet_mentions_limit")]
        public int FacetMentionsLimit { get; set; }

        [DataMember(Name = "attribute_mentions_limit")]
        [XmlElementAttribute("attribute_mentions_limit")]
        public int AttributeMentionsLimit { get; set; }

        [DataMember(Name = "concept_topics_limit")]
        [XmlElementAttribute("concept_topics_limit")]
        public int ConceptTopicsLimit { get; set; }

        [DataMember(Name = "query_topics_limit")]
        [XmlElementAttribute("query_topics_limit")]
        public int QueryTopicsLimit { get; set; }

        [DataMember(Name = "named_entities_limit")]
        [XmlElementAttribute("named_entities_limit")]
        public int NamedEntitiesLimit { get; set; }

        [DataMember(Name = "themes_limit")]
        [XmlElementAttribute("themes_limit")]
        public int ThemesLimit { get; set; }

        [DataMember(Name = "theme_mentions_limit")]
        [XmlElementAttribute("theme_mentions_limit")]
        public int ThemeMentionsLimit { get; set; }    }
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
            return data.Replace("<fbe947eeb47>", "<configuration>").Replace("</fbe947eeb47>", "</configuration>");
        }

        private string ItemKey(Configuration item)
        {
            return item.ConfigId;
        }
    }
}
