using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Runtime.Serialization.Json;
using System.Text;
using System.IO;

namespace Semantria.Com.Serializers
{
    public sealed class JsonSerializer : ISerializer
    {
        #region ISerializer Members

        public string Type()
        {
            return "json";
        }

        public string Serialize<T>(T obj)
        {
            string result = string.Empty;
            MemoryStream ms = new MemoryStream();
            using (ms)
            {
                DataContractJsonSerializer serializer = new DataContractJsonSerializer(obj.GetType());
                serializer.WriteObject(ms, obj);
                result = Encoding.UTF8.GetString(ms.ToArray());
                ms.Close();
            }
            return result;
        }

        public T Deserialize<T>(string str)
        {
            T obj = default(T);
            MemoryStream ms = new MemoryStream(Encoding.UTF8.GetBytes(str));
            using (ms)
            {
                DataContractJsonSerializer serializer = new DataContractJsonSerializer(typeof(T));
                obj = (T)serializer.ReadObject(ms);
                ms.Close();
            }
            return obj;
        }

        #endregion
    }
}
