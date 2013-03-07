#include "QueryXmlHandler.h"

QueryXmlHandler::QueryXmlHandler() {}
QueryXmlHandler::~QueryXmlHandler() {}

void QueryXmlHandler::startElement(void* user_data, const xmlChar* name, const xmlChar** attrs) {
    Stub_Queries* stub = static_cast<Stub_Queries*>(user_data);
    string elementName = (const char *)(name);

    if ("query" == elementName) {
        stub->idx += 1;
        if (stub->idx == 1) {
            stub->SetQuery(new Query());
        }
    }

    stub->SetCurrent(elementName);
}

void QueryXmlHandler::endElement(void* user_data, const xmlChar* name) {
    Stub_Queries* stub = static_cast<Stub_Queries*>(user_data);
    string elementName = (const char *)(name);

    if ("query" == elementName && stub->idx == 1) {
        stub->GetQueries()->push_back(stub->GetQuery());
        stub->SetQuery(new Query());
        stub->idx -= 1;
    } else if ("query" == elementName && stub->idx == 2) {
        stub->idx -= 1;
    }

    stub->SetCurrent("");
}

void QueryXmlHandler::characters(void* user_data, const xmlChar* ch, int len) {
    Stub_Queries* stub = static_cast<Stub_Queries*>(user_data);
    string value = (const char *)(xmlStrncatNew(BAD_CAST "", xmlStrsub(ch, 0, len), len));
    Query* query = stub->GetQuery();

    if (stub->GetCurrent() == "name") {
        query->SetName(query->GetName() + value);
    } else if (stub->GetCurrent() == "query") {
        query->SetQuery(query->GetQuery() + value);
    }
}
