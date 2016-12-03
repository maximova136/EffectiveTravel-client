package com.et.exception.api;


import com.et.exception.EffectiveTravelException;

public class ApiCallException extends EffectiveTravelException {
    public ApiCallException() {
        super();
    }

    public ApiCallException(String message) {
        super(message);
    }

    public ApiCallException(Throwable throwable) {
        super(throwable);
    }
}

