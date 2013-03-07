#include <string>
#include "TestApp.h"

int main() {
    std::string key = "";
    std::string secret = "";
    
    TestApp testApp(key, secret);
    testApp.run();

    return 0;
}
