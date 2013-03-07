using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Configuration.Stub
{
    [DataContract(Name = "blacklists")]
    [XmlRootAttribute("blacklist", Namespace = "")]
    public class Blacklists : IStub<string>
    {
        public Blacklists()
        {
        }

        public Blacklists(List<string> items)
        {
            this.Data = items;
        }

        [DataMember(Name = "blacklist")]
        [XmlElementAttribute("item")]
        public List<string> Data { get; set; }

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
            _keys = this.Data.ConvertAll<string>(new Converter<string, string>(ItemKey));
            this.Data = null;
        }

        public string ConvertTag(string data)
        {
            return data.Replace("<fbe947eeb47>", "<item>").Replace("</fbe947eeb47>", "</item>");
        }

        private string ItemKey(string item)
        {
            return item;
        }
    }
}
