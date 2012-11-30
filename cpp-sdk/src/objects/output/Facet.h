#ifndef FACET_H
#define FACET_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/output/Attribute.h"

using namespace std;

class Facet: public JsonSerializable {
public:
    Facet();
    virtual ~Facet();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetLabel() {return label;}
    int GetCount() {return count;}
    int GetNegativeCount() {return negative_count;}
    int GetNeutralCount() {return neutral_count;}
    int GetPositiveCount() {return positive_count;}
    vector<Attribute*>* GetAttributes() {return attributes;}

    void SetLabel(string label) {this->label = label;}
    void SetCount(int count) {this->count = count;}
    void SetNegativeCount(int negative_count) {this->negative_count = negative_count;}
    void SetNeutralCount(int neutral_count) {this->neutral_count = neutral_count;}
    void SetPositiveCount(int positive_count) {this->positive_count = positive_count;}
    void SetAttributes(vector<Attribute*>* attributes) {this->attributes = attributes;}
    void AddAttributes(Attribute* attribute) {this->attributes->push_back(attribute);}

private:
    string label;
    int count;
    int negative_count;
    int neutral_count;
    int positive_count;
    vector<Attribute*>* attributes;
};

#endif // FACET_H
