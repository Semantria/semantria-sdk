#ifndef STUB_BLACKLIST_H
#define STUB_BLACKLIST_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../serializers/xml/BlacklistXmlHandler.h"

using namespace std;

class Stub_Blacklist: public JsonSerializable, public XmlSerializable {
public:
    Stub_Blacklist();
    virtual ~Stub_Blacklist();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    void Serialize(xmlNodePtr node);
    void Deserialize(string source);
    xmlSAXHandler* GetXmlHandler();
    string GetRootElement();

    vector<string>* GetBlacklistItems() {return items;}

    void clear() {
        if (NULL != items) {
            delete items;
        }
    }

private:
    vector<string>* items;
};

#endif // STUB_BLACKLIST_H
