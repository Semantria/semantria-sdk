using System;
using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Diagnostics;
using System.Text;

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

        private string _consumerKey = "";
        private string _consumerSecret = "";

        private string _id = "74215b37-8144-4811-a7de-c5b642aa6db1"; 
        private string _message = "Amazon Web Services has announced a new feature called VM₤Ware Import, which allows IT departments to move virtual machine images from their internal data centers to the cloud.";
        
        private ISerializer _serializer = null;
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
            _serializer = new JsonSerializer();
            _session = Session.CreateSession(_consumerKey, _consumerSecret, _serializer);

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
            _serializer = null;
            _session = null;
        }
        
        #endregion

        [TestMethod]
        public void CheckSession()
        {
            Assert.IsNotNull(_serializer);
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
        public void GetConfigurations()
        {
            List<Configuration> result = _session.GetConfigurations();
            Assert.IsNotNull(result);
            if (result == null) return;
        }

        [TestMethod]
        public void UpdateConfiguration()
        {
			List<Configuration> result = _session.GetConfigurations();
            Assert.IsNotNull(result);
            if (result == null) return;

            foreach (Configuration item in result)
            {
                if (item.IsPrimary == true)
                {
                    item.AutoResponse = false;
                    item.CharsThreshold = 50;
					item.OneSentence = true;
                }
            }

            int res = _session.UpdateConfigurations(result);
            Assert.IsTrue(res == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void GetBlacklist()
        {
            List<string> result = _session.GetBlacklist();
            Assert.IsNotNull(result);
        }

        [TestMethod]
        public void AddBlacklist()
        {
            List<string> result = new List<string>();
            result.Add("net*");
            int res = _session.AddBlacklist(result);
            Assert.IsTrue(res == 202, "RESULT: " + res);
            
            //Remove created
            //RemoveBlacklist();
        }

        [TestMethod]
        public void RemoveBlacklist()
        {
            List<string> result = _session.GetBlacklist();
            Assert.IsNotNull(result);

            List<string> items = new List<string>(); 
            foreach (string item in result)
            {
                if (item == "net*")
                {
                    items.Add(item);
                }
            }

            int res = _session.RemoveBlacklist(items);
            Assert.IsTrue(res == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void GetCategories()
        {
            List<Category> result = _session.GetCategories();
            Assert.IsNotNull(result);
        }

        [TestMethod]
        public void AddCategories()
        {
            List<Category> result = new List<Category>();
            result.Add(new Category { Name = "NET" });
            int res = _session.AddCategories(result);
            Assert.IsTrue(res == 202, "RESULT: " + res);

            //Remove created
            //RemoveCategories();
        }

        [TestMethod]
        public void RemoveCategories()
        {
            List<Category> result = _session.GetCategories();
            Assert.IsNotNull(result);

            List<Category> items = new List<Category>(); 
            foreach (Category item in result)
            {
                if (item.Name == "NET")
                {
                    items.Add(item);
                }
            }

            int res = _session.RemoveCategories(items);
            Assert.IsTrue(res == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void GetQueries()
        {
            List<Query> result = _session.GetQueries();
            Assert.IsNotNull(result);
        }

        [TestMethod]
        public void AddQueries()
        {
            List<Query> result = new List<Query>(); 
            result.Add(new Query { Name = "NET", Content = "Data" });
            int res = _session.AddQueries(result);
            Assert.IsTrue(res == 202, "RESULT: " + res);

            //Remove created
            //RemoveQueries();
        }

        [TestMethod]
        public void RemoveQueries()
        {
            List<Query> result = _session.GetQueries();
            Assert.IsNotNull(result);

            List<Query> items = new List<Query>(); 
            foreach (Query item in result)
            {
                if (item.Name == "NET")
                {
                    items.Add(item);
                }
            }

            int res = _session.RemoveQueries(items);
            Assert.IsTrue(res == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void GetSentimentPhrases()
        {
            List<SentimentPhrase> result = _session.GetSentimentPhrases();
            Assert.IsNotNull(result);
        }

        [TestMethod]
        public void AddSentimentPhrases()
        {
            List<SentimentPhrase> result = new List<SentimentPhrase>();
            result.Add(new SentimentPhrase { Name = "NET", Weight = 0.1f });
            int res = _session.AddSentimentPhrases(result);
            Assert.IsTrue(res == 202, "RESULT: " + res);

            //Remove created
            //RemoveSentimentPhrases();
        }

        [TestMethod]
        public void RemoveSentimentPhrases()
        {
            List<SentimentPhrase> result = _session.GetSentimentPhrases();
            Assert.IsNotNull(result);

            List<SentimentPhrase> items = new List<SentimentPhrase>(); 
            foreach (SentimentPhrase item in result)
            {
                if (item.Name == "NET")
                {
                    items.Add(item);
                }
            }

            int res = _session.RemoveSentimentPhrases(items);
            Assert.IsTrue(res == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void GetEntities()
        {
            List<UserEntity> result = _session.GetEntities();
            Assert.IsNotNull(result);
        }

        [TestMethod]
        public void AddEntities()
        {
            List<UserEntity> result = new List<UserEntity>();
            result.Add(new UserEntity { Name = "NET", Type = "Language" });
            int res = _session.UpdateEntities(result);
            Assert.IsTrue(res == 202, "RESULT: " + res);

            // Remove created entity
            RemoveEntities();
        }

        [TestMethod]
        public void RemoveEntities()
        {
            List<UserEntity> result = _session.GetEntities();
            Assert.IsNotNull(result);

            List<UserEntity> items = new List<UserEntity>();
            foreach (UserEntity item in result)
            {
                if (item.Name == "NET")
                {
                    items.Add(item);
                }
            }

            int res = _session.UpdateEntities(items);
            Assert.IsTrue(res == 202, "RESULT: " + res);
        }

        [TestMethod]
        public void CreateDocument()
        {
            Document task = new Document();
            task.Id = _id;
            task.Text = _message;

            int res = _session.QueueDocument(task);
            Assert.IsTrue((res == 200 || res == 202), "RESULT: " + res);
        }

        [TestMethod]
        public void GetDocument()
        {
            DocAnalyticData data = _session.GetDocument(_id);
            Assert.IsNotNull(data);
        }

        [TestMethod]
        public void CancelDocument()
        {
            int res = _session.CancelDocument(_id);
            Assert.IsTrue((res == 200 || res == 202 || res == 404), "RESULT: " + res);
        }

        [TestMethod]
        public void CreateBatch()
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
        }

        [TestMethod]
        public void GetProcessedDocuments()
        {
            IList<DocAnalyticData> data = _session.GetProcessedDocuments();
            Assert.IsNotNull(data);
        }

        [TestMethod]
        public void CreateCollection()
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
        }

        [TestMethod]
        public void GetCollection()
        {
            CollAnalyticData data = _session.GetCollection(_id);
            Assert.IsNotNull(data);
        }

        [TestMethod]
        public void CancelCollection()
        {
            int res = _session.CancelCollection(_id);
            Assert.IsTrue((res == 200 || res == 202 || res == 404), "RESULT: " + res);
        }

        [TestMethod]
        public void GetProcessedCollections()
        {
            IList<CollAnalyticData> data = _session.GetProcessedCollections();
            Assert.IsNotNull(data);
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
