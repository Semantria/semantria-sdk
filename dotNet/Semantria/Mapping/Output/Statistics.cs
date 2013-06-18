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

        [ReadOnly(true), Category("Common"), DisplayName("Subscriber status"), Description("Subscriber status")]
        [DataMember(Name = "status")]
        [XmlElementAttribute("status")]
        public string Status { get; set; }

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

        [ReadOnly(true), Category("Overall"), DisplayName("Processed texts"), Description("Total number of processed texts")]
        [DataMember(Name = "overall_texts")]
        [XmlElementAttribute("overall_texts")]
        public int OverallTexts { get; set; }

        [ReadOnly(true), Category("Overall"), DisplayName("Queued batches"), Description("Number of queued batches")]
        [DataMember(Name = "overall_batches")]
        [XmlElementAttribute("overall_batches")]
        public int OverallBatches { get; set; }

        [ReadOnly(true), Category("Overall"), DisplayName("Overall API calls"), Description("Total number of all performed API calls")]
        [DataMember(Name = "overall_calls")]
        [XmlElementAttribute("overall_calls")]
        public int OverallCalls { get; set; }

		[ReadOnly(true), Category("Overall"), DisplayName("Overall texts exceeded"), Description("Total number of all texts exceeded suggested limit")]
		[DataMember(Name = "overall_exceeded")]
		[XmlElementAttribute("overall_exceeded")]
		public int OverallExceeded { get; set; }

        [ReadOnly(true), Category("Overall"), DisplayName("System API calls"), Description("Number of performed system API calls")]
        [DataMember(Name = "calls_system")]
        [XmlElementAttribute("calls_system")]
        public int CallsSystem { get; set; }

        [ReadOnly(true), Category("Overall"), DisplayName("Data API calls"), Description("Number of performed data calls")]
        [DataMember(Name = "calls_data")]
        [XmlElementAttribute("calls_data")]
        public int CallsData { get; set; }

        [ReadOnly(true), Category("Overall document"), DisplayName("Queued documents"), Description("Total number of queued documents")]
        [DataMember(Name = "overall_docs")]
        [XmlElementAttribute("overall_docs")]
        public int OverallDocs { get; set; }

        [ReadOnly(true), Category("Overall document"), DisplayName("Processed documents"), Description("Number of PROCESSED documents")]
        [DataMember(Name = "docs_processed")]
        [XmlElementAttribute("docs_processed")]
        public int DocsProcessed { get; set; }

        [ReadOnly(true), Category("Overall document"), DisplayName("Failed documents"), Description("Number of FAILED documents ")]
        [DataMember(Name = "docs_failed")]
        [XmlElementAttribute("docs_failed")]
        public int DocsFailed { get; set; }

        [ReadOnly(true), Category("Overall document"), DisplayName("Returned documents"), Description("Number of returned documents")]
        [DataMember(Name = "docs_responded")]
        [XmlElementAttribute("docs_responded")]
        public int DocsResponded { get; set; }

        [ReadOnly(true), Category("Overall collection"), DisplayName("Queued collections"), Description("Total number of queued collections")]
        [DataMember(Name = "overall_colls")]
        [XmlElementAttribute("overall_colls")]
        public int OverallColls { get; set; }

        [ReadOnly(true), Category("Overall collection"), DisplayName("Processed collections"), Description("Number of PROCESSED collections")]
        [DataMember(Name = "colls_processed")]
        [XmlElementAttribute("colls_processed")]
        public int CollsProcessed { get; set; }

        [ReadOnly(true), Category("Overall collection"), DisplayName("Failed collections"), Description("Number of FAILED collections")]
        [DataMember(Name = "colls_failed")]
        [XmlElementAttribute("colls_failed")]
        public int CollsFailed { get; set; }

        [ReadOnly(true), Category("Overall collection"), DisplayName("Returned collections"), Description("Number of returned collections")]
        [DataMember(Name = "colls_responded")]
        [XmlElementAttribute("colls_responded")]
        public int CollsResponded { get; set; }

        [ReadOnly(true), Category("Overall collection"), DisplayName("Documents in collections"), Description("Total number of documents queued within collections")]
        [DataMember(Name = "colls_documents")]
        [XmlElementAttribute("colls_documents")]
        public int CollsDocuments { get; set; }

		[ReadOnly(true), Category("Common"), DisplayName("Last used app"), Description("The latest used application")]
		[DataMember(Name = "latest_used_app")]
		[XmlElementAttribute("latest_used_app")]
		public string LatestUsedApp { get; set; }

		[ReadOnly(true), Category("Common"), DisplayName("All used apps"), Description("All used applications")]
		[DataMember(Name = "used_apps")]
		[XmlElementAttribute("used_apps")]
		public string UsedApp { get; set; }
    }
}
