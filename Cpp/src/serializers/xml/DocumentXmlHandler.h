#ifndef DOCUMENTXMLHANDLER_H
#define DOCUMENTXMLHANDLER_H

#include "../../objects/stub/Stub_DocAnalyticDatas.h"

class DocumentXmlHandler {
public:
    DocumentXmlHandler();
    virtual ~DocumentXmlHandler();

    static void startElement(void* user_data, const xmlChar* name, const xmlChar** attrs);
    static void endElement(void* user_data, const xmlChar* name);
    static void characters(void* user_data, const xmlChar* ch, int len);
};

#endif // DOCUMENTXMLHANDLER_H
