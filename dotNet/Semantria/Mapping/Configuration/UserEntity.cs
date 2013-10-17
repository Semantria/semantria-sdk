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

		[DataMember(Name = "label")]
		[XmlElementAttribute("label")]
		public string Label { get; set; }

        [DataMember(Name = "normalized")]
        [XmlElementAttribute("normalized")]
        public string Normalized { get; set; }    }
}

namespace Semantria.Com.Mapping.Configuration.Stub
{
    [DataContract(Name = "entities")]
    [XmlRootAttribute("entities", Namespace = "")]
    public class UserEntities : IStub<UserEntity>
    {
        public UserEntities()
        {
        }

        public UserEntities(List<UserEntity> items)
        {
            this.Data = items;
        }

        [XmlElementAttribute("entity")]
        public List<UserEntity> Data { get; set; }

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
            _keys = this.Data.ConvertAll<string>(new Converter<UserEntity, string>(ItemKey));
            this.Data = null;
        }

        public string ConvertTag(string data)
        {
            return data.Replace("<fbe947eeb47>", "<entity>").Replace("</fbe947eeb47>", "</entity>");
        }

        private string ItemKey(UserEntity item)
        {
            return item.Name;
        }
    }
}
