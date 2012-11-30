#include "TestApp.h"

TestApp::TestApp(std::string key, std::string secret) {
    // Initializes new session with the serializer object and the keys.
    session = Session::CreateSession(key, secret);

    // Callback handler also could be setted
    //session->SetCallbackHandler(new SimpleCallBackHandler());

    // Initializes random generator
    srand(time(NULL));
    analyticData = NULL;

    // Initial texts for processing
    initialText = new vector<string>();
    initialText->push_back("Lisa - there's 2 Skinny cow coupons available $5 skinny cow ice cream coupons on special k boxes and Printable FPC from facebook - a teeny tiny cup of ice cream. I printed off 2 (1 from my account and 1 from dh's). I couldn't find them instore and i'm not going to walmart before the 19th. Oh well sounds like i'm not missing much ...lol");
    initialText->push_back("In Lake Louise - a ₤ guided walk for the family with Great Divide Nature Tours  rent a canoe on Lake Louise or Moraine Lake  go for a hike to the Lake Agnes Tea House. In between Lake Louise and Banff - visit Marble Canyon or Johnson Canyon or both for family friendly short walks. In Banff  a picnic at Johnson Lake  rent a boat at Lake Minnewanka  hike up Tunnel Mountain  walk to the Bow Falls and the Fairmont Banff Springs Hotel  visit the Banff Park Museum. The \"must-do\" in Banff is a visit to the Banff Gondola and some time spent on Banff Avenue - think candy shops and ice cream.");
    initialText->push_back("On this day in 1786 - In New York City  commercial ice cream was manufactured for the first time.");

}

TestApp::~TestApp() {
    delete session;
    delete initialText;
    if (NULL != analyticData) {
        delete analyticData;
    }
}

#ifdef _WIN32
    #include <windows.h>
    void TestApp::sleep(unsigned milliseconds) {
        Sleep(milliseconds);
    }
#else
    #include <unistd.h>
    void TestApp::sleep(unsigned milliseconds) {
        usleep(milliseconds * 1000); // takes microseconds
    }
#endif

void TestApp::run() {
    cout << "Semantria service demo." << endl;
    queueDocuments();
    // As Semantria isn't real-time solution you need to wait some time before getting of the processed results
    // In real application here can be implemented two separate jobs, one for queuing of source data another one for retreiving
    // Wait ten seconds while Semantria process queued document
    sleep(TIMEOUT_BEFORE_GETTING_RESPONSE);
    receiveAnalyticData();
    outputResults();
}

void TestApp::queueDocuments() {
    for(vector<string>::size_type i = 0; i != initialText->size(); i++) {
        // Generating some UID
        string uid = "TASK-" + intToStr(rand() % 100000000 + 10000);
        if (202 >= session->QueueDocument(new Document(uid, initialText->at(i)))) {
            cout << "\"" << uid << "\" document queued succsessfully." << endl;
        } else {
            cout << "Something went wrong." << endl;
        }
    }
}

void TestApp::receiveAnalyticData() {
    analyticData = new vector<DocAnalyticData*>;

    while (analyticData->size() < initialText->size()) {
		cout << "Retrieving your processed results..." << endl;
        // Requests processed results from Semantria service
        vector<DocAnalyticData*>* temp = session->GetProcessedDocuments();
        analyticData->insert(analyticData->end(), temp->begin(), temp->end());

        if (analyticData->size() >= initialText->size()) {
            return;
        }

        sleep(TIMEOUT_BEFORE_GETTING_RESPONSE);
    }
}

void TestApp::outputResults() {
    // Output results
    for (vector<DocAnalyticData*>::size_type i = 0; i != analyticData->size(); i++) {
        DocAnalyticData* docAnalyticData = analyticData->at(i);
        cout << endl << "Document " << docAnalyticData->GetId() << ". Sentiment score: "
             << docAnalyticData->GetSentimentScore() << endl;

        vector<DocTheme*>* themes = docAnalyticData->GetThemes();
        if (NULL != themes && themes->size() > 0) {
            cout << "Document themes:" << endl;
            for (vector<DocTheme*>::size_type i = 0; i != themes->size(); i++) {
                cout << themes->at(i)->GetTitle() << " (sentiment: "
                     << themes->at(i)->GetSentimentScore() << ")" << endl;
            }
        } else {
            cout << "No themes were extracted for this text" << endl;
        }

        vector<DocEntity*>* entities = docAnalyticData->GetEntities();
        if (NULL != entities && entities->size() > 0) {
            cout << "Entities:" << endl;
            for (vector<DocEntity*>::size_type i = 0; i != entities->size(); i++) {
                cout << entities->at(i)->GetTitle() << " (sentiment: "
                     << entities->at(i)->GetSentimentScore() << ")" << endl;
            }
        } else {
            cout << "No entities were extracted for this text" << endl;
        }
    }
}

string TestApp::intToStr(int value) {
    std::stringstream stream;
    stream << value;
    return stream.str();
}

const int TestApp::TIMEOUT_BEFORE_GETTING_RESPONSE = 5000; //in milliseconds
