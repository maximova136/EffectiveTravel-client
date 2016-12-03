package com.et.exception.api;


import com.et.exception.api.InsuccessfulResponseException;

public class LoginFailedException extends InsuccessfulResponseException {
    public LoginFailedException() {
        super();
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(Throwable throwable) {
        super(throwable);
    }
}
