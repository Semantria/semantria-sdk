#include "Stub_Categories.h"

Stub_Categories::Stub_Categories() {
    categories = new vector<Category*>();
}

Stub_Categories::~Stub_Categories() {
    // For removing stub's items use clear() function instead
    category = NULL;
}

void Stub_Categories::Serialize(Json::Value& root) {}

void Stub_Categories::Deserialize(Json::Value& root) {
    if (NULL == this->categories) {
        this->categories = new vector<Category*>();
    }

    for ( int i = 0; i < root.size(); ++i ) {
        Category* category = new Category();
        category->Deserialize(root[i]);
        this->categories->push_back(category);
    }
}

void Stub_Categories::Deserialize(std::string source) {
    xmlSAXUserParseMemory(GetXmlHandler(), this, source.c_str(), int(source.length()));
    xmlCleanupParser();
}

void Stub_Categories::Serialize(xmlNodePtr root) {}

xmlSAXHandler* Stub_Categories::GetXmlHandler() {
    xmlSAXHandler* result = new xmlSAXHandler();
    result->startElement = &CategoryXmlHandler::startElement;
    result->endElement = &CategoryXmlHandler::endElement;
    result->characters = &CategoryXmlHandler::characters;

    return result;
}

string Stub_Categories::GetRootElement() {
    return "category";
}
