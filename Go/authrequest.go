package sem4

import (
	"crypto/hmac"
	"crypto/md5"
	"crypto/sha1"
	"encoding/base64"
	"encoding/hex"
	"errors"
	"fmt"
	"io"
	"math/rand"
	"net/url"
	"sort"
	"strings"
	"time"

	"github.com/levigross/grequests"
	"github.com/rs/zerolog/log"
)

const (
	authorizationHeader     = "Authorization"
	oAuthVersion            = "1.0"
	oAuthConsumerKeyKey     = "oauth_consumer_key"
	oAuthVersionKey         = "oauth_version"
	oAuthSignatureMethodKey = "oauth_signature_method"
	oAuthSignatureKey       = "oauth_signature"
	oAuthTimestampKey       = "oauth_timestamp"
	oAuthNonceKey           = "oauth_nonce"
)

// AuthRequest represents an authenticated request to Semantria
type AuthRequest struct {
	ConsumerKey        string
	ConsumerSecret     string
	ApplicationName    string
	APIVersion         string
	DisableCompression bool
}

// AuthRequestOptions represents the options to a particular authenticated request
type AuthRequestOptions struct {
	// JSON can be used for sending data
	JSON interface{}

	// Headers if you want to add additional headers to the request
	Headers map[string]string
}

// DoAuthRequest makes an authenticated request
func (ar AuthRequest) DoAuthRequest(verb string, url string, options *AuthRequestOptions) (string, error) {
	if options == nil {
		options = &AuthRequestOptions{}
	}

	if !DisableLogging {
        log.Debug().Msgf("%s %s", verb, url)
	}

	nonce := generateNonce(20)
	timestamp := ar.generateTimestamp()
	requestURL, err := generateAuthURL(verb, url, ar.ConsumerKey, timestamp, nonce)
	if err != nil {
		return "", err
	}
	authorization := generateAuthHeader(requestURL, ar.ConsumerKey, ar.ConsumerSecret, timestamp, nonce)
	authHeaders := ar.generateHeaders(verb, authorization, options.Headers)

	goptions := &grequests.RequestOptions{
		Headers:            authHeaders,
		DisableCompression: ar.DisableCompression,
	}
	if options.JSON != nil {
		goptions.JSON = options.JSON
	}

	resp, err := grequests.Req(verb, requestURL, goptions)
	if err != nil {
		return "", nil
	}
	if !resp.Ok {
		return "", errors.New("Request failed: " + resp.String())
	}
	if resp.StatusCode == 202 {
		return "", nil
	}
	return resp.String(), nil
}

// AuthGet makes an authenticate GET request
func (ar AuthRequest) AuthGet(url string, options *AuthRequestOptions) (string, error) {
	return ar.DoAuthRequest("GET", url, options)
}

// AuthPost makes an authenticate GET request
func (ar AuthRequest) AuthPost(url string, options *AuthRequestOptions) (string, error) {
	return ar.DoAuthRequest("POST", url, options)
}

// generateNonce generates the random nonce
func generateNonce(length int) string {
	r := rand.New(rand.NewSource(time.Now().Unix()))
	s := ""
	for n := 0; n < length; n++ {
		s += fmt.Sprintf("%d", r.Intn(10))
	}
	return s
}

func (ar AuthRequest) generateTimestamp() int64 {
	return time.Now().Unix()
}

// generateAuthURL adds the oath magic got the url
func generateAuthURL(verb string, requestURL string, consumerKey string, timestamp int64, nonce string) (string, error) {
	parts, err := url.Parse(requestURL)
	if err != nil {
		return "", err
	}
	np := getNormalizedParameters(consumerKey, timestamp, nonce)

	authURL := requestURL
	if len(parts.RawQuery) > 0 {
		authURL += "&oauth_verifier=" + url.QueryEscape(np)
	} else {
		authURL += "?" + np
	}
	return authURL, nil
}

// getSHA1 calculates the hmac digest
func getSHA1(key string, message string) string {
	mac := hmac.New(sha1.New, []byte(key))
	mac.Write([]byte(message))
	sha1res := mac.Sum(nil)
	sha1sig := base64.StdEncoding.EncodeToString(sha1res)
	return sha1sig
}

// generateMD5FromString creates an MD5 digest from the given message
func generateMD5FromString(message string) string {
	h := md5.New()
	io.WriteString(h, message)
	return hex.EncodeToString(h.Sum(nil))
}

// escape escapes the url properly
func escape(theURL string) string {
	return url.QueryEscape(theURL)
}

// generateAuthHeader creates the oath authorization value
func generateAuthHeader(requestURL string, consumerKey string, consumerSecret string, timestamp int64, nonce string) string {
	md5cs := generateMD5FromString(consumerSecret)
	escapedURL := escape(requestURL)
	hash := escape(getSHA1(md5cs, escapedURL))

	pairs := []pair{
		{"OAuth realm", ""},
		{oAuthVersionKey, oAuthVersion},
		{oAuthTimestampKey, fmt.Sprintf("%d", timestamp)},
		{oAuthNonceKey, nonce},
		{oAuthSignatureMethodKey, "HMAC-SHA1"},
		{oAuthConsumerKeyKey, consumerKey},
		{oAuthSignatureKey, hash},
	}
	sort.Sort(byKey(pairs))

	var builder strings.Builder
	for _, pair := range pairs {
		if builder.Len() > 0 {
			builder.WriteString(",")
		}
		builder.WriteString(pair.Key)
		builder.WriteString(`="`)
		builder.WriteString(pair.Value)
		builder.WriteString(`"`)
	}
	return builder.String()
}

// pair is a pair
type pair struct {
	Key   string
	Value string
}

// byKey implements sorting by the key
type byKey []pair

// Len is the length of A
func (a byKey) Len() int {
	return len(a)
}

// Less tells if i is less than j
func (a byKey) Less(i, j int) bool {
	return a[i].Key < a[j].Key
}

// Swap swaps i for j
func (a byKey) Swap(i, j int) {
	a[i], a[j] = a[j], a[i]
}

// getNormalizedParameters creates the oath parameters string
func getNormalizedParameters(consumerKey string, timestamp int64, nonce string) string {
	pairs := []pair{
		{oAuthVersionKey, oAuthVersion},
		{oAuthTimestampKey, fmt.Sprintf("%d", timestamp)},
		{oAuthNonceKey, nonce},
		{oAuthSignatureMethodKey, "HMAC-SHA1"},
		{oAuthConsumerKeyKey, consumerKey},
	}
	sort.Sort(byKey(pairs))

	encodedStr := ""
	for _, pair := range pairs {
		if len(encodedStr) > 0 {
			encodedStr += "&"
		}
		encodedStr += url.QueryEscape(pair.Key)
		encodedStr += "="
		encodedStr += url.QueryEscape(fmt.Sprintf("%s", pair.Value))
	}
	// Encode signature parameters per Oauth Core 1.0 protocol
	// Spaces must be encoded with "%20" instead of "+"
	encodedStr = strings.ReplaceAll(encodedStr, "+", "%20")
	encodedStr = strings.ReplaceAll(encodedStr, "%7E", "~")

	return encodedStr
}

func (ar AuthRequest) generateHeaders(verb string, authorization string, customHeaders map[string]string) map[string]string {
	headers := make(map[string]string)
	if customHeaders != nil {
		for key, value := range customHeaders {
			headers[key] = value
		}
	}
	headers[authorizationHeader] = authorization
	if ar.APIVersion != "" {
		headers["x-api-version"] = ar.APIVersion
	} else {
		headers["x-api-version"] = DefaultAPIVersion
	}
	if ar.ApplicationName != "" {
		headers["x-app-name"] = ar.ApplicationName
	}
	return headers
}
