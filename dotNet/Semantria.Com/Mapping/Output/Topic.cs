using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "topic")]
    [XmlRootAttribute("topic", Namespace = "")]
    public class Topic
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

        [DataMember(Name = "label")]
        [XmlElementAttribute("label")]
        public string Label { get; set; }

        [DataMember(Name = "sentiment_polarity")]
        [XmlElementAttribute("sentiment_polarity")]
        public string SentimentPolarity { get; set; }

        [DataMember(Name = "topics")]
        [XmlArrayAttribute("topics")]
        [XmlArrayItemAttribute("topic", typeof(Topic))]
        public List<Topic> Topics { get; set; }

        [DataMember(Name = "mentions")]
        [XmlArrayAttribute("mentions")]
        [XmlArrayItemAttribute("mention", typeof(DocMention))]
        public List<DocMention> Mentions { get; set; }

        [DataMember(Name = "sentiment_phrases")]
        [XmlArrayAttribute("sentiment_phrases")]
        [XmlArrayItemAttribute("sentiment_phrase", typeof(SentimentMentionPhrase))]
        public List<SentimentMentionPhrase> SentimentPhrases
        {
           get
            {
                if (sentiment_phrases == null) {
                    sentiment_phrases = new List<SentimentMentionPhrase>();
                }
                return sentiment_phrases;
            }
            set
            {
                sentiment_phrases = (value != null) ? value : new List<SentimentMentionPhrase>();
            }
        }
        private List<SentimentMentionPhrase> sentiment_phrases = new List<SentimentMentionPhrase>();
    }
}