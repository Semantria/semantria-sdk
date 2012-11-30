#ifndef XMLSERIALIZER_H
#define XMLSERIALIZER_H

#include "../Serializer.h"
#include "XmlSerializable.h"

class XmlSerializer: public Serializer<XmlSerializable> {
public:
    XmlSerializer();
    virtual ~XmlSerializer();

    string GetType();
	string Serialize(XmlSerializable* object);
	void Deserialize(string source, XmlSerializable* object);
};

#endif // XMLSERIALIZER_H
