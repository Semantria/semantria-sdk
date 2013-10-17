using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;
using Semantria.Com.Mapping.Output;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "location")]
    [XmlRootAttribute("location", Namespace = "")]
    public class CollLocation
    {
        [DataMember(Name = "index")]
        [XmlElementAttribute("index")]
        public int Index { get; set; }

        [DataMember(Name = "offset")]
        [XmlElementAttribute("offset")]
        public int Offset { get; set; }

        [DataMember(Name = "length")]
        [XmlElementAttribute("length")]
        public int Length { get; set; }
    }
}
