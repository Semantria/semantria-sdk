using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "topic")]
    [XmlRootAttribute("topic", Namespace = "")]
    public class DocTopic
    {
        [DataMember(Name = "title")]
        [XmlElementAttribute("title")]
        public string Title { get; set; }

        [DataMember(Name = "type")]
        [XmlElementAttribute("type")]
        public string Type { get; set; }

        [DataMember(Name = "hitcount")]
        [XmlElementAttribute("hitcount")]
        public int Hitcount { get; set; }

        [DataMember(Name = "strength_score")]
        [XmlElementAttribute("strength_score")]
        public float StrengthScore { get; set; }

        [DataMember(Name = "sentiment_score")]
        [XmlElementAttribute("sentiment_score")]
        public float SentimentScore { get; set; }

        [DataMember(Name = "sentiment_polarity")]
        [XmlElementAttribute("sentiment_polarity")]
        public string SentimentPolarity { get; set; }

        [DataMember(Name = "label")]
        [XmlElementAttribute("label")]
        public string Label { get; set; }
    }
}
