using System.Collections.Generic;

namespace Semantria.Com.Workers
{
    /// <summary>
    /// IDataProvider interface that defines a signature for data provider classes.
    /// </summary>
    public interface IDataProvider
    {
        /// <summary>
        /// Determines whether the data provider has more data or not.
        /// </summary>
        bool HasData
        {
            get;
        }

        /// <summary>
        /// Determines whether the data provider supports rescheduling of failed records.
        /// </summary>
        bool CanReschedule
        {
            get;
        }

        /// <summary>
        /// Determines whether the data provider supports batched mode.
        /// </summary>
        bool SupportBatches
        {
            get;
        }

        /// <summary>
        /// Defines characters limit per document supported by the Worker.
        /// </summary>
        int CharactersLimit
        {
            get;
            set;
        }

        /// <summary>
        /// Initializes the data provider.
        /// </summary>
        /// <param name="args">Optional parameters required for worker initialization.</param>
        /// <returns>Returns worker initialization state. false if the worker was not initialized and can't be used.</returns>
        bool Initialize(params object[] args);

        /// <summary>
        /// Reads the next record if exists.
        /// </summary>
        /// <returns>Returns record for analysis or null if the record doesn't exist.</returns>
        dynamic ReadNext();

        /// <summary>
        /// Reads the next batch of records if supported.
        /// </summary>
        /// <param name="size">The number of records to read.</param>
        /// <returns>Returns the batch of records or null if no records are available.</returns>
        List<dynamic> ReadBatch(int size);

        /// <summary>
        /// Reschedules a record for retry if supported.
        /// </summary>
        /// <param name="record">A failed record to reschedule.</param>
        /// <returns>Returns status of opperation. false if a record was not rescheduled.</returns>
        bool Reschedule(dynamic record);

        /// <summary>
        /// Reschedules a batch of records for retry if supported.
        /// </summary>
        /// <param name="batch">A batch of records to reschedule.</param>
        /// <returns>Returns status of opperation. false if a batch was not rescheduled.</returns>
        bool RescheduleBatch(List<dynamic> batch);
    }
}
