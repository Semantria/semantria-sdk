using System;

namespace Semantria.Com.Workers
{
    public class DataProviderException : Exception
    {
        public DataProviderException(string message) :
            base(message)
        {
        }

        public DataProviderException(string message, Exception innerException) :
            base(message, innerException)
        {
        }
    }
}
