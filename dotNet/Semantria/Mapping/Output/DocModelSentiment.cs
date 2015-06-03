using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;
using Semantria.Com.Mapping.Output;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "model_sentiment")]
    [XmlRootAttribute("model_sentiment", Namespace = "")]
    public class DocModelSentiment
    {
        [DataMember(Name = "model_name")]
        [XmlElementAttribute("model_name")]
        public string ModelName { get; set; }

        [DataMember(Name = "mixed_score")]
        [XmlElementAttribute("mixed_score")]
        public float MixedScore { get; set; }

        [DataMember(Name = "negative_score")]
        [XmlElementAttribute("negative_score")]
        public float NegativeScore { get; set; }

        [DataMember(Name = "neutral_score")]
        [XmlElementAttribute("neutral_score")]
        public float NeutralScore { get; set; }

        [DataMember(Name = "positive_score")]
        [XmlElementAttribute("positive_score")]
        public float PositiveScore { get; set; }

        [DataMember(Name = "sentiment_polarity")]
        [XmlElementAttribute("sentiment_polarity")]
        public string SentimentPolarity { get; set; }
    }
}
