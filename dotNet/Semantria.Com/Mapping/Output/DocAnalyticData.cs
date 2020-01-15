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

        [DataMember(Name = "tag")]
        [XmlElementAttribute("tag")]
        public string Tag { get; set; }

        [DataMember(Name = "config_id")]
        [XmlElementAttribute("config_id")]
        public string ConfigId { get; set; }

        [DataMember(Name = "job_id")]
        [XmlElementAttribute("job_id")]
        public string JobId { get; set; }

        [DataMember(Name = "metadata")]
        [XmlElementAttribute("metadata")]
        public string Metadata { get; set; }

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

        [DataMember(Name = "model_sentiment")]
        [XmlElementAttribute("model_sentiment")]
        public DocModelSentiment ModelSentiment { get; set; }

        [DataMember(Name = "language")]
        [XmlElementAttribute("language")]
        public string Language { get; set; }

		[DataMember(Name = "language_score")]
		[XmlElementAttribute("language_score")]
		public string LanguageScore { get; set; }

        [DataMember(Name = "intentions")]
        [XmlArrayAttribute("intentions")]
        [XmlArrayItemAttribute("intention", typeof(DocIntention))]
        public List<DocIntention> Intentions { get; set; }

        [DataMember(Name = "entities")]
        [XmlArrayAttribute("entities")]
        [XmlArrayItemAttribute("entity", typeof(DocEntity))]
        public List<DocEntity> Entities { get; set; }

        [DataMember(Name = "topics")]
        [XmlArrayAttribute("topics")]
        [XmlArrayItemAttribute("topic", typeof(Topic))]
        public List<Topic> Topics { get; set; }

        [DataMember(Name = "auto_categories")]
        [XmlArrayAttribute("auto_categories")]
        [XmlArrayItemAttribute("category", typeof(Topic))]
        public List<Topic> AutoCategories { get; set; }

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

		[DataMember(Name = "opinions")]
		[XmlArrayAttribute("opinions")]
		[XmlArrayItemAttribute("opinion", typeof(DocOpinion))]
		public List<DocOpinion> Opinions { get; set; }

        [DataMember(Name = "details")]
        [XmlArrayAttribute("details")]
        [XmlArrayItemAttribute("detail", typeof(DocSentence))]
        public List<DocSentence> Details { get; set; }

        [DataMember(Name = "taxonomy")]
        [XmlElementAttribute("taxonomy")]
        [XmlArrayItemAttribute("topic", typeof(Topic))]
        public List<Topic> Taxonomy { get; set; }
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
}
