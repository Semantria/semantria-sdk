using System.Runtime.Serialization.Json;
using System.Text;
using System.IO;

using System.Web.Script.Serialization;
using Semantria.Com.Serializers.Data.Json;

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
            return serialize(obj);
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

        public dynamic Deserialize(string str)
        {
            JavaScriptSerializer serializer = new JavaScriptSerializer();
            serializer.RegisterConverters(new[] { new DynamicJsonObjectConverter() });
            dynamic obj = serializer.Deserialize(str, typeof(DynamicJsonObject));
            return obj;
        }

        public string Serialize(dynamic obj)
        {
            return serialize(obj);
        }

        #endregion
    
        private string serialize(dynamic obj)
        {
            JavaScriptSerializer serializer = new JavaScriptSerializer();
            serializer.RegisterConverters(new[] { new DynamicJsonObjectConverter() });
            var output = serializer.Serialize(obj);
            return output;
        }
    }
}
