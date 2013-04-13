#ifndef ATTRIBUTE_H
#define ATTRIBUTE_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/output/Mention.h"

using namespace std;

class Attribute: public JsonSerializable {
public:
    Attribute();
    virtual ~Attribute();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetLabel() {return label;}
    int GetCount() {return count;}
    vector<Mention*>* GetMentions() {return mentions;}

    void SetLabel(string label) {this->label = label;}
    void SetCount(int count) {this->count = count;}
    void SetMentions(vector<Mention*>* mentions) {this->mentions = mentions;}
    void AddMentions(Mention* mention) {this->mentions->push_back(mention);}

private:
    string label;
    int count;
    vector<Mention*>* mentions;
};

#endif // ATTRIBUTE_H
