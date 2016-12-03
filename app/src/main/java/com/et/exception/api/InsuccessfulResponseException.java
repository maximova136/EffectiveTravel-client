package com.et.exception.api;


import com.et.exception.api.ApiCallException;

public class InsuccessfulResponseException extends ApiCallException {
    public InsuccessfulResponseException() {
        super();
    }

    public InsuccessfulResponseException(String message) {
        super(message);
    }

    public InsuccessfulResponseException(Throwable throwable) {
        super(throwable);
    }
}
