using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Configuration
{
    [DataContract(Name = "taxonomy")]
    [XmlRootAttribute("taxonomy", Namespace = "")]
    public class TaxonomyNode
    {
        [DataMember(Name = "id", EmitDefaultValue = false)]
        [XmlElementAttribute("id")]
        public string Id { get; set; }

        [DataMember(Name = "modified", EmitDefaultValue = false)]
        [XmlElementAttribute("modified")]
        public UInt64 Modified { get; set; }

        [DataMember(Name = "name")]
        [XmlElementAttribute("name")]
        public string Name { get; set; }

        [DataMember(Name = "enforce_parent_matching", EmitDefaultValue = false)]
        [XmlElementAttribute("enforce_parent_matching")]
        public bool EnforceParentMatching { get; set; }

        [DataMember(Name = "topics", EmitDefaultValue = false)]
        [XmlElementAttribute("topics")]
        public List<TaxonomyTopic> Data { get; set; }

        [DataMember(Name = "nodes", EmitDefaultValue = false)]
        [XmlElementAttribute("nodes")]
        public List<TaxonomyNode> Nodes { get; set; }
    }
}

namespace Semantria.Com.Mapping.Configuration.Stub
{
    [DataContract(Name = "taxonomies")]
    [XmlRootAttribute("taxonomies", Namespace = "")]
    public class Taxonomies : IStub<TaxonomyNode>
    {
        public Taxonomies()
        {
        }

        public Taxonomies(List<TaxonomyNode> items)
        {
            this.Data = items;
        }

        [XmlElementAttribute("taxonomy")]
        public List<TaxonomyNode> Data { get; set; }

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
            _keys = this.Data.ConvertAll<string>(new Converter<TaxonomyNode, string>(ItemKey));
            this.Data = null;
        }

        public string ConvertTag(string data)
        {
            return data.Replace("<fbe947eeb47>", "<id>").Replace("</fbe947eeb47>", "</id>");
        }

        private string ItemKey(TaxonomyNode item)
        {
            return item.Id;
        }
    }
}
