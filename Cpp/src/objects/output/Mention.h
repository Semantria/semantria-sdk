#ifndef __semantria_sdk__Mentions__
#define __semantria_sdk__Mentions__

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class Mention: public JsonSerializable {
public:
    Mention();
    virtual ~Mention();
    
    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);
    
    string GetLabel()           {return label;}
    bool GetIsNedated()         {return is_negated;}
    string GetNegationPhrase()  {return negation_phrase;}
    vector<int>* GetIndexes()   {return indexes;}

    void SetLabel(string label)                     {this->label = label;}
    void SetIsNedated(bool is_negated)              {this->is_negated = is_negated;}
    void SetNegationPhrase(string negation_phrase)  {this->negation_phrase = negation_phrase;}
    void SetIndexes(vector<int>* indexes)           {this->indexes = indexes;}

private:
    string label;
    bool is_negated;
    string negation_phrase;
    vector<int>* indexes;
};

#endif /* defined(__semantria_sdk__Mentions__) */
