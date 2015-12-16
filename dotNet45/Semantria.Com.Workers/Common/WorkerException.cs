using System;
using System.Net;

namespace Semantria.Com.Workers
{
    public class WorkerException : Exception
    {
        public WorkerException(string message) :
            base(message)
        {
        }

        public WorkerException(string message, Exception innerException) :
            base(message, innerException)
        {
        }
    }

    public class SemantriaWorkerException : WorkerException
    {
        public SemantriaWorkerException(string message, Exception innerException) :
            base(message, innerException)
        {
        }

        public SemantriaWorkerException(string message, HttpStatusCode statusCode, Exception innerException) :
            base(message, innerException)
        {
            StatusCode = statusCode;
        }

        public SemantriaWorkerException(string message, HttpStatusCode statusCode) :
            base(message)
        {
            StatusCode = statusCode;
        }

        public HttpStatusCode StatusCode
        {
            get;
            private set;
        }
    }
}
