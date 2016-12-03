package com.et.exception.storage;




public class DeleteObjectFailed extends LocalStorageException {
    public DeleteObjectFailed() {
        super();
    }

    public DeleteObjectFailed(String message) {
        super(message);
    }

    public DeleteObjectFailed(Throwable throwable) {
        super(throwable);
    }
}
