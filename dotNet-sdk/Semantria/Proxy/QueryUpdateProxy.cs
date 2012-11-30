using System;
using System.Collections.Generic;
using System.Text;
using Semantria.Com.Mapping.Configuration;
using Semantria.Com.Mapping.Configuration.Stub;

namespace Semantria.Com.Proxy
{
    public sealed class QueryUpdateProxy : IUpdateProxy<Query>
    {
        #region Constructor

        private QueryUpdateProxy()
        {
        }

        internal static IUpdateProxy<Query> CreateInstance()
        {
            return new QueryUpdateProxy();
        }

        #endregion Constructor

        #region Properties

        private static string REMOVE_ALL_ITEMS_MARK = "*";
        private QueryManagable _managable = new QueryManagable();
        internal QueryManagable Managable
        {
            get { return _managable; }
            set { _managable = value; }
        }

        #endregion Properties

        #region IUpdateProxy<Query> Members 

        object IUpdateProxy<Query>.Stub
        {
            get { return _managable; }
        }

        void IUpdateProxy<Query>.Add(Query obj)
        {
            _managable.Added.Add(obj);
        }

        void IUpdateProxy<Query>.Remove(Query obj)
        {
            _managable.Removed.Add(obj.Name);
        }

        void IUpdateProxy<Query>.Edit(Query obj)
        {
            _managable.Added.Add(obj);
        }

        void IUpdateProxy<Query>.Clone(Query obj) { }

        void IUpdateProxy<Query>.RemoveAll()
        {
            _managable.Added.Clear();
            _managable.Removed.Clear();
            _managable.Removed.Add(REMOVE_ALL_ITEMS_MARK);
        }

        #endregion
    }
}
