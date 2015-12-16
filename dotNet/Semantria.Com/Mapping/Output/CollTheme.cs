using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "theme")]
    [XmlRootAttribute("theme", Namespace = "")]
    public class CollTheme
    {
        [DataMember(Name = "phrases_count")]
        [XmlElementAttribute("phrases_count")]
        public int PhrasesCount { get; set; }

        [DataMember(Name = "themes_count")]
        [XmlElementAttribute("themes_count")]
        public int ThemesCount { get; set; }

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
        [XmlArrayItemAttribute("mention", typeof(CollMention))]
        public List<CollMention> Mentions { get; set; }
    }
}

