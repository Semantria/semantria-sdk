#include "EnumsHelper.h"

EnumsHelper::EnumsHelper() {}
EnumsHelper::~EnumsHelper() {}

std::string EnumsHelper::GetQueryMethodAsString(QueryMethod method) {
    switch (method) {
        case GET:
            return "GET";
        case POST:
            return "POST";
        case DELETE:
            return "DELETE";
    }
}

std::string EnumsHelper::GetStatusAsString(TaskStatus status) {
    switch(status) {
        case UNDEFINED:
            return "UNDEFINED";
        case FAILED:
            return "FAILED";
        case QUEUED:
            return "QUEUED";
        case IN_SERVICE:
            return "IN_SERVICE";
        case PROCESSED:
            return "PROCESSED";
    }
}

TaskStatus EnumsHelper::GetStatusFromString(std::string status) {
    if (status == "UNDEFINED") {
        return UNDEFINED;
    } else if (status == "FAILED") {
        return FAILED;
    } else if (status == "QUEUED") {
        return QUEUED;
    } else if (status == "IN_SERVICE") {
        return IN_SERVICE;
    } else if (status == "PROCESSED") {
        return PROCESSED;
    }
}
