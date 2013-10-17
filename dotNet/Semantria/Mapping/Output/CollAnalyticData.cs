using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Xml.Serialization;

namespace Semantria.Com.Mapping.Output
{
	[DataContract(Name = "collection")]
	[XmlRootAttribute("collection", Namespace = "")]
	public class CollAnalyticData
	{
		[DataMember(Name = "id")]
		[XmlElementAttribute("id")]
		public string Id { get; set; }

        [DataMember(Name = "tag")]
        [XmlElementAttribute("tag")]
        public string Tag { get; set; }

		[DataMember(Name = "config_id")]
		[XmlElementAttribute("config_id")]
		public string ConfigId { get; set; }

		[DataMember(Name = "status")]
		private string _status;
		[XmlElementAttribute("status")]
		public TaskStatus Status
		{
			get
			{
				if (!String.IsNullOrEmpty(_status))
				{
					return (TaskStatus)Enum.Parse(typeof(TaskStatus), _status, true);
				}
				else
				{
					return TaskStatus.UNDEFINED;
				}
			}
			set { _status = value.ToString(); }
		}

		[DataMember(Name = "facets")]
		[XmlArrayAttribute("facets")]
		[XmlArrayItemAttribute("facet", typeof(Facet))]
		public List<Facet> Facets { get; set; }

		[DataMember(Name = "themes")]
		[XmlArrayAttribute("themes")]
		[XmlArrayItemAttribute("theme", typeof(CollTheme))]
		public List<CollTheme> Themes { get; set; }

		[DataMember(Name = "topics")]
		[XmlArrayAttribute("topics")]
		[XmlArrayItemAttribute("topic", typeof(CollTopic))]
		public List<CollTopic> Topics { get; set; }

		[DataMember(Name = "entities")]
		[XmlArrayAttribute("entities")]
		[XmlArrayItemAttribute("entity", typeof(CollEntity))]
		public List<CollEntity> Entities { get; set; }
	}
}

namespace Semantria.Com.Mapping.Output.Stub
{
	[DataContract(Name = "collections")]
	[XmlRootAttribute("collections", Namespace = "")]
	public class CollAnalyticsData
	{
		[DataMember(Name = "collection")]
		[XmlElementAttribute("collection")]
		public List<CollAnalyticData> Data { get; set; }
	}
}
