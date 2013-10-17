using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;
using Semantria.Com.Mapping.Output;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "mention")]
    [XmlRootAttribute("mention", Namespace = "")]
    public class CollMention
    {
        [DataMember(Name = "label")]
        [XmlElementAttribute("label")]
        public string Label { get; set; }

        [DataMember(Name = "is_negated")]
        [XmlElementAttribute("is_negated")]
        public bool IsNegated { get; set; }

        [DataMember(Name = "negating_phrase")]
        [XmlElementAttribute("negating_phrase")]
        public string NegatingPhrase { get; set; }

        [DataMember(Name = "locations")]
        [XmlArrayAttribute("locations")]
        [XmlArrayItemAttribute("location", typeof(CollLocation))]
        public List<CollLocation> Locations { get; set; }
    }
}
