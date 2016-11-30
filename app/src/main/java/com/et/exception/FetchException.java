package com.et.exception;


public class FetchException extends EffectiveTravelException {
    public FetchException(Throwable throwable) {
        super(throwable);
    }

    public FetchException(String errorCode) {
        super("Fetch routes failed with code" + errorCode);
    }
}
