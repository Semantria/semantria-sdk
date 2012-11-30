#ifndef SUBSCRIPTIONXMLHANDLER_H
#define SUBSCRIPTIONXMLHANDLER_H

#include "../../objects/output/Subscription.h"

class SubscriptionXmlHandler {
public:
    SubscriptionXmlHandler();
    virtual ~SubscriptionXmlHandler();

    static void startElement(void* user_data, const xmlChar* name, const xmlChar** attrs);
    static void endElement(void* user_data, const xmlChar* name);
    static void characters(void* user_data, const xmlChar* ch, int len);
};

#endif // SUBSCRIPTIONXMLHANDLER_H
