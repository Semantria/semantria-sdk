using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using System.Reflection;
using Semantria.Com.Mapping.Configuration;
using Semantria.Com.Mapping.Configuration.Stub;
using Semantria.Com.Mapping.Output;
using Semantria.Com.Mapping.Output.Stub;
using Semantria.Com.Mapping;
using Semantria.Com.Mapping.Stub;
using Semantria.Com.Serializers;

namespace Semantria.Com
{
    public sealed class Session : IDisposable
    {
        #region Constructor
        
        private Session()
        {
        }
        
        private Session(string consumerKey, string consumerSecret, string appName) :
            this (consumerKey, consumerSecret, new JsonSerializer(), appName)
        {
        }

        private Session(string consumerKey, string consumerSecret, ISerializer serializer, string appName)
        {
            _consumerKey = consumerKey;
            _consumerSecret = consumerSecret;
            _serializer = serializer;
            _format = serializer.Type();
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
        private ISerializer _serializer = null;
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
            return CreateSession(consumerKey, consumerSecret, new JsonSerializer());
        }

        public static Session CreateSession(string consumerKey, string consumerSecret, string appName)
        {
            return new Session(consumerKey, consumerSecret, appName);
        }

        public static Session CreateSession(string consumerKey, string consumerSecret, ISerializer serializer)
        {
            if (serializer == null)
            {
                throw new ArgumentNullException("serializer");
            }

            return CreateSession(consumerKey, consumerSecret, serializer, string.Empty);
        }

        public static Session CreateSession(string consumerKey, string consumerSecret, ISerializer serializer, string appName)
        {
            if (serializer == null)
            {
                throw new ArgumentNullException("serializer");
            }

            return new Session(consumerKey, consumerSecret, serializer, appName);
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

        public Status GetStatus()
        {
            //GET https://api.semantria.com/status.json
            string url = String.Format("{0}/status.{1}", _host, _format);
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return this.ProcessGetResponse<Status>(authResponse);
        }

        public Subscription GetSubscription()
        {
            //GET https://api.semantria.com/subscription.json
            string url = String.Format("{0}/subscription.{1}", _host, _format);
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return this.ProcessGetResponse<Subscription>(authResponse);
        }

        public Statistics GetStatistics(string configId = null, string interval = null)
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
            return this.ProcessGetResponse<Statistics>(authResponse);
        }

        public IList<FeaturesSet> GetSupportedFeatures(string language = null)
        {
            //GET https://api.semantria.com/features.json
            string url = String.Format("{0}/features.{1}", _host, _format);

            if (!String.IsNullOrEmpty(language))
            {
                url = String.Format("{0}/features.{1}?language={2}", _host, _format, language);
            }

            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            List<FeaturesSet> obj = new List<FeaturesSet>();

            switch (_format)
            {
                case "json":
                    {
                        List<FeaturesSet> result = this.ProcessGetResponse<List<FeaturesSet>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        FeaturesSetList result = this.ProcessGetResponse<FeaturesSetList>(authResponse);
                        if (result != null) obj = result.Data;
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }
       
        #endregion

        #region Configuration

        public List<Configuration> GetConfigurations()
        {
            AuthResponse authResponse = this.Get<Configuration>();
            List<Configuration> obj = new List<Configuration>();

            switch (_format)
            {
                case "json":
                    {
                        List<Configuration> result = this.ProcessGetResponse<List<Configuration>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        Configurations result = this.ProcessGetResponse<Configurations>(authResponse);
                        if (result != null) obj = result.Data;
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }

        public int AddConfigurations(List<Configuration> items)
        {
            return this.Update<Configuration>(QueryMethod.POST, items);
        }

        public int UpdateConfigurations(List<Configuration> items)
        {
            return this.Update<Configuration>(QueryMethod.POST, items);
        }

        public int RemoveConfigurations(List<Configuration> items)
        {
            return this.Update<Configuration>(QueryMethod.DELETE, items);
        }

        public int CloneConfiguration(string name, string template)
        {
            var items = new List<Configuration>();
            items.Add(new Configuration() { Name = name, Template = template });

            return this.AddConfigurations( items);
        }

        #endregion Configuration

        #region Blacklist

        public List<string> GetBlacklist(string configId = null)
        {
            AuthResponse authResponse = this.Get<string>(configId);
            List<string> obj = new List<string>();

            switch (_format)
            {
                case "json":
                    {
                        List<string> result = this.ProcessGetResponse<List<string>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        Blacklists result = this.ProcessGetResponse<Blacklists>(authResponse);
                        if (result != null) obj = result.Data;
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }

        public int AddBlacklist(List<string> items, string configId = null)
        {
            return this.Update<string>(QueryMethod.POST, items, configId);
        }

        public int RemoveBlacklist(List<string> items, string configId = null)
        {
            return this.Update<string>(QueryMethod.DELETE, items, configId);
        }

        #endregion Blacklist

        #region Category

        public List<Category> GetCategories(string configId = null)
        {
            AuthResponse authResponse = this.Get<Category>(configId);
            List<Category> obj = new List<Category>();

            switch (_format)
            {
                case "json":
                    {
                        List<Category> result = this.ProcessGetResponse<List<Category>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        Categories result = this.ProcessGetResponse<Categories>(authResponse);
                        if (result != null) obj = result.Data;
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }

        public int AddCategories(List<Category> items, string configId = null)
        {
            return this.Update<Category>(QueryMethod.POST, items, configId);
        }

        public int UpdateCategories(List<Category> items, string configId = null)
        {
            return this.Update<Category>(QueryMethod.POST, items, configId);
        }

        public int RemoveCategories(List<Category> items, string configId = null)
        {
            return this.Update<Category>(QueryMethod.DELETE, items, configId);
        }

        #endregion Category

        #region Query

        public List<Query> GetQueries(string configId = null)
        {
            AuthResponse authResponse = this.Get<Query>(configId);
            List<Query> obj = new List<Query>();

            switch (_format)
            {
                case "json":
                    {
                        List<Query> result = this.ProcessGetResponse<List<Query>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        Queries result = this.ProcessGetResponse<Queries>(authResponse);
                        if (result != null) obj = result.Data;
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }

        public int AddQueries(List<Query> items, string configId = null)
        {
            return this.Update<Query>(QueryMethod.POST, items, configId);
        }

        public int UpdateQueries(List<Query> items, string configId = null)
        {
            return this.Update<Query>(QueryMethod.POST, items, configId);
        }

        public int RemoveQueries(List<Query> items, string configId = null)
        {
            return this.Update<Query>(QueryMethod.DELETE, items, configId);
        }

        #endregion Query

        #region Entity

        public List<UserEntity> GetEntities(string configId = null)
        {
            AuthResponse authResponse = this.Get<UserEntity>(configId);
            List<UserEntity> obj = new List<UserEntity>();

            switch (_format)
            {
                case "json":
                    {
                        List<UserEntity> result = this.ProcessGetResponse<List<UserEntity>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        UserEntities result = this.ProcessGetResponse<UserEntities>(authResponse);
                        if (result != null) obj = result.Data;
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }

        public int AddEntities(List<UserEntity> items, string configId = null)
        {
            return this.Update<UserEntity>(QueryMethod.POST, items, configId);
        }

        public int UpdateEntities(List<UserEntity> items, string configId = null)
        {
            return this.Update<UserEntity>(QueryMethod.POST, items, configId);
        }

        public int RemoveEntities(List<UserEntity> items, string configId = null)
        {
            return this.Update<UserEntity>(QueryMethod.DELETE, items, configId);
        }

        #endregion Entity

        #region SentimentPhrase

        public List<SentimentPhrase> GetSentimentPhrases(string configId = null)
        {
            AuthResponse authResponse = this.Get<SentimentPhrase>(configId);
            List<SentimentPhrase> obj = new List<SentimentPhrase>();

            switch (_format)
            {
                case "json":
                    {
                        List<SentimentPhrase> result = this.ProcessGetResponse<List<SentimentPhrase>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        SentimentPhrases result = this.ProcessGetResponse<SentimentPhrases>(authResponse);
                        if (result != null) obj = result.Data;
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }

        public int AddSentimentPhrases(List<SentimentPhrase> items, string configId = null)
        {
            return this.Update<SentimentPhrase>(QueryMethod.POST, items, configId);
        }

        public int UpdateSentimentPhrases(List<SentimentPhrase> items, string configId = null)
        {
            return this.Update<SentimentPhrase>(QueryMethod.POST, items, configId);
        }

        public int RemoveSentimentPhrases(List<SentimentPhrase> items, string configId = null)
        {
            return this.Update<SentimentPhrase>(QueryMethod.DELETE, items, configId);
        }

        #endregion SentimentPhrase

        #region Document

        public int QueueDocument(Document task, string configId = null)
        {
            string url = String.Format("{0}/document.{1}", _host, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document.{1}?config_id={2}", _host, _format, configId);
            }
            string data = _serializer.Serialize(task);
            IList<DocAnalyticData> result = this.PostAnalyticData<DocAnalyticData>(url, data);

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

        public int QueueBatchOfDocuments(IList<Document> tasks, string configId = null)
        {
            string url = String.Format("{0}/document/batch.{1}", _host, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document/batch.{1}?config_id={2}", _host, _format, configId);
            }

            List<Document> list = new List<Document>();
            list.AddRange(tasks);

            IList<DocAnalyticData> result = this.PostQueueBatch(url, list);

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

        public DocAnalyticData GetDocument(string id, string configId = null)
        {
            string url = String.Format("{0}/document/{1}.{2}", _host, id, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document/{1}.{2}?config_id={3}", _host, id, _format, configId);
            }
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return this.ProcessGetResponse<DocAnalyticData>(authResponse);
        }

        public int CancelDocument(string id, string configId = null)
        {
            string url = String.Format("{0}/document/{1}.{2}", _host, id, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document/{1}.{2}?config_id={3}", _host, id, _format, configId);
            }
            AuthResponse authResponse = this.RunRequest(QueryMethod.DELETE, url, null);
            return this.ProcessPostResponse<Document>(authResponse);
        }

        public IList<DocAnalyticData> GetProcessedDocuments(string configId = null)
        {
            string url = String.Format("{0}/document/processed.{1}", _host, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document/processed.{1}?config_id={2}", _host, _format, configId);
            }
            IList<DocAnalyticData> result = this.RequestProcessedDocuments(url);
            return result;
        }

        public IList<DocAnalyticData> GetProcessedDocumentsByJobId(string jobId)
        {
            if (String.IsNullOrEmpty(jobId))
            {
                throw new ArgumentNullException("jobId", "jobId parameter can not be null or string empty.");
            }

            string url = String.Format("{0}/document/processed.{1}?job_id={2}", _host, _format, jobId);
            IList<DocAnalyticData> result = this.RequestProcessedDocuments(url);
            return result;
        }

        private List<DocAnalyticData> RequestProcessedDocuments(string url)
        {
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            List<DocAnalyticData> obj = new List<DocAnalyticData>();

            switch (_format)
            {
                case "json":
                    {
                        List<DocAnalyticData> result = this.ProcessGetResponse<List<DocAnalyticData>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        DocAnalyticsData result = this.ProcessGetResponse<DocAnalyticsData>(authResponse);
                        if (result != null) obj = result.Data;
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }

        #endregion Document

        #region Collection

        public int QueueCollection(Collection task, string configId = null)
        {
            string url = String.Format("{0}/collection.{1}", _host, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/collection.{1}?config_id={2}", _host, _format, configId);
            }

            string data = _serializer.Serialize(task);
            IList<CollAnalyticData> result = this.PostAnalyticData<CollAnalyticData>(url, data);

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

        public CollAnalyticData GetCollection(string id, string configId = null)
        {
            string url = String.Format("{0}/collection/{1}.{2}", _host, id, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/collection/{1}.{2}?config_id={3}", _host, id, _format, configId);
            }
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return this.ProcessGetResponse<CollAnalyticData>(authResponse);
        }

        public int CancelCollection(string id, string configId = null)
        {
            string url = String.Format("{0}/collection/{1}.{2}", _host, id, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/collection/{1}.{2}?config_id={3}", _host, id, _format, configId);
            }
            AuthResponse authResponse = this.RunRequest(QueryMethod.DELETE, url, null);
            return this.ProcessPostResponse<Collection>(authResponse);
        }

        public IList<CollAnalyticData> GetProcessedCollections(string configId = null)
        {
            string url = String.Format("{0}/collection/processed.{1}", _host, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/collection/processed.{1}?config_id={2}", _host, _format, configId);
            }
            IList<CollAnalyticData> result = this.RequestProcessedCollections(url);
            return result;
        }

        public IList<CollAnalyticData> GetProcessedCollectionsByJobId(string jobId)
        {
            if (String.IsNullOrEmpty(jobId))
            {
                throw new ArgumentNullException("jobId", "jobId parameter can not be null or string empty.");
            }

            string url = String.Format("{0}/collection/processed.{1}?job_id={2}", _host, _format, jobId);
            IList<CollAnalyticData> result = this.RequestProcessedCollections(url);
            return result;
        }

        private List<CollAnalyticData> RequestProcessedCollections(string url)
        {
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            List<CollAnalyticData> obj = new List<CollAnalyticData>();

            switch (_format)
            {
                case "json":
                    {
                        List<CollAnalyticData> result = this.ProcessGetResponse<List<CollAnalyticData>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        CollAnalyticsData result = this.ProcessGetResponse<CollAnalyticsData>(authResponse);
                        if (result != null) obj = result.Data;
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }

        #endregion Collection

        #region Private request and response support

        private AuthResponse Get<T>(string configId = null)
        {
            string tag = this.GetTag<T>();
            
            string url = String.Format("{0}/{1}.{2}", _host, tag, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/{1}.{2}?config_id={3}", _host, tag, _format, configId);
            }

            return this.RunRequest(QueryMethod.GET, url, null);
        }

        private int Update<T>(QueryMethod method, List<T> items, string configId = null)
        {
            string tag = this.GetTag<T>();
            
            string url = String.Format("{0}/{1}.{2}", _host, tag, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/{1}.{2}?config_id={3}", _host, tag, _format, configId);
            }

            string data = null;
            if (method == QueryMethod.DELETE)
            {
                IStub<T> xstub = this.GetStub<T>(items) as IStub<T>;
                xstub.ToKeys();
                if (_format == "xml")
                {
                    data = _serializer.Serialize(xstub);
                    data = xstub.ConvertTag(data);
                }
                else
                {
                    data = _serializer.Serialize(xstub.Keys);
                }
            }
            else
            {
                object stub = items;
                if (_format == "xml")
                {
                    stub = this.GetStub<T>(items);
                }
                data = _serializer.Serialize(stub);
            }
            
            AuthResponse authResponse = this.RunRequest(method, url, data);
            return this.ProcessPostResponse<T>(authResponse);
        }

        private string GetTag<T>()
        {
            string tag = string.Empty;
            
            if (typeof(T).Equals(typeof(Configuration)))
                tag = "configurations";
            else if (typeof(T).Equals(typeof(Category)))
                tag = "categories";
            else if (typeof(T).Equals(typeof(String)))
                tag = "blacklist";
            else if (typeof(T).Equals(typeof(Query)))
                tag = "queries";
            else if (typeof(T).Equals(typeof(UserEntity)))
                tag = "entities";
            else if (typeof(T).Equals(typeof(SentimentPhrase)))
                tag = "phrases";
            else
                throw new ArgumentOutOfRangeException();

            return tag;
        }

        private object GetStub<T>(List<T> items)
        {
            object data = null;
            
            if (typeof(T).Equals(typeof(Configuration)))
                data = new Configurations(items as List<Configuration>);
            else if (typeof(T).Equals(typeof(Category)))
                data = new Categories(items as List<Category>);
            else if (typeof(T).Equals(typeof(String)))
                data = new Blacklists(items as List<string>) ;
            else if (typeof(T).Equals(typeof(Query)))
                data = new Queries(items as List<Query>);
            else if (typeof(T).Equals(typeof(UserEntity)))
                data = new UserEntities(items as List<UserEntity>);
            else if (typeof(T).Equals(typeof(SentimentPhrase)))
                data = new SentimentPhrases(items as List<SentimentPhrase>);
            else
                throw new ArgumentOutOfRangeException();
 
            return data;
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

        private T ProcessGetResponse<T>(AuthResponse authResponse)
        {
            int status = (int)authResponse.Status;
            string message = authResponse.Data;
            T obj = default(T);

            switch (status)
            {
                case 200:
                    obj = this.ConvertResponse<T>(message);
                    return obj;
                case 202:
                    return obj;
                default:
                    this.ResolveError<T>(authResponse);
                    return obj;
            }
        }

        private int ProcessPostResponse<T>(AuthResponse authResponse)
        {
            int status = (int)authResponse.Status;
            string message = authResponse.Data;

            switch (status)
            {
                case 200:
                    return status;
                case 202:
                    return status;
                default:
                    this.ResolveError<T>(authResponse);
                    return status;
            }
        }

        private T ConvertResponse<T>(string data)
        {
            T obj = default(T);
            if (_serializer != null)
            {
                obj = _serializer.Deserialize<T>(data);
            }
            else if (_format == "json")
            {
                ISerializer serializer = new JsonSerializer();
                obj = serializer.Deserialize<T>(data);
            }
            return obj;
        }

        private List<DocAnalyticData> PostQueueBatch(string url, List<Document> tasks)
        {
            string data = string.Empty;
            switch (_format)
            {
                case "json":
                    {
                        data = _serializer.Serialize(tasks);
                    }
                    break;
                case "xml":
                    {
                        Documents wrapper = new Documents();
                        wrapper.Data = tasks;
                        data = _serializer.Serialize(wrapper);
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return this.PostAnalyticData<DocAnalyticData>(url, data);
        }

        private List<T> PostAnalyticData<T>(string url, string data)
        {
            AuthResponse authResponse = this.RunRequest(QueryMethod.POST, url, data);

            int status = (int)authResponse.Status;
            string message = authResponse.Data;
            
            List<T> obj = new List<T>();

            switch (status)
            {
                case 200:
                    {
                        switch (_format)
                        {
                            case "json":
                                {
                                    List<T> result = this.ProcessGetResponse<List<T>>(authResponse);
                                    if (result != null) obj = result;
                                }
                                break;
                            case "xml":
                                {
                                    T result = this.ProcessGetResponse<T>(authResponse);
                                    if (result != null)
                                        obj = (List<T>)result.GetType().InvokeMember("Data", BindingFlags.Instance | BindingFlags.Public | BindingFlags.GetProperty, Type.DefaultBinder, result, null);
                                }
                                break;
                            default:
                                throw new ArgumentOutOfRangeException();
                        }
                        return obj;
                    }
                case 202:
                    return obj;
                default:
                    this.ResolveError<T>(authResponse);
                    return null;
            }
        }

        private void ResolveError<T>(AuthResponse authResponse)
        {
            int status = (int)authResponse.Status;
            string message = authResponse.Data;
            OnError(this, new ResponseErrorEventArgs(typeof(T), authResponse.Status, message));
        }

        #endregion
    }
}
