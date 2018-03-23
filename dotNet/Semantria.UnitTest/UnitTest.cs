using System;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Diagnostics;
using System.Linq;

using Semantria.Com.Mapping.Configuration;
using Semantria.Com.Mapping.Output;
using Semantria.Com.Mapping;
using System.IO;

namespace Semantria.Com.TestUnitApi
{
    /// <summary>
    /// Summary description for UnitTest1
    /// </summary>
    [TestClass]
    public class UnitTest
    {
        public UnitTest()
        {
        }

        // Set environment vars before calling this program
        // or edit this file and put your key and secret here.
        private string _consumerKey = System.Environment.GetEnvironmentVariable("SEMANTRIA_KEY");
        private string _consumerSecret = System.Environment.GetEnvironmentVariable("SEMANTRIA_SECRET");

        private string _docId = "3E08B37B-0D74-4BF0-9380-E4D7E8C0279E"; 
        private string _message = "Amazon Web Services has announced a new feature called VM₤Ware Import, which allows IT departments to move virtual machine images from their internal data centers to the cloud.";

        private readonly string TEST_CONFIG_NAME = "TEST_CONFIG";
        private readonly string TEST_CLONE_CONFIG_NAME = "TEST_CLONE_CONFIG";
        private string _configId = null;
        private Session _session = null;

        #region Additional test attributes
  
        [TestInitialize()]
        public void MyTestInitialize() 
        {
            _session = Session.CreateSession(_consumerKey, _consumerSecret);

            _session.Request += new Session.RequestHandler(session_Request);
            _session.Response += new Session.ResponseHandler(session_Response);
            _session.Error += new Session.ErrorHandler(session_Error);
            _session.DocsAutoResponse += new Session.DocsAutoResponseHandler(session_DocsAutoResponse);
            _session.CollsAutoResponse += new Session.CollsAutoResponseHandler(session_CollsAutoResponse);

            CreateOrGetTestConfig();      
        }

        private void CreateOrGetTestConfig()
        {   
            List<Configuration> configsList = _session.GetConfigurations();
            Configuration config = null;
            // Updates or Creates new configuration for the test
            if (!configsList.Any(item => item.Name.Equals(TEST_CONFIG_NAME)))
            {
                List<Configuration> newConfigs = new List<Configuration>();
                newConfigs.Add(new Configuration { Name = TEST_CONFIG_NAME, Language = "English" });
                List<Configuration> result = _session.AddConfigurations(newConfigs);
                config = result[0];
            }
            else
            {
                config = configsList.First(item => item.Name.Equals(TEST_CONFIG_NAME));
            }
            _configId = config.Id;
        }

        // Use TestCleanup to run code after each test has run
        [TestCleanup()]
        public void MyTestCleanup() 
        {
            _session = null;
        }
        
        #endregion

        [TestMethod]
        public void CheckSession()
        {
            Assert.IsNotNull(_session);
        }

        [TestMethod]
        public void GetStatus()
        {
            Status res = _session.GetStatus();
            Assert.IsNotNull(res);
        }

        [TestMethod]
        public void GetSubscription()
        {
            Subscription sub = _session.GetSubscription();
            Assert.IsNotNull(sub);
        }

        [TestMethod]
        public void GetStatistics()
        {
            StatisticsOverall stats = _session.GetStatistics(StatsInterval.day);
            Assert.IsNotNull(stats);
        }

        [TestMethod]
        public void GetStatisticsFromTo()
        {
            StatisticsOverall stats = _session.GetStatistics(DateTime.Now.AddDays(-2), DateTime.Now);
            Assert.IsNotNull(stats);
        }

        [TestMethod]
        public void GetStatisticsGroupBy()
        {
            List<StatisticsGrouped> stats = _session.GetStatistics(StatsInterval.day, "app");
            Assert.IsNotNull(stats);
        }

        [TestMethod]
        public void GetSupportedFeatures()
        {
            IList<FeaturesSet> features = _session.GetSupportedFeatures();
            Assert.IsNotNull(features);
        }

        [TestMethod]
        public void CloneAndUpdateConfiguration()
        {
            List<Configuration> result = _session.GetConfigurations();
            Assert.IsNotNull(result);
            if (result == null) return;

            string template = string.Empty;
            Configuration primaryConfig = null;
            foreach (Configuration item in result)
            {
                if (item.IsPrimary == true)
                {
                    template = item.Id;
                    primaryConfig = item;
                    break;
                }
            }
            Assert.AreNotSame(string.Empty, template);
            Configuration clone = _session.CloneConfiguration(TEST_CLONE_CONFIG_NAME, template);
            Assert.IsNotNull(clone);
            Assert.AreNotEqual(primaryConfig.Id, clone.Id);
            Assert.AreNotEqual(primaryConfig.Name, clone.Name);

            List<Configuration> updateConfigs = new List<Configuration>();
            updateConfigs.Add(new Configuration { Id = clone.Id, AlphanumericThreshold = 0 });
            result = _session.UpdateConfigurations(updateConfigs);
            Assert.IsNotNull(result);

            result = _session.GetConfigurations();
            if (!result.Any(item => item.Name.Equals(TEST_CLONE_CONFIG_NAME)))
            {
                Assert.Fail("can't find cloned config");
            }

            if (result.Where(item => item.Name.Equals(TEST_CLONE_CONFIG_NAME)).First().AlphanumericThreshold != 0)
            {
                Assert.Fail("alphanumeric threshold on cloned config not changed");
            }

            List<string> removeList = new List<string>();
            removeList.Add(clone.Id);
            var res = _session.RemoveConfigurations(removeList);
            Assert.AreEqual(202, res);

            result = _session.GetConfigurations();
            if (result.Any(item => item.Name.Equals(TEST_CLONE_CONFIG_NAME)))
            {
                Assert.Fail("one or more cloned configs still exist");
            }
        }

        [TestMethod]
        public void CreateConfiguration()
        {
            List<Configuration> result = _session.AddConfigurations(new List<Configuration>()
            {
                new Configuration()
                {
                    Name = TEST_CONFIG_NAME,
                    Language = "English"
                }
            });

            Assert.IsNotNull(result);
            Configuration added = result[0];
            Assert.AreEqual(added.Name, TEST_CONFIG_NAME);

            _configId = added.Id;
        }

        [TestMethod]
        public void CRUDBlacklist()
        {
            List<BlacklistedItem> result = _session.GetBlacklist(_configId);
            Assert.IsNotNull(result);

            result = new List<BlacklistedItem>();
            result.Add(new BlacklistedItem { Name = "net*" });
            var obj = _session.AddBlacklist(result, _configId);
            Assert.IsNotNull(obj);

            result = _session.GetBlacklist(_configId);
            List<BlacklistedItem> items = new List<BlacklistedItem>();
            foreach (BlacklistedItem item in result)
            {
                if (item.Name == "net*")
                {
                    item.Name = "net1*";
                    item.Modified = UInt64.MinValue;
                    items.Add(item);
                }
            }

            List<BlacklistedItem> res = _session.UpdateBlacklist(items, _configId);
            Assert.IsNotNull(res);

            foreach (BlacklistedItem item in items)
            {
                item.Name = "net*";
            }

            res = _session.UpdateBlacklist(items, _configId);
            Assert.IsNotNull(res);

            result = _session.GetBlacklist(_configId);
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>();
            foreach (BlacklistedItem item in result)
            {
                if (item.Name == "net*")
                {
                    ditems.Add(item.Id);
                }
            }

            int r = _session.RemoveBlacklist(ditems, _configId);
            Assert.IsTrue(r == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void CRUDCategory()
        {
            List<Category> result = _session.GetCategories(_configId);
            Assert.IsNotNull(result);

            result = new List<Category>();
            result.Add(new Category { Name = "NET" });
            var obj = _session.AddCategories(result, _configId);
            Assert.IsNotNull(obj);

            result = _session.GetCategories(_configId);
            List<Category> items = new List<Category>();
            foreach (Category item in result)
            {
                if (item.Name == "NET")
                {
                    item.Name = "NET1*";
                    item.Modified = UInt64.MinValue;
                    items.Add(item);
                    break;
                }
            }

            List<Category> res = _session.UpdateCategories(items, _configId);
            Assert.IsNotNull(res);

            foreach (Category item in items)
            {
                item.Name = "NET";
            }

            res = _session.UpdateCategories(items, _configId);
            Assert.IsNotNull(res);

            result = _session.GetCategories(_configId);
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>(); 
            foreach (Category item in result)
            {
                if (item.Name == "NET")
                {
                    ditems.Add(item.Id);
                }
            }

            int r = _session.RemoveCategories(ditems, _configId);
            Assert.IsTrue(r == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void CRUDQuery()
        {
            List<Query> result = _session.GetQueries(_configId);
            Assert.IsNotNull(result);

            result = new List<Query>(); 
            result.Add(new Query { Name = "NET", Content = "Data" });
            var obj = _session.AddQueries(result, _configId);
            Assert.IsNotNull(obj);

            result = _session.GetQueries(_configId);
            List<Query> items = new List<Query>();
            foreach (Query item in result)
            {
                if (item.Name == "NET")
                {
                    item.Name = "NET1*";
                    item.Modified = UInt64.MinValue;
                    items.Add(item);
                }
            }

            List<Query> res = _session.UpdateQueries(items, _configId);
            Assert.IsNotNull(res);

            foreach (Query item in items)
            {
                item.Name = "NET";
            }

            res = _session.UpdateQueries(items, _configId);
            Assert.IsNotNull(res);

            result = _session.GetQueries(_configId);
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>(); 
            foreach (Query item in result)
            {
                if (item.Name == "NET")
                {
                    ditems.Add(item.Id);
                }
            }

            int r = _session.RemoveQueries(ditems, _configId);
            Assert.IsTrue(r == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void CRUDSentimentPhrase()
        {
            List<SentimentPhrase> result = _session.GetSentimentPhrases(_configId);
            Assert.IsNotNull(result);

            result = new List<SentimentPhrase>();
            result.Add(new SentimentPhrase { Name = "NET", Weight = 0.1f });
            var obj = _session.AddSentimentPhrases(result, _configId);
            Assert.IsNotNull(obj);

            result = _session.GetSentimentPhrases(_configId);
            List<SentimentPhrase> items = new List<SentimentPhrase>();
            foreach (SentimentPhrase item in result)
            {
                if (item.Name == "NET")
                {
                    item.Name = "NET1*";
                    item.Modified = UInt64.MinValue;
                    items.Add(item);
                }
            }

            List<SentimentPhrase> res = _session.UpdateSentimentPhrases(items, _configId);
            Assert.IsNotNull(res);

            foreach (SentimentPhrase item in items)
            {
                item.Name = "NET";
            }

            res = _session.UpdateSentimentPhrases(items, _configId);

            Assert.IsNotNull(res);

            result = _session.GetSentimentPhrases(_configId);
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>();
            foreach (SentimentPhrase item in result)
            {
                if (item.Name == "NET")
                {
                    ditems.Add(item.Id);
                }
            }

            int r = _session.RemoveSentimentPhrases(ditems, _configId);
            Assert.IsTrue(r == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void CRUDEntity()
        {
            List<UserEntity> result = _session.GetEntities(_configId);
            Assert.IsNotNull(result);

            result = new List<UserEntity>();
            result.Add(new UserEntity { Name = "NET", Type = "Language" });
            var obj = _session.AddEntities(result, _configId);
            Assert.IsNotNull(obj);

            result = _session.GetEntities(_configId);
            List<UserEntity> items = new List<UserEntity>();
            foreach (UserEntity item in result)
            {
                if (item.Name == "NET")
                {
                    item.Name = "NET1*";
                    item.Modified = UInt64.MinValue;
                    items.Add(item);
                }
            }

            List<UserEntity> res = _session.UpdateEntities(items, _configId);
            Assert.IsNotNull(res);

            foreach (UserEntity item in items)
            {
                item.Name = "NET";
            }

            res = _session.UpdateEntities(items, _configId);
            Assert.IsNotNull(res);

            result = _session.GetEntities(_configId);
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>();
            foreach (UserEntity item in result)
            {
                if (item.Name == "NET")
                {
                    ditems.Add(item.Id);
                }
            }

            int r = _session.RemoveEntities(ditems, _configId);
            Assert.IsTrue(r == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void CRUDTaxonomy()
        {
            List<TaxonomyNode> result = _session.GetTaxonomy(_configId);
            Assert.IsNotNull(result);

            result = new List<TaxonomyNode>();
            result.Add(new TaxonomyNode { Name = "NET", Nodes = new List<TaxonomyNode>(new TaxonomyNode[] { new TaxonomyNode() { Name = "NETWORK_0" }, new TaxonomyNode() { Name = "NETWORK_1" } })});
            var obj = _session.AddTaxonomy(result, _configId);
            Assert.IsNotNull(obj);

            result = _session.GetTaxonomy(_configId);
            List<TaxonomyNode> items = new List<TaxonomyNode>();
            foreach (TaxonomyNode item in result)
            {
                if (item.Name == "NET")
                {
                    item.Name = "NET1*";
                    item.Modified = UInt64.MinValue;
                    item.Nodes = null;
                    item.Data = null;

                    items.Add(item);
                }
            }

            List<TaxonomyNode> res = _session.UpdateTaxonomy(items, _configId);
            Assert.IsNotNull(res);

            foreach (TaxonomyNode item in items)
            {
                item.Name = "NET";
                item.Modified = UInt64.MinValue;
                item.Nodes = null;
                item.Data = null;
            }

            res = _session.UpdateTaxonomy(items, _configId);
            Assert.IsNotNull(res);

            result = _session.GetTaxonomy(_configId);
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>();
            foreach (TaxonomyNode item in result)
            {
                if (item.Name == "NET")
                {
                    ditems.Add(item.Id);
                }
            }

            int r = _session.RemoveTaxonomy(ditems, _configId);
            Assert.IsTrue(r == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void Document()
        {
            Document task = new Document();
            task.Id = _docId;
            task.Text = _message;

            int res = _session.QueueDocument(task, _configId);
            Assert.IsTrue((res == 200 || res == 202), "RESULT: " + res);

            DocAnalyticData data = _session.GetDocument(_docId, _configId);
            Assert.IsNotNull(data);

            int r = _session.CancelDocument(_docId, _configId);
            Assert.IsTrue((r == 200 || r == 202 || r == 404), "RESULT: " + res);
        }

        [TestMethod]
        public void Batch()
        {
            List<Document> list = new List<Document>();
            for (int i = 0; i < 5; i++)
            {
                Document task = new Document();
                task.Id = System.Guid.NewGuid().ToString();
                task.Text = _message;
                list.Add(task);
            }

            int res = _session.QueueBatchOfDocuments(list, _configId);
            Assert.IsTrue((res == 200 || res == 202), "RESULT: " + res);

            System.Threading.Thread.Sleep(5000);
            
            IList<DocAnalyticData> data = _session.GetProcessedDocuments(_configId);
            Assert.IsNotNull(data);
        }

        [TestMethod]
        public void Collection()
        {
            Collection task = new Collection();
            task.Id = _docId;
            task.Documents = new List<string>() { _message + "1", _message + "2" };

            int res = _session.QueueCollection(task, _configId);
            Assert.IsTrue((res == 200 || res == 202), "RESULT: " + res);

            task = new Collection();
            task.Id = System.Guid.NewGuid().ToString();
            task.Documents = new List<string>() { _message + "1", _message + "2" };
            res = _session.QueueCollection(task, _configId);
            Assert.IsTrue((res == 200 || res == 202), "RESULT: " + res);

            CollAnalyticData data = _session.GetCollection(_docId, _configId);
            Assert.IsNotNull(data);

            int r = _session.CancelCollection(_docId, _configId);
            Assert.IsTrue((r == 200 || r == 202 || r == 404), "RESULT: " + res);

            IList<CollAnalyticData> ad = _session.GetProcessedCollections(_configId);
            Assert.IsNotNull(ad);
        }

        [TestMethod]
        public void GetUserDirectory()
        {
            List<Query> result = _session.GetQueries(_configId);
            Assert.IsNotNull(result);

            result = new List<Query>();
            string queryString = "dog AND cat";
            result.Add(new Query { Name = "q1", Content = queryString });
            var obj = _session.AddQueries(result, _configId);
            Assert.IsNotNull(obj);

            byte[] data = _session.GetUserDirectory(_configId, "tar");
            // Since tar is not compressed we can just search for the query string
            Assert.IsTrue(SearchBytes(System.Text.Encoding.UTF8.GetBytes(queryString), data));

            string tempDir = System.Environment.GetEnvironmentVariable("temp");
            if (tempDir == null)
            {
                tempDir = "\\tmp";
            }
            string tarFile = Path.Combine(tempDir, "test-user-dir.tar");
            _session.WriteUserDirectoryToFile(_configId, tarFile);
            System.Console.WriteLine(string.Format("Check that tarfile contains valid data: {0}", tarFile));

            // Cleanup queries
            result = _session.GetQueries(_configId);
            List<string> ditems = new List<string>(); 
            foreach (Query item in result)
            {
                ditems.Add(item.Id);
            }
            _session.RemoveQueries(ditems, _configId);
        }

        [TestMethod]
        public void RemoveConfigurations()
        {
            List<string> removeList = new List<string>();
            removeList.Add(_configId);
            var res = _session.RemoveConfigurations(removeList);
            Assert.AreEqual(202, res);
        }

        // Returns true if 'what' is a subsequence of 'bytes'
        private static bool SearchBytes(byte[] what, byte[] bytes)
        {
            var len = what.Length;
            var limit = bytes.Length - len;
            for (var i = 0; i <= limit; i++)
            {
                var k = 0;
                for (; k < len; k++)
                {
                    if (what[k] != bytes[i + k]) break;
                }
                if (k == len) return true;
            }
            return false;
        }

        void session_Request(object sender, RequestEventArgs e)
        {
            Debug.WriteLine(String .Format("REQUEST: {0} {1} - message: {2}", e.Method, e.Url, e.Message));
        }

        void session_Response(object sender, ResponseEventArgs e)
        {
            Debug.WriteLine(String.Format("RESPONSE: {0}({1}) - message: {2}", e.Status, (int)e.Status, e.Message));
            bool res = (e.Status == System.Net.HttpStatusCode.OK || e.Status == System.Net.HttpStatusCode.Accepted || e.Status == System.Net.HttpStatusCode.NotFound);
            Assert.AreEqual(res, true);
        }

        void session_Error(object sender, ResponseEventArgs e)
        {
            Debug.WriteLine(String.Format("ERROR: {0}({1}) - message: {2}", e.Status, (int)e.Status, e.Message));
        }

        static void session_DocsAutoResponse(object sender, DocsAutoResponseEventArgs e)
        {
            Debug.WriteLine(String.Format("DOCSAUTORESPONSE: {0}", e.AnalyticData.Count));
            Assert.IsNotNull(e.AnalyticData);
        }

        static void session_CollsAutoResponse(object sender, CollsAutoResponseEventArgs e)
        {
            Debug.WriteLine(String.Format("COLLSAUTORESPONSE: {0}", e.AnalyticData.Count));
            Assert.IsNotNull(e.AnalyticData);
        }
 
    }
}
