#include <string>
#include "BasicTestApp.h"

int main() {
    std::string key = "";
    std::string secret = "";

    BasicTestApp testApp(key, secret);
    testApp.run();

    return 0;
}
