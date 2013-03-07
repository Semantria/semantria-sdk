using System;
using System.Collections.Generic;
using System.Runtime.Serialization;
using System.Text;
using System.IO;
using System.Xml;
using System.Xml.Serialization;

namespace Semantria.Com.Serializers
{
    public sealed class XmlSerializer : ISerializer
    {
        #region ISerializer Members

        public string Type()
        {
            return "xml";
        }

        public string Serialize<T>(T obj)
        {
            string result = string.Empty;
            MemoryStream ms = new MemoryStream();
            using (ms)
            {
                System.Xml.Serialization.XmlSerializer serializer = new System.Xml.Serialization.XmlSerializer(obj.GetType());
                serializer.Serialize(ms, obj);
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
                System.Xml.Serialization.XmlSerializer serializer = new System.Xml.Serialization.XmlSerializer(typeof(T));
                obj = (T)serializer.Deserialize(ms); 
                ms.Close();
            }
            return obj;
        }

        #endregion
    }
}
