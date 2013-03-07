#ifndef STUB_CATEGORIES_H
#define STUB_CATEGORIES_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/configuration/Category.h"
#include "../../serializers/xml/XmlSerializable.h"
#include "../../serializers/xml/CategoryXmlHandler.h"

using namespace std;

class Stub_Categories: public JsonSerializable, public XmlSerializable {
public:
    Stub_Categories();
    virtual ~Stub_Categories();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    void Serialize(xmlNodePtr node);
    void Deserialize(string source);
    xmlSAXHandler* GetXmlHandler();
    string GetRootElement();

    vector<Category*>* GetCategories() {return categories;}
    Category* GetCategory() {return category;}
    void SetCategory(Category* category) {this->category = category;}

    void clear() {
        if (NULL != categories) {
            delete categories;
        }

        if (NULL != category) {
            delete category;
        }
    }
private:
    vector<Category*>* categories;
    Category* category;
};

#endif // STUB_CATEGORIES_H
