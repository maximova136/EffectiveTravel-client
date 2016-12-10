package com.et.exception.manager;



public class WrongTransportType extends ManagerException {
    public WrongTransportType() {
        super();
    }

    public WrongTransportType(String message) {
        super(message);
    }

    public WrongTransportType(Throwable throwable) {
        super(throwable);
    }
}
