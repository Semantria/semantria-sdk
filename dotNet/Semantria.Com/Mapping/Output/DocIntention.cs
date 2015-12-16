using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "intention")]
    [XmlRootAttribute("intention", Namespace = "")]
    public class DocIntention
    {
        [DataMember(Name = "type")]
        [XmlElementAttribute("type")]
        public string Type { get; set; }

        [DataMember(Name = "evidence_phrase")]
        [XmlElementAttribute("evidence_phrase")]
        public string EvidencePhrase { get; set; }

        [DataMember(Name = "what")]
        [XmlElementAttribute("what")]
        public string What { get; set; }

        [DataMember(Name = "who")]
        [XmlElementAttribute("who")]
        public string Who { get; set; }
    }
}

