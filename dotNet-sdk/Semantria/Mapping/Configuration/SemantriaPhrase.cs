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
        [DataMember(Name = "title")]
        [XmlElementAttribute("title")]
        public string Title { get; set; }

        [DataMember(Name = "weight")]
        [XmlElementAttribute("weight")]
        public float Weight { get; set; }
    }
}

namespace Semantria.Com.Mapping.Configuration.Stub
{
    [DataContract(Name = "phrases")]
    [XmlRootAttribute("phrases", Namespace = "")]
    public class SentimentPhrases
    {
        [DataMember(Name = "phrase")]
        [XmlElementAttribute("phrase")]
        public List<SentimentPhrase> Data { get; set; }
    }

    [DataContract(Name = "phrases")]
    [XmlRootAttribute("phrases", Namespace = "")]
    public class SentimentPhraseManagable : IManagable<SentimentPhrase, string>
    {
        private List<SentimentPhrase> _added = new List<SentimentPhrase>();
        private List<string> _removed = new List<string>();

        #region IManagable<T,U> Members

        [DataMember(Name = "added")]
        [XmlArrayAttribute("added")]
        [XmlArrayItemAttribute("phrase", typeof(SentimentPhrase))]
        public List<SentimentPhrase> Added
        {
            get
            {
                return _added;
            }
            set
            {
                _added = value;
            }
        }

        [DataMember(Name = "removed")]
        [XmlArrayAttribute("removed")]
        [XmlArrayItemAttribute("phrase", typeof(string))]
        public List<string> Removed
        {
            get
            {
                return _removed;
            }
            set
            {
                _removed = value;
            }
        }

        #endregion
    }
}
