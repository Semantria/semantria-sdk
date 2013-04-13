#ifndef __semantria_sdk__DocDetails__
#define __semantria_sdk__DocDetails__

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/output/DocWords.h"

using namespace std;

class DocDetails: public JsonSerializable {
public:
    DocDetails();
    virtual ~DocDetails();
    
    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);
    
    bool GetIsImperative()        {return is_imperative;}
    bool GetIsPolar()             {return is_polar;}
    vector<DocWords*>* GetWords() {return words;}
    
    void SetIsImperative(bool is_imperative)        {this->is_imperative = is_imperative;}
    void SetIsPolar(bool is_polar)                  {this->is_polar = is_polar;}
    void SetWords(vector<DocWords*>* words)         {this->words = words;}
    
private:
    bool is_imperative;
    bool is_polar;
    vector<DocWords*>* words;
};

#endif /* defined(__semantria_sdk__DocDetails__) */
