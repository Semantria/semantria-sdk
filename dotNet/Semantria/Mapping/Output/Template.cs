using System;
using System.Runtime.Serialization;
using System.Collections.Generic;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "document")]
    [XmlRootAttribute("document", Namespace = "")]
    public class Template
    {

        [DataMember(Name = "id", EmitDefaultValue = false)]
        [XmlElementAttribute("id")]
        public string Id { get; set; }

        [DataMember(Name = "config_id", EmitDefaultValue = false)]
        [XmlElementAttribute("config_id")]
        public string ConfigId { get; set; }

        [DataMember(Name = "is_free")]
        [XmlElementAttribute("is_free")]
        public bool IsFree { get; set; }

        [DataMember(Name = "language")]
        [XmlElementAttribute("language")]
        public string Language { get; set; }

        [DataMember(Name = "name")]
        [XmlElementAttribute("name")]
        public string Name { get; set; }

        [DataMember(Name = "type")]
        [XmlElementAttribute("type")]
        public string Type { get; set; }

        [DataMember(Name = "version")]
        [XmlElementAttribute("version")]
        public string Version { get; set; }

        [DataMember(Name = "description")]
        [XmlElementAttribute("description")]
        public string Description { get; set; }
    }
}