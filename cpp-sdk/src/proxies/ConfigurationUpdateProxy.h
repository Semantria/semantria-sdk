#ifndef CONFIGURATIONUPDATEPROXY_H
#define CONFIGURATIONUPDATEPROXY_H

#include "UpdateProxy.h"
#include "../objects/configuration/Configuration.h"

class ConfigurationUpdateProxy: public UpdateProxy<Configuration> {
public:
    ConfigurationUpdateProxy();
    virtual ~ConfigurationUpdateProxy();

    virtual void Serialize(xmlNodePtr node);
    virtual string GetRootElement();
    void Remove(Configuration* obj);
    void Clone(Configuration* obj);
};

#endif // CONFIGURATIONUPDATEPROXY_H
