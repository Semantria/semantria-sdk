#ifndef BLACKLISTUPDATEPROXY_H
#define BLACKLISTUPDATEPROXY_H

#include "UpdateProxy.h"
#include "../objects/configuration/Blacklisted.h"

class BlacklistUpdateProxy: public UpdateProxy<Blacklisted> {
public:
    BlacklistUpdateProxy();
    virtual ~BlacklistUpdateProxy();

    void Serialize(Json::Value& root);
    void Serialize(xmlNodePtr node);
    void Deserialize(std::string source);
    string GetRootElement();
    void Remove(Blacklisted* val);
};

#endif // BLACKLISTUPDATEPROXY_H
