using System;

namespace Semantria.Com.Workers
{
    public class DocumentMetrics
    {
        public string documentId;

        public DateTime Queued;
        public DateTime Retrieved;

        public double AnalysisTime
        {
            get
            {
                if (Queued > DateTime.MinValue && Retrieved > DateTime.MinValue)
                {
                    return (Retrieved - Queued).TotalSeconds;
                }
                else
                {
                    return 0;
                }
            }
        }

        public int NumberOfRetries;
        public int BatchSize;
    }

    public enum SemantriaAPImethod
    {
        GetSubscription,
        QueueBatch,
        RetrieveResultsByJobId
    }

    public class RequestMetrics
    {
        public RequestMetrics(SemantriaAPImethod executedMethod, TimeSpan executionTime)
        {
            ExecutedMethod = executedMethod;
            ExecutionTime = executionTime;
        }

        public RequestMetrics(SemantriaAPImethod executedMethod, TimeSpan executionTime, object state)
        {
            ExecutedMethod = executedMethod;
            ExecutionTime = executionTime;
            State = state;
        }

        public TimeSpan ExecutionTime;

        public SemantriaAPImethod ExecutedMethod;

        public object State;
    }
}
