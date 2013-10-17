#ifndef Location_H
#define Location_H

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class Location: public JsonSerializable {
public:
    Location();
    virtual ~Location();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    int GetIndex() {return index;}
    int GetOffset() {return offset;}
    int GetLength() {return length;}

    void SetIndex(int index) {this->index = index;}
    void SetOffset(int offset) {this->offset = offset;}
    void SetLength(int length) {this->length = length;}

private:
    int index;
    int offset;
    int length;
};

#endif // Location_H
