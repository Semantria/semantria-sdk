#include "QueryUpdateProxy.h"

QueryUpdateProxy::QueryUpdateProxy() {}
QueryUpdateProxy::~QueryUpdateProxy() {}

void QueryUpdateProxy::Remove(Query* item) {
    removed->push_back(item->GetName());
}

void QueryUpdateProxy::Serialize(xmlNodePtr root) {
    if (added->size() > 0) {
        xmlNodePtr addedNode = xmlNewChild(root, NULL, BAD_CAST "added", NULL);
        for(vector<string>::size_type i = 0; i != added->size(); i++) {
            xmlNodePtr queryNode = xmlNewChild(addedNode, NULL, BAD_CAST "query", NULL);
            Query* query = added->at(i);
            addNewXmlChild(queryNode, "name", query->GetName());
            addNewXmlChild(queryNode, "query", query->GetQuery());
        }
    }

    if (removed->size() > 0) {
        xmlNodePtr removedNode = xmlNewChild(root, NULL, BAD_CAST "removed", NULL);
        for(vector<string>::size_type i = 0; i != removed->size(); i++) {
            addNewXmlChild(removedNode, "query", removed->at(i));
        }
    }
}

string QueryUpdateProxy::GetRootElement() {
    return "queries";
}
