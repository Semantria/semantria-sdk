#ifndef CALLBACKHANDLER_H
#define CALLBACKHANDLER_H

#include <vector>

#include "../common/ResponseArgs.h"
#include "../common/RequestArgs.h"
#include "../objects/output/DocAnalyticData.h"
#include "../objects/output/CollAnalyticData.h"

class CallbackHandler {
public:
    CallbackHandler();
    virtual ~CallbackHandler();

    virtual void OnResponse(void* sender, ResponseArgs* responseArgs) = 0;
    virtual void OnRequest(void* sender, RequestArgs* requestArgs) = 0;
    virtual void OnError(void* sender, ResponseArgs* responseArgs) = 0;
    virtual void OnDocsAutoResponse(void* sender, std::vector<DocAnalyticData*>* processedData) = 0;
    virtual void OnCollsAutoResponse(void* sender, std::vector<CollAnalyticData*>* processedData) = 0;

};

#endif // CALLBACKHANDLER_H
