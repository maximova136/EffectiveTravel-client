package com.et.exception.api;


import com.et.exception.api.ApiCallException;

public class RequestFailedException extends ApiCallException {
    public RequestFailedException() {
        super();
    }

    public RequestFailedException(String message) {
        super(message);
    }

    public RequestFailedException(Throwable throwable) {
        super(throwable);
    }
}
