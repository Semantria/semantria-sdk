#ifndef CONFIGURATIONXMLHANDLER_H
#define CONFIGURATIONXMLHANDLER_H

#include "../../objects/stub/Stub_Configurations.h"

class ConfigurationXmlHandler {
public:
    ConfigurationXmlHandler();
    virtual ~ConfigurationXmlHandler();

    static void startElement(void* user_data, const xmlChar* name, const xmlChar** attrs);
    static void endElement(void* user_data, const xmlChar* name);
    static void characters(void* user_data, const xmlChar* ch, int len);
};

#endif // CONFIGURATIONXMLHANDLER_H
