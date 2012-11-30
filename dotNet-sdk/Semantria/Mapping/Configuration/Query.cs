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
    public class Queries
    {
        [DataMember(Name = "query")]
        [XmlElementAttribute("query")]
        public List<Query> Data { get; set; }
    }

    [DataContract(Name = "queries")]
    [XmlRootAttribute("queries", Namespace = "")]
    public class QueryManagable : IManagable<Query, string>
    {
        private List<Query> _added = new List<Query>();
        private List<string> _removed = new List<string>();

        #region IManagable<T,U> Members

        [DataMember(Name = "added")]
        [XmlArrayAttribute("added")]
        [XmlArrayItemAttribute("query", typeof(Query))]
        public List<Query> Added
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
        [XmlArrayItemAttribute("query", typeof(string))]
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
