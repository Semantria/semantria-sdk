using System;
using System.Collections.Generic;
using System.Text;
using System.Net;

namespace Semantria.Com
{
    internal class AuthResponse
    {
        public AuthResponse()
        {
        }

        public AuthResponse(HttpStatusCode status, string data)
        {
            _status = status;
            _data = data;
        }

        private QueryMethod _method = QueryMethod.GET;
        private HttpStatusCode _status = HttpStatusCode.Unauthorized;
        private string _data = "";


        public QueryMethod Method
        {
            get { return _method; }
            set { _method = value; }
        }

        public HttpStatusCode Status
        {
            get { return _status; }
            set { _status = value; }
        }

        public string Data
        {
            get { return _data; }
            set { _data = value; }
        }
    }
}
