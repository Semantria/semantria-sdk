#include "XmlSerializer.h"
#include <iostream>

XmlSerializer::XmlSerializer() {}
XmlSerializer::~XmlSerializer() {}


string XmlSerializer::GetType() {
    return "xml";
}

string XmlSerializer::Serialize(XmlSerializable* object) {
    LIBXML_TEST_VERSION;

    xmlDocPtr doc = xmlNewDoc(BAD_CAST "1.0");
    xmlNodePtr root_node = xmlNewNode(NULL, BAD_CAST object->GetRootElement().c_str());
    xmlDocSetRootElement(doc, root_node);

    object->Serialize(root_node);

    xmlChar *s;
    int size;
    xmlDocDumpMemory(doc, &s, &size);

    xmlFreeDoc(doc);
    xmlCleanupParser();

    return (char *)s;
}

void XmlSerializer::Deserialize(string source, XmlSerializable* object) {
    /*
     * This part worked incorrect ("object" didn't save its state), so I moved it to XmlSerializable objects

     * xmlSAXHandler* handler = object->GetXmlHandler();
     * int result = xmlSAXUserParseMemory(handler, object, source.c_str(), int(source.length()));
     * xmlCleanupParser();
     */
    object->Deserialize(source);
}

