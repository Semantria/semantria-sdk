#include "EntityUpdateProxy.h"

EntityUpdateProxy::EntityUpdateProxy() {}
EntityUpdateProxy::~EntityUpdateProxy() {}

void EntityUpdateProxy::Remove(UserEntity* item) {
    removed->push_back(item->GetName());
}

void EntityUpdateProxy::Serialize(xmlNodePtr root) {
    if (added->size() > 0) {
        xmlNodePtr addedNode = xmlNewChild(root, NULL, BAD_CAST "added", NULL);
        for(vector<string>::size_type i = 0; i != added->size(); i++) {
            xmlNodePtr entityNode = xmlNewChild(addedNode, NULL, BAD_CAST "entity", NULL);
            UserEntity* entity = added->at(i);
            addNewXmlChild(entityNode, "name", entity->GetName());
            addNewXmlChild(entityNode, "type", entity->GetType());
        }
    }

    if (removed->size() > 0) {
        xmlNodePtr removedNode = xmlNewChild(root, NULL, BAD_CAST "removed", NULL);
        for(vector<string>::size_type i = 0; i != removed->size(); i++) {
            addNewXmlChild(removedNode, "entity", removed->at(i));
        }
    }
}

string EntityUpdateProxy::GetRootElement() {
    return "entities";
}
