using System.Collections.Generic;

namespace Semantria.Com
{
    public interface ISerializer
    {
        string Type();
        string Serialize<T>(T obj);
        T Deserialize<T>(string json);
        dynamic Deserialize(string json);
        string Serialize(dynamic obj);
    }

    internal interface IStub<T>
    {
        List<T> Data { get; set; }
        List<string> Keys { get; }
        void ToKeys();
        string ConvertTag(string data);
    }
}