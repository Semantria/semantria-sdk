using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;
using Semantria.Com.Mapping.Output;

namespace Semantria.Com.Mapping.Output
{
	[DataContract(Name = "mention")]
	[XmlRootAttribute("mention", Namespace = "")]
	public class DocMention
	{
		[DataMember(Name = "label")]
		[XmlElementAttribute("label")]
		public string Label { get; set; }

		[DataMember(Name = "is_negated")]
		[XmlElementAttribute("is_negated")]
		public bool IsNegated { get; set; }

		[DataMember(Name = "negating_phrase")]
		[XmlElementAttribute("negating_phrase")]
		public string NegatingPhrase { get; set; }

		[DataMember(Name = "locations")]
		[XmlArrayAttribute("locations")]
		[XmlArrayItemAttribute("location", typeof(Location))]
		public List<Location> Locations { get; set; }
	}

	[DataContract(Name = "location")]
	[XmlRootAttribute("location", Namespace = "")]
	public class Location
	{
		[DataMember(Name = "offset")]
		[XmlElementAttribute("offset")]
		public int Offset { get; set; }

		[DataMember(Name = "length")]
		[XmlElementAttribute("length")]
		public int Length { get; set; }
	}
}
