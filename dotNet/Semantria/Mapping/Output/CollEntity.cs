using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "entity")]
    [XmlRootAttribute("entity", Namespace = "")]
    public class CollEntity
    {
        [DataMember(Name = "title")]
        [XmlElementAttribute("title")]
        public string Title { get; set; }

		[DataMember(Name = "label")]
		[XmlElementAttribute("label")]
		public string Label { get; set; }

        [DataMember(Name = "type")]
        [XmlElementAttribute("type")]
        public string Type { get; set; }

        [DataMember(Name = "entity_type")]
        [XmlElementAttribute("entity_type")]
        public string EntityType { get; set; }

        [DataMember(Name = "count")]
        [XmlElementAttribute("count")]
        public int Count { get; set; }

        [DataMember(Name = "negative_count")]
        [XmlElementAttribute("negative_count")]
        public int NegativeCount { get; set; }

        [DataMember(Name = "neutral_count")]
        [XmlElementAttribute("neutral_count")]
        public int NeutralCount { get; set; }

        [DataMember(Name = "positive_count")]
        [XmlElementAttribute("positive_count")]
        public int PositiveCount { get; set; }

        [DataMember(Name = "mentions")]
        [XmlArrayAttribute("mentions")]
        [XmlArrayItemAttribute("mention", typeof(CollMention))]
        public List<CollMention> Mentions { get; set; }
    }
}