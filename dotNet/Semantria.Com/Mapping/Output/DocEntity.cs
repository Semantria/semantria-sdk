using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "entity")]
    [XmlRootAttribute("entity", Namespace = "")]
    public class DocEntity
    {
        [DataMember(Name = "type")]
        [XmlElementAttribute("type")]
        public string Type { get; set; }

        [DataMember(Name = "evidence")]
        [XmlElementAttribute("evidence")]
        public int Evidence { get; set; }

        [DataMember(Name = "confident")]
        [XmlElementAttribute("confident")]
        public bool Confident { get; set; }

        [DataMember(Name = "is_about")]
        [XmlElementAttribute("is_about")]
        public bool IsAbout { get; set; }

        [DataMember(Name = "entity_type")]
        [XmlElementAttribute("entity_type")]
        public string EntityType { get; set; }

        [DataMember(Name = "title")]
        [XmlElementAttribute("title")]
        public string Title { get; set; }

		[DataMember(Name = "label")]
		[XmlElementAttribute("label")]
		public string Label { get; set; }

        [DataMember(Name = "sentiment_score")]
        [XmlElementAttribute("sentiment_score")]
        public float SentimentScore { get; set; }

        [DataMember(Name = "sentiment_polarity")]
        [XmlElementAttribute("sentiment_polarity")]
        public string SentimentPolarity { get; set; }

		[DataMember(Name = "mentions")]
		[XmlArrayAttribute("mentions")]
		[XmlArrayItemAttribute("mention", typeof(DocMention))]
		public List<DocMention> Mentions { get; set; }

        [DataMember(Name = "themes")]
        [XmlArrayAttribute("themes")]
        [XmlArrayItemAttribute("theme", typeof(DocTheme))]
        public List<DocTheme> Themes { get; set; }
    }
}

