#ifndef STUB_COLLANALYTICDATAS_H
#define STUB_COLLANALYTICDATAS_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/output/CollAnalyticData.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../serializers/xml/CollectionXmlHandler.h"

using namespace std;

class Stub_CollAnalyticDatas: public JsonSerializable, public XmlSerializable {
public:
    Stub_CollAnalyticDatas();
    virtual ~Stub_CollAnalyticDatas();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    void Serialize(xmlNodePtr node);
    void Deserialize(string source);
    xmlSAXHandler* GetXmlHandler();
    string GetRootElement();

    vector<CollAnalyticData*>* GetAnalyticData() {return analyticData;}
    CollAnalyticData* GetCollAnalytic() {return collAnalytic;}
    void SetCollAnalytic(CollAnalyticData* collAnalytic) {this->collAnalytic = collAnalytic;}
    Facet* GetFacet() {return facet;}
    void SetFacet(Facet* facet) {this->facet = facet;}
    Attribute* GetAttribute() {return attribute;}
    void SetAttribute(Attribute* attribute) {this->attribute = attribute;}
    CollEntity* GetCollEntity() {return collEntity;}
    void SetCollEntity(CollEntity* collEntity) {this->collEntity = collEntity;}
    CollTheme* GetCollTheme() {return collTheme;}
    void SetCollTheme(CollTheme* collTheme) {this->collTheme = collTheme;}
    CollTopic* GetCollTopic() {return collTopic;}
    void SetCollTopic(CollTopic* collTopic) {this->collTopic = collTopic;}
    vector<string>* GetHierarchy() {return hierarchy;}

    void clear() {
        if (NULL != analyticData) {delete analyticData;}
        if (NULL != collAnalytic) {delete collAnalytic;}
        if (NULL != facet) {delete facet;}
        if (NULL != attribute) {delete attribute;}
        if (NULL != collEntity) {delete collEntity;}
        if (NULL != collTheme) {delete collTheme;}
        if (NULL != collTopic) {delete collTopic;}
        if (NULL != hierarchy) {delete hierarchy;}
    }

private:
    vector<CollAnalyticData*>* analyticData;
    CollAnalyticData* collAnalytic;
    Facet* facet;
    Attribute* attribute;
    CollEntity* collEntity;
    CollTheme* collTheme;
    CollTopic* collTopic;
    vector<string>* hierarchy;
};

#endif // STUB_COLLANALYTICDATAS_H
