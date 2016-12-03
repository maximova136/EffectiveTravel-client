package com.et.exception.api;


import com.et.exception.api.InsuccessfulResponseException;

public class SignupFailedException extends InsuccessfulResponseException {
    public SignupFailedException() {
        super();
    }

    public SignupFailedException(String message) {
        super(message);
    }

    public SignupFailedException(Throwable throwable) {
        super(throwable);
    }
}
