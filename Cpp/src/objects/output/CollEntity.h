#ifndef COLLENTITY_H
#define COLLENTITY_H

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class CollEntity: public JsonSerializable {
public:
    CollEntity();
    virtual ~CollEntity();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetTitle() {return title;}
    string GetType() {return type;}
    string GetLabel() {return label;}
    string GetEntityType() {return entity_type;}
    int GetCount() {return count;}
    int GetNegativeCount() {return negative_count;}
    int GetNeutralCount() {return neutral_count;}
    int GetPositiveCount() {return positive_count;}

    void SetTitle(string title) {this->title = title;}
    void SetType(string type) {this->type = type;}
    void SetLabel(string label) {this->label = label;}
    void SetEntityType(string entity_type) {this->entity_type = entity_type;}
    void SetCount(int count) {this->count = count;}
    void SetNegativeCount(int negative_count) {this->negative_count = negative_count;}
    void SetNeutralCount(int neutral_count) {this->neutral_count = neutral_count;}
    void SetPositiveCount(int positive_count) {this->positive_count = positive_count;}

private:
    string title;
    string type;
    string label;
    string entity_type;
    int count;
    int negative_count;
    int neutral_count;
    int positive_count;
};

#endif // COLLENTITY_H
