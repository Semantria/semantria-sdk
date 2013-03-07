#ifndef BLACKLISTED_H
#define BLACKLISTED_H

#include "../../serializers/json/JsonSerializable.h"
//#include "../../serializers/xml/XmlSerializable.h"
//#include "../../serializers/xml/BlacklistXmlHandler.h"

using namespace std;

class Blacklisted: public JsonSerializable {
public:
    Blacklisted();
    Blacklisted(string item);
    virtual ~Blacklisted();

    virtual void Serialize(Json::Value& root);
    virtual void Deserialize(Json::Value& root);

    string GetItem() {return item;}
    void SetItem(string item) {this->item = item;}

private:
    string item;
};

#endif // BLACKLISTED_H
