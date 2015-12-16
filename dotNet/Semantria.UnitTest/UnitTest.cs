using System;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Diagnostics;
using System.Text;
using System.Linq;

using Semantria.Com.Mapping.Configuration;
using Semantria.Com.Mapping.Output;
using Semantria.Com.Serializers;
using Semantria.Com.Mapping;

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
            //
            // TODO: Add constructor logic here
            //
        }

        private string _consumerKey = string.Empty;
        private string _consumerSecret = string.Empty;

        private string _id = "3E08B37B-0D74-4BF0-9380-E4D7E8C0279E"; 
        private string _message = "Amazon Web Services has announced a new feature called VM₤Ware Import, which allows IT departments to move virtual machine images from their internal data centers to the cloud.";

        private Session _session = null;

        private TestContext _testContextInstance;

        /// <summary>
        ///Gets or sets the test context which provides
        ///information about and functionality for the current test run.
        ///</summary>
        public TestContext TestContext
        {
            get
            {
                return _testContextInstance;
            }
            set
            {
                _testContextInstance = value;
            }
        }

        #region Additional test attributes
        //
        // You can use the following additional attributes as you write your tests:
        //
        // Use ClassInitialize to run code before running the first test in the class
        // [ClassInitialize()]
        // public static void MyClassInitialize(TestContext testContext) { }
        //
        // Use ClassCleanup to run code after all tests in a class have run
        // [ClassCleanup()]
        // public static void MyClassCleanup() { }
        //
        // Use TestInitialize to run code before running each test 
        [TestInitialize()]
        public void MyTestInitialize() 
        {
            _session = Session.CreateSession(_consumerKey, _consumerSecret);
            _session.Host = "https://api.semantria.com";

            _session.Request += new Session.RequestHandler(session_Request);
            _session.Response += new Session.ResponseHandler(session_Response);
            _session.Error += new Session.ErrorHandler(session_Error);
            _session.DocsAutoResponse += new Session.DocsAutoResponseHandler(session_DocsAutoResponse);
            _session.CollsAutoResponse += new Session.CollsAutoResponseHandler(session_CollsAutoResponse);        
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
            Statistics stat = _session.GetStatistics();
            Assert.IsNotNull(stat);
        }

        [TestMethod]
        public void GetSupportedFeatures()
        {
            IList<FeaturesSet> features = _session.GetSupportedFeatures();
            Assert.IsNotNull(features);
        }

        [TestMethod]
        public void CRUDConfiguration()
        {
            List<Configuration> result = _session.GetConfigurations();
            Assert.IsNotNull(result);
            if (result == null) return;

            Configuration primaryConf = null;
            foreach (Configuration item in result)
            {
                if (item.IsPrimary == true)
                {
                    item.AutoResponse = false;
                    item.CharsThreshold = 50;
                    item.OneSentence = true;
                }

                primaryConf = item;
            }

            List<Configuration> res = _session.UpdateConfigurations(new List<Configuration>() { primaryConf });
            Assert.IsTrue(res != null);
        }

        [TestMethod]
        public void CloneConfiguration()
        {
            List<Configuration> result = _session.GetConfigurations();
            Assert.IsNotNull(result);
            if (result == null) return;

            string name = "netCloneTest_name";
            string template = string.Empty;
            Configuration primaryConfig = null;
            foreach (Configuration item in result)
            {
                if (item.IsPrimary == true)
                {
                    template = item.ConfigId;
                    primaryConfig = item;
                    break;
                }
            }
            Assert.AreNotSame(string.Empty, template);
            result = _session.CloneConfiguration(name, template);
            Assert.IsNotNull(result);
            Configuration clone = result[0];
            Assert.AreNotEqual(primaryConfig.ConfigId, clone.ConfigId);
            Assert.AreNotEqual(primaryConfig.Name, clone.Name);

            result = _session.GetConfigurations();
            if (!result.Any(item => item.Name.Equals(name)))
            {
                Assert.Fail();
            }

            List<string> removeList = new List<string>();
            removeList.Add(clone.ConfigId);
            var res = _session.RemoveConfigurations(removeList);
            Assert.AreEqual(202, res);

            result = _session.GetConfigurations();
            if (result.Any(item => item.Name.Equals(name)))
            {
                Assert.Fail();
            }

        }


        [TestMethod]
        public void CRUDBlacklist()
        {
            List<Blacklist> result = _session.GetBlacklist();
            Assert.IsNotNull(result);

            result = new List<Blacklist>();
            result.Add(new Blacklist { Name = "net*" });
            var obj = _session.AddBlacklist(result);
            Assert.IsTrue(obj != null);

            result = _session.GetBlacklist();
            List<Blacklist> items = new List<Blacklist>();
            foreach (Blacklist item in result)
            {
                if (item.Name == "net*")
                {
                    item.Name = "net1*";
                    items.Add(item);
                }
            }

            List<Blacklist> res = _session.UpdateBlacklist(items);
            Assert.IsTrue(res != null);

            foreach (Blacklist item in items)
            {
                item.Name = "net*";
            }

            res = _session.UpdateBlacklist(items);
            Assert.IsTrue(res != null);

            result = _session.GetBlacklist();
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>();
            foreach (Blacklist item in result)
            {
                if (item.Name == "net*")
                {
                    ditems.Add(item.Id);
                }
            }

            int r = _session.RemoveBlacklist(ditems);
            Assert.IsTrue(r == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void CRUDCategory()
        {
            List<Category> result = _session.GetCategories();
            Assert.IsNotNull(result);

            result = new List<Category>();
            result.Add(new Category { Name = "NET" });
            var obj = _session.AddCategories(result);
            Assert.IsTrue(obj != null);

            result = _session.GetCategories();
            List<Category> items = new List<Category>();
            foreach (Category item in result)
            {
                if (item.Name == "NET")
                {
                    item.Name = "NET1*";
                    items.Add(item);
                    break;
                }
            }

            List<Category> res = _session.UpdateCategories(items);
            Assert.IsTrue(res != null);

            foreach (Category item in items)
            {
                item.Name = "NET";
            }

            res = _session.UpdateCategories(items);
            Assert.IsTrue(res != null);

            result = _session.GetCategories();
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>(); 
            foreach (Category item in result)
            {
                if (item.Name == "NET")
                {
                    ditems.Add(item.Id);
                }
            }

            int r = _session.RemoveCategories(ditems);
            Assert.IsTrue(r == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void CRUDQuery()
        {
            List<Query> result = _session.GetQueries();
            Assert.IsNotNull(result);

            result = new List<Query>(); 
            result.Add(new Query { Name = "NET", Content = "Data" });
            var obj = _session.AddQueries(result);
            Assert.IsTrue(obj != null);

            result = _session.GetQueries();
            List<Query> items = new List<Query>();
            foreach (Query item in result)
            {
                if (item.Name == "NET")
                {
                    item.Name = "NET1*";
                    items.Add(item);
                }
            }

            List<Query> res = _session.UpdateQueries(items);
            Assert.IsTrue(res != null);

            foreach (Query item in items)
            {
                item.Name = "NET";
            }

            res = _session.UpdateQueries(items);
            Assert.IsTrue(res != null);

            result = _session.GetQueries();
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>(); 
            foreach (Query item in result)
            {
                if (item.Name == "NET")
                {
                    ditems.Add(item.Id);
                }
            }

            int r = _session.RemoveQueries(ditems);
            Assert.IsTrue(r == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void CRUDSentimentPhrase()
        {
            List<SentimentPhrase> result = _session.GetSentimentPhrases();
            Assert.IsNotNull(result);

            result = new List<SentimentPhrase>();
            result.Add(new SentimentPhrase { Name = "NET", Weight = 0.1f });
            var obj = _session.AddSentimentPhrases(result);
            Assert.IsTrue(obj != null);

            result = _session.GetSentimentPhrases();
            List<SentimentPhrase> items = new List<SentimentPhrase>();
            foreach (SentimentPhrase item in result)
            {
                if (item.Name == "NET")
                {
                    item.Name = "NET1*";
                    items.Add(item);
                }
            }

            List<SentimentPhrase> res = _session.UpdateSentimentPhrases(items);
            Assert.IsTrue(res != null);

            foreach (SentimentPhrase item in items)
            {
                item.Name = "NET";
            }

            res = _session.UpdateSentimentPhrases(items);

            Assert.IsTrue(res != null);

            result = _session.GetSentimentPhrases();
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>();
            foreach (SentimentPhrase item in result)
            {
                if (item.Name == "NET")
                {
                    ditems.Add(item.Id);
                }
            }

            int r = _session.RemoveSentimentPhrases(ditems);
            Assert.IsTrue(r == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void CRUDEntity()
        {
            List<UserEntity> result = _session.GetEntities();
            Assert.IsNotNull(result);

            result = new List<UserEntity>();
            result.Add(new UserEntity { Name = "NET", Type = "Language" });
            var obj = _session.AddEntities(result);
            Assert.IsTrue(obj != null);

            result = _session.GetEntities();
            List<UserEntity> items = new List<UserEntity>();
            foreach (UserEntity item in result)
            {
                if (item.Name == "NET")
                {
                    item.Name = "NET1*";
                    items.Add(item);
                }
            }

            List<UserEntity> res = _session.UpdateEntities(items);
            Assert.IsTrue(res != null);

            foreach (UserEntity item in items)
            {
                item.Name = "NET";
            }

            res = _session.UpdateEntities(items);
            Assert.IsTrue(res != null);

            result = _session.GetEntities();
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>();
            foreach (UserEntity item in result)
            {
                if (item.Name == "NET")
                {
                    ditems.Add(item.Id);
                }
            }

            int r = _session.RemoveEntities(ditems);
            Assert.IsTrue(r == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void CRUDTaxonomy()
        {
            List<TaxonomyNode> result = _session.GetTaxonomy();
            Assert.IsNotNull(result);

            result = new List<TaxonomyNode>();
            result.Add(new TaxonomyNode { Name = "NET", Nodes = new List<TaxonomyNode>(new TaxonomyNode[] { new TaxonomyNode() { Name = "NETWORK_0" }, new TaxonomyNode() { Name = "NETWORK_1" } })});
            var obj = _session.AddTaxonomy(result);
            Assert.IsTrue(obj != null);

            result = _session.GetTaxonomy();
            List<TaxonomyNode> items = new List<TaxonomyNode>();
            foreach (TaxonomyNode item in result)
            {
                if (item.Name == "NET")
                {
                    item.Name = "NET1*";
                    item.Nodes = null;
                    item.Data = null;

                    items.Add(item);
                }
            }

            List<TaxonomyNode> res = _session.UpdateTaxonomy(items);
            Assert.IsTrue(res != null);

            foreach (TaxonomyNode item in items)
            {
                item.Name = "NET";
                item.Nodes = null;
                item.Data = null;
            }

            res = _session.UpdateTaxonomy(items);
            Assert.IsTrue(res != null);

            result = _session.GetTaxonomy();
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>();
            foreach (TaxonomyNode item in result)
            {
                if (item.Name == "NET")
                {
                    ditems.Add(item.Id);
                }
            }

            int r = _session.RemoveTaxonomy(ditems);
            Assert.IsTrue(r == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void Document()
        {
            Document task = new Document();
            task.Id = _id;
            task.Text = _message;

            int res = _session.QueueDocument(task);
            Assert.IsTrue((res == 200 || res == 202), "RESULT: " + res);

            DocAnalyticData data = _session.GetDocument(_id);
            Assert.IsNotNull(data);

            int r = _session.CancelDocument(_id);
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

            int res = _session.QueueBatchOfDocuments(list);
            Assert.IsTrue((res == 200 || res == 202), "RESULT: " + res);

            System.Threading.Thread.Sleep(5000);
            
            IList<DocAnalyticData> data = _session.GetProcessedDocuments();
            Assert.IsNotNull(data);
        }

        [TestMethod]
        public void Collection()
        {
            Collection task = new Collection();
            task.Id = _id;
            task.Documents = new List<string>() { _message + "1", _message + "2" };

            int res = _session.QueueCollection(task);
            Assert.IsTrue((res == 200 || res == 202), "RESULT: " + res);

            task = new Collection();
            task.Id = System.Guid.NewGuid().ToString();
            task.Documents = new List<string>() { _message + "1", _message + "2" };
            res = _session.QueueCollection(task);
            Assert.IsTrue((res == 200 || res == 202), "RESULT: " + res);

            CollAnalyticData data = _session.GetCollection(_id);
            Assert.IsNotNull(data);

            int r = _session.CancelCollection(_id);
            Assert.IsTrue((r == 200 || r == 202 || r == 404), "RESULT: " + res);

            IList<CollAnalyticData> ad = _session.GetProcessedCollections();
            Assert.IsNotNull(ad);
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
