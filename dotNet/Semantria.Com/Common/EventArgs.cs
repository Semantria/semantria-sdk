using System;
using System.Collections.Generic;
using System.Text;
using System.Net;
using Semantria.Com.Mapping.Output;

namespace Semantria.Com
{
    public class RequestEventArgs : EventArgs
    {
        public RequestEventArgs(string method, string url, string message)
        {
            _method = method;
            _url = url;
            _message = message;
        }

        private string _method;
        public string Method
        {
            get
            {
                return _method;
            }
        }

        private string _url;
        public string Url
        {
            get
            {
                return _url;
            }
        }

        private string _message;
        public string Message
        {
            get
            {
                return _message;
            }
        }
    }

    public class ResponseEventArgs : EventArgs
    {
        public ResponseEventArgs()
        {
        }

        public ResponseEventArgs(HttpStatusCode status, string message)
        {
            _status = status;
            _message = message;
        }

        private HttpStatusCode _status;
        public HttpStatusCode Status
        {
            get
            {
                return _status;
            }
        }

        private string _message;
        public string Message
        {
            get
            {
                return _message;
            }
        }
    }

    public class ResponseErrorEventArgs : ResponseEventArgs
    {
        public ResponseErrorEventArgs(Type type, HttpStatusCode status, string message) : base(status, message)
        {
            _type = type;
        }

        private Type _type;
        public Type Type
        {
            get
            {
                return _type;
            }
        }
    }

    public class DocsAutoResponseEventArgs : EventArgs
    {
        public DocsAutoResponseEventArgs(IList<DocAnalyticData> analyticdata)
        {
            _analyticdata = analyticdata;
        }

        private IList<DocAnalyticData> _analyticdata;
        public IList<DocAnalyticData> AnalyticData
        {
            get
            {
                return _analyticdata;
            }
        }
    }

    public class CollsAutoResponseEventArgs : EventArgs
    {
        public CollsAutoResponseEventArgs(IList<CollAnalyticData> analyticdata)
        {
            _analyticdata = analyticdata;
        }

        private IList<CollAnalyticData> _analyticdata;
        public IList<CollAnalyticData> AnalyticData
        {
            get
            {
                return _analyticdata;
            }
        }
    }
}
