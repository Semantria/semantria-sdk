#ifndef DOCUMENT_H
#define DOCUMENT_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../serializers/xml/XmlSerializable.h"

using namespace std;

class Document: public JsonSerializable, public XmlSerializable {
public:
    Document();
    Document(string id, string text);
    virtual ~Document();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);
    void Serialize(xmlNodePtr node);
    xmlSAXHandler* GetXmlHandler() {return NULL;}
    string GetRootElement();

    string GetId() {return id;}
    string GetText() {return text;}

    void SetId(string id) {this->id = id;}
    void SetText(string text) {this->text = text;}

private:
    string id;
    string text;
};

#endif // DOCUMENT_H
