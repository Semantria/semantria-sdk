using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Text;
using System.Security.Cryptography;
using System.IO;
using System.Net;
using System.Web;

namespace Semantria.Com
{
    internal class AuthRequest : IDisposable
    { 
        #region Constructor

        public AuthRequest(string consumerKey, string consumerSecret, string appName, bool useCompression = false)
        {
            _consumerKey = consumerKey; 
            _consumerSecret = consumerSecret;
            _appName = appName;
            _useCompression = useCompression;

			//This option need to beuncommented to prevent verification of SSL certificate.
            //ServicePointManager.ServerCertificateValidationCallback = (a, b, c, d) => true;
        }

        #endregion

        #region IDisposable Members

        public void Dispose()
        {
        }

        #endregion
        
        #region Private

        private string _consumerKey = "";
        private string _consumerSecret = "";
        private string _appName = "";
        private bool _useCompression = false;
        
        #endregion

        #region Protected

        protected const string unreservedChars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.~";
        protected const string OAuthVersion = "1.0";
        protected const string OAuthParameterPrefix = "oauth_";
        protected const string OAuthConsumerKeyKey = "oauth_consumer_key";
        protected const string OAuthVersionKey = "oauth_version";
        protected const string OAuthSignatureMethodKey = "oauth_signature_method";
        protected const string OAuthSignatureKey = "oauth_signature";
        protected const string OAuthTimestampKey = "oauth_timestamp";
        protected const string OAuthNonceKey = "oauth_nonce";

        #endregion

        #region Public Methods
        /// <summary>
        /// Signut a web request using Auth.
        /// </summary>
        /// <param name="method">Http method GET, POST or DELETE</param>
        /// <param name="url">The full url, including the querystring.</param>
        /// <param name="postData">Data to post in xml or json fromat</param>
        /// <returns>The web server response.</returns>
        public AuthResponse AuthWebRequest(QueryMethod method, string url, string postData)
        {
            string querystring = "";
            string authheader = "";

            Uri uri = new Uri(url);

            string nonce = this.GenerateNonce();
            string timeStamp = this.GenerateTimeStamp();
            
            authheader = this.GenerateAuthorizationHeader(uri,
                method.ToString(),
                timeStamp,
                nonce);
                
            querystring = this.GenerateQueryString(uri,
                method.ToString(),
                timeStamp,
                nonce);

            AuthResponse authResponse = WebRequest(method, querystring, postData, authheader);

            return authResponse;
        }

        #endregion

        #region Private Methods

        private AuthResponse WebRequest(QueryMethod method, string url, string postData, string authheader)
        {
            HttpWebRequest webRequest = null;
            StreamWriter requestWriter = null;

            webRequest = (HttpWebRequest)System.Net.WebRequest.Create(url);
            webRequest.Method = method.ToString();
            webRequest.Credentials = CredentialCache.DefaultCredentials;

            webRequest.Headers.Add("x-app-name", _appName);
            webRequest.Headers.Add("Authorization", authheader);
            if (_useCompression == true)
            {
                webRequest.Headers.Add("Accept-Encoding", "gzip,deflate");
                webRequest.AutomaticDecompression = DecompressionMethods.GZip | DecompressionMethods.Deflate;
            }

            if (method == QueryMethod.POST || method == QueryMethod.DELETE)
            {
                webRequest.ContentType = "application/x-www-form-urlencoded";

                //Write the data.
                requestWriter = new StreamWriter(webRequest.GetRequestStream());
                try
                {
                    requestWriter.Write(postData);
                }
                catch
                {
                    throw;
                }
                finally
                {
                    requestWriter.Close();
                    requestWriter = null;
                }
            }

            AuthResponse authResponse = WebResponseGet(webRequest);
            authResponse.Method = method;
            
            webRequest = null;

            return authResponse;
        }

        private AuthResponse WebResponseGet(HttpWebRequest webRequest)
        {
            StreamReader responseReader = null;
            AuthResponse authResponse = new AuthResponse();
            Stream stream = null;

            try
            {
                HttpWebResponse response = (HttpWebResponse)webRequest.GetResponse();
                using (response)
                {
                    authResponse.Status = response.StatusCode;
                    stream = response.GetResponseStream();
                    responseReader = new StreamReader(stream);
                    authResponse.Data = responseReader.ReadToEnd();
                }
            }
            catch (WebException e)
            {
                if (e.Status == WebExceptionStatus.ProtocolError)
                {
                    HttpWebResponse response = (HttpWebResponse)e.Response;
                    using (response)
                    {
                        authResponse.Status = response.StatusCode;
                        authResponse.Data = response.StatusDescription;

                        stream = response.GetResponseStream();
                        if (null != stream && stream.CanRead)
                        {
                            responseReader = new StreamReader(stream);
                            authResponse.Data = responseReader.ReadToEnd();
                        }
                    }
                }
                else
                {
                    authResponse.Status = HttpStatusCode.BadRequest;
                    authResponse.Data = e.Message;
                }
            }
            finally
            {
                if (stream != null)
                {
                    stream.Close();
                }
                if (responseReader != null)
                {
                    responseReader.Close();
                    responseReader = null;
                }
            }

            return authResponse;
        }

        private string GenerateQueryUrl(Uri url)
        {
            string normalizedUrl = string.Format("{0}://{1}", url.Scheme, url.Host);
            if (!((url.Scheme == "http" && url.Port == 80) || (url.Scheme == "https" && url.Port == 443)))
            {
                normalizedUrl += ":" + url.Port;
            }
            normalizedUrl += url.AbsolutePath;
            return normalizedUrl;
        }

        private string GenerateQueryString(Uri url, string httpMethod, string timeStamp, string nonce)
        {
            List<QueryParameter> parameters = this.GetQueryParameters(url.Query);
            parameters.Add(new QueryParameter(OAuthVersionKey, OAuthVersion));
            parameters.Add(new QueryParameter(OAuthNonceKey, nonce));
            parameters.Add(new QueryParameter(OAuthTimestampKey, timeStamp));
            parameters.Add(new QueryParameter(OAuthSignatureMethodKey, "HMAC-SHA1"));
            parameters.Add(new QueryParameter(OAuthConsumerKeyKey, _consumerKey));

            parameters.Sort(new QueryParameterComparer());

            string normalizedUrl = this.GenerateQueryUrl(url);
            string normalizedRequestParameters = this.NormalizeRequestParameters(parameters);

            StringBuilder signatureBase = new StringBuilder();
            signatureBase.AppendFormat("{0}?", normalizedUrl);
            signatureBase.AppendFormat("{0}", normalizedRequestParameters);

            return signatureBase.ToString();
        }

        private string GenerateAuthorizationHeader(Uri url, string httpMethod, string timeStamp, string nonce)
        {
            string query = this.GenerateQueryString(url, httpMethod, timeStamp, nonce);
            query = this.UrlEncode(query);
            string md5cs = this.CalculateMD5Hash(_consumerSecret);

            HMACSHA1 hmacsha1 = new HMACSHA1();
            hmacsha1.Key = Encoding.UTF8.GetBytes(md5cs);
            string hash = this.ComputeHash(hmacsha1, query);
            hash = HttpUtility.UrlEncode(hash);

            List<QueryParameter> parameters = this.GetQueryParameters(url.Query);
            parameters.Add(new QueryParameter("OAuth realm", ""));
            parameters.Add(new QueryParameter(OAuthVersionKey, OAuthVersion));
            parameters.Add(new QueryParameter(OAuthNonceKey, nonce));
            parameters.Add(new QueryParameter(OAuthTimestampKey, timeStamp));
            parameters.Add(new QueryParameter(OAuthSignatureMethodKey, "HMAC-SHA1"));
            parameters.Add(new QueryParameter(OAuthConsumerKeyKey, _consumerKey));
            parameters.Add(new QueryParameter(OAuthSignatureKey, hash));

            parameters.Sort(new QueryParameterComparer());

            string normalizedHeaderParameters = NormalizeHeaderParameters(parameters);

            StringBuilder signatureBase = new StringBuilder();
            signatureBase.AppendFormat("{0}", normalizedHeaderParameters);

            return signatureBase.ToString();
        }

        private List<QueryParameter> GetQueryParameters(string parameters)
        {
            if (parameters.StartsWith("?"))
            {
                parameters = parameters.Remove(0, 1);
            }

            List<QueryParameter> result = new List<QueryParameter>();

            if (!string.IsNullOrEmpty(parameters))
            {
                string[] p = parameters.Split('&');
                foreach (string s in p)
                {
                    if (!string.IsNullOrEmpty(s) && !s.StartsWith(OAuthParameterPrefix))
                    {
                        if (s.IndexOf('=') > -1)
                        {
                            string[] temp = s.Split('=');
                            result.Add(new QueryParameter(temp[0], temp[1]));
                        }
                        else
                        {
                            result.Add(new QueryParameter(s, string.Empty));
                        }
                    }
                }
            }

            return result;
        }

        private string NormalizeRequestParameters(IList<QueryParameter> parameters)
        {
            StringBuilder sb = new StringBuilder();
            QueryParameter p = null;
            for (int i = 0; i < parameters.Count; i++)
            {
                p = parameters[i];
                sb.AppendFormat("{0}={1}", p.Name, p.Value);

                if (i < parameters.Count - 1)
                {
                    sb.Append("&");
                }
            }

            return sb.ToString();
        }

        private string NormalizeHeaderParameters(IList<QueryParameter> parameters)
        {
            StringBuilder sb = new StringBuilder();
            QueryParameter p = null;
            for (int i = 0; i < parameters.Count; i++)
            {
                p = parameters[i];
                sb.AppendFormat("{0}=\"{1}\"", p.Name, p.Value);

                if (i < parameters.Count - 1)
                {
                    sb.Append(",");
                }
            }

            return sb.ToString();
        }

        private string ComputeHash(HashAlgorithm hashAlgorithm, string data)
        {
            byte[] dataBuffer = System.Text.Encoding.UTF8.GetBytes(data);
            byte[] hashBytes = hashAlgorithm.ComputeHash(dataBuffer);

            return Convert.ToBase64String(hashBytes);
        }

        private string CalculateMD5Hash(string input)
        {
            MD5 md5 = System.Security.Cryptography.MD5.Create();
            byte[] inputBytes = System.Text.Encoding.UTF8.GetBytes(input);
            byte[] hash = md5.ComputeHash(inputBytes);

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < hash.Length; i++)
            {
                sb.Append(hash[i].ToString("X2"));
            }
            return sb.ToString().ToLower();
        }

        private string UrlEncode(string value)
        {
            StringBuilder result = new StringBuilder();

            foreach (char symbol in value)
            {
                if (unreservedChars.IndexOf(symbol) != -1)
                {
                    result.Append(symbol);
                }
                else
                {
                    result.Append('%' + String.Format("{0:X2}", (int)symbol));
                }
            }

            return result.ToString();
        }

        private string GenerateTimeStamp()
        {
            // Default implementation of UNIX time of the current UTC time
            TimeSpan ts = DateTime.UtcNow - new DateTime(1970, 1, 1, 0, 0, 0, 0);
            return Convert.ToInt64(ts.TotalSeconds).ToString();
        }

        private string GenerateNonce()
        {
            return this.NextUInt64().ToString();
            // Just a simple implementation of a random number between 123400 and 9999999
            //Random random = new Random();
            //return random.Next(123400, 9999999).ToString();
        }

        private UInt64 NextUInt64()
        {
            var bytes = new byte[sizeof(UInt64)];
            RNGCryptoServiceProvider gen = new RNGCryptoServiceProvider();
            gen.GetBytes(bytes);
            return BitConverter.ToUInt64(bytes, 0);
        }

        #endregion
    }
}
