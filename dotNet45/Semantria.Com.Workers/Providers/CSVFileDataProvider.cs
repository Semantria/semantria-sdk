using System;
using System.IO;
using System.Dynamic;
using System.Collections.Generic;

using Semantria.Com.CSV;

namespace Semantria.Com.Workers
{
    public class CSVFileDataProvider : IDataProvider
    {
        #region Members

        bool _hasData = false;
        string _filePath = string.Empty;

        CsvFileReader _reader = null;

        #endregion

        #region Static members

        static object _syncCallback = new object();

        #endregion

        #region Constructors

        public CSVFileDataProvider(string filePath)
        {
            Delimiter = ',';

            IdColumnIndex = uint.MaxValue;
            TextColumnIndex = uint.MaxValue;
            TagColumnIndex = uint.MaxValue;

            _filePath = filePath;
        }

        #endregion

        #region Properties

        public char Delimiter
        {
            get;
            set;
        }

        public uint IdColumnIndex
        {
            get;
            set;
        }

        public uint TextColumnIndex
        {
            get;
            set;
        }

        public uint TagColumnIndex
        {
            get;
            set;
        }

        #endregion

        #region IDataProvider implementation

        public bool HasData
        {
            get
            {
                return _hasData;
            }
        }

        public bool CanReschedule
        {
            get
            {
                return false;
            }
        }

        public bool SupportBatches
        {
            get
            {
                return true;
            }
        }

        public int CharactersLimit
        {
            get;
            set;
        }

        public bool Initialize(params object[] args)
        {
            if (IdColumnIndex == uint.MaxValue || TextColumnIndex == uint.MaxValue)
            {
                throw new DataProviderException("IdColumnIndex or TextColumnIndex properties are not defined!");
            }

            if (File.Exists(_filePath))
            {
                _reader = new CsvFileReader(_filePath, EmptyLineBehavior.Ignore);
                _reader.Delimiter = Delimiter;
                _hasData = _reader.ReadRow(new List<string>());

                return true;
            }

            return false;
        }

        public List<dynamic> ReadBatch(int size)
        {
            if (_reader == null)
            {
                return null;
            }

            List<dynamic> batch = new List<dynamic>(size);

            for (int i = 0; i < size; i++)
            {
                dynamic document = ReadNext();
                if (document == null)
                {
                    return batch;
                }

                batch.Add(document);
            }

            return batch;
        }

        public dynamic ReadNext()
        {
            if (_reader == null)
            {
                return null;
            }

            List<string> row = new List<string>();
            bool hasMore = _reader.ReadRow(row);
            if (!hasMore)
            {
                _hasData = false;
                return null;
            }

            if (row.Count == 0 || row[(int)TextColumnIndex].Length >= CharactersLimit)
            {
                return ReadNext();
            }

            dynamic document = new ExpandoObject();
            document.id = row[(int)IdColumnIndex];
            document.text = row[(int)TextColumnIndex];

            if (TagColumnIndex != uint.MaxValue)
            {
                document.tag = row[(int)TagColumnIndex];
            }

            return document;
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
    }
}
