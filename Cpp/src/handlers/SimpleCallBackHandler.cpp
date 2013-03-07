#include "SimpleCallBackHandler.h"

SimpleCallBackHandler::SimpleCallBackHandler() {}
SimpleCallBackHandler::~SimpleCallBackHandler() {}

void SimpleCallBackHandler::OnResponse(void* sender, ResponseArgs* responseArgs) {
    cout << endl << "OnResponse: ";
    cout << endl << "Status: " << responseArgs->GetSatatus();
    cout << endl << "Message: " << responseArgs->GetMessage();
}

void SimpleCallBackHandler::OnRequest(void* sender, RequestArgs* requestArgs) {
    cout << endl << "OnRequest: ";
    cout << endl << "Method: " << requestArgs->GetMethod();
    cout << endl << "Url: " << requestArgs->GetUrl();
    cout << endl << "Message: " << requestArgs->GetMessage();
}

void SimpleCallBackHandler::OnError(void* sender, ResponseArgs* responseArgs) {
    cout << endl << "OnError: ";
    cout << endl << "Status: " << responseArgs->GetSatatus();
    cout << endl << "Message: " << responseArgs->GetMessage();
}

void SimpleCallBackHandler::OnDocsAutoResponse(void* sender, std::vector<DocAnalyticData*>* processedData) {
    cout << endl << "OnDocsAutoResponse";
}

void SimpleCallBackHandler::OnCollsAutoResponse(void* sender, std::vector<CollAnalyticData*>* processedData) {
    cout << endl << "OnCollsAutoResponse";
}
