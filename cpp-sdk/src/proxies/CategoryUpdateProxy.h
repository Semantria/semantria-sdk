#ifndef CATEGORYUPDATEPROXY_H
#define CATEGORYUPDATEPROXY_H

#include "UpdateProxy.h"
#include "../objects/configuration/Category.h"

class CategoryUpdateProxy: public UpdateProxy<Category> {
public:
    CategoryUpdateProxy();
    virtual ~CategoryUpdateProxy();

    virtual void Serialize(xmlNodePtr node);
    virtual string GetRootElement();
    void Remove(Category* val);
};

#endif // CATEGORYUPDATEPROXY_H
