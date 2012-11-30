#ifndef ENTITYUPDATEPROXY_H
#define ENTITYUPDATEPROXY_H

#include "UpdateProxy.h"
#include "../objects/configuration/UserEntity.h"

class EntityUpdateProxy: public UpdateProxy<UserEntity> {
public:
    EntityUpdateProxy();
    virtual ~EntityUpdateProxy();

    virtual void Serialize(xmlNodePtr node);
    virtual string GetRootElement();
    void Remove(UserEntity* val);
};

#endif // ENTITYUPDATEPROXY_H
