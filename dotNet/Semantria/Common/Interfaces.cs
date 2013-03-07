using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Semantria.Com
{
    public interface ISerializer
    {
        string Type();
        string Serialize<T>(T obj);
        T Deserialize<T>(string json);
    }

    internal interface IStub<T>
    {
        List<T> Data { get; set; }
        List<string> Keys { get; }
        void ToKeys();
        string ConvertTag(string data);
    }
}