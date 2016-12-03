package com.et.exception.storage;

import com.et.exception.EffectiveTravelException;



public class LocalStorageException extends EffectiveTravelException {
    public LocalStorageException() {
        super();
    }

    public LocalStorageException(String message) {
        super(message);
    }

    public LocalStorageException(Throwable throwable) {
        super(throwable);
    }
}
