#ifndef ENUMSHELPER_H
#define ENUMSHELPER_H

#include <string>

#include "Enums.h"

class EnumsHelper {
public:
    virtual ~EnumsHelper();
    static std::string GetQueryMethodAsString(QueryMethod method);
    static TaskStatus GetStatusFromString(std::string status);
    static std::string GetStatusAsString(TaskStatus status);

private:
    EnumsHelper();
};

#endif // ENUMSHELPER_H
