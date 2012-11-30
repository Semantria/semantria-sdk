using System;
using System.Collections.Generic;
using System.Text;
using Semantria.Com.Mapping.Configuration;
using Semantria.Com.Mapping.Configuration.Stub;

namespace Semantria.Com.Proxy
{
    public sealed class SentimentPhrasesUpdateProxy : IUpdateProxy<SentimentPhrase>
    {
        #region Constructor

        private SentimentPhrasesUpdateProxy()
        {
        }

        internal static IUpdateProxy<SentimentPhrase> CreateInstance()
        {
            return new SentimentPhrasesUpdateProxy();
        }

        #endregion Constructor

        #region Properties

        private static string REMOVE_ALL_ITEMS_MARK = "*";
        private SentimentPhraseManagable _managable = new SentimentPhraseManagable();
        internal SentimentPhraseManagable Managable
        {
            get { return _managable; }
            set { _managable = value; }
        }

        #endregion Properties

        #region IUpdateProxy<SemantriaPhrase> Members

        object IUpdateProxy<SentimentPhrase>.Stub
        {
            get { return _managable; }
        }

        void IUpdateProxy<SentimentPhrase>.Add(SentimentPhrase obj)
        {
            _managable.Added.Add(obj);
        }

        void IUpdateProxy<SentimentPhrase>.Remove(SentimentPhrase obj)
        {
            _managable.Removed.Add(obj.Title);
        }

        void IUpdateProxy<SentimentPhrase>.Edit(SentimentPhrase obj)
        {
            _managable.Added.Add(obj);
        }

        void IUpdateProxy<SentimentPhrase>.Clone(SentimentPhrase obj) { }

        void IUpdateProxy<SentimentPhrase>.RemoveAll()
        {
            _managable.Added.Clear();
            _managable.Removed.Clear();
            _managable.Removed.Add(REMOVE_ALL_ITEMS_MARK);
        }

        #endregion
    }
}

