package com.et.exception.manager;

import com.et.exception.EffectiveTravelException;




public class ManagerException extends EffectiveTravelException {
    public ManagerException() {
        super();
    }

    public ManagerException(String message) {
        super(message);
    }

    public ManagerException(Throwable throwable) {
        super(throwable);
    }
}
