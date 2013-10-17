#include "Stub_DocAnalyticDatas.h"

Stub_DocAnalyticDatas::Stub_DocAnalyticDatas() {
    analyticData = new vector<DocAnalyticData*>();
    hierarchy = new vector<string>();
}

Stub_DocAnalyticDatas::~Stub_DocAnalyticDatas() {
    // For removing stub's items use clear() function instead
    docAnalytic = NULL;
    docEntity = NULL;
    docPhrase = NULL;
    docTheme = NULL;
    docTopic = NULL;
    if (NULL != hierarchy) {delete hierarchy;}
}

void Stub_DocAnalyticDatas::Serialize(Json::Value& root) {}

void Stub_DocAnalyticDatas::Deserialize(Json::Value& root) {
    if (NULL == this->analyticData) {
        this->analyticData = new vector<DocAnalyticData*>();
    }

    for ( unsigned int i = 0; i < root.size(); ++i ) {
        DocAnalyticData* data = new DocAnalyticData();
        data->Deserialize(root[i]);
        this->analyticData->push_back(data);
    }
}

void Stub_DocAnalyticDatas::Deserialize(std::string source) {
    xmlSAXUserParseMemory(GetXmlHandler(), this, source.c_str(), int(source.length()));
    xmlCleanupParser();
}

void Stub_DocAnalyticDatas::Serialize(xmlNodePtr root) {}

xmlSAXHandler* Stub_DocAnalyticDatas::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &DocumentXmlHandler::startElement;
    result->endElement = &DocumentXmlHandler::endElement;
    result->characters = &DocumentXmlHandler::characters;

    return result;
}

string Stub_DocAnalyticDatas::GetRootElement() {
    return "document";
}
