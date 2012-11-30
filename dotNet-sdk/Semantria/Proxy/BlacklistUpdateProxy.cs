using System;
using System.Collections.Generic;
using System.Text;
using Semantria.Com.Mapping.Configuration.Stub;

namespace Semantria.Com.Proxy
{
    public sealed class BlacklistUpdateProxy : IUpdateProxy<string>
    {
        #region Constructor

        private BlacklistUpdateProxy()
        {
        }

        internal static IUpdateProxy<string> CreateInstance()
        {
            return new BlacklistUpdateProxy();
        }

        #endregion Constructor

        #region Properties

        private static string REMOVE_ALL_ITEMS_MARK = "*";
        private BlacklistManagable _managable = new BlacklistManagable();
        internal BlacklistManagable Managable
        {
            get { return _managable; }
            set { _managable = value; }
        }

        #endregion Properties

        #region IUpdateProxy<Blacklist> Members

        object IUpdateProxy<string>.Stub
        {
            get { return _managable; }
        }

        void IUpdateProxy<string>.Add(string obj)
        {
            _managable.Added.Add(obj);
        }

        void IUpdateProxy<string>.Remove(string obj)
        {
            _managable.Removed.Add(obj);
        }

        void IUpdateProxy<string>.Edit(string obj)
        {
            _managable.Added.Add(obj);
        }

        void IUpdateProxy<string>.Clone(string obj){}

        void IUpdateProxy<string>.RemoveAll()
        {
            _managable.Added.Clear();
            _managable.Removed.Clear();
            _managable.Removed.Add(REMOVE_ALL_ITEMS_MARK);
        }

        #endregion
    }
}
