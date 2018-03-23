using System;
using System.IO;
using System.Collections.Generic;
using Semantria.Com.Serializers;
using System.Collections;
using System.Linq;
using System.Net;
using System.Web;

namespace Semantria.Com
{
    public sealed class Session : IDisposable
    {
        #region Constructor
        
        private Session()
        {
        }
        
        private Session(string consumerKey, string consumerSecret, string appName, bool usedAPIkeys = true)
        {
            if (usedAPIkeys)
            {
                _consumerKey = consumerKey;
                _consumerSecret = consumerSecret;
            }
            else
            {
                _username = consumerKey;
                _password = consumerSecret;
            }

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
        private string _username = string.Empty;
        private string _password = string.Empty;
        private string _appName = string.Empty;
        private ISerializer _serializer = new JsonSerializer();
        private string _format = "json";
        private string _authHost = "https://semantria.com/auth";
        private string _authAppKey = "cd954253-acaf-4dfa-a417-0a8cfb701f12";
        private string _host = "https://api.semantria.com";
        private bool _useCompression = false;
        private string _apiVersion = "4.2";
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

        internal bool ReUseSession
        {
            get;
            set;
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
            return CreateSession(consumerKey, consumerSecret, null);
        }

        public static Session CreateSession(string consumerKey, string consumerSecret, string appName)
        {
            return new Session(consumerKey, consumerSecret, appName);
        }

        public static Session CreateUserSession(string username, string password, bool reUseSession = true)
        {
            return new Session(username, password, null, false) { ReUseSession = reUseSession };
        }

        public static Session CreateUserSession(string username, string password, string appName, bool reUseSession = true)
        {
            return new Session(username, password, appName, false) { ReUseSession = reUseSession };
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
            AuthResponse authResponse = RunRequest(QueryMethod.GET, url, null);
            return ProcessGetResponse(authResponse);
        }

        public dynamic GetSubscription()
        {
            //GET https://api.semantria.com/subscription.json
            string url = String.Format("{0}/subscription.{1}", _host, _format);
            AuthResponse authResponse = RunRequest(QueryMethod.GET, url, null);
            return ProcessGetResponse(authResponse);
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
            
            AuthResponse authResponse = RunRequest(QueryMethod.GET, url, null);
            return ProcessGetResponse(authResponse);
        }

        public dynamic GetSupportedFeatures(string language = null)
        {
            //GET https://api.semantria.com/features.json
            string url = String.Format("{0}/features.{1}", _host, _format);

            if (!String.IsNullOrEmpty(language))
            {
                url = String.Format("{0}/features.{1}?language={2}", _host, _format, language);
            }

            AuthResponse authResponse = RunRequest(QueryMethod.GET, url, null);
            dynamic obj = ProcessGetResponse(authResponse);
            
            return obj;
        }
       
        #endregion

        #region Configuration

        public dynamic GetConfigurations()
        {
            AuthResponse authResponse = Get("configurations");
            dynamic obj = ProcessGetResponse(authResponse);
            return obj;
        }


        public dynamic AddConfigurations(dynamic items)
        {
            return Add(QueryMethod.POST, items, "configurations");
        }

        public dynamic UpdateConfigurations(dynamic items)
        {
            return Update(QueryMethod.PUT, items, "configurations");
        }

        public int RemoveConfigurations(dynamic items)
        {
            return Delete(QueryMethod.DELETE, items, "configurations");
        }

        public dynamic CloneConfiguration(string name, string template)
        {
            var item = new[] { new {name = name, template = template} };
            return Add(QueryMethod.POST, item, "configurations");
        }

        #endregion Configuration

        #region Blacklist

        public dynamic GetBlacklist(string configId = null)
        {
            AuthResponse authResponse = Get("blacklist", configId);
            dynamic obj = ProcessGetResponse(authResponse);
            return obj;
        }

        public dynamic AddBlacklist(dynamic items, string configId = null)
        {
            return Add(QueryMethod.POST, items, "blacklist", configId);
        }

        public dynamic UpdateBlacklist(dynamic items, string configId = null)
        {
            return Update(QueryMethod.PUT, items, "blacklist", configId);
        }

        public int RemoveBlacklist(dynamic items, string configId = null)
        {
            return Delete(QueryMethod.DELETE, items, "blacklist", configId);
        }

        #endregion Blacklist

        #region Category

        public dynamic GetCategories(string configId = null)
        {
            AuthResponse authResponse = Get("categories", configId);
            return ProcessGetResponse(authResponse);
        }

        public dynamic AddCategories(dynamic items, string configId = null)
        {
            return Add(QueryMethod.POST, items, "categories", configId);
        }

        public dynamic UpdateCategories(dynamic items, string configId = null)
        {
            return Update(QueryMethod.PUT, items, "categories", configId);
        }

        public int RemoveCategories(dynamic items, string configId = null)
        {
            return Delete(QueryMethod.DELETE, items, "categories", configId);
        }

        #endregion Category

        #region Query

        public dynamic GetQueries(string configId = null)
        {
            AuthResponse authResponse = Get("queries", configId);
            return ProcessGetResponse(authResponse);
        }

        public dynamic AddQueries(dynamic items, string configId = null)
        {
            return Add(QueryMethod.POST, items, "queries", configId);
        }

        public dynamic UpdateQueries(dynamic items, string configId = null)
        {
            return Update(QueryMethod.PUT, items, "queries", configId);
        }

        public int RemoveQueries(dynamic items, string configId = null)
        {
            return Delete(QueryMethod.DELETE, items, "queries", configId);
        }

        #endregion Query

        #region Entity

        public dynamic GetEntities(string configId = null)
        {
            AuthResponse authResponse = Get("entities", configId);
            return ProcessGetResponse(authResponse);
        }

        public dynamic AddEntities(dynamic items, string configId = null)
        {
            return Add(QueryMethod.POST, items, "entities", configId);
        }

        public dynamic UpdateEntities(dynamic items, string configId = null)
        {
            return Update(QueryMethod.PUT, items, "entities", configId);
        }

        public int RemoveEntities(dynamic items, string configId = null)
        {
            return Delete(QueryMethod.DELETE, items, "entities", configId);
        }

        #endregion Entity

        #region SentimentPhrase

        public dynamic GetSentimentPhrases(string configId = null)
        {
            AuthResponse authResponse = Get("phrases", configId);
            return ProcessGetResponse(authResponse);
        }

        public dynamic AddSentimentPhrases(dynamic items, string configId = null)
        {
            return Add(QueryMethod.POST, items, "phrases", configId);
        }

        public dynamic UpdateSentimentPhrases(dynamic items, string configId = null)
        {
            return Update(QueryMethod.PUT, items, "phrases", configId);
        }

        public int RemoveSentimentPhrases(dynamic items, string configId = null)
        {
            return Delete(QueryMethod.DELETE, items, "phrases", configId);
        }

        #endregion SentimentPhrase

        #region Taxonomy

        public dynamic GetTaxonomy(string configId = null)
        {
            AuthResponse authResponse = Get("taxonomy", configId);
            return ProcessGetResponse(authResponse);
        }

        public dynamic AddTaxonomy(dynamic items, string configId = null)
        {
            return Add(QueryMethod.POST, items, "taxonomy", configId);
        }

        public dynamic UpdateTaxonomy(dynamic items, string configId = null)
        {
            return Update(QueryMethod.PUT, items, "taxonomy", configId);
        }

        public int RemoveTaxonomy(dynamic items, string configId = null)
        {
            return Delete(QueryMethod.DELETE, items, "taxonomy", configId);
        }

        #endregion Taxonomy

        #region Document

        public int QueueDocument(dynamic task, string configId = null)
        {
            string url = String.Format("{0}/document.{1}", _host, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document.{1}?config_id={2}", _host, _format, configId);
            }
            string data = _serializer.Serialize(task);
            dynamic result = PostAnalyticData(url, data);

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

            dynamic result = PostQueueBatch(url, tasks);

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
            string encodedId = HttpUtility.UrlEncode(id);
            string url = String.Format("{0}/document/{1}.{2}", _host, encodedId, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document/{1}.{2}?config_id={3}", _host, encodedId, _format, configId);
            }
            AuthResponse authResponse = RunRequest(QueryMethod.GET, url, null);
            return ProcessGetResponse(authResponse);
        }

        public int CancelDocument(string id, string configId = null)
        {
            string encodedId = HttpUtility.UrlEncode(id);
            string url = String.Format("{0}/document/{1}.{2}", _host, encodedId, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document/{1}.{2}?config_id={3}", _host, encodedId, _format, configId);
            }
            AuthResponse authResponse = RunRequest(QueryMethod.DELETE, url, null);
            return ProcessPostResponse(authResponse);
        }

        public List<dynamic> GetProcessedDocuments(string configId = null)
        {
            string url = String.Format("{0}/document/processed.{1}", _host, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/document/processed.{1}?config_id={2}", _host, _format, configId);
            }
            return RequestProcessedList(url);
        }

        public List<dynamic> GetProcessedDocumentsByJobId(string jobId)
        {
            if (String.IsNullOrEmpty(jobId))
            {
                throw new ArgumentNullException("jobId", "jobId parameter can not be null or string empty.");
            }

            string url = String.Format("{0}/document/processed.{1}?job_id={2}", _host, _format, jobId);
            return RequestProcessedList(url);
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
            dynamic result = PostAnalyticData(url, data);

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
            string encodedId = HttpUtility.UrlEncode(id);
            string url = String.Format("{0}/collection/{1}.{2}", _host, encodedId, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/collection/{1}.{2}?config_id={3}", _host, encodedId, _format, configId);
            }
            AuthResponse authResponse = RunRequest(QueryMethod.GET, url, null);
            return ProcessGetResponse(authResponse);
        }

        public int CancelCollection(string id, string configId = null)
        {
            string encodedId = HttpUtility.UrlEncode(id);
            string url = String.Format("{0}/collection/{1}.{2}", _host, encodedId, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/collection/{1}.{2}?config_id={3}", _host, encodedId, _format, configId);
            }
            AuthResponse authResponse = RunRequest(QueryMethod.DELETE, url, null);
            return ProcessPostResponse(authResponse);
        }

        public List<dynamic> GetProcessedCollections(string configId = null)
        {
            string url = String.Format("{0}/collection/processed.{1}", _host, _format);
            if (!String.IsNullOrEmpty(configId))
            {
                url = String.Format("{0}/collection/processed.{1}?config_id={2}", _host, _format, configId);
            }
            return RequestProcessedList(url);
        }

        public List<dynamic> GetProcessedCollectionsByJobId(string jobId)
        {
            if (String.IsNullOrEmpty(jobId))
            {
                throw new ArgumentNullException("jobId", "jobId parameter can not be null or string empty.");
            }

            string url = String.Format("{0}/collection/processed.{1}?job_id={2}", _host, _format, jobId);
            return RequestProcessedList(url);
        }

        #endregion Collection

        #region Salience user directory

        public static readonly string ZIP_FORMAT = "zip";
        public static readonly string TAR_FORMAT = "tar";
        public static readonly string TARGZ_FORMAT = "tar.gz";
        public static readonly List<string> archiveFormats = new List<string>() {
                ZIP_FORMAT, TAR_FORMAT, TARGZ_FORMAT, 
        };

        private string getArchiveFormat(string path) {
            foreach (string format in archiveFormats) {
                if (path.EndsWith("." + format)) {
                    return format;
                }
            }
            return ZIP_FORMAT;
        }

        public byte[] GetUserDirectory(string configId) {
            return GetUserDirectory(configId, ZIP_FORMAT);
        }

        public byte[] GetUserDirectory(string configId, string format) {
            AuthResponse authResponse = GetBinary("salience/user-directory." + format, configId);
            return authResponse.BinaryData;
        }

        public void WriteUserDirectoryToFile(string configId, string path) {
             byte[] bytes = GetUserDirectory(configId, getArchiveFormat(path));
             if (bytes == null) {
                 throw new SystemException(String.Format(
                     "Can't get user directory for config {0}. Check error handler for specific error message",
                     configId));
             }
            WriteByteArrayToFile(path, bytes);
        }

        private static void WriteByteArrayToFile(string fileName, byte[] byteArray)
        {
            using (var fs = new FileStream(fileName, FileMode.Create, FileAccess.Write))
            {
                fs.Write(byteArray, 0, byteArray.Length);
            }
        }

        #endregion Salience user directory

        #region Private request and response support

        private string makeUrl(string tag, string configId, bool omitFormat=false) {
            string url = String.Format("{0}/{1}", _host, tag);
            if (!omitFormat)
            {
                url += "." + _format;
            }
            if (!String.IsNullOrEmpty(configId))
            {
                url += String.Format("?config_id={0}", configId);
            }
            return url;
        }

        private AuthResponse Get(string tag, string configId = null)
        {
            string url = makeUrl(tag, configId);
            return RunRequest(QueryMethod.GET, url, null);
        }

        private AuthResponse GetBinary(string tag, string configId = null)
        {
            string url = makeUrl(tag, configId, true);
            return RunRequest(QueryMethod.GET, url, null, true);
        }

        private dynamic Add(QueryMethod method, dynamic items, string tag, string configId = null)
        {
            string url = makeUrl(tag, configId);
            string data = _serializer.Serialize(items);

            AuthResponse authResponse = RunRequest(method, url, data);
            dynamic obj = ProcessGetResponse(authResponse);

            return obj;
        }

        private dynamic Update(QueryMethod method, dynamic items, string tag, string configId = null)
        {
            string url = makeUrl(tag, configId);
            string data = _serializer.Serialize(items);
            
            AuthResponse authResponse = RunRequest(method, url, data);
            dynamic obj = ProcessGetResponse(authResponse);
            
            return obj;
        }

        private int Delete(QueryMethod method, dynamic items, string tag, string configId = null)
        {
            string url = makeUrl(tag, configId);
            string data = _serializer.Serialize(items);

            AuthResponse authResponse = RunRequest(method, url, data);
            return ProcessPostResponse(authResponse);
        }

        private WebResponse RunRegularRequest(QueryMethod method, string url, string data)
        {
            WebRequest request = WebRequest.Create(url);
            request.Proxy = WebRequest.DefaultWebProxy;
            request.ContentType = "application/json";
            request.Method = method.ToString();

            if (method == QueryMethod.POST)
            {
                using (StreamWriter writter = new StreamWriter(request.GetRequestStream()))
                {
                    writter.Write(data);
                }
            }

            HttpWebResponse response = null;
            try
            {
                response = (HttpWebResponse)request.GetResponse();
            }
            catch (WebException e)
            {
                if (e.Status == WebExceptionStatus.ProtocolError)
                {
                    using (HttpWebResponse exResponse = (HttpWebResponse)e.Response)
                    {
                        OnError(this, new ResponseErrorEventArgs(null, exResponse.StatusCode, exResponse.StatusDescription));
                    }
                }
                else
                {
                    OnError(this, new ResponseErrorEventArgs(null, HttpStatusCode.BadRequest, e.Message));
                }
            }

            return response as WebResponse;
        }

        private dynamic GetRegularResponseData(WebResponse response)
        {
            if (response != null)
            {
                using (StreamReader reader = new StreamReader(response.GetResponseStream()))
                {
                    string data = reader.ReadToEnd();
                    return _serializer.Deserialize(data);
                }
            }

            return null;
        }

        private bool GetTemporaryKeys()
        {
            string requestUrl = string.Format("{0}/session.json?appkey={1}", _authHost, _authAppKey);
            string requestData = _serializer.Serialize(new
            {
                username = _username,
                password = _password
            });

            WebResponse resp = RunRegularRequest(QueryMethod.POST, requestUrl, requestData);
            dynamic respObj = GetRegularResponseData(resp);

            if (respObj == null)
            {
                return false;
            }

            string sessionId = respObj.id;
            WriteSessionFile(sessionId);

            _consumerKey = respObj.custom_params.key;
            _consumerSecret = respObj.custom_params.secret;

            return true;
        }

        private bool ValidateSession(string session)
        {
            string requestUrl = string.Format("{0}/session/{1}.json?appkey={2}", _authHost, session, _authAppKey);
            WebResponse resp = RunRegularRequest(QueryMethod.GET, requestUrl, null);
            dynamic respObj = GetRegularResponseData(resp);

            if (respObj == null)
            {
                return false;
            }

            string sessionId = respObj.id;
            WriteSessionFile(sessionId);

            _consumerKey = respObj.custom_params.key;
            _consumerSecret = respObj.custom_params.secret;

            return true;
        }

        private void WriteSessionFile(string sessionId)
        {
            string sessionFile = Path.Combine(Path.GetTempPath(), "semantria-session.dat");
            using (StreamWriter writer = new StreamWriter(sessionFile))
            {
                writer.Write(sessionId);
            }
        }

        private string ReadSessionFile()
        {
            string sessionFile = Path.Combine(Path.GetTempPath(), "semantria-session.dat");
            if (File.Exists(sessionFile))
            {
                using (StreamReader reader = new StreamReader(sessionFile))
                {
                    string sessionId = reader.ReadToEnd();
                    return sessionId;
                }
            }

            return null;
        }

        private AuthResponse RunRequest(QueryMethod method, string url, string data, bool isBinary=false)
        {
            if (string.IsNullOrEmpty(_consumerKey) && string.IsNullOrEmpty(_consumerSecret))
            {
                string sessionId = ReadSessionFile();
                if (!string.IsNullOrEmpty(sessionId) && ReUseSession)
                {
                    if (!ValidateSession(sessionId))
                    {
                        if (!GetTemporaryKeys())
                        {
                            return null;
                        }
                    }
                }
                else
                {
                    if (!GetTemporaryKeys())
                    {
                        return null;
                    }
                }
            }

            return ExecuteRequest(method, url, data, isBinary);
        }

        private AuthResponse ExecuteRequest(QueryMethod method, string url, string data, bool isBinary)
        {
            string appName = GetAppName();
            AuthRequest authRequest = new AuthRequest(_consumerKey, _consumerSecret, appName, _useCompression, _apiVersion);
            using (authRequest)
            {
                OnRequest(this, new RequestEventArgs(method.ToString(), url, data));
                AuthResponse authResponse = authRequest.AuthWebRequest(method, url, data, isBinary);
                OnResponse(this, new ResponseEventArgs(authResponse.Status, authResponse.Data));
                return authResponse;
            }
        }

        private List<dynamic> RequestProcessedList(string url)
        {
            AuthResponse authResponse = RunRequest(QueryMethod.GET, url, null);
            dynamic obj = new List<dynamic>();

            dynamic result = ProcessGetResponse(authResponse);
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
                case 202:
                    return status;
                default:
                    ResolveError(authResponse);
                    return status;
            }
        }

        private dynamic PostQueueBatch(string url, dynamic tasks)
        {
            string data = _serializer.Serialize(tasks);
            return PostAnalyticData(url, data);
        }

        private dynamic PostAnalyticData(string url, string data)
        {
            AuthResponse authResponse = RunRequest(QueryMethod.POST, url, data);

            int status = (int)authResponse.Status;
            string message = authResponse.Data;

            dynamic obj = new List<dynamic>();

            switch (status)
            {
                case 200:
                    {
                        dynamic result = ProcessGetResponse(authResponse);
                        if (result != null) obj = result;
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
            OnError(this, new ResponseErrorEventArgs(null, authResponse.Status, message));
        }


        #endregion
    }
}
