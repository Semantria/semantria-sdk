#ifndef REQUESTARGS_H
#define REQUESTARGS_H

#include <string>

class RequestArgs {
public:
    RequestArgs(std::string method, std::string url, std::string message);
    virtual ~RequestArgs();

    std::string GetMethod() {return method;}
    std::string GetUrl() {return url;}
    std::string GetMessage() {return message;}

    void SetMethod(std::string method) {this->method = method;}
    void SetUrl(std::string url) {this->url = url;}
    void SetMessage(std::string message) {this->message = message;}

private:
    std::string method;
    std::string url;
    std::string message;
};

#endif // REQUESTARGS_H
