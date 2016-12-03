package com.et.exception.storage;




public class UpdateObjectFailed extends LocalStorageException {
    public UpdateObjectFailed() {
        super();
    }

    public UpdateObjectFailed(String message) {
        super(message);
    }

    public UpdateObjectFailed(Throwable throwable) {
        super(throwable);
    }
}
