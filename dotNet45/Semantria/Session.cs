using System;
using System.Collections.Generic;
using Semantria.Com.Serializers;
using System.Collections;
using System.Linq;

namespace Semantria.Com
{
    public sealed class Session : IDisposable
    {
        #region Constructor
        
        private Session()
        {
        }
        
        private Session(string consumerKey, string consumerSecret, string appName)
        {
            _consumerKey = consumerKey;
            _consumerSecret = consumerSecret;
            _appName = appName;
        }

        private string GetAppName()
        {
            string appName = _appName;
            Version version = System.Reflection.Assembly.GetExecutingAssembly().GetName().Version;

            if (!String.IsNullOrEmpty(appName))
            {
                appName = String.Format("{0}/{1}/{2}/{3}", appName, WRAPPER_NAME, version, _format.ToUpper());
            } 
            else 
            {
                appName = String.Format("{0}/{1}/{2}", WRAPPER_NAME, version, _format.ToUpper());
            }
            return appName;
        }

        #endregion Constructor

        #region IDisposable Members

        public void Dispose()
        {
        }

        #endregion

        #region Private variables

        private string _consumerKey = string.Empty;
        private string _consumerSecret = string.Empty;
        private string _appName = string.Empty;
        private ISerializer _serializer = new JsonSerializer();
        private string _format = "json";
        private string _host = "https://api.semantria.com";
        private bool _useCompression = false;
        private string _apiVersion = "3.9";
        private const string WRAPPER_NAME = "dotNet";

        #endregion

        #region Properties

        public string Host
        {
            get { return _host; }
            set { _host = value; } 
        }

        public bool UseCompression  
        {
            get { return _useCompression; }
            set { _useCompression = value; }
        }

        public string APIversion
        {
            get { return _apiVersion; }
            set { _apiVersion = value; }
        }

        #endregion

        #region Events

        public delegate void RequestHandler(object sender, RequestEventArgs e);
        public event RequestHandler Request;
        internal void OnRequest(object sender, RequestEventArgs e)
        {
            if (Request != null)
            {
                Request(sender, e);
            }
        }

        public delegate void ResponseHandler(object sender, ResponseEventArgs e);  
        public event ResponseHandler Response;  
        internal void OnResponse(object sender, ResponseEventArgs e)  
        {  
            if (Response != null)  
            {
                Response(sender, e);  
            }  
        }

        public delegate void ErrorHandler(object sender, ResponseErrorEventArgs e);
        public event ErrorHandler Error;
        internal void OnError(object sender, ResponseErrorEventArgs e)
        {
            if (Error != null)
            {
                Error(sender, e);
            }
            else
            {
                throw new System.Web.HttpException((int)e.Status, e.Message);
            }
        }

        public delegate void DocsAutoResponseHandler(object sender, DocsAutoResponseEventArgs e);
        public event DocsAutoResponseHandler DocsAutoResponse;
        internal void OnDocsAutoResponse(object sender, DocsAutoResponseEventArgs e)
        {
            if (DocsAutoResponse != null)
            {
                DocsAutoResponse(sender, e);
            }
        }

        public delegate void CollsAutoResponseHandler(object sender, CollsAutoResponseEventArgs e);
        public event CollsAutoResponseHandler CollsAutoResponse;
        internal void OnCollsAutoResponse(object sender, CollsAutoResponseEventArgs e)
        {
            if (CollsAutoResponse != null)
            {
                CollsAutoResponse(sender, e);
            }
        } 

        #endregion

        #region Public Static

        public static Session CreateSession(string consumerKey, string consumerSecret)
        {
            return CreateSession(consumerKey, consumerSecret, (string)null);
        }

        public static Session CreateSession(string consumerKey, string consumerSecret, string appName)
        {
            return new Session(consumerKey, consumerSecret, appName);
        }

   
        #endregion

        #region API Status
 
        public void RegisterSerializer(ISerializer serializer)
        {
            if (serializer == null)
            {
                throw new ArgumentNullException("serializer");
            }
            
            _serializer = serializer;
            _format = serializer.Type();
        }

        public dynamic GetStatus()
        {
            //GET https://api.semantria.com/status.json
            string url = String.Format("{0}/status.{1}", _host, _format);
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return this.ProcessGetResponse(authResponse);
        }

        public dynamic GetSubscription()
        {
            //GET https://api.semantria.com/subscription.json
            string url = String.Format("{0}/subscription.{1}", _host, _format);
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return this.ProcessGetResponse(authResponse);
        }

        public dynamic GetStatistics(string configId = null, string interval = null)
        {
            //GET https://api.semantria.com/statistics.json
            string url = String.Format("{0}/statistics.{1}", _host, _format);
            
            if (!String.IsNullOrEmpty(configId) && !String.IsNullOrEmpty(interval))
            {
                url = String.Format("{0}/statistics.{1}?config_id={2}&interval={3}", _host, _format, configId, interval);
            }
            else if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/statistics.{1}?config_id={2}", _host, _format, configId);
            }
            else if (!String.IsNullOrEmpty(interval))
            {
                url = String.Format("{0}/statistics.{1}?interval={2}", _host, _format, interval);
            }
            
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return this.ProcessGetResponse(authResponse);
        }

        public dynamic GetSupportedFeatures(string language = null)
        {
            //GET https://api.semantria.com/features.json
            string url = String.Format("{0}/features.{1}", _host, _format);

            if (!String.IsNullOrEmpty(language))
            {
                url = String.Format("{0}/features.{1}?language={2}", _host, _format, language);
            }

            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            dynamic obj = this.ProcessGetResponse(authResponse);
            
            return obj;
        }
       
        #endregion

        #region Configuration

        public dynamic GetConfigurations()
        {
            AuthResponse authResponse = this.Get("configurations");
            dynamic obj = this.ProcessGetResponse(authResponse);
            return obj;
        }


        public int AddConfigurations(dynamic items)
        {
            return this.Send(QueryMethod.POST, items, "configurations");
        }

        public int UpdateConfigurations(dynamic items)
        {
            return this.Send(QueryMethod.POST, items, "configurations");
        }

        public int RemoveConfigurations(dynamic items)
        {
            return this.Send(QueryMethod.DELETE, items, "configurations");
        }

        public int CloneConfiguration(string name, string template)
        {
            var items = new List<dynamic>();
            items.Add(new { name = name, template = template });

            return this.AddConfigurations(items);
        }

        #endregion Configuration

        #region Blacklist

        public dynamic GetBlacklist(string configId = null)
        {
            AuthResponse authResponse = this.Get("blacklist", configId);
            dynamic obj = this.ProcessGetResponse(authResponse);
            return obj;
        }

        public int AddBlacklist(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.POST, items, "blacklist", configId);
        }

        public int RemoveBlacklist(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.DELETE, items, "blacklist", configId);
        }

        #endregion Blacklist

        #region Category

        public dynamic GetCategories(string configId = null)
        {
            AuthResponse authResponse = this.Get("categories", configId);
            return this.ProcessGetResponse(authResponse);
        }

        public int AddCategories(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.POST, items, "categories", configId);
        }

        public int UpdateCategories(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.POST, items, "categories", configId);
        }

        public int RemoveCategories(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.DELETE, items, "categories", configId);
        }

        #endregion Category

        #region Query

        public dynamic GetQueries(string configId = null)
        {
            AuthResponse authResponse = this.Get("queries", configId);
            return this.ProcessGetResponse(authResponse);
        }

        public int AddQueries(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.POST, items, "queries", configId);
        }

        public int UpdateQueries(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.POST, items, "queries", configId);
        }

        public int RemoveQueries(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.DELETE, items, "queries", configId);
        }

        #endregion Query

        #region Entity

        public dynamic GetEntities(string configId = null)
        {
            AuthResponse authResponse = this.Get("entities", configId);
            return this.ProcessGetResponse(authResponse);
        }

        public int AddEntities(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.POST, items, "entities", configId);
        }

        public int UpdateEntities(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.POST, items, "entities", configId);
        }

        public int RemoveEntities(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.DELETE, items, "entities", configId);
        }

        #endregion Entity

        #region SentimentPhrase

        public dynamic GetSentimentPhrases(string configId = null)
        {
            AuthResponse authResponse = this.Get("phrases", configId);
            return this.ProcessGetResponse(authResponse);
        }

        public int AddSentimentPhrases(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.POST, items, "phrases", configId);
        }

        public int UpdateSentimentPhrases(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.POST, items, "phrases", configId);
        }

        public int RemoveSentimentPhrases(dynamic items, string configId = null)
        {
            return this.Send(QueryMethod.DELETE, items, "phrases", configId);
        }

        #endregion SentimentPhrase

        #region Document

        public int QueueDocument(dynamic task, string configId = null)
        {
            string url = String.Format("{0}/document.{1}", _host, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document.{1}?config_id={2}", _host, _format, configId);
            }
            string data = _serializer.Serialize(task);
            dynamic result = this.PostAnalyticData(url, data);

            if (result != null)
            {
                if (result.Count > 0)
                {
                    OnDocsAutoResponse(this, new DocsAutoResponseEventArgs(result));
                    return 200;
                }
                else
                {
                    return 202;
                }
            }
            return -1;
        }

        public int QueueBatchOfDocuments(dynamic tasks, string configId = null)
        {
            string url = String.Format("{0}/document/batch.{1}", _host, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document/batch.{1}?config_id={2}", _host, _format, configId);
            }

            dynamic result = this.PostQueueBatch(url, tasks);

            if (result != null)
            {
                if (result.Count > 0)
                {
                    OnDocsAutoResponse(this, new DocsAutoResponseEventArgs(result));
                    return 200;
                }
                else
                {
                    return 202;
                }
            }
            return -1;
        }

        public dynamic GetDocument(string id, string configId = null)
        {
            string url = String.Format("{0}/document/{1}.{2}", _host, id, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document/{1}.{2}?config_id={3}", _host, id, _format, configId);
            }
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return this.ProcessGetResponse(authResponse);
        }

        public int CancelDocument(string id, string configId = null)
        {
            string url = String.Format("{0}/document/{1}.{2}", _host, id, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document/{1}.{2}?config_id={3}", _host, id, _format, configId);
            }
            AuthResponse authResponse = this.RunRequest(QueryMethod.DELETE, url, null);
            return this.ProcessPostResponse(authResponse);
        }

        public List<dynamic> GetProcessedDocuments(string configId = null)
        {
            string url = String.Format("{0}/document/processed.{1}", _host, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document/processed.{1}?config_id={2}", _host, _format, configId);
            }
            return this.RequestProcessedList(url);
        }

        public List<dynamic> GetProcessedDocumentsByJobId(string jobId)
        {
            if (String.IsNullOrEmpty(jobId))
            {
                throw new ArgumentNullException("jobId", "jobId parameter can not be null or string empty.");
            }

            string url = String.Format("{0}/document/processed.{1}?job_id={2}", _host, _format, jobId);
            return this.RequestProcessedList(url);
        }

        #endregion Document

        #region Collection

        public int QueueCollection(dynamic task, string configId = null)
        {
            string url = String.Format("{0}/collection.{1}", _host, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/collection.{1}?config_id={2}", _host, _format, configId);
            }

            string data = _serializer.Serialize(task);
            dynamic result = this.PostAnalyticData(url, data);

            if (result != null)
            {
                if (result.Count > 0)
                {
                    OnCollsAutoResponse(this, new CollsAutoResponseEventArgs(result));
                    return 200;
                }
                else
                {
                    return 202;
                }
            }
            return -1;
        }

        public dynamic GetCollection(string id, string configId = null)
        {
            string url = String.Format("{0}/collection/{1}.{2}", _host, id, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/collection/{1}.{2}?config_id={3}", _host, id, _format, configId);
            }
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return this.ProcessGetResponse(authResponse);
        }

        public int CancelCollection(string id, string configId = null)
        {
            string url = String.Format("{0}/collection/{1}.{2}", _host, id, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/collection/{1}.{2}?config_id={3}", _host, id, _format, configId);
            }
            AuthResponse authResponse = this.RunRequest(QueryMethod.DELETE, url, null);
            return this.ProcessPostResponse(authResponse);
        }

        public List<dynamic> GetProcessedCollections(string configId = null)
        {
            string url = String.Format("{0}/collection/processed.{1}", _host, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/collection/processed.{1}?config_id={2}", _host, _format, configId);
            }
            return this.RequestProcessedList(url);
        }

        public List<dynamic> GetProcessedCollectionsByJobId(string jobId)
        {
            if (String.IsNullOrEmpty(jobId))
            {
                throw new ArgumentNullException("jobId", "jobId parameter can not be null or string empty.");
            }

            string url = String.Format("{0}/collection/processed.{1}?job_id={2}", _host, _format, jobId);
            return this.RequestProcessedList(url);
        }

        #endregion Collection

        #region Private request and response support

        private AuthResponse Get(string tag, string configId = null)
        {
            string url = String.Format("{0}/{1}.{2}", _host, tag, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/{1}.{2}?config_id={3}", _host, tag, _format, configId);
            }

            return this.RunRequest(QueryMethod.GET, url, null);
        }

        private int Send(QueryMethod method, dynamic items, string tag, string configId = null)
        {
            string url = String.Format("{0}/{1}.{2}", _host, tag, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/{1}.{2}?config_id={3}", _host, tag, _format, configId);
            }

            string data = null;
            data = _serializer.Serialize(items);

            AuthResponse authResponse = this.RunRequest(method, url, data);
            return this.ProcessPostResponse(authResponse);
        }

        private AuthResponse RunRequest(QueryMethod method, string url, string data)
        {
            string appName = this.GetAppName();
            AuthRequest authRequest = new AuthRequest(_consumerKey, _consumerSecret, appName, _useCompression, _apiVersion);
            using (authRequest)
            {
                OnRequest(this, new RequestEventArgs(method.ToString(), url, data));
                AuthResponse authResponse = authRequest.AuthWebRequest(method, url, data);
                OnResponse(this, new ResponseEventArgs(authResponse.Status, authResponse.Data));
                return authResponse;
            }
        }

        private List<dynamic> RequestProcessedList(string url)
        {
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            dynamic obj = new List<dynamic>();

            dynamic result = this.ProcessGetResponse(authResponse);
            if (result != null)
            {
                obj = new List<dynamic>(((IEnumerable)result).Cast<dynamic>()); ;
            }
            return obj;
        }

        private dynamic ProcessGetResponse(AuthResponse authResponse)
        {
            int status = (int)authResponse.Status;
            string message = authResponse.Data;
            dynamic obj = null;

            switch (status)
            {
                case 200:
                    obj = _serializer.Deserialize(message);
                    return obj;
                case 202:
                    return obj;
                default:
                    this.ResolveError(authResponse);
                    return obj;
            }
        }

        private int ProcessPostResponse(AuthResponse authResponse)
        {
            int status = (int)authResponse.Status;
            string message = authResponse.Data;

            switch (status)
            {
                case 200:
                case 202:
                    return status;
                default:
                    this.ResolveError(authResponse);
                    return status;
            }
        }

        private dynamic PostQueueBatch(string url, dynamic tasks)
        {
            string data = _serializer.Serialize(tasks);
            return this.PostAnalyticData(url, data);
        }

        private dynamic PostAnalyticData(string url, string data)
        {
            AuthResponse authResponse = this.RunRequest(QueryMethod.POST, url, data);

            int status = (int)authResponse.Status;
            string message = authResponse.Data;

            dynamic obj = new List<dynamic>();

            switch (status)
            {
                case 200:
                    {
                        dynamic result = this.ProcessGetResponse(authResponse);
                        if (result != null) obj = result;
                        return obj;
                    }
                case 202:
                    return obj;
                default:
                    this.ResolveError(authResponse);
                    return null;
            }
        }

        private void ResolveError(AuthResponse authResponse)
        {
            int status = (int)authResponse.Status;
            string message = authResponse.Data;
            OnError(this, new ResponseErrorEventArgs(null, authResponse.Status, message));
        }


        #endregion
    }
}
