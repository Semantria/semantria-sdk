#include "CategoryUpdateProxy.h"

CategoryUpdateProxy::CategoryUpdateProxy() {}
CategoryUpdateProxy::~CategoryUpdateProxy() {}

void CategoryUpdateProxy::Remove(Category* item) {
    removed->push_back(item->GetName());
}

void CategoryUpdateProxy::Serialize(xmlNodePtr root) {
    if (added->size() > 0) {
        xmlNodePtr addedNode = xmlNewChild(root, NULL, BAD_CAST "added", NULL);
        for(vector<string>::size_type i = 0; i != added->size(); i++) {
            xmlNodePtr categoryNode = xmlNewChild(addedNode, NULL, BAD_CAST "category", NULL);
            Category* category = added->at(i);
            addNewXmlChild(categoryNode, "name", category->GetName());
            addNewXmlChild(categoryNode, "weight", DoubleToString(category->GetWeight()));

            xmlNodePtr samplesNode = xmlNewChild(categoryNode, NULL, BAD_CAST "samples", NULL);
            vector<string>* samples = category->GetSamples();
            for(vector<string>::size_type i = 0; i != samples->size(); i++) {
                addNewXmlChild(samplesNode, "sample", samples->at(i));
            }
        }
    }

    if (removed->size() > 0) {
        xmlNodePtr removedNode = xmlNewChild(root, NULL, BAD_CAST "removed", NULL);
        for(vector<string>::size_type i = 0; i != removed->size(); i++) {
            addNewXmlChild(removedNode, "category", removed->at(i));
        }
    }
}

string CategoryUpdateProxy::GetRootElement() {
    return "categories";
}
