using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Configuration
{
    [DataContract(Name = "entity")]
    [XmlRootAttribute("entity", Namespace = "")]
    public class UserEntity
    {
        [DataMember(Name = "name")]
        [XmlElementAttribute("name")]
        public string Name { get; set; }

        [DataMember(Name = "type")]
        [XmlElementAttribute("type")]
        public string Type { get; set; }
    }
}

namespace Semantria.Com.Mapping.Configuration.Stub
{
    [DataContract(Name = "entities")]
    [XmlRootAttribute("entities", Namespace = "")]
    public class UserEntities
    {
        [DataMember(Name = "entity")]
        [XmlElementAttribute("entity")]
        public List<UserEntity> Data { get; set; }
    }

    [DataContract(Name = "entities")]
    [XmlRootAttribute("entities", Namespace = "")]
    public class EntityManagable : IManagable<UserEntity, string>
    {
        private List<UserEntity> _added = new List<UserEntity>();
        private List<string> _removed = new List<string>();

        #region IManagable<T,U> Members

        [DataMember(Name = "added")]
        [XmlArrayAttribute("added")]
        [XmlArrayItemAttribute("entity", typeof(UserEntity))]
        public List<UserEntity> Added
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
        [XmlArrayItemAttribute("entity", typeof(string))]
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
