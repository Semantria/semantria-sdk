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
    public class Categories
    {
        [DataMember(Name = "categories")]
        [XmlElementAttribute("category")]
        public List<Category> Data { get; set; }
    }

    [DataContract(Name = "categories")]
    [XmlRootAttribute("categories", Namespace = "")]
    public class CategoryManagable : IManagable<Category, string>
    {
        private List<Category> _added = new List<Category>();
        private List<string> _removed = new List<string>();

        #region IManagable<T,U> Members

        [DataMember(Name = "added")]
        [XmlArrayAttribute("added")]
        [XmlArrayItemAttribute("category", typeof(Category))]
        public List<Category> Added
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
        [XmlArrayItemAttribute("category", typeof(string))]
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
