package com.et.exception;




public class EffectiveTravelException extends Exception {
    public  EffectiveTravelException() {
        super();
    }

    public  EffectiveTravelException(String message) {
        super(message);
    }

    public  EffectiveTravelException(Throwable throwable) {
        super(throwable);
    }
}
