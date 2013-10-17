#ifndef DOCCATEGORIES_H
#define DOCCATEGORIES_H

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class DocCategories: JsonSerializable {
public:
    DocCategories();
    virtual ~DocCategories();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetTitle() {return title;}
    string GetType() {return type;}
    double GetStrengthScore() {return strength_score;}

    void SetTitle(string title) {this->title = title;}
    void SetType(string type) {this->type = type;}
    void SetStrengthScore(double strength_score) {this->strength_score = strength_score;}

private:
    string title;
    string type;
    double strength_score;
};

#endif // DOCAUTOCATEGORIES_H
