using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Configuration.Stub
{
    [DataContract(Name = "blacklists")]
    [XmlRootAttribute("blacklist", Namespace = "")]
    public class Blacklists
    {
        [DataMember(Name = "blacklist")]
        [XmlElementAttribute("item")]
        public List<string> Data { get; set; }
    }

    [DataContract(Name = "blacklist")]
    [XmlRootAttribute("blacklist", Namespace = "")]
    public class BlacklistManagable : IManagable<string, string>
    {
        private List<string> _added = new List<string>();
        private List<string> _removed = new List<string>();

        #region IManagable<T,U> Members

        [DataMember(Name = "added")]
        [XmlArrayAttribute("added")]
        [XmlArrayItemAttribute("item", typeof(string))]
        public List<string> Added
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
        [XmlArrayItemAttribute("item", typeof(string))]
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
