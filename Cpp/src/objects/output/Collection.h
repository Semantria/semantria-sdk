#ifndef COLLECTION_H
#define COLLECTION_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../objects/output/Document.h"

using namespace std;

class Collection: public JsonSerializable, public XmlSerializable {
public:
    Collection();
    virtual ~Collection();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);
    void Serialize(xmlNodePtr node);
    xmlSAXHandler* GetXmlHandler() {return NULL;}
    string GetRootElement();

    string GetId() {return id;}
    vector<string>* GetDocuments() {return documents;}

    void SetId(string id) {this->id = id;}
    void SetDocuments(vector<string>* documents) {this->documents = documents;}

private:
    string id;
    vector<string>* documents;
};

#endif // COLLECTION_H
