#ifndef SERIALIZER_H_
#define SERIALIZER_H_

#include <string>

using namespace std;

template <class T>
class Serializer {
public:
    virtual string GetType() = 0;
	virtual string Serialize(T* obj) = 0;
	virtual void Deserialize(string source, T* obj) = 0;
	virtual ~Serializer(){}
};

#endif /* SERIALIZER_H_ */
