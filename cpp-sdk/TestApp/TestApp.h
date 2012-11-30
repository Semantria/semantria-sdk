#ifndef TESTAPP_H
#define TESTAPP_H

#include <string>
#include <iostream>
#include <sstream>
#include <dos.h>
#include <time.h>
#include <cstdlib>

#include "../src/Session.h"
#include "../src/handlers/SimpleCallbackHandler.h"

class TestApp {
public:
    TestApp(std::string key, std::string password);
    virtual ~TestApp();

    void run();

private:
    TestApp() {}
    void queueDocuments();
    void receiveAnalyticData();
    void outputResults();
    void sleep(unsigned milliseconds);
    string intToStr(int value);

    Session* session;
    vector<string>* initialText;
    vector<DocAnalyticData*>* analyticData;
    static const int TIMEOUT_BEFORE_GETTING_RESPONSE;
};

#endif // TESTAPP_H
