#ifndef SIMPLECALLBACKHANDLER_H
#define SIMPLECALLBACKHANDLER_H

#include <iostream>
#include "CallbackHandler.h"

class SimpleCallBackHandler: public CallbackHandler {
public:
    SimpleCallBackHandler();
    virtual ~SimpleCallBackHandler();

    virtual void OnResponse(void* sender, ResponseArgs* responseArgs);
    virtual void OnRequest(void* sender, RequestArgs* requestArgs);
    virtual void OnError(void* sender, ResponseArgs* responseArgs);
    virtual void OnDocsAutoResponse(void* sender, std::vector<DocAnalyticData*>* processedData);
    virtual void OnCollsAutoResponse(void* sender, std::vector<CollAnalyticData*>* processedData);
};

#endif // SIMPLECALLBACKHANDLER_H
