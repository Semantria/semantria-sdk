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
    public class StatisticsGrouped
    {
        [ReadOnly(true), Category("Common"), DisplayName("Consumer name"), Description("Consumer name")]
        [DataMember(Name = "consumer_name")]
        [XmlElementAttribute("consumer_name")]
        public string ConsumerName { get; set; }

        [ReadOnly(true), Category("Common"), DisplayName("Consumer configuration id"), Description("Consumer configuration id")]
        [DataMember(Name = "config_id")]
        [XmlElementAttribute("config_id")]
        public string ConfigId { get; set; }

        [ReadOnly(true), Category("Common"), DisplayName("Consumer email"), Description("Consumer email")]
        [DataMember(Name = "user_email")]
        [XmlElementAttribute("user_email")]
        public string UserEmail { get; set; }

        [ReadOnly(true), Category("Common"), DisplayName("Consumer edition name"), Description("Consumer edition name")]
        [DataMember(Name = "edition_name")]
        [XmlElementAttribute("edition_name")]
        public string EditionName { get; set; }

        [ReadOnly(true), Category("Common"), DisplayName("Language"), Description("Language")]
        [DataMember(Name = "language")]
        [XmlElementAttribute("language")]
        public string Language { get; set; }

        [ReadOnly(true), Category("Common"), DisplayName("Group name"), Description("Group name")]
        [DataMember(Name = "app_group")]
        [XmlElementAttribute("app_group")]
        public string AppGroup { get; set; }

        [Browsable(false)]
        [DataMember(Name = "values")]
        [XmlArrayAttribute("values")]
        [XmlArrayItemAttribute("statistic", typeof(StatisticsGroup))]
        public List<StatisticsGroup> Items { get; set; }
    }

    [DataContract(Name = "statistic")]
    [XmlRootAttribute("statistic", Namespace = "")]
    [DefaultProperty("Name")]
    public class StatisticsOverall : StatisticsBase
    {
        [Browsable(false)]
        [DataMember(Name = "config_id")]
        [XmlElementAttribute("config_id")]
        public string ConfigId { get; set; }

        [ReadOnly(true), Category("Common"), DisplayName("Consumer name"), Description("Consumer name")]
        [DataMember(Name = "consumer_name")]
        [XmlElementAttribute("consumer_name")]
        public string ConsumerName { get; set; }

        [ReadOnly(true), Category("Common"), DisplayName("Last used app"), Description("The latest used application")]
        [DataMember(Name = "latest_used_app")]
        [XmlElementAttribute("latest_used_app")]
        public string LatestUsedApp { get; set; }
    }

    [DataContract(Name = "statistic")]
    [XmlRootAttribute("statistic", Namespace = "")]
    [DefaultProperty("Name")]
    public class StatisticsGroup : StatisticsBase
    {
        [ReadOnly(true), Category("Overall"), DisplayName("Time"), Description("Time")]
        [DataMember(Name = "time")]
        [XmlElementAttribute("time")]
        public long Time { get; set; }
    }

    [DataContract(Name = "statistic")]
    [XmlRootAttribute("statistic", Namespace = "")]
    public abstract class StatisticsBase
    {
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
    }

    public enum StatsGroupBy
    {
        ConfigID = 1,
        ConfigName = 2,
        UserId = 3,
        UserName = 4,
        Language = 5,
        Application = 6
    }

    public enum StatsInterval
    {
        undefined = 0,
        minute = 1,
        hour = 2,
        day = 3,
        week = 4,
        month = 5
    }
}
