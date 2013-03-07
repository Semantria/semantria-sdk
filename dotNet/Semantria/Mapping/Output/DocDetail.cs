using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "detail")]
    [XmlRootAttribute("detail", Namespace = "")]
    public class DocDetail
    {
        [DataMember(Name = "is_impoerative")]
        [XmlElementAttribute("is_impoerative")]
        public bool IsImpoerative { get; set; }

        [DataMember(Name = "is_polar")]
        [XmlElementAttribute("is_polar")]
        public bool IsPolar { get; set; }

        [DataMember(Name = "words")]
        [XmlArrayAttribute("words")]
        [XmlArrayItemAttribute("word", typeof(DocWord))]
        public List<DocWord> Words { get; set; }
    }
}

