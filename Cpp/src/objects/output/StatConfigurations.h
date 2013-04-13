#ifndef __semantria_sdk__StatConfigurations__
#define __semantria_sdk__StatConfigurations__

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class StatConfigurations: public JsonSerializable {
public:
    StatConfigurations();
    virtual ~StatConfigurations();
    
    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);
    
    string GetConfigId()            {return config_id;}
    string GetName()                {return name;}
    int GetOverallTexts()           {return overall_texts ;}
    int GetOverallBatches()         {return overall_batches;}
    int GetOverallCalls()           {return overall_calls;}
    int GetCallsSystem()            {return calls_system;}
    int GetCallsData()              {return calls_data;}
    int GetOverallDocs()            {return overall_docs;}
    int GetDocsProcessed()          {return docs_processed;}
    int GetDocsFailed()             {return docs_failed;}
    int GetDocsResponded()          {return docs_responded;}
    int GetOverallColls()           {return overall_colls;}
    int GetCollsProcessed()         {return colls_processed;}
    int GetCollsFailed()            {return colls_failed;}
    int GetCollsResponded()         {return colls_responded;}
    int GetCollsDocuments()         {return colls_documents;}
    string GetLatestUsedApp()       {return latest_used_app;}
    string GetUsedApps()            {return used_apps;}
    
    
private:
    string config_id;
    string name;
    int overall_texts;
    int overall_batches;
    int overall_calls;
    int calls_system;
    int calls_data;
    int overall_docs;
    int docs_processed;
    int docs_failed;
    int docs_responded;
    int overall_colls;
    int colls_processed;
    int colls_failed;
    int colls_responded;
    int colls_documents;
    string latest_used_app;
    string used_apps;
};

#endif /* defined(__semantria_sdk__StatConfigurations__) */
