#ifndef CATEGORY_H
#define CATEGORY_H

#include "../../serializers/json/JsonSerializable.h"

using namespace std;

class Category: public JsonSerializable {
public:
    Category();
    Category(string name, double weight);
    Category(string name, double weight, vector<string>* samples);
    virtual ~Category();

    virtual void Serialize(Json::Value& root);
    virtual void Deserialize(Json::Value& root);

    string GetName() {return name;}
    double GetWeight() {return weight;}
    vector<string>* GetSamples() {return samples;}

    void SetName(string name) {this->name = name;}
    void SetWeight(double weight) {this->weight = weight;}
    void SetSamples(vector<string>* samples) {this->samples = samples;}
    void AddSample(string sample) {samples->push_back(sample);}

private:
    string name;
    double weight;
    vector<string>* samples;
};

#endif // CATEGORY_H
