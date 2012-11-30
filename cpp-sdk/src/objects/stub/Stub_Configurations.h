#ifndef STUB_CONFIGURATIONS_H
#define STUB_CONFIGURATIONS_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/configuration/Configuration.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../serializers/xml/ConfigurationXmlHandler.h"

using namespace std;

class Stub_Configurations: public JsonSerializable, public XmlSerializable {
public:
    Stub_Configurations();
    virtual ~Stub_Configurations();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    void Serialize(xmlNodePtr node);
    void Deserialize(string source);
    xmlSAXHandler* GetXmlHandler();
    string GetRootElement();

    vector<Configuration*>* GetConfigurations() {return configurations;}
    Configuration* GetConfiguration() {return configuration;}
    void SetConfiguration(Configuration* configuration) {this->configuration = configuration;}
    vector<string>* GetHierarchy() {return hierarchy;}

    void clear() {
        if (NULL != configurations) {delete configurations;}
        if (NULL != configuration) {delete configuration;}
        if (NULL != hierarchy) {delete hierarchy;}
    }
private:
    vector<Configuration*>* configurations;
    Configuration* configuration;
    vector<string>* hierarchy;
};

#endif // STUB_CONFIGURATIONS_H
