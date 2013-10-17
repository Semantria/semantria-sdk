#ifndef __semantria_sdk__Mentions__
#define __semantria_sdk__Mentions__

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/output/Location.h"

using namespace std;

class Mention: public JsonSerializable {
public:
    Mention();
    virtual ~Mention();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetLabel()           {return label;}
    bool GetIsNedated()         {return is_negated;}
    string GetNegatingPhrase()  {return negating_phrase;}
    vector<Location*>* GetLocations()   {return locations;}

    void SetLabel(string label)                     {this->label = label;}
    void SetIsNedated(bool is_negated)              {this->is_negated = is_negated;}
    void SetNegatingPhrase(string negating_phrase)  {this->negating_phrase = negating_phrase;}
    void SetLocations(vector<Location*>* locations) {this->locations = locations;}

private:
    string label;
    bool is_negated;
    string negating_phrase;
    vector<Location*>* locations;
};

#endif /* defined(__semantria_sdk__Mentions__) */
