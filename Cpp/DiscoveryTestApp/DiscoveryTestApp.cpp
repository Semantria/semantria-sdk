#include "DiscoveryTestApp.h"

DiscoveryTestApp::DiscoveryTestApp(std::string key, std::string secret) {
    // Initializes new session with the serializer object and the keys.
    session = Session::CreateSession(key, secret);

    // Callback handler also could be setted
    //session->SetCallbackHandler(new SimpleCallBackHandler());

    // Initializes random generator
    srand((unsigned int)time(NULL));
    analyticData = NULL;

    // Initial texts for processing
    initialText = new vector<string>();

    string line;
    ifstream myfile ("source.txt");
    if (myfile.is_open())
    {
       while ( getline (myfile,line) )
       {
            if( line == "" || line.length() < 3)
                continue;

            initialText->push_back(line);
       }
       myfile.close();
    }
    else cout << "Unable to open file";
}

DiscoveryTestApp::~DiscoveryTestApp() {
    delete session;
    delete initialText;
    if (NULL != analyticData) {
        delete analyticData;
    }
}

#ifdef _WIN32
    #include <windows.h>
    void DiscoveryTestApp::sleep(unsigned milliseconds) {
        Sleep(milliseconds);
    }
#else
    #include <unistd.h>
    void DiscoveryTestApp::sleep(unsigned milliseconds) {
        usleep(milliseconds * 1000); // takes microseconds
    }
#endif

void DiscoveryTestApp::run() {
    cout << "Semantria Discovery Mode demo ..." << endl;
    queueCollection();
    //queueDocuments();
    // As Semantria isn't real-time solution you need to wait some time before getting of the processed results
    // In real application here can be implemented two separate jobs, one for queuing of source data another one for retreiving
    // Wait ten seconds while Semantria process queued document
    sleep(TIMEOUT_BEFORE_GETTING_RESPONSE);
    receiveAnalyticData();
    outputResults();
}

void DiscoveryTestApp::queueCollection() {

    Collection *collection = new Collection();
    collection->SetDocuments(initialText);
    string uid = "TASK-" + intToStr(rand() % 100000000 + 10000);
    collection->SetId(uid);

    if (202 >= session->QueueCollection(collection)) {
        cout << "Collection " << collection->GetId() << " queued succsessfully." << endl;
    } else {
        cout << "Something went wrong." << endl;
    }
}

void DiscoveryTestApp::receiveAnalyticData() {
    cout << "Requesting of the processed results..." << endl;

    sleep(TIMEOUT_BEFORE_GETTING_RESPONSE);
    analyticData = new vector<CollAnalyticData*>;
    analyticData = session->GetProcessedCollections();
}

void DiscoveryTestApp::outputResults() {
    // Output results

    for(vector<CollAnalyticData*>::size_type c = 0; c < analyticData->size(); c++)
    {

        CollAnalyticData* result = analyticData->at(c);

        vector<Facet*>* facets = result->GetFacets();

        for(vector<Facet*>::size_type i = 0; i < facets->size(); i++)
        {
            Facet *facet = facets->at(i);
            cout<<facet->GetLabel()<<":"<<facet->GetCount()<<endl;

            if(facet->GetAttributes() == NULL)
                continue;

            for(vector<Attribute*>::size_type j = 0; j < facet->GetAttributes()->size(); j++)
            {
                Attribute *att = facet->GetAttributes()->at(j);
                cout<<"\t"<<att->GetLabel()<<":"<<att->GetCount()<<endl;
            }
        }
    }
}

string DiscoveryTestApp::intToStr(int value) {
    std::stringstream stream;
    stream << value;
    return stream.str();
}

const int DiscoveryTestApp::TIMEOUT_BEFORE_GETTING_RESPONSE = 5000; //in milliseconds
