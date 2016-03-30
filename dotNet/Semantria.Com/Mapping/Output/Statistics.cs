using System;
using System.Runtime.Serialization;
using System.Xml.Serialization;
using System.Collections.Generic;
using System.ComponentModel;

namespace Semantria.Com.Mapping.Output
{
    [DataContract(Name = "statistics")]
    [XmlRootAttribute("statistics", Namespace = "")]
    [DefaultProperty("Name")]
    public class Statistics : StatisticsBase
    {
        [ReadOnly(true), Category("Common"), DisplayName("Subscriber name"), Description("Subscriber name")]
        [DataMember(Name = "name")]
        [XmlElementAttribute("name")]
        public override string Name { get; set; }

        [Browsable(false)]
        [DataMember(Name = "configurations")]
        [XmlArrayAttribute("configurations")]
        [XmlArrayItemAttribute("configuration", typeof(StatisticsConfiguration))]
        public List<StatisticsConfiguration> Configurations { get; set; }
    }

    [DataContract(Name = "statistic")]
    [XmlRootAttribute("statistic", Namespace = "")]
    [DefaultProperty("Name")]
    public class StatisticsConfiguration : StatisticsBase
    {
        [Browsable(false)]
        [DataMember(Name = "config_id")]
        [XmlElementAttribute("config_id")]
        public string ConfigId { get; set; }
    }

    [DataContract(Name = "statistic")]
    [XmlRootAttribute("statistic", Namespace = "")]
    public abstract class StatisticsBase
    {
        [ReadOnly(true), Category("Common"), DisplayName("Configuration name"), Description("Configuration name")]
        [DataMember(Name = "name")]
        [XmlElementAttribute("name")]
        public virtual string Name { get; set; }

        [ReadOnly(true), Category("Overall"), DisplayName("Queued batches"), Description("Number of queued batches")]
        [DataMember(Name = "batches_queued")]
        [XmlElementAttribute("batches_queued")]
        public int OverallBatches { get; set; }

        [ReadOnly(true), Category("Overall"), DisplayName("Overall API calls"), Description("Total number of all performed API calls")]
        [DataMember(Name = "total_api_calls")]
        [XmlElementAttribute("total_api_calls")]
        public int OverallCalls { get; set; }

        [ReadOnly(true), Category("Overall"), DisplayName("Settings API calls"), Description("Number of performed settings API calls")]
        [DataMember(Name = "calls_settings")]
        [XmlElementAttribute("calls_settings")]
        public int CallsSettings { get; set; }

        [ReadOnly(true), Category("Overall"), DisplayName("Polling API calls"), Description("Number of performed polling API calls")]
        [DataMember(Name = "calls_polling")]
        [XmlElementAttribute("calls_polling")]
        public int CallsPolling { get; set; }

        [ReadOnly(true), Category("Overall"), DisplayName("Data API calls"), Description("Number of performed data API calls")]
        [DataMember(Name = "calls_data")]
        [XmlElementAttribute("calls_data")]
        public int CallsData { get; set; }

        [ReadOnly(true), Category("Overall document"), DisplayName("Queued documents"), Description("Total number of queued documents")]
        [DataMember(Name = "docs_queued")]
        [XmlElementAttribute("docs_queued")]
        public int OverallDocs { get; set; }

        [ReadOnly(true), Category("Overall document"), DisplayName("Processed documents"), Description("Total number of PROCESSED documents")]
        [DataMember(Name = "docs_successful")]
        [XmlElementAttribute("docs_successful")]
        public int DocsProcessed { get; set; }

        [ReadOnly(true), Category("Overall document"), DisplayName("Failed documents"), Description("Total number of FAILED documents ")]
        [DataMember(Name = "docs_failed")]
        [XmlElementAttribute("docs_failed")]
        public int DocsFailed { get; set; }

        [ReadOnly(true), Category("Overall document"), DisplayName("Returned documents"), Description("Number of returned documents")]
        [DataMember(Name = "docs_retrieved")]
        [XmlElementAttribute("docs_retrieved")]
        public int DocsResponded { get; set; }

		[ReadOnly(true), Category("Common"), DisplayName("Last used app"), Description("The latest used application")]
		[DataMember(Name = "latest_used_app")]
		[XmlElementAttribute("latest_used_app")]
		public string LatestUsedApp { get; set; }
    }
}
