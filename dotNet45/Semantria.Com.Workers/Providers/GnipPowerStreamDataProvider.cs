using System;
using System.IO;
using System.IO.Compression;
using System.Text.RegularExpressions;
using System.Collections.Generic;
using System.Dynamic;
using System.Net;
using System.Web.Script.Serialization;

using Semantria.Com.Serializers.Data.Json;

namespace Semantria.Com.Workers
{
    public sealed class GnipPowerStreamDataProvider : IDataProvider
    {
        #region Members

        string _username = string.Empty;
        string _password = string.Empty;
        string _dataUrl = string.Empty;

        IEnumerator<Uri> _linksEnumerator = null;
        List<dynamic> _tweets = null;
        int _tweetIndex = 0;

        #endregion

        #region Constructor

        public GnipPowerStreamDataProvider(string username, string password, string dataUrl)
        {
            _username = username;
            _password = password;
            _dataUrl = dataUrl;
        }

        #endregion

        #region IDataProvider implementation

        public bool CanReschedule
        {
            get
            {
                return false;
            }
        }

        public bool HasData
        {
            get
            {
                if (_linksEnumerator != null)
                {
                    return _linksEnumerator.Current != null;
                }
                else
                {
                    return false;
                }
            }
        }

        public bool SupportBatches
        {
            get
            {
                return true;
            }
        }

        public bool Initialize(params object[] args)
        {
            Uri dataUri = new Uri(_dataUrl);
            List<Uri> dataLinksList = new List<Uri>();

            WebRequest request = WebRequest.CreateDefault(dataUri);
            request.Credentials = new NetworkCredential(_username, _password);
            request.Timeout = 360000;

            using (WebResponse response = request.GetResponse())
            using (StreamReader reader = new StreamReader(response.GetResponseStream()))
            {
                string responseData = reader.ReadToEnd();

                JavaScriptSerializer serializer = new JavaScriptSerializer();
                serializer.MaxJsonLength = Int32.MaxValue;

                serializer.RegisterConverters(new[] { new DynamicJsonObjectConverter() });
                dynamic serialized = serializer.Deserialize(responseData, typeof(DynamicJsonObject));

                foreach (dynamic link in serialized.urlList)
                {
                    Uri dataItemUri = new Uri(link as string);
                    dataLinksList.Add(dataItemUri);
                }
            }

            if (dataLinksList.Count > 0)
            {
                _linksEnumerator = dataLinksList.GetEnumerator();
                _linksEnumerator.MoveNext();
                return true;
            }

            return false;
        }

        public List<dynamic> ReadBatch(int size)
        {
            List<dynamic> batch = new List<dynamic>(size);

            for (int i = 0; i < size; i++)
            {
                dynamic tweet = ReadNext();
                if (tweet == null)
                {
                    break;
                }

                batch.Add(tweet);
            }

            return batch;
        }

        public dynamic ReadNext()
        {
            dynamic tweet = null;
            if (_linksEnumerator.Current == null)
            {
                return tweet;
            }

            if (_tweetIndex == 0)
            {
                string data = FetchLinkData(_linksEnumerator.Current);
                _tweets = GetTweets(data);

                if (_tweets.Count == 0)
                {
                    _linksEnumerator.MoveNext();
                    return ReadNext();
                }
            }

            tweet = _tweets[_tweetIndex];
            _tweetIndex++;

            if (_tweetIndex == _tweets.Count)
            {
                _tweetIndex = 0;
                _linksEnumerator.MoveNext();
            }

            return ExtractTweetData(tweet);
        }

        public bool Reschedule(dynamic record)
        {
            throw new NotImplementedException();
        }

        public bool RescheduleBatch(List<dynamic> batch)
        {
            throw new NotImplementedException();
        }

        #endregion

        #region Methods

        string FetchLinkData(Uri url)
        {
            string result = null;

            WebRequest request = WebRequest.CreateDefault(url);
            request.Credentials = new NetworkCredential(_username, _password);
            request.Timeout = 360000;

            using (WebResponse response = request.GetResponse())
            using (Stream compressedStream = response.GetResponseStream())
            using (GZipStream stream = new GZipStream(compressedStream, CompressionMode.Decompress))
            using (MemoryStream memStream = new MemoryStream())
            using (StreamReader reader = new StreamReader(memStream))
            {
                stream.CopyTo(memStream);
                memStream.Position = 0;

                result = reader.ReadToEnd();
                Regex regex = new Regex("\r?\n|\r");
                result = regex.Replace(result, string.Empty);
                result = result.Replace("}{", "},{");
                result = string.Format("[{0}]", result);
            }

            return result;
        }

        static List<dynamic> GetTweets(string data)
        {
            List<dynamic> result = new List<dynamic>();

            JavaScriptSerializer serializer = new JavaScriptSerializer();
            serializer.MaxJsonLength = Int32.MaxValue;
            serializer.RegisterConverters(new[] { new DynamicJsonObjectConverter() });
            dynamic serialized = serializer.Deserialize(data, typeof(DynamicJsonObject));

            int count = serialized[serialized.Count - 1]["info"].activity_count;
            for (int i = 0; i < count; i++)
            {
                result.Add(serialized[i]);
            }

            return result;
        }

        ExpandoObject ExtractTweetData(dynamic tweet)
        {
            Regex regex = new Regex("\r?\n|\r|\n|\t");

            string id = tweet.id as string;
            id = id.Substring(id.LastIndexOf(":") + 1);
            string message = regex.Replace(tweet.body as string, " ").Replace("\"", string.Empty).Replace("“", string.Empty).Replace("”", string.Empty);
            dynamic location = tweet.gnip.profileLocations;
            string region = (location != null) ? location[0].displayName : string.Empty;
            region = region.Replace("'", string.Empty);

            dynamic result = new ExpandoObject();
            result.id = id;
            result.text = message;
            result.tag = region;

            return result;
        }

        #endregion
    }
}
