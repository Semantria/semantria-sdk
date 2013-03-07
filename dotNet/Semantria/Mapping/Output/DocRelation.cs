using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "relation")]
    [XmlRootAttribute("relation", Namespace = "")]
    public class DocRelation
    {
        [DataMember(Name = "type")]
        [XmlElementAttribute("type")]
        public string Type { get; set; }

        [DataMember(Name = "relation_type")]
        [XmlElementAttribute("relation_type")]
        public string RelationType { get; set; }

        [DataMember(Name = "confidence_score")]
        [XmlElementAttribute("confidence_score")]
        public float ConfidenceScore { get; set; }

        [DataMember(Name = "extra")]
        [XmlElementAttribute("extra")]
        public string Extra { get; set; }

        [DataMember(Name = "entities")]
        [XmlArrayAttribute("entities")]
        [XmlArrayItemAttribute("entity", typeof(DocEntity))]
        public List<DocEntity> Entities { get; set; }
    }
}

