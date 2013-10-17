#include "DocAutoCategories.h"

DocAutoCategories::DocAutoCategories() {
    categories = new vector<DocCategories*>();

}
DocAutoCategories::~DocAutoCategories() {
    delete categories;
}

void DocAutoCategories::Serialize(Json::Value& root) {
    root["title"] = title;
    root["type"] = type;
    root["strength_score"] = strength_score;
}

void DocAutoCategories::Deserialize(Json::Value& root) {
    title = root.get("title", "").asString();
    type = root.get("type", "").asString();
    strength_score = root.get("strength_score", 0.0).asDouble();

    // Categories
    if (NULL == this->categories) {
        this->categories = new vector<DocCategories*>();
    }

    Json::Value categories = root["categories"];
    for ( int i = 0; i < categories.size(); ++i ) {
        DocCategories* category = new DocCategories();
        category->Deserialize(categories[i]);
        this->categories->push_back(category);
    }
}
