using System;
using System.Dynamic;
using System.IO;
using System.Collections.Generic;
using System.Linq;

namespace Semantria.Com.Workers
{
    public class FileSystemDataProvider : IDataProvider
    {
        #region Members

        string _dirPath = string.Empty;
        string _fileMask = "*.*";

        IEnumerator<FileInfo> _filesEnumerator = null;

        #endregion

        #region Constructors

        public FileSystemDataProvider(string dirPath)
        {
            _dirPath = dirPath;

            CharactersLimit = 8192;
        }

        public FileSystemDataProvider(string dirPath, string fileMask) :
            this(dirPath)
        {
            _fileMask = fileMask;
        }

        #endregion

        #region IDataProvider implementation

        public bool HasData
        {
            get
            {
                if (_filesEnumerator != null)
                {
                    return _filesEnumerator.Current != null;
                }
                else
                {
                    return false;
                }
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
            if (Directory.Exists(_dirPath))
            {
                DirectoryInfo dirInfo = new DirectoryInfo(_dirPath);
                List<FileInfo> files = dirInfo.GetFiles(_fileMask).ToList();

                if (files.Count > 0)
                {
                    _filesEnumerator = files.GetEnumerator();
                    _filesEnumerator.MoveNext();

                    return true;
                }
            }

            return false;
        }

        public List<dynamic> ReadBatch(int size)
        {
            List<dynamic> batch = new List<dynamic>(size);

            for (int index = 0; index < size; index++)
            {
                dynamic document = ReadNext();

                if (document == null)
                {
                    break;
                }

                batch.Add(document);
            }

            return batch;
        }

        public dynamic ReadNext()
        {
            if (_filesEnumerator.Current == null)
            {
                return null;
            }

            using (TextReader reader = File.OpenText(_filesEnumerator.Current.FullName))
            {
                string content = reader.ReadToEnd();
                if (content.Length > CharactersLimit)
                {
                    _filesEnumerator.MoveNext();
                    return ReadNext();
                }

                dynamic document = new ExpandoObject();
                document.id = _filesEnumerator.Current.Name;
                document.text = content;

                _filesEnumerator.MoveNext();
                return document;
            }
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
