package sem4

import (
	"regexp"
	"testing"
)

func TestNonce(t *testing.T) {
	nonce := generateNonce(20)
	if len(nonce) != 20 {
		t.Errorf("Excpected nonce to be 20")
	}
	matched, err := regexp.MatchString(`^[0-9]+$`, nonce)
	if err != nil {
		t.Errorf("Bad regex")
	}
	if !matched {
		t.Errorf("Excpected nonce to be a string of digits")
	}
}

func TestGenerateAuthUrl(t *testing.T) {
	consumerKey := "consumer-key"
	nonce := "45095438949668051540"
	var timestamp int64
	timestamp = 1551274346
	requestURL := "https://api.semantria.com/status.json"
	verb := "GET"

	expectedAuthURL := "https://api.semantria.com/status.json?oauth_consumer_key=consumer-key&oauth_nonce=45095438949668051540&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1551274346&oauth_version=1.0"

	authURL, err := generateAuthURL(verb, requestURL, consumerKey, timestamp, nonce)
	if err != nil {
		t.Errorf("GenerateAuthURL failed %s", err)
	}
	if authURL != expectedAuthURL {
		t.Errorf("Bad auth url\nactual:   %s\nexpected: %s", authURL, expectedAuthURL)
	}
}

func TestGenerateAuthHeader(t *testing.T) {
	consumerKey := "consumer-key"
	consumerSecret := "consumer-secret"
	nonce := "45095438949668051540"
	var timestamp int64
	timestamp = 1551274346
	authURL := "https://api.semantria.com/status.json?oauth_consumer_key=consumer-key&oauth_nonce=45095438949668051540&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1551274346&oauth_version=1.0"
	authorization := generateAuthHeader(authURL, consumerKey, consumerSecret, timestamp, nonce)

	expectedAuthorization := `OAuth realm="",oauth_consumer_key="consumer-key",oauth_nonce="45095438949668051540",oauth_signature="NSwpaxPSOXNCS6spVRxBiDDN%2BHs%3D",oauth_signature_method="HMAC-SHA1",oauth_timestamp="1551274346",oauth_version="1.0"`
	if authorization != expectedAuthorization {
		t.Errorf("Bad authorization\nactual:   %s\nexpected: %s", authorization, expectedAuthorization)
	}
}

func TestEscape(t *testing.T) {
	url := "https://api.semantria.com/status.json?oauth_consumer_key=consumer-key&oauth_nonce=45095438949668051540&oauth_signature_method=HMAC-SHA1&oauth_timestamp=1551274346&oauth_version=1.0"
	expectedEscapedURL := `https%3A%2F%2Fapi.semantria.com%2Fstatus.json%3Foauth_consumer_key%3Dconsumer-key%26oauth_nonce%3D45095438949668051540%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1551274346%26oauth_version%3D1.0`
	escapedURL := escape(url)
	if escapedURL != expectedEscapedURL {
		t.Errorf("Bad escaped url\nactual:   %s\nexpected: %s", escapedURL, expectedEscapedURL)
	}
}

func TestGenerateMD5FromString(t *testing.T) {
	message := "consumer-secret"
	expectedMD5 := "c04af039f2f0b3ce260a84f46050db58"
	md5 := generateMD5FromString(message)
	if md5 != expectedMD5 {
		t.Errorf("Bad md5\nactual:   %s\nexpected: %s", md5, expectedMD5)
	}
}

func TestGetSHA1(t *testing.T) {
	escapedURL := `https%3A%2F%2Fapi.semantria.com%2Fstatus.json%3Foauth_consumer_key%3Dconsumer-key%26oauth_nonce%3D45095438949668051540%26oauth_signature_method%3DHMAC-SHA1%26oauth_timestamp%3D1551274346%26oauth_version%3D1.0`
	md5cs := `c04af039f2f0b3ce260a84f46050db58`
	sha1 := getSHA1(md5cs, escapedURL)
	expectedSha1 := `NSwpaxPSOXNCS6spVRxBiDDN+Hs=`
	if sha1 != expectedSha1 {
		t.Errorf("Bad sha1\nactual:   %s\nexpected: %s", sha1, expectedSha1)
	}
}
