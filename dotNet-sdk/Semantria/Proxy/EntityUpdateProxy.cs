using System;
using System.Collections.Generic;
using System.Text;
using Semantria.Com.Mapping.Configuration;
using Semantria.Com.Mapping.Configuration.Stub;

namespace Semantria.Com.Proxy
{
    public sealed class EntityUpdateProxy : IUpdateProxy<UserEntity>
    {
        #region Constructor

        private EntityUpdateProxy()
        {
        }

        internal static IUpdateProxy<UserEntity> CreateInstance()
        {
            return new EntityUpdateProxy();
        }

        #endregion Constructor

        #region Properties

        private static string REMOVE_ALL_ITEMS_MARK = "*";
        private EntityManagable _managable = new EntityManagable();
        internal EntityManagable Managable
        {
            get { return _managable; }
            set { _managable = value; }
        }

        #endregion Properties

        #region IUpdateProxy<Entity> Members

        object IUpdateProxy<UserEntity>.Stub
        {
            get { return _managable; }
        }

        void IUpdateProxy<UserEntity>.Add(UserEntity obj)
        {
            _managable.Added.Add(obj);
        }

        void IUpdateProxy<UserEntity>.Remove(UserEntity obj)
        {
            _managable.Removed.Add(obj.Name);
        }

        void IUpdateProxy<UserEntity>.Edit(UserEntity obj)
        {
            _managable.Added.Add(obj);
        }

        void IUpdateProxy<UserEntity>.Clone(UserEntity obj) {}

        void IUpdateProxy<UserEntity>.RemoveAll()
        {
            _managable.Added.Clear();
            _managable.Removed.Clear();
            _managable.Removed.Add(REMOVE_ALL_ITEMS_MARK);
        }

        #endregion
    }
}