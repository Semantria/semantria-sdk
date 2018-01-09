using System.Collections.Generic;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using System.Collections;
using System.Diagnostics;
using System.Linq;

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

        // Set environment vars before calling this program
        // or edit this file and put your key and secret here.
        private string _consumerKey = System.Environment.GetEnvironmentVariable("SEMANTRIA_KEY");
        private string _consumerSecret = System.Environment.GetEnvironmentVariable("SEMANTRIA_SECRET");

        private string _id = "3E08B37B-0D74-4BF0-9380-E4D7E8C0279E"; 
        private string _message = "Amazon Web Services has announced a new feature called VM₤Ware Import, which allows IT departments to move virtual machine images from their internal data centers to the cloud.";
        private string _testConfig = null;

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

            dynamic temp = _session.GetConfigurations();
            var configsList = ((IEnumerable)temp).Cast<dynamic>();
            dynamic config = null;
            // Updates or Creates new configuration for the test purposes.
            if (!configsList.Any(item => item.name.Equals("NetTest")))
            {
                config = new { name = "NetTest", language = "English", is_primary = false, auto_response = false };
                config = _session.AddConfigurations(new[] { config });
                config = config[0];
            }
            else
            {
                config = configsList.First(item => item.name.Equals("NetTest"));
                   
            }
            _testConfig = config.id;
        }
        
        // Use TestCleanup to run code after each test has run
        [TestCleanup()]
        public void MyTestCleanup() 
        {
            _session.RemoveConfigurations(new[] { _testConfig });
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
            dynamic res = _session.GetStatus();
            Assert.IsNotNull(res);
        }

        [TestMethod]
        public void GetSubscription()
        {
            dynamic sub = _session.GetSubscription();
            Assert.IsNotNull(sub);
        }

        [TestMethod]
        public void GetStatistics()
        {
            dynamic stat = _session.GetStatistics(_testConfig, "hour");
            if (stat == null)
            {
                Debug.WriteLine("statistics returned null."
                    + " This usually means that there is no usage for the given interval."
                    + " Try again in a few minutes");
            }
        }

        [TestMethod]
        public void GetSupportedFeatures()
        {
            dynamic features = _session.GetSupportedFeatures();
            Assert.IsNotNull(features);
        }

        [TestMethod]
        public void CRUDConfiguration()
        {
            dynamic result = _session.GetConfigurations();
            Assert.IsNotNull(result);

            string configId = result[0].id;
            int threshold = result[0].alphanumeric_threshold;
                       
            result = _session.UpdateConfigurations(new [] { new { id = configId, alphanumeric_threshold = threshold + 1 } });
            Assert.IsNotNull(result);
            Assert.AreEqual(configId, result[0].id);
            Assert.AreEqual(threshold + 1, result[0].alphanumeric_threshold);

            result = _session.UpdateConfigurations(new[] { new { id = configId, alphanumeric_threshold = threshold } });
            Assert.IsNotNull(result);
        }

        [TestMethod]
        public void CloneConfiguration()
        {
            var result = _session.CloneConfiguration("TestCloneConfig_NET", _testConfig);
            Assert.IsNotNull(result);

            dynamic temp = _session.GetConfigurations();
            var configsList = ((IEnumerable)temp).Cast<dynamic>();
            if (!configsList.Any(item => item.name.Equals("TestCloneConfig_NET")))
            {
                Assert.Fail("New Config not found");
            }
            else
            {
                dynamic config = configsList.First(item => item.name.Equals("NetTest"));
                var res = _session.RemoveConfigurations(new[]{config.id});
                Assert.IsTrue(res == 202);
            }

            
        }



        [TestMethod]
        public void CRUDBlacklist()
        {
            dynamic result = _session.GetBlacklist(_testConfig);

            // add 'net*' to blacklist
            result = new List<dynamic>();
            result.Add(new { name = "net*" });
            var obj = _session.AddBlacklist(result, _testConfig);
            Assert.IsNotNull(obj);

            // Note, this will get all blacklist items compared to AddBlacklist, above,
            // which only returns the added items.
            result = _session.GetBlacklist(_testConfig);
            // item = ((IEnumerable) result).FirstOrDefault(x => x.name.Equals("net*"));
            Assert.IsNotNull(result);
            
            // change 'net*' to 'net1*'
            List<dynamic> newItems = new List<dynamic>();
            foreach (dynamic item in result)
            {
                if (item.name == "net*")
                {
                    newItems.Add(new { name = "net1*", id = item.id });
                }
            }
            result = _session.UpdateBlacklist(newItems, _testConfig);
            Assert.IsNotNull(result);
              
            // Delete that blacklist item         
            List<string> ditems = new List<string>();
            foreach (dynamic item in result)
            {
                if (item.name == "net1*")
                {
                    ditems.Add(item.id);
                }
            }

            int r = _session.RemoveBlacklist(ditems, _testConfig);
            Assert.IsTrue(r == 202, "RESULT: "/* + res */);
        }

        [TestMethod]
        public void CRUDCategory()
        {
            dynamic result = _session.GetCategories(_testConfig);

            result = new List<dynamic>();
            result.Add(new { name = "NET" });
            var obj = _session.AddCategories(result, _testConfig);
            Assert.IsNotNull(obj);

            result = _session.GetCategories(_testConfig);
            Assert.IsNotNull(result);

            List<dynamic> newItems = new List<dynamic>();
            foreach (dynamic item in result)
            {
                if (item.name == "NET")
                {
                    newItems.Add(new { id = item.id, name = "NET1*" } );
                    break;
                }
            }

            result = _session.UpdateCategories(newItems, _testConfig);
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>(); 
            foreach (dynamic item in result)
            {
                if (item.name == "NET1*")
                {
                    ditems.Add(item.id);
                }
            }

            int r = _session.RemoveCategories(ditems, _testConfig);
            Assert.IsTrue(r == 202, "RESULT: " + r);
        }

        [TestMethod]
        public void CRUDQuery()
        {
            dynamic result = _session.GetQueries(_testConfig);

            result = new List<dynamic>();
            result.Add(new { name = "NET", query = "Data" });
            var obj = _session.AddQueries(result, _testConfig);
            Assert.IsNotNull(obj);

            result = _session.GetQueries(_testConfig);
            Assert.IsNotNull(result);

            List<dynamic> newItems = new List<dynamic>();
            foreach (dynamic item in result)
            {
                if (item.name == "NET")
                {
                    newItems.Add(new { id = item.id, name = "NET1*" } );
                }
            }

            result = _session.UpdateQueries(newItems, _testConfig);
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>(); 
            foreach (dynamic item in result)
            {
                if (item.name == "NET1*")
                {
                    ditems.Add(item.id);
                }
            }

            int r = _session.RemoveQueries(ditems, _testConfig);
            Assert.IsTrue(r == 202, "RESULT: " + r);
        }

        [TestMethod]
        public void CRUDSentimentPhrase()
        {
            dynamic result = _session.GetSentimentPhrases(_testConfig);

            result = new List<dynamic>();
            result.Add(new { name = "NET", weight = 0.1f });
            var obj = _session.AddSentimentPhrases(result, _testConfig);
            Assert.IsNotNull(obj);

            result = _session.GetSentimentPhrases(_testConfig);
            Assert.IsNotNull(result);

            List<dynamic> newItems = new List<dynamic>();
            foreach (dynamic item in result)
            {
                if (item.name == "NET")
                {
                    newItems.Add(new { id = item.id, name = "NET1*" } );
                }
            }

            result = _session.UpdateSentimentPhrases(newItems, _testConfig);
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>();
            foreach (dynamic item in result)
            {
                if (item.name == "NET1*")
                {
                    ditems.Add(item.id);
                }
            }

            int r = _session.RemoveSentimentPhrases(ditems, _testConfig);
            Assert.IsTrue(r == 202, "RESULT: " + r);
        }

        [TestMethod]
        public void CRUDEntity()
        {
            dynamic result = _session.GetEntities(_testConfig);

            result = new List<dynamic>();
            result.Add(new { name = "NET", type = "Language" });
            var obj = _session.AddEntities(result, _testConfig);
            Assert.IsNotNull(obj);

            result = _session.GetEntities(_testConfig);
            Assert.IsNotNull(result);

            List<dynamic> newItems = new List<dynamic>();
            foreach (dynamic item in result)
            {
                if (item.name == "NET")
                {
                    newItems.Add(new { id = item.id, name = "NET1*" } );
                }
            }

            result = _session.UpdateEntities(newItems, _testConfig);
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>();
            foreach (dynamic item in result)
            {
                if (item.name == "NET1*")
                {
                    ditems.Add(item.id);
                }
            }

            int r = _session.RemoveEntities(ditems, _testConfig);
            Assert.IsTrue(r == 202, "RESULT: " + r);
        }

        [TestMethod]
        public void CRUDTaxonomy()
        {
            dynamic result = _session.GetTaxonomy(_testConfig);
            
            result = new List<dynamic>();
            result.Add(new { name = "NET", nodes = new [] { new { name = "NETWORK_0" }, new  { name = "NETWORK_1" } }});
            var obj = _session.AddTaxonomy(result, _testConfig);
            Assert.IsNotNull(obj);

            result = _session.GetTaxonomy(_testConfig);
            Assert.IsNotNull(result);

            List<dynamic> newItems = new List<dynamic>();
            foreach (dynamic item in result)
            {
                if (item.name == "NET")
                {
                    newItems.Add(new { id = item.id, name = "NET_1*" } );
                    break;
                }
            }

            result = _session.UpdateTaxonomy(newItems, _testConfig);
            Assert.IsNotNull(result);

            List<string> ditems = new List<string>();
            foreach (dynamic item in result)
            {
                if (item.name == "NET_1*")
                {
                    ditems.Add(item.id);
                }
            }

            int r = _session.RemoveTaxonomy(ditems, _testConfig);
            Assert.IsTrue(r == 202, "RESULT: " + r);
        }

        [TestMethod]
        public void Document()
        {
            dynamic task = new { id = _id, text = _message };

            int result = _session.QueueDocument(task, _testConfig);
            Assert.IsTrue((result == 200 || result == 202), "RESULT: " + result);

            dynamic data = _session.GetDocument(_id, _testConfig);
            Assert.IsNotNull(data);

            result = _session.CancelDocument(_id, _testConfig);
            Assert.IsTrue((result == 200 || result == 202 || result == 404), "RESULT: " + result);
        }

        [TestMethod]
        public void Batch()
        {
            List<dynamic> list = new List<dynamic>();
            for (int i = 0; i < 5; i++)
            {
                dynamic task = new
                {
                    id = System.Guid.NewGuid().ToString(),
                    text = _message
                };
                list.Add(task);
            }

            int res = _session.QueueBatchOfDocuments(list, _testConfig);
            Assert.IsTrue((res == 200 || res == 202), "RESULT: " + res);

            System.Threading.Thread.Sleep(5000);

            dynamic data = _session.GetProcessedDocuments(_testConfig);
            Assert.IsNotNull(data);
        }

        [TestMethod]
        public void Collection()
        {
            dynamic task = new
            {
                id = _id,
                documents = new[] { _message + "1", _message + "2" }
            };

            int result = _session.QueueCollection(task, _testConfig);
            Assert.IsTrue((result == 200 || result == 202), "RESULT: " + result);

            task = new
            {
                id = System.Guid.NewGuid().ToString(),
                documents = new[] { _message + "1", _message + "2" }
            };
            result = _session.QueueCollection(task, _testConfig);
            Assert.IsTrue((result == 200 || result == 202), "RESULT: " + result);

            dynamic data = _session.GetCollection(_id, _testConfig);
            Assert.IsNotNull(data);

            result = _session.CancelCollection(_id, _testConfig);
            Assert.IsTrue((result == 200 || result == 202 || result == 404), "RESULT: " + result);

            dynamic ad = _session.GetProcessedCollections(_testConfig);
            Assert.IsNotNull(ad);
        }
    
        void session_Request(object sender, RequestEventArgs e)
        {
            Debug.WriteLine("REQUEST: {0} {1} - message: {2}", e.Method, e.Url, e.Message);
        }

        void session_Response(object sender, ResponseEventArgs e)
        {
            Debug.WriteLine("RESPONSE: {0}({1}) - message: {2}", e.Status, (int)e.Status, e.Message);
            bool res = (e.Status == System.Net.HttpStatusCode.OK || e.Status == System.Net.HttpStatusCode.Accepted || e.Status == System.Net.HttpStatusCode.NotFound);
            Assert.IsTrue(res);
        }

        void session_Error(object sender, ResponseEventArgs e)
        {
            Debug.WriteLine("ERROR: {0}({1}) - message: {2}", e.Status, (int)e.Status, e.Message);
        }

        static void session_DocsAutoResponse(object sender, DocsAutoResponseEventArgs e)
        {
            int count = e.AnalyticData.Count;
            Debug.WriteLine("DOCSAUTORESPONSE: {0}", count);
            Assert.IsNotNull(e.AnalyticData);
        }

        static void session_CollsAutoResponse(object sender, CollsAutoResponseEventArgs e)
        {
            int count = e.AnalyticData.Count;
            Debug.WriteLine("COLLSAUTORESPONSE: {0}", count);
            Assert.IsNotNull(e.AnalyticData);
        }
 
    }
}
