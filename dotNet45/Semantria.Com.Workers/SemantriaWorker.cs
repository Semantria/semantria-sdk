using System;
using System.Dynamic;
using System.Collections.Generic;
using System.Threading;
using System.Diagnostics;
using System.Reflection;

namespace Semantria.Com.Workers
{
    public class SemantriaWorker : IWorker
    {
        #region Members

        int _docsInProgress = 0;

        string _dataDirPath = string.Empty;
        string _moveDirPath = string.Empty;

        string _apiKey = string.Empty;
        string _apiSecret = string.Empty;
        string _configId = string.Empty;

        Session _engine = null;
        List<Thread> _dataRetrievingWorkers = new List<Thread>();

        Dictionary<string, DocumentMetrics> _docsTracker = new Dictionary<string, DocumentMetrics>();

        #endregion

        #region Static members

        static int _workerIndex = 0;
        static object _syncProvider = new object();
        static object _syncTracker = new object();

        #endregion

        #region Constructor

        internal SemantriaWorker(IDataProvider provider, string apiKey, string apiSecret, string configId)
        {
            DataProvider = provider;

            _apiKey = apiKey;
            _apiSecret = apiSecret;
            _configId = configId;

            WorkerId = (++_workerIndex).ToString();
            PullingTimeout = 500;
            QueuingTimeout = 500;
            NumberOfRetries = 1;
            NumberOfRetrievalThreads = 1;
        }

        #endregion

        #region IWorker implementation

        public string WorkerId
        {
            get;
            private set;
        }

        public int NumberOfRetries
        {
            get;
            set;
        }

        public bool IsAlive
        {
            get
            {
                if (_docsInProgress > 0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        public IDataProvider DataProvider
        {
            get;
            private set;
        }

        public MethodExecutedCallbackHandler ExecutedMethodCallback
        {
            get;
            set;
        }

        public RecordStateChangedCallbackHandler OutputReceivedCallback
        {
            get;
            set;
        }

        public RecordStateChangedCallbackHandler RecordQueuedCallback
        {
            get;
            set;
        }

        public ErrorOccurredCallbackHandler ErrorOccurredCallback
        {
            get;
            set;
        }

        public bool Initialize(params object[] args)
        {
            if (_engine != null)
            {
                _engine.Dispose();
                _engine = null;
            }

            try
            {
                _engine = Session.CreateSession(_apiKey, _apiSecret);
                _engine.Error += _engine_Error;
            }
            catch (Exception ex)
            {
                SemantriaWorkerException workerException = new SemantriaWorkerException("Semantria session initialization failed!", ex);
                OnErrorOccurred(workerException);
                return false;
            }

            return true;
        }

        public void Process(object state = null)
        {
            Stopwatch watcher = new Stopwatch();
            watcher.Start();
            dynamic subscription = _engine.GetSubscription();
            watcher.Stop();

            OnMethodExecuted(new RequestMetrics(SemantriaAPImethod.GetSubscription, watcher.Elapsed));

            int batchSize = subscription.basic_settings.incoming_batch_limit;
            int docSize = subscription.basic_settings.characters_limit;
            DataProvider.CharactersLimit = docSize;

            if (DataProvider.SupportBatches)
            {
                while (DataProvider.HasData)
                {
                    List<dynamic> rawBatch = null;
                    lock (_syncProvider)
                    {
                        try
                        {
                            rawBatch = DataProvider.ReadBatch(batchSize);
                        }
                        catch (Exception ex)
                        {
                            OnErrorOccurred(ex);
                        }
                    }

                    if (rawBatch == null || rawBatch.Count > 0)
                    {
                        continue;
                    }

                    List<dynamic> batch = TransformRawBatch(rawBatch);

                    int status = QueueBatch(batch);
                    DateTime timestamp = DateTime.Now;
                    if (status == -1)
                    {
                        RetryBatch(batch);
                    }
                    else
                    {
                        UpdateTrackedList(batch, timestamp);
                    }

                    Thread.Sleep(QueuingTimeout);
                }
            }
            else
            {
                List<dynamic> batch = new List<dynamic>(batchSize);

                while (DataProvider.HasData)
                {
                    dynamic rawItem = null;
                    lock (_syncProvider)
                    {
                        try
                        {
                            rawItem = DataProvider.ReadNext();
                        }
                        catch (Exception ex)
                        {
                            OnErrorOccurred(ex);
                        }
                    }

                    dynamic item = ExpandoObjectToDynamic(rawItem);
                    batch.Add(item);

                    if (batch.Count == batchSize)
                    {
                        int status = QueueBatch(batch);
                        DateTime timestamp = DateTime.Now;
                        if (status == -1)
                        {
                            RetryBatch(batch);
                        }
                        else
                        {
                            UpdateTrackedList(batch, timestamp);
                        }

                        batch = new List<dynamic>(batchSize);
                        Thread.Sleep(QueuingTimeout);
                    }
                }

                if (batch.Count > 0)
                {
                    int status = QueueBatch(batch);
                    DateTime timestamp = DateTime.Now;
                    if (status == -1)
                    {
                        RetryBatch(batch);
                    }
                    else
                    {
                        UpdateTrackedList(batch, timestamp);
                    }
                }
            }
        }

        #endregion

        #region Properties

        public int PullingTimeout
        {
            get;
            set;
        }

        public int QueuingTimeout
        {
            get;
            set;
        }

        public int NumberOfRetrievalThreads
        {
            get;
            set;
        }

        #endregion

        #region Methods

        void _engine_Error(object sender, ResponseErrorEventArgs ea)
        {
            SemantriaWorkerException workerException = new SemantriaWorkerException(ea.Message, ea.Status);
            ErrorOccurredCallback(this, workerException);
        }

        void OnErrorOccurred(Exception ex)
        {
            if (ErrorOccurredCallback != null)
            {
                ErrorOccurredCallback(this, ex);
            }
        }

        void OnRecordQueued(dynamic record, DocumentMetrics metrics)
        {
            if (RecordQueuedCallback != null)
            {
                RecordQueuedCallback(this, record, metrics);
            }
        }

        void OnOutputReceived(dynamic output, DocumentMetrics metrics)
        {
            if (OutputReceivedCallback != null)
            {
                OutputReceivedCallback(this, output, metrics);
            }
        }

        void OnMethodExecuted(RequestMetrics metrics)
        {
            if (ExecutedMethodCallback != null)
            {
                ExecutedMethodCallback(this, metrics);
            }
        }

        dynamic DynamicToExpandoObject(dynamic obj)
        {
            if (obj == null)
            {
                return null;
            }

            dynamic expObj = new ExpandoObject();
            expObj.id = obj.id;
            expObj.text = obj.text;

            PropertyInfo tagInfo = obj.GetType().GetProperty("tag");
            if (tagInfo != null)
            {
                expObj.tag = obj.tag;
            }

            return expObj;
        }

        dynamic ExpandoObjectToDynamic(dynamic obj)
        {
            if (obj == null)
            {
                return null;
            }

            ExpandoObject expObj = obj as ExpandoObject;
            IDictionary<string, object> castedObj = expObj as IDictionary<string, object>;

            if (castedObj == null)
            {
                return null;
            }

            dynamic dynObj = null;
            if (castedObj.ContainsKey("tag"))
            {
                dynObj = new
                {
                    id = castedObj["id"],
                    text = castedObj["text"],
                    tag = castedObj["tag"],
                    job_id = string.Format("worker_{0}", WorkerId)
                };
            }
            else
            {
                dynObj = new
                {
                    id = castedObj["id"],
                    text = castedObj["text"],
                    job_id = string.Format("worker_{0}", WorkerId)
                };
            }

            return dynObj;
        }

        List<dynamic> TransformRawBatch(List<dynamic> rawBatch)
        {
            List<dynamic> batch = new List<dynamic>(rawBatch.Count);

            foreach (ExpandoObject rawItem in rawBatch)
            {
                dynamic item = ExpandoObjectToDynamic(rawItem);
                if (item == null)
                {
                    break;
                }

                batch.Add(item);
            }

            return batch;
        }

        void DataRetrievingWorker()
        {
            string jobId = string.Format("worker_{0}", WorkerId);

            while (_docsInProgress > 0)
            {
                dynamic results = null;
                Stopwatch watcher = new Stopwatch();
                DateTime retrieved = DateTime.MinValue;

                try
                {
                    watcher.Start();
                    results = _engine.GetProcessedDocumentsByJobId(jobId);
                    watcher.Stop();
                    retrieved = DateTime.Now;
                }
                catch (Exception ex)
                {
                    SemantriaWorkerException workerException = new SemantriaWorkerException("An exception occured during retrieval of the documents from the server.", ex);
                    OnErrorOccurred(workerException);
                }

                if (results == null || results.Count == 0)
                {
                    Thread.Sleep(PullingTimeout);
                    continue;
                }

                OnMethodExecuted(new RequestMetrics(SemantriaAPImethod.RetrieveResultsByJobId, watcher.Elapsed, results.Count));

                foreach (dynamic doc in results)
                {
                    DocumentMetrics metrics = new DocumentMetrics();
                    lock (_syncTracker)
                    {
                        if (!_docsTracker.ContainsKey(doc.id))
                        {
                            continue;
                        }

                        _docsTracker[doc.id].Retrieved = retrieved;
                        metrics = _docsTracker[doc.id];
                        _docsInProgress--;
                    }

                    OnOutputReceived(doc, metrics);
                }
            }
        } 

        void UpdateTrackedList(List<dynamic> batch, DateTime timestamp)
        {
            foreach (dynamic doc in batch)
            {
                lock (_syncTracker)
                {
                    if (!_docsTracker.ContainsKey(doc.id))
                    {
                        _docsTracker.Add(doc.id, new DocumentMetrics()
                        {
                            Queued = timestamp,
                            Retrieved = DateTime.MinValue,
                            NumberOfRetries = 1
                        });
                    }
                    else
                    {
                        _docsTracker[doc.id].Queued = timestamp;
                        _docsTracker[doc.id].Retrieved = DateTime.MinValue;
                        _docsTracker[doc.id].NumberOfRetries++;
                    }
                }

                DocumentMetrics metrics = _docsTracker[doc.id];
                dynamic expObj = DynamicToExpandoObject(doc);
                OnRecordQueued(expObj, metrics);
            }
        }

        void RetryBatch(List<dynamic> batch)
        {
            if (!DataProvider.CanReschedule)
            {
                return;
            }

            if (NumberOfRetries > 1)
            {
                List<dynamic> batchToReschedule = new List<dynamic>();
                foreach (dynamic doc in batch)
                {
                    int retries = 1;
                    lock (_syncTracker)
                    {
                        if (_docsTracker.ContainsKey(doc.id))
                        {
                            retries = _docsTracker[doc.id].NumberOfRetries;
                        }
                        else
                        {
                            continue;
                        }
                    }

                    if (retries >= NumberOfRetries)
                    {
                        OnErrorOccurred(new WorkerException(string.Format("Number of allowed retries for {0} document has been reached.", doc.id)));
                        continue;
                    }

                    lock (_syncTracker)
                    {
                        _docsTracker[doc.id].NumberOfRetries++;
                    }

                    ExpandoObject expObj = DynamicToExpandoObject(doc);
                    batchToReschedule.Add(expObj);
                }

                bool status = false;
                lock (_syncProvider)
                {
                    try
                    {
                        status = DataProvider.RescheduleBatch(batchToReschedule);
                    }
                    catch (Exception ex)
                    {
                        OnErrorOccurred(ex);
                    }
                }

                if (!status)
                {
                    foreach (dynamic doc in batch)
                    {
                        lock (_syncTracker)
                        {
                            _docsTracker.Remove(doc.id);
                        }
                    }
                }
            }
        }

        int QueueBatch(List<dynamic> batch)
        {
            Stopwatch watcher = new Stopwatch();

            int status = -1;
            try
            {
                watcher.Start();
                status = _engine.QueueBatchOfDocuments(batch, _configId);
                watcher.Stop();
            }
            catch (Exception ex)
            {
                SemantriaWorkerException workerException = new SemantriaWorkerException("An exception occured during document queuing.", ex);
                OnErrorOccurred(workerException);
            }

            if (status != -1)
            {
                OnMethodExecuted(new RequestMetrics(SemantriaAPImethod.QueueBatch, watcher.Elapsed, batch.Count));

                _dataRetrievingWorkers.RemoveAll(worker => !worker.IsAlive);
                if (_dataRetrievingWorkers.Count < NumberOfRetrievalThreads)
                {
                    for (int i = _dataRetrievingWorkers.Count; i < NumberOfRetrievalThreads; i++)
                    {
                        Thread worker = new Thread(DataRetrievingWorker);
                        _dataRetrievingWorkers.Add(worker);
                        worker.Start();
                    }
                }

                lock (_syncTracker)
                {
                    _docsInProgress += batch.Count;
                }
            }

            return status;
        }

        #endregion
    }
}
