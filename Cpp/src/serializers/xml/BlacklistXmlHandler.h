#ifndef BLACKLISTXMLHANDLER_H
#define BLACKLISTXMLHANDLER_H

#include "../../objects/stub/Stub_Blacklist.h"

class BlacklistXmlHandler {
public:
    BlacklistXmlHandler();
    virtual ~BlacklistXmlHandler();

    static void startElement(void* user_data, const xmlChar* name, const xmlChar** attrs);
    static void endElement(void* user_data, const xmlChar* name);
    static void characters(void* user_data, const xmlChar* ch, int len);
};

#endif // BLACKLISTXMLHANDLER_H
