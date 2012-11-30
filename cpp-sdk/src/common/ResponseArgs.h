#ifndef RESPONSEARGS_H
#define RESPONSEARGS_H

#include <string>

class ResponseArgs {
public:
    ResponseArgs(int status, std::string message);
    virtual ~ResponseArgs();

    int GetSatatus() {return status;}
    std::string GetMessage() {return message;}

    void SetStatus(int status) {this->status = status;}
    void SetMessage(std::string message) {this->message = message;}

private:
    int status;
    std::string message;
};

#endif // RESPONSEARGS_H
