#ifndef STUB_ENTITIES_H
#define STUB_ENTITIES_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../serializers/xml/UserEntityXmlHandler.h"
#include "../../objects/configuration/UserEntity.h"

using namespace std;

class Stub_Entities: public JsonSerializable, public XmlSerializable {
public:
    Stub_Entities();
    virtual ~Stub_Entities();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    void Serialize(xmlNodePtr node);
    void Deserialize(string source);
    xmlSAXHandler* GetXmlHandler();
    string GetRootElement();

    vector<UserEntity*>* GetEntities() {return entities;}
    UserEntity* GetEntity() {return entity;}
    void SetEntity(UserEntity* entity) {this->entity = entity;}

    void clear() {
        if (NULL != entities) {delete entities;}
        if (NULL != entity) {delete entity;}
    }
private:
    vector<UserEntity*>* entities;
    UserEntity* entity;
};

#endif // STUB_ENTITIES_H
