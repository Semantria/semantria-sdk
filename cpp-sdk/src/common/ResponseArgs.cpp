#include "ResponseArgs.h"

ResponseArgs::ResponseArgs(int status, std::string message) {
    this->status = status;
    this->message = message;
}

ResponseArgs::~ResponseArgs() {}
