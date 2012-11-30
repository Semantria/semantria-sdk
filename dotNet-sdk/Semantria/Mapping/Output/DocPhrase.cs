using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "phrase")]
    [XmlRootAttribute("phrase", Namespace = "")]
    public class DocPhrase
    {
        [DataMember(Name = "negating_phrase")]
        [XmlElementAttribute("negating_phrase")]
        public string NegatingPhrase { get; set; }

        [DataMember(Name = "is_negated")]
        [XmlElementAttribute("is_negated")]
        public bool IsNegated { get; set; }

        [DataMember(Name = "sentiment_score")]
        [XmlElementAttribute("sentiment_score")]
        public float SentimentScore { get; set; }

        [DataMember(Name = "title")]
        [XmlElementAttribute("title")]
        public string Title { get; set; }
    }
}
