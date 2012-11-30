using System;
using System.Collections.Generic;
using System.Text;
using Semantria.Com.Mapping.Configuration;
using Semantria.Com.Mapping.Configuration.Stub;

namespace Semantria.Com.Proxy
{
    public sealed class CategoryUpdateProxy : IUpdateProxy<Category>
    {
        #region Constructor

        private CategoryUpdateProxy()
        {
        }

        internal static IUpdateProxy<Category> CreateInstance()
        {
            return new CategoryUpdateProxy();
        }

        #endregion Constructor

        #region Properties

        private static string REMOVE_ALL_ITEMS_MARK = "*";
        private CategoryManagable _managable = new CategoryManagable();
        internal CategoryManagable Managable
        {
            get { return _managable; }
            set { _managable = value; }
        }

        #endregion Properties

        #region IUpdateProxy<Category> Members

        object IUpdateProxy<Category>.Stub
        {
            get { return _managable; }
        }

        void IUpdateProxy<Category>.Add(Category obj)
        {
            _managable.Added.Add(obj);
        }

        void IUpdateProxy<Category>.Remove(Category obj)
        {
            _managable.Removed.Add(obj.Name);
        }

        void IUpdateProxy<Category>.Edit(Category obj)
        {
            _managable.Added.Add(obj);
        }

        void IUpdateProxy<Category>.Clone(Category obj) {}

        void IUpdateProxy<Category>.RemoveAll()
        {
            _managable.Added.Clear();
            _managable.Removed.Clear();
            _managable.Removed.Add(REMOVE_ALL_ITEMS_MARK);
        }

        #endregion
    }
}
