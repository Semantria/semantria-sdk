#include <map>
#include <time.h>
#include <stdlib.h>
#include <sstream>

#include "AuthRequest.h"
#include "Md5.h"
#include "HMAC_SHA1.h"
#include "base64.h"

AuthRequest::AuthRequest(string consumerKey, string consumerSecret) {
    init(consumerKey, consumerSecret, (string)"", (string)"");
}

AuthRequest::AuthRequest(string consumerKey, string consumerSecret, string configId) {
    init(consumerKey, consumerSecret, configId, (string)"");
}

AuthRequest::AuthRequest(string consumerKey, string consumerSecret, string configId, string applicationName) {
    init(consumerKey, consumerSecret, configId, applicationName);
}

void AuthRequest::init(string consumerKey, string consumerSecret, string configId, string applicationName) {
    this->consumerKey = consumerKey;
    this->consumerSecret = consumerSecret;
    this->configId = configId;
    this->applicationName = applicationName;
}

AuthRequest::~AuthRequest() {}

string AuthRequest::intToStr(int tmp) {
    std::stringstream stream;
    stream << tmp;
    return stream.str();
}

int AuthRequest::authWebRequest(string url, QueryMethod method) {
    return authWebRequest(url, method, "");
}

int AuthRequest::authWebRequest(string url, QueryMethod method, string body) {
    inputBuffer = body;

    srand(time(NULL));
    string nonce = intToStr(rand() % 100000000 + 10000);
    string timestamp = intToStr(time(NULL));

    string query = generateQuery(url, timestamp, nonce);
    string authHeader = generateAuthHeader(query, timestamp, nonce);

    vector<string> headers;
    headers.push_back("Authorization: " + authHeader);
    headers.push_back("x-api-version: 2");
    if ("" != applicationName) {
        headers.push_back("x-app-name: " + applicationName);
    }

    if (method == POST && 0 != inputBuffer.length()) {
        //cout << "sending (" << inputBuffer.length() << " characters)..." << inputBuffer << endl;
        headers.push_back("Content-Type: application/x-www-form-urlencoded; charset=UTF-8");
        headers.push_back("Content-Length: " + inputBuffer.length());
    }

    return httpRequest(query, method, headers);
}

string AuthRequest::generateQuery(string url, string timestamp, string nonce) {
    if (endsWith(url, "xml") || endsWith(url, "json")) {
        url += "?";
    } else {
        url += "&";
    }

    url += getNormalizedParameters(timestamp, nonce);
    return url;
}

string AuthRequest::getSHA1(string key, string url) {
    vector<char> keyBytes(key.begin(), key.end());
    keyBytes.push_back('\0');
    const char *keySecretPointer = &keyBytes[0];

    BYTE digest[20];

    vector<char> bytes(url.begin(), url.end());
    bytes.push_back('\0');
    const char *c = &bytes[0];

    CHMAC_SHA1* sha = new CHMAC_SHA1();
    sha->HMAC_SHA1((BYTE*)c, strlen(c), (BYTE*)keySecretPointer, strlen(keySecretPointer), digest);

    return urlEncode(base64_encode(digest, sizeof(digest)));
}

string AuthRequest::generateAuthHeader(string query, string timestamp, string nonce) {
    string hash = getSHA1(md5(this->consumerSecret), urlEncode(query));

    map<string, string> headers;
    headers.insert(pair<string,string>(oAuthVersionKey, oAuthVersion));
    headers.insert(pair<string,string>(oAuthTimestampKey, timestamp));
    headers.insert(pair<string,string>(oAuthNonceKey, nonce));
    headers.insert(pair<string,string>(oAuthSignatureMethodKey, "HMAC-SHA1"));
    headers.insert(pair<string,string>(oAuthConsumerKeyKey, consumerKey));
    headers.insert(pair<string,string>(oAuthSignatureKey, hash));

    string result = "OAuth,";

    for (map<string, string>::iterator iter = headers.begin(); iter != headers.end(); ++iter) {
        result.append(iter->first);
        result.append("=");
        result.append("\"" + iter->second + "\"");
        result.append(",");
    }

    return result.substr(0, result.size()-1);
}

bool AuthRequest::endsWith(string const &fullString, string const &ending) {
    if (fullString.length() >= ending.length()) {
        return (0 == fullString.compare (fullString.length() - ending.length(), ending.length(), ending));
    } else {
        return false;
    }
}

int AuthRequest::httpRequest(string url, QueryMethod method, vector<string> & headers) {
    CURL* handler = curl_easy_init();
    if (handler) {
        curl_easy_setopt(handler, CURLOPT_URL, url.c_str());
        curl_easy_setopt(handler, CURLOPT_VERBOSE, 0L);
        curl_easy_setopt(handler, CURLOPT_TIMEOUT, 30);
        curl_easy_setopt(handler, CURLOPT_SSL_VERIFYPEER, 0L);

        switch(method) {
        case POST:
            curl_easy_setopt(handler, CURLOPT_POSTFIELDS, inputBuffer.c_str());
            curl_easy_setopt(handler, CURLOPT_POSTFIELDSIZE, inputBuffer.length());
            curl_easy_setopt(handler, CURLOPT_POST, 1L);
            //NOTE: no break - we may receive some information in response
        case GET:
            curl_easy_setopt(handler, CURLOPT_WRITEFUNCTION, writefunc);
            curl_easy_setopt(handler, CURLOPT_WRITEDATA, &outputBuffer);
            break;
        case DELETE:
            curl_easy_setopt(handler, CURLOPT_CUSTOMREQUEST, "DELETE");
            break;
        }

        curl_easy_setopt(handler, CURLOPT_HTTPHEADER, getHeaders(headers));

        CURLcode result = curl_easy_perform(handler);
        long status;
        curl_easy_getinfo(handler, CURLINFO_RESPONSE_CODE, &status);
        curl_easy_cleanup(handler);

        cout << endl;
        cout << "Status => " << status;
        cout << ". Result => " << result << " ("<< curl_easy_strerror(result) << ") " << endl;

        return (int)(status);
    } else {
        return -1;
    }
}

struct curl_slist* AuthRequest::getHeaders(vector<string> & headers) {
    struct curl_slist* result = NULL;

    for(vector<string>::size_type i = 0; i != headers.size(); i++) {
        result = curl_slist_append(result, headers.at(i).c_str());
    }

    return result;
}

string AuthRequest::getNormalizedParameters(string timestamp, string nonce) {
    map<string, string> headers;
    headers.insert(pair<string,string>(oAuthVersionKey, oAuthVersion));
    headers.insert(pair<string,string>(oAuthTimestampKey, timestamp));
    headers.insert(pair<string,string>(oAuthNonceKey, nonce));
    headers.insert(pair<string,string>(oAuthSignatureMethodKey, "HMAC-SHA1"));
    headers.insert(pair<string,string>(oAuthConsumerKeyKey, consumerKey));

    if ("" != configId) {
        headers.insert(pair<string,string>(configIdKey, configId));
    }

    string result;
    for (map<string, string>::iterator iter = headers.begin(); iter != headers.end(); ++iter) {
        result.append(iter->first);
        result.append("=");
        result.append(iter->second);
        result.append("&");
    }

    return result.substr(0, result.size()-1);
}

string AuthRequest::char2hex( char dec ) {
    char dig1 = (dec&0xF0)>>4;
    char dig2 = (dec&0x0F);
    if ( 0<= dig1 && dig1<= 9) dig1+=48;    //0,48inascii
    if (10<= dig1 && dig1<=15) dig1+=97-10; //a,97inascii
    if ( 0<= dig2 && dig2<= 9) dig2+=48;
    if (10<= dig2 && dig2<=15) dig2+=97-10;

    string r;
    dig1 = (char)toupper(dig1);
    dig2 = (char)toupper(dig2);
    r.append( &dig1, 1);
    r.append( &dig2, 1);
    return r;
}

//based on javascript encodeURIComponent()
string AuthRequest::urlEncode(const string &c) {
    string escaped="";
    int max = c.length();
    for(int i=0; i<max; i++) {
        if ( (48 <= c[i] && c[i] <= 57) ||//0-9
                (65 <= c[i] && c[i] <= 90) ||//abc...xyz
                (97 <= c[i] && c[i] <= 122) || //ABC...XYZ
                (c[i]=='~' || c[i]=='!' || c[i]=='*' || c[i]=='('
                 || c[i]==')' || c[i]=='\'' || c[i]=='.' || c[i]=='_'
                 || c[i]=='-')
           ) {
            escaped.append( &c[i], 1);
        } else {
            escaped.append("%");
            escaped.append( char2hex(c[i]) );//converts char 255 to string "ff"
        }
    }
    return escaped;
}

size_t AuthRequest::writefunc(char *data, size_t size, size_t nmemb, string *outputBuffer) {
    if (outputBuffer != NULL) {
        outputBuffer->append(data, size * nmemb);
    }
    return (size * nmemb);
}

const string AuthRequest::oAuthVersion = "1.0";
const string AuthRequest::oAuthParameterPrefix = "oauth_";
const string AuthRequest::oAuthConsumerKeyKey = "oauth_consumer_key";
const string AuthRequest::oAuthVersionKey = "oauth_version";
const string AuthRequest::oAuthSignatureMethodKey = "oauth_signature_method";
const string AuthRequest::oAuthSignatureKey = "oauth_signature";
const string AuthRequest::oAuthTimestampKey = "oauth_timestamp";
const string AuthRequest::oAuthNonceKey = "oauth_nonce";
const string AuthRequest::configIdKey = "config_id";
