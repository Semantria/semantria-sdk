#ifndef TESTAPP_H
#define TESTAPP_H

#include <fstream>
#include <string>
#include <iostream>
#include <sstream>
//#include <dos.h>
#include <time.h>
#include <cstdlib>

#include "../src/Session.h"
#include "../src/handlers/SimpleCallbackHandler.h"

class DiscoveryTestApp {
public:
    DiscoveryTestApp(std::string key, std::string password);
    virtual ~DiscoveryTestApp();

    void run();

private:
    DiscoveryTestApp() {}
    void queueCollection();
    void receiveAnalyticData();
    void outputResults();
    void sleep(unsigned milliseconds);
    string intToStr(int value);

    Session* session;
    vector<string>* initialText;
    vector<CollAnalyticData*>* analyticData;
    static const int TIMEOUT_BEFORE_GETTING_RESPONSE;
};

#endif // TESTAPP_H
