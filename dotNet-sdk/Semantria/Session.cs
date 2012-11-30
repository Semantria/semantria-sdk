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
using Semantria.Com.Proxy;
using Semantria.Com.Serializers;

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
            setAppName(appName);
        }

        private Session(string consumerKey, string consumerSecret, ISerializer serializer, string appName)
        {
            _consumerKey = consumerKey;
            _consumerSecret = consumerSecret;
            _serializer = serializer;
            _format = serializer.Type();
            setAppName(appName);
        }

        private void setAppName(string appName)
        {
            if (!String.IsNullOrEmpty(appName))
            {
                _appName = String.Format("{0}.{1}", appName, WRAPPER_NAME);
            } 
            else 
            {
                _appName = WRAPPER_NAME;
            }
        }

        #endregion Constructor

        #region IDisposable Members

        public void Dispose()
        {
        }

        #endregion

        #region Private variables

        private string _consumerKey = "";
        private string _consumerSecret = "";
        private string _appName = "";
        private ISerializer _serializer = null;
        private string _format = "json";
        private string _host = "https://api21.semantria.com";
        private const string WRAPPER_NAME = "dotNet";

        #endregion

        #region Properties
        public string Host
        {
            get { return _host; }
            set { _host = value; } 
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

        public delegate void ErrorHandler(object sender, ResponseEventArgs e);
        public event ErrorHandler Error;
        internal void OnError(object sender, ResponseEventArgs e)
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

        public static Session CreateSession(string consumerKey, string consumerSecret, ISerializer serializer)
        {
            return CreateSession(consumerKey, consumerSecret, serializer, null);
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
            return ProcessGetResponse<Status>(authResponse);
        }

        public Subscription VerifySubscription()
        {
            //GET https://api.semantria.com/subscription.json
            string url = String.Format("{0}/subscription.{1}", _host, _format);
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return ProcessGetResponse<Subscription>(authResponse);
        }
       
        #endregion

        #region UpdateProxy

        public IUpdateProxy<Configuration> CreateConfigurationsUpdateProxy()
        {
            IUpdateProxy<Configuration> proxy = ConfigurationUpdateProxy.CreateInstance();
            return proxy;
        }

        public IUpdateProxy<string> CreateBlacklistUpdateProxy()
        {
            IUpdateProxy<string> proxy = BlacklistUpdateProxy.CreateInstance();
            return proxy;
        }

        public IUpdateProxy<Category> CreateCategoriesUpdateProxy()
        {
            IUpdateProxy<Category> proxy = CategoryUpdateProxy.CreateInstance();
            return proxy;
        }

        public IUpdateProxy<Query> CreateQueriesUpdateProxy()
        {
            IUpdateProxy<Query> proxy = QueryUpdateProxy.CreateInstance();
            return proxy;
        }

        public IUpdateProxy<UserEntity> CreateEntitiesUpdateProxy()
        {
            IUpdateProxy<UserEntity> proxy = EntityUpdateProxy.CreateInstance();
            return proxy;
        }

        public IUpdateProxy<SentimentPhrase> CreateSentimentPhrasesUpdateProxy()
        {
            IUpdateProxy<SentimentPhrase> proxy = SentimentPhrasesUpdateProxy.CreateInstance();
            return proxy;
        }

        #endregion

        #region Configuration

        public List<Configuration> GetConfigurations()
        {
            //GET https://api.semantria.com/configurations.json
            string url = String.Format("{0}/configurations.{1}", _host, _format);
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            List<Configuration> obj = new List<Configuration>();
            
            switch (_format)
            {
                case "json":
                    {
                        List<Configuration> result = ProcessGetResponse<List<Configuration>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        Configurations result = ProcessGetResponse<Configurations>(authResponse);
                        if (result != null) obj = result.Data;
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }
            
            return obj;
        }

        public int UpdateConfigurations(IUpdateProxy<Configuration> updates)
        {
            //POST https://api.semantria.com/configurations.json
            string url = String.Format("{0}/configurations.{1}", _host, _format);

            ConfigurationManagable cm = (ConfigurationManagable)updates.Stub;
            string data = _serializer.Serialize(cm);
            AuthResponse authResponse = this.RunRequest(QueryMethod.POST, url, data);

            return ProcessPostResponse(authResponse);           
        }

        #endregion Configuration

        #region Blacklist

        public List<string> GetBlacklist()
        {
            //GET https://api.semantria.com/blacklist.json
            string url = String.Format("{0}/blacklist.{1}", _host, _format);
            return RequestBlacklist(url);
        }

        public List<string> GetBlacklist(string configId)
        {
            //GET https://api.semantria.com/blacklist.json?config_id=23498367
            string url = String.Format("{0}/blacklist.{1}?config_id={2}", _host, _format, configId);
            return RequestBlacklist(url);
        }

        private List<string> RequestBlacklist(string url)
        {
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            List<string> obj = new List<string>();

            switch (_format)
            {
                case "json":
                    {
                        List<string> result = ProcessGetResponse<List<string>>(authResponse);
                        if (result != null)
                        {
                            foreach (string item in result)
                            {
                                obj.Add(item);
                            }
                        }
                    }
                    break;
                case "xml":
                    {
                        Blacklists result = ProcessGetResponse<Blacklists>(authResponse);
                        if (result != null)
                        {
                            foreach (string item in result.Data)
                            {
                                obj.Add(item);
                            }
                        }
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }

        public int UpdateBlacklist(IUpdateProxy<string> updates)
        {
            //POST https://api.semantria.com/blacklist.json
            string url = String.Format("{0}/blacklist.{1}", _host, _format);

            BlacklistManagable cm = (BlacklistManagable)updates.Stub;
            string data = _serializer.Serialize(cm);
            AuthResponse authResponse = this.RunRequest(QueryMethod.POST, url, data);
            return ProcessPostResponse(authResponse);           
        }

        public int UpdateBlacklist(IUpdateProxy<string> updates, string configId)
        {
            //POST https://api.semantria.com/blacklist.json?config_id=23498367
            string url = String.Format("{0}/blacklist.{1}?config_id={2}", _host, _format, configId);

            BlacklistManagable cm = (BlacklistManagable)updates.Stub;
            string data = _serializer.Serialize(cm);
            AuthResponse authResponse = this.RunRequest(QueryMethod.POST, url, data);
            return ProcessPostResponse(authResponse);
        }

        #endregion Blacklist

        #region Category

        public List<Category> GetCategories()
        {
            //GET https://api.semantria.com/categories.json
            string url = String.Format("{0}/categories.{1}", _host, _format);
            return RequestCategories(url);
        }

        public List<Category> GetCategories(string configId)
        {
            //GET https://api.semantria.com/categories.json?config_id=23498367
            string url = String.Format("{0}/categories.{1}?config_id={2}", _host, _format, configId);
            return RequestCategories(url);
        }

        private List<Category> RequestCategories(string url)
        {
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            List<Category> obj = new List<Category>();

            switch (_format)
            {
                case "json":
                    {
                        List<Category> result = ProcessGetResponse<List<Category>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        Categories result = ProcessGetResponse<Categories>(authResponse);
                        if (result != null) obj = result.Data;
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }

        public int UpdateCategories(IUpdateProxy<Category> updates)
        {
            //POST https://api.semantria.com/categories.json
            string url = String.Format("{0}/categories.{1}", _host, _format);

            CategoryManagable cm = (CategoryManagable)updates.Stub;
            string data = _serializer.Serialize(cm);
            AuthResponse authResponse = this.RunRequest(QueryMethod.POST, url, data);
            return ProcessPostResponse(authResponse);
        }

        public int UpdateCategories(IUpdateProxy<Category> updates, string configId)
        {
            //POST https://api.semantria.com/categories.json?config_id=23498367
            string url = String.Format("{0}/categories.{1}?config_id={2}", _host, _format, configId);

            CategoryManagable cm = (CategoryManagable)updates.Stub;
            string data = _serializer.Serialize(cm);
            AuthResponse authResponse = this.RunRequest(QueryMethod.POST, url, data);
            return ProcessPostResponse(authResponse);
        }

        #endregion Category

        #region Query

        public List<Query> GetQueries()
        {
            //GET https://api.semantria.com/queries.json
            string url = String.Format("{0}/queries.{1}", _host, _format);
            return RequestQueries(url);
        }

        public List<Query> GetQueries(string configId)
        {
            //GET https://api.semantria.com/queries.json?config_id=23498367
            string url = String.Format("{0}/queries.{1}?config_id={2}", _host, _format, configId);
            return RequestQueries(url);
        }

        private List<Query> RequestQueries(string url)
        {
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            List<Query> obj = new List<Query>();

            switch (_format)
            {
                case "json":
                    {
                        List<Query> result = ProcessGetResponse<List<Query>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        Queries result = ProcessGetResponse<Queries>(authResponse);
                        if (result != null) obj = result.Data;
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }

        public int UpdateQueries(IUpdateProxy<Query> updates)
        {
            //POST https://api.semantria.com/queries.json
            string url = String.Format("{0}/queries.{1}", _host, _format);

            QueryManagable cm = (QueryManagable)updates.Stub;
            string data = _serializer.Serialize(cm);
            AuthResponse authResponse = this.RunRequest(QueryMethod.POST, url, data);
            return ProcessPostResponse(authResponse);
        }

        public int UpdateQueries(IUpdateProxy<Query> updates, string configId)
        {
            //POST https://api.semantria.com/queries.json?config_id=23498367
            string url = String.Format("{0}/queries.{1}?config_id={2}", _host, _format, configId);

            QueryManagable cm = (QueryManagable)updates.Stub;
            string data = _serializer.Serialize(cm);
            AuthResponse authResponse = this.RunRequest(QueryMethod.POST, url, data);
            return ProcessPostResponse(authResponse);
        }

        #endregion Query

        #region Entity

        public List<UserEntity> GetEntities()
        {
            //GET https://api.semantria.com/entities.json
            string url = String.Format("{0}/entities.{1}", _host, _format);
            return RequestEntity(url);
        }

        public List<UserEntity> GetEntities(string configId)
        {
            //GET https://api.semantria.com/entities.json?config_id=23498367
            string url = String.Format("{0}/entities.{1}?config_id={2}", _host, _format, configId);
            return RequestEntity(url);
        }

        private List<UserEntity> RequestEntity(string url)
        {
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            List<UserEntity> obj = new List<UserEntity>();

            switch (_format)
            {
                case "json":
                    {
                        List<UserEntity> result = ProcessGetResponse<List<UserEntity>>(authResponse);
                        if (result != null)
                        {
                            foreach (UserEntity item in result)
                            {
                                obj.Add(item);
                            }
                        }
                    }
                    break;
                case "xml":
                    {
                        UserEntities result = ProcessGetResponse<UserEntities>(authResponse);
                        if (result != null)
                        {
                            foreach (UserEntity item in result.Data)
                            {
                                obj.Add(item);
                            }
                        }
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }

        public int UpdateEntities(IUpdateProxy<UserEntity> updates)
        {
            //POST https://api.semantria.com/entities.json
            string url = String.Format("{0}/entities.{1}", _host, _format);

            EntityManagable cm = (EntityManagable)updates.Stub;
            string data = _serializer.Serialize(cm);
            AuthResponse authResponse = this.RunRequest(QueryMethod.POST, url, data);
            return ProcessPostResponse(authResponse);
        }

        public int UpdateEntities(IUpdateProxy<UserEntity> updates, string configId)
        {
            //POST https://api.semantria.com/entities.json?config_id=23498367
            string url = String.Format("{0}/entities.{1}?config_id={2}", _host, _format, configId);

            EntityManagable cm = (EntityManagable)updates.Stub;
            string data = _serializer.Serialize(cm);
            AuthResponse authResponse = this.RunRequest(QueryMethod.POST, url, data);
            return ProcessPostResponse(authResponse);
        }

        #endregion Entity

        #region SentimentPhrase

        public List<SentimentPhrase> GetSentimentPhrases()
        {
            //GET https://api.semantria.com/sentiment.json
            string url = String.Format("{0}/sentiment.{1}", _host, _format);
            return RequestSentimentPhrases(url);
        }

        public List<SentimentPhrase> GetSentimentPhrases(string configId)
        {
            //GET https://api.semantria.com/sentiment.json?config_id=23498367
            string url = String.Format("{0}/sentiment.{1}?config_id={2}", _host, _format, configId);
            return RequestSentimentPhrases(url);
        }

        private List<SentimentPhrase> RequestSentimentPhrases(string url)
        {
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            List<SentimentPhrase> obj = new List<SentimentPhrase>();

            switch (_format)
            {
                case "json":
                    {
                        List<SentimentPhrase> result = ProcessGetResponse<List<SentimentPhrase>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        SentimentPhrases result = ProcessGetResponse<SentimentPhrases>(authResponse);
                        if (result != null) obj = result.Data;
                    }
                    break;
                default:
                    throw new ArgumentOutOfRangeException();
            }

            return obj;
        }

        public int UpdateSentimentPhrases(IUpdateProxy<SentimentPhrase> updates)
        {
            //POST https://api.semantria.com/sentiment.json
            string url = String.Format("{0}/sentiment.{1}", _host, _format);

            SentimentPhraseManagable cm = (SentimentPhraseManagable)updates.Stub;
            string data = _serializer.Serialize(cm);
            AuthResponse authResponse = this.RunRequest(QueryMethod.POST, url, data);
            return ProcessPostResponse(authResponse);
        }

        public int UpdateSentimentPhrases(IUpdateProxy<SentimentPhrase> updates, string configId)
        {
            //POST https://api.semantria.com/sentiment.json?config_id=23498367
            string url = String.Format("{0}/sentiment.{1}?config_id={2}", _host, _format, configId);

            SentimentPhraseManagable cm = (SentimentPhraseManagable)updates.Stub;
            string data = _serializer.Serialize(cm);
            AuthResponse authResponse = this.RunRequest(QueryMethod.POST, url, data);
            return ProcessPostResponse(authResponse);
        }

        #endregion SentimentPhrase

        #region Document

        public int QueueDocument(Document task)
        {
            //POST https://api.semantria.com/document.json
            string url = String.Format("{0}/document.{1}", _host, _format);
            string data = _serializer.Serialize(task);
            IList<DocAnalyticData> result = this.RequestAnalyticData<DocAnalyticData>(url, data);

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

        public int QueueDocument(Document task, string configId)
        {
            //POST https://api.semantria.com/document.json?config_id=23498367
            string url = String.Format("{0}/document.{1}?config_id={2}", _host, _format, configId);
            string data = _serializer.Serialize(task);
            IList<DocAnalyticData> result = this.RequestAnalyticData<DocAnalyticData>(url, data);

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

        public int QueueBatchOfDocuments(IList<Document> tasks)
        {
            //POST https://api.semantria.com/document/batch.json
            string url = String.Format("{0}/document/batch.{1}", _host, _format);

            List<Document> list = new List<Document>();
            list.AddRange(tasks);

            IList<DocAnalyticData> result = this.RequestQueueBatch(url, list);

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

        public int QueueBatchOfDocuments(IList<Document> tasks, string configId)
        {
            //POST https://api.semantria.com/document/batch.json?config_id=23498367
            string url = String.Format("{0}/document/batch.{1}?config_id={2}", _host, _format, configId);

            List<Document> list = new List<Document>();
            list.AddRange(tasks);

            IList<DocAnalyticData> result = this.RequestQueueBatch(url, list);

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

        public DocAnalyticData GetDocument(string id)
        {
            //GET https://api.semantria.com/document/{document_id}.json
            string url = String.Format("{0}/document/{1}.{2}", _host, id, _format);
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return ProcessGetResponse<DocAnalyticData>(authResponse);
        }

        public DocAnalyticData GetDocument(string id, string configId)
        {
            //GET https://api.semantria.com/document/{document_id}.json
            string url = String.Format("{0}/document/{1}.{2}?config_id={3}", _host, id, _format, configId);
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return ProcessGetResponse<DocAnalyticData>(authResponse);
        }

        public int CancelDocument(string id)
        {
            //GET https://api.semantria.com/document/{document_id}.json
            string url = String.Format("{0}/document/{1}.{2}", _host, id, _format);
            AuthResponse authResponse = this.RunRequest(QueryMethod.DELETE, url, null);
            return ProcessPostResponse(authResponse);
        }

        public int CancelDocument(string id, string configId)
        {
            //GET https://api.semantria.com/document/{document_id}.json
            string url = String.Format("{0}/document/{1}.{2}?config_id={3}", _host, id, _format, configId);
            AuthResponse authResponse = this.RunRequest(QueryMethod.DELETE, url, null);
            return ProcessPostResponse(authResponse);
        }

        public IList<DocAnalyticData> GetProcessedDocuments()
        {
            //GET https://api.semantria.com/document/processed.json
            string url = String.Format("{0}/document/processed.{1}", _host, _format);
            IList<DocAnalyticData> result = RequestProcessedDocuments(url);
            return result;
        }

        public IList<DocAnalyticData> GetProcessedDocuments(string configId)
        {
            //GET https://api.semantria.com/document/processed.json?config_id=23498367
            string url = String.Format("{0}/document/processed.{1}?config_id={2}", _host, _format, configId);
            IList<DocAnalyticData> result = RequestProcessedDocuments(url);
            return result;
        }

        [Obsolete(@"Semantria API 2.1 doesn't offer an ability to request limited amount of processed documents.
            This mehod is obsolete and will be removed in API 2.5. ", true)]
        public IList<DocAnalyticData> GetProcessedDocuments(int number, string configId)
        {
            return GetProcessedDocuments(configId);
        }

        private List<DocAnalyticData> RequestProcessedDocuments(string url)
        {
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            List<DocAnalyticData> obj = new List<DocAnalyticData>();

            switch (_format)
            {
                case "json":
                    {
                        List<DocAnalyticData> result = ProcessGetResponse<List<DocAnalyticData>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        DocAnalyticsData result = ProcessGetResponse<DocAnalyticsData>(authResponse);
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

        public int QueueCollection(Collection task)
        {
            //POST https://api.semantria.com/collection.json
            string url = String.Format("{0}/collection.{1}", _host, _format);
            string data = _serializer.Serialize(task);
            IList<CollAnalyticData> result = this.RequestAnalyticData<CollAnalyticData>(url, data);

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

        public int QueueCollection(Collection task, string configId)
        {
            //POST https://api.semantria.com/collection.json?config_id=23498367
            string url = String.Format("{0}/collection.{1}?config_id={2}", _host, _format, configId);
            string data = _serializer.Serialize(task);
            IList<CollAnalyticData> result = this.RequestAnalyticData<CollAnalyticData>(url, data);

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

        public CollAnalyticData GetCollection(string id)
        {
            //GET https://api.semantria.com/collection/{collection_id}.json
            string url = String.Format("{0}/collection/{1}.{2}", _host, id, _format);
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return ProcessGetResponse<CollAnalyticData>(authResponse);
        }

        public CollAnalyticData GetCollection(string id, string configId)
        {
            //GET https://api.semantria.com/collection/{collection_id}.json
            string url = String.Format("{0}/collection/{1}.{2}?config_id={3}", _host, id, _format, configId);
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            return ProcessGetResponse<CollAnalyticData>(authResponse);
        }

        public int CancelCollection(string id)
        {
            //GET https://api.semantria.com/collection/{collection_id}.json
            string url = String.Format("{0}/collection/{1}.{2}", _host, id, _format);
            AuthResponse authResponse = this.RunRequest(QueryMethod.DELETE, url, null);
            return ProcessPostResponse(authResponse);
        }

        public int CancelCollection(string id, string configId)
        {
            //GET https://api.semantria.com/collection/{collection_id}.json
            string url = String.Format("{0}/collection/{1}.{2}?config_id={3}", _host, id, _format, configId);
            AuthResponse authResponse = this.RunRequest(QueryMethod.DELETE, url, null);
            return ProcessPostResponse(authResponse);
        }

        public IList<CollAnalyticData> GetProcessedCollections()
        {
            //GET https://api.semantria.com/collection/processed.json
            string url = String.Format("{0}/collection/processed.{1}", _host, _format);
            IList<CollAnalyticData> result = RequestProcessedCollections(url);
            return result;
        }

        public IList<CollAnalyticData> GetProcessedCollections(string configId)
        {
            //GET https://api.semantria.com/collection/processed.json?config_id=23498367
            string url = String.Format("{0}/collection/processed.{1}?config_id={2}", _host, _format, configId);
            IList<CollAnalyticData> result = RequestProcessedCollections(url);
            return result;
        }

        [Obsolete(@"Semantria API 2.1 doesn't offer an ability to request limited amount of processed collections.
            This mehod is obsolete and will be removed in API 2.5. ", true)]
        public IList<CollAnalyticData> GetProcessedCollections(int number, string configId)
        {
            return GetProcessedCollections(configId);
        }

        private List<CollAnalyticData> RequestProcessedCollections(string url)
        {
            AuthResponse authResponse = this.RunRequest(QueryMethod.GET, url, null);
            List<CollAnalyticData> obj = new List<CollAnalyticData>();

            switch (_format)
            {
                case "json":
                    {
                        List<CollAnalyticData> result = ProcessGetResponse<List<CollAnalyticData>>(authResponse);
                        if (result != null) obj = result;
                    }
                    break;
                case "xml":
                    {
                        CollAnalyticsData result = ProcessGetResponse<CollAnalyticsData>(authResponse);
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

        private AuthResponse RunRequest(QueryMethod method, string url, string data)
        {
            AuthRequest authRequest = new AuthRequest(_consumerKey, _consumerSecret, _appName);
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
                    obj = ConvertResponse<T>(message);
                    return obj;
                case 202:
                    return obj;
                default:
                    ResolveError(authResponse);
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
                    return status;
                case 202:
                    return status;
                default:
                    ResolveError(authResponse);
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

        private List<DocAnalyticData> RequestQueueBatch(string url, List<Document> tasks)
        {
            string data = "";
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

            return RequestAnalyticData<DocAnalyticData>(url, data);
        }

        private List<T> RequestAnalyticData<T>(string url, string data)
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
                                    List<T> result = ProcessGetResponse<List<T>>(authResponse);
                                    if (result != null) obj = result;
                                }
                                break;
                            case "xml":
                                {
                                    T result = ProcessGetResponse<T>(authResponse);
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
                    ResolveError(authResponse);
                    return null;
            }
        }

        private void ResolveError(AuthResponse authResponse)
        {
            int status = (int)authResponse.Status;
            string message = authResponse.Data;
            OnError(this, new ResponseEventArgs(authResponse.Status, message));
        }

        #endregion
    }
}
