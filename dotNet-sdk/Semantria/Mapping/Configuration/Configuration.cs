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
        [DataMember(Name = "config_id", EmitDefaultValue=false)]
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
        
        [DataMember(Name = "is_primary")]
        [XmlElementAttribute("is_primary")]
        public bool IsPrimary { get; set; }

        [DataMember(Name = "auto_responding")]
        [XmlElementAttribute("auto_responding")]
        public bool AutoResponding { get; set; }

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

        [DataMember(Name = "user_entities_limit")]
        [XmlElementAttribute("user_entities_limit")]
        public int UserEntitiesLimit { get; set; }

        [DataMember(Name = "entity_themes_limit")]
        [XmlElementAttribute("entity_themes_limit")]
        public int EntityThemesLimit { get; set; }

        [DataMember(Name = "themes_limit")]
        [XmlElementAttribute("themes_limit")]
        public int ThemesLimit { get; set; }

        [DataMember(Name = "phrases_limit")]
        [XmlElementAttribute("phrases_limit")]
        public int PhrasesLimit { get; set; }

        [DataMember(Name = "summary_limit")]
        [XmlElementAttribute("summary_limit")]
        public int SummaryLimit { get; set; }
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
    }
}

namespace Semantria.Com.Mapping.Configuration.Stub
{
    [DataContract(Name = "configurations")]
    [XmlRootAttribute("configurations", Namespace = "")]
    public class Configurations
    {
        [DataMember(Name = "configuration")]
        [XmlElementAttribute("configuration")]
        public List<Configuration> Data { get; set; }
    }

    [DataContract(Name = "configurations")]
    [XmlRootAttribute("configurations", Namespace = "")]
    public class ConfigurationManagable : IManagable<Configuration, string>
    {
        private List<Configuration> _added = new List<Configuration>();
        private List<string> _removed = new List<string>();

        #region IManagable<T,U> Members

        [DataMember(Name = "added")]
        [XmlArrayAttribute("added")]
        [XmlArrayItemAttribute("configuration", typeof(Configuration))]
        public List<Configuration> Added
        {
            get
            {
                return _added;
            }
            set
            {
                _added = value;
            }
        }

        [DataMember(Name = "removed")]
        [XmlArrayAttribute("removed")]
        [XmlArrayItemAttribute("configuration", typeof(string))]
        public List<string> Removed
        {
            get
            {
                return _removed;
            }
            set
            {
                _removed = value;
            }
        }

        #endregion
    }
}
