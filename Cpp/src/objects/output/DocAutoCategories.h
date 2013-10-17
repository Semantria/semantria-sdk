#ifndef DOCAUTOCATEGORIES_H
#define DOCAUTOCATEGORIES_H

#include "../../serializers/json/JsonSerializable.h"
#include "../../objects/output/DocCategories.h"

using namespace std;

class DocAutoCategories: JsonSerializable {
public:
    DocAutoCategories();
    virtual ~DocAutoCategories();

    void Serialize(Json::Value& root);
    void Deserialize(Json::Value& root);

    string GetTitle()                           {return title;}
    string GetType()                            {return type;}
    double GetStrengthScore()                   {return strength_score;}
    vector<DocCategories*>* GetCategories()     {return categories;}

    void SetTitle(string title)                             {this->title = title;}
    void SetType(string type)                               {this->type = type;}
    void SetStrengthScore(double strength_score)            {this->strength_score = strength_score;}
    void SetCategories(vector<DocCategories*>* categories)  {this->categories = categories;}

private:
    string title;
    string type;
    double strength_score;
    vector<DocCategories*>* categories;
};

#endif // DOCAUTOCATEGORIES_H
