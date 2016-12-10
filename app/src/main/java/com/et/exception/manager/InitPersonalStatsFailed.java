package com.et.exception.manager;


import com.et.exception.EffectiveTravelException;

public class InitPersonalStatsFailed extends ManagerException {
    public InitPersonalStatsFailed() {
        super();
    }

    public InitPersonalStatsFailed(String message) {
        super(message);
    }

    public InitPersonalStatsFailed(Throwable throwable) {
        super(throwable);
    }
}
