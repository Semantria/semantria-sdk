using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Web.Script.Serialization;

namespace Semantria.Com.Serializers.Data.Json
{
    public sealed class DynamicJsonObjectConverter : JavaScriptConverter
    {
        ReadOnlyCollection<Type> _supportedTypes = new ReadOnlyCollection<Type>(new List<Type>()
        {
            typeof(DynamicJsonObject)
        });

        public override object Deserialize(IDictionary<string, object> dictionary, Type type, JavaScriptSerializer serializer)
        {
            if (dictionary == null)
                throw new ArgumentNullException("dictionary");

            if (!_supportedTypes.Contains(type))
                throw new NotSupportedException(type.ToString());

            return Activator.CreateInstance(type, new object[] { dictionary });
        }

        public override IDictionary<string, object> Serialize(object obj, JavaScriptSerializer serializer)
        {
            var data = obj as DynamicJsonObject;
            return data.getData();
        }

        public override IEnumerable<Type> SupportedTypes
        {
            get
            {
                return _supportedTypes;
            }
        }
    }
}
