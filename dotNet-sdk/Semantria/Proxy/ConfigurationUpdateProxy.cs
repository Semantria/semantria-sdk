using System;
using System.Collections.Generic;
using System.Text;
using Semantria.Com.Mapping.Configuration;
using Semantria.Com.Mapping.Configuration.Stub;

namespace Semantria.Com.Proxy
{
    public sealed class ConfigurationUpdateProxy : IUpdateProxy<Configuration>
    {
        #region Constructor

        private ConfigurationUpdateProxy()
        {
        }

        internal static IUpdateProxy<Configuration> CreateInstance()
        {
            return new ConfigurationUpdateProxy();
        }

        #endregion Constructor

        #region Properties

        private static string REMOVE_ALL_ITEMS_MARK = "*";
        private ConfigurationManagable _managable = new ConfigurationManagable();
        internal ConfigurationManagable Managable 
        {
            get { return _managable; }
            set { _managable = value; }
        }

        #endregion Properties

        #region IUpdateProxy<Configuration> Members

        object IUpdateProxy<Configuration>.Stub
        {
            get { return _managable; }
        }

        void IUpdateProxy<Configuration>.Add(Configuration obj)
        {
            _managable.Added.Add(obj);
        }

        void IUpdateProxy<Configuration>.Remove(Configuration obj)
        {
            _managable.Removed.Add(obj.ConfigId);
        }

        void IUpdateProxy<Configuration>.Edit(Configuration obj)
        {
            _managable.Added.Add(obj);
        }

        void IUpdateProxy<Configuration>.Clone(Configuration obj)
        {
            obj.Template = obj.ConfigId;
            obj.ConfigId = null;
            _managable.Added.Add(obj);
        }

        void IUpdateProxy<Configuration>.RemoveAll()
        {
            _managable.Added.Clear();
            _managable.Removed.Clear();
            _managable.Removed.Add(REMOVE_ALL_ITEMS_MARK);
        }

        #endregion
    }
}
