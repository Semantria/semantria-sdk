#ifndef __semantria_sdk__Mention2s__
#define __semantria_sdk__Mention2s__

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/output/Location.h"

using namespace std;

class MentionWithLocations: public JsonSerializable {
public:
    MentionWithLocations();
    virtual ~MentionWithLocations();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetLabel()           {return label;}
    bool GetIsNedated()         {return is_negated;}
    string GetNegationPhrase()  {return negation_phrase;}
    vector<Location*>* GetLocations()   {return locations;}

    void SetLabel(string label)                     {this->label = label;}
    void SetIsNedated(bool is_negated)              {this->is_negated = is_negated;}
    void SetNegationPhrase(string negation_phrase)  {this->negation_phrase = negation_phrase;}
    void SetLocations(vector<Location*>* locations) {this->locations = locations;}

private:
    string label;
    bool is_negated;
    string negation_phrase;
    vector<Location*>* locations;
};

#endif /* defined(__semantria_sdk__Mention2s__) */
