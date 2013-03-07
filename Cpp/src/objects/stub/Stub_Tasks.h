#ifndef STUB_TASKS_H
#define STUB_TASKS_H

#include <vector>
#include "../output/Document.h"
#include "../../serializers/json/JsonSerializable.h"
#include "../../serializers/xml/XmlSerializable.h"

class Stub_Tasks: public JsonSerializable, public XmlSerializable {
public:
    Stub_Tasks();
    Stub_Tasks(vector<Document*>* documents) {this->documents = documents;}
    virtual ~Stub_Tasks();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    void Serialize(xmlNodePtr node);
    void Deserialize(std::string source);
    string GetRootElement();

    vector<Document*>* GetDocuments() {return documents;}
    void SetDocument(vector<Document*>* documents) {this->documents = documents;}

    void clear() {
        if (NULL != documents) {delete documents;}
    }
private:
    vector<Document*>* documents;
};

#endif // STUB_TASKS_H
