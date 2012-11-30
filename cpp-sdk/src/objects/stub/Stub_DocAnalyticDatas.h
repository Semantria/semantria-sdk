#ifndef STUB_DOCANALYTICDATAS_H
#define STUB_DOCANALYTICDATAS_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/output/DocAnalyticData.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../serializers/xml/DocumentXmlHandler.h"

using namespace std;

class Stub_DocAnalyticDatas: public JsonSerializable, public XmlSerializable {
public:
    Stub_DocAnalyticDatas();
    virtual ~Stub_DocAnalyticDatas();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    void Serialize(xmlNodePtr node);
    void Deserialize(string source);
    xmlSAXHandler* GetXmlHandler();
    string GetRootElement();

    vector<DocAnalyticData*>* GetAnalyticData() {return analyticData;}
    DocAnalyticData* GetDocAnalytic() {return docAnalytic;}
    void SetDocAnalytic(DocAnalyticData* docAnalytic) {this->docAnalytic = docAnalytic;}

    DocEntity* GetDocEntity() {return docEntity;}
    void SetDocEntity(DocEntity* docEntity) {this->docEntity = docEntity;}
    DocPhrase* GetDocPhrase() {return docPhrase;}
    void SetDocPhrase(DocPhrase* docPhrase) {this->docPhrase = docPhrase;}
    DocTheme* GetDocTheme() {return docTheme;}
    void SetDocTheme(DocTheme* docTheme) {this->docTheme = docTheme;}
    DocTopic* GetDocTopic() {return docTopic;}
    void SetDocTopic(DocTopic* docTopic) {this->docTopic = docTopic;}
    vector<string>* GetHierarchy() {return hierarchy;}

    void clear() {
        if (NULL != analyticData) {delete analyticData;}
        if (NULL != docAnalytic) {delete docAnalytic;}
        if (NULL != docEntity) {delete docEntity;}
        if (NULL != docPhrase) {delete docPhrase;}
        if (NULL != docTheme) {delete docTheme;}
        if (NULL != docTopic) {delete docTopic;}
        if (NULL != hierarchy) {delete hierarchy;}
    }
private:
    vector<DocAnalyticData*>* analyticData;
    DocAnalyticData* docAnalytic;
    DocEntity* docEntity;
    DocPhrase* docPhrase;
    DocTheme* docTheme;
    DocTopic* docTopic;
    vector<string>* hierarchy;
};

#endif // STUB_DOCANALYTICDATAS_H
