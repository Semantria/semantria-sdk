using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "phrase")]
    [XmlRootAttribute("phrase", Namespace = "")]
    public class DocPhrase
    {
        [DataMember(Name = "title")]
        [XmlElementAttribute("title")]
        public string Title { get; set; }

        [DataMember(Name = "type")]
        [XmlElementAttribute("type")]
        public string Type { get; set; }

        [DataMember(Name = "is_negated")]
        [XmlElementAttribute("is_negated")]
        public bool IsNegated { get; set; }

        [DataMember(Name = "negating_phrase")]
        [XmlElementAttribute("negating_phrase")]
        public string NegatingPhrase { get; set; }

        [DataMember(Name = "is_intensified")]
        [XmlElementAttribute("is_intensified")]
        public bool IsIntensified { get; set; }

        [DataMember(Name = "intensifying_phrase")]
        [XmlElementAttribute("intensifying_phrase")]
        public string IntensifyingPhrase { get; set; }

        [DataMember(Name = "sentiment_score")]
        [XmlElementAttribute("sentiment_score")]
        public float SentimentScore { get; set; }

        [DataMember(Name = "sentiment_polarity")]
        [XmlElementAttribute("sentiment_polarity")]
        public string SentimentPolarity { get; set; }

        [DataMember(Name = "offset")]
        [XmlElementAttribute("offset")]
        public string Offset { get; set; }

        [DataMember(Name = "length")]
        [XmlElementAttribute("length")]
        public string Length { get; set; }
    }
}
