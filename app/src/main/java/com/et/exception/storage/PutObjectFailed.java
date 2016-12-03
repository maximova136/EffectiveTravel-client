package com.et.exception.storage;




public class PutObjectFailed extends LocalStorageException {
    public PutObjectFailed() {
        super();
    }

    public PutObjectFailed(String message) {
        super(message);
    }

    public PutObjectFailed(Throwable throwable) {
        super(throwable);
    }
}
