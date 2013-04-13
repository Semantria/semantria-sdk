#ifndef __semantria_sdk__DocRelations__
#define __semantria_sdk__DocRelations__

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/output/DocRelEntities.h"

using namespace std;

class DocRelations: public JsonSerializable {
public:
    DocRelations();
    virtual ~DocRelations();
    
    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);
    
    string GetType()                       {return type;}
    string GetRelationType()               {return relation_type;}
    string GetConfidenceScore()            {return confidence_score;}
    string GetExtra()                      {return extra;}
    vector<DocRelEntities*>* GetEntitles() {return entitles;}
    
    void SetType(string type)                                   {this->type = type;}
    void SetRelationType(string relation_type)                  {this->relation_type = relation_type;}
    void SetConfidenceScore(string confidence_score)            {this->confidence_score = confidence_score;}
    void SetExtra(string extra)                                 {this->extra = extra;}
    void SetEntitles(vector<DocRelEntities*>* entitles)         {this->entitles = entitles;}
    
private:
    string type;
    string relation_type;
    string confidence_score;
    string extra;
    vector<DocRelEntities*>* entitles;
};

#endif /* defined(__semantria_sdk__DocRelations__) */
