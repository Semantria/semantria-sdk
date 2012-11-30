#pragma once

#ifndef UPDATEPROXY_H_
#define UPDATEPROXY_H

#include <vector>
#include "../serializers/json/JsonSerializable.h"
#include "../serializers/xml/XmlSerializable.h"

using namespace std;

template <class T>
class UpdateProxy: virtual public JsonSerializable, virtual public XmlSerializable {
public:
    UpdateProxy() {
        added = new vector<T*>();
        removed = new vector<string>();
    }

    virtual ~UpdateProxy() {
        if (NULL != added) {
            delete added;
        }

        if (NULL != removed) {
            delete removed;
        }
    }

	virtual void Add(T* obj) { added->push_back(obj); }
	virtual void Remove(T* obj) {}
	virtual void Update(T* obj) { added->push_back(obj); }
	virtual void Clone(T* obj) {}
	void RemoveAll() {
        added->clear();
        removed->clear();
        removed->push_back("*");
	}


	void Serialize(Json::Value& root) {
        for(int i = 0; i != added->size(); i++) {
            Json::Value addedElement;
            added->at(i)->Serialize(addedElement);
            root["added"].append(addedElement);
        }

        for(vector<string>::size_type i = 0; i != removed->size(); i++) {
            Json::Value removedElement(removed->at(i));
            root["removed"].append(removedElement);
        }
	}

    void Deserialize(Json::Value& root) {}
    void Serialize(xmlNodePtr node) {}
    void Deserialize(std::string source) {}
    string GetRootElement() {return "";}

    vector<T*>* GetAdded() {return added;}
    vector<string>* GetRemoved() {return removed;}

protected:
    vector<T*>* added;
    vector<string>* removed;
};

#endif /* UPDATEPROXY_H_ */
