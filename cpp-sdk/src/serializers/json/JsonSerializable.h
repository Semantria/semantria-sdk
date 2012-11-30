#ifndef JSONSERIALIZABLE_H_
#define JSONSERIALIZABLE_H_

#include "jsoncpp/json.h"

class JsonSerializable {
public:
	virtual ~JsonSerializable(){}
	virtual void Serialize( Json::Value& root ){}
    virtual void Deserialize( Json::Value& root){}
};

#endif /* JSONSERIALIZABLE_H_ */
