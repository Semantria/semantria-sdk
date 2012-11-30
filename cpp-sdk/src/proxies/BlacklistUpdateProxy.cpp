#include "BlacklistUpdateProxy.h"

BlacklistUpdateProxy::BlacklistUpdateProxy() {}
BlacklistUpdateProxy::~BlacklistUpdateProxy() {}

void BlacklistUpdateProxy::Serialize(Json::Value& root) {
    for(int i = 0; i != added->size(); i++) {
        root["added"].append(added->at(i)->GetItem());
    }

    for(vector<string>::size_type i = 0; i != removed->size(); i++) {
        Json::Value removedElement(removed->at(i));
        root["removed"].append(removedElement);
    }
}

void BlacklistUpdateProxy::Remove(Blacklisted* item) {
    removed->push_back(item->GetItem());
}

void BlacklistUpdateProxy::Serialize(xmlNodePtr root) {
    if (added->size() > 0) {
        xmlNodePtr addedNode = xmlNewChild(root, NULL, BAD_CAST "added", NULL);
        for(vector<string>::size_type i = 0; i != added->size(); i++) {
            addNewXmlChild(addedNode, "item", added->at(i)->GetItem());
        }
    }

    if (removed->size() > 0) {
        xmlNodePtr removedNode = xmlNewChild(root, NULL, BAD_CAST "removed", NULL);
        for(vector<string>::size_type i = 0; i != removed->size(); i++) {
            addNewXmlChild(removedNode, "item", removed->at(i));
        }
    }
}

void BlacklistUpdateProxy::Deserialize(std::string source) {}

string BlacklistUpdateProxy::GetRootElement() {
    return "blacklist";
}
