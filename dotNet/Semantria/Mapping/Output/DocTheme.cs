using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "theme")]
    [XmlRootAttribute("theme", Namespace = "")]
    public class DocTheme
    {
        [DataMember(Name = "evidence")]
        [XmlElementAttribute("evidence")]
        public int Evidence { get; set; }

        [DataMember(Name = "is_about")]
        [XmlElementAttribute("is_about")]
        public bool IsAbout { get; set; }

        [DataMember(Name = "strength_score")]
        [XmlElementAttribute("strength_score")]
        public float StrengthScore { get; set; }

        [DataMember(Name = "sentiment_score")]
        [XmlElementAttribute("sentiment_score")]
        public float SentimentScore { get; set; }

        [DataMember(Name = "sentiment_polarity")]
        [XmlElementAttribute("sentiment_polarity")]
        public string SentimentPolarity { get; set; }

        [DataMember(Name = "title")]
        [XmlElementAttribute("title")]
        public string Title { get; set; }

		[DataMember(Name = "mentions")]
		[XmlArrayAttribute("mentions")]
		[XmlArrayItemAttribute("mention", typeof(DocMention))]
		public List<DocMention> Mentions { get; set; }
    }
}
