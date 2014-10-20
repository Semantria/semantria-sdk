#include <string>
#include "DiscoveryTestApp.h"

int main() {
    std::string key = "";
    std::string secret = "";

    DiscoveryTestApp testApp(key, secret);
    testApp.run();

    return 0;
}
