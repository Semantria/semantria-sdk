#ifndef ATTRIBUTE_H
#define ATTRIBUTE_H

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class Attribute: public JsonSerializable {
public:
    Attribute();
    virtual ~Attribute();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetLabel() {return label;}
    int GetCount() {return count;}

    void SetLabel(string label) {this->label = label;}
    void SetCount(int count) {this->count = count;}

private:
    string label;
    int count;

};

#endif // ATTRIBUTE_H
