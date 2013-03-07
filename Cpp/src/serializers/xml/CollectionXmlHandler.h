#ifndef COLLECTIONXMLHANDLER_H
#define COLLECTIONXMLHANDLER_H

#include "../../objects/stub/Stub_CollAnalyticDatas.h"

class CollectionXmlHandler {
public:
    CollectionXmlHandler();
    virtual ~CollectionXmlHandler();

    static void startElement(void* user_data, const xmlChar* name, const xmlChar** attrs);
    static void endElement(void* user_data, const xmlChar* name);
    static void characters(void* user_data, const xmlChar* ch, int len);
};

#endif // COLLECTIONXMLHANDLER_H
