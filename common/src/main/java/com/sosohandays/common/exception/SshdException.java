package com.sosohandays.common.exception;

public class SshdException extends RuntimeException {
    public SshdException() {
        super();
    }

    public SshdException(String message) {
        super(message);
    }

    public SshdException(String message, Throwable cause) {
        super(message, cause);
    }

    public SshdException(Throwable cause) {
        super(cause);
    }
}