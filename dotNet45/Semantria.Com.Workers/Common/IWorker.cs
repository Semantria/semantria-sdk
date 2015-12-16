using System;
using System.Dynamic;

namespace Semantria.Com.Workers
{
    /// <summary>
    /// Delegate that defines signature for method execution callback.
    /// </summary>
    /// <param name="sender">Calling object, the worker itself.</param>
    /// <param name="metrics">Request execution metrics.</param>
    public delegate void MethodExecutedCallbackHandler(object sender, RequestMetrics metrics);

    /// <summary>
    /// Delegate that defines signature for record queuing/delivering callback.
    /// </summary>
    /// <param name="sender">Calling object, the worker itself.</param>
    /// <param name="record">Queued/retrieved record</param>
    /// <param name="metrics">Document metrics, like execution time, etc.</param>
    public delegate void RecordStateChangedCallbackHandler(object sender, dynamic record, DocumentMetrics metrics);

    /// <summary>
    /// Delegate that defines signature for errors callback.
    /// </summary>
    /// <param name="sender">Calling object, the worker itself.</param>
    /// <param name="ex">Exception occured in the worker.</param>
    public delegate void ErrorOccurredCallbackHandler(object sender, Exception ex);

    /// <summary>
    /// IWorker interface that defines a signature for workers.
    /// </summary>
    public interface IWorker
    {
        /// <summary>
        /// Unique Worker identifier.
        /// </summary>
        string WorkerId
        {
            get;
        }

        /// <summary>
        /// Number of retries the worker will try to process a document.
        /// The worker reschedules a document if given data provider supports it (IDataProvider.CanReschedule). 
        /// </summary>
        int NumberOfRetries
        {
            get;
            set;
        }

        /// <summary>
        /// Shows current state of the worker.
        /// </summary>
        bool IsAlive
        {
            get;
        }

        /// <summary>
        /// Data provider used by the worker to get source data for analysis.
        /// </summary>
        IDataProvider DataProvider
        {
            get;
        }

        /// <summary>
        /// The callback method that informs subscriber about executed method of the engine.
        /// Can be used for logging purposes to know that something is going on inside the worker.
        /// </summary>
        MethodExecutedCallbackHandler ExecutedMethodCallback
        {
            get;
            set;
        }

        /// <summary>
        /// The callback method that delivers analisis results to subscriber.
        /// </summary>
        RecordStateChangedCallbackHandler OutputReceivedCallback
        {
            get;
            set;
        }

        /// <summary>
        /// The callback method that notifies listener about queued record.
        /// </summary>
        RecordStateChangedCallbackHandler RecordQueuedCallback
        {
            get;
            set;
        }

        /// <summary>
        /// The callback method that reports errors occured in the worker.
        /// </summary>
        ErrorOccurredCallbackHandler ErrorOccurredCallback
        {
            get;
            set;
        }

        /// <summary>
        /// Initializes the worker.
        /// </summary>
        /// <param name="args">Optional parameters required for worker initialization.</param>
        /// <returns>Returns worker initialization state. false if the worker was not initialized and can't be used.</returns>
        bool Initialize(params object[] args);

        /// <summary>
        /// The main execution end-point for the worker. Starts execution.
        /// </summary>
        /// <param name="state">Optional parameter that allows passing of any data to the worker if needed.</param>
        void Process(object state = null);
    }
}
