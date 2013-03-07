using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Configuration
{
    [DataContract(Name = "phrase")]
    [XmlRootAttribute("phrase", Namespace = "")]
    public class SentimentPhrase
    {
        [DataMember(Name = "name")]
        [XmlElementAttribute("name")]
        public string Name { get; set; }

        [DataMember(Name = "weight")]
        [XmlElementAttribute("weight")]
        public float Weight { get; set; }
    }
}

namespace Semantria.Com.Mapping.Configuration.Stub
{
    [DataContract(Name = "phrases")]
    [XmlRootAttribute("phrases", Namespace = "")]
    public class SentimentPhrases : IStub<SentimentPhrase>
    {
        public SentimentPhrases()
        {
        }

        public SentimentPhrases(List<SentimentPhrase> items)
        {
            this.Data = items;
        }

        [XmlElementAttribute("phrase")]
        public List<SentimentPhrase> Data { get; set; }

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
            _keys = this.Data.ConvertAll<string>(new Converter<SentimentPhrase, string>(ItemKey));
            this.Data = null;
        }

        public string ConvertTag(string data)
        {
            return data.Replace("<fbe947eeb47>", "<phrase>").Replace("</fbe947eeb47>", "</phrase>");
        }

        private string ItemKey(SentimentPhrase item)
        {
            return item.Name;
        }
    }
}
