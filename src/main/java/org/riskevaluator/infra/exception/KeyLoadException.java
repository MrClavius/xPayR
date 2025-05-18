package org.riskevaluator.infra.exception;

public class KeyLoadException extends Exception {
    public KeyLoadException(String message) {
        super(message);
    }

    public KeyLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}