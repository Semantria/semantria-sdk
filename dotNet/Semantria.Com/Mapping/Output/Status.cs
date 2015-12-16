using System;
using System.Runtime.Serialization;
using System.Xml.Serialization;
using System.Collections.Generic;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "status")]
    [XmlRootAttribute("status", Namespace = "")]
    public class Status
    {
        [DataMember(Name = "service_status")]
        [XmlElementAttribute("service_status")]
        public string ServiceStatus { get; set; }

        [DataMember(Name = "api_version")]
        [XmlElementAttribute("api_version")]
        public string ApiVersion { get; set; }

        [DataMember(Name = "service_version")]
        [XmlElementAttribute("service_version")]
        public string ServiceVersion { get; set; }

        [DataMember(Name = "supported_encoding")]
        [XmlElementAttribute("supported_encoding")]
        public string SupportedEncoding { get; set; }

        [DataMember(Name = "supported_compression")]
        [XmlElementAttribute("supported_compression")]
        public string SupportedCompression { get; set; }

        [DataMember(Name = "supported_languages", EmitDefaultValue = false)]
        [XmlArrayAttribute("supported_languages")]
        [XmlArrayItemAttribute("language", typeof(string))]
        public List<string> SupportedLanguages { get; set; }
    }
}
