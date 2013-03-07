using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "document")]
    [XmlRootAttribute("document", Namespace = "")]
    public class DocAnalyticData
    {
        [DataMember(Name = "id")]
        [XmlElementAttribute("id")]
        public string Id { get; set; }

        [DataMember(Name = "config_id")]
        [XmlElementAttribute("config_id")]
        public string ConfigId { get; set; }

        [DataMember(Name = "status")]
        private string _status;
        [XmlElementAttribute("status")]
        public TaskStatus Status
        {
            get 
            {
                if (!String.IsNullOrEmpty(_status))
                {
                    return (TaskStatus)Enum.Parse(typeof(TaskStatus), _status, true); 
                }
                else
                {
                    return TaskStatus.UNDEFINED;
                }
            }
            set { _status = value.ToString(); }
        }

        [DataMember(Name = "summary")]
        [XmlElementAttribute("summary")]
        public string Summary { get; set; }

        [DataMember(Name = "source_text")]
        [XmlElementAttribute("source_text")]
        public string SourceText { get; set; }

        [DataMember(Name = "sentiment_score")]
        [XmlElementAttribute("sentiment_score")]
        public float SentimentScore { get; set; }

        [DataMember(Name = "sentiment_polarity")]
        [XmlElementAttribute("sentiment_polarity")]
        public string SentimentPolarity { get; set; }

        [DataMember(Name = "language")]
        [XmlElementAttribute("language")]
        public string Language { get; set; }

        [DataMember(Name = "entities")]
        [XmlArrayAttribute("entities")]
        [XmlArrayItemAttribute("entity", typeof(DocEntity))]
        public List<DocEntity> Entities { get; set; }

        [DataMember(Name = "topics")]
        [XmlArrayAttribute("topics")]
        [XmlArrayItemAttribute("topic", typeof(DocTopic))]
        public List<DocTopic> Topics { get; set; }

        [DataMember(Name = "themes")]
        [XmlArrayAttribute("themes")]
        [XmlArrayItemAttribute("theme", typeof(DocTheme))]
        public List<DocTheme> Themes { get; set; }

        [DataMember(Name = "phrases")]
        [XmlArrayAttribute("phrases")]
        [XmlArrayItemAttribute("phrase", typeof(DocPhrase))]
        public List<DocPhrase> Phrases { get; set; }

        [DataMember(Name = "relations")]
        [XmlArrayAttribute("relations")]
        [XmlArrayItemAttribute("relation", typeof(DocRelation))]
        public List<DocRelation> Relations { get; set; }

        [DataMember(Name = "details")]
        [XmlArrayAttribute("details")]
        [XmlArrayItemAttribute("detail", typeof(DocDetail))]
        public List<DocDetail> Details { get; set; }
    }

    [DataContract(Name = "collection")]
    [XmlRootAttribute("collection", Namespace = "")]
    public class CollAnalyticData
    {
        [DataMember(Name = "id")]
        [XmlElementAttribute("id")]
        public string Id { get; set; }

        [DataMember(Name = "config_id")]
        [XmlElementAttribute("config_id")]
        public string ConfigId { get; set; }

        [DataMember(Name = "status")]
        private string _status;
        [XmlElementAttribute("status")]
        public TaskStatus Status
        {
            get
            {
                if (!String.IsNullOrEmpty(_status))
                {
                    return (TaskStatus)Enum.Parse(typeof(TaskStatus), _status, true);
                }
                else
                {
                    return TaskStatus.UNDEFINED;
                }
            }
            set { _status = value.ToString(); }
        }

        [DataMember(Name = "facets")]
        [XmlArrayAttribute("facets")]
        [XmlArrayItemAttribute("facet", typeof(Facet))]
        public List<Facet> Facets { get; set; }

        [DataMember(Name = "themes")]
        [XmlArrayAttribute("themes")]
        [XmlArrayItemAttribute("theme", typeof(CollTheme))]
        public List<CollTheme> Themes { get; set; }

        [DataMember(Name = "topics")]
        [XmlArrayAttribute("topics")]
        [XmlArrayItemAttribute("topic", typeof(CollTopic))]
        public List<CollTopic> Topics { get; set; }

        [DataMember(Name = "entities")]
        [XmlArrayAttribute("entities")]
        [XmlArrayItemAttribute("entity", typeof(CollEntity))]
        public List<CollEntity> Entities { get; set; }
    }
}

namespace Semantria.Com.Mapping.Output.Stub
{
    [DataContract(Name = "documents")]
    [XmlRootAttribute("documents", Namespace = "")]
    public class DocAnalyticsData
    {
        [DataMember(Name = "document")]
        [XmlElementAttribute("document")]
        public List<DocAnalyticData> Data { get; set; }
    }

    [DataContract(Name = "collections")]
    [XmlRootAttribute("collections", Namespace = "")]
    public class CollAnalyticsData
    {
        [DataMember(Name = "collection")]
        [XmlElementAttribute("collection")]
        public List<CollAnalyticData> Data { get; set; }
    }
}
