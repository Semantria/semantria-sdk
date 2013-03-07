#include "RequestArgs.h"

RequestArgs::RequestArgs(std::string method, std::string url, std::string message) {
    this->method = method;
    this->url = url;
    this->message = message;
}

RequestArgs::~RequestArgs() {}
