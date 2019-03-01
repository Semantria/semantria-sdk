package sem4

import (
	"encoding/json"
	"fmt"
	"net/url"
	"runtime"
	"strings"
)

// DisableLogging allows one to turn off all logging from the library
// If true there will be no calls to zerolog from the library
var DisableLogging bool

// Document represents a document to analyze
type Document struct {
	ID       string            `json:"id"`
	Text     string            `json:"text"`
	Metadata map[string]string `json:"metadata"`
}

// SessionOptions represents a semantria session
type SessionOptions struct {
	ConsumerKey        string
	ConsumerSecret     string
	ApplicationName    string
	DisableCompression bool
}

// Session is the Semantria session
type Session struct {
	consumerKey    string
	consumerSecret string
	apiVersion     string
	format         string
	host           string
	httpHeaders    map[string]string
	authRequest    AuthRequest
}

// SessionError represents an error
type SessionError struct {
	Message string
}

func (e *SessionError) Error() string {
	return fmt.Sprintf("SessionError: %s", e.Message)
}

// WrapperVersion is the detailed version number
const WrapperVersion = "4.2.93"

// DefaultAPIVersion is the version sent to Semantria
const DefaultAPIVersion = "4.2"

// DefaultAPIHost is the default endpoint
const DefaultAPIHost = "https://api.semantria.com"

// MakeSession instantiates a new Session
func MakeSession(options SessionOptions) (*Session, error) {
	if options.ConsumerKey == "" {
		return nil, &SessionError{"ConsumerKey is required"}
	}
	if options.ConsumerSecret == "" {
		return nil, &SessionError{"ConsumerSecret is required"}
	}

	session := Session{
		host: DefaultAPIHost,
	}
	defaultApplicationName := runtime.Version() + "/" + WrapperVersion
	var applicationName string
	if options.ApplicationName != "" {
		applicationName = fmt.Sprintf("%s/%s", defaultApplicationName, options.ApplicationName)
	} else {
		applicationName = defaultApplicationName
	}

	session.format = "json" // todo: add support for xml
	applicationName = fmt.Sprintf("%s/%s", applicationName, session.format)

	session.authRequest = AuthRequest{
		ConsumerKey:        options.ConsumerKey,
		ConsumerSecret:     options.ConsumerSecret,
		ApplicationName:    applicationName,
		DisableCompression: options.DisableCompression,
	}
	return &session, nil
}

// GetStatus returns the Semantria status
func (s *Session) GetStatus() (interface{}, error) {
	url := s.makeURL("/status", nil)
	resp, err := s.authRequest.AuthGet(url, &AuthRequestOptions{Headers: s.httpHeaders})
	if err != nil {
		return nil, err
	}
	return asJSON(resp, nil)
}

// GetConfigurations returns the Semantria status
func (s *Session) GetConfigurations() (interface{}, error) {
	url := s.makeURL("/configurations", nil)
	resp, err := s.authRequest.AuthGet(url, &AuthRequestOptions{Headers: s.httpHeaders})
	if err != nil {
		return nil, err
	}
	return asJSON(resp, nil)
}

// GetProcessedDocumentsByJobID gets documents waiting for the specified job id
func (s *Session) GetProcessedDocumentsByJobID(jobID string) ([]interface{}, error) {
	url := s.makeURL("/document/processed", &makeURLOptions{jobID: jobID})
	resp, err := s.authRequest.AuthGet(url, &AuthRequestOptions{Headers: s.httpHeaders})
	if err != nil {
		return nil, err
	}
	return asJSONArray(resp)
}

// GetProcessedDocumentsByConfigID gets documents waiting for the specified job id
func (s *Session) GetProcessedDocumentsByConfigID(configID string) ([]interface{}, error) {
	url := s.makeURL("/document/processed", &makeURLOptions{configID: configID})
	resp, err := s.authRequest.AuthGet(url, &AuthRequestOptions{Headers: s.httpHeaders})
	if err != nil {
		return nil, err
	}
	return asJSONArray(resp)
}

// QueueBatch sends a batch of documents to Semantria
// jobID is optional
func (s *Session) QueueBatch(batch []Document, configID string, jobID string) (interface{}, error) {
	url := s.makeURL("/document/batch", &makeURLOptions{configID: configID, jobID: jobID})
	resp, err := s.authRequest.AuthPost(url, &AuthRequestOptions{Headers: s.httpHeaders, JSON: batch})
	if err != nil {
		return nil, err
	}
	return asJSON(resp, nil)
}

func asJSON(str string, data interface{}) (interface{}, error) {
	if str == "" {
		return nil, nil
	}
	if data == nil {
		var i interface{}
		data = i
	}
	err := json.Unmarshal([]byte(str), &data)
	if err != nil {
		return nil, err
	}
	return data, nil
}

func asJSONArray(str string) ([]interface{}, error) {
	if str == "" {
		return nil, nil
	}
	data := make([]interface{}, 0)
	err := json.Unmarshal([]byte(str), &data)
	if err != nil {
		return nil, err
	}
	return data, nil
}

type makeURLOptions struct {
	configID string
	jobID    string
	params   map[string]string
	format   string
}

func (s *Session) makeURL(path string, options *makeURLOptions) string {
	if options == nil {
		options = &makeURLOptions{}
	}

	if !strings.HasPrefix(path, "/") {
		path = "/" + path
	}
	format := s.format
	if options.format != "" {
		format = options.format
	}
	theURL := s.host + path + "." + format
	paramString := ""
	if len(options.params) > 0 {
		for key, value := range options.params {
			if len(paramString) > 0 {
				paramString += "&"
			}
			paramString += fmt.Sprintf("%s=%s", url.QueryEscape(key), url.QueryEscape(value))
		}
	}
	if options.configID != "" {
		if len(paramString) > 0 {
			paramString += "&"
		}
		paramString += fmt.Sprintf("config_id=%s", url.QueryEscape(options.configID))
	}
	if options.jobID != "" {
		if len(paramString) > 0 {
			paramString += "&"
		}
		paramString += fmt.Sprintf("job_id=%s", url.QueryEscape(options.jobID))
	}
	if paramString != "" {
		theURL += "?" + paramString
	}
	return theURL
}

// ApplicationName returns the application name
func (s *Session) ApplicationName() string {
	return s.authRequest.ApplicationName
}

// Host is the api endpoint being used
func (s *Session) Host() string {
	return s.host
}

// SetHost sets a new api endpoint to be used for subsequent requests
func (s *Session) SetHost(host string) {
	if strings.HasSuffix(host, "/") {
		host = host[:len(host)-1]
	}
	s.host = host
}

// AddHTTPHeader adds a custom http header to use with requests
func (s *Session) AddHTTPHeader(name string, value string) {
	s.httpHeaders[name] = value
}
