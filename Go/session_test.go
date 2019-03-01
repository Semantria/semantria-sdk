package sem4

import (
	"runtime"
	"testing"
)

func TestMissingConsumerKey(t *testing.T) {
	_, err := MakeSession(SessionOptions{
		ConsumerSecret: "consumer-secret",
	})
	if err == nil {
		t.Errorf("Excpected an error about missing consumer key")
	}
}

func TestMissingConsumerSecret(t *testing.T) {
	_, err := MakeSession(SessionOptions{
		ConsumerKey: "consumer-key",
	})
	if err == nil {
		t.Errorf("Excpected an error about missing consumer secret")
	}
}

func TestDefaultApplicationName(t *testing.T) {
	session, err := MakeSession(SessionOptions{
		ConsumerKey:    "consumer-key",
		ConsumerSecret: "consumer-secret",
	})
	if err != nil {
		t.Errorf("Unable to create session %s", err)
	}
	if session.ApplicationName() != (runtime.Version() + "/4.2.93/json") {
		t.Errorf("Unexpected application name: %s", session.ApplicationName())
	}
}

func TestCustomApplicationName(t *testing.T) {
	session, err := MakeSession(SessionOptions{
		ConsumerKey:    "consumer-key",
        ConsumerSecret: "consumer-secret",
        ApplicationName: "foo",
	})
	if err != nil {
		t.Errorf("Unable to create session %s", err)
	}
	if session.ApplicationName() != (runtime.Version() + "/4.2.93/foo/json") {
		t.Errorf("Unexpected application name: %s", session.ApplicationName())
	}
}

func TestCustomHosts(t *testing.T) {
	session, err := MakeSession(SessionOptions{
		ConsumerKey:    "consumer-key",
        ConsumerSecret: "consumer-secret",
	})
	if err != nil {
		t.Errorf("Unable to create session %s", err)
	}
    if session.Host() != DefaultAPIHost {
        t.Errorf("Was expecting the DefaultAPIHost")
    }
    session.SetHost("http://foo.bar/")
    if session.Host() != "http://foo.bar" {
        t.Errorf("Was expecting the custom api host")
    }
}
