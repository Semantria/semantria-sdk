using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;
using Semantria.Com.Mapping.Output;

namespace Semantria.Com.Mapping.Output
{
	[DataContract(Name = "sentiment_phrase")]
	[XmlRootAttribute("sentiment_phrase", Namespace = "")]
	public class SentimentMentionPhrase
	{
		[DataMember(Name = "score")]
		[XmlElementAttribute("score")]
		public float SentimentScore { get; set; }

		[DataMember(Name = "modified")]
		[XmlElementAttribute("modified")]
		public int Modified { get; set; }

		[DataMember(Name = "type")]
		[XmlElementAttribute("type")]
		public int Type { get; set; }

		[DataMember(Name = "phrase")]
		[XmlElementAttribute("phrase")]
		public MentionPhrase Phrase { get; set; }

        [DataMember(Name = "supporting_phrases")]
        [XmlArrayAttribute("supporting_phrases")]
        [XmlArrayItemAttribute("supporting_phrase", typeof(MentionPhrase))]
        public List<MentionPhrase> SupportingPhrases
        {
            get
            {
                if (supporting_phrases == null) {
                    supporting_phrases = new List<MentionPhrase>();
                }
                return supporting_phrases;
            }
            set
            {
                supporting_phrases = (value != null) ? value : new List<MentionPhrase>();
            }
        }
        private List<MentionPhrase> supporting_phrases = new List<MentionPhrase>();
        
	}

	[DataContract(Name = "phrase")]
	[XmlRootAttribute("phrase", Namespace = "")]
	public class MentionPhrase
	{
		[DataMember(Name = "title")]
		[XmlElementAttribute("title")]
		public string Title { get; set; }

		[DataMember(Name = "type")]
		[XmlElementAttribute("type")]
		public int Type { get; set; }

		[DataMember(Name = "is_negated")]
		[XmlElementAttribute("is_negated")]
		public bool IsNegated { get; set; }

		[DataMember(Name = "negator")]
		[XmlElementAttribute("negator")]
		public string Negator { get; set; }

		[DataMember(Name = "document")]
		[XmlElementAttribute("document")]
		public int Document { get; set; }

		[DataMember(Name = "sentence")]
		[XmlElementAttribute("sentence")]
		public int Sentence { get; set; }

		[DataMember(Name = "section")]
		[XmlElementAttribute("section")]
		public int Section { get; set; }

		[DataMember(Name = "word")]
		[XmlElementAttribute("word")]
		public int Word { get; set; }

		[DataMember(Name = "length")]
		[XmlElementAttribute("length")]
		public int Length { get; set; }

		[DataMember(Name = "byte_offset")]
		[XmlElementAttribute("byte_offset")]
		public int ByteOffset { get; set; }

		[DataMember(Name = "byte_length")]
		[XmlElementAttribute("byte_length")]
		public int ByteLength { get; set; }
	}
}
