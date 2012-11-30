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

    internal interface IManagable<T,U>
    {
        List<T> Added { get; set; }
        List<U> Removed { get; set; }
    }

    public interface IUpdateProxy<T>
    {
        void Add(T obj);
        void Remove(T obj);
        void Edit(T obj);
        void Clone(T obj);
        void RemoveAll();
        
        object Stub { get; }
    }
}