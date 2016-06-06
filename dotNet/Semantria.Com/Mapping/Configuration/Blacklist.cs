using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Configuration
{
    [DataContract(Name = "blacklist")]
    [XmlRootAttribute("blacklist", Namespace = "")]
    public class BlacklistedItem
    {
        public BlacklistedItem()
        {
        }

        [DataMember(Name = "id", EmitDefaultValue = false)]
        [XmlElementAttribute("id")]
        public string Id { get; set; }

        [DataMember(Name = "name")]
        [XmlElementAttribute("name")]
        public string Name { get; set; }

        [DataMember(Name = "modified", EmitDefaultValue = false)]
        [XmlElementAttribute("modified")]
        public UInt64 Modified { get; set; }
    }
}

namespace Semantria.Com.Mapping.Configuration.Stub
{
    [DataContract(Name = "blacklist")]
    [XmlRootAttribute("blacklist", Namespace = "")]
    public class Blacklists : IStub<BlacklistedItem>
    {
        public Blacklists()
        {
        }

        public Blacklists(List<BlacklistedItem> items)
        {
            this.Data = items;
        }

        [XmlElementAttribute("item")]
        public List<BlacklistedItem> Data { get; set; }

        private List<string> _keys = null;
        [DataMember(Name = "fbe947eeb47")]
        [XmlElementAttribute("fbe947eeb47")]
        public List<string> Keys
        {
            get
            {
                return _keys;
            }
        }

        public void ToKeys()
        {
            _keys = this.Data.ConvertAll<string>(new Converter<BlacklistedItem, string>(ItemKey));
            this.Data = null;
        }

        public string ConvertTag(string data)
        {
            return data.Replace("<fbe947eeb47>", "<id>").Replace("</fbe947eeb47>", "</id>");
        }

        private string ItemKey(BlacklistedItem item)
        {
            return item.Id;
        }
    }
}
