package com.et.exception;


public class FetchRoutesException extends EffectiveTravelException {
    public FetchRoutesException(Throwable throwable) {
        super(throwable);
    }

    public FetchRoutesException(String errorCode) {
        super("Fetch routes failed with code" + errorCode);
    }
}
