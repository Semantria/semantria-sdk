#ifndef CATEGORYXMLHANDLER_H
#define CATEGORYXMLHANDLER_H

#include "../../objects/stub/Stub_Categories.h"

class CategoryXmlHandler {
public:
    CategoryXmlHandler();
    virtual ~CategoryXmlHandler();

    static void startElement(void* user_data, const xmlChar* name, const xmlChar** attrs);
    static void endElement(void* user_data, const xmlChar* name);
    static void characters(void* user_data, const xmlChar* ch, int len);
};

#endif // CATEGORYXMLHANDLER_H
