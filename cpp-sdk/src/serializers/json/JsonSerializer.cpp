#include "JsonSerializer.h"

JsonSerializer::JsonSerializer() {}
JsonSerializer::~JsonSerializer() {}

string JsonSerializer::GetType() {
    return "json";
}

string JsonSerializer::Serialize(JsonSerializable* object) {
    Json::Value root;
    Json::FastWriter writer;

    object->Serialize(root);

    return writer.write(root);
}

void JsonSerializer::Deserialize(string source, JsonSerializable* object) {
    Json::Value root;
    Json::Reader reader;

    if (!reader.parse(source, root)) {
        std::cout  << "Failed to parse sources\n";
    } else {
        object->Deserialize(root);
    }
}
