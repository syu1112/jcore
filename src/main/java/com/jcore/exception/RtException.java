package com.jcore.exception;

public class RtException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public RtException() {
    }

    public RtException(String message) {
        super(message);
    }

    public RtException(String message, Throwable cause) {
        super(message, cause);
    }

    public RtException(Throwable cause) {
        super(cause);
    }
}
