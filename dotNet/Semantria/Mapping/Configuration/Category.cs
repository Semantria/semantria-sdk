using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Configuration
{
    [DataContract(Name = "category")]
    [XmlRootAttribute("category", Namespace = "")]
    public class Category
    {
        public Category() {
            Samples = new List<string>();
        }

        [DataMember(Name = "name")]
        [XmlElementAttribute("name")]
        public string Name { get; set; }

        [DataMember(Name = "weight")]
        [XmlElementAttribute("weight")]
        public float Weight { get; set; }

        [DataMember(Name = "samples", EmitDefaultValue = false)]
        [XmlArrayAttribute("samples", IsNullable = true)]
        [XmlArrayItemAttribute("sample", typeof(string), IsNullable = true)]
        public List<string> Samples { get; set; }
    }
}

namespace Semantria.Com.Mapping.Configuration.Stub
{
    [DataContract(Name = "categories")]
    [XmlRootAttribute("categories", Namespace = "")]
    public class Categories : IStub<Category>
    {
        public Categories()
        {
        }

        public Categories(List<Category> items)
        {
            this.Data = items;
        }
        
        [XmlElementAttribute("category")]
        public List<Category> Data { get; set; }

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
            _keys = this.Data.ConvertAll<string>(new Converter<Category, string>(ItemKey));
            this.Data = null;
        }

        public string ConvertTag(string data)
        {
            return data.Replace("<fbe947eeb47>", "<category>").Replace("</fbe947eeb47>", "</category>");
        }

        private string ItemKey(Category item)
        {
            return item.Name;
        }
    }
}
