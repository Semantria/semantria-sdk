#ifndef QUERYUPDATEPROXY_H
#define QUERYUPDATEPROXY_H

#include "UpdateProxy.h"
#include "../objects/configuration/Query.h"

class QueryUpdateProxy: public UpdateProxy<Query> {
public:
    QueryUpdateProxy();
    virtual ~QueryUpdateProxy();

    virtual void Serialize(xmlNodePtr node);
    virtual string GetRootElement();
    void Remove(Query* val);
};

#endif // QUERYUPDATEPROXY_H
