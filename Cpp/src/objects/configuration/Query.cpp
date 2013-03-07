#include "Query.h"

Query::Query() {}
Query::~Query() {}

Query::Query(string name, string query) {
    this->name = name;
    this->query = query;
}

void Query::Serialize(Json::Value& root) {
    root["name"] = name;
    root["query"] = query;
}

void Query::Deserialize(Json::Value& root) {
    name = root.get("name", "").asString();
    query = root.get("query", "").asString();
}
