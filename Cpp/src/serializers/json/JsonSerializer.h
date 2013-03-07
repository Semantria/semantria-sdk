#ifndef JSONSERIALIZER_H
#define JSONSERIALIZER_H

#include "../Serializer.h"
#include "JsonSerializable.h"

class JsonSerializer: public Serializer<JsonSerializable> {
public:
	JsonSerializer();
	virtual ~JsonSerializer();

	string GetType();
	string Serialize(JsonSerializable* obj);
    string Serialize(vector<JsonSerializable*>* list);
	void Deserialize(string source, JsonSerializable* obj);

};

#endif // JSONSERIALIZER_H
