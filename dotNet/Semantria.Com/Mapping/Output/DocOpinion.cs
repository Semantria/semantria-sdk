using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;
using Semantria.Com.Mapping.Output;

namespace Semantria.Com.Mapping.Output
{
	[DataContract(Name = "opinion")]
	[XmlRootAttribute("opinion", Namespace = "")]
	public class DocOpinion
	{
		[DataMember(Name = "quotation")]
		[XmlElementAttribute("quotation")]
		public string Label { get; set; }

		[DataMember(Name = "type")]
		[XmlElementAttribute("type")]
		public string Type { get; set; }

		[DataMember(Name = "speaker")]
		[XmlElementAttribute("speaker")]
		public string Speaker { get; set; }

		[DataMember(Name = "topic")]
		[XmlElementAttribute("topic")]
		public string Topic { get; set; }

		[DataMember(Name = "sentiment_score")]
		[XmlElementAttribute("sentiment_score")]
		public float SentimentScore { get; set; }

		[DataMember(Name = "sentiment_polarity")]
		[XmlElementAttribute("sentiment_polarity")]
		public string SentimentPolarity { get; set; }
	}
}
