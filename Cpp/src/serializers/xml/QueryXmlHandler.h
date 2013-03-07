#ifndef QUERYXMLHANDLER_H
#define QUERYXMLHANDLER_H

#include "../../objects/stub/Stub_Queries.h"

class QueryXmlHandler {
public:
    QueryXmlHandler();
    virtual ~QueryXmlHandler();

    static void startElement(void* user_data, const xmlChar* name, const xmlChar** attrs);
    static void endElement(void* user_data, const xmlChar* name);
    static void characters(void* user_data, const xmlChar* ch, int len);
};

#endif // QUERYXMLHANDLER_H
