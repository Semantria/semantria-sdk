#ifndef QUERY_H
#define QUERY_H

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class Query: public JsonSerializable {
public:
    Query();
    Query(string name, string query);
    virtual ~Query();

    virtual void Serialize(Json::Value& root);
    virtual void Deserialize(Json::Value& root);

    string GetName() {return name;}
    string GetQuery() {return query;}
    void SetName(string name) {this->name = name;}
    void SetQuery(string query) {this->query = query;}

private:
    string name;
    string query;
};

#endif // QUERY_H
