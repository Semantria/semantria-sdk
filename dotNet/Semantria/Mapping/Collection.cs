using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping
{
    [DataContract(Name = "collection")]
    [XmlRootAttribute("collection", Namespace = "")]
    public class Collection
    {
        [DataMember(Name = "id")]
        [XmlElementAttribute("id")]
        public string Id { get; set; }

        [DataMember(Name = "tag")]
        [XmlElementAttribute("tag")]
        public string Tag { get; set; }

        [DataMember(Name = "documents", EmitDefaultValue = false)]
        [XmlArrayAttribute("documents")]
        [XmlArrayItemAttribute("document", typeof(string))]
        public List<string> Documents { get; set; }
    }
}

namespace Semantria.Com.Mapping.Stub
{
    [DataContract(Name = "collections")]
    [XmlRootAttribute("collections", Namespace = "")]
    public class Collections
    {
        [DataMember(Name = "collection")]
        [XmlElementAttribute("collection")]
        public List<Collection> Data { get; set; }
    }
}
