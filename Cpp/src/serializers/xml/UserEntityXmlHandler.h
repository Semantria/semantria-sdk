#ifndef USERENTITYXMLHANDLER_H
#define USERENTITYXMLHANDLER_H

#include "../../objects/stub/Stub_Entities.h"

class UserEntityXmlHandler {
public:
    UserEntityXmlHandler();
    virtual ~UserEntityXmlHandler();

    static void startElement(void* user_data, const xmlChar* name, const xmlChar** attrs);
    static void endElement(void* user_data, const xmlChar* name);
    static void characters(void* user_data, const xmlChar* ch, int len);
};

#endif // USERENTITYXMLHANDLER_H
