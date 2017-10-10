package com.semantria.auth;

public class CredentialException extends Exception {
    int status;

    public CredentialException(int status, String message) {
        super(message);
        this.status = status;
    }

    public CredentialException(int status, String message, Throwable throwable) {
        super(message, throwable);
        this.status = status;
    }
    public CredentialException(String message) {
        super(message);
    }

    public CredentialException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public int getStatus() {
        return status;
    }

}
