using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "word")]
    [XmlRootAttribute("word", Namespace = "")]
    public class DocWord
    {
        [DataMember(Name = "tag")]
        [XmlElementAttribute("tag")]
        public string Tag { get; set; }

        [DataMember(Name = "title")]
        [XmlElementAttribute("title")]
        public string Title { get; set; }
        
        [DataMember(Name = "stremmed")]
        [XmlElementAttribute("stremmed")]
        public string Stremmed { get; set; }

        [DataMember(Name = "sentiment_score")]
        [XmlElementAttribute("sentiment_score")]
        public float SentimentScore { get; set; }

        [DataMember(Name = "is_negated")]
        [XmlElementAttribute("is_negated")]
        public bool IsNegated { get; set; }
    }
}

