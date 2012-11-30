#ifndef STUB_QUERIES_H
#define STUB_QUERIES_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/configuration/Query.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../serializers/xml/QueryXmlHandler.h"

using namespace std;

class Stub_Queries: public JsonSerializable, public XmlSerializable {
public:
    Stub_Queries();
    virtual ~Stub_Queries();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    void Serialize(xmlNodePtr node);
    void Deserialize(string source);
    xmlSAXHandler* GetXmlHandler();
    string GetRootElement();

    vector<Query*>* GetQueries() {return queries;}
    Query* GetQuery() {return query;}
    void SetQuery(Query* query) {this->query = query;}

    int idx;

    void clear() {
        if (NULL != queries) {delete queries;}
        if (NULL != query) {delete query;}
    }
private:
    vector<Query*>* queries;
    Query* query;
};

#endif // STUB_QUERIES_H
