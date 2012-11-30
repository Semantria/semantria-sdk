#include "CategoryXmlHandler.h"

CategoryXmlHandler::CategoryXmlHandler() {}
CategoryXmlHandler::~CategoryXmlHandler() {}

void CategoryXmlHandler::startElement(void* user_data, const xmlChar* name, const xmlChar** attrs) {
    Stub_Categories* stub = static_cast<Stub_Categories*>(user_data);
    string elementName = (const char *)(name);

    if ("category" == elementName) {
        stub->SetCategory(new Category());
    }
    stub->SetCurrent(elementName);
}

void CategoryXmlHandler::endElement(void* user_data, const xmlChar* name) {
    Stub_Categories* stub = static_cast<Stub_Categories*>(user_data);
    string elementName = (const char *)(name);

    if ("category" == elementName) {
        stub->GetCategories()->push_back(stub->GetCategory());
        stub->SetCategory(new Category());
    }

    stub->SetCurrent("");
}

void CategoryXmlHandler::characters(void* user_data, const xmlChar* ch, int len) {
    Stub_Categories* stub = static_cast<Stub_Categories*>(user_data);
    string value = (const char *)(xmlStrncatNew(BAD_CAST "", xmlStrsub(ch, 0, len), len));
    Category* category = stub->GetCategory();

    if (stub->GetCurrent() == "name") {
        category->SetName(category->GetName() + value);
    } else if (stub->GetCurrent() == "weight") {
        category->SetWeight(atof(value.c_str()));
    } else if (stub->GetCurrent() == "sample") {
        category->AddSample(value);
    }
}
