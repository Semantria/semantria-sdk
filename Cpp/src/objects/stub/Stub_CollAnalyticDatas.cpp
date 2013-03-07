#include "Stub_CollAnalyticDatas.h"

Stub_CollAnalyticDatas::Stub_CollAnalyticDatas() {
    analyticData = new vector<CollAnalyticData*>();
    hierarchy = new vector<string>();
}

Stub_CollAnalyticDatas::~Stub_CollAnalyticDatas() {
    // For removing stub's items use clear() function instead
    collAnalytic = NULL;
	attribute = NULL;
	facet = NULL;
    collEntity = NULL;
    collTheme = NULL;
    collTopic = NULL;
    if (NULL != hierarchy) {delete hierarchy;}
}

void Stub_CollAnalyticDatas::Serialize(Json::Value& root) {}

void Stub_CollAnalyticDatas::Deserialize(Json::Value& root) {
    if (NULL == this->analyticData) {
        this->analyticData = new vector<CollAnalyticData*>();
    }

    for ( int i = 0; i < root.size(); ++i ) {
        CollAnalyticData* data = new CollAnalyticData();
        data->Deserialize(root[i]);
        this->analyticData->push_back(data);
    }
}

void Stub_CollAnalyticDatas::Deserialize(std::string source) {
    xmlSAXUserParseMemory(GetXmlHandler(), this, source.c_str(), int(source.length()));
    xmlCleanupParser();
}

void Stub_CollAnalyticDatas::Serialize(xmlNodePtr root) {}

xmlSAXHandler* Stub_CollAnalyticDatas::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &CollectionXmlHandler::startElement;
    result->endElement = &CollectionXmlHandler::endElement;
    result->characters = &CollectionXmlHandler::characters;

    return result;
}

string Stub_CollAnalyticDatas::GetRootElement() {
    return "collection";
}
