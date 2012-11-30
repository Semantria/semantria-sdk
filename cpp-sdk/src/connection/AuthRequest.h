#ifndef AUTHREQUEST_H
#define AUTHREQUEST_H

#include "../common/Enums.h"

#include <string>
#include <vector>
#include <iostream>
#include <curl/curl.h>

using namespace std;

class AuthRequest {
public:
    AuthRequest(string consumerKey, string consumerSecret);
    AuthRequest(string consumerKey, string consumerSecret, string configId);
    AuthRequest(string consumerKey, string consumerSecret, string configId, string applicationName);
    virtual ~AuthRequest();

    int authWebRequest(string url, QueryMethod method);
    int authWebRequest(string url, QueryMethod method, string body);
    string GetReceivedData() {return outputBuffer;}

private:
    int httpRequest(string url, QueryMethod method, vector<string>& headers);
    struct curl_slist* getHeaders(vector<string> & headers);
    string char2hex(char dec);
    string urlEncode(const string &c);
    string getNormalizedParameters(string timestamp, string nonce);
    string intToStr(int tmp);
    string generateQuery(string url, string timestamp, string nonce);
    string generateAuthHeader(string query, string timestamp, string nonce);
    string getSHA1(string key, string url);
    void init(string consumerKey, string consumerSecret, string configId, string applicationName);
    bool endsWith(string const &fullString, string const &ending);
    static size_t writefunc(char *data, size_t size, size_t nmemb, string *outputBuffer);

private:
    string consumerKey;
    string consumerSecret;
    string outputBuffer;
    string inputBuffer;
    string applicationName;
    string configId;

    static const string oAuthVersion;
    static const string oAuthParameterPrefix;
    static const string oAuthConsumerKeyKey;
    static const string oAuthVersionKey;
    static const string oAuthSignatureMethodKey;
    static const string oAuthSignatureKey;
    static const string oAuthTimestampKey;
    static const string oAuthNonceKey;
    static const string configIdKey;
};

#endif // AUTHREQUEST_H
