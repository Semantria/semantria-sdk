using System;
using System.Collections.Generic;

namespace Semantria.Com.Workers
{
    public class WorkersBuilder
    {
        public WorkersBuilder(IDataProvider dataProvider)
        {
            DataProvider = dataProvider;

            SemantriaAPIkey = null;
            SemantriaAPIsecret = null;

            Configuration = null;

            SalienceLicensePath = null;
            SalienceDataDirectory = null;
            SalienceUserDirectory = null;
        }

        public IDataProvider DataProvider
        {
            get;
            private set;
        }

        public string SemantriaAPIkey
        {
            get;
            set;
        }

        public string SemantriaAPIsecret
        {
            get;
            set;
        }

        public string Configuration
        {
            get;
            set;
        }

        public string SalienceLicensePath
        {
            get;
            set;
        }

        public string SalienceDataDirectory
        {
            get;
            set;
        }

        public string SalienceUserDirectory
        {
            get;
            set;
        }

        public IWorker CreateSemantriaWorker()
        {
            if (string.IsNullOrEmpty(SemantriaAPIkey))
            {
                throw new InvalidOperationException("SemantriaAPIkey is nod defined!");
            }
            else if (string.IsNullOrEmpty(SemantriaAPIsecret))
            {
                throw new InvalidOperationException("SemantriaAPIsecret is nod defined!");
            }

            DataProvider.Initialize();

            return new SemantriaWorker(DataProvider, SemantriaAPIkey, SemantriaAPIsecret, Configuration);
        }
    }
}
