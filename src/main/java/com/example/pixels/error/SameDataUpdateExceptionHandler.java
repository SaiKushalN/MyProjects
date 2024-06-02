package com.example.pixels.error;

public class SameDataUpdateExceptionHandler extends Exception{
    public SameDataUpdateExceptionHandler() {
        super();
    }

    public SameDataUpdateExceptionHandler(String message) {
        super(message);
    }

    public SameDataUpdateExceptionHandler(String message, Throwable cause) {
        super(message, cause);
    }

    public SameDataUpdateExceptionHandler(Throwable cause) {
        super(cause);
    }

    protected SameDataUpdateExceptionHandler(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
