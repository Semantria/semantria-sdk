using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Configuration
{
    [DataContract(Name = "query")]
    [XmlRootAttribute("query", Namespace = "")]
    public class Query
    {
        [DataMember(Name = "name")]
        [XmlElementAttribute("name")]
        public string Name { get; set; }
        
        [DataMember(Name = "query")]
        [XmlElementAttribute("query")]
        public string Content { get; set; }
    }
}

namespace Semantria.Com.Mapping.Configuration.Stub
{
    [DataContract(Name = "queries")]
    [XmlRootAttribute("queries", Namespace = "")]
    public class Queries : IStub<Query>
    {
        public Queries()
        {
        }

        public Queries(List<Query> items)
        {
            this.Data = items;
        }
        
        [XmlElementAttribute("query")]
        public List<Query> Data { get; set; }

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
            _keys = this.Data.ConvertAll<string>(new Converter<Query, string>(ItemKey));
            this.Data = null;
        }

        public string ConvertTag(string data)
        {
            return data.Replace("<fbe947eeb47>", "<query>").Replace("</fbe947eeb47>", "</query>");
        }

        private string ItemKey(Query item)
        {
            return item.Name;
        }
    }
}
