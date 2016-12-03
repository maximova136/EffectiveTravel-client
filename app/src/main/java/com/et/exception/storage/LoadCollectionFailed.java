package com.et.exception.storage;



public class LoadCollectionFailed extends LocalStorageException {
    public LoadCollectionFailed() {
        super();
    }

    public LoadCollectionFailed(String message) {
        super(message);
    }

    public LoadCollectionFailed(Throwable throwable) {
        super(throwable);
    }
}
