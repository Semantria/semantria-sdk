using System;
using System.Web;
using System.Text;
using System.Dynamic;
using System.Collections;
using System.Collections.Generic;

namespace Semantria.Com.Serializers.Data.Json
{
    public class DynamicJsonObject : DynamicObject, IList, IEnumerator
    {
        public IDictionary<string, object> getData()
        {
            return _rawData;
        }

        #region Private members

        readonly IDictionary<string, object> _rawData;
        int index = 0;
        IEnumerator<KeyValuePair<string, object>> _rawDataEnumerator = null;

        private dynamic getDynamicJsonObject(dynamic data)
        {
            if (data is IDictionary<string, object> && ((IDictionary<string, object>)data).Count > 0)
                data = new DynamicJsonObject((IDictionary<string, object>)data);
            else if (data is IList && ((IList)data).Count > 0)
            {
                IList iterate = data as IList;
                IDictionary<string, object> res = new Dictionary<string, object>(iterate.Count);
                for (int i = 0; i < iterate.Count; i++)
                    res.Add(i.ToString(), iterate[i]);

                data = new DynamicJsonObject((IDictionary<string, object>)res);
            }
            return data;
        }

        #endregion

        #region Constructor

        public DynamicJsonObject()
        {
            _rawData = new Dictionary<string, object>();
        }

        public DynamicJsonObject(IDictionary<string, object> dictionary)
        {
            if (dictionary == null)
                throw new ArgumentNullException("dictionary");

            _rawData = dictionary;
        }

        #endregion

        #region Overrides

        public override bool TryGetMember(GetMemberBinder binder, out object result)
        {
            result = null;

            if (_rawData.Count > 0)
            {
                if (!_rawData.TryGetValue(binder.Name, out result))
                    return true;

                result = getDynamicJsonObject(result);
            }

            return true;
        }

        public override bool TrySetMember(SetMemberBinder binder, object value)
        {
            if (_rawData.Count > 0)
            {
                if (_rawData.ContainsKey(binder.Name))
                    _rawData[binder.Name] = value;
                else
                    _rawData.Add(binder.Name, value);

                return true;
            }

            return false;
        }

        public override bool TryGetIndex(GetIndexBinder binder, object[] indexes, out object result)
        {
            result = null;

            if (indexes != null && indexes.Length > 0 && indexes[0] != null)
            {
                if (typeof(int) == indexes[0].GetType())
                {
                    int index = (((int)indexes[0]) >= 0) ? ((int)indexes[0]) : 0;
                    List<object> values = new List<object>(_rawData.Values);
                    result = values[index];
                }
                else if (typeof(string) == indexes[0].GetType())
                {
                    string key = indexes[0] as string;
                    if (_rawData.ContainsKey(key))
                        result = _rawData[key];
                }
            }

            if (result != null)
            {
                result = getDynamicJsonObject(result);
                return true;
            }

            return true;
        }

        public override bool TrySetIndex(SetIndexBinder binder, object[] indexes, object value)
        {
            throw new NotImplementedException();
        }

       
        public override string ToString()
        {
            StringBuilder sb = new StringBuilder();

            sb.Append("{");
            bool firstInDictionary = true;
            foreach (KeyValuePair<string, object> pair in _rawData)
            {
                if (!firstInDictionary)
                    sb.Append(",");
                firstInDictionary = false;

                if (pair.Value is string)
                    sb.AppendFormat("\"{0}\":\"{1}\"", pair.Key, HttpUtility.JavaScriptStringEncode((string)pair.Value));
                else if (pair.Value is IDictionary<string, object>)
                {
                    sb.AppendFormat("\"{0}\":", pair.Key);
                    sb.Append(new DynamicJsonObject((IDictionary<string, object>)pair.Value).ToString());
                }
                else if (pair.Value is IList)
                {
                    sb.AppendFormat("\"{0}\":[", pair.Key);

                    bool firstInArray = true;
                    foreach (object item in (IList)pair.Value)
                    {
                        if (!firstInArray)
                            sb.Append(",");
                        firstInArray = false;

                        if (item is IDictionary<string, object>)
                            sb.Append(new DynamicJsonObject((IDictionary<string, object>)item).ToString());
                        else if (item is string)
                            sb.AppendFormat("\"{0}\"", HttpUtility.JavaScriptStringEncode((string)item));
                        else
                            sb.AppendFormat("{0}", item);
                    }
                    sb.Append("]");
                }
                else if (pair.Value is Boolean)
                    sb.AppendFormat("\"{0}\":{1}", pair.Key, pair.Value.ToString().ToLower());
                else
                {
                    if (pair.Value != null)
                        sb.AppendFormat("\"{0}\":{1}", pair.Key, pair.Value);
                    else
                        sb.AppendFormat("\"{0}\":null", pair.Key);
                }
            }
            sb.Append("}");

            return sb.ToString();
        }

        public override IEnumerable<string> GetDynamicMemberNames()
        {
            if (_rawData.Count > 0)
                return _rawData.Keys;

            return new List<string>() { string.Empty };
        }

        #endregion

        #region Public methods

        public string DecodeFromUtf8(string utf8String)
        {
            byte[] utf8Bytes = new byte[utf8String.Length];
            for (int i = 0; i < utf8String.Length; ++i)
            {
                utf8Bytes[i] = (byte)utf8String[i];
            }
            return Encoding.UTF8.GetString(utf8Bytes, 0, utf8Bytes.Length);
        }

        public void DeleteMember(string name)
        {
            _rawData.Remove(name);
        }

        #endregion

        #region IList members

        public int Add(object value)
        {
            _rawData.Add(index.ToString(), value);
            return index++;
        }

        public void Clear()
        {
            throw new NotImplementedException();
        }

        public bool Contains(object value)
        {
            throw new NotImplementedException();
        }

        public int IndexOf(object value)
        {
            throw new NotImplementedException();
        }

        public void Insert(int index, object value)
        {
            throw new NotImplementedException();
        }

        public bool IsFixedSize
        {
            get { throw new NotImplementedException(); }
        }

        public bool IsReadOnly
        {
            get { throw new NotImplementedException(); }
        }

        public void Remove(object value)
        {
            throw new NotImplementedException();
        }

        public void RemoveAt(int index)
        {
            throw new NotImplementedException();
        }

        public object this[int index]
        {
            get
            {
                var result = _rawData[index.ToString()];

                if (result != null)
                {
                    return getDynamicJsonObject(result);
                }
                return null;

            }
            set
            {
                throw new NotImplementedException();
            }
        }

        public void CopyTo(Array array, int index)
        {
            throw new NotImplementedException();
        }

        public int Count
        {
            get { return _rawData.Count; }
        }

        public bool IsSynchronized
        {
            get { throw new NotImplementedException(); }
        }

        public object SyncRoot
        {
            get { throw new NotImplementedException(); }
        }

        public IEnumerator GetEnumerator()
        {
            _rawDataEnumerator = _rawData.GetEnumerator();
            return this;
        }

        #endregion

        #region IEnumerator members

        public object Current
        {
            get
            {
                var current = _rawDataEnumerator.Current;
                var result = current.Value;
                if (result != null)
                {
                    result = getDynamicJsonObject(result);
                    int index;
                    if (!int.TryParse(current.Key, out index))
                    {
                        var res = new Dictionary<string, object>();
                        res.Add(current.Key, result);
                        result = new DynamicJsonObject((IDictionary<string, object>)res);
                    }
                    return result;
                }
                return null;
            }
        }

        public bool MoveNext()
        {
            return _rawDataEnumerator.MoveNext();
        }

        public void Reset()
        {
            _rawDataEnumerator.Reset();
        }

        #endregion
    }

}
