#ifndef XMLSERIALIZABLE_H
#define XMLSERIALIZABLE_H

#include <libxml/parser.h>
#include <cstring>
#include <string>
#include <sstream>
#include <iostream>

class XmlSerializable {
public:
    virtual ~XmlSerializable(){}

    virtual void Serialize(xmlNodePtr node) = 0;
    virtual void Deserialize(std::string source) {}
    virtual std::string GetRootElement() {return "";}
    virtual xmlSAXHandler* GetXmlHandler() {return NULL;}

    std::string GetCurrent() {return current;}
    void SetCurrent(std::string current) {this->current = current;}
    std::string GetParent() {return parent;}
    void SetParent(std::string parent) {this->parent = parent;}

    bool StringToBool(std::string value) {
        return value == "true";
    }

    std::string BoolToString(bool value) {
        //return value ? "true" : "false";
        std::ostringstream result;
        result << std::boolalpha << value;
        return result.str();
    }

    std::string IntToString(int value) {
        std::ostringstream result;
        result << value;
        return result.str();
    }

    std::string DoubleToString(double value) {
        std::ostringstream result;
        result << value;
        return result.str();
    }

    void addNewXmlChild(xmlNodePtr root, std::string tagName, std::string value) {
        if (value != "") {
            xmlNewChild(root, NULL, BAD_CAST tagName.c_str(), BAD_CAST value.c_str());
        }
    }

private:
    std::string current;
    std::string parent;
};

#endif // XMLSERIALIZABLE_H
