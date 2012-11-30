#ifndef SERVICESTATUSXMLHANDLER_H
#define SERVICESTATUSXMLHANDLER_H

#include "../../objects/output/ServiceStatus.h"

class ServiceStatusXmlHandler {
public:
    ServiceStatusXmlHandler();
    virtual ~ServiceStatusXmlHandler();

    static void startElement(void* user_data, const xmlChar* name, const xmlChar** attrs);
    static void endElement(void* user_data, const xmlChar* name);
    static void characters(void* user_data, const xmlChar* ch, int len);
};

#endif // SERVICESTATUSXMLHANDLER_H
