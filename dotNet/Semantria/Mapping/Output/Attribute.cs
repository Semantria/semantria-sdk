using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "attribute")]
    [XmlRootAttribute("attribute", Namespace = "")]
    public class Attribute
    {
        [DataMember(Name = "label")]
        [XmlElementAttribute("label")]
        public string Label { get; set; }

        [DataMember(Name = "count")]
        [XmlElementAttribute("count")]
        public int Count { get; set; }

        [DataMember(Name = "mentions")]
        [XmlArrayAttribute("mentions")]
        [XmlArrayItemAttribute("mention", typeof(CollMention))]
        public List<CollMention> Mentions { get; set; }
    }
}
