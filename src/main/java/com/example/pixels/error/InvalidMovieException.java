package com.example.pixels.error;

public class InvalidMovieException extends Exception{
    public InvalidMovieException() {
        super();
    }

    public InvalidMovieException(String message) {
        super(message);
    }

    public InvalidMovieException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidMovieException(Throwable cause) {
        super(cause);
    }

    protected InvalidMovieException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
